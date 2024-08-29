package exel.engine.spreadsheet.versionmanager.imp;

import exel.engine.spreadsheet.cell.api.Cell;
import exel.engine.spreadsheet.cell.imp.CellImp;
import exel.engine.spreadsheet.imp.SheetImp;

import java.util.*;

public class VersionManager {
    private SheetImp baseSheet;
    private List<Map<String, String>> changesPerVersion = new LinkedList<>();

    public VersionManager(SheetImp initialSheet) {
        this.baseSheet = initialSheet;
        changesPerVersion.add(new HashMap<>()); // Initial version with no changes
    }

    public void recordChanges(List<Cell> changedCells) {
        //get a list of all chnaged cells
        Map<String, String> versionChanges = new HashMap<>();

        changedCells.forEach(Cell->versionChanges.put(Cell.getCoordinate(), Cell.getEffectiveValue().getValue().toString()));

        changesPerVersion.add(versionChanges);
    }

    public SheetImp getSheetByVersion(int version) {
        SheetImp versionSheet = baseSheet.copySheet(); // Start with the base state
        for (int i = 1; i <= version; i++) {
            applyChanges(versionSheet, changesPerVersion.get(i));
        }
        return versionSheet;
    }

    private void applyChanges(SheetImp sheet, Map<String, String> changes) {
        changes.forEach(sheet::setCell);
    }

    public void displayVersionChanges() {
        for (int version = 1; version < changesPerVersion.size(); version++) {
            System.out.println("Version " + version + " changes:");
            changesPerVersion.get(version).forEach((cell, value) ->
                    System.out.println("Cell " + cell + " changed to: " + value));
        }
    }
}