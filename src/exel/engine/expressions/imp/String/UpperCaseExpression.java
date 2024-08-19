package exel.engine.expressions.imp.String;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.cell.api.CellType;

public class UpperCaseExpression implements Expression {

    private final String value;

    public UpperCaseExpression(String value) {
        this.value = value;
    }

    @Override
    public EffectiveValue eval() {
        return new EffectiveValueImp(CellType.STRING, value.toUpperCase());
    }

    @Override
    public CellType getFunctionResultType()
    {
        return CellType.STRING;
    }
}