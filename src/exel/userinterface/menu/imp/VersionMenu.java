package exel.userinterface.menu.imp;

import exel.engine.api.Engine;
import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;
import exel.userinterface.input.api.InputHandler;
import exel.userinterface.menu.api.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionMenu implements Menu {
    private InputHandler inputHandler;
    private Engine engineAPI;
    private List<Integer> changesPerVersion;

    public VersionMenu(Engine engineAPI, InputHandler inputhandler) {
        this.inputHandler = inputhandler;
        this.engineAPI = engineAPI;
        this.changesPerVersion = engineAPI.getListOfVersionChanges();
    }

    @Override
    public void displayOptions() {
        displayVersionTable();
        System.out.println("1) Insert version number");
        System.out.println("2) Continue");
        System.out.println("9) Exit program");
        int choice = this.inputHandler.readInt();
        handleUserChoice(choice);

    }

    private void displayVersionTable() {
        System.out.println("Version | Cells changed");
        for (int i = 0; i < changesPerVersion.size(); i++) {
            System.out.printf("%7d | %12d%n", i + 1, changesPerVersion.get(i));
        }
    }

    @Override
    public void handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                displaySheetForVersion();
                break;
            case 2:
                System.out.println("Continuing...");
                break;
            case 9:
                System.out.println("Exiting program...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private void displaySheetForVersion() {
        System.out.print("Enter version number: ");
        int versionNumber = inputHandler.readInt() - 1;
        try{
            ReadOnlySheet readSheet = engineAPI.getSheetOfVersion(versionNumber);
            displayVersionSheet(readSheet);
        }catch (Exception e){
            System.out.println("Version chosen cannot be displayed :" + e.getMessage());
        }
    }

    public void displayVersionSheet(ReadOnlySheet sheet) {
        if (sheet == null) {
            System.out.println("No spreadsheet is currently loaded.");
            return;
        }

        System.out.println("Spreadsheet: " + sheet.getName());
        System.out.println("Version: " + sheet.getVersion());

        // Get cell width from the sheet properties
        int cellWidth = sheet.getCellWidth();

        // Initialize a map to quickly access cell values by coordinates
        Map<String, String> cellMap = new HashMap<>();
        for (ReadOnlyCell cell : sheet.getCells()) {
            cellMap.put(cell.getCoordinate(), cell.getEffectiveValue() != null ? cell.getEffectiveValue() : "");
        }

        // Print column headers with proper formatting
        System.out.print("   | ");
        for (int i = 0; i < sheet.getNumOfCols(); i++) {
            String header = String.format("%-" + cellWidth + "s", (char)('A' + i));
            System.out.print(header + " | ");
        }
        System.out.println();  // New line after headers

        // Print row data with cell values formatted to not exceed their width
        for (int i = 1; i <= sheet.getNumOfRows(); i++) {
            System.out.printf("%02d | ", i);
            for (int j = 0; j < sheet.getNumOfCols(); j++) {
                String cellCoordinate = "" + (char)('A' + j) + i;
                String cellValue = cellMap.getOrDefault(cellCoordinate, "");
                cellValue = formatCellValue(cellValue, cellWidth);  // Ensure cell value is formatted correctly
                System.out.print(cellValue + " | ");
            }
            System.out.println();  // New line after each row
        }
    }

    private static String formatCellValue(String value, int width) {
        if (value == null) {
            value = "";  // Treat null as empty string
        }
        if (value.length() > width) {
            if (width > 3) {
                return value.substring(0, width - 3) + "...";  // Add ellipsis if there's enough space
            } else {
                return value.substring(0, width);  // No space for ellipsis, just truncate
            }
        }
        return String.format("%-" + width + "s", value);  // Pad with spaces to ensure alignment
    }

}
