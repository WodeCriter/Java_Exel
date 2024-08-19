package exel.engine.spreadsheet.cell.imp;

import java.util.List;

import exel.engine.expressions.api.Expression;
import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.expressions.imp.FunctionParser;


public class CellImp implements exel.engine.spreadsheet.cell.api.Cell {

    private final String coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int version;
    private final List<CellImp> dependsOn;
    private final List<CellImp> influencingOn;

    //TODO: dependsOn needs to be updated by calculateEffectiveValue()
    public CellImp(String coordinate, String originalValue) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        this.version = 1;
        calculateEffectiveValue();
        this.dependsOn = null;
        this.influencingOn = null;
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
    public void calculateEffectiveValue() {
        effectiveValue = FunctionParser.parseExpression(originalValue).eval();
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
