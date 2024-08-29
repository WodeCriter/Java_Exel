package exel.engine.expressions.imp;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.effectivevalue.imp.EffectiveValueImp;
import exel.engine.expressions.api.Expression;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.api.CellType;

public class RefExpression implements Expression
{
    private final String coordinate;
    private CellType type;

    public RefExpression(String coordinate)
    {
        this.coordinate = coordinate.toUpperCase();
        type = CellType.UNDEFINED;
    }

    @Override
    public EffectiveValue eval(Sheet sheet)
    {
        Cell cell = sheet.getCell(coordinate);

        if (cell.getEffectiveValue().getValue() == "")
        {
            type = CellType.STRING;
            return new EffectiveValueImp(CellType.UNDEFINED, UNDEFINED_STRING);
        }

        EffectiveValue value = cell.getEffectiveValue();
        type = value.getCellType();
        return value;
    }

    @Override
    public CellType getFunctionResultType()
    {
        return type;
    }
}
