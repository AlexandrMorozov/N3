package com.company.io;

import com.company.interfaces.IConsoleIO;
import java.util.Scanner;

public class ConsoleIO implements IConsoleIO {

    public void displayMenu(String[] moves) {

        System.out.println("Available moves:");

        for (int i = 0; i < moves.length; i++) {
            System.out.println((i + 1) + " - " + moves[i]);
        }
        System.out.println("0 - exit");
        System.out.print("Enter your move: ");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public Integer enterMove() {

        Integer playerMove = null;
        Scanner moveReceiver = new Scanner(System.in);

        if (moveReceiver.hasNextInt()) {
            playerMove = moveReceiver.nextInt();
        }

        return playerMove;
    }

}
