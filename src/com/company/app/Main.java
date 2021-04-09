package com.company.app;

import com.company.interfaces.IConsoleIO;
import com.company.io.ConsoleIO;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main {

    private static IConsoleIO inputOutput = new ConsoleIO();

    public static void main(String[] moves) {

        int playerMove = 0, computerMove = 0;
        SecretKey hmacKey = null;

        if (moves.length < 3) {
            inputOutput.displayMessage("Incorrect input! Number of arguments must be greater than two!");
        } else if ((moves.length % 2) == 0) {
            inputOutput.displayMessage("Incorrect input! Number of arguments must be uneven!");
        } else if (!checkForDuplicates(moves)) {
            inputOutput.displayMessage("Incorrect input! Arguments mustn't repeat!");
        } else {
            //Key generation
            hmacKey = generateKey();
            //Computer turn
            computerMove = (int)(Math.random() * (moves.length - 1));
            byte[] computerMoveMessage = moves[computerMove].getBytes(StandardCharsets.UTF_8);
            //HMAC generation
            generateHmac(hmacKey, computerMoveMessage);

            boolean isDataValid = false;

            while (!isDataValid) {

                inputOutput.displayMenu(moves);
                Integer inputResult = inputOutput.enterMove();

                if (inputResult == null) {
                    inputOutput.displayMessage("Incorrect input! You should enter only numbers!");
                } else if ((inputResult >= 0) && (inputResult < moves.length + 1)) {
                    isDataValid = true;
                    playerMove = inputResult;
                } else {
                    inputOutput.displayMessage("Incorrect input! You can choose only from available options!");
                }
            }

            if (playerMove != 0) {
                //Decreasing playerMove value by 1 to match indexes in array
                playerMove--;

                inputOutput.displayMessage("Your move: " + moves[playerMove]);
                inputOutput.displayMessage("Computer move: " + moves[computerMove]);
                //How many moves will win or lose
                int rangeOfResult = moves.length / 2;
                determineWinner(rangeOfResult, playerMove, computerMove);

                inputOutput.displayMessage("HMAC key: "+ new BigInteger(1, hmacKey.getEncoded()).toString(16));
            }

        }
    }

    private static SecretKey generateKey() {

        SecretKey hmacKey = null;
        SecureRandom randomizer = new SecureRandom();

        try {
            KeyGenerator keyCreator = KeyGenerator.getInstance("HMACSHA256");
            keyCreator.init(128,randomizer);
            hmacKey = keyCreator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        return hmacKey;
    }

    private static void generateHmac(SecretKey hmacKey, byte[] moveMessage) {
        try {
            Mac hmacGenerator = Mac.getInstance("HmacSHA256");
            hmacGenerator.init(hmacKey);
            byte[] hmac = hmacGenerator.doFinal(moveMessage);

            inputOutput.displayMessage(String.format("HMAC: %032x", new BigInteger(1, hmac)).toUpperCase());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void determineWinner(int resultRange, int playerMove, int computerMove) {

        //Distance between player move and computer move
        int moveSpace = computerMove - playerMove;

        if (((moveSpace <= resultRange) && (moveSpace > 0)) || ((moveSpace < 0) && (moveSpace < -resultRange))) {
            inputOutput.displayMessage("You lose!");
        } else if (((moveSpace > resultRange) && (moveSpace > 0)) || ((moveSpace < 0) && (moveSpace >= -resultRange))) {
            inputOutput.displayMessage("You win!");
        } else {
            inputOutput.displayMessage("Draw!");
        }
    }

    private static boolean checkForDuplicates(String[] moves) {

        for (int i = 0; i < moves.length; i++) {
            int counter = 0;
            for (int j = i + 1; j < moves.length; j++) {

                if (moves[i].equals(moves[j])) {
                    counter++;
                }

                if (counter > 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
