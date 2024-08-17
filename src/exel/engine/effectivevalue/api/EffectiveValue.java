package exel.engine.effectivevalue.api;

import exel.engine.spreadsheet.cell.api.CellType;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue();
    <T> T extractValueWithExpectation(Class<T> type);
}

