package exel.userinterface.resources.app.popups.sort;

import exel.engine.imp.EngineImp;
import exel.eventsys.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SetSortScreenController
{
    private EventBus eventBus;

    @FXML
    private GridPane gridPane;
    @FXML
    private TextField cell1TextField, cell2TextField;
    @FXML
    private ChoiceBox<String> columnsToChoose;
    @FXML
    private Label sortByLabel;

    private List<String> colChoices = null;
    private List<String> colsPicked;
    private int rowIndex = 4;  // Track the current row index for new rows

    @FXML
    private void initialize()
    {
        colsPicked = new LinkedList<>();
        columnsToChoose.setOnAction(event-> whenChoiceBoxClicked());
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @FXML
    public void addRow()
    {
        shiftRowsDown(rowIndex);
        // Create new content for the new row
        Label guideLabel = new Label("then by");
        ChoiceBox<String> newChoiceBox = new ChoiceBox<>();
        Label choiceBoxTitle = new Label("Column");
        gridPane.addRow(rowIndex, guideLabel, newChoiceBox);

        newChoiceBox.setPrefWidth(columnsToChoose.getPrefWidth());
        newChoiceBox.setPrefHeight(columnsToChoose.getPrefHeight());
        guideLabel.setPrefWidth(sortByLabel.getPrefWidth());
        guideLabel.setPrefHeight(sortByLabel.getPrefHeight());
        guideLabel.setAlignment(Pos.CENTER);

        newChoiceBox.getItems().addAll(colChoices);
        newChoiceBox.setOnAction(event->whenChoiceBoxClicked());

        // Get the current stage and resize the window to fit new content
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.sizeToScene();  // Resize window to fit the new content

        // Increment the rowIndex for the next row
        rowIndex++;
    }

    private void shiftRowsDown(int startRow)
    {
        // Loop through all nodes in the grid pane
        for (Node node : gridPane.getChildren()) {
            // Get the current row index of the node
            Integer row = GridPane.getRowIndex(node);
            if (row == null) {
                row = 0; // If no row index is set, it's assumed to be in row 0
            }

            // If the node is in or below the startRow, move it down one row
            if (row >= startRow) {
                GridPane.setRowIndex(node, row + 1);
            }
        }
    }

    @FXML
    void addChoices(ActionEvent event)
    {
        if (!columnsToChoose.getItems().isEmpty())
            return;

        colChoices = EngineImp.getAllColumnsBetween2Cords(cell1TextField.getText(), cell2TextField.getText());
        if (colChoices != null)
            columnsToChoose.getItems().addAll(colChoices);
    }

    private void whenChoiceBoxClicked()
    {
        colsPicked.add(columnsToChoose.getValue());
        if (colChoices != null && !colChoices.isEmpty())
            colChoices.remove(columnsToChoose.getValue());
    }
}
