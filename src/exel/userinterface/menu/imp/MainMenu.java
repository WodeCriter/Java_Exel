package exel.userinterface.menu.imp;

import exel.engine.api.Engine;
import exel.userinterface.input.api.InputHandler;
import exel.userinterface.menu.api.Menu;

public class MainMenu implements Menu {
    private InputHandler inputHandler;
    private Engine engineAPI;
    private final static int MAX_COLUMN_NUM = 20;
    private final static int MAX_ROW_NUM = 50;
    private final static int DEFAULT_CELL_WIDTH = 5;
    private final static int DEFAULT_CELL_LENGTH = 1;


    public MainMenu(Engine engineAPI) {
        this.engineAPI = engineAPI;
    }

    @Override
    public void displayOptions() {
        System.out.println("1. Create a new Spreadsheet");
        System.out.println("2. Load a Spreadsheet from an XML file");
        System.out.println("3. Load system state from file");
        System.out.println("9. Exit");
        int choice = this.inputHandler.readInt();
        this.handleUserChoice(choice);
    }

    @Override
    public void handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                // logic to create new spreadsheet
                break;
            case 2:
                loadSpreadsheet();
                break;
            case 3:
                //logic to load system file
                break;
            case 9:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void loadSpreadsheet() {
        System.out.print("Please enter the full path to the XML file: ");
        String path = this.inputHandler.readLine();

        // Example validation - would normally be replaced with actual file checks and XML parsing
        if (!path.endsWith(".xml")) {
            System.out.println("Error: The file must be an XML file (.xml extension).");
        } else {
            try {
                this.engineAPI.loadSheet(path);
                System.out.println("File loaded successfully. Spreadsheet is now at version 1.");
            }catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createNewSpreadsheet() {
        System.out.println("Enter sheet name");
        String sheetName = this.inputHandler.readLine();
        System.out.println("Enter desired number of columns (up to 20)");
        int columnNum = this.inputHandler.readIntRange(1,20);
        System.out.println("Enter desired number of rows (up to 50)");
        int rowNum = this.inputHandler.readIntRange(1,20);
        this.engineAPI.createSheet();
    }
}
