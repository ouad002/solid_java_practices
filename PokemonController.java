package com.example.pokedex.controllers;
import com.example.pokedex.models.PokemonData;
import com.example.pokedex.services.PokemonService;
import com.example.pokedex.utils.OutputFormat;
import com.example.pokedex.views.ConsoleOutputView;


public class PokemonController {
    // Service to retrieve Pokemon data from an API.
    private final PokemonService apiPokemonService;

    // Service to retrieve Pokemon data from the local database.
    private final PokemonService databasePokemonService;

    // View that handles displaying Pokemon data to the user.
    private final ConsoleOutputView outputView;

    // Specifies the desired format for outputting Pokemon data.
    private OutputFormat outputFormat;

    // Initializes the controller with the required services and view for operation.
    public PokemonController(
            PokemonService apiPokemonService,
            PokemonService databasePokemonService,
            ConsoleOutputView outputView,
            OutputFormat outputFormat
    ) {
        this.apiPokemonService = apiPokemonService;
        this.databasePokemonService = databasePokemonService;
        this.outputView = outputView;
        this.outputFormat = outputFormat;
    }

    // Retrieves and displays Pokemon data based on ID and database location.
    public void fetchAndDisplayPokemonData(int id, String databasePath) {
        // Retrieves Pokemon data from the database as the primary source.
        PokemonData pokemonData = databasePokemonService.getPokemonData(id, databasePath);

        // If no data is found locally, fetch data from the external API as a fallback.
        if (pokemonData == null) {
            pokemonData = apiPokemonService.getPokemonData(id, null);
        }

        // Sends the retrieved Pokemon data to the view for presentation to the user.
        outputView.displayPokemonData(pokemonData);
    }
}
