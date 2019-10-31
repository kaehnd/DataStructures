/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 1: Dot2Dot
 * Name: Daniel Kaehn
 * Created: 3/6/2019
 */
package kaehnd;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Controls interaction between GUI and Picture and Dot objects
 */
public class Dot2DotController {

    @FXML
    private Canvas canvas;

    @FXML
    private MenuItem dotsOnlyItem;

    @FXML
    private MenuItem linesOnlyItem;

    @FXML
    private MenuItem removeItem;


    @FXML
    private Label dotLabel;

    private Picture originalPicture;
    private Picture currentPicture;

    @FXML
    private void open() {
        clearCanvas(canvas);
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("DOT Files", "*.dot"));
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                originalPicture = new Picture(new ArrayList<Dot>());
                originalPicture.load(file);
                originalPicture.drawDots(canvas);
                originalPicture.drawLines(canvas);
                currentPicture = originalPicture;
                disableElements(false);
                updateLabel();
            }
        } catch (IOException e) {
            throwAlert("Open Error", "File failed to load");
        } catch (IllegalArgumentException e) {
            throwAlert("Open Error", e.getMessage());
        } catch (NoSuchElementException e) {
            throwAlert("Open Error", ".dot file is corrupted or malformed");
        }
    }

    @FXML
    private void close() {
        Platform.exit();
    }

    @FXML
    private void linesOnly() {
        clearCanvas(canvas);
        currentPicture.drawLines(canvas);
    }

    @FXML
    private void dotsOnly() {
        clearCanvas(canvas);
        currentPicture.drawDots(canvas);
    }

    @FXML
    private void removeDots() {
        try {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Dot Reduction");
            inputDialog.setHeaderText("Enter desired number of dots");
            Optional<String> stringOptional = inputDialog.showAndWait();
            if (stringOptional.isPresent()) {

                int dots = Integer.parseInt(stringOptional.get());
                if (dots <= originalPicture.getNumDots()) {
                    currentPicture = new Picture(originalPicture, new ArrayList<Dot>());
                    currentPicture.removeDots(dots);
                    clearCanvas(canvas);
                    currentPicture.drawDots(canvas);
                    currentPicture.drawLines(canvas);
                    updateLabel();
                }
            }
        } catch(NumberFormatException e) {
            throwAlert("Remove Error", "Enter a valid number of dots");
        } catch(IllegalArgumentException e) {
            throwAlert("Remove Error", e.getMessage());
        }
    }

    @FXML
    private void save() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("DOT Files", "*.dot"));

            File file = fileChooser.showSaveDialog(new Stage());
            currentPicture.save(file);
        } catch (IOException e) {
            throwAlert("Save Error", "File failed to save");
        }
    }

    private void throwAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearCanvas(Canvas canvas){
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getHeight(), canvas.getWidth());
    }

    private void disableElements(boolean b){
        dotsOnlyItem.setDisable(b);
        linesOnlyItem.setDisable(b);
        removeItem.setDisable(b);
    }

    private void updateLabel(){
        dotLabel.setText("Dots: " + currentPicture.getNumDots());
    }
}