package edu.guilford;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class SearchController {
    static double NUM_ROWS_ON_SCREEN = 4.0;

    @FXML
    private ChoiceBox<Integer> numItemsChoiceBox;

    @FXML
    private FlowPane resultsPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private void initialize() {
        numItemsChoiceBox.getItems().addAll(10, 15, 25, 50);
        numItemsChoiceBox.setValue(10);
    }

    @FXML
    private void runSearch() {
        resultsPane.getChildren().clear();

        long rowHeight = Math.round(resultsPane.getHeight() / NUM_ROWS_ON_SCREEN);

        System.out.println(searchTextField.getText());
        ImageResults results = ImageSearch.getImageResults(searchTextField.getText(), numItemsChoiceBox.getValue());
        ImageResult[] images = results.getResults();

        if (images.length != 0) {
            for (ImageResult image : images) {
                ImageView imageView = new ImageView(image.getSmallImage());
                imageView.setFitHeight(rowHeight);
                imageView.setPreserveRatio(true);
                imageView.setOnMouseClicked(e -> {
                    System.out.println("Clicked on " + image.getId());
                });
                resultsPane.getChildren().add(imageView);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Results");
            alert.setHeaderText("No results found for " + searchTextField.getText());
            alert.showAndWait();
        }
    }
}
