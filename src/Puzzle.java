import java.util.*;
import java.io.*;

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

    //Print the state of the puzzle
    public void printState (){

        String print = "";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState[i][j] == 0)
                    print += "b";
                else
                    print += currentState [i][j];
            }
        }
        System.out.println("The state of the puzzle is:");
        System.out.println(print);
    }

    //Set the state of the puzzle
    public void setState(String s)
    {
        int k = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (s.charAt(k) == ('b')) {
                    currentState[i][j] = 0;
                } else {
                    currentState[i][j] = s.charAt(k) - '0';
                }
                k++;
            }
        }
    }

    /*
    MAIN METHOD
     */
    public static void main(String[] args) {

        Puzzle puzzle = new Puzzle();
        System.out.println("Welcome to Anna's Project!");
        System.out.println();

        puzzle.setState("b12345678");
        puzzle.printState();

        /*
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName)));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                // process line
            }
        } finally {
            br.close();
        }*/

        //Check for input
        if (args.length > 0) {
            if (args[0].equals( "printState"))
                puzzle.printState();
            if (args[0].equals("setState"))
                puzzle.setState(args[1] + args[2] + args[3]);
        }
        else
            System.out.println("No command line arguments found, check your inputs.");
    }
}
