/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 3: Dot2Dot
 * Name: Daniel Kaehn
 * Created: 3/16/2019
 */
package msoe.kaehnd.lab3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Application allowing .dot files to be read
 * and dots and connecting lines to be displayed on a Canvas
 */
public class Dot2Dot extends Application {


    private static final int HEIGHT = 600;
    private static final int WIDTH = 550;

    /**
     * Loads GUI Elements from FXML file and builds GUI
     * @param primaryStage Stage created in launch method
     * @throws Exception warning for FXML loader IOException,
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Dot2Dot.fxml"));
        Parent root = mainLoader.load();
        primaryStage.setTitle("Dot2Dot");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
