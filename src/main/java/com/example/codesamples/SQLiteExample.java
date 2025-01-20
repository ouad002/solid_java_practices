package com.example.codesamples;

import org.sqlite.SQLiteConfig;

import java.sql.*;

public class SQLiteExample {
    public static void makeDbRequest() {
        try {

            /* Setup database path */
            String databaseFilePath = "./ressources/pokemondatabase.sqlite";
            String url = "jdbc:sqlite:" + databaseFilePath;

            /* Open database connection */
            SQLiteConfig config = new SQLiteConfig();
            config.setEncoding(SQLiteConfig.Encoding.UTF8);
            Connection dbConnection = DriverManager.getConnection(url, config.toProperties());

            /* Prepare query */
            PreparedStatement stmt  = dbConnection.prepareStatement("SELECT id, name, description, height, weight FROM pokemons WHERE id = ?");
            stmt.setInt(1, 5);

            /* Run query */
            ResultSet pokemonResultSet = stmt.executeQuery();

            /* Navigate the Result Set (iterator pattern) */
            while (pokemonResultSet.next()) {
                String name = pokemonResultSet.getString("name");
                Integer height = pokemonResultSet.getInt("height");
                System.out.printf("Pokemon name: %s, Pokemon height: %d\n", name, height);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
