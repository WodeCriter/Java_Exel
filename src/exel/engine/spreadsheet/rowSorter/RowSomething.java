package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.range.Range;

import java.util.*;
import java.util.stream.Collectors;

public abstract class RowSomething
{
    private final Range range;
    private final List<Row> rows;
    private final List<Integer> colsToOperateOn;
    private int minRowNum, maxRowNum;
    private Sheet sheet;

    public RowSomething(Range range, Sheet sheet, int... colsToSortFrom)
    {
        this(range, sheet, Arrays.stream(colsToSortFrom).boxed().collect(Collectors.toCollection(LinkedList::new)));
    }

    public RowSomething(Range range, Sheet sheet, List<Integer> colsToSortFrom)
    {
        this.range = range;
        this.colsToOperateOn = colsToSortFrom;
        findMinAndMaxRowNum();
        this.rows = convertCellsListToRowsList();
        this.sheet = sheet;
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

    protected List<Integer> getColsToOperateOn() {
        return colsToOperateOn;
    }

    protected int getMinRowNum(){
        return minRowNum;
    }

    protected int getMaxRowNum(){
        return maxRowNum;
    }

    protected Sheet getSheet()
    {
        return sheet;
    }

    private List<Row> convertCellsListToRowsList(List<Cell> cells)
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
                rowList.add(new Row(cellsInRow, colsToOperateOn));
        }

        return rowList;
    }

    private List<Row> convertCellsListToRowsList()
    {
        return convertCellsListToRowsList(range.getCellsInRange());
    }

    public abstract void changeSheet();
    public abstract void returnSheetBackToNormal();
    public abstract ReadOnlySheet getSheetAfterChange();
}
