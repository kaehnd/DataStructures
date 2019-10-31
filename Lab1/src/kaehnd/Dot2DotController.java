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
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

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

    private Picture picture;

    @FXML
    private void open() {
        clearCanvas(canvas);
        try {
            File file = new FileChooser().showOpenDialog(new Stage());
            if (file != null) {
                picture = new Picture();
                picture.load(file);
                picture.drawDots(canvas);
                picture.drawLines(canvas);
                dotsOnlyItem.setDisable(false);
                linesOnlyItem.setDisable(false);
            }
        } catch (IOException e) {
            throwAlert("Open Error", "File failed to load");
        } catch (IllegalArgumentException e) {
            throwAlert("Open Error", e.getMessage());
        } catch (NoSuchElementException e){
            throwAlert("Open Error", ".dot file is corrupted or malformed");
        }
    }

    @FXML
    private void close(){
        Platform.exit();
    }

    @FXML
    private void linesOnly(){
        clearCanvas(canvas);
        picture.drawLines(canvas);
    }

    @FXML
    private void dotsOnly(){
        clearCanvas(canvas);
        picture.drawDots(canvas);
    }

    private void throwAlert(String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearCanvas(Canvas canvas){
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getHeight(), canvas.getWidth());
    }
}