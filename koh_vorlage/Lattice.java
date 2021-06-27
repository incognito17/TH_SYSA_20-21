package koh_vorlage;

public class Lattice {
	// Breite, Höhe der Karte
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	//Dimension der Gewichtsvektoren
	public static int DIM = 4;
	
	// Gitter, das aus einem zweidimensionalen Feld von Knoten besteht
	private Node[][] matrix;
	

	public Lattice() {
		matrix = new Node[WIDTH][HEIGHT];
		for (int x=0; x<WIDTH; x++) {
			for (int y=0; y<HEIGHT; y++) {
				// Zu jedem Knoten gehört ein Gewichtsvektor, bei RGB-Daten der Dimension 3
				// Vorbelegung des Vektors beispielsweise mit Zufallszahlen zwischen 0 und 1
				matrix[x][y] = new Node(DIM);
				
				// x- und y-Koordinate des Knotens auf der Karte setzen:
				matrix[x][y].setX(x);
				matrix[x][y].setY(y);
			}
		}
	}
	
	// Liefert den Knoten anhand (x,y)-Koordinate auf der Karte
	public Node getNode(int x, int y) {
		return matrix[x][y];
	}
		
	/** Suche den Knoten/Neuron, das am besten zum inputVector passt
	 *  (best matching unit) 
	 */
	public Node getBMU(MyVector inputVector) {
		// Start: Annahme, das Element 0,0 ist die best matching unit
		Node bmu = matrix[0][0];
		double bestDist = inputVector.euclideanDist(bmu.getVector());
		double curDist;
		
		// Suche in der ganzen Matrix nach besser passenden Knoten 
		// gemäß Abstand vom inputVector zum Matrix-Vektor (Euklidische Distanz)
		for (int x=0; x<WIDTH; x++) {
			for (int y=0; y<HEIGHT; y++) {
				curDist = inputVector.euclideanDist(matrix[x][y].getVector());
				if (curDist < bestDist) {
					// Kleinere Distanz, dann neue BMU gefunden
					bmu = matrix[x][y];
					bestDist = curDist;
				}
			}
		}
		
		return bmu;
	}
	
}
