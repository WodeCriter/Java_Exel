package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.cell.api.Cell;

import java.util.*;

class Row implements Comparable<Row>
{
    private Map<Integer, Cell> colNumToCellMap;
    private List<Integer> colsToSortFrom;
    private int rowNum;

    public Row(List<Cell> cells, List<Integer> colsToSortFrom)
    {
        this.colNumToCellMap = new HashMap<>();
        cells.forEach(cell -> this.colNumToCellMap.put(cell.getCoordinate().getColIndex(), cell));
        this.rowNum = cells.getFirst().getCoordinate().getRow();

        //todo: What if a col is given not in range (might fix elsewhere).
        this.colsToSortFrom = colsToSortFrom;
    }

    @Override
    public int compareTo(Row o)
    {
        int compare;
        for (Integer col : colsToSortFrom)
        {
            Cell thisCell = colNumToCellMap.get(col);
            Cell otherCell = o.colNumToCellMap.get(col);

            if (thisCell == null && otherCell == null)
                compare = 0;
            else if (thisCell == null)
                compare = 1;
            else if (otherCell == null)
                compare = -1;
            else
                compare = thisCell.compareTo(otherCell);

            if (compare != 0)
                return compare;
        }

        return 0;
    }

    public void changeRowNum(int newRowNum)
    {
        if (rowNum != newRowNum)
        {
            colNumToCellMap.values().forEach(cell -> cell.setCoordinateRowNum(newRowNum));
            rowNum = newRowNum;
        }
    }

    public int getRowNum()
    {
        return rowNum;
    }
}
