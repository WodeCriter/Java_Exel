package exel.userinterface.input.imp;

import exel.userinterface.input.api.InputHandler;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandlerImp implements InputHandler {
    private Scanner scanner;

    public InputHandlerImp(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public int readInt() {
        while (true) {
            try {
                int result = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after reading the integer
                return result;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Consume the invalid input
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    @Override
    public int readIntRange(int min, int max){
        int number = readInt();
        while (number < min || number > max) {
            System.out.println("Invalid input. Please enter an integer.");
            number = readInt();
        }
        return number;
    }

    @Override
    public void close() {
        scanner.close();
    }
}