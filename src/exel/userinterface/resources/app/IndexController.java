package exel.userinterface.resources.app;


import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.eventsys.events.*;
import exel.userinterface.resources.app.popups.newRange.CreateNewRangeScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import exel.eventsys.EventBus;
import exel.userinterface.resources.app.popups.newsheet.CreateNewSheetScreenController;
import exel.userinterface.resources.app.Sheet.SheetController;

import java.util.Objects;

public class IndexController {

    private ReadOnlyCell selectedCell;

    private EventBus eventBus;

    @FXML
    private MenuItem buttonNewFile;

    @FXML
    private AnchorPane sheetContainer;

    @FXML
    private Label labelOriginalVal;

    @FXML
    private Label labelCoordinate;

    @FXML
    private Label labelCellVersion;

    @FXML
    private TextField textFiledOriginalVal;

    @FXML
    private Button buttonUpdateCell;

    @FXML
    private ListView rangesList;

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        eventBus.subscribe(DisplaySelectedCellEvent.class, this::handleDisplaySelectedCell);
        eventBus.subscribe(RangeCreatedEvent.class, this::handleNewRangeCreated);
    }

    @FXML
    void newFileEventLisener(ActionEvent event) {
        try {
            // Load the FXML file for the new sheet popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/exel/userinterface/resources/app/popups/newsheet/CreateNewSheetScreen.fxml"));
            VBox popupRoot = loader.load();

            Object controller = loader.getController();

            if (controller instanceof CreateNewSheetScreenController) {
                ((CreateNewSheetScreenController) controller).setEventBus(eventBus);
            }

            // Get the controller and set the EventBus
            CreateNewSheetScreenController popupController = loader.getController();
            //popupController.setEventBus(eventBus);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Create New Sheet");
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(((MenuItem) event.getSource()).getParentPopup().getScene().getWindow());  // Set the owner to the current stage
            popupStage.setScene(new Scene(popupRoot, 200, 150));

            // Show the popup
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();  // Handle exceptions appropriately
        }
    }

    public void refreshSheetPlane() {
        try {
            // Load the sheet FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/exel/userinterface/resources/app/Sheet/Sheet.fxml"));
            Pane sheetRoot = loader.load();

            Object controller = loader.getController();

            if (controller instanceof SheetController) {
                ((SheetController) controller).setEventBus(eventBus);
            }

            // Remove any existing content
            sheetContainer.getChildren().clear();

            // Add the sheet to the container
            sheetContainer.getChildren().add(sheetRoot);

            // Anchor the sheet to all sides
            AnchorPane.setTopAnchor(sheetRoot, 0.0);
            AnchorPane.setBottomAnchor(sheetRoot, 0.0);
            AnchorPane.setLeftAnchor(sheetRoot, 0.0);
            AnchorPane.setRightAnchor(sheetRoot, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDisplaySelectedCell(DisplaySelectedCellEvent event){
        this.selectedCell = event.getCell();
        labelCoordinate.setText("Cell: " + selectedCell.getCoordinate());
        labelOriginalVal.setText("original value:" + selectedCell.getOriginalValue());
        labelCellVersion.setText("Cell version:" + String.valueOf(selectedCell.getVersion()));
        textFiledOriginalVal.setText(selectedCell.getOriginalValue());
    }

    @FXML
    void updateCellButtonListener(ActionEvent event) {
        try{
            String NewCellValue = textFiledOriginalVal.getText();
            if(!Objects.equals(NewCellValue, selectedCell.getOriginalValue())){
                eventBus.publish(new CellUpdateEvent(selectedCell.getCoordinate(), NewCellValue));
            }
        }
        catch(Exception e){

            showAlert("Failed to update cell", e.getMessage());
        }
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void addNewRangeButtonListener(ActionEvent event)
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
            //popupStage.initOwner(((MenuItem) event.getSource()).getParentPopup().getScene().getWindow());  // Set the owner to the current stage
            popupStage.setScene(new Scene(popupRoot, 200, 150));

            // Show the popup
            popupStage.showAndWait();
        }
        catch (Exception e)
        {
            e.printStackTrace();  // Handle exceptions appropriately
        }
    }

    private void handleNewRangeCreated(RangeCreatedEvent event)
    {
        rangesList.getItems().add(event.getRangeName());
    }

    @FXML
    void pressListViewTest(MouseEvent event)
    {
        String selectedRange = (String)rangesList.getSelectionModel().getSelectedItem();
        eventBus.publish(new RangeSelectedEvent(selectedRange));
    }

}
