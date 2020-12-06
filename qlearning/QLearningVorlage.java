package qlearning;

// Quelle: http://mnemstudio.org/ai/path/q_learning_java_ex1.txt
//teilweise korrigiert F. Mehler, marcel.meinerz , 2017
import java.util.Arrays;
import java.util.Random;

public class QLearningVorlage {
	private final int Q_SIZE = 6;
	private final int ITERATIONS = 100; // Anzahl Episoden, z.B. 100
	private final int INITIAL_STATES[] = new int[] { 0, 1, 2, 3, 4, 5 };
	// Beispiel Belohnungsmatrix R
	private final int[][] R = new int[][] {

			// 0  1   2  3   4   5
			{ -1, 0, -1, 0, -1, -1 },   // 0
			{ 0, -1, 0, -1, 0, -1 },    // 1
			{ -1, 0, -1, -1, -1, 100 }, // 2
			{ 0, -1, -1, -1, 0, -1 },    // 3
			{ -1, 0, -1, 0, -1, 100 },  // 4
			{ -1, -1, -1, -1, -1, 100 }   // 5

	};

	// Wissen des Agenten in Q-Matrix:
	private int Q[][] = new int[Q_SIZE][Q_SIZE];
	private Random zufall = new Random(123);

	public QLearningVorlage() {
		learning();
		ergebnisBerechnen();
		QMatrixAusgeben();
	}

	private void QMatrixAusgeben() {
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
	private int max(int action) {
		int[] ar = Q[action].clone();
		Arrays.sort(ar);
		return ar[Q_SIZE - 1];
	}

	private int chooseAnAction(int currentState) {
		int nextState;
		do {
			// Sucht zufaellig einen moeglichen Wert != -1
			nextState = zufall.nextInt(Q_SIZE);
		} while (R[currentState][nextState] == -1);

		// keine Aktualisierung der Q-Matrix bei Endzustand
		if (currentState == 5) {
			return 5;
		}

		// Berechnet den neuen Belohnungswert Q-Wert mit Hilfe der R-Matrix
		double ALPHA = 0.5;
		Q[currentState][nextState] = (int) (R[currentState][nextState] + (ALPHA * max(nextState)));

		currentState = nextState;
		return currentState;
	}

	private void episode(final int initialState) {
		int currentState = initialState;
		System.out.print("initialState: " + initialState + " ");
		// Die Schleife sucht nun so lange bis der Endzustand erreicht ist.
		do {
			currentState = chooseAnAction(currentState);
			System.out.print("currSt: " + currentState + " ");
		} while (currentState != 5);
		System.out.println();
	}

	private void learning() {
		// starte das Training mit allen initial Werten
		for (int j = 0; j < ITERATIONS; j++) {
			for (int i = 0; i < Q_SIZE; i++) {
				episode(INITIAL_STATES[i]);
			}
			// QMatrixAusgeben();
		}
	}

	private void ergebnisBerechnen() {
		// Ausgabe der kuerzesten Routen
		System.out.println("Kuerzeste Pfade von den Ausgangszustaenden:");
		int currentState;
		for (int i = 0; i < Q_SIZE; i++) {
			currentState = INITIAL_STATES[i];
			int newState;
			int highValue;
			while (currentState < 5) {
				highValue = max(currentState);
				for (newState = 0; newState < Q_SIZE; newState++) {
					if (highValue == Q[currentState][newState]) {
						break;
					}
				}
				System.out.print(currentState + ", ");
				currentState = newState;
			}
			// Ausgabe von 5 als Zielzustand
			System.out.print("5\n");
		}
	}

	public static void main(String[] args) {
		new QLearningVorlage();
	}

}
