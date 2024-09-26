package exel.userinterface.resources.app;


import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.eventsys.events.*;
import exel.userinterface.resources.app.popups.newRange.CreateNewRangeScreenController;
import exel.userinterface.resources.app.popups.sort.SetSortScreenController;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import exel.eventsys.EventBus;
import exel.userinterface.resources.app.popups.newsheet.CreateNewSheetScreenController;
import exel.userinterface.resources.app.Sheet.SheetController;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class IndexController {

    private ReadOnlyCell selectedCell;
    private EventBus eventBus;
    private boolean wasSheetCreated = false;

    @FXML
    private MenuItem buttonNewFile;

    @FXML
    private MenuItem buttonLoadFile;

    @FXML
    private MenuItem buttonSaveFile;

    @FXML
    private MenuItem buttonSaveAsFile;

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
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((MenuItem) event.getSource()).getParentPopup().getScene().getWindow());  // Set the owner to the current stage
            popupStage.setScene(new Scene(popupRoot, 200, 150));

            // Show the popup
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();  // Handle exceptions appropriately
        }
    }

    @FXML
    void loadFileListener(ActionEvent event) {
        // Create a new FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set the title of the dialog
        fileChooser.setTitle("Open Spreadsheet File");

        // (Optional) Set initial directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        //Add file extension filters
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Xml Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // **Retrieve the owner window from a node in the scene**
        // Using 'sheetContainer' which is part of the scene graph
        Window ownerWindow = sheetContainer.getScene().getWindow();

        // Show the open file dialog
        File selectedFile = fileChooser.showOpenDialog(ownerWindow);

        if (selectedFile != null) {
            // Get the absolute path as a String
            String absolutePath = selectedFile.getAbsolutePath();

            try {
                // **Pass the absolute path to your engine**
                //Todo: handle file load

                // Optionally, notify the user that the file was loaded successfully
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("File Loaded");
                alert.setHeaderText(null);
                alert.setContentText("Successfully loaded file: " + selectedFile.getName());
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                // Show an error alert to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File Load Error");
                alert.setHeaderText("Could not load the file");
                alert.setContentText("An error occurred while loading the file: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            //System.out.println("File selection cancelled by user.");
        }

    }


    @FXML
    void saveAsFileListener(ActionEvent event) {

    }

    @FXML
    void saveFileListener(ActionEvent event) {

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
        if (!wasSheetCreated || selectedCell == null)
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
            popupStage.initModality(Modality.APPLICATION_MODAL);
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
        if (selectedRange == null)
            return;

        if (event.getButton() == MouseButton.SECONDARY)
            rangeDeleteMenu.show(rangesList, event.getScreenX(), event.getScreenY());
        else
            eventBus.publish(new RangeSelectedEvent(selectedRange));
    }

    private void setUpRangeDeleteMenu()
    {
        rangeDeleteMenu = new ContextMenu();
        MenuItem deleteRange = new MenuItem("Delete Range");


        deleteRange.setOnAction(event -> {
            String selectedRange = (String)rangesList.getSelectionModel().getSelectedItem();
            eventBus.publish(new RangeDeleteEvent(selectedRange));
        });

        rangeDeleteMenu.getItems().add(deleteRange);
    }

    private void hideContextMenu(MouseEvent event) {
        // If the context menu is open and the click happens outside the context menu, hide it
        if (rangeDeleteMenu.isShowing()) {
            rangeDeleteMenu.hide();
        }
    }

    @FXML
    void sortRangeListener(ActionEvent event)
    {
        try {
            // Load the FXML file for the new sheet popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/exel/userinterface/resources/app/popups/sort/SetSortScreen.fxml"));
            VBox popupRoot = loader.load();

            Object controller = loader.getController();

            if (controller instanceof SetSortScreenController)
                ((SetSortScreenController) controller).setEventBus(eventBus);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Sort Range");
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
