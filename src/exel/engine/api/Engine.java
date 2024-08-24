package exel.engine.api;

import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;

import java.util.List;

public interface Engine {
    /**
     * Creates a new spreadsheet.
     */
    void createSheet(String sheetName, int cellHeight, int cellWidth,int numOfCols , int numOfRows);

    /**
     * Loads a spreadsheet from an XML file.
     * @param filePath The path to the XML file.
     * @throws Exception if there is an issue loading the file.
     */
    void loadSheet(String filePath) throws Exception;

    /**
     * Retrieves the contents of the entire sheet for display purposes.
     * @return A list of lists representing the spreadsheet, where each inner list is a row of cell values.
     *
     */
    ReadOnlySheet getSheet();

    /**
     * Retrieves the contents of a specific cell.
     * @param cellCoordinate The column letter of the cell.
     * @return The contents of the specified cell.
     */
    ReadOnlyCell getCellContents(String cellCoordinate);

    /**
     * Updates the contents of a specific cell.
     * @param coordinate The coordinate of the cell.
     * @param value The new value for the cell.
     * @throws Exception if there is an issue with updating the cell
     */
    void updateCellContents(String coordinate, String value) throws Exception;

    /**
     * Retrieves the version of the current spreadsheet.
     * @return The version as a String.
     */
    String getVersion();

    /**
     * Saves the current spreadsheet to a file.
     * @param filePath The path where the file should be saved.
     * @throws Exception if there is an issue saving the file.
     */
    void saveFile(String filePath) throws Exception;

    // Method to check if the engine has a loaded sheet
    boolean hasSheet();

    // Method to clear the current sheet
    void clearSheet();
}