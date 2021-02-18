package sarsa;

// Quelle: http://mnemstudio.org/ai/path/q_learning_java_ex1.txt
//teilweise korrigiert F. Mehler, marcel.meinerz , 2017
import java.util.Arrays;
import java.util.Random;

public class SarsaAlgorithm {

    private final int Q_SIZE = 8;
    private final int[] INITIAL_STATES = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };

    // The reward matrix R which resembles the current labyrinth with valid paths and reward points
    private final int[][] R = new int[][] {

            // 0   1   2   3   4   5   6   7
            { -1,  0, -1, -1, -1, -1, -1, 100 },    // 0
            {  0, -1,  0, -1, -1, -1, -1, -1 },     // 1
            { -1,  0, -1, -1, -1,  0, -1, -1 },     // 2
            { -1, -1, -1, -1, -1, -1,  0, -1 },     // 3
            { -1, -1, -1, -1, -1, -1,  0, -1 },     // 4
            { -1, -1,  0, -1, -1, -1,  0, -1 },     // 5
            { -1, -1, -1,  0,  0,  0, -1, 100 },    // 6
            {  0, -1, -1, -1, -1, -1,  0, 100 }     // 7

    };

    // Q-Matrix which contains the agent's knowledge
    private final int[][] Q = new int[Q_SIZE][Q_SIZE];

    public SarsaAlgorithm() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("----------###----------");
        System.out.println("---------SARSA---------");
        System.out.println("-----------#-----------");
        System.out.println();

        learning();
        calculatePaths();
        printQMatrix();
    }

    /**
     * Prints the current state of the Q-Matrix
     */
    private void printQMatrix() {
        // Ausgabe der Q Matrix
        System.out.println("Q Matrix values:");
        for (int i = 0; i < Q_SIZE; i++) {
            for (int j = 0; j < Q_SIZE; j++) {
                System.out.print(Q[i][j] + ",\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    // suche in einer Zeile der Q-Matrix den hoechsten Wert
    /**
     *
     * @param action
     * @return
     */
    private int[] max(int action) {
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
     *
     * @param action
     * @return
     */
    private int[] getRandomAction(int action) {
        int[] ar = Q[action].clone();
        int randomState = new Random().nextInt(ar.length);
        return new int[] {randomState, ar[randomState]};
    }

    /**
     *
     * @param action
     * @return
     */
    private int[] epsilonGreedyPolicy(int action) {
        double EPSILON = 0.3;
        double random = Math.random();
        if (random < EPSILON) {
            return this.getRandomAction(action);
        } else {
            return this.max(action);
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
        if (currentState == 7) {
            return 7;
        }

        // Berechnet den neuen Belohnungswert Q-Wert mit Hilfe der R-Matrix
        double ALPHA = 0.7;
        int qValue = this.epsilonGreedyPolicy(nextState)[1];
        Q[currentState][nextState] = (int) (R[currentState][nextState] + (ALPHA * qValue));

        currentState = nextState;
        return currentState;
    }

    /**
     *
     * @param initialState
     */
    private void episode(final int initialState) {
        int currentState = initialState;
        System.out.println("initialState: " + initialState + " ");
        // Die Schleife sucht nun so lange bis der Endzustand erreicht ist.
        do {
            currentState = chooseActionWithEpsilonGreedy(currentState);
            System.out.println("currSt: " + currentState + " ");
        } while (currentState != 7);
        System.out.println();
    }

    /**
     *
     */
    private void learning() {
        // starte das Training mit allen initial Werten
        // Anzahl Episoden, z.B. 100
        int ITERATIONS = 5000;
        for (int j = 0; j < ITERATIONS; j++) {
            System.out.println("---> ITERATION " + j);
            for (int i = 0; i < Q_SIZE; i++) {
                episode(INITIAL_STATES[i]);
            }
            // QMatrixAusgeben();
        }
    }

    private void calculatePaths() {
        // Ausgabe der kuerzesten Routen
        System.out.println("Kuerzeste Pfade von den Ausgangszustaenden:");
        int currentState;
        for (int i = 0; i < Q_SIZE; i++) {
            currentState = INITIAL_STATES[i];
            int newState;
            int highValue = 0;
            while (currentState < 7) {
                highValue = max(currentState)[1];
                for (newState = 0; newState < Q_SIZE; newState++) {
                    if (highValue == Q[currentState][newState]) {
                        break;
                    }
                }
                if (highValue == 0) {
                    System.out.println("Path not correctly found");
                    break;
                } else {
                    System.out.print(currentState + ", ");
                    currentState = newState;
                }
            }
            if (highValue != 0) {
                // Ausgabe von 7 als Zielzustand
                System.out.print("7\n");
            }
        }
    }

    public static void main(String[] args) {
        new SarsaAlgorithm();
    }

}
