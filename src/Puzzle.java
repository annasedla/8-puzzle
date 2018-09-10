import java.util.*;

public class Puzzle {

    /*
    STATES
     */

    //goal state
    int goalState [][]= {{0,1,2}, {3,4,5}, {6,7,8}};

    //current state
    int currentState [][] = new int [3][3];

    /*
    CONSTRUCTORS
     */



    /*
    HELPER METHODS
     */

    public String printState (){
        System.out.println("Current state of the puzzle: " + Arrays.toString(goalState));
        return ("Current state of the puzzle: " + Arrays.toString(goalState));
    }

    /*
    MAIN METHOD
     */
    public static void main(String[] args) {

        Puzzle puzzle = new Puzzle();
        System.out.println("Welcome to Anna's Project!");

        //Check for input
        if (args.length > 0)
        {
            for (int i = 0; i < args.length; i++)
            {
                if (args[0].equals( "printState"))
                    puzzle.printState();
            }

        }
        else
            System.out.println("No command line arguments found, check your inputs.");
    }
}
