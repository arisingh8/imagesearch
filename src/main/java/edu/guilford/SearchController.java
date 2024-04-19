package edu.guilford;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SearchController {
    private static String api_key = "OzoyRCa_d79pAF6YX1PNdAzh0RNCKMTCHtLuMcXtW94";
    static int NUM_PICS_IN_ROW = 4;
    static double BLANK_ASPECT_RATIO = 1.0;

    ImageResults currentResults;

    @FXML
    private ChoiceBox<Integer> numItemsChoiceBox;

    @FXML
    private FlowPane resultsPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private void initialize() {
        numItemsChoiceBox.getItems().addAll(10, 15, 20, 25);
        numItemsChoiceBox.setValue(10);

        resultsPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            drawImagesFromResults();
        });
    }

    @FXML
    private void runSearch() {
        System.out.println(searchTextField.getText());

        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.unsplash.com/search/photos?query=" + searchTextField.getText() + "&per_page=" + numItemsChoiceBox.getValue()))
                .header("Authorization", "Client-ID " + api_key)
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            ImageResults results = gson.fromJson(response.body(), ImageResults.class);
            int numReceived = results.getResults().length;
            System.out.println("got response with " + numReceived + " results");
            System.out.println("could've gotten " + results.getTotal() + " results");
            currentResults = results;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
        }

        drawImagesFromResults();
    }

    private void drawImagesFromResults() {
        if (currentResults == null) {
            return;
        }
        resultsPane.getChildren().clear();
        
        int rowWidth = (int) resultsPane.getWidth() - ((int) resultsPane.getHgap() * (NUM_PICS_IN_ROW - 1)) - ((int) resultsPane.getPadding().getLeft() + (int) resultsPane.getPadding().getRight()) - 5;
        System.out.println("Row width: " + rowWidth);
        System.out.println("Pane width: " + resultsPane.getWidth());

        if (currentResults.getTotal() > 0) {
            ImageResult[] images = currentResults.getResults();
    
            double rowHeights = 0;
            for (int i = 0; i < images.length; i += NUM_PICS_IN_ROW) {
                double aspectRatioSum = 0;
                for (int j = 0; j < NUM_PICS_IN_ROW && i + j < images.length; j++) {
                    aspectRatioSum += ((double) images[i + j].getWidth()) / images[i + j].getHeight();
                }
                if (i + NUM_PICS_IN_ROW > images.length) {
                    aspectRatioSum += BLANK_ASPECT_RATIO * (NUM_PICS_IN_ROW - (images.length % NUM_PICS_IN_ROW));
                }
                double rowHeight = rowWidth / aspectRatioSum;
                rowHeights += rowHeight;
                for (int j = 0; j < NUM_PICS_IN_ROW && i + j < images.length; j++) {
                    ImageView imageView = images[i + j].getSmallImage();
                    imageView.setFitHeight(rowHeight);
                    imageView.setPreserveRatio(true);

                    final int idx = i + j;
                    imageView.setOnMouseClicked(e -> {
                        System.out.println("Clicked on " + images[idx].getLikes());
                    });
                    resultsPane.getChildren().add(imageView);
                }
            }

            resultsPane.setPrefHeight(rowHeights + (resultsPane.getVgap() * (images.length / NUM_PICS_IN_ROW)) + resultsPane.getPadding().getTop() + resultsPane.getPadding().getBottom() + 5);

            System.out.println("Pane height: " + resultsPane.getPrefHeight());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Results");
            alert.setHeaderText("No results found for " + searchTextField.getText());
            alert.showAndWait();
        }
    }
}
