package com.example.pokedex.views;

import com.example.pokedex.models.PokemonData;

/**
 * Defines methods for generating Pokémon data in multiple formats like HTML,
 * CSV, and text.
 */
public interface MultipleFormatGenerator {

    /** Creates an HTML representation of the given Pokémon data. */
    String generateHTML(PokemonData pokemonData);

    /** Creates a CSV representation of the given Pokémon data. */
    String generateCSV(PokemonData pokemonData);

    /** Creates a readable text format of the given Pokémon data. */
    String generateHumanReadableText(PokemonData pokemonData);
}
