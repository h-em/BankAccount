package main;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class  AbstractMenu {

    public void displayMenu() {

        Integer option = Integer.MAX_VALUE;
        while (option != 0) {
            displayOption();
            option = readOption();
            try {
                executeOption(option);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Integer readOption() {
        System.out.println("Your option is: ");
        Scanner scanner = new Scanner(System.in);
        try {
            Integer option = scanner.nextInt();
            return option;
        }catch (InputMismatchException e){
            return -1;
        }
    }

    protected abstract void executeOption(Integer option) throws IOException;

    protected abstract void displayOption();


}
