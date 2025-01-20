package com.example.codesamples;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpExample {
    public static void makeHttpRequest()  {
        try {

                /* Setup HTTP Client */
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            /* Prepare HTTP request */
            HttpGet request = new HttpGet("https://pokeapi.co/api/v2/pokemon/1");
            // necessary header to get a JSON response
            request.addHeader("content-type", "application/json");
            // run the request
            HttpEntity responseEntity = httpClient.execute(request).getEntity();

            // decode the response body into a Java string
            String responseBody = EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println("Response is : " + responseBody);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
