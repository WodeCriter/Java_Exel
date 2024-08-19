package exel.engine.effectivevalue.api;

import exel.engine.spreadsheet.cell.api.CellType;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue();

    //basically does casting if can. If it can't, returns null.
    <T> T extractValueWithExpectation(Class<T> type);
}

