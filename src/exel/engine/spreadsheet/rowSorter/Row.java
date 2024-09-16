package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.cell.api.Cell;

import java.util.*;
import java.util.stream.Collectors;

class Row implements Comparable<Row>
{
    private Map<Integer, Cell> cells;
    private List<Integer> colsToSortFrom;

    public Row(List<Cell> cells, List<Integer> colsToSortFrom)
    {
        this.cells = new HashMap<>();
        cells.forEach(cell -> this.cells.put(cell.getCoordinate().getColIndex(), cell));

        //todo: What if a col is given not in range (might fix elsewhere).
        this.colsToSortFrom = colsToSortFrom;
    }

    @Override
    public int compareTo(Row o)
    {
        int compare;
        for (Integer col : colsToSortFrom)
        {
            Cell thisCell = cells.get(col);
            Cell otherCell = o.cells.get(col);

            if (thisCell == null && otherCell == null)
                compare = 0;
            else if (thisCell == null)
                compare = -1;
            else if (otherCell == null)
                compare = 1;
            else
                compare = thisCell.compareTo(otherCell);

            if (compare != 0)
                return compare;
        }

        return 0;
    }
}
