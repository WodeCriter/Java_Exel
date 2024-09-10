package exel.userinterface;

import exel.engine.api.Engine;
import exel.eventsys.EventBus;
import exel.userinterface.resources.app.IndexController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI {

    private Engine engine;
    private EventBus eventBus;

    public UI(Engine engine, EventBus eventBus) {
        this.engine = engine;
        this.eventBus = eventBus;
    }

    public void show(Stage primaryStage) {
        try {
            // Assuming the FXML file is named "MainScreen.fxml" and is located in the "app" directory under resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/exel/userinterface/resources/app/index.fxml"));

            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof IndexController) {
                ((IndexController) controller).setEventBus(eventBus);
            }
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
}