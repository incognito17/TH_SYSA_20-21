package cellautomat;

public class GameOfLife {
	
	static final int SIZE = 10;
	static final int MAXTIME = 50;
	// Daten der Lebewesen in einer Matrix (1= lebende Zelle, 0 = nicht lebende Zelle)
	static int[][] S = new int[SIZE][SIZE];
	// Daten der n�chsten Generation
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


	// Berechne Zellen der n�chsten Iteration
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
		// �bernehme neue Daten in die Matrix
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
		
		// Blinker:
		S[8][1] = 1; S[8][2] = 1; S[8][3] = 1;
		
		// Glider:
						S[1][3] = 1;
					 					S[2][4] = 1;
		S[3][2] = 1; 	S[3][3] = 1; 	S[3][4] = 1;
		
		
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
