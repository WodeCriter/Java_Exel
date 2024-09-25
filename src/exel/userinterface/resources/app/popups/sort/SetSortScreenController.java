package exel.userinterface.resources.app.popups.sort;

import exel.engine.imp.EngineImp;
import exel.eventsys.EventBus;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;

import java.util.*;

public class SetSortScreenController
{
    private EventBus eventBus;

    @FXML
    private GridPane gridPane;
    @FXML
    private TextField cell1TextField, cell2TextField;
    @FXML
    private ComboBox<String> mainColumnComboBox;
    @FXML
    private Label sortByLabel;

    private List<String> possibleColumnChoices = null;
    //private List<String> pickedColumns;
    private List<ComboBox<String>> allComboBoxes;
    private Map<ComboBox<String>, String> boxToChosenColumn;
    private int rowIndex = 4;  // Track the current row index for new rows

    @FXML
    private void initialize()
    {
        //pickedColumns = new ArrayList<>(5);
        allComboBoxes = new ArrayList<>(5);
        allComboBoxes.add(mainColumnComboBox);
        boxToChosenColumn = new HashMap<>();
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
        ComboBox<String> newComboBox = new ComboBox<>();
        //Label choiceBoxTitle = new Label("Column");
        gridPane.addRow(rowIndex, guideLabel, newComboBox);

        newComboBox.setPrefWidth(mainColumnComboBox.getPrefWidth());
        newComboBox.setPrefHeight(mainColumnComboBox.getPrefHeight());
        guideLabel.setPrefWidth(sortByLabel.getPrefWidth());
        guideLabel.setPrefHeight(sortByLabel.getPrefHeight());
        guideLabel.setAlignment(Pos.CENTER);

//        if (possibleColumnChoices != null)
//            newComboBox.getItems().addAll(possibleColumnChoices);
        newComboBox.setOnAction(this::whenPickingAColumn);
        allComboBoxes.add(newComboBox);

//        // Get the current stage and resize the window to fit new content
//        Stage stage = (Stage) gridPane.getScene().getWindow();
//        stage.sizeToScene();  // Resize window to fit the new content

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
    public void whenClickingOnComboBox(MouseEvent event)
    {
        if (possibleColumnChoices == null)
            possibleColumnChoices = EngineImp.getAllColumnsBetween2Cords(cell1TextField.getText(), cell2TextField.getText());
        if (possibleColumnChoices != null)
            mainColumnComboBox.getItems().addAll(possibleColumnChoices);
    }
    @FXML
    private void whenPickingAColumn(ActionEvent event)
    {
        ComboBox<String> chosenComboBox = (ComboBox<String>) event.getSource();
        String previousPickedColumn = boxToChosenColumn.get(chosenComboBox);

        if (previousPickedColumn == null)
        {
            boxToChosenColumn.put(chosenComboBox, chosenComboBox.getValue());
            ListIterator<ComboBox<String>> iterator = getIteratorPointingAtNextBoxInList(chosenComboBox);
            if (iterator != null && iterator.hasNext())
            {
                List<String> choicesWithoutPickedChoice = chosenComboBox.getItems().stream().
                        filter(column->!column.equals(chosenComboBox.getValue())).toList();

                ComboBox<String> nextBox = iterator.next();
                nextBox.setValue(null);

                nextBox.getItems().clear();
                nextBox.getItems().addAll(choicesWithoutPickedChoice);

                while (iterator.hasNext())
                {
                    nextBox = iterator.next();
                    nextBox.setValue(null);
                    nextBox.getItems().clear();
                }
            }
        }
        else
        {
            boxToChosenColumn.put(chosenComboBox, chosenComboBox.getValue());
            ListIterator<ComboBox<String>> iterator = getIteratorPointingAtNextBoxInList(chosenComboBox);
            if (iterator != null && iterator.hasNext())
            {
                List<String> choicesWithoutPickedChoice = new LinkedList<>();
                choicesWithoutPickedChoice.add(previousPickedColumn);
                choicesWithoutPickedChoice.addAll(chosenComboBox.getItems().stream().
                        filter(column->!column.equals(chosenComboBox.getValue())).toList());

                iterator.next().getItems().addAll(choicesWithoutPickedChoice);
                while (iterator.hasNext())
                {
                    ComboBox<String> nextBox = iterator.next();
                    nextBox.setValue(null);
                    nextBox.getItems().clear();
                }
            }
            //Need to go to the next box, put back the previousPickedColumn in it, and remove chosenComboBox.getValue()
        }
    }

    private ListIterator<ComboBox<String>> getIteratorPointingAtNextBoxInList(ComboBox<String> box)
    {
        ListIterator<ComboBox<String>> iterator = allComboBoxes.listIterator();

        while (iterator.hasNext())
        {
            if (iterator.next() == box)
                return iterator;
        }

        return null;
    }
}
