package exel.engine.expressions.imp.String;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.cell.api.CellType;

public class ConcatExpression implements Expression
{
    private final String left, right;

    public ConcatExpression(String left, String right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public EffectiveValue eval()
    {
        return new EffectiveValueImp(CellType.STRING, left + right);
    }
}
