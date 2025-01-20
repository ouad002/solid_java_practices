package com.example.pokedex;

import com.example.pokedex.controllers.PokemonController;
import com.example.pokedex.models.PokemonData;
import com.example.pokedex.services.PokemonHttpProvider;
import com.example.pokedex.services.PokemonSqliteProvider;
import com.example.pokedex.utilities.AbstractPokedexRunner;
import com.example.pokedex.utilities.OutputFormat;
import com.example.pokedex.views.PokemonView;

public class PokedexRunner extends AbstractPokedexRunner {

    private PokemonHttpProvider myPokemonService = new PokemonHttpProvider();
    private PokemonSqliteProvider mySqliteService = new PokemonSqliteProvider();
    private PokemonController pokemonController;
    private PokemonView pokemonView;
    private DataSource currentDataSource;

    @Override
    public void onOptionsChange(DataSource dataSource, String dbPath) throws Exception {
        setupServiceLocale(myPokemonService);
        pokemonView = new PokemonView(OutputFormat.TEXT, this.locale);
        currentDataSource = dataSource;

        if (dataSource == DataSource.POKEAPI) {
            pokemonController = new PokemonController(myPokemonService, mySqliteService, pokemonView);
        } else {
            pokemonController = new PokemonController(mySqliteService, mySqliteService, pokemonView);
        }
    }

    @Override
    public void runPokedex(Integer pokemonId) throws Exception {
        // Fetch and display Pokemon data
        if (currentDataSource == DataSource.POKEAPI) {
            var pokemonData = myPokemonService.getPokemonData(pokemonId, null);
            if (pokemonData != null) {
                displayPokemonData(pokemonData);
            } else {
                System.out.println("No data found for the given Pokemon ID.");
            }
        } else {
            pokemonController.fetchAndDisplayPokemonData(pokemonId, this.databasePath);
        }
    }

    private void displayPokemonData(PokemonData pokemonData) {
        if ("fr".equals(this.locale)) {
            System.out.printf("""
                =============================
                Pokémon # %d
                Nom : %s
                Description : %s
                Taille : %d
                Poids : %d
                =============================
                """, pokemonData.getId(), pokemonData.getName(), pokemonData.getDescription(), pokemonData.getHeight(), pokemonData.getWeight());
        } else {
            System.out.printf("""
                =============================
                Pokemon # %d
                Name: %s
                Description: %s
                Height: %d
                Weight: %d
                =============================
                """, pokemonData.getId(), pokemonData.getName(), pokemonData.getDescription(), pokemonData.getHeight(), pokemonData.getWeight());
        }
    }
}