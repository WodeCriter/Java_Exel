package exel.engine.expressions.imp.String;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.cell.api.CellType;

public class SubExpression implements Expression
{
    private final Expression source;
    private Expression startIndex, endIndex;

    public SubExpression(Expression source, Expression startIndex, Expression endIndex)
    {
        this.source = source;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public EffectiveValue eval()
    {
        EffectiveValue sourceValue = source.eval();
        EffectiveValue startValue = this.startIndex.eval();
        EffectiveValue endValue = this.endIndex.eval();

        if (sourceValue.getCellType() != CellType.STRING)
            throw new RuntimeException("The source given is not a String");
        if (startValue.getCellType() != CellType.NUMERIC || endValue.getCellType() != CellType.NUMERIC)
            throw new RuntimeException("The start or end index given are not Numeric");

        String sourceStr = sourceValue.extractValueWithExpectation(String.class);
        int startIndex = startValue.extractValueWithExpectation(Integer.class);
        int endIndex = endValue.extractValueWithExpectation(Integer.class);

        if (startIndex > endIndex)
        {
            int tmp = startIndex;
            startIndex = endIndex;
            endIndex = tmp;
        }

        if (startIndex < 0 || endIndex < 0 || startIndex >= sourceStr.length() || endIndex >= sourceStr.length())
            return new EffectiveValueImp(CellType.STRING, "!UNDEFINED!");

        return new EffectiveValueImp(CellType.STRING, sourceStr.substring(startIndex, endIndex));
    }

    @Override
    public CellType getFunctionResultType()
    {
        return CellType.STRING;
    }
}
