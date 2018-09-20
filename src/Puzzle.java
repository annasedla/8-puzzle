import java.util.*;

public class Puzzle {

    /*
    STATES
     */

    //goal state
    private int goalState [][]= {{0,1,2}, {3,4,5}, {6,7,8}};

    //current state
    private int currentState [][] = {{0,1,2}, {3,4,5}, {6,7,8}};

    //position of blank
    private int currentBlank [] = new int [2];

    /*
    PRIORITY QUEUE
     */
    private PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();

    /*
    HELPER METHODS
     */

    // Print the state of the puzzle
    public void printCurrentState (){

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

    // Print the state of the puzzle
    public void printState (int [][] state){

        String print = "";
        System.out.println("The state of the puzzle is:");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0)
                    print += "b";
                else
                    print += state [i][j];
                print += " ";
            }
            System.out.println(print);
            print = "";

        }
    }

    // Convert the state array to string
    public String toString(int [][] state){
        String result = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0){
                    result += "b";
                } else {
                    result += Integer.toString(state[i][j]);
                }
            }
            result += " ";
        }
        return result;
    }

    // Set the state of the puzzle
    public void setState(String s)
    {
        int i = 0;
        int j = 0;

        for (int k = 0; k < s.length(); k++){
            if (s.charAt(k) == 'b'){
                currentState [i][j] = 0;
                currentBlank[0] = i;
                currentBlank[1] = j;

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
    public int[][] move(String direction, int[][] state){
        int blank[] = new int[2];

        //locate the blank tile for the inputted state
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0){
                    blank[0] = i;
                    blank[1] = j;
                }
            }
        }
        if (direction == "up" && blank[0] != 0){
            //swap the tiles
            int x = state[blank[0]-1][blank[1]];
            state[blank[0]-1][blank[1]] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [0] = blank[0] - 1;

            return state;

        } else if (direction == "down" && blank[0] != 2 ){
            //swap the tiles
            int x = state[blank[0]+1][blank[1]];
            state[blank[0]+1][blank[1]] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [0] = blank[0] + 1;

            return state;

        } else if (direction == "left" && blank[1] != 0){
            //swap the tiles
            int x = state[blank[0]][blank[1]-1];
            state[blank[0]][blank[1] - 1] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [1] = blank[1] - 1;

            return state;

        } else if (direction =="right" && blank[1] != 2){
            //swap the tiles
            int x = state[blank[0]][blank[1]+1];
            state[blank[0]][blank[1] + 1] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [1] = blank[1] + 1;

            return state;

        } else {

            //return the same without changing it??
            return state;
        }
    }

    // Make n random moves from the goal state
    public void randomizeState(int n, int[][] state){

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
                move("up", state);
            } else if (number == 2){
                move("down", state);
                System.out.println("Moving blank tile down.");
            } else if (number == 3){
                move ("left", state);
                System.out.println("Moving blank tile left.");
            } else if (number == 4){
                move ("right", state);
                System.out.println("Moving blank tile right.");
            }
            printCurrentState();
            System.out.println("---------------------");
        }
    }

    /*
    MORE HELPER METHODS
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

    //Helper class for node
    private class Node{
        private int [][] state;
        private Node previous;
        private String move;
        private int pathCost;

        private Node(int [][] state, Node previous, String move, int pathCost){
            this.state = state;
            this.previous = previous;
            this.move = move;
            this.pathCost = pathCost;
        }
    }

    /*
    A STAR
     */

    //using A * to solve the puzzle
    //add current state onto priority queue
    //create four children with four moves and add them onto priority queue
    //pop the smallest one
    //repeat until goal state
    public void aStar(){

        //stores number of moves in the algorithm
        int numberOfMoves = 0;

        //create a parent node from the current state
        Node parent =  new Node (currentState, null, "", 0);

        //add current state onto priority queue
        priorityQueue.add(parent);

        while (currentState != goalState){

            //pop the queue
            Node current = priorityQueue.poll();

            //Change current state to reflect the node pulled off the queue
            setState(toString(current.state));

            //explore left, right, up, down
            Node left = new Node(move("left", current.state), current,
                    "left", calculateH1()+calculateH2()+1);
            Node right = new Node(move("right", current.state), current,
                    "right", calculateH1()+calculateH2()+1);
            Node up = new Node(move("up", current.state), current,
                    "right", calculateH1()+calculateH2()+1);
            Node down = new Node(move("down", current.state), current,
                    "right", calculateH1()+calculateH2()+1);

            //push on the queue
            priorityQueue.add(left);
            priorityQueue.add(right);
            priorityQueue.add(up);
            priorityQueue.add(down);

            //increase the number of moves
            numberOfMoves++;
        }
        printCurrentState();
    }

    /*
    MAIN METHOD
     */
    public static void main(String[] args) {

        Puzzle puzzle = new Puzzle();
        System.out.println("Welcome to Anna's Project!");
        System.out.println();

        int someState [][]= {{0,1,2}, {3,4,5}, {6,7,8}};

        //puzzle.setState("1b2345678");
        //puzzle.printCurrentState();

        puzzle.move("down", someState);

        puzzle.printState(someState);

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
                puzzle.printCurrentState();
            if (args[0].equals("setState"))
                puzzle.setState(args[1] + args[2] + args[3]);
        }
        else
            System.out.println("No command line arguments found, check your inputs.");
    }
}