/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.guilford;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.google.gson.Gson;

/**
 *
 * @author ari
 */
public final class ImageSearch {
    private static String api_key = "OzoyRCa_d79pAF6YX1PNdAzh0RNCKMTCHtLuMcXtW94";

    private ImageSearch() {
    }

    public static ImageResults getImageResults(String query, int numPics) {
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.unsplash.com/search/photos?query=" + query + "&per_page=" + numPics))
                .header("Authorization", "Client-ID " + api_key)
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            ImageResults results = gson.fromJson(response.body(), ImageResults.class);
            int numReceived = results.getResults().length;
            System.out.println("got response with " + numReceived + " results");
            System.out.println("could've gotten " + results.getTotal() + " results");
            return results;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ImageSearch.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
