package exel.engine.spreadsheet.rowSorter;

import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.imp.ReadOnlySheetImp;
import exel.engine.spreadsheet.range.Range;

import java.util.*;
import java.util.stream.Collectors;

public class RowSorter extends RowSomething
{
    public RowSorter(Range range, Sheet sheet, int... colsToSortFrom)
    {
        this(range, sheet, Arrays.stream(colsToSortFrom).boxed().collect(Collectors.toCollection(LinkedList::new)));
    }

    public RowSorter(Range range, Sheet sheet, List<Integer> colsToSortFrom)
    {
        super(range, sheet, colsToSortFrom);
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
    public void changeSheet() //sorts
    {
        getRows().sort(Row::compareTo);
        fixCellCoordinatesAfterSort();
    }

    @Override
    public void returnSheetBackToNormal()
    {
        getRows().forEach(Row::setBackToOriginalRowNum);
    }

    @Override
    public ReadOnlySheet getSheetAfterChange()
    {
        changeSheet();
        ReadOnlySheet toReturn = new ReadOnlySheetImp(getSheet());
        returnSheetBackToNormal();
        return toReturn;
    }
}
