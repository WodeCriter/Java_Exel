package exel.engine.effectivevalue.imp;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.spreadsheet.cell.api.CellType;

import java.io.Serializable;

public class EffectiveValueImp implements EffectiveValue, Serializable {
    private static final long serialVersionUID = 1L;
    private CellType cellType;
    private Object value;

    public EffectiveValueImp(CellType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T extractValueWithExpectation(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(value);
        }
        // error handling... exception ? return null ?
        return null;
    }
}

