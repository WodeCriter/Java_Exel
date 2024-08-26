package exel.engine.spreadsheet.cell.api;

//import exel.engine.spreadsheet.api.EffectiveValue;
import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.spreadsheet.cell.imp.CellImp;
import exel.engine.spreadsheet.imp.SheetImp;

import java.util.List;

public interface Cell {
    String getCoordinate();
    String getOriginalValue();
    void setCellOriginalValue(String value);
    EffectiveValue getEffectiveValue();
    void calculateEffectiveValue();
    int getVersion();
    void updateVersion();
    List<CellImp> getDependsOn();
    List<CellImp> getInfluencingOn();
}
