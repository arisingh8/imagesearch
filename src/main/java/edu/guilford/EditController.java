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

    private BufferedImage getBufferedImage() {
        return SwingFXUtils.fromFXImage(currentImage.getImage().getImage(), null);
    }

    private Image getFXImage() {
        return SwingFXUtils.toFXImage(currentEditedImage, null);
    }

    public void setCurrentImage(ImageResult currentImage) {
        this.currentImage = currentImage;

        double width = Double.parseDouble(widthTextField.getText());
        double height = Double.parseDouble(heightTextField.getText());
        currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, (int) width, (int) height);
        drawImage();
    }

    public void setSceneSwapper(Runnable sceneSwapper) {
        this.sceneSwapper = sceneSwapper;
    }

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

    @FXML
    private void widthChanged() {
        if (aspectRatioCheckBox.isSelected()) {
            if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty()) {
                return;
            }
            double width = Double.parseDouble(widthTextField.getText());
            double height = Double.parseDouble(heightTextField.getText());
            if (width <= 0 || height <= 0) {
                return;
            }
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, (int) width, (int) height);
        } else {
            if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty()) {
                return;
            }
            double width = Double.parseDouble(widthTextField.getText());
            double height = Double.parseDouble(heightTextField.getText());
            if (width <= 0 || height <= 0) {
                return;
            }
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, (int) width, (int) height);
        }

        drawImage();
    }

    @FXML
    private void heightChanged() {
        if (aspectRatioCheckBox.isSelected()) {
            if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty()) {
                return;
            }
            double width = Double.parseDouble(widthTextField.getText());
            double height = Double.parseDouble(heightTextField.getText());
            if (width <= 0 || height <= 0) {
                return;
            }
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, (int) width, (int) height);
        } else {
            if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty()) {
                return;
            }
            double width = Double.parseDouble(widthTextField.getText());
            double height = Double.parseDouble(heightTextField.getText());
            if (width <= 0 || height <= 0) {
                return;
            }
            currentEditedImage = Scalr.resize(getBufferedImage(), Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, (int) width, (int) height);
        }

        drawImage();
    }

    @FXML
    private void drawImage() {
        imagePane.getChildren().clear();

        ImageView imageView = new ImageView(getFXImage());

        imagePane.getChildren().add(imageView);

        widthTextField.setText(Integer.toString(currentEditedImage.getWidth()));
        heightTextField.setText(Integer.toString(currentEditedImage.getHeight()));
    }

    @FXML
    private void backButtonPressed() {
        sceneSwapper.run();
    }

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
            }
        }
    }

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
