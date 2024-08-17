package exel.engine.spreadsheet.cell.imp;

import java.util.List;

import exel.engine.expressions.api.Expression;
import exel.engine.effectivevalue.api.EffectiveValue;


public class CellImp implements exel.engine.spreadsheet.cell.api.Cell {

    private final String coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int version;
    private final List<CellImp> dependsOn;
    private final List<CellImp> influencingOn;

    public CellImp(String coordinate, String originalValue, EffectiveValue effectiveValue, int version, List<CellImp> dependsOn, List<CellImp> influencingOn) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.version = version;
        this.dependsOn = dependsOn;
        this.influencingOn = influencingOn;
    }
    @Override
    public String getCoordinate() {
        return coordinate;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setCellOriginalValue(String value) {
        this.originalValue = value;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

    @Override
    //TODO: code this
    public void calculateEffectiveValue() {
        // build the expression
        // 60 + 46 ---> 106(expression(NUM))
        // A3 + B4 ---> 70 + 50 ----> 120

        //Expression expression = new ;

        //effectiveValue = expression.eval(); ----> 106(expression(NUM))
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public List<CellImp> getDependsOn() {
        return dependsOn;
    }

    @Override
    public List<CellImp> getInfluencingOn() {
        return influencingOn;
    }
}
