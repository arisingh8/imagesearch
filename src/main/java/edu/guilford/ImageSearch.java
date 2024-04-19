package edu.guilford;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class ImageSearch extends Application {

    private Scene searchScene;
    private Scene editScene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader searchFxmlLoader = new FXMLLoader(getClass().getResource("search.fxml"));
        searchScene = new Scene(searchFxmlLoader.load(), 800, 600);
        SearchController searchController = searchFxmlLoader.getController();

        FXMLLoader editFxmlLoader = new FXMLLoader(getClass().getResource("edit.fxml"));
        editScene = new Scene(editFxmlLoader.load(), 800, 600);
        EditController editController = editFxmlLoader.getController();
        searchController.setEditController(editController);
        searchController.setSceneSwapper(
            () -> {
                stage.setScene(editScene);
            }
        );

        stage.setScene(searchScene);
        stage.show();
    }

    /*
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ImageSearch.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    */

    public static void main(String[] args) {
        launch();
    }
}