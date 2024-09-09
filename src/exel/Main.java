package exel;

import exel.engine.api.Engine;
import exel.engine.imp.EngineImp;
import exel.userinterface.UI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the engine
            Engine engine = new EngineImp();


            Thread engineThread = new Thread(() -> {
                //engine.start();
            });
            engineThread.setDaemon(true);
            engineThread.start();


            UI ui = new UI();
            ui.show(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);  // This static method launches the JavaFX application
    }
}