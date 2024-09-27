package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.range.Range;

import java.util.*;
import java.util.stream.Collectors;

public abstract class RowSomething
{
    private final Range range;
    private final List<Row> rows;
    private final List<Integer> colsToSortFrom;
    private int minRowNum, maxRowNum;

    public RowSomething(Range range, int... colsToSortFrom)
    {
        this(range, Arrays.stream(colsToSortFrom).boxed().collect(Collectors.toCollection(LinkedList::new)));
    }

    public RowSomething(Range range, List<Integer> colsToSortFrom)
    {
        this.range = range;
        this.colsToSortFrom = colsToSortFrom;
        findMinAndMaxRowNum();
        this.rows = sortCellsToRows();
    }

    private void findMinAndMaxRowNum()
    {
        minRowNum = getRange().getTopLeft().getRow();
        maxRowNum = getRange().getBottomRight().getRow();
    }

    protected Range getRange(){
        return range;
    }

    protected List<Row> getRows() {
        return rows;
    }

    protected int getMinRowNum(){
        return minRowNum;
    }

    protected int getMaxRowNum(){
        return maxRowNum;
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

    public abstract void sortRows(); //abstract
    public abstract void moveCellsToOriginalCoordinates(); //abstract
}
