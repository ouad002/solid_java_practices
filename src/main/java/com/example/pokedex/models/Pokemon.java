package com.example.pokedex.models;

/**
 * Represents a Pokémon with its basic attributes: ID, name, height, and weight.
 * This class is part of the models package.
 */
public class Pokemon {
    /** Unique identifier for the Pokémon. */
    private final int id;

    /** Name of the Pokémon. */
    private final String name;

    /** Height of the Pokémon. */
    private final int height;

    /** Weight of the Pokémon. */
    private final int weight;

    public Pokemon(int id, String name, int height, int weight) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    /** Gets the ID of the Pokémon. */
    public int getId() {
        return id;
    }

    /** Gets the name of the Pokémon. */
    public String getName() {
        return name;
    }

    /** Gets the height of the Pokémon. */
    public int getHeight() {
        return height;
    }

    /** Gets the weight of the Pokémon. */
    public int getWeight() {
        return weight;
    }
}
