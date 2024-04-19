package edu.guilford;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
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
        widthTextField.setText(Double.toString(imagePane.getPrefWidth()));
        heightTextField.setText(Double.toString(imagePane.getPrefHeight()));
        System.out.println("width: " + widthTextField.getText());
        System.out.println("height: " + heightTextField.getText());

        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image files", "*.png")
        );
    }

    public void setCurrentImage(ImageResult currentImage) {
        this.currentImage = currentImage;
        this.currentEditedImage = SwingFXUtils.fromFXImage(currentImage.getImage().getImage(), null);

        double width = Double.parseDouble(widthTextField.getText());
        double height = Double.parseDouble(heightTextField.getText());
        currentEditedImage = Scalr.resize(currentEditedImage, Scalr.Method.SPEED, Scalr.Mode.AUTOMATIC, (int) width, (int) height);
        drawImage();
    }

    public void setSceneSwapper(Runnable sceneSwapper) {
        this.sceneSwapper = sceneSwapper;
    }

    @FXML
    private void reset() {
        this.currentEditedImage = SwingFXUtils.fromFXImage(currentImage.getImage().getImage(), null);

        double width = imagePane.getPrefWidth();
        double height = imagePane.getPrefHeight();
        widthTextField.setText(Double.toString(width));
        heightTextField.setText(Double.toString(height));
        aspectRatioCheckBox.setSelected(true);

        currentEditedImage = Scalr.resize(currentEditedImage, Scalr.Method.SPEED, Scalr.Mode.AUTOMATIC, (int) width, (int) height);
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
            currentEditedImage = Scalr.resize(currentEditedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, (int) width, (int) height);
        } else {
            if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty()) {
                return;
            }
            double width = Double.parseDouble(widthTextField.getText());
            double height = Double.parseDouble(heightTextField.getText());
            if (width <= 0 || height <= 0) {
                return;
            }
            currentEditedImage = Scalr.resize(currentEditedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, (int) width, (int) height);
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
            currentEditedImage = Scalr.resize(currentEditedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, (int) width, (int) height);
        } else {
            if (widthTextField.getText().isEmpty() || heightTextField.getText().isEmpty()) {
                return;
            }
            double width = Double.parseDouble(widthTextField.getText());
            double height = Double.parseDouble(heightTextField.getText());
            if (width <= 0 || height <= 0) {
                return;
            }
            currentEditedImage = Scalr.resize(currentEditedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, (int) width, (int) height);
        }

        drawImage();
    }

    @FXML
    private void drawImage() {
        imagePane.getChildren().clear();

        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(currentEditedImage, null));

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

        content.putImage(SwingFXUtils.toFXImage(currentEditedImage, null));
        clipboard.setContent(content);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copied!");
        alert.setHeaderText("Image copied to clipboard");
        alert.showAndWait();
    }
}
