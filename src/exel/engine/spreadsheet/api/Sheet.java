package exel.engine.spreadsheet.api;

import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.engine.spreadsheet.cell.imp.CellImp;
import exel.engine.spreadsheet.coordinate.imp.Coordinate;

import java.util.List;

public interface Sheet {
    int getVersion();
    Cell getCell(String coordinate);
    Cell getCell(Coordinate coordinate);

    Cell setCell(String coordinate, String value) throws IllegalArgumentException;
    List<Cell> getCells();
    String getName();
    int getNumOfCols();
    int getNumOfRows();
    int getCellWidth();

    Cell setCell(Coordinate coordinate, String value) throws IllegalArgumentException;

    int getCellHeight();
    Sheet updateCellValueAndCalculate(String coordinate, String newValue);
    Sheet updateCellValueAndCalculate(Coordinate coordinate, String newValue);
    Sheet getSheetByVersion(int version);
    List<Integer> getNumOfChangesInEachVersion();
    List<ReadOnlyCell> getReadOnlyCells();
}
