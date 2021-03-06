package sarsa;

/*
The code below is based on the code for the Q-Learning algorithm from the lecture Systemanalysis ("SYSA")
by Prof. Dr. F. Mehler in TH Bingen. The primary source though for this code is:
http://mnemstudio.org/ai/path/q_learning_java_ex1.txt
 */

import java.util.ArrayList;
import java.util.Random;

/**
 * This class performs the reinforcement learning algorithm "SARSA" on a 32-room maze and searches with the help of it
 * a possible way out, out of each room. (Optionally it can do the same task with the Q-Learning algorithm).
 */
public class SarsaAlgorithm {

    // Size of the Q-Matrix. This constant is important through the whole algorithm.
    private final int Q_SIZE = 33;

    // Q-Matrix which contains the agent's knowledge. It has the same size as the reward matrix R.
    private final int[][] Q = new int[Q_SIZE][Q_SIZE];

    // These are the starting rooms, from which the search for a way out will begin successively.
    private final int[] INITIAL_STATES = new int[] {0, 1, 2, 3, 4, 5,  6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};

    // The reward matrix R which resembles the current labyrinth with valid paths and reward points
    private final int[][] R = new int[][] {

            // 0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31  32
            { -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 0
            { -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 1
            { -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100},  // 2
            { -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 3
            {  0, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 4
            { -1,  0, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 5
            { -1, -1,  0,  0, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 6
            { -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 7
            { -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 8
            { -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 9
            { -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 10
            { -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 11
            { -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 12
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 13
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 14
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 15
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 16
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 17
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 18
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 19
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1 },  // 20
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 21
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 22
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 23
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1 },  // 24
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1 },  // 25
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1 },  // 26
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1,  0,  0, -1 },  // 27
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, 100},  // 28
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1 },  // 29
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1 },  // 30
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1 },  // 31
            { -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, 100}   // 32
    };

    // How many times the algorithm will repeat
    private final int ITERATIONS = 1000;

    // The weight of the reward
    private final double ALPHA = 0.8;

    // The rate of choosing a random state in the next action. Relevant for SARSA.
    private final double EPSILON = 0.2;

    // If true, the Q-Learning algorithm will run.
    private final boolean Q_LEARNING_MODE;

    // How many paths were correctly found.
    private int correctPathCounter = 0;

    // Collects throughout the run the initial states from which the paths were correctly found.
    private final ArrayList<Integer> correctPathsFromStates = new ArrayList<>();

    // How many times the agent got stuck in a loop.
    private int loopCounter = 0;

    // From which initial states the paths contained a loop
    private final ArrayList<Integer> loopsFromStates = new ArrayList<>();

    /**
     * The actual SARSA algorithm. The process in this class has three major steps (see below).
     */
    public SarsaAlgorithm(boolean Q_LEARNING_MODE) {
        this.Q_LEARNING_MODE = Q_LEARNING_MODE;
        /*
        Some visual output for the console indicating the start of the program.
         */
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("----------###----------");
        System.out.println("---------SARSA---------");
        System.out.println("-----------#-----------");
        System.out.println();

        /*
         Step 1. The agent learns his environment with the help of the algorithm,
         it's action-selection-policy and both of the matrices Q and R.
         */
        learning();

        /*
        Step 2. In this step all the available paths from each single room
        (if such were found) are read and printed out to the console.
         */
        calculatePaths();

        // Step 3. At the end the final state of the Q-Matrix and statistics are printed to the console.
        this.printStats();
    }

    /**
     * Prints the current state of the Q-Matrix to the console
     */
    private void printQMatrix() {
        System.out.println();
        System.out.println();
        System.out.println("-== Q Matrix values: ==-");
        System.out.println();
        System.out.println("\t   [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10][11][12][13][14][15][16][17][18][19][20][21][22][23][24][25][26][27][28][29][30][31][32]");
        for (int i = 0; i < Q_SIZE; i++) {
            if (i < 10) {
                System.out.print(" [" + i + "]\t");
            } else {
                System.out.print("[" + i + "]\t");
            }
            for (int j = 0; j < Q_SIZE; j++) {
                System.out.print(Q[i][j] + ",\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints the information about which algorithm was used, how many paths were found
     * and the values of Alpha and Epsilon to the console. (Should do this at the end of the program).
     */
    private void printStats() {
        String algorithm;

        if (this.Q_LEARNING_MODE) {
            algorithm = "Q-Learning";
        } else {
            algorithm = "SARSA";
        }

        this.printQMatrix();
        System.out.println();
        System.out.println("------=== Final statistics: ===------");
        System.out.println();
        System.out.println("Algorithm used:\t\t\t" + algorithm);
        System.out.println("Alpha-Value:\t\t\t" + ALPHA);
        if (!this.Q_LEARNING_MODE) {
            System.out.println("Epsilon-Value:\t\t\t" + EPSILON);
        }
        System.out.println("Correct paths found:\t" + this.correctPathCounter + "/" + this.INITIAL_STATES.length);
        System.out.println("Correct paths from:\t\t" + correctPathsFromStates.toString());
        System.out.println("Stuck in loops:\t\t\t" + this.loopCounter);
        System.out.println("Loops from:\t\t\t\t" + loopsFromStates.toString());
        System.out.println("Iterations:\t\t\t\t" + ITERATIONS);
    }

    /**
     * Function, returning the first valid state, to which the agent can move from the inputState
     * @param inputState - the state from which the valid path will be searched
     * @return validState - the returned valid state to which the agent can move from inputState
     */
    private int getFirstValidTransition(int inputState) {
        int[] rValues = R[inputState].clone();
        int validState = -99;

        for (int i = 0; i < rValues.length; i++) {
            if (rValues[i] > -1) {
                validState = i;
            }
        }

        return validState;
    }

    /**
     * Returns the best state with it's number and it's maximum value
     * from the Q-Matrix as the next action for an input state.
     * Only states from valid transitions are chosen (R-Matrix value for this
     * action from inputState to bestState mustn't be -1).
     *
     * @param inputState - input state for which the next action
     *               with maximum value is returned
     *               from the corresponding Q-Matrix row
     *
     * @return int array - an array with the length of 2
     *                  which represents the best state returned.
     *                  The first value from this array is 'bestState'
     *                  which is the number of the returned state,
     *                  the second value is this state's Q-Value
     */
    private int[] getMaxAction(int inputState) {
        int[] qValues = Q[inputState].clone();

        // Just in Case: make sure that only valid transitions will be considered.
        int bestState = this.getFirstValidTransition(inputState);
        int qMaxValue = qValues[bestState];

        for (int i = 0; i < qValues.length; i++) {
            if ((qValues[i] >= qMaxValue) && (R[inputState][i] > -1)) {
                qMaxValue = qValues[i];
                bestState = i;
            }
        }
        return new int[] {bestState, qMaxValue};
    }

    /**
     * Selects randomly a state from the Q-Matrix as the next action for an input state.
     * Only states from valid transitions are chosen (R-Matrix value for this
     * action from inputState to randomState mustn't be -1).
     *
     * @param inputState - input state/room for which the next action/state
     *               is chosen randomly from the corresponding Q-Matrix row
     *
     * @return int array - an array with the length of 2
     *                    which represents the random state returned.
     *                    The first value from this array is 'randomState'
     *                    which is the number of the returned state,
     *                    the second value is this state's Q-Value
     */
    private int[] getRandomAction(int inputState) {
        int randomState;
        int[] qValues = Q[inputState].clone();
        do {
            randomState = new Random().nextInt(qValues.length);
        } while (R[inputState][randomState] == -1);
        return new int[] {randomState, qValues[randomState]};
    }

    /**
     * This function bears the functionality of the ε-greedy Policy and returns either
     * the best next state or a random state for an action given. This is dependent from the
     * value of the 'EPSILON' constant and a random number, which is generated in this function.
     *
     * @param action - input state/action for which the next state is selected with the ε-greedy Policy
     *
     * @return int array - an array with the length of 2
     *                    which represents the state returned.
     *                    The first value from this array is 'state'
     *                    is the number of the returned state,
     *                    the second value is this state's Q-Value
     */
    private int[] epsilonGreedyPolicy(int action) {

        // A random number between 0 and 1
        double random = Math.random();

        /*
        If the random number is smaller than epsilon - return a random action,
        otherwise return an action with maximum Q-Value
         */
        return random < EPSILON ? this.getRandomAction(action) : this.getMaxAction(action);
    }

    /**
     * Chooses the next action for the current state and updates the Q-Matrix
     * in each run and each episode. Does this considering which algorithm-mode is set.
     *
     * @param currentState - the state/room for which the next action is chosen.
     * @return next state.
     */
    private int chooseNextAction(int currentState) {

        int nextState;

        /*
         If Q_LEARNING_MODE is true, the next state will be selected randomly
         otherwise the Epsilon-Greedy-Policy is used.
         */
        if (Q_LEARNING_MODE) {
            nextState = this.getRandomAction(currentState)[0];
        } else {
            nextState = this.epsilonGreedyPolicy(currentState)[0];
        }

        // If the final state is reached the Q-Matrix is not updated anymore.
        if (currentState == 32) {
            return 32;
        }

        int qValue;

        /*
        Here the new Q-Value is calculated and inserted into the Q-Matrix for the current state.
        If Q-Learning-Mode is active the Q-Value is always the max value for the next state.
        Otherwise it is selected with the ε-greedy Policy.
         */
        if (Q_LEARNING_MODE) {
            qValue = this.getMaxAction(nextState)[1];
        } else {
            qValue = this.epsilonGreedyPolicy(nextState)[1];
        }

        // Updates the Q-Matrix.
        Q[currentState][nextState] = (int) (R[currentState][nextState] + (this.ALPHA * qValue));

        currentState = nextState;

        return currentState;
    }

    /**
     * In this method an episode is completed. The agent walks through
     * the labyrinth from each initial state until the finish is found.
     *
     * @param initialState - the initial state from which the agent will start
     */
    private void episode(final int initialState) {
        int currentState = initialState;
        do {
            currentState = chooseNextAction(currentState);
        } while (currentState != 32);
    }

    /**
     * In this method the agent is learning and trying out different paths from all of the initial states (rooms).
     */
    private void learning() {
        for (int j = 0; j < ITERATIONS; j++) {
            if (this.Q_LEARNING_MODE) {
                System.out.println("ITERATION " + j + "/" + ITERATIONS + " (Q-Learning)");
            } else {
                System.out.println("ITERATION " + j + "/" + ITERATIONS + " (SARSA)");
            }
            for (int initial_state : INITIAL_STATES) {
                episode(initial_state);
            }
        }
    }

    /**
     * Uses the final state of the Q-Matrix to calculate all the paths
     * that the agent gone through and prints them to the console.
     */
    private void calculatePaths() {
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println("Paths out of each room:");
        System.out.println();
        int initState;
        for (int currentState : INITIAL_STATES) {
            System.out.print("Path from [" + currentState + "]: ");
            initState = currentState;
            int prevState = currentState;
            int newState;
            int highValue = 0;
            int loopCount = 0;
            while (currentState <= INITIAL_STATES[INITIAL_STATES.length - 1]) {
                highValue = getMaxAction(currentState)[1];
                for (newState = 0; newState < Q_SIZE; newState++) {
                    if (highValue == Q[currentState][newState]) {
                        break;
                    }
                }

                /*
                 If there are only zeroes in a Q-Matrix row, the path wasn't found correctly.
                 (Probably not enough Iterations)
                 */
                if (highValue == 0) {
                    System.out.println("No correct path found.");
                    break;
                } else {
                    System.out.print("(" + currentState + ") --> ");

                    /*
                     Checks if there are loops, by comparing the previous state
                     and the state after the current state.
                     */
                    if (prevState == newState) {
                        loopCount++;

                        /*
                         If there are more then 2 repetitions, increment the loop counter
                         and add the initial state to the "Loop-ArrayList".
                         Start with the next initial state after that.
                         */
                        if (loopCount > 2) {
                            System.out.println("Agent stuck in a loop...");
                            loopsFromStates.add(initState);
                            this.loopCounter++;
                            break;
                        }
                    }
                    prevState = currentState;
                    currentState = newState;
                }
            }
            /*
            If no loop was found in the Q-Matrix & at least one column for the current row in it
            is greater than 0, then the goal is printed out and the counter of correctly found paths is increased.
             */
            if (loopCount <= 2 && highValue != 0) {
                System.out.print("<<32>>\n");
                this.correctPathCounter++;
                correctPathsFromStates.add(initState);
            }
        }
    }

    public static void main(String[] args) {
        new SarsaAlgorithm(false);
    }

}
