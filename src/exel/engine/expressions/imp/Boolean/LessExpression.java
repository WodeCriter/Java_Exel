package exel.engine.expressions.imp.Boolean;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.CellType;

public class LessExpression implements Expression {
    private Expression arg1;
    private Expression arg2;

    public LessExpression(Expression arg1, Expression arg2)
    {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public EffectiveValue eval(Sheet sheet)
    {
        return new BiggerExpression(arg2, arg1).eval(sheet);
    }

    @Override
    public CellType getFunctionResultType()
    {
        return CellType.BOOLEAN;
    }
}
