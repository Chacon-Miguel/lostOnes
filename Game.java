import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {
    static Scanner Input = new Scanner(System.in);
    public static void main(String[] args) throws InterruptedException, IOException {
        boolean run = true;
        while (run) {
            runGame();
            TimeUnit.SECONDS.sleep(2);
            
            // Ask user if they want to play again
            boolean validAnswer = false;
            while (!validAnswer) {
                System.out.print("Would you like to play again? (Enter y/n): ");
                char answer = Input.next().charAt(0);    
                if (answer == 'y') {
                    run = true;
                    validAnswer = true;
                } else if (answer == 'n') {
                    System.out.println("Thanks for playing! ");
                    run = false;
                    validAnswer = true;
                    Input.close();
                } else {
                    System.out.println("You either didn't enter a y or an n, or I didn't");
                    System.out.println("understand what you meant. Try again. ");
                }    
            }
        }
    }

    static void displayPiles(String A, String B, String C) {
        for (int i = 9; i >= 0; i--) {System.out.println("\t" + A.charAt(i) + " " + B.charAt(i) + " " + C.charAt(i));}
        System.out.println("\tA B C");
    }

    static boolean check_if_player_lost(Row A, Row B, Row C, String winnersName, String losersName) {
        // Fix grammar, if needed
        String proper_phrase = ", you";
        String proper_phrase2 = "you lose";
        if (losersName.equals("The computer")) {
            proper_phrase = "";
            proper_phrase2 = "it lost";
        }

        // If there's only one stone left...
        if ((A.rocks + B.rocks + C.rocks) == 1) {
            displayPiles(A.visual, B.visual, C.visual);
            System.out.println(losersName + proper_phrase + " must take the last remaining counter, so ");
            System.out.println( proper_phrase2 + ". " + winnersName + " wins!");
            return true;
        } else if ((A.rocks + B.rocks + C.rocks ) == 0){
            System.out.println(winnersName + ", you took the the last counter, so ");
            System.out.println(proper_phrase2 + losersName + " wins!");
            return true;
        } else {
            return false;
        }
    }

    static void runGame() throws InterruptedException {
        Player player1 = new Player();
        player1.getName(1);
        
        Row A = new Row("A");
        Row B = new Row("B");
        Row C = new Row("C");
        Bot bot = new Bot();
        Player player2 = new Player();
        boolean playerLost = false;

        // Ask user if they want to play against the computer or with another person
        boolean validReponse = false;
        System.out.println(player1.name + ", would you like to play with another person or");
        System.out.println("against the computer? Enter c for computer and p for another person");
        char response = '0';
        while (!validReponse) {
            response = Input.next().charAt(0);
            if (response == 'p') {
                player2.getName(2);
                validReponse = true;
            } else if (response == 'c') {
                validReponse = true;
            } else {
                System.out.println("I do not understand what you inserted. Try again.");
            }
        }

        while (!playerLost) {
            // Player 1's turn
            displayPiles(A.visual, B.visual, C.visual);
            player1.getValidInput(A, B, C);
            
            // Check if player two lost. If so, stop the game
            playerLost = check_if_player_lost(A, B, C, player1.name, "The computer");
            if (playerLost) {break;}

            // Check who player 1 is going against
            displayPiles(A.visual, B.visual, C.visual);
            if (response == 'c') {
                System.out.println("It's the computer's turn.");
                // Delay the bot's turn for 1 seconds
                TimeUnit.SECONDS.sleep(1);
                bot.turn(A, B, C);    
            } else {
                player2.getValidInput(A, B, C);
            }
            // check if player one lost
            playerLost = check_if_player_lost(A, B, C, "The computer", player1.name);
            if (playerLost) {break;}
        }
    }
}