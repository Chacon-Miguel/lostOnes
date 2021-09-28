import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.*;

public class Bot {
    /**
     * How the bot works: 1. It first gets the amount of every pile and breaks the
     * amount into multiples of 1, 2, 4, and 8 2. It then determines if the current
     * multiples are a nim sum. If they are, then the bot only removes one stone
     * from whichever pile. 3. When it isn't given a nim sum, it then finds all
     * unpaired multiples and stores both their location and value 4. It then goes
     * through all test cases described in the create nim sum method to find the
     * optimal move.
     */

    /**
     * Breaks down a given amount into multiples of 8, 4, 2, and 1 in that order The
     * method returns an array that has all the multiples.
     */
    static int[] divide_row_into_multiples(int amount) {
        // Save given amount to local variable
        int amountA = amount;
        // Initialize the array the method will be retuning.
        // Holds the multiples that make up the given amount
        int[] multiples = new int[4];

        // Go in the following order: 8, 4, 2, 1. That way, the amount is divided
        // into singular multiples (i.e. 4 will be 4 rather than 1x2, 1x2)
        for (int i = 3; i >= 0; i--) {
            // skip if the current multiple is greater than the actual amount
            if (amountA < Math.pow(2, i)) {
                continue;
            }
            // Add to the array. Subtract the multiple from the amount to avoid inaccuracies
            multiples[i] = (int) Math.pow(2, i);
            amountA -= Math.pow(2, i);
        }
        return multiples;
    }

    /** Determines if the given multiples form a nim sum */
    static boolean find_nim_sum(int[] args) {
        // See if there is a nim sum
        for (int numb : args) {
            // If the number of multiples of that integer is not even...
            if (numb % 2 != 0) {
                return false;
            }
        }
        return true;
    }

