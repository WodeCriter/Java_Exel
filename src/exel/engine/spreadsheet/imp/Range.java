package exel.engine.spreadsheet.imp;

import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.coordinate.Coordinate;
import exel.engine.spreadsheet.coordinate.CoordinateIterator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Range
{
    private Coordinate topLeft, bottomRight;
    private Sheet sheet;

    public Range(Coordinate cellCord1, Coordinate cellCord2, Sheet sheet)
    {
        //todo: Make sure range is actually inside sheet borders.
        this.sheet = sheet;
        topLeft = cellCord1;
        bottomRight = cellCord2;
        handleInvalidCellsInput();
    }

    public int getNumOfCellsInRange()
    {
        return (bottomRight.getRow() - topLeft.getRow() + 1) * (bottomRight.getColIndex() - topLeft.getColIndex() + 1);
    }

    private void handleInvalidCellsInput()
    {
        if (topLeft.getRow() <= bottomRight.getRow())
        {
            if (topLeft.getColIndex() > bottomRight.getColIndex())
            {
                String leftCol = topLeft.getCol();
                topLeft.setCol(bottomRight.getCol());
                bottomRight.setCol(leftCol);
            }
        }
        else
        {
            if (topLeft.getColIndex() <= bottomRight.getColIndex())
            {
                int leftRow = topLeft.getRow();
                topLeft.setRow(bottomRight.getRow());
                bottomRight.setRow(leftRow);
            }
            else
            {
                Coordinate tmp = topLeft;
                topLeft = bottomRight;
                bottomRight = tmp;
            }
        }
    }

    public Boolean isCoordinateInRange(Coordinate coordinate) {
        if (coordinate == null)
            return false; // Early return for null or empty string input.

        int column = coordinate.getColIndex();
        int row = coordinate.getRow();

        // Check if the column index and row index are within the allowed range.
        return column >= topLeft.getColIndex() &&
                column <= bottomRight.getColIndex() &&
                row >= topLeft.getRow() &&
                row <= bottomRight.getRow();
    }

    public List<Cell> getCellsInRange()
    {
        if (getNumOfCellsInRange() <= sheet.getMaxNumOfCells()/2)
            return getCellsInRangeUsingIterator();
        else
            return sheet.getCells().stream().filter(cell -> isCoordinateInRange(cell.getCoordinate())).toList();
    }

    private List<Cell> getCellsInRangeUsingIterator()
    {
        List<Cell> cells = new LinkedList<>();
        CoordinateIterator iterator = new CoordinateIterator(topLeft, this);
        while (iterator.hasNext())
        {
            Coordinate coordinate = iterator.next();
            if (sheet.isCellActive(coordinate))
                cells.add(sheet.getCell(coordinate));
        }
        return cells;
    }
}
