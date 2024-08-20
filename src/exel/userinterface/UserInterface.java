package exel.userinterface;
//handles user input and display
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner = new Scanner(System.in);
    private boolean sheetLoaded = false;

    public void start_menu() {
        while (true) {
            System.out.println("Welcome to Sheet-Cell. Please choose an option:");
            if (!sheetLoaded) {
                System.out.println("1. Create a new Spreadsheet");
                System.out.println("2. Load a Spreadsheet from an XML file");
                System.out.println("3. Exit");
            } else {
                System.out.println("1. Show sheet");
                System.out.println("2. Show cell contents");
                System.out.println("3. Update cell contents");
                System.out.println("4. Show version");
                System.out.println("8. Save file and exit to main menu");
            }

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            if (!sheetLoaded) {
                switch (choice) {
                    case 1:
                        createNewSpreadsheet();
                        break;
                    case 2:
                        loadSpreadsheet();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
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
                    case 8:
                        saveAndExit();
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private void loadSpreadsheet() {
        System.out.print("Please enter the full path to the XML file: ");
        String path = scanner.nextLine().trim();

        // Example validation - would normally be replaced with actual file checks and XML parsing
        if (!path.endsWith(".xml")) {
            System.out.println("Error: The file must be an XML file (.xml extension).");
        } else {
            // TODO: create exel.engine.EngineAPI.load()
            System.out.println("File loaded successfully. Spreadsheet is now at version 1.");
        }
    }

    //the function that will get a sheet object and display it on the screen
    private void displaySpreadsheet() {
        //TODO: create exel.engine.EngineAPI.getSheet()

        // Mock data and display logic
        System.out.println("Version: 1");
        System.out.println("Spreadsheet: ExampleSheet");

        //
        int rows = 10;
        int columns = 5;

        // Print column headers
        System.out.print("   | ");  // Initial spacing for row numbers
        for (int i = 0; i < columns; i++) {
            System.out.print((char)('A' + i) + "       | ");  // Adjust spacing based on the actual column width
        }
        System.out.println();  // New line after headers

        // Print row data
        for (int i = 1; i <= rows; i++) {
            System.out.printf("%02d | ", i);  // Print row number
            for (int j = 0; j < columns; j++) {
                // Mock data display, replace with actual cell data retrieval
                System.out.print("Value   | ");  // Each cell value, ensure alignment with headers
            }
            System.out.println();  // New line after each row
        }
    }
    private void createNewSpreadsheet() {
        //TODO: create exel.engine.EngineAPI.createSheet()
        // Logic to create a new sheet
    }

    private void showCellContents() {
        //TODO: create exel.engine.EngineAPI.getCellContents()
        // Logic to display contents of a specific cell
    }

    private void updateCellContents() {
        //TODO: create exel.engine.EngineAPI.updateCellContents()
        // Logic to update contents of a specific cell
    }

    private void showVersion() {
        //TODO: create exel.engine.EngineAPI.showVersion()
        // Logic to update contents of a specific cell
    }

    private void saveAndExit() {
        // Logic to save the file and exit to the main menu
        //TODO: create exel.engine.EngineAPI.saveFile()
        sheetLoaded = false; // Reset the sheetLoaded flag
        System.out.println("File saved and returning to main menu.");
    }

    public static void main(String[] args) {
        new UserInterface().start_menu();
    }
}