package exel.engine.spreadsheet.imp;

import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.engine.spreadsheet.cell.imp.CellImp;
import exel.engine.spreadsheet.cell.imp.ReadOnlyCellImp;
import exel.exel.engine.spreadsheet.coordinate.Coordinate;
import exel.engine.spreadsheet.versionmanager.imp.VersionManager;

import java.io.*;
import java.util.*;

public class SheetImp implements Sheet, Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<Coordinate, CellImp> activeCells;
    private String sheetName;
    private int version;
    private VersionManager versionManager;
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
        this.versionManager = new VersionManager(this.copySheet());
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
    public List<ReadOnlyCell> getReadOnlyCells() {
        List<ReadOnlyCell> readOnlyCellsList = new LinkedList<>();
        for (Cell cell : activeCells.values()) {
            ReadOnlyCell readOnlyCell = new ReadOnlyCellImp(
                    cell.getCoordinateStr(),
                    cell.getOriginalValue(),
                    cell.getEffectiveValue(),
                    cell.getVersion(),
                    cell.getDependsOn(),
                    cell.getInfluencingOn()
            );
            readOnlyCellsList.add(readOnlyCell);
        }
        return readOnlyCellsList; // return the list
    }

    @Override
    public int getVersion()
    {
        return version;
    }

    @Override
    public Sheet getSheetByVersion(int version)
    {
        return versionManager.getSheetByVersion(version);
    }

    @Override
    public List<Integer> getNumOfChangesInEachVersion()
    {
        return versionManager.getNumOfChangesInEachVersion();
    }

    public void rebase(){
       this.versionManager.setBaseSheet(this.copySheet(), this.getCells());
    }

    @Override
    public CellImp getCell(Coordinate coordinate)
    {
        CellImp cellToReturn = activeCells.get(coordinate);
        if (cellToReturn == null)
            cellToReturn = (CellImp) setCell(coordinate, "");

        return cellToReturn;
    }

    @Override
    public Cell setCell(String coordinate, String value) throws IllegalArgumentException
    {
        return setCell(new Coordinate(coordinate), value);
    }

    @Override
    public Cell setCell(Coordinate coordinate, String value) throws IllegalArgumentException
    {
        if (isCoordinateInRange(coordinate))
        {
            Cell cell = activeCells.computeIfAbsent(coordinate, cord -> new CellImp(cord, this));
            cell.setCellOriginalValue(value);
            return cell;
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

    @Override
    public Sheet updateCellValueAndCalculate(Coordinate coordinate, String newValue)
    {
        CellImp cell = getCell(coordinate);
        if (cell == null) throw new IllegalArgumentException("Cell " + coordinate + " not found in map.");
        List<Cell> orderedCells = cell.setOriginalValueIfPossible(newValue);

        version++;
        orderedCells.forEach(Cell::calculateEffectiveValue);
        orderedCells.forEach(orderedCell -> orderedCell.setVersion(version));
        versionManager.recordChanges(orderedCells);
        passVersionManager(versionManager);

        return this;
    }

    public SheetImp copySheet() {
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

    @Override
    public Boolean isCoordinateInRange(Coordinate coordinate) {
        if (coordinate == null)
            return false; // Early return for null or empty string input.

        // Split the string into the alphabetic part and the numeric part.
        int column = coordinate.getColIndex();
        int row = coordinate.getRow();

        // Check if the column index and row index are within the allowed range.
        return column > 0 && column <= this.numOfCols && row > 0 && row <= this.numOfRows;
    }

    private void passVersionManager(VersionManager newMan){
        this.versionManager = newMan;
    }
}



