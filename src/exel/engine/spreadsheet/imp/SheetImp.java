package exel.engine.spreadsheet.imp;

import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;

import java.util.HashMap;
import java.util.Map;

public class SheetImp implements Sheet {

    private Map<String, Cell> activeCells;

    public SheetImp() {
        this.activeCells = new HashMap<>();
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public Cell getCell(String coordinate) {
        return activeCells.get(coordinate);
    }

    @Override
    public void setCell(String coordinate, String value) {
        Cell cell = activeCells.get(coordinate);
        // if null need to create the cell...

        cell.setCellOriginalValue(value);
    }
}
