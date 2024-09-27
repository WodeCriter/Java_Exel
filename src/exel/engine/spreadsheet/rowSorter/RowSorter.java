package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.coordinate.Coordinate;
import exel.engine.spreadsheet.range.Range;

import java.util.*;
import java.util.stream.Collectors;

public class RowSorter extends RowSomething
{
    private final List<Integer> originalRowOrder;

    public RowSorter(Range range, int... colsToSortFrom)
    {
        this(range, Arrays.stream(colsToSortFrom).boxed().collect(Collectors.toCollection(LinkedList::new)));
    }

    public RowSorter(Range range, List<Integer> colsToSortFrom)
    {
        super(range, colsToSortFrom);
        this.originalRowOrder = new LinkedList<>();
        getRows().forEach(row->originalRowOrder.add(row.getRowNum()));
    }

    private void fixCellCoordinatesAfterSort()
    {
        int rowNum = getMinRowNum();
        for (Row row : getRows())
        {
            row.changeRowNum(rowNum);
            rowNum++;
        }
    }

    @Override
    public void sortRows()
    {
        getRows().sort(Row::compareTo);
        fixCellCoordinatesAfterSort();
    }

    @Override
    public void moveCellsToOriginalCoordinates()
    {
        ListIterator<Integer> iterator = originalRowOrder.listIterator();
        getRows().forEach(row -> row.changeRowNum(iterator.next()));
    }
}
