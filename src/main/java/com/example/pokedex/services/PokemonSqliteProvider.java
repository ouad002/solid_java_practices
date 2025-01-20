package com.example.pokedex.services;

import com.example.pokedex.models.PokemonData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Service for retrieving Pokémon data from a local SQLite database by ID. */
public class PokemonSqliteProvider implements AbstractPokemonProviderService {

    /**
     * Fetches Pokémon data from the local database based on the given ID and
     * database path.
     */
    @Override
    public PokemonData getPokemonData(int id, String databasePath) {
        Connection conn = null;
        try {
            // Creates a connection to the SQLite database using the provided path.
            String url = "jdbc:sqlite:" + databasePath;
            conn = DriverManager.getConnection(url);
            System.out.println("Successfully connected to the SQLite database.");

            // Prepares an SQL query to retrieve the Pokémon data for the given ID.
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT name, description, height, weight FROM pokemons WHERE id = ?");
            stmt.setInt(1, id); // Sets the ID parameter in the SQL query.

            ResultSet rs = stmt.executeQuery(); // Executes the query and retrieves the result set.

            // Extracts Pokémon details from the query result set.
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                int height = rs.getInt("height");
                int weight = rs.getInt("weight");

                // Constructs and returns a PokemonData object with the retrieved information.
                return new PokemonData(id, name, description, height, weight);
            } else {
                System.out.println("No data found for the given Pokemon ID.");
            }
        } catch (SQLException e) {
            // Logs the exception message if an SQL error occurs.
            System.out.println("SQL error occurred: " + e.getMessage());
        } finally {
            // Closes the database connection to release resources.
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                // Logs an error if closing the connection fails.
                System.out.println("Error closing the database connection: " + ex.getMessage());
            }
        }
        // Returns null if no data could be retrieved from the database.
        return null;
    }
}