import java.util.Scanner;

public class Player {
    Scanner input = new Scanner(System.in);
    String name;
    boolean lost;
    String rowChosen;
    Row chosen;
    void getName(int ID) {
        System.out.print("Player " + ID +", enter your name: ");
        String name = input.next();
        this.name = name;
    }
    void getValidInput(Row A, Row B, Row C) {
        boolean InvalidRow = true;
        int rocksToDelete = 0;
        // First get valid row
        while (InvalidRow) {
            System.out.print(name + ", choose a pile: ");
            // Only get the first letter if the player entered multiple ones
            // also capitalize it if it wasn't already
            char row = input.next().charAt(0);
            // check if the player entered a letter
            if (!Character.isLetter(row)) {
                System.out.println("You didn't type in a letter. Try again.");
                continue;
            } else {
                rowChosen = Character.toString(row);
            }
            // Check which row player chose. Tell them that they didn't enter a 
            // real pile if they didn't enter A, B, or C
            if (rowChosen.toUpperCase().equals("A")) {
                chosen = A;
            } else if (rowChosen.toUpperCase().equals("B")) {
                chosen = B;
            } else if (rowChosen.toUpperCase().equals("C")) {
                chosen = C;
            } else {
                System.out.println("You didn't enter one of the three piles.");
                System.out.println("Try again.");
                continue;
            }
            InvalidRow = chosen.validRow(name);
        }
        // Then get valid number of rocks to delete
        do {
            System.out.println("How many to remove from pile " + chosen.name + ": ");
            while (!input.hasNextInt()) {
                System.out.println("You didn't enter a number. Try again.");
                input.next(); // this is important!
            }
            rocksToDelete = input.nextInt();
        } while (chosen.validNumb(rocksToDelete, name));
        
        // After receiving valid input, remove rocks
        chosen.removeRocks(rocksToDelete);
    }
}
