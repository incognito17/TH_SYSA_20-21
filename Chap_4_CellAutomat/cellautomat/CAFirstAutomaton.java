package cellautomat;

// Cellular automaton
public class CAFirstAutomaton {
	// Länge einer Zeile
	static int LENGTH;
	
	// Anzahl von Schritten, Zeitachse
	static int MAXTIME;
	
	// erster Index: Zeitachse, zweiter Index: eine komplette Zeile
	static int[][] S;

	// Regeln
	static int[] Regel18 = new int[] {0, 1, 0, 0, 1, 0, 0, 0};
	static int[] Regel30 = new int[] {0, 1, 1, 1, 1, 0, 0, 0};
	static int[] Regel57 = new int[] {1, 0, 0, 1, 1, 1, 0, 0};
	static int[] Regel62 = new int[] {0, 1, 1, 1, 1, 1, 0, 0};
	
	// Konstruktor: Felder für Zellen anlegen und mit Inhalt füllen
	CAFirstAutomaton () {
		LENGTH = 100;
		MAXTIME = 199;
		S = new int[MAXTIME][LENGTH];
		// Initialisierung aller Zellen S
		for (int i = 0; i < LENGTH; i++) 
			S[0][i] = 0;
		S[0][49] = 1;
		printPopulation(0);
	}
	
	// Berechne für den Zeitschritt t den Inhalt der Zellen aus der Vorgängergeneration t-1
	static void nextGeneration(int t) {
		for (int i = 1; i < LENGTH-1; i++) {
			// Berechne Index für Regel aus Vorgängergeneration
			int index = 4*S[t-1][i-1] + 2*S[t-1][i] + S[t-1][i+1];
			// Wende die Regel an
			// S[t][i]= Regel30[index];
			S[t][i]= Regel18[index];
		}
	}
	
	// Ausgabe auf Konsole
	static void printPopulation(int t) {
		for (int i = 0; i < LENGTH; i++) {
			if (S[t][i] == 1) System.out.print(" 1 "); 
			else System.out.print(" . ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		new CAFirstAutomaton();
		// Erzeuge mehrere Generationen
		for (int t = 1; t < MAXTIME; t++) {
			nextGeneration(t);
			printPopulation(t);
		}
		// Ausgabe des Ergebnisses als gefüllte/nicht gefüllte Rechtecke auf dem Bildschirm
		RenderCA r = new RenderCA(S, LENGTH, MAXTIME);
		r.repaint();

	}

}
