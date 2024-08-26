package exel.engine.spreadsheet.api;

import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.imp.SheetImp;

import java.util.List;

public interface Sheet {
    int getVersion();
    Cell getCell(String coordinate);
    void setCell(String coordinate, String value) throws IllegalArgumentException;
    List<Cell> getCells();
    String getName();
    int getNumOfCols();
    int getNumOfRows();
    int getCellWidth();
    int getCellHeight();
    Sheet updateCellValueAndCalculate(String coordinate, String newValue);
}