    static void create_nim_sum(int[][] multiples, Row[] rows, int[] modes) {
        /*
         * TEST CASES: 
         * 1. When there is one row left; remove all rocks except one 
         * 2. When there are two rows if one of them has only one stone, 
         *    remove the other entire row 
         * 3. When two rows have only one stone; leave the third with
         *    one stone too 
         * 4. When there are three rows: 
         *      a. If the greatest unparied multiple is alone, sum the others, 
         *         and remove the difference from the pile
         *         that has the greatest one 
         *      b. If it isn't alone, then add the unpaired multiples in the 
         *         row with the greatest multiple, sum the rest, and remove the
         *         difference from the pile that has the greatest multiple.
         */

        // Stores how many unpaired multiples there are
        int unpairedMults = 0;
        // Stores the value of the unpaired multiples
        int[] mults = new int[4];
        // Stores the index of the row where the i-th unpaired multiple is located
        int[] indices = new int[4];

        // Iterate through the 2-D array that holds the amounts of each pile broken down
        // into multiples
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 4; column++) {
                // If the number is 1, 2, 4, or 8; if the mode is odd;
                // and if the number has not yet been included as an unpaired multiple
                if (multiples[row][column] != 0 && modes[column] % 2 != 0
                        && numberIsNotInArray(multiples[row][column], mults)) {
                    // Add the location and value into each array, respectively. Also increment the
                    // unpairedMults value
                    indices[unpairedMults] = row;
                    mults[unpairedMults] = multiples[row][column];
                    unpairedMults++;
                }
            }
        }

        // TEST CASE 1: When there is only one row left; leave only one remaining to win
        if (rows[0].rocks == 0 && rows[1].rocks == 0) { rows[2].removeRocks(rows[2].rocks - 1); } 
        else if (rows[0].rocks == 0 && rows[2].rocks == 0) { rows[1].removeRocks(rows[1].rocks - 1); } 
        else if (rows[1].rocks == 0 && rows[2].rocks == 0) { rows[0].removeRocks(rows[0].rocks - 1); }
        // TEST CASE 2: When there are two rows and one row has only one stone;
        // remove the other row entirely; that's the optimal move
        else if (rows[0].rocks == 0 && rows[1].rocks > 1 && rows[2].rocks == 1) { rows[1].removeRocks(rows[1].rocks); } 
        else if (rows[0].rocks == 0 && rows[2].rocks > 1 && rows[1].rocks == 1) { rows[2].removeRocks(rows[2].rocks); }
        else if (rows[1].rocks == 0 && rows[0].rocks > 1 && rows[2].rocks == 1) { rows[0].removeRocks(rows[0].rocks); }
        else if (rows[1].rocks == 0 && rows[2].rocks > 1 && rows[0].rocks == 1) { rows[2].removeRocks(rows[2].rocks); }
        else if (rows[2].rocks == 0 && rows[1].rocks > 1 && rows[0].rocks == 1) { rows[1].removeRocks(rows[1].rocks); } 
        else if (rows[2].rocks == 0 && rows[0].rocks > 1 && rows[1].rocks == 1) { rows[0].removeRocks(rows[0].rocks); }
        // TEST CASE 3: When there are three rows, but two of them have only one stone;
        // only two row combo that is not a nim sum
        else if (rows[0].rocks == 1 && rows[1].rocks == 1 && rows[2].rocks > 1) { rows[2].removeRocks(rows[2].rocks - 1); } 
        else if (rows[0].rocks == 1 && rows[2].rocks == 1 && rows[1].rocks > 1) { rows[1].removeRocks(rows[1].rocks - 1); } 
        else if (rows[1].rocks == 1 && rows[2].rocks == 1 && rows[0].rocks > 1) { rows[0].removeRocks(rows[0].rocks - 1); }
        // TEST CASE 4
        else {
            /* GUM = greatest unpaired multiple 
             * To find a nim sum, there are two possible ways to do so. The first is to find 
             * the GUM, sum the other unpaired multiples, and then remove the difference between 
             * the GUM and the sum in the row that has the GUM. The second option is when the GUM 
             * is not alone. In this case, add all the unpaired multiples in the row that has the 
             * GUM, add all other unpaired multiples, and remove the difference between these two sums
             * from the row that has the GUM. */

            // Stores GUM
            int MaxUnpairedMult = 0;
            // Stores GUM's locations
            int MaxMultLocation = 0;
            // Will store the sum of unpaired multiples besides the GUM
            int Sum = 0;
            // If the GUM isn't alone, it will store the sum of all unpaired multiples
            // in the row that has the GUM.
            int MaxSum;

            // Finds the greatest unpaired multiple
            for (int i = 0; i < 4; i++) {
        // System.out.println(mults[i] + " " + indices[i]);
                if (mults[i] > MaxUnpairedMult) {
                    MaxUnpairedMult = mults[i];
                    MaxMultLocation = indices[i];
                }
            }
            // In case the mode of some number is three, adjust the index to 
            // find the zero nim sum
            for (int i = 0; i < 4; i++) {
                if (modes[i] == 3) {
                    for (int index = 0; index < 4; index++) {
                        if (mults[index] == Math.pow(2, i)) {
                            indices[index] = MaxMultLocation;
                        }
                    }
                }
            }
            /*
             * Initially, MaxSum will be equal to the GUM. However, if another unpaired
             * multiple is with the GUM, it will be added to MaxSum; otherwise, it will be
             * added to the other Sum.
             */
            MaxSum = MaxUnpairedMult;
            for (int i = 0; i < 4; i++) {
                if (mults[i] != MaxUnpairedMult) {
                    if (indices[i] != MaxMultLocation) {
                        Sum += mults[i];
                    } else {
                        MaxSum += mults[i];
                    }
                }
            }

            // Remove difference
    // System.out.println("Sum: " + Sum);
    // System.out.println("Max Sum: " + MaxSum);
    // System.out.println("Max Unpaired Multiple: " + MaxUnpairedMult);
            rows[MaxMultLocation].removeRocks(MaxSum - Sum);
        }
    }

    void turn(Row A, Row B, Row C) {
        // Save given rows to an array to then pass on to other methods, if needed.
        Row[] rows = { A, B, C };

        // 2-D array that keeps that holds multiples of each row
        int[][] Multiples = new int[3][4];

        // for every row in the array rows...
        for (int i = 0; i < 3; i++) {
            // Divide each row into multiples and store it in the same index as they are
            // found in rows
            Multiples[i] = divide_row_into_multiples(rows[i].rocks);
        }

        // Create an array that holds the modes of each multiple
        int[] modes = new int[4];

        // Find the modes of all multiples (1, 2, 4, & 8)
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 4; column++) {
                if (Multiples[row][column] != 0) {
                    modes[column] += 1;
                }
            }
        }

        // Iterate through the modes array. If any number is not even, then there is no
        // nim sum.
        // Otherwise, one is present, so only remove one stone, the least chaotic move
        // the bot can perform
        if (find_nim_sum(modes)) {
            // remove one rock from any row
            for (int i = 0; i < 3; i++) {
                if (IntStream.of(Multiples[i]).sum() != 0) {
                    rows[i].removeRocks(1);
                    break;
                }
            }
        } else {
            create_nim_sum(Multiples, rows, modes);
        }
    }

    static boolean numberIsNotInArray(int numb, int[] arr) {
        for (int number : arr) {
            if (number == numb) {
                return false;
            }
        }
        return true;
    }
}