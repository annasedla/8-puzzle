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
    private int currentBlank [] = new int [2];
    private int maxNodes = 10000000;
    private int seed = 123456789;

    /*
    HELPER METHODS
     */

    // Print the state of the puzzle
    public void printState (){
        printState(currentState);
    }

    // Print any chosen state of the puzzle
    public String printState (int [][] state){

        String print = "";
        System.out.println("The state of the puzzle is:");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0)
                    print += " ";
                else
                    print += state [i][j];
                print += " ";
            }
            System.out.println(print);
            print = "";

        }
        return print;
    }

    // Convert the state array to string
    private String toString(int [][] state){
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

    //gets the current state of the puzzle
    public int[][] getState(){
        return currentState;
    }

    //move method without state input
    public int[][] move(String direction){
        return move(direction, currentState);
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
        if (direction.equals("up") && blank[0] != 0){
            //swap the tiles
            int x = state[blank[0]-1][blank[1]];
            state[blank[0]-1][blank[1]] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [0] = blank[0] - 1;

            return state;

        } else if (direction.equals("down") && blank[0] != 2 ){
            //swap the tiles
            int x = state[blank[0]+1][blank[1]];
            state[blank[0]+1][blank[1]] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [0] = blank[0] + 1;

            return state;

        } else if (direction.equals("left") && blank[1] != 0){
            //swap the tiles
            int x = state[blank[0]][blank[1]-1];
            state[blank[0]][blank[1] - 1] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [1] = blank[1] - 1;

            return state;

        } else if (direction.equals("right") && blank[1] != 2){
            //swap the tiles
            int x = state[blank[0]][blank[1]+1];
            state[blank[0]][blank[1] + 1] = 0;
            state[blank[0]][blank[1]] = x;

            //overwrite location of blank tile
            blank [1] = blank[1] + 1;

            return state;

        } else {
            return state;
        }
    }

    // Make n random moves from the goal state
    public void randomizeState(int n){

        //first set to goal state
        setState("b12345678");
        Random rand = new Random(seed);

        //randomize the state of the puzzle
        for (int i = 0; i < n; i++){
            //create a new random variable at each iteration
            int number = rand.nextInt(4) + 1;

            if (number == 1){
                move("up");
            } else if (number == 2){
                move("down");
            } else if (number == 3){
                move ("left");
            } else if (number == 4){
                move ("right");
            }
        }
    }

    //calculating h1 heuristic (misplaced tiles)
    private int calculateH1(int[][] state){

        int misplacedTiles = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] != goalState[i][j]){
                    if (state[i][j] != 0) {
                        misplacedTiles++;
                    }
                }
            }
        }
        return misplacedTiles;
    }

    //calculating h2 heuristic (manhattan distances)
    public int calculateH2(int[][] state){

        //manhatttan distance between all the tiles
        int distance = 0;
        int a = 0;
        int b = 0;

        //oof triple loop but constant time so its okay
        for (int k = 0; k < 9; k++){
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if (state[i][j] == k && k != 0){
                        distance += Math.abs(i - b);
                        distance += Math.abs(j - a);
                        if (a > 1) {
                            a = 0;
                            b ++;
                        } else {
                            a ++;
                        }
                    } else if (state[i][j] == k && k == 0){
                        if (a > 1) {
                            a = 0;
                            b ++;
                        } else {
                            a++;
                        }
                    }
                }
            }
        }
        return distance;
    }

    public void maxNodes(int n){
        maxNodes = n;
    }

    //Helper class for node
    private class Node implements Comparable<Node>{
        private int [][] state;
        private Node previous;
        private String move;
        private int pathCost;
        private int heuristics;

        private Node(int [][] state, Node previous, String move, int pathCost){
            if (move.equals("")) {
                this.state = state;
            } else {
                this.state = move(move, cloneState(state));
            }
            this.previous = previous;
            this.move = move;
            this.pathCost = pathCost;
        }

        @Override
        public int compareTo(Node node) {
            return (this.pathCost+this.heuristics) > (node.pathCost + node.heuristics) ? 1 : -1;
        }

        private int[][] cloneState(int[][] state){
            int[][] newState = new int [3][3];
            for(int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++) {
                    newState[i][j] = state[i][j];
                }
            }
            return newState;
        }
    }

    /*
    A STAR
     */

    public void solveAStar(String s){

        //priority queue and stack to output moves at the end
        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
        Stack<Node> stack = new Stack<>();

        //create a parent node from the current state
        Node parent =  new Node (currentState, null, "start", 0);
        Node current;
        Node stackNode;
        Node printNode;

        //clear priority queue and stack
        priorityQueue.clear();
        stack.empty();

        //add current state onto priority queue
        priorityQueue.add(parent);

        //how long it takes the algorithm to solve the puzzle
        int iterations = 0;
        int nodeCount = 0;

       while (!toString(currentState).equals(toString(goalState))){

            if (nodeCount > maxNodes)
                //throw an exception and terminate the method
                throw new IndexOutOfBoundsException();

            //pop the queue
            current = priorityQueue.poll();
            iterations++;

            //Change current state to reflect the node pulled off the queue
            setState(toString(current.state));

            //explore left, right, up, down
            Node left = new Node(current.state, current, "left", current.pathCost + 1);
            if (s.equals("h1")){
                left.heuristics = calculateH1(left.state);
            } else {
                left.heuristics = calculateH2(left.state);
            }

            Node right = new Node(current.state, current, "right", current.pathCost + 1);
            right.heuristics = calculateH2(right.state);
           if (s.equals("h1")){
               right.heuristics = calculateH1(right.state);
           } else {
               right.heuristics = calculateH2(right.state);
           }

            Node up = new Node(current.state, current, "up", current.pathCost + 1);
            up.heuristics = calculateH2(up.state);
           if (s.equals("h1")){
               up.heuristics = calculateH1(up.state);
           } else {
               up.heuristics = calculateH2(up.state);
           }

            Node down = new Node(current.state, current, "down", current.pathCost + 1);
            down.heuristics = calculateH2(down.state);
           if (s.equals("h1")){
               down.heuristics = calculateH1(down.state);
           } else {
               down.heuristics = calculateH2(down.state);
           }

            //push on the queue
            if (!toString(current.state).equals(toString(left.state))){
                priorityQueue.add(left);
                nodeCount++;
            }

            if (!toString(current.state).equals(toString(right.state))){
                priorityQueue.add(right);
                nodeCount++;
            }

            if (!toString(current.state).equals(toString(up.state))){
                priorityQueue.add(up);
                nodeCount++;
            }

            if (!toString(current.state).equals(toString(down.state))){
                priorityQueue.add(down);
                nodeCount++;
            }

            //goes through during last iteration
            if (toString(current.state).equals(toString(goalState))){

                //calculates the number of nodes
                System.out.println("Solution length: " + current.pathCost);
                System.out.println("Number of total loops: " + (iterations-1));
                System.out.println();

                stackNode = current;

                //backtrace all the nodes using a stack
                while(stackNode.previous != null){
                    stack.add(stackNode);
                    stackNode = stackNode.previous;
                }

                stack.add(stackNode);

                while(!stack.isEmpty()){
                    printNode = stack.pop();
                    System.out.println(printNode.move);
                    printState(printNode.state);
                    System.out.println();
                }
            }
        }
    }

    /*
    LOCAL BEAM SEARCH
     */

    //change evaluation function to h1 + h2
    //compare states so they dont repeat

    public void solveBeamSearch(int k){

        // Priority queues and stack to output moves at the end
        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
        PriorityQueue<Node> priorityQueue2 = new PriorityQueue<>();
        Stack<Node> stack = new Stack<>();

        HashMap<String, Node> map = new HashMap<>();

        //create a parent node from the current state
        Node parent =  new Node (currentState, null, "start", 0);
        Node current;
        Node stackNode;
        Node printNode;

        //clear priority queue and stack
        priorityQueue.clear();
        stack.empty();

        //add current state onto priority queue
        priorityQueue.add(parent);

        //how long it takes the algorithm to solve the puzzle
        int iterations = 0;

        while (!toString(currentState).equals(toString(goalState))){

            current = priorityQueue.poll();
            iterations++;

            //Change current state to reflect the node pulled off the queue
            setState(toString(current.state));

            //explore left, right, up, down
            Node left = new Node(current.state, current, "left", 0);
            left.heuristics = calculateH2(left.state) + calculateH1(left.state);

            Node right = new Node(current.state, current, "right", 0);
            right.heuristics = calculateH2(right.state) + calculateH1(right.state);

            Node up = new Node(current.state, current, "up", 0);
            up.heuristics = calculateH2(up.state) + calculateH1(up.state);

            Node down = new Node(current.state, current, "down", 0);
            down.heuristics = calculateH2(down.state) + calculateH1(down.state);

            //push on the queue
            if (!toString(current.state).equals(toString(left.state)) && map.get(toString(left.state)) == null){
                priorityQueue.add(left);
                map.put(toString(left.state), left);
            }


            if (!toString(current.state).equals(toString(right.state)) && map.get(toString(right.state)) == null){
                priorityQueue.add(right);
                map.put(toString(right.state), right);
            }

            if (!toString(current.state).equals(toString(up.state)) && map.get(toString(up.state)) == null){
                priorityQueue.add(up);
                map.put(toString(up.state), up);
            }

            if (!toString(current.state).equals(toString(down.state)) && map.get(toString(down.state)) == null){
                priorityQueue.add(down);
                map.put(toString(down.state), down);
            }

            // Wipe the queue if over k nodes in it
            if  (priorityQueue.size() > k){
                for (int i = 0; i < k; i++)
                    priorityQueue2.add(priorityQueue.poll());
                priorityQueue.clear();

                for (int i = 0; i < k; i++)
                    priorityQueue.add(priorityQueue2.poll());
                priorityQueue2.clear();
            }

            //goes through during last iteration
            if (toString(current.state).equals(toString(goalState))){

                stackNode = current;
                int path = 0;

                //backtrace all the nodes using a stack
                while(stackNode.previous != null){
                    stack.add(stackNode);
                    stackNode = stackNode.previous;
                    path++;
                }

                stack.add(stackNode);

                //calculates the number of nodes
                System.out.println("Solution length: " + path);
                System.out.println("Number of total loops: " + (iterations-1));
                System.out.println();

                while(!stack.isEmpty()){
                    printNode = stack.pop();
                    System.out.println(printNode.move);
                    printState(printNode.state);
                    System.out.println();
                }
            }
        }
    }

    /*
    MAIN METHOD
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{

        Puzzle puzzle = new Puzzle();
        System.out.println("Welcome to Anna's Project!");
        System.out.println();

        try
        {
            FileInputStream textFile = new FileInputStream ("test.txt");
            System.out.println("File test.txt has been opened.");

            Scanner inFile = new Scanner (textFile);

            while(inFile.hasNextLine())
            {
                String oneline = inFile.nextLine();
                System.out.println(oneline);
                String[] items = oneline.split(" ");

                if (items[0].equals("printState")){
                    puzzle.printState();
                } else if (items[0].equals("setState")){
                    puzzle.setState(items[1] + " " + items[2] + " " + items[3]);
                } else if (items[0].equals("randomizeState")){
                    puzzle.randomizeState(Integer.parseInt(items[1]));
                } else if (items[0].equals("move")){
                    puzzle.move(items[1]);
                } else if (items[0].equals("maxNodes")) {
                    puzzle.maxNodes(Integer.parseInt(items[1]));
                } else if (items[0].equals("solve") && items[1].equals("beam")) {
                    puzzle.solveBeamSearch(Integer.parseInt(items[2]));
                } else if (items[0].equals("solve") && items[1].equals("A-star")) {
                    if (items[2].equals("h1"))
                        puzzle.solveAStar("h1");
                    else
                        puzzle.solveAStar("h2");
                }
            }
        }

        catch (FileNotFoundException fnfe)
        {
            System.out.println("File data.txt was not found!");
        }
    }
}