package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.range.Range;
import exel.engine.spreadsheet.range.RangeDatabase;

import java.util.*;
import java.util.stream.Collectors;

public class RowSorter
{
    private final Range range;
    private final List<Row> rows;
    private final List<Integer> colsToSortFrom;
    private final List<Integer> originalRowOrder;

    private int minRowNum, maxRowNum;

    private RowSorter(Range range, int... colsToSortFrom)
    {
        this.range = range;
        this.colsToSortFrom = Arrays.stream(colsToSortFrom).boxed().collect(Collectors.toCollection(LinkedList::new));
        findMinAndMaxRowNum();
        this.rows = sortCellsToRows();

        this.originalRowOrder = new LinkedList<>();
        rows.forEach(row->originalRowOrder.add(row.getRowNum()));
        sortRows();
    }

    public RowSorter(String rangeName, int... colsToSortFrom)
    {
        this(RangeDatabase.getRangeAndCountUse(rangeName), colsToSortFrom);
    }

    private void findMinAndMaxRowNum()
    {
        minRowNum = range.getTopLeft().getRow();
        maxRowNum = range.getBottomRight().getRow();
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

    private List<Row> sortCellsToRows()
    {
        return sortCellsToRows(range.getCellsInRange());
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
        range.removeUseOfRange();
    }
}
