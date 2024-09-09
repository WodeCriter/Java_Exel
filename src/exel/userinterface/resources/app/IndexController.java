package exel.userinterface.resources.app;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import exel.eventsys.EventBus;
import exel.userinterface.resources.app.popups.newsheet.CreateNewSheetScreenController;

public class IndexController {

    @FXML
    private MenuItem buttonNewFile;

    @FXML
    void newFileEventLisener(ActionEvent event) {
        try {
            // Load the FXML file for the new sheet popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/exel/userinterface/resources/app/popups/newsheet/CreateNewSheetScreen.fxml"));
            VBox popupRoot = loader.load();

            // Get the controller and set the EventBus
            CreateNewSheetScreenController popupController = loader.getController();
            //popupController.setEventBus(eventBus);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Create New Sheet");
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(((MenuItem) event.getSource()).getParentPopup().getScene().getWindow());  // Set the owner to the current stage
            popupStage.setScene(new Scene(popupRoot));

            // Show the popup
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();  // Handle exceptions appropriately
        }
    }

}
