package exel.engine.spreadsheet.imp;

import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.imp.CellImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetImp implements Sheet {

    private Map<String, Cell> activeCells;
    private String sheetName;
    private int cellHeight;
    private int cellWidth;
    private int numOfCols;
    private int numOfRows;


    public SheetImp(int cellHeight, int cellWidth, int numOfCols, int numOfRows, String sheetName) {
        this.activeCells = new HashMap<>();
        this.sheetName = sheetName;
        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
        this.numOfCols = numOfCols;
        this.numOfRows = numOfRows;
    }

    @Override
    public String getName(){
        return sheetName;
    }

    @Override
    public List<Cell> getCells() {
        return new ArrayList<>(activeCells.values());
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
        Cell cell = activeCells.computeIfAbsent(coordinate, CellImp::new);
        cell.setCellOriginalValue(value);
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getNumOfCols() {
        return numOfCols;
    }

    public int getNumOfRows() {
        return numOfRows;
    }
}
