package exel.engine.expressions.api;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.spreadsheet.cell.api.CellType;

public interface Expression {
    EffectiveValue eval();
    CellType getFunctionResultType();
}
