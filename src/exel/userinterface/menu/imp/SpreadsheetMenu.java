package exel.userinterface.menu.imp;

import exel.engine.api.Engine;
import exel.userinterface.menu.api.Menu;

import java.util.Scanner;

public class SpreadsheetMenu implements Menu {
    private Scanner scanner = new Scanner(System.in);
    private Engine engineAPI;

    public SpreadsheetMenu(Engine engineAPI) {
        this.engineAPI = engineAPI;
    }

    @Override
    public void displayOptions() {
        System.out.println("1. Show sheet");
        System.out.println("2. Show cell contents");
        System.out.println("3. Update cell contents");
        System.out.println("4. Show version");
        System.out.println("8. Save file and exit to main menu");
    }

    @Override
    public void handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                // logic to display the spreadsheet
                break;
            case 2:
                // logic to show cell contents
                break;
            case 3:
                // logic to update cell contents
                break;
            case 4:
                // logic to show version
                break;
            case 8:
                // logic to save and exit
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}
