package exel.engine.expressions.imp.Math;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.api.CellType;
import exel.engine.spreadsheet.imp.Range;

import java.util.List;

public class AverageExpression implements Expression
{
    Range range;

    public AverageExpression(Range range)
    {
        if (range == null)
            throw new IllegalArgumentException("Range cannot be null");
        this.range = range;
    }

    @Override
    public EffectiveValue eval(Sheet sheet)
    {
        List<Cell> cells = range.getCellsInRange(sheet);
        double sum = 0;
        boolean numericCellExist = false;

        for (Cell cell : cells)
        {
            EffectiveValue value = cell.getEffectiveValue();
            if (value.getCellType() == CellType.NUMERIC)
            {
                sum += value.extractValueWithExpectation(Double.class);
                numericCellExist = true;
            }
        }

        if (!numericCellExist)
            throw new IllegalArgumentException("Range does not contain cells with numeric values.");
        return new EffectiveValueImp(CellType.NUMERIC, sum/cells.size());
    }

    @Override
    public CellType getFunctionResultType()
    {
        return CellType.NUMERIC;
    }
}
