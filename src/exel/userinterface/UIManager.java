package exel.userinterface;

import exel.engine.api.Engine;
import exel.engine.imp.EngineImp;
import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.eventsys.EventBus;
import exel.eventsys.events.*;
import exel.userinterface.resources.app.IndexController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;

public class UIManager {

    private Engine engine;
    private EventBus eventBus;
    private ReadOnlySheet readOnlySheet;
    private IndexController indexController;

    public UIManager(Engine engine, EventBus eventBus) {
        this.engine = engine;
        this.eventBus = eventBus;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        // Subscribe to the CreateNewSheetEvent
        eventBus.subscribe(CreateNewSheetEvent.class, this::handleCreateNewSheet);
        eventBus.subscribe(CellSelectedEvent.class, this::handleCellSelected);
        eventBus.subscribe(CellUpdateEvent.class, this::handleCellUpdate);
        eventBus.subscribe(CreateNewRangeEvent.class, this::handleCreateNewRange);
        eventBus.subscribe(RangeSelectedEvent.class, this::handleRangeSelected);
        eventBus.subscribe(RangeDeleteEvent.class, this::handleRangeDelete);
        eventBus.subscribe(SortRequestedEvent.class, this::handleSortRequested);
        eventBus.subscribe(LoadSheetEvent.class, this::handleLoadSheet);
    }

    private void handleCreateNewSheet(CreateNewSheetEvent event) {
        // Call the engine to create a new sheet based on the event details
        readOnlySheet = engine.createSheet(event.getSheetName(), event.getRows(), event.getCols(), event.getWidth(), event.getHeight());
        indexController.refreshSheetPlane();
        eventBus.publish(new SheetCreatedEvent(
                readOnlySheet.getName(),
                readOnlySheet.getCellHeight(),
                readOnlySheet.getCellWidth(),
                readOnlySheet.getNumOfRows(),
                readOnlySheet.getNumOfCols()));

        //System.out.println("Sheet created: " + event.getSheetName());
    }

    private void handleLoadSheet(LoadSheetEvent event){
        try
        {
            readOnlySheet = engine.loadSheet(event.getFilePath());
            indexController.refreshSheetPlane();
            eventBus.publish(new SheetCreatedEvent(
                    readOnlySheet.getName(),
                    readOnlySheet.getCellHeight(),
                    readOnlySheet.getCellWidth(),
                    readOnlySheet.getNumOfRows(),
                    readOnlySheet.getNumOfCols()));

            eventBus.publish(new SheetDisplayEvent(readOnlySheet));
            //todo: add for readOnlySheet a readonlyRange (which includes the top left and bottom cell cords, and the rangeName)



        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    private void handleCreateNewRange(CreateNewRangeEvent event) {
        engine.addNewRange(event.getRangeName(), event.getTopLeftCord(), event.getBottomRightCord());
        eventBus.publish(new RangeCreatedEvent(event.getRangeName(), event.getTopLeftCord(), event.getBottomRightCord()));
    }

    private void handleCellSelected(CellSelectedEvent event) {
        // Call the engine to create a new sheet based on the event details
        ReadOnlyCell cell = engine.getCellContents(event.getCellId());
        eventBus.publish(new DisplaySelectedCellEvent(cell));
    }

    private void handleCellUpdate(CellUpdateEvent event) {
        try
        {
            engine.updateCellContents(event.getCoordinate(), event.getOriginalValue());
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
        ReadOnlySheet updatedSheet = engine.getSheet();
        eventBus.publish(new SheetDisplayEvent(updatedSheet));
    }

    private void handleRangeSelected(RangeSelectedEvent event)
    {
        List<String> cords = engine.getCordsOfCellsInRange(event.getRangeName());
        eventBus.publish(new CellsRequestedToBeMarkedEvent(cords));
    }

    private void handleRangeDelete(RangeDeleteEvent event)
    {
        engine.deleteRange(event.getRangeName());
        eventBus.publish(new DeletedRangeEvent(event.getRangeName()));
    }

    private void handleSortRequested(SortRequestedEvent event)
    {
        ReadOnlySheet sortedSheet = engine.createSortedSheetFromCords(event.getCord1(), event.getCord2(), event.getPickedColumns());
        eventBus.publish(new SheetDisplayEvent(sortedSheet));
    }

    public void showUI(Stage primaryStage) {
        try {
            // Assuming the FXML file is named "MainScreen.fxml" and is located in the "app" directory under resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/exel/userinterface/resources/app/index.fxml"));

            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof IndexController) {
                ((IndexController) controller).setEventBus(eventBus);
                this.indexController = (IndexController) controller;
            }
            // Setting the title of the stage (optional)
            primaryStage.setTitle("Shticell");
            primaryStage.getIcons().add(new Image("file:photos\\icons\\default.png"));

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