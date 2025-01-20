package com.example.pokedex.controllers;

import com.example.pokedex.models.PokemonData;
import com.example.pokedex.services.AbstractPokemonProviderService;
import com.example.pokedex.views.PokemonView;

public class PokemonController {
    // Service to retrieve Pokemon data from an API.
    private final AbstractPokemonProviderService apiPokemonService;
    // Service to retrieve Pokemon data from the local database.
    private final AbstractPokemonProviderService databasePokemonService;
    // View that handles displaying Pokemon data to the user.
    private final PokemonView outputView;

    // Constructor initializes the controller with services and the output view.
    public PokemonController(
            AbstractPokemonProviderService apiPokemonService,
            AbstractPokemonProviderService databasePokemonService,
            PokemonView outputView) {
        this.apiPokemonService = apiPokemonService;
        this.databasePokemonService = databasePokemonService;
        this.outputView = outputView;
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
        outputView.displayPokemonData(pokemonData); // The format is handled by PokemonView.
    }
}
