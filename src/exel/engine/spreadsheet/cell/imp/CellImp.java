package exel.engine.spreadsheet.cell.imp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import exel.engine.expressions.api.Expression;
import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.expressions.imp.FunctionParser;


public class CellImp implements exel.engine.spreadsheet.cell.api.Cell {

    private final String coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int version;
    private List<CellImp> dependsOn;
    private List<CellImp> influencingOn;


    public CellImp(String coordinate, String originalValue) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        this.version = 1;
        calculateEffectiveValue();
        setUpDependsOn();
        this.influencingOn = null;
    }

    public CellImp(String coordinate){
        this(coordinate, null);
    }

    private void setUpDependsOn(){
        dependsOn = new LinkedList<>();
        List<String> dependsOnStr = FunctionParser.getCellsInString(originalValue);
        //TODO: Needs to finish this method. Need to find a way to convert the coordinates into actual cells to add to dependsOn.
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
