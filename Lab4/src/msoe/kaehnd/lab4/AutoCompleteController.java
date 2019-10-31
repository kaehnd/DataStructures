/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 4: AutoComplete
 * Name: Daniel Kaehn
 * Created: 3/28/2019
 */

package msoe.kaehnd.lab4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Controls interaction between user and AutoComplete Application
 */
public class AutoCompleteController implements Initializable {


    private AutoCompleter autoCompleter;

    private String currentDictPath;

    @FXML
    private ListView<String> resultListView;

    @FXML
    private TextField searchField;

    @FXML
    private Label timeLabel;

    @FXML
    private Label matchLabel;


    /**
     * Runs on startup of Application, gives the Application a default AutoCompleter
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        autoCompleter = new IndexAutoCompleter(new ArrayList<>());
    }


    @FXML
    private void open() {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                currentDictPath = file.getPath();
                autoCompleter.initialize(currentDictPath);
                searchField.setDisable(false);
                resultListView.setDisable(false);
                updateTimeLabel();
            }
        } catch (IOException e) {
            throwAlert("Load Error", e.getMessage());
        } catch (NoSuchElementException e) {
            throwAlert("Load Error", "Dictionary File is corrupted or malformed");
        } catch (IllegalArgumentException e) {
            throwAlert("Load Error", e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throwAlert("Load Error", "Dictionary File argument is malformed");
        }
    }

    @FXML
    private void onKeyTyped() {
        if (searchField.getText().isEmpty()) {
            resultListView.getItems().clear();
            matchLabel.setText("Matches Found: ");
        } else {
            resultListView.getItems().clear();
            String beginning = searchField.getText();
            List<String> stringList = autoCompleter.allThatBeginWith(beginning);
            for (String s : stringList) {
                resultListView.getItems().add(s);
            }
            matchLabel.setText("Matches Found: " + stringList.size());
            updateTimeLabel();
        }
    }

    @FXML
    private void changeAutoCompleter(ActionEvent e) {
        String newInfo = ((RadioMenuItem)e.getSource()).getText();
        switch (newInfo) {
            case "ArrayList with Indices":
                autoCompleter = new IndexAutoCompleter(new ArrayList<>());
                break;
            case "LinkedList with Indices":
                autoCompleter = new IndexAutoCompleter(new ArrayList<>());
                break;
            case "ArrayList with Iterator":
                autoCompleter = new IteratorAutoCompleter(new ArrayList<>());
                break;
            case "LinkedList with Iterator":
                autoCompleter = new IteratorAutoCompleter(new LinkedList<>());
                break;
        }
        if (currentDictPath != null) {
            try {
                autoCompleter.initialize(currentDictPath);
                updateTimeLabel();
                resultListView.getItems().clear();
                searchField.clear();
            } catch (IOException exception) {
                throwAlert("Load Error", "Dictionary Reload Failed");
            }
        }
    }

    @FXML
    private void complete() {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word != null && !word.isEmpty()) {
            searchField.setText(word);
        }
    }

    private void updateTimeLabel() {
        timeLabel.setText("Time Taken: " + timeToString(autoCompleter.getLastOperationTime()));
    }

    private void throwAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static String timeToString(long nanos) {
        final double nanToSec = 0.000000001;
        final double nanToMillis = 0.000001;
        final double nanToMicro = 0.001;
        if (TimeUnit.NANOSECONDS.toSeconds(nanos) > 0) {
            long min = TimeUnit.NANOSECONDS.toMinutes(nanos);
            nanos -= TimeUnit.MINUTES.toNanos(min);
            double sec = nanos * nanToSec;
            return String.format("%02d:%07.4f", min, sec);
        } else if (TimeUnit.NANOSECONDS.toMillis(nanos) > 0) {
            double millis = nanos * nanToMillis;
            return String.format("%.4f ms", millis);
        } else if (TimeUnit.NANOSECONDS.toMicros(nanos) > 0) {
            double micros = nanos * nanToMicro;
            return String.format("%.4f \u03BCs", micros);
        } else {
            return nanos + " ns";
        }
    }
}
