/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 4: AutoComplete
 * Name: Daniel Kaehn
 * Created: 3/28/2019
 */
package msoe.kaehnd.lab4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Application allowing users to auto complete typed words
 * from a loaded dictionary txt or csv file using four different strategies
 */
public class AutoComplete extends Application {

    private static final int HEIGHT = 400;
    private static final int WIDTH = 335;

    /**
     * Builds the GUI from FXML
     * @param primaryStage Stage the main Scene is seated
     * @throws Exception Never thrown
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("AutoComplete.fxml"));
        primaryStage.setTitle("AutoComplete");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
