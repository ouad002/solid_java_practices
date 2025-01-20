package com.example.pokedex.models;

/**
 * Represents a Pokémon with additional details like description, extending the
 * base Pokémon model.
 */
public class PokemonData extends Pokemon {
    /** A text description providing more information about the Pokémon. */
    private final String description;

    /** Initializes a Pokémon with its ID, name, description, height, and weight. */
    public PokemonData(int id, String name, String description, int height, int weight) {
        super(id, name, height, weight); // Calls the constructor of the base class to initialize common attributes.
        this.description = description; // Sets the additional description for this Pokémon.
    }

    /** Returns the description of the Pokémon. */
    public String getDescription() {
        return description;
    }
}
