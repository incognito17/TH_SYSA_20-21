package cellautomat;

public class Uebung_4_3 {
    // Zustand einer Zelle von 0...N
    static final int N = 10;
    static final int SIZE = 400;
    static final int MAXTIME = 10000;
    // Daten der Lebewesen in einer Matrix (1= lebende Zelle, 0 = nicht lebende Zelle)
    static int[][] S = new int[SIZE][SIZE];
    // Daten der nächsten Generation
    static int[][] SNew = new int[SIZE][SIZE];

    // Berechne Vergleich von Zelle [i][j] mit allen Nachbarn
    static int neighbours(int i, int j) {
        // default: alten Wert behalten
        int newValue = S[i][j];

        // il ist i left: falls i==0 ist, dann von links außen nach rechts außen gehen
        int il = 0;
        if (i==0)
            il = SIZE-1;
            // oder normalerweise: Zelle links von i
        else
            il = i-1;
        // i rechts: normalerweise um 1 erhöhen, falls am rechten Rand, dann wieder links anfangen
        int ir = (i+1) % SIZE;

        int jl = 0;
        if (j==0)
            jl = SIZE-1;
        else
            jl = j-1;
        int jr = (j+1) % SIZE;

        /*
        // diagonal, oben, links
        int ml = 0;
        if (il == 0) {
            ml = SIZE-1;
        } else {
            ml = il-1;
        }

        // diagonal, oben, rechts
        int mr = (il+1) % SIZE;


        // diagonal, unten, links
        int nl = 0;
        if (ir == 0) {
            nl = SIZE-1;
        } else {
            nl = ir-1;
        }

        // diagonal, unten, rechts
        int nr = (ir+1) % SIZE;
        */

        int count = 0;

        // Falls ein Nachbar genau 1 größer als Zelle ist, soll die Zelle dessen Wert übernehmen
        // Deshalb der aktuelle Wert + 1
        int actValue = S[i][j] + 1;
        // falls höchster Wert erreicht, wieder auf 0 zurücksetzen
        if (actValue == N)
            actValue = 0;
        // nur die Nachbarn links, oben, rechts, unten betrachten:
        if (S[il][j ] == actValue)
            count++;
        if (S[i ][jl] == actValue)
            count++;
        if (S[ir][j ] == actValue)
            count++;
        if (S[i ][jr] == actValue)
            count++;

        // diagonale Nachbarn betrachten
        if (S[il][jl] == actValue)
            count++;
        if (S[il][jr] == actValue)
            count++;
        if (S[ir][jl] == actValue)
            count++;
        if (S[ir][jr] == actValue)
            count++;

        // neuen Wert setzen
        if (count >= 2) {
            newValue = actValue;
        }

        return newValue;
    }

    // Berechne Zellen der nächsten Iteration
    static void nextGeneration() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int newValue = neighbours(i, j);
                SNew[i][j] = newValue;
            }
        }
        // Übernehme neue Daten in die Matrix
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                S[i][j] = SNew[i][j];
    }

    // Ausgabe Konsole
    static void printPopulation() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(S[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                //Zufallszahl zwischen 0 und N-1
                S[i][j] = (int)(Math.random()*(N));
                SNew[i][j] = 0;
            }

        printPopulation();
        RenderCyclic r = new RenderCyclic(S);
        r.repaint();
        Thread.sleep(1000);
        for (int t = 1; t < MAXTIME; t++) {
            nextGeneration();
            //printPopulation();
            r.repaint();
            System.out.println(t);
            Thread.sleep(10);
        }
    }
}
