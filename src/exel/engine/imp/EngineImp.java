package exel.engine.imp;

import exel.engine.api.Engine;
import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.api.Sheet;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.engine.spreadsheet.cell.imp.ReadOnlyCellImp;
import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.coordinate.Coordinate;
import exel.engine.spreadsheet.imp.ReadOnlySheetImp;
import exel.engine.spreadsheet.imp.SheetImp;
import exel.engine.spreadsheet.range.Range;
import exel.engine.spreadsheet.range.RangeDatabase;
import exel.engine.util.file_man.load.imp.sysStateLoader;
import exel.engine.util.file_man.load.imp.xmlFileLoader;
import exel.engine.util.file_man.save.imp.sysStateSaver;
import exel.engine.util.file_man.save.imp.xmlFileSaver;
import exel.eventsys.EventBus;

import java.util.LinkedList;
import java.util.List;

public class EngineImp implements Engine {
    private Sheet currentSheet;
    private ReadOnlySheet readOnlyCurrentSheet;
    private EventBus eventBus;
    private String filePath;

    public EngineImp() {
    }

    @Override
    public ReadOnlySheet createSheet(String sheetName, int rowNum , int colNum , int cellWidth , int cellHeight) {
        //Todo: check for validity of size

        // Create a new modifiable Sheet and its ReadOnly counterpart
        this.currentSheet = new SheetImp(cellHeight, cellWidth, colNum, rowNum, sheetName);
        this.readOnlyCurrentSheet = new ReadOnlySheetImp(currentSheet);
        return this.readOnlyCurrentSheet;
    }

    public ReadOnlySheet createNewSheet()
    {
    }

    @Override
    public void loadSheet(String filePath) throws Exception {
        // Simulated loading logic
        this.filePath = filePath;
        // parse the xml and create a sheet and a copy sheet object
        this.currentSheet = xmlFileLoader.loadSpreadsheet(filePath);
        this.readOnlyCurrentSheet = new ReadOnlySheetImp(currentSheet);
    }

    @Override
    public void loadSysState(String filePath) throws Exception {
        // Simulated loading logic
        this.filePath = filePath;
        // create a sheet object from the binary file
        this.currentSheet = sysStateLoader.loadSysState(filePath);
        this.readOnlyCurrentSheet = new ReadOnlySheetImp(currentSheet);
    }

    @Override
    public ReadOnlySheet getSheet() {
        return readOnlyCurrentSheet;
    }

    @Override
    public ReadOnlySheet getSheetOfVersion(int version) {
        Sheet verSheet = currentSheet.getSheetByVersion(version);
        return new ReadOnlySheetImp(verSheet);
    }

    @Override
    public List<Integer> getListOfVersionChanges() {
        return currentSheet.getNumOfChangesInEachVersion();
    }

    @Override
    public ReadOnlyCell getCellContents(String cellCoordinate) {
        if (currentSheet != null) {
            Cell cell = currentSheet.getCell(new Coordinate(cellCoordinate));
            return cell != null ? new ReadOnlyCellImp(cellCoordinate, cell.getOriginalValue(), cell.getEffectiveValue(), cell.getVersion(), cell.getDependsOn(), cell.getInfluencingOn()) : null;
        }
        return null;
    }

    @Override
    public void updateCellContents(String coordinate, String value) throws Exception
    {
        if (currentSheet == null) {
            throw new IllegalStateException("No sheet is currently loaded.");
        }
        //update the current sheet to a copy created inside
        currentSheet = currentSheet.updateCellValueAndCalculate(new Coordinate(coordinate), value); // Directly set the cell's value in the modifiable sheet
        //update your read only sheet based on the copy you just got
        readOnlyCurrentSheet = new ReadOnlySheetImp(currentSheet);
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

    @Override
    public void addNewRange(String rangeName, String topLeftCord, String bottomRightCord)
    {
        new Range(new Coordinate(topLeftCord), new Coordinate(bottomRightCord), rangeName, currentSheet);
    }

    @Override
    public List<String> getCordsOfCellsInRange(String rangeName)
    {
        if (!RangeDatabase.isRangeInDatabase(rangeName))
            throw new IllegalArgumentException("The range \"" + rangeName + "\" does not exist.");

        Range range = RangeDatabase.getRange(rangeName);
        return range.getCordsStrInRange();
    }

    @Override
    public void deleteRange(String rangeName)
    {
        //todo: What happens when we try to delete a range that's currently used
        RangeDatabase.removeRange(rangeName);
    }

    public static List<String> getAllColumnsBetween2Cords(String cord1Str, String cord2Str)
    {
        if (Coordinate.isStringACellCoordinate(cord1Str) && Coordinate.isStringACellCoordinate(cord2Str))
        {
            Coordinate cord1 = new Coordinate(cord1Str);
            Coordinate cord2 = new Coordinate(cord2Str);

            if (cord1.getColIndex() > cord2.getColIndex())
            {
                Coordinate tmp = cord1;
                cord1 = cord2;
                cord2 = tmp;
            }

            List<String> cordsInBetween = new LinkedList<>();

            do
            {
                cordsInBetween.add(cord1.getCol());
                cord1 = cord1.getCordOnRight();
            } while (cord1.getColIndex() <= cord2.getColIndex());

            return cordsInBetween;
        }

        return null;
    }
}