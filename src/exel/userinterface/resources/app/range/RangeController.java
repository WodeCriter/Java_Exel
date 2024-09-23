package exel.userinterface.resources.app.range;

import exel.eventsys.EventBus;
import exel.eventsys.events.DisplaySelectedCellEvent;
import exel.userinterface.resources.app.popups.newRange.CreateNewRangeScreenController;
import exel.userinterface.resources.app.popups.newsheet.CreateNewSheetScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.html.ListView;

public class RangeController
{
    private EventBus eventBus;

    @FXML
    private ListView rangesList;

    @FXML
    private Button addRangeButton;

    public void setEventBus(EventBus eventBus)
    {
        this.eventBus = eventBus;
        subscribeToEvents();
    }

    private void subscribeToEvents()
    {
        //eventBus.subscribe(DisplaySelectedCellEvent.class, this::handleDisplaySelectedCell);
    }

    @FXML
    void newRangeButtonClicked(ActionEvent event)
    {
        try {
            // Load the FXML file for the new sheet popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/exel/userinterface/resources/app/popups/newRange/AddNewRangeScreen.fxml"));
            VBox popupRoot = loader.load();

            Object controller = loader.getController();

            if (controller instanceof CreateNewRangeScreenController)
                ((CreateNewRangeScreenController) controller).setEventBus(eventBus);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Add New Range");
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(((MenuItem) event.getSource()).getParentPopup().getScene().getWindow());  // Set the owner to the current stage
            popupStage.setScene(new Scene(popupRoot, 200, 150));

            // Show the popup
            popupStage.showAndWait();
        }
        catch (Exception e)
        {
            e.printStackTrace();  // Handle exceptions appropriately
        }
    }
}
