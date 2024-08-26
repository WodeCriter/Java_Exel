package exel.engine.spreadsheet.imp;

import exel.engine.expressions.imp.FunctionParser;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.imp.CellImp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class SheetImp implements Sheet
{
    private Map<String, CellImp> activeCells;
    private String sheetName;
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
        return 0;
    }

    @Override
    public CellImp getCell(String coordinate)
    {
        return activeCells.get(coordinate);
    }

    @Override
    public void setCell(String coordinate, String value)
    {
        Cell cell = activeCells.computeIfAbsent(coordinate, s -> new CellImp(s, this));
        cell.setCellOriginalValue(value);
        //cell.setUpDependsOn(this);
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

    public void updateCellValueAndCalculate(String coordinate, String newValue)
    {
        Cell cell = activeCells.get(coordinate);
        cell.setCellOriginalValue(newValue);
        List<Cell> orderedCells = orderCellsForCalculation(cell);

        for (Cell orderedCell : orderedCells)
        {
            orderedCell.calculateEffectiveValue();
        }
    }

    private List<Cell> orderCellsForCalculation(Cell startingCell)
    {
        List<Cell> orderedCells = new LinkedList<>();
        Map<Cell, Color> coloredCells = new HashMap<>();

        for (Cell cell : activeCells.values())
            coloredCells.put(cell, Color.WHITE);

        try
        {
            orderCellsForCalculationHelper(startingCell, coloredCells, orderedCells);
        }
        catch (Exception e)
        {
            throw e; //todo: figure out what to do here, when circle is found.
        }
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
}


