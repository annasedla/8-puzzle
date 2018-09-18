import java.util.*;
import java.io.*;

public class Puzzle {

    /*
    STATES
     */

    //goal state
    private int goalState [][]= {{0,1,2}, {3,4,5}, {6,7,8}};

    //current state
    private int currentState [][] = {{0,1,2}, {3,4,5}, {6,7,8}};

    //position of blank
    private int blank [] = {0,0};

    /*
    HELPER METHODS
     */

    // Print the state of the puzzle
    public void printState (){

        String print = "";
        System.out.println("The state of the puzzle is:");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState[i][j] == 0)
                    print += "b";
                else
                    print += currentState [i][j];
                    print += " ";
            }
            System.out.println(print);
            print = "";

        }
    }

    // Set the state of the puzzle
    public void setState(String s)
    {
        int i = 0;
        int j = 0;

        for (int k = 0; k < s.length(); k++){
            if (s.charAt(k) == 'b'){
                currentState [i][j] = 0;
                blank[0] = i;
                blank[1] = j;

                if (j > 1) {
                    j = 0;
                    i++;
                } else {
                    j++;
                }

            } else if (Character.isDigit(s.charAt(k))){
                currentState[i][j] = s.charAt(k) - '0';

                if (j > 1) {
                    j = 0;
                    i++;
                } else {
                    j++;
                }
            }
        }
    }

    // Move the blank piece around
    public void move(String s){
        if (s == "up" && blank[0] != 0){
            //swap the tiles
            int x = currentState[blank[0]-1][blank[1]];
            currentState[blank[0]-1][blank[1]] = 0;
            currentState[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [0] = blank[0] - 1;

        } else if (s == "down" && blank[0] != 2 ){
            //swap the tiles
            int x = currentState[blank[0]+1][blank[1]];
            currentState[blank[0]+1][blank[1]] = 0;
            currentState[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [0] = blank[0] + 1;

        } else if (s == "left" && blank[1] != 0){
            //swap the tiles
            int x = currentState[blank[0]][blank[1]-1];
            currentState[blank[0]][blank[1] - 1] = 0;
            currentState[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [1] = blank[1] - 1;

        } else if (s =="right" && blank[1] != 2){
            //swap the tiles
            int x = currentState[blank[0]][blank[1]+1];
            currentState[blank[0]][blank[1] + 1] = 0;
            currentState[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [1] = blank[1] + 1;

        } else {
            System.out.println("Not a valid move, please try again.");
        }
    }

    // Make n random moves from the goal state
    public void randomizeState(int n){

        //first set to goal state
        setState("b12345678");
        Random rand = new Random();

        //randomize the state of the puzzle
        for (int i = 0; i < n; i++){
            //create a new random variable at each iteration
            int number = rand.nextInt(4) + 1;
            System.out.println("Random number at this iteration: " + number);

            if (number == 1){
                System.out.println("Moving blank tile up.");
                move("up");
            } else if (number == 2){
                move("down");
                System.out.println("Moving blank tile down.");
            } else if (number == 3){
                move ("left");
                System.out.println("Moving blank tile left.");
            } else if (number == 4){
                move ("right");
                System.out.println("Moving blank tile right.");
            }
            printState();
            System.out.println("---------------------");
        }
    }

    /*
    SEARCH
     */

    //calculating h1 heuristic (misplaced tiles)
    public int calculateH1(){

        int misplacedTiles = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState[i][j] != goalState[i][j])
                    if (currentState[i][j] == 0) {
                        //do nothing since blank is not a tile
                    } else {
                        misplacedTiles++;
                }
            }
        }

        System.out.println("Misplaced tiles: " + misplacedTiles);
        return misplacedTiles;
    }

    //calculating h2 heuristic (manhattan distances)
    public int calculateH2(){

        //manhatttan distance between all the tiles
        int distance = 0;
        int a = 0;
        int b = 0;

        //oof triple loop but constant time so its okay
        for (int k = 0; k < 9; k++){
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if (currentState[i][j] == k && k != 0){

                        distance += Math.abs(i - b);
                        distance += Math.abs(j - a);
                        if (a > 1) {
                            a = 0;
                            b ++;
                        } else {
                            a ++;
                        }
                    } else if (currentState[i][j] == k && k == 0){
                        if (a > 1) {
                            a = 0;
                            b ++;
                        } else {
                            a ++;
                        }
                    }
                }
            }
        }
        System.out.println("Sum of all Manhattan distances: " + distance);
        return distance;
    }

    /*
    MAIN METHOD
     */
    public static void main(String[] args) {

        Puzzle puzzle = new Puzzle();
        System.out.println("Welcome to Anna's Project!");
        System.out.println();

        puzzle.setState("1b2345678");
        puzzle.printState();
        puzzle.calculateH1();
        puzzle.calculateH2();

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