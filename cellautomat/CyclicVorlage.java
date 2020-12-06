package cellautomat;

public class CyclicVorlage {
	// Zustand einer Zelle von 0...N
	static final int N = 10;
	static final int SIZE = 400;
	static final int MAXTIME = 5000;
	// Daten der Lebewesen in einer Matrix (1= lebende Zelle, 0 = nicht lebende Zelle)
	static int[][] S = new int[SIZE][SIZE];
	// Daten der n�chsten Generation
	static int[][] SNew = new int[SIZE][SIZE];
	
	// Berechne Vergleich von Zelle [i][j] mit allen Nachbarn
	static int neighbours(int i, int j) {
		// default: alten Wert behalten
		int newValue = S[i][j]; 
		
		// il ist i left: falls i==0 ist, dann von links au�en nach rechts au�en gehen
		int il = 0;
		if (i==0) 
			il = SIZE-1;
		// oder normalerweise: Zelle links von i
		else 
			il = i-1;
		// i rechts: normalerweise um 1 erh�hen, falls am rechten Rand, dann wieder links anfangen 
		int ir = (i+1) % SIZE;

		int jl = 0;
		if (j==0) 
			jl = SIZE-1;
		else 
			jl = j-1;
		int jr = (j+1) % SIZE;
		
		// Falls ein Nachbar genau 1 gr��er als Zelle ist, soll die Zelle dessen Wert �bernehmen
		// Deshalb der aktuelle Wert + 1
		int actValue = S[i][j] + 1;
		// falls h�chster Wert erreicht, wieder auf 0 zur�cksetzen
		if (actValue == N)
			actValue = 0;
		// nur die Nachbarn links, oben, rechts, unten betrachten:
		if (S[il][j ] == actValue) 
			newValue = S[il][j ];
		if (S[i ][jl] == actValue) 
			newValue = S[i ][jl]; 
		if (S[ir][j ] == actValue) 
			newValue = S[ir][j ]; 
		if (S[i ][jr] == actValue) 
			newValue = S[i ][jr]; 

		return newValue;
	}

	// Berechne Zellen der n�chsten Iteration
	static void nextGeneration() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int newValue = neighbours(i, j);
				SNew[i][j] = newValue;
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
			Thread.sleep(50);
		}
	}
}
