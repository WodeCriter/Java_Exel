package exel.userinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Assuming the FXML file is named "MainScreen.fxml" and is located in the "app" directory under resources
            Parent root = FXMLLoader.load(getClass().getResource("/exel/resources/app/index.fxml"));

            // Setting the title of the stage (optional)
            primaryStage.setTitle("Exel");

            // Creating a scene object with the loaded layout
            Scene scene = new Scene(root);

            // Adding the scene to the stage
            primaryStage.setScene(scene);

            // Displaying the contents of the stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}