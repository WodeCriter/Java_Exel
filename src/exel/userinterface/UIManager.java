package exel.userinterface;

import exel.engine.api.Engine;
import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.eventsys.EventBus;
import exel.eventsys.events.CreateNewSheetEvent;
import exel.userinterface.resources.app.IndexController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIManager {

    private Engine engine;
    private EventBus eventBus;
    private ReadOnlySheet readOnlySheet;

    public UIManager(Engine engine, EventBus eventBus) {
        this.engine = engine;
        this.eventBus = eventBus;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        // Subscribe to the CreateNewSheetEvent
        eventBus.subscribe(CreateNewSheetEvent.class, this::handleCreateNewSheet);
    }

    private void handleCreateNewSheet(CreateNewSheetEvent event) {
        // Call the engine to create a new sheet based on the event details
        ReadOnlySheet readOnlySheet = engine.createSheet(event.getSheetName(), event.getCols(), event.getRows(), event.getWidth(), event.getHeight());

        System.out.println("Sheet created: " + event.getSheetName());
    }



    public void showUI(Stage primaryStage) {
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