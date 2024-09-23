package exel.userinterface.resources.app.Sheet;


import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.eventsys.EventBus;
import exel.eventsys.events.CellSelectedEvent;
import exel.eventsys.events.CellsRequestedToBeMarkedEvent;
import exel.eventsys.events.SheetCreatedEvent;
import exel.eventsys.events.SheetDisplayEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetController {

    private Label currentlySelectedCell = null;
    private List<String> currentlyMarkedCellCords = null;

    @FXML
    private GridPane spreadsheetGrid;

    @FXML
    private ScrollPane sheetScrollPane; // Reference to ScrollPane (optional)

    private EventBus eventBus;

    // Map to store cell labels with their coordinates
    private Map<String, Label> cellLabelMap = new HashMap<>();

    @FXML
    private void initialize()
    {
        // Add a global mouse listener to the scene to hide the context menu when clicking elsewhere
//        spreadsheetGrid.sceneProperty().addListener((obs, oldScene, newScene) -> {
//            if (newScene != null) {
//                newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, this::unmarkCellsInList);
//            }
//        });
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        eventBus.subscribe(SheetCreatedEvent.class, this::handleSheetCreated);
        eventBus.subscribe(SheetDisplayEvent.class, this::handleSheetDisplay);
        eventBus.subscribe(CellsRequestedToBeMarkedEvent.class, this::handleMarkCell);
        // Subscribe to other events if needed (e.g., cell update events)
    }

    private void handleSheetCreated(SheetCreatedEvent event) {
        Platform.runLater(() -> {
            // Clear any existing cells
            spreadsheetGrid.getChildren().clear();
            cellLabelMap.clear();

            // Build the grid based on the event's sheet details
            int numRows = event.getNumOfRows();
            int numCols = event.getNumOfCols();

            // Create column headers (A, B, C, ...)
            for (int col = 1; col <= numCols; col++) {
                Label label = new Label(String.valueOf((char) ('A' + col - 1)));
                label.getStyleClass().add("header-label");
                spreadsheetGrid.add(label, col, 0); // Column headers at row 0
            }

            // Create row headers (1, 2, 3, ...)
            for (int row = 1; row <= numRows; row++) {
                Label label = new Label(String.valueOf(row));
                label.getStyleClass().add("header-label");
                label.setMinWidth(30);
                spreadsheetGrid.add(label, 0, row); // Row headers at column 0
            }

            // Create cells
            for (int row = 1; row <= numRows; row++) {
                for (int col = 1; col <= numCols; col++) {
                    Label cellLabel = new Label();
                    cellLabel.setMinWidth(event.getCellWidth());
                    cellLabel.setMinHeight(event.getCellHeight());
                    //cellLabel.setStyle("-fx-border-color: lightgrey;");
                    cellLabel.getStyleClass().add("cell-label");
                    final int currentRow = row;
                    final int currentCol = col;

                    cellLabel.setOnMouseClicked(mouseEvent -> handleCellClick(mouseEvent, currentRow, currentCol));
                    // Add any additional cell properties or event handlers here


                    // Store the cell label with its coordinates
                    String cellId = getCellId(row, col);
                    cellLabelMap.put(cellId, cellLabel);

                    spreadsheetGrid.add(cellLabel, col, row);
                }
            }
        });
    }

    private String getCellId(int row, int col) {
        // Convert column number to letter (e.g., 1 -> A, 2 -> B)
        String columnLetter = String.valueOf((char) ('A' + col - 1));
        return columnLetter + row;
    }

    // Method to update a cell value
    private void updateCellUI(String coordinate, String value) {
        Label cellLabel = cellLabelMap.get(coordinate);
        if (cellLabel != null) {
            cellLabel.setText(value);
        } else {
            // Handle the case where the cell label is not found
            System.err.println("Cell label not found for coordinate: " + coordinate);
        }
    }

    private void handleSheetDisplay(SheetDisplayEvent event) {
        ReadOnlySheet sheet = event.getSheet();

        Platform.runLater(() -> {
            // Optionally, you might want to clear existing cells or headers here
            // depending on your application's logic.

            // Iterate through all cells in the sheet
            for (ReadOnlyCell cell : sheet.getCells()) {
                String coordinate = cell.getCoordinate();
                String value = cell.getEffectiveValue(); // Or cell.getValue() if applicable

                // Update the cell in the UI
                updateCellUI(coordinate, value);
            }

            // Optionally, update sheet metadata such as version if needed
            // For example:
            // labelSheetVersion.setText(String.valueOf(sheet.getVersion()));
        });
    }

    // Event handler for cell clicks
    private void handleCellClick(MouseEvent event, int row, int col) {
        unmarkCellsInList();
        currentlyMarkedCellCords = null;

        String cellId = getCellId(row, col);
        //System.out.println("Cell clicked: " + cellId);

        // Get the clicked cell Label from the map
        Label clickedCell = cellLabelMap.get(cellId);

        // Remove selection from the previously selected cell
        if (currentlySelectedCell != null) {
            currentlySelectedCell.getStyleClass().remove("cell-selected");
        }

        // Add selection to the clicked cell
        if (!clickedCell.getStyleClass().contains("cell-selected")) {
            clickedCell.getStyleClass().add("cell-selected");
            currentlySelectedCell = clickedCell;
        }

        // Create and publish the CellSelectedEvent
        CellSelectedEvent cellSelectedEvent = new CellSelectedEvent(cellId, row, col);
        eventBus.publish(cellSelectedEvent);
    }

    private void handleMarkCell(CellsRequestedToBeMarkedEvent event)
    {
        //todo: If 2 ranges have the same cells, pressing the other range will unmark the currently picked range. Need to fix that.
        unmarkCellsInList();
        if (event.getCellsMarkedCords().equals(currentlyMarkedCellCords))
        {
            currentlyMarkedCellCords = null;
            return;
        }

        List<String> cellsCordsToMark = event.getCellsMarkedCords();
        for (String cellId : cellsCordsToMark)
        {
            Label cellLabel = cellLabelMap.get(cellId);
            if (!cellLabel.getStyleClass().contains("cell-marked"))
                cellLabel.getStyleClass().add("cell-marked");
        }
        currentlyMarkedCellCords = cellsCordsToMark;
    }

    private void unmarkCellsInList()
    {
        if (currentlyMarkedCellCords != null)
        {
            for (String cellId : currentlyMarkedCellCords)
                cellLabelMap.get(cellId).getStyleClass().remove("cell-marked");
        }
    }
}
