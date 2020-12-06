package cellautomat;

import java.util.Stack;

// Cellular automaton
public class Uebung_4_1 {

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

    // Array, welches dabei hilft die Periode zu berechnen.
    static Stack<Integer> periodArr = new Stack<>();

    // Konstruktor: Felder für Zellen anlegen und mit Inhalt füllen
    Uebung_4_1 () {
        LENGTH = 10;
        MAXTIME = 99;
        S = new int[MAXTIME][LENGTH];
        // Initialisierung aller Zellen S
        for (int i = 0; i < LENGTH; i++) {
            S[0][i] = 0;
        }
        S[0][1] = 1;
        S[0][3] = 1;
        S[0][5] = 1;
        S[0][6] = 1;
        printPopulation(0);
    }

    // Berechne für den Zeitschritt t den Inhalt der Zellen aus der Vorgängergeneration t-1
    static void nextGeneration(int t) {
        for (int i = 1; i < LENGTH-1; i++) {
            // Berechne Index für Regel aus Vorgängergeneration
            int index = 4*S[t-1][i-1] + 2*S[t-1][i] + S[t-1][i+1];
            // Wende die Regel an
            // S[t][i]= Regel30[index];
            S[t][i]= Regel62[index];
        }
    }

    // Ausgabe auf Konsole
    static void printPopulation(int t) {
        int sum = 0;
        for (int i = 0; i < LENGTH; i++) {
            if (S[t][i] == 1) {
                sum += Math.pow(2, i);
                System.out.print(" 1 ");
            } else {
                System.out.print(" . ");
            }
        }
        periodArr.push(sum);
        System.out.println();
    }

    public static void main(String[] args) {
        new Uebung_4_1();
        // Erzeuge mehrere Generationen
        for (int t = 1; t < MAXTIME; t++) {
            nextGeneration(t);
            printPopulation(t);
        }
        // Ausgabe des Ergebnisses als gefüllte/nicht gefüllte Rechtecke auf dem Bildschirm
        RenderCA r = new RenderCA(S, LENGTH, MAXTIME);
        r.repaint();

        System.out.println();
        System.out.println("Binaercodierung-Folge:");
        System.out.println(periodArr);
    }
}
