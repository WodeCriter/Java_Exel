package exel.engine.expressions.imp.Math;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.cell.api.CellType;

public class Number implements Expression
{
    private final EffectiveValue value;
    public Number(double num)
    {
        value = new EffectiveValueImp(CellType.NUMERIC, num);
    }

    @Override
    public EffectiveValue eval()
    {
        return value;
    }
}
