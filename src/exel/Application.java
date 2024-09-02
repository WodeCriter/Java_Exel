package exel;

import exel.engine.api.Engine;
import exel.engine.imp.EngineImp;
import exel.userinterface.input.api.InputHandler;
import exel.userinterface.input.imp.InputHandlerImp;
import exel.userinterface.menu.imp.MainMenu;
import exel.userinterface.menu.imp.SpreadsheetMenu;

import java.util.Scanner;

public class Application
{
    public static void main(String[] args) {
        Engine engine = new EngineImp();
        InputHandler inputHandler = new InputHandlerImp(new Scanner(System.in));
        SpreadsheetMenu spreadsheetMenu = new SpreadsheetMenu(engine, inputHandler);
        MainMenu mainMenu = new MainMenu(engine,inputHandler);
        System.out.println("----Welcome to Exel----");

        while (true){
            if (engine.hasSheet()) {
                // If a sheet is loaded, display the spreadsheet menu

                spreadsheetMenu.displayOptions();
            } else {
                // If no sheet is loaded, display the main menu
                mainMenu.displayOptions();
            }
        }

    }
}