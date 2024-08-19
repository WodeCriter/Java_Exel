package exel.engine.expressions.imp.Math;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.cell.api.CellType;

public class AbsExpression implements Expression {
    private Expression exp;

    public AbsExpression(Expression exp) {
        this.exp = exp;
    }

    @Override
    public EffectiveValue eval() {
        EffectiveValue expValue = exp.eval();

        if (expValue.getCellType() != CellType.NUMERIC)
            return null; //should throw exception

        double result = Math.abs(expValue.extractValueWithExpectation(Double.class));
        return new EffectiveValueImp(CellType.NUMERIC, result);
    }
}
