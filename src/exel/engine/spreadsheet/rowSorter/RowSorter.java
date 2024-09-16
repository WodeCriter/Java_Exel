package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.cell.api.Cell;

import java.util.*;
import java.util.stream.Collectors;

public class RowSorter
{
    private List<Row> rows;
    private final List<Integer> colsToSortFrom;

    public RowSorter(List<Cell> cells, int... colsToSortFrom)
    {
        this.colsToSortFrom = Arrays.stream(colsToSortFrom).boxed().collect(Collectors.toCollection(LinkedList::new));
        this.rows = sortCellsToRows(cells);
    }

    private List<Row> sortCellsToRows(List<Cell> cells)
    {
        Map<Integer, List<Cell>> rowToCellsInRowMap = new HashMap<>();
        int numOfRows = -1;

        for (Cell cell : cells)
        {
            int rowNum = cell.getCoordinate().getRow();
            if (rowNum > numOfRows)
                numOfRows = rowNum;

            rowToCellsInRowMap.computeIfAbsent(rowNum, k -> new ArrayList<>()).add(cell);
        }

        List<Row> rowList = new ArrayList<>(numOfRows);
        for (int row = 1; row <= numOfRows; row++)
        {
            List<Cell> cellsInRow = rowToCellsInRowMap.get(row);

            if (cellsInRow != null)
                rowList.add(new Row(cellsInRow, colsToSortFrom));
        }

        return rowList;
    }
}
