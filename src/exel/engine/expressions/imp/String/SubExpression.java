package exel.engine.expressions.imp.String;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.cell.api.CellType;

public class SubExpression implements Expression
{
    private final Expression source;
    private int startIndex, endIndex;

    public SubExpression(Expression source, int startIndex, int endIndex)
    {
        this.source = source;
        this.startIndex = startIndex;
        this.endIndex = endIndex;

        if (startIndex == endIndex)
            throw new IllegalArgumentException("start and end indexes cannot be equal");

        if (startIndex > endIndex)
            swapStartAndEndIndexes();
    }

    @Override
    public EffectiveValue eval()
    {
        EffectiveValue sourceValue = source.eval();

        if (sourceValue.getCellType() != CellType.STRING)
            throw new RuntimeException("The item given is not a String");

        String sourceStr = sourceValue.extractValueWithExpectation(String.class);

        if (startIndex < 0 || endIndex < 0 || startIndex >= sourceStr.length() || endIndex >= sourceStr.length())
            return new EffectiveValueImp(CellType.STRING, "!UNDEFINED!");

        return new EffectiveValueImp(CellType.STRING, sourceStr.substring(startIndex, endIndex));
    }

    private void swapStartAndEndIndexes()
    {
        int tmp = startIndex;
        startIndex = endIndex;
        endIndex = tmp;
    }

    @Override
    public CellType getFunctionResultType()
    {
        return CellType.STRING;
    }
}
