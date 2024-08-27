package exel.engine.spreadsheet.cell.imp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import exel.engine.effectivevalue.api.EffectiveValue;
import exel.engine.expressions.imp.FunctionParser;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.imp.SheetImp;


public class CellImp implements exel.engine.spreadsheet.cell.api.Cell, Serializable {

    private static final long serialVersionUID = 1L;
    private final String coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private final SheetImp sheet;
    private int version;
    private List<CellImp> dependsOn;
    private List<CellImp> influencingOn;


    public CellImp(String coordinate, String originalValue, SheetImp sheet) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        this.sheet = sheet;
        this.version = 1;
        this.influencingOn = new LinkedList<>();
        calculateEffectiveValue();
        setUpDependsOn();
    }

    public CellImp(String coordinate, SheetImp sheet){
        this.coordinate = coordinate;
        this.originalValue = "";
        this.sheet = sheet;
        this.version = 1;
        this.influencingOn = new LinkedList<>();
        this.dependsOn = new LinkedList<>();
    }

    private void setUpDependsOn(){
        dependsOn = new LinkedList<>();
        List<String> influencingCellsCords = FunctionParser.getCellCordsInOriginalValue(originalValue);
        for (String cellCord : influencingCellsCords)
        {
            CellImp influencingCell = sheet.getCell(cellCord);
            dependsOn.add(influencingCell);
            influencingCell.influencingOn.add(this);
        }
    }

    private void stopCellFromDepending() {
        for (CellImp influencingCell : dependsOn)
            influencingCell.influencingOn.remove(this);
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
    public void updateDependencies(){
        stopCellFromDepending();
        setUpDependsOn();
        version++;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        calculateEffectiveValue();
        return effectiveValue;
    }

    @Override
    public void calculateEffectiveValue() {
        effectiveValue = FunctionParser.parseExpression(originalValue).eval(sheet);
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
