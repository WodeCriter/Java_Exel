package exel.userinterface.resources.app;


import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.eventsys.events.*;
import exel.userinterface.resources.app.popups.newRange.CreateNewRangeScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
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
    private boolean wasSheetCreated = false;

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
    private ContextMenu rangeDeleteMenu;

    @FXML
    private void initialize()
    {
        setUpRangeDeleteMenu();

        // Add a global mouse listener to the scene to hide the context menu when clicking elsewhere
        rangesList.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, this::hideContextMenu);
            }
        });
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        eventBus.subscribe(DisplaySelectedCellEvent.class, this::handleDisplaySelectedCell);
        eventBus.subscribe(RangeCreatedEvent.class, this::handleNewRangeCreated);
        eventBus.subscribe(DeletedRangeEvent.class, this::handleRangeDeleted);
        eventBus.subscribe(SheetCreatedEvent.class, this::handleSheetCreated);
    }

    @FXML
    void newFileEventListener(ActionEvent event) {
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

    private void handleSheetCreated(SheetCreatedEvent event)
    {
        wasSheetCreated = true;
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
        if (!wasSheetCreated)
            return;

        try
        {
            String NewCellValue = textFiledOriginalVal.getText();
            if(!Objects.equals(NewCellValue, selectedCell.getOriginalValue()))
                eventBus.publish(new CellUpdateEvent(selectedCell.getCoordinate(), NewCellValue));
        }
        catch(Exception e)
        {
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
        if (!wasSheetCreated)
            return;

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

    private void handleRangeDeleted(DeletedRangeEvent event)
    {
        rangesList.getItems().remove(event.getRangeName());
    }

    @FXML
    void rangeSelectedListener(MouseEvent event)
    {
        String selectedRange = (String)rangesList.getSelectionModel().getSelectedItem();

        if (event.getButton() == MouseButton.SECONDARY)
            rangeDeleteMenu.show(rangesList, event.getScreenX(), event.getScreenY());
        else
            eventBus.publish(new RangeSelectedEvent(selectedRange));
    }

    private void setUpRangeDeleteMenu()
    {
        rangeDeleteMenu = new ContextMenu();
        MenuItem deleteRange = new MenuItem("Delete Range");

        String selectedRange = (String)rangesList.getSelectionModel().getSelectedItem();
        deleteRange.setOnAction(eventr -> {eventBus.publish(new RangeDeleteEvent(selectedRange));});

        rangeDeleteMenu.getItems().add(deleteRange);
    }

    private void hideContextMenu(MouseEvent event) {
        // If the context menu is open and the click happens outside the context menu, hide it
        if (rangeDeleteMenu.isShowing()) {
            rangeDeleteMenu.hide();
        }
    }
}
