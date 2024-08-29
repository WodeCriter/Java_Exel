package exel.engine.imp;

import exel.engine.api.Engine;
import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.engine.spreadsheet.cell.imp.ReadOnlyCellImp;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.imp.ReadOnlySheetImp;
import exel.engine.spreadsheet.imp.SheetImp;
import exel.engine.util.file_man.load.imp.xmlFileLoader;
import exel.engine.util.file_man.save.imp.sysStateSaver;
import exel.engine.util.file_man.save.imp.xmlFileSaver;

import java.io.File;
import java.util.ArrayList;

public class EngineImp implements Engine {
    private Sheet currentSheet;
    private ReadOnlySheet readOnlyCurrentSheet;
    private String filePath;

    public EngineImp() {
    }

    @Override
    public void createSheet(String sheetName, int cellHeight, int cellWidth,int numOfCols , int numOfRows) {
        //Todo: check for validity of size

        // Create a new modifiable Sheet and its ReadOnly counterpart
        this.currentSheet = new SheetImp(cellHeight, cellWidth, numOfCols, numOfRows, sheetName);
        this.readOnlyCurrentSheet = new ReadOnlySheetImp(currentSheet.getVersion(), new ArrayList<>(), currentSheet.getName(), currentSheet.getNumOfCols(), currentSheet.getNumOfRows(), currentSheet.getCellWidth(), currentSheet.getCellHeight());
        System.out.println("New spreadsheet created.");
    }

    @Override
    public void loadSheet(String filePath) throws Exception {
        // Simulated loading logic
        this.filePath = filePath;
        // Real implementation would involve parsing XML and creating a Sheet object
        this.currentSheet = xmlFileLoader.loadSpreadsheet(filePath);
        this.readOnlyCurrentSheet = new ReadOnlySheetImp(currentSheet.getVersion(), new ArrayList<>(), currentSheet.getName(), currentSheet.getNumOfCols(), currentSheet.getNumOfRows(), currentSheet.getCellWidth(), currentSheet.getCellHeight());

    }

    @Override
    public ReadOnlySheet getSheet() {
        return readOnlyCurrentSheet;
    }

    @Override
    public ReadOnlyCell getCellContents(String cellCoordinate) {
        if (currentSheet != null) {
            Cell cell = currentSheet.getCell(cellCoordinate);
            return cell != null ? new ReadOnlyCellImp(cell.getCoordinate(), cell.getOriginalValue(), cell.getEffectiveValue(), cell.getVersion(), cell.getDependsOn(), cell.getInfluencingOn()) : null;
        }
        return null;
    }

    @Override
    public void updateCellContents(String coordinate, String value) throws IllegalStateException {
        if (currentSheet == null) {
            throw new IllegalStateException("No sheet is currently loaded.");
        }
        //update the current sheet to a copy created inside
        currentSheet = currentSheet.updateCellValueAndCalculate(coordinate, value); // Directly set the cell's value in the modifiable sheet
        //update your read only sheet based on the copy you just got
        readOnlyCurrentSheet = new ReadOnlySheetImp(currentSheet.getVersion(), new ArrayList<>(), currentSheet.getName(), currentSheet.getNumOfCols(), currentSheet.getNumOfRows(), currentSheet.getCellWidth(), currentSheet.getCellHeight());
    }

    @Override
    public String getVersion() {
        return currentSheet != null ? String.valueOf(currentSheet.getVersion()) : "No sheet loaded.";
    }

    @Override
    public void saveXmlFile(String filePath, String fileName) throws Exception {
        if (fileName.isEmpty())
            fileName = currentSheet.getName() + "_sheet_v" + currentSheet.getVersion();
        xmlFileSaver.saveSpreadsheet(this.currentSheet , filePath+ '/' + fileName + ".xml");
    }

    @Override
    public void saveSysStateFile(String filePath, String fileName) {
        if (fileName.isEmpty())
            fileName = currentSheet.getName() + "_systemState_v"+ currentSheet.getVersion();
        sysStateSaver.saveSheetState(filePath +"/"+ fileName + ".bin", this.currentSheet);
    }

    @Override
    public boolean hasSheet() {
        return this.currentSheet != null;
    }

    @Override
    public void clearSheet() {
        this.currentSheet = null;
    }
}