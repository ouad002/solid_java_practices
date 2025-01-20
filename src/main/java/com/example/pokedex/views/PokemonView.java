package com.example.pokedex.views;

import com.example.pokedex.models.PokemonData;
import com.example.pokedex.utilities.OutputFormat;

/**
 * Handles the generation and display of Pokémon data in multiple formats
 * (text, HTML, CSV) based on the chosen output format.
 */
public class PokemonView implements MultipleFormatGenerator {
    private final OutputFormat outputFormat; // Determines the output format.
    private final String locale; // Determines the locale.
    /** Initializes the view with the specified output format. */
    public PokemonView(OutputFormat outputFormat, String locale) {
        this.outputFormat = outputFormat;
        this.locale = locale;
    }

    /** Generates a human-readable HTML representation of the Pokémon data. */
    @Override
    public String generateHTML(PokemonData pokemonData) {
        return "<h1>" + pokemonData.getName() + "</h1>\n" +
                "<ul>\n" +
                "<li>Id: " + pokemonData.getId() + "</li>\n" +
                "<li>Height: " + pokemonData.getHeight() + "</li>\n" +
                "<li>Weight: " + pokemonData.getWeight() + "</li>\n" +
                "<li>Description: " + pokemonData.getDescription() + "</li>\n" +
                "</ul>";
    }

    /** Generates a CSV representation of the Pokémon data. */
    @Override
    public String generateCSV(PokemonData pokemonData) {
        return "Id;Name;Height;Weight;Description\n" +
                pokemonData.getId() + ";" +
                pokemonData.getName() + ";" +
                pokemonData.getHeight() + ";" +
                pokemonData.getWeight() + ";" +
                pokemonData.getDescription();
    }

    /** Generates a human-readable text representation of the Pokémon data. */
    @Override
    public String generateHumanReadableText(PokemonData pokemonData) {
        return """
                =============================
                Pokemon # %d
                Nom : %s
                Taille : %d
                Poids : %d
                Description : %s
                =============================
                """.formatted(
                pokemonData.getId(),
                pokemonData.getName(),
                pokemonData.getHeight(),
                pokemonData.getWeight(),
                pokemonData.getDescription());
    }

    /** Displays the Pokémon data in the selected output format. */
    public void displayPokemonData(PokemonData pokemonData) {
        switch (outputFormat) {
            case TEXT -> System.out.println(generateHumanReadableText(pokemonData));
            case HTML -> System.out.println(generateHTML(pokemonData));
            case CSV -> System.out.println(generateCSV(pokemonData));
            default -> System.err.println("Unsupported output format");
        }
    }
}
