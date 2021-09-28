import java.util.Random;

public class Row {
    String name;
    int rocks;
    String visual;
    Row(String ID) {
        name = ID;
        // Generate random number of rocks in the pile
        Random ran = new Random();
        rocks = ran.nextInt(10)+1;
    // rocks = amount;
        // Create visual for the pile. Max length is 10; If the number of rocks in the pile
        // is smaller than 10, the rest of the string is whitespace
        visual = new String(new char[rocks]).replace("\0", "*") + new String(new char[10-rocks]).replace("\0", " ");
    }

    boolean validRow(String Name) {
        // Checks if the pile is empty, meaning the user cannot choose this pile
        if (rocks == 0) {
            System.out.println("Nice try " + Name + " but pile " + name + " is empty.");
            System.out.println("Try again.");
            return true;
        } else {
            return false;
        }
    }
    /**Checks if the player tried to remove too many rocks, if they try to not remove any
    * rocks and if they entered a negative number. Returns false otherwise
    */
    boolean validNumb(int numb, String player_name) {
        if (numb > rocks) {
            System.out.println("Pile " + name + " doesn't have that many rocks. Try again. ");
            return true;
        } else if (numb == 0) {
            System.out.println("You must choose at least 1. ");
            return true;
        } else if (numb < 0) {
            System.out.println("You cannot remove a negative amount of rocks. Try again.");
            return true;
        } else {
            return false;
        }
    }

    void removeRocks(int numb) {
        /**Changes the string that displays the rocks and the rocks counter variable */
        visual = visual.substring(numb) + new String(new char[numb]).replace("\0", " ");
        rocks -= numb;
    }
}