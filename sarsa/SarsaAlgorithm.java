package sarsa;

/*
The code below is based on the code for the Q-Learning algorithm from the lecture Systemanalysis ("SYSA")
by Prof. Dr. F. Mehler in TH Bingen. The primary source though for this code is:
http://mnemstudio.org/ai/path/q_learning_java_ex1.txt
 */

import java.util.Random;

/**
 * This class performs the reinforcement learning algorithm "SARSA" on a 32-room maze and searches with the help of it
 * a shortest possible way out, out of each room. (Optionally it can do the same task with the Q-Learning algorithm).
 */
public class SarsaAlgorithm {

    // Size of the Q-Matrix. This constant is important through the whole algorithm.
    private final int Q_SIZE = 33;

    // These are the starting rooms, from which the search for a way out will begin successively.
    private final int[] INITIAL_STATES = new int[] { 0, 1, 2, 3, 4, 5,  6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };

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

    // Q-Matrix which contains the agent's knowledge. It has the same size as the reward matrix R.
    private final int[][] Q = new int[Q_SIZE][Q_SIZE];

    private final double ALPHA = 0.8;
    private final double EPSILON = 0.4;

    private final boolean qLearningMode;

    private int correctPathCounter = 0;

    /**
     * The actual SARSA algorithm. The process in this class has three major steps (see below).
     */
    public SarsaAlgorithm(boolean qLearningMode) {
        this.qLearningMode = qLearningMode;
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
        Step 2. In this step all the shortest paths from each single room
        (if such were found) are read and printed out to the console.
         */
        calculatePaths();

        // Step 3. At the end the final state of the Q-Matrix is printed to the console.
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

    private void printStats() {
        String algorithm;

        if (this.qLearningMode) {
            algorithm = "Q-Learning";
        } else {
            algorithm = "SARSA";
        }

        this.printQMatrix();
        System.out.println();
        System.out.println("------=== Final statistics: ===------");
        System.out.println();
        System.out.println("Algorithm used:\t\t\t" + algorithm);
        System.out.println("Alpha-Value:\t\t\t" + this.ALPHA);
        if (!this.qLearningMode) {
            System.out.println("Epsilon-Value:\t\t\t" + this.EPSILON);
        }
        System.out.println("Correct paths found:\t" + this.correctPathCounter + "/" + this.INITIAL_STATES.length);
    }

    /**
     * Function, returning the first valid state, to which the agent can move from the inputState
     * @param inputState
     * @return
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
     * action from state to randomstate mustn't be -1).
     *
     * @param state - input state/action for which the next action
     *               with maximum value is returned
     *               from the corresponding Q-Matrix row
     *
     * @return int array - an array with the length of 2
     *                  which represents the best state returned.
     *                  The first value from this array is 'bestState'
     *                  which is the number of the returned state,
     *                  the second value is this state's Q-Value
     */
    private int[] getMaxAction(int state) {

        int[] qValues = Q[state].clone();
        int bestState = this.getFirstValidTransition(state);
        int qMaxValue = qValues[bestState];

        for (int i = 0; i < qValues.length; i++) {
            if ((qValues[i] >= qMaxValue) && (R[state][i] > -1)) {
                qMaxValue = qValues[i];
                bestState = i;
            }
        }

        return new int[] {bestState, qMaxValue};
    }

    /**
     * Selects randomly a state from the Q-Matrix as the next action for an input state.
     * Only states from valid transitions are chosen (R-Matrix value for this
     * action from state to randomstate mustn't be -1).
     *
     * @param state - input state/room for which the next action
     *               is chosen randomly from the corresponding Q-Matrix row
     *
     * @return int array - an array with the length of 2
     *                    which represents the random state returned.
     *                    The first value from this array is 'state'
     *                    which is the number of the returned state,
     *                    the second value is this state's Q-Value
     */
    private int[] getRandomAction(int state) {
        int randomState;
        int[] qValues = Q[state].clone();
        // Sucht zufaellig einen moeglichen Wert != -1
        do {
            randomState = new Random().nextInt(qValues.length);
        } while (R[state][randomState] == -1);
        return new int[] {randomState, qValues[randomState]};
    }

    /**
     * This function bears the functionality of the ε-greedy Policy and returns either
     * the best next state or a random state for an action given. This is dependent from the
     * value of the 'epsilon' constant and a random number, which is generated in this function.
     *
     * @param action - input state/action for which the next action is selected with the ε-greedy Policy
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
        return random < this.EPSILON ? this.getRandomAction(action) : this.getMaxAction(action);
    }

    /**
     *
     * @param currentState
     * @return
     */
    private int chooseNextAction(int currentState) {

        int nextState;

        if (this.qLearningMode) {
            nextState = this.getRandomAction(currentState)[0];
        } else {
            nextState = this.epsilonGreedyPolicy(currentState)[0];
        }

        // keine Aktualisierung der Q-Matrix bei Endzustand
        if (currentState == 32) {
            return 32;
        }

        // Berechnet den neuen Belohnungswert Q-Wert mit Hilfe der R-Matrix
        int qValue;

        if (this.qLearningMode) {
            qValue = this.getMaxAction(currentState)[1];
        } else {
            qValue = this.epsilonGreedyPolicy(nextState)[1];
        }

        Q[currentState][nextState] = (int) (R[currentState][nextState] + (this.ALPHA * qValue));

        currentState = nextState;
        return currentState;
    }

    /**
     *
     * @param initialState
     */
    private void episode(final int initialState) {
        int currentState = initialState;
        // Die Schleife sucht nun so lange bis der Endzustand erreicht ist.
        do {
            currentState = chooseNextAction(currentState);
        } while (currentState != 32);
    }

    /**
     *
     */
    private void learning() {
        // starte das Training mit allen initial Werten
        // Anzahl Episoden, z.B. 100
        int ITERATIONS = 5000;
        for (int j = 0; j < ITERATIONS; j++) {
            if (this.qLearningMode) {
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
     * Calculates from the final state of the Q-Matrix all the paths
     * that the agent gone through and prints them to the console.
     */
    private void calculatePaths() {
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println("Shortest paths out of each room:");
        System.out.println();
        for (int currentState : INITIAL_STATES) {
            System.out.print("Path from [" + currentState + "]: ");
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
                if (highValue == 0) {
                    System.out.println("No correct path found.");
                    break;
                } else {
                    System.out.print("(" + currentState + ") --> ");
                    if (prevState == newState) {
                        loopCount++;
                        if (loopCount > 2) {
                            System.out.println("Agent stuck in a loop...");
                            break;
                        }
                    }
                    prevState = currentState;
                    currentState = newState;
                }
            }
            if (loopCount <= 2 && highValue != 0) {
                // Ausgabe des Zielzustandes
                System.out.print("<<32>>\n");
                this.correctPathCounter++;
            }
        }
    }

    public static void main(String[] args) {
        new SarsaAlgorithm(false);
    }

}
