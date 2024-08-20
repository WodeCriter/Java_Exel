package exel.engine.api;

import exel.engine.spreadsheet.cell.api.ReadOnlyCell;

import java.util.List;

public interface Engine {
    /**
     * Creates a new spreadsheet.
     */
    void createSheet();

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
     * @param row The row number of the cell.
     * @param column The column letter of the cell.
     * @param value The new value for the cell.
     * @throws Exception if there is an issue with updating the cell
     */
    void updateCellContents(int row, char column, String value);

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
}