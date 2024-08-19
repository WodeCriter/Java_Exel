package exel.engine.spreadsheet.api;

import exel.engine.spreadsheet.cell.api.Cell;

import java.util.List;

public interface Sheet {
    int getVersion();
    Cell getCell(String coordinate);
    void setCell(String coordinate, String value);
    List<Cell> getCells();
    String getName();
    int getNumOfCols();
    int getNumOfRows();
    int getCellWidth();
    int getCellHeight();
}
