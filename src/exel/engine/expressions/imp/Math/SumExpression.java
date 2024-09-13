package exel.engine.expressions.imp.Math;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.api.CellType;
import exel.engine.spreadsheet.range.Range;

import java.util.List;

public class SumExpression implements Expression
{
    Range range;

    public SumExpression(Range range)
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

        for (Cell cell : cells)
        {
            EffectiveValue value = cell.getEffectiveValue();
            if (value.getCellType() == CellType.NUMERIC)
                sum += value.extractValueWithExpectation(Double.class);
        }

        return new EffectiveValueImp(CellType.NUMERIC, sum);
    }

    @Override
    public CellType getFunctionResultType()
    {
        return CellType.NUMERIC;
    }
}
