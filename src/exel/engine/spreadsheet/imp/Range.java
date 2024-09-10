package exel.engine.spreadsheet.imp;

import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.imp.CellImp;

import java.util.List;

class Range
{
    private Cell topLeft, bottomRight;
    private Sheet sheet;

    public Range(String cellCord1, String cellCord2, Sheet sheet)
    {
        this(sheet.getCell(cellCord1), sheet.getCell(cellCord2), sheet);
    }

    private Range(Cell cell1, Cell cell2, Sheet sheet)
    {
        this.sheet = sheet;
        topLeft = cell1;
        bottomRight = cell2;
        flipCellsIfNeeded();
    }

    private void flipCellsIfNeeded()
    {
        if (!areCellsPositionsValid())
        {
            Cell tmp = topLeft;
            topLeft = bottomRight;
            bottomRight = tmp;
        }
    }

    //todo: make it work for longer cords
    private boolean areCellsPositionsValid()
    {
        char[] cord1 = topLeft.getCoordinate().toCharArray();
        char[] cord2 = bottomRight.getCoordinate().toCharArray();

        return cord1[0] <= cord2[0] && cord1[1] <= cord2[1];
    }

    //todo: complete this
    public List<CellImp> getCellsInRange()
    {
        return null;
    }
}
