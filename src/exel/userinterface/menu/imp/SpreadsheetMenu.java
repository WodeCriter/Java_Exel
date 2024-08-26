package exel.userinterface.menu.imp;

import exel.engine.api.Engine;
import exel.userinterface.input.api.InputHandler;
import exel.userinterface.menu.api.Menu;
import exel.engine.spreadsheet.api.ReadOnlySheet;
import exel.engine.spreadsheet.cell.api.ReadOnlyCell;

import java.util.List;

public class SpreadsheetMenu implements Menu {
    private InputHandler inputHandler;
    private Engine engineAPI;

    public SpreadsheetMenu(Engine engineAPI, InputHandler inputhandler) {
        this.engineAPI = engineAPI;
        this.inputHandler = inputhandler;
    }

    @Override
    public void displayOptions() {
        System.out.println("1. Show sheet");
        System.out.println("2. Show cell contents");
        System.out.println("3. Update cell contents");
        System.out.println("4. Show version");
        System.out.println("5. Save file");
        System.out.println("6. Load file");
        System.out.println("8. Exit to main menu");
        System.out.println("9. Exit program");
        int choice = this.inputHandler.readInt();
        handleUserChoice(choice);
    }

    @Override
    public void handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                displaySpreadsheet();
                break;
            case 2:
                showCellContents();
                break;
            case 3:
                updateCellContents();
                break;
            case 4:
                showVersion();
                break;
            case 5:
                saveFile();
                break;
            case 6:
                loadFile();
                break;
            case 8:
                System.out.println("Exiting to main menu...");
                engineAPI.clearSheet();
                break;
            case 9:
                System.out.println("Exiting program...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void displaySpreadsheet() {
        ReadOnlySheet sheet = engineAPI.getSheet();
        if (sheet == null) {
            System.out.println("No spreadsheet is currently loaded.");
            return;
        }

        System.out.println("Spreadsheet: " + sheet.getName());
        System.out.println("Version: " + sheet.getVersion());

        // Get cell width and height from the sheet properties
        int cellWidth = sheet.getCellWidth();

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
                ReadOnlyCell cell = engineAPI.getCellContents(cellCoordinate);
                String cellValue = (cell != null && cell.getEffectiveValue() != null) ? cell.getEffectiveValue() : "";
                cellValue = formatCellValue(cellValue, cellWidth);  // Ensure cell value is formatted correctly
                System.out.print(cellValue + " | ");
            }
            System.out.println();  // New line after each row
        }
    }

    private void showCellContents() {
        System.out.print("Enter cell coordinate (e.g., A1): ");
        //Todo: validate coordinate and ease of input
        String coordinate = inputHandler.readLine();
        ReadOnlyCell cell = engineAPI.getCellContents(coordinate);
        if (cell != null) {
            System.out.println("Cell: " + coordinate);
            System.out.println("Original Value: " + cell.getOriginalValue());
            System.out.println("Effective Value: " + cell.getEffectiveValue());
            System.out.println("Version: " + cell.getVersion());
            printDependenciesAndInfluences(cell);
        } else {
            System.out.println("No contents found at " + coordinate);
        }
    }

    //Todo: fix the not working part
    private void updateCellContents() {
        System.out.print("Enter cell coordinate (e.g., A1): ");
        String coordinate = inputHandler.readLine().toUpperCase();
        System.out.print("Enter new value for the cell: ");
        String value = inputHandler.readLine();
        try {
            engineAPI.updateCellContents(coordinate, value);
            System.out.println("Cell updated successfully.");
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }


    }

    private void showVersion() {
        //TODO: FIX LOGIC
        String version = engineAPI.getVersion();
        System.out.println("FIX:Current spreadsheet version: " + version);
    }

    private void saveFile(){
        System.out.print("Enter file path to save: ");
        String filePath = inputHandler.readLine();
        try {
            engineAPI.saveXmlFile(filePath);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("File saved successfully at " + filePath);
    }

    private void loadFile() {
        System.out.print("Enter file path to load: ");
        String filePath = inputHandler.readLine();
        try {
            engineAPI.loadSheet(filePath);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("File loaded successfully from " + filePath);
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

    private void printDependenciesAndInfluences(ReadOnlyCell cell) {
        List<String> dependsOn = cell.getDependsOn();
        List<String> influencingOn = cell.getInfluencingOn();

        System.out.print("Depends On: ");
        if (!dependsOn.isEmpty())
            for (String dep : dependsOn)
                System.out.print(dep + " ");
        else{
            System.out.print("None.");
        }
        System.out.println();

        System.out.print("Influencing On: ");
        if (!influencingOn.isEmpty())
            for (String inf : influencingOn)
                System.out.print(inf + " ");
        else{
            System.out.print("None.");
        }
        System.out.println();

    }
}