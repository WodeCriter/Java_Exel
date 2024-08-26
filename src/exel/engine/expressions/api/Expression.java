package exel.engine.expressions.api;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.CellType;

public interface Expression {
    static final String UNDEFINED_VALUE = "!UNDEFINED!";
    EffectiveValue eval(Sheet sheet);
    CellType getFunctionResultType();
}
