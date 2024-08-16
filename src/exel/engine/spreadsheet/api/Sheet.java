package exel.engine.spreadsheet.api;

import exel.engine.spreadsheet.cell.api.Cell;

public interface Sheet {
    int getVersion();
    Cell getCell(String coordinate);
    void setCell(String coordinate, String value);
}
