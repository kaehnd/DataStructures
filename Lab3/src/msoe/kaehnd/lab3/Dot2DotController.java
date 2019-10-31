/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 3: Dot2Dot
 * Name: Daniel Kaehn
 * Created: 3/16/2019
 */
package msoe.kaehnd.lab3;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


/**
 * Controls interaction between GUI and Picture and Dot objects
 */
public class Dot2DotController implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private MenuItem dotsOnlyItem;

    @FXML
    private MenuItem linesOnlyItem;

    @FXML
    private MenuItem removeItem;

    @FXML
    private MenuItem saveItem;


    @FXML
    private Label dotLabel;

    private Picture originalPicture;
    private Picture currentPicture;


    private Stage removeDotsWindow;

    private TextField dotsField;
    private MenuButton listTypeMenu;
    private MenuButton algorithmMenu;
    private boolean closed;


    /**
     * Builds removeDotsWindow when Controller is instantiated
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        removeDotsWindow = buildRemoveDotsWindow();
    }

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
        final double nanToSec = 1000000000;
        removeDotsWindow.showAndWait();
        if (closed) {
            closed = false;
            return;
        }
        String listType = listTypeMenu.getText();
        String algorithmType = algorithmMenu.getText();

        long nanos;

        try {
            int dots = Integer.parseInt(dotsField.getText());
            if (dots <= originalPicture.getNumDots()) {
                if (listType.equals("ArrayList")) {
                    currentPicture = new Picture(originalPicture, new ArrayList<Dot>());
                } else {
                    currentPicture = new Picture(originalPicture, new LinkedList<Dot>());
                }
                if (algorithmType.equals("Index Algorithm")) {
                    nanos = currentPicture.removeDots(dots);
                } else {
                    nanos = currentPicture.removeDots2(dots);
                }
                clearCanvas(canvas);
                currentPicture.drawDots(canvas);
                currentPicture.drawLines(canvas);
                updateLabel();
                Alert time = new Alert(Alert.AlertType.INFORMATION);
                long hr = TimeUnit.NANOSECONDS.toHours(nanos);
                nanos -= TimeUnit.HOURS.toNanos(hr);
                long min = TimeUnit.NANOSECONDS.toMinutes(nanos);
                nanos -= TimeUnit.MINUTES.toNanos(min);
                double sec = nanos / nanToSec;

                String hms = String.format("%02d:%02d:%07.4f", hr, min, sec);
                time.setContentText("Operation took " + hms);
                time.setTitle("Time Result");
                time.setHeaderText("Time Elapsed for " + listType + " " + algorithmType);
                time.showAndWait();
            } else {
                dotsField.setText(String.valueOf(currentPicture.getNumDots()));
                removeDots();
            }
        } catch(NumberFormatException e) {
            throwAlert("Remove Error", "Enter a valid number of dots");
            removeDots();
        } catch(IllegalArgumentException e) {
            throwAlert("Remove Error", e.getMessage());
            removeDots();
        }
    }

    @FXML
    private void save() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("DOT Files", "*.dot"));

            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                currentPicture.save(file);
            }
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
        final int clearStartingPoint = 0;
        canvas.getGraphicsContext2D().clearRect(clearStartingPoint, clearStartingPoint,
                canvas.getHeight(), canvas.getWidth());
    }

    private void disableElements(boolean b){
        dotsOnlyItem.setDisable(b);
        linesOnlyItem.setDisable(b);
        removeItem.setDisable(b);
        saveItem.setDisable(b);
    }

    private void updateLabel(){
        dotLabel.setText("Dots: " + currentPicture.getNumDots());
    }

    private Stage buildRemoveDotsWindow() {
        final int buttonWidth = 140;
        final int lowerPaneSpacing = 10;
        final int height = 200;
        final int width = 500;
        final int labelWidth = 100;

        Stage stageToReturn = new Stage();
        VBox mainPane = new VBox();
        HBox upperPane = new HBox();
        HBox middlePane = new HBox();
        HBox lowerPane = new HBox();
        HBox buttonPane = new HBox();

        Label listType = new Label("List Type:");
        listType.setPrefWidth(labelWidth);
        listTypeMenu = new MenuButton();
        listTypeMenu.setPrefWidth(buttonWidth);
        listTypeMenu.setAlignment(Pos.CENTER);
        MenuItem arrayList = new MenuItem("ArrayList");

        arrayList.setOnAction(e -> listTypeMenu.setText("ArrayList"));
        MenuItem linkedList = new MenuItem("LinkedList");
        linkedList.setOnAction(e -> listTypeMenu.setText("LinkedList"));
        listTypeMenu.getItems().addAll(arrayList, linkedList);
        upperPane.getChildren().addAll(listType, listTypeMenu);
        upperPane.setAlignment(Pos.CENTER);
        upperPane.setSpacing(lowerPaneSpacing);

        Label algorithm = new Label("Algorithm:");
        algorithm.setPrefWidth(labelWidth);
        algorithmMenu = new MenuButton();
        algorithmMenu.setPrefWidth(buttonWidth);
        algorithmMenu.setAlignment(Pos.CENTER);
        MenuItem indexAlgorithm = new MenuItem("Index Algorithm");
        indexAlgorithm.setOnAction(e -> algorithmMenu.setText("Index Algorithm"));
        MenuItem iteratorAlgorithm = new MenuItem("Iterator Algorithm");
        iteratorAlgorithm.setOnAction(e -> algorithmMenu.setText("Iterator Algorithm"));
        middlePane.getChildren().addAll(algorithm, algorithmMenu);
        middlePane.setAlignment(Pos.CENTER);
        middlePane.setSpacing(lowerPaneSpacing);

        algorithmMenu.getItems().addAll(indexAlgorithm, iteratorAlgorithm);

        Label dotsLabel = new Label("Dots Desired:");
        dotsLabel.setPrefWidth(labelWidth);
        dotsField = new TextField();
        dotsField.setAlignment(Pos.CENTER);
        dotsField.setPrefWidth(buttonWidth);
        lowerPane.getChildren().addAll(dotsLabel, dotsField);
        lowerPane.setAlignment(Pos.CENTER);
        lowerPane.setSpacing(lowerPaneSpacing);

        Button goButton = new Button("GO");
        goButton.setOnAction(e -> removeDotsWindow.close());
        goButton.setDefaultButton(true);
        buttonPane.getChildren().addAll(goButton);
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        buttonPane.setMaxWidth(labelWidth + buttonWidth + lowerPaneSpacing);

        mainPane.getChildren().addAll(upperPane, middlePane, lowerPane, buttonPane);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setSpacing(lowerPaneSpacing);
        stageToReturn.setScene(new Scene(mainPane, width, height));
        stageToReturn.setTitle("Remove Dots Window");
        stageToReturn.setOnCloseRequest(e -> closed = true);
        return stageToReturn;
    }
}