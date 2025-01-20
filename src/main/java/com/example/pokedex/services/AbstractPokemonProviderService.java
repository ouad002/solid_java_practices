package com.example.pokedex.services;

import com.example.pokedex.models.PokemonData;

/**
 * Interface that defines the method to retrieve Pokémon data using an ID and
 * optional database path.
 */
public interface AbstractPokemonProviderService {

    /**
     * Method declaration for fetching Pokémon details by ID, optionally using a
     * database path.
     */
    PokemonData getPokemonData(int id, String databasePath);
}
