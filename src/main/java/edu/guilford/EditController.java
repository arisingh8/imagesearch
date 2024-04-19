package edu.guilford;

import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.util.logging.Logger;

import org.imgscalr.Scalr;

import java.util.logging.Level;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Controller for the edit scene (the image editing and exporting page)
 * 
 * @author Ari Singh
 * @see SearchController
 */
public class EditController {
    static int INITIAL_WIDTH = 600;
    static int INITIAL_HEIGHT = 400;

    private ImageResult currentImage;
    private BufferedImage currentEditedImage;
    private Runnable sceneSwapper;
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private CheckBox aspectRatioCheckBox;

    @FXML
    private Pane imagePane;

    /**
     * Initializes the edit scene by setting the initial width and height of the image
     * and adding a filter for image files to the file chooser
     */
    @FXML
    private void initialize() {
        widthTextField.setText(Double.toString(INITIAL_WIDTH));
        heightTextField.setText(Double.toString(INITIAL_HEIGHT));
        System.out.println("width: " + widthTextField.getText());
        System.out.println("height: " + heightTextField.getText());

        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image files", "*.png")
        );
    }

    /**
     * Gets the current image as a BufferedImage
     * 
     * @return the current image as a BufferedImage
     */
    private BufferedImage getBufferedImage() {
        return SwingFXUtils.fromFXImage(currentImage.getImage().getImage(), null);
    }

    /**
     * Gets the current image as an Image (JavaFX version)
     * 
     * @return the current image as an Image
     */
    private Image getFXImage() {
        return SwingFXUtils.toFXImage(currentEditedImage, null);
    }

    /**
     * Sets the current image to the given image
     * 
     * @param currentImage the image to set as the current image
     */
    public void setCurrentImage(ImageResult currentImage) {
        this.currentImage = currentImage;

        double width = Double.parseDouble(widthTextField.getText());
        double height = Double.parseDouble(heightTextField.getText());
        currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, (int) width, (int) height);
        drawImage();
    }

    /**
     * Sets the scene swapper for this edit controller
     * 
     * @param sceneSwapper the scene swapper to set
     */
    public void setSceneSwapper(Runnable sceneSwapper) {
        this.sceneSwapper = sceneSwapper;
    }

    /**
     * Resets the image to its original size and shape
     */
    @FXML
    private void reset() {
        this.currentEditedImage = getBufferedImage();

        double width = INITIAL_WIDTH;
        double height = INITIAL_HEIGHT;
        widthTextField.setText(Double.toString(width));
        heightTextField.setText(Double.toString(height));
        aspectRatioCheckBox.setSelected(true);

        currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, (int) width, (int) height);
        drawImage();
    }

    /**
     * Changes the width of the image to the value in the width text field
     */
    @FXML
    private void widthChanged() {
        if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty() || 
            !widthTextField.getText().matches("[0-9]+") || !heightTextField.getText().matches("[0-9]+") ||
            Integer.parseInt(widthTextField.getText()) <= 0 || Integer.parseInt(heightTextField.getText()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid width or height");
            alert.setHeaderText("Width and height must be positive numbers!");
            alert.showAndWait();
            return;
        }
        double width = Double.parseDouble(widthTextField.getText());
        double height = Double.parseDouble(heightTextField.getText());
        if (aspectRatioCheckBox.isSelected()) {
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, (int) width, (int) height);
        } else {
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, (int) width, (int) height);
        }

        drawImage();
    }

    /**
     * Changes the height of the image to the value in the height text field
     */
    @FXML
    private void heightChanged() {
        if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty() || 
            !widthTextField.getText().matches("[0-9]+") || !heightTextField.getText().matches("[0-9]+") ||
            Integer.parseInt(widthTextField.getText()) <= 0 || Integer.parseInt(heightTextField.getText()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid width or height");
            alert.setHeaderText("Width and height must be positive numbers!");
            alert.showAndWait();
            return;
        }
        double width = Double.parseDouble(widthTextField.getText());
        double height = Double.parseDouble(heightTextField.getText());
        if (aspectRatioCheckBox.isSelected()) {
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, (int) width, (int) height);
        } else {
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, (int) width, (int) height);
        }

        drawImage();
    }

    /**
     * Draws the current image on the window
     */
    @FXML
    private void drawImage() {
        imagePane.getChildren().clear();

        ImageView imageView = new ImageView(getFXImage());

        imagePane.getChildren().add(imageView);

        widthTextField.setText(Integer.toString(currentEditedImage.getWidth()));
        heightTextField.setText(Integer.toString(currentEditedImage.getHeight()));
    }

    /**
     * Listens for the back button to be pressed and switches the scene back to the search scene
     */
    @FXML
    private void backButtonPressed() {
        sceneSwapper.run();
    }

    /**
     * Listens for the save button to be pressed and saves the current image to a file
     */
    @FXML
    private void saveButtonPressed() {
        File selectedLocation = fileChooser.showSaveDialog(null);
        if (selectedLocation != null) {
            try {
                ImageIO.write(currentEditedImage, "png", selectedLocation);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Saved!");
                alert.setHeaderText("Image saved to " + selectedLocation.getAbsolutePath());
                alert.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(EditController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error saving image to " + selectedLocation.getAbsolutePath());
                alert.showAndWait();
            }
        }
    }

    /**
     * Listens for the copy button to be pressed and copies the current image to the clipboard
     */
    @FXML
    private void copyButtonPressed() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        content.putImage(getFXImage());
        clipboard.setContent(content);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copied!");
        alert.setHeaderText("Image copied to clipboard");
        alert.showAndWait();
    }

    /**
     * Listens for the show metadata button to be pressed and shows the metadata of the current image in a dialog
     */
    @FXML
    private void showMetadataButtonPressed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Metadata");
        alert.setHeaderText("Image metadata from Unsplash shown below");
        alert.setContentText("Likes: " + currentImage.getLikes() + "\n" +
                             "Original width: " + currentImage.getWidth() + "\n" +
                             "Original height: " + currentImage.getHeight() + "\n" +
                             "Description: " + currentImage.getDescription() + "\n" +
                             "Created at: " + currentImage.getCreated_at());
        alert.showAndWait();
    }
}
