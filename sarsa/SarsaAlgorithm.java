package sarsa;

/*
The code below is based on the code for the Q-Learning algorithm from the lecture Systemanalysis ("SYSA")
by Prof. Dr. F. Mehler in TH Bingen. The primary source though for this code is:
http://mnemstudio.org/ai/path/q_learning_java_ex1.txt
 */

import org.jetbrains.annotations.Contract;

import java.util.Arrays;
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
            { -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,1000},  // 2
            { -1, -1, -1, -1, -1, -1, 200,-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 3
            {  0, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 4
            { -1,  0, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 5
            { -1, -1, 500, 0, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 6
            { -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 7
            { -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 8
            { -1, -1, -1, -1, -1,  0, 200,-1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 9
            { -1, -1, -1, -1, -1, -1, 200,-1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // 10
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
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 500, 0, -1, -1, -1 },  // 25
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1 },  // 26
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1,  0,  0, -1 },  // 27
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1,1000},  // 28
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 200, 0, -1, -1, -1, -1, -1, -1 },  // 29
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1 },  // 30
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1 },  // 31
            { -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1,1000}  // 32

    };

    // Q-Matrix which contains the agent's knowledge. It has the same size as the reward matrix R.
    private final int[][] Q = new int[Q_SIZE][Q_SIZE];

    /**
     * The actual SARSA algorithm. The process in this class has three major steps (see below).
     */
    public SarsaAlgorithm() {

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
        printQMatrix();
    }

    /**
     * Prints the current state of the Q-Matrix to the console
     */
    private void printQMatrix() {
        System.out.println();
        System.out.println();
        System.out.println("-== Q Matrix values: ==-");
        System.out.println();
        System.out.println("   [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10][11][12][13][14][15][16][17][18][19][20][21][22][23][24][25][26][27][28][29][30][31][32]");
        for (int i = 0; i < Q_SIZE; i++) {
            System.out.print("[" + i + "] ");
            for (int j = 0; j < Q_SIZE; j++) {
                if (Q[i][j] != 0) {
                    System.out.print(Q[i][j] + " ");
                } else {
                    System.out.print(Q[i][j] + ",\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * Returns the best state with it's number and it's maximum value
     * from the Q-Matrix as the next action for an input state.
     *
     * @param action - input state/action for which the next action
     *               with maximum value is returned
     *               from the corresponding Q-Matrix row
     *
     * @return int array - an array with the length of 2
     *                  which represents the best state returned.
     *                  The first value from this array is 'state'
     *                  which is the number of the returned state,
     *                  the second value is this state's Q-Value
     */
    private int[] getMaxAction(int action) {
        int[] ar = Q[action].clone();
        Arrays.sort(ar);
        int qMaxValue = ar[Q_SIZE - 1];
        int state = -1;

        for (int i = 0; i < Q_SIZE; i++) {
            if (Q[action][i] == qMaxValue) {
                state = i;
                break;
            }
        }
        return new int[] {state, qMaxValue};
    }

    /**
     * Selects randomly a state from the Q-Matrix as the next action for an input action.
     *
     * @param action - input state/action for which the next action
     *               is chosen randomly from the corresponding Q-Matrix row
     *
     * @return int array - an array with the length of 2
     *                    which represents the random state returned.
     *                    The first value from this array is 'state'
     *                    which is the number of the returned state,
     *                    the second value is this state's Q-Value
     */
    private int[] getRandomAction(int action) {
        int[] ar = Q[action].clone();
        int randomState = new Random().nextInt(ar.length);
        return new int[] {randomState, ar[randomState]};
    }

    /**
     * This function bears the functionality of the ε-greedy Policy and returns either
     * the best next state or a random state for an action given. This is depending from
     * the value of the 'epsilon' variable, which is declared in this function.
     *
     * @param action
     * @return
     */
    private int[] epsilonGreedyPolicy(int action) {
        double epsilon = 0.15;
        double random = Math.random();
        if (random < epsilon) {
            return this.getRandomAction(action);
        } else {
            return this.getMaxAction(action);
        }
    }

    /**
     *
     * @param currentState
     * @return
     */
    private int chooseActionWithEpsilonGreedy(int currentState) {
        int nextState;

        do {
            // Sucht zufaellig einen moeglichen Wert != -1
            nextState = this.epsilonGreedyPolicy(currentState)[0];
        } while (R[currentState][nextState] == -1);
        // keine Aktualisierung der Q-Matrix bei Endzustand
        if (currentState == 32) {
            return 32;
        }

        // Berechnet den neuen Belohnungswert Q-Wert mit Hilfe der R-Matrix
        double alpha = 0.7;
        int qValue = this.epsilonGreedyPolicy(nextState)[1];
        Q[currentState][nextState] = (int) (R[currentState][nextState] + (alpha * qValue));

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
            currentState = chooseActionWithEpsilonGreedy(currentState);
        } while (currentState != 32);
    }

    /**
     *
     */
    private void learning() {
        // starte das Training mit allen initial Werten
        // Anzahl Episoden, z.B. 100
        int ITERATIONS = 100;
        for (int j = 0; j < ITERATIONS; j++) {
            System.out.println("ITERATION " + j + "/" + ITERATIONS);
            for (int initial_state : INITIAL_STATES) {
                episode(initial_state);
            }
        }
    }

    private void calculatePaths() {
        // Ausgabe der kuerzesten Routen
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println("Kuerzeste Pfade von den Ausgangszustaenden:");
        System.out.println();
        int currentState;
        for (int initial_state : INITIAL_STATES) {
            currentState = initial_state;
            System.out.print("Path from [" + currentState + "]: ");
            int newState;
            int highValue = 0;
            while (currentState < 32) {
                highValue = getMaxAction(currentState)[1];
                for (newState = 0; newState < Q_SIZE; newState++) {
                    if (highValue == Q[currentState][newState]) {
                        break;
                    }
                }
                if (highValue == 0) {
                    System.out.println("Path not correctly found");
                    break;
                } else {
                    System.out.print("(" + currentState + ") --> ");
                    currentState = newState;
                }
            }
            if (highValue != 0) {
                // Ausgabe des Zielzustandes
                System.out.print("<32>\n");
            }
        }
    }

    public static void main(String[] args) {
        new SarsaAlgorithm();
    }

}
