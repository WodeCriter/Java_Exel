package exel.userinterface.resources.app.Sheet;


import exel.eventsys.EventBus;
import exel.eventsys.events.SheetCreatedEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.HashMap;
import java.util.Map;

public class SheetController {

    @FXML
    private GridPane spreadsheetGrid;

    private EventBus eventBus;

    // Map to store cell labels with their coordinates
    private Map<String, Label> cellLabelMap = new HashMap<>();

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        eventBus.subscribe(SheetCreatedEvent.class, this::handleSheetCreated);
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
                label.setStyle("-fx-font-weight: bold;");
                spreadsheetGrid.add(label, col, 0); // Column headers at row 0
            }

            // Create row headers (1, 2, 3, ...)
            for (int row = 1; row <= numRows; row++) {
                Label label = new Label(String.valueOf(row));
                label.setStyle("-fx-font-weight: bold;");
                spreadsheetGrid.add(label, 0, row); // Row headers at column 0
            }

            // Create cells
            for (int row = 1; row <= numRows; row++) {
                for (int col = 1; col <= numCols; col++) {
                    Label cellLabel = new Label();
                    cellLabel.setPrefWidth(event.getCellWidth());
                    cellLabel.setPrefHeight(event.getCellHeight());
                    cellLabel.setStyle("-fx-border-color: lightgrey;");
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
    public void updateCell(int row, int col, String value) {
        Platform.runLater(() -> {
            String cellId = getCellId(row, col);
            Label cellLabel = cellLabelMap.get(cellId);
            if (cellLabel != null) {
                cellLabel.setText(value);
            }
        });
    }
}
