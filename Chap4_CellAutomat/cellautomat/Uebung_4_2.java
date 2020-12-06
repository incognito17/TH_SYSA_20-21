package cellautomat;

public class Uebung_4_2 {

    static final int SIZE = 40;
    static final int MAXTIME = 100;
    // Daten der Lebewesen in einer Matrix (1= lebende Zelle, 0 = nicht lebende Zelle)
    static int[][] S = new int[SIZE][SIZE];
    // Daten der nächsten Generation
    static int[][] SNew = new int[SIZE][SIZE];

    // Liefere die Anzahl der lebenden Nachbarzellen von cell[i][j]
    static int neighbours(int i, int j) {
        int n = 0;
        // Betrachte alle 8 Nachbarn (sofern vorhanden)
        if (i > 0 && S[i-1][j] == 1) n++;
        if (i > 0 && j > 0 && S[i-1][j-1] == 1) n++;
        if (j > 0 && S[i][j-1] == 1) n++;
        if (i < SIZE-1 && j > 0 && S[i+1][j-1] == 1) n++;
        if (i < SIZE-1 && S[i+1][j] == 1) n++;
        if (i < SIZE-1 && j < SIZE-1 && S[i+1][j+1] == 1) n++;
        if (j < SIZE-1 && S[i][j+1] == 1) n++;
        if (i > 0 && j < SIZE-1 && S[i-1][j+1] == 1) n++;
        return n;
    }


    // Berechne Zellen der nächsten Iteration
    static void nextGeneration() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int n = neighbours(i, j);
                if (n==3)
                    SNew[i][j] = 1;
                if (n<2)
                    SNew[i][j] = 0;
                if (n==2 && S[i][j]==1)
                    SNew[i][j] = 1;
                if (n>3)
                    SNew[i][j] = 0;
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
                if (S[i][j] == 1) System.out.print(" 1 ");
                else System.out.print(" . ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                S[i][j] = 0;
                SNew[i][j] = 0;
            }

        // n = 3
        // S[9][7] = 1; S[9][8] = 1; S[9][9] = 1;

        // n = 4
        // S[9][7] = 1; S[9][8] = 1; S[9][9] = 1; S[9][10] = 1;

        // n = 5
        // S[9][7] = 1; S[9][8] = 1; S[9][9] = 1; S[9][10] = 1; S[9][11] = 1;

        // n = 6
        // S[9][7] = 1; S[9][8] = 1; S[9][9] = 1; S[9][10] = 1; S[9][11] = 1; S[9][12] = 1;

        // n = 7
        // S[9][7] = 1; S[9][8] = 1; S[9][9] = 1; S[9][10] = 1; S[9][11] = 1; S[9][12] = 1; S[9][13] = 1;

        // n = 8
        // S[9][7] = 1; S[9][8] = 1; S[9][9] = 1; S[9][10] = 1; S[9][11] = 1; S[9][12] = 1; S[9][13] = 1; S[9][14] = 1;

        // n = 9
        // S[9][7] = 1; S[9][8] = 1; S[9][9] = 1; S[9][10] = 1; S[9][11] = 1; S[9][12] = 1; S[9][13] = 1; S[9][14] = 1; S[9][15] = 1;

        // n = 10
        S[9][7] = 1; S[9][8] = 1; S[9][9] = 1; S[9][10] = 1; S[9][11] = 1; S[9][12] = 1; S[9][13] = 1; S[9][14] = 1; S[9][15] = 1; S[9][16] = 1;


        printPopulation();
        RenderLife r = new RenderLife(S);
        r.repaint();
        Thread.sleep(1000);
        for (int t = 1; t < MAXTIME; t++) {
            nextGeneration();
            //printPopulation();
            r.repaint();
            System.out.println(t);
            Thread.sleep(200);
        }

    }

}
