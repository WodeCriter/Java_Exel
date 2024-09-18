package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.cell.api.Cell;

import java.util.*;
import java.util.stream.Collectors;

public class RowSorter
{
    private final List<Row> rows;
    private final List<Integer> colsToSortFrom;
    private final List<Integer> originalRowOrder;

    private int minRowNum, maxRowNum;

    public RowSorter(List<Cell> cells, int... colsToSortFrom)
    {
        this.colsToSortFrom = Arrays.stream(colsToSortFrom).boxed().collect(Collectors.toCollection(LinkedList::new));
        findMinAndMaxRowNum(cells);
        this.rows = sortCellsToRows(cells);

        this.originalRowOrder = new LinkedList<>();
        rows.forEach(row->originalRowOrder.add(row.getRowNum()));
        sortRows();
    }

    private void findMinAndMaxRowNum(List<Cell> cells)
    {
        int min = Integer.MAX_VALUE, max = 0;
        for (Cell cell : cells)
        {
            int rowNum = cell.getCoordinate().getRow();
            if (rowNum < min) min = rowNum;
            if (rowNum > max) max = rowNum;
        }
        minRowNum = min;
        maxRowNum = max;
    }

    private List<Row> sortCellsToRows(List<Cell> cells)
    {
        Map<Integer, List<Cell>> fromRowNumToListOfCellsInRow = new HashMap<>();

        for (Cell cell : cells)
        {
            int rowNum = cell.getCoordinate().getRow();
            fromRowNumToListOfCellsInRow.computeIfAbsent(rowNum, k -> new LinkedList<>()).add(cell);
        }

        List<Row> rowList = new ArrayList<>(maxRowNum - minRowNum + 1);
        for (int row = minRowNum; row <= maxRowNum; row++)
        {
            List<Cell> cellsInRow = fromRowNumToListOfCellsInRow.get(row);

            if (cellsInRow != null)
                rowList.add(new Row(cellsInRow, colsToSortFrom));
        }

        return rowList;
    }

    private void sortRows()
    {
        rows.sort(Row::compareTo);
        fixCellCoordinatesAfterSort();
    }

    private void fixCellCoordinatesAfterSort()
    {
        int rowNum = minRowNum;
        for (Row row : rows)
        {
            row.changeRowNum(rowNum);
            rowNum++;
        }
    }

    public void moveCellsToOriginalCoordinates()
    {
        ListIterator<Integer> iterator = originalRowOrder.listIterator();
        rows.forEach(row -> row.changeRowNum(iterator.next()));
    }
}
