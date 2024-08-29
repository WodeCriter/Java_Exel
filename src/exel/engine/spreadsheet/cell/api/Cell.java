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
    void updateDependencies();
    EffectiveValue getEffectiveValue();
    void calculateEffectiveValue();
    void setVersion(int version);
    int getVersion();
    List<CellImp> getDependsOn();
    List<CellImp> getInfluencingOn();
}
