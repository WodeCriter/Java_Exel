package exel.engine.spreadsheet.imp;

import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.exel.engine.spreadsheet.coordinate.Coordinate;

import java.util.List;

public class Range
{
    private Coordinate topLeft, bottomRight;
    private Sheet sheet;

    public Range(Coordinate cellCord1, Coordinate cellCord2, Sheet sheet)
    {
        //todo: Make sure range is actually inside sheet borders.
        handleInvalidCellsInput(cellCord1, cellCord2);
        this.sheet = sheet;
        topLeft = cellCord1;
        bottomRight = cellCord2;
    }

    private void handleInvalidCellsInput(Coordinate topLeftCord, Coordinate bottomRightCord)
    {
        if (topLeftCord.getRow() <= bottomRightCord.getRow())
        {
            if (topLeftCord.getColIndex() > bottomRightCord.getColIndex())
            {
                String leftCol = topLeftCord.getCol();
                topLeftCord.setCol(bottomRightCord.getCol());
                bottomRightCord.setCol(leftCol);
            }
        }
        else
        {
            if (topLeftCord.getColIndex() <= bottomRightCord.getColIndex())
            {
                int leftRow = topLeftCord.getRow();
                topLeftCord.setRow(bottomRightCord.getRow());
                bottomRightCord.setRow(leftRow);
            }
            else
            {
                Coordinate tmp = topLeftCord;
                topLeftCord = bottomRightCord;
                bottomRightCord = tmp;
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
        Coordinate iterate = topLeft;
        return null;
    }
}
