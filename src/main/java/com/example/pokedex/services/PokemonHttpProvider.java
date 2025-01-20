package com.example.pokedex.services;

import org.apache.http.HttpResponse;
import com.example.pokedex.models.PokemonData;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

/** Service for fetching Pokémon data from the PokeAPI using an HTTP request. */
public class PokemonHttpProvider implements AbstractPokemonProviderService, LocalizedPropertyProviderInterface {
    private String locale = "en";

    @Override
    public void setStringPropertyLocale(String localeCode) {
        this.locale = localeCode;
    }

    @Override
    public String getStringPropertyLocale() {
        return this.locale;
    }
    
    /** Retrieves Pokémon data from the PokeAPI based on the given ID. */
    @Override
    public PokemonData getPokemonData(int id, String databasePath) {
        String jsonResponse = "";
        try {
            // Initializes the HTTP client for the API request.
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            // Constructs an HTTP GET request with the specific Pokémon ID.
            HttpGet request = new HttpGet("https://pokeapi.co/api/v2/pokemon/" + id);
            request.addHeader("content-type", "application/json");

            // Executes the API request and retrieves the response as a JSON string.
            HttpResponse result = httpClient.execute(request);
            jsonResponse = EntityUtils.toString(result.getEntity(), "UTF-8");
            System.out.println("API connection successfully established.");

            // Parses the JSON response to convert it into a usable object.
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(jsonResponse);

            // Validates and processes the response if it is a JSON object.
            if (resultObject instanceof JSONObject) {
                JSONObject obj = (JSONObject) resultObject;
                String name = obj.get("name").toString();
                int weight = Integer.parseInt(obj.get("weight").toString());
                int height = Integer.parseInt(obj.get("height").toString());

                // Fetches the description based on the locale.
                String description = fetchPokemonDescription(id);

                // Creates and returns a PokemonData object with the retrieved details.
                return new PokemonData(id, name, description, height, weight);
            } else {
                // Logs an error if the response is not the expected JSON object.
                System.err.println("Expected a JSON Object but received a different response.");
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            // Logs the exception in case of an error during the request or parsing.
            e.printStackTrace();
        }
        // Returns null if no data could be retrieved.
        return null;
    }

    private String fetchPokemonDescription(int id) {
        String jsonResponse = "";
        try {
            // Initializes the HTTP client for the API request.
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            // Constructs an HTTP GET request for the Pokémon species endpoint.
            HttpGet request = new HttpGet("https://pokeapi.co/api/v2/pokemon-species/" + id);
            request.addHeader("content-type", "application/json");

            // Executes the API request and retrieves the response as a JSON string.
            HttpResponse result = httpClient.execute(request);
            jsonResponse = EntityUtils.toString(result.getEntity(), "UTF-8");

            // Parses the JSON response to convert it into a usable object.
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(jsonResponse);

            // Validates and processes the response if it is a JSON object.
            if (resultObject instanceof JSONObject) {
                JSONObject obj = (JSONObject) resultObject;
                JSONArray flavorTextEntries = (JSONArray) obj.get("flavor_text_entries");

                // Iterates through the flavor text entries to find the description in the desired locale.
                for (Object entry : flavorTextEntries) {
                    JSONObject flavorTextEntry = (JSONObject) entry;
                    JSONObject language = (JSONObject) flavorTextEntry.get("language");
                    if (language.get("name").toString().equals(this.locale)) {
                        return flavorTextEntry.get("flavor_text").toString().replace("\n", " ");
                    }
                }
            } else {
                // Logs an error if the response is not the expected JSON object.
                System.err.println("Expected a JSON Object but received a different response.");
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            // Logs the exception in case of an error during the request or parsing.
            e.printStackTrace();
        }
        // Returns a default message if no description could be retrieved.
        return "No description found in the API";
    }
}
