package com.example.codesamples;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONExample {
    public static void parseJsonString() {
        try {
            String jsonString = "{ \"name\": \"Charizard\", \"height\": 1 }";
            JSONParser parser = new JSONParser();
            Object resultObject = parser.parse(jsonString);
            if (resultObject instanceof JSONObject) {
                JSONObject rootObject = (JSONObject) resultObject;
                String name = (String) rootObject.get("name");
                Integer height = Math.toIntExact((Long) rootObject.get("height"));
                System.out.printf("Name: %s, Height: %d\n", name, height);
            }
        } catch (ParseException e) {
            System.err.println(e);
        }
    }

    public static void browseJsonArray() {
        try {
            String jsonString = "{ \"pokemons\": [{\"name\": \"Charizard\"}, {\"name\": \"Charmander\"}] }";
            JSONParser parser = new JSONParser();
            JSONObject rootObject = (JSONObject) parser.parse(jsonString);
            JSONArray pokemonsArray = (JSONArray) rootObject.get("pokemons");
            for (Object o: pokemonsArray) {
                JSONObject pokemonObject = (JSONObject) o;
                String pokemonName = (String)pokemonObject.get("name");
                System.out.println("Pokemon : " + pokemonName);
            }
        } catch (ParseException e) {
            System.err.println(e);
        }
    }
}
