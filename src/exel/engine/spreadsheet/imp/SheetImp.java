package exel.engine.spreadsheet.imp;

import exel.engine.expressions.imp.FunctionParser;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.imp.CellImp;

import java.io.*;
import java.util.*;

public class SheetImp implements Sheet
{
    private Map<String, CellImp> activeCells;
    private String sheetName;
    private int version;
    private int cellHeight;
    private int cellWidth;
    private int numOfCols;
    private int numOfRows;


    public SheetImp(int cellHeight, int cellWidth, int numOfCols, int numOfRows, String sheetName)
    {
        this.activeCells = new HashMap<>();
        this.sheetName = sheetName;
        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
        this.numOfCols = numOfCols;
        this.numOfRows = numOfRows;
        this.version = 1;
    }

    @Override
    public String getName()
    {
        return sheetName;
    }

    @Override
    public List<Cell> getCells()
    {
        return new ArrayList<>(activeCells.values());
    }

    @Override
    public int getVersion()
    {
        return version;
    }

    @Override
    public CellImp getCell(String coordinate)
    {
        return activeCells.get(coordinate);
    }

    @Override
    public void setCell(String coordinate, String value) throws IllegalArgumentException
    {
        if (isCoordinateInRange(coordinate)){
            Cell cell = activeCells.computeIfAbsent(coordinate, s -> new CellImp(s, this));
            cell.setCellOriginalValue(value);
        }
        else {
            throw new IllegalArgumentException("Cell Coordinate outside of range");
        }
    }

    public int getCellHeight()
    {
        return cellHeight;
    }

    public int getCellWidth()
    {
        return cellWidth;
    }

    public int getNumOfCols()
    {
        return numOfCols;
    }

    public int getNumOfRows()
    {
        return numOfRows;
    }

    private enum Color
    {
        WHITE, BLACK, GREY
    }

    public Sheet updateCellValueAndCalculate(String coordinate, String newValue)
    {
        SheetImp copySheet = copySheet();
        copySheet.version++;
        copySheet.setCell(coordinate, newValue);//update the cell in the copy sheet
        Cell cell = copySheet.activeCells.get(coordinate);
        if (cell == null)
            throw new IllegalArgumentException("Cell " + coordinate + " not found in map after set cell");
        cell.updateDependencies();

        try
        {
            List<Cell> orderedCells =  orderCellsForCalculation(cell); //todo: figure out what to do here, when circle is found.
            for (Cell orderedCell : orderedCells)
            {
                orderedCell.calculateEffectiveValue();
            }
            return copySheet;
        }
        catch (Exception ex)
        {
            return this;
        }

    }

    private List<Cell> orderCellsForCalculation(Cell startingCell)
    {
        List<Cell> orderedCells = new LinkedList<>();
        Map<Cell, Color> coloredCells = new HashMap<>();

        for (Cell cell : activeCells.values())
            coloredCells.put(cell, Color.WHITE);

        orderCellsForCalculationHelper(startingCell, coloredCells, orderedCells);
        return orderedCells;
    }

    private void orderCellsForCalculationHelper(Cell cell, Map<Cell, Color> coloredCells, List<Cell> orderedCells)
    {
        coloredCells.put(cell, Color.GREY);

        for (Cell dependentCell : cell.getInfluencingOn())
        {
            switch (coloredCells.get(dependentCell))
            {
                case WHITE:
                    orderCellsForCalculationHelper(dependentCell, coloredCells, orderedCells);
                case GREY:
                    throw new IllegalArgumentException("Cells are not allowed to influence each other.");
            }
        }

        coloredCells.put(cell, Color.BLACK);
        orderedCells.addFirst(cell);
    }

    private SheetImp copySheet() {
        // lots of options here:
        // 1. implement clone all the way (yac... !)
        // 2. implement copy constructor for CellImpl and SheetImpl

        // 3. how about serialization ?
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return (SheetImp) ois.readObject();
        } catch (Exception e) {
            // deal with the runtime error that was discovered as part of invocation
            return this;
        }
    }

    private Boolean isCoordinateInRange(String coordinate) {
        if (coordinate == null || coordinate.isEmpty()) {
            return false; // Early return for null or empty string input.
        }

        // Separate the column letter(s) from the row number.
        int i = 0;
        while (i < coordinate.length() && Character.isLetter(coordinate.charAt(i))) {
            i++;
        }

        // Split the string into the alphabetic part and the numeric part.
        String columnPart = coordinate.substring(0, i);
        String rowPart = coordinate.substring(i);

        // Convert the column letters to a column index (0-based).
        int column = 0;
        for (int j = 0; j < columnPart.length(); j++) {
            column = column * 26 + (Character.toUpperCase(columnPart.charAt(j)) - 'A' + 1);
        }

        // Convert the row string to an integer.
        int row;
        try {
            row = Integer.parseInt(rowPart);
        } catch (NumberFormatException e) {
            return false; // Return false if row part is not an integer.
        }

        // Check if the column index and row index are within the allowed range.
        return column > 0 && column <= this.numOfCols && row > 0 && row <= this.numOfRows;
    }
}


