package exel.engine.expressions.imp.Math;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.cell.api.CellType;

public class PlusExpression implements Expression {
    private Expression left;
    private Expression right;

    public PlusExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public EffectiveValue eval() {
        EffectiveValue leftValue = left.eval();
        EffectiveValue rightValue = right.eval();

        if (leftValue.getCellType() != CellType.NUMERIC || rightValue.getCellType() != CellType.NUMERIC )
            return null; //should throw exception

        double result = leftValue.extractValueWithExpectation(Double.class) + rightValue.extractValueWithExpectation(Double.class);

        return new EffectiveValueImp(CellType.NUMERIC, result);
    }
}
