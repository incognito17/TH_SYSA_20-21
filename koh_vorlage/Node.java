package koh_vorlage;

public class Node {
	// Gewichte pro Knoten
	private MyVector weights;
	
	// x- und y-Koordinaten des Knotens auf der zweidimensionalen Karte
	private int xp, yp;
	
	/** Konstruktor: Knoten mit Gewichten anlegen. 
	 * Parameter dim = Dimension der Eingabedaten, z.B. drei für RGB-Werte
	 */
	public Node(int dim) {
		weights = new MyVector();
	
		if (Trainer.myDATA == Trainer.DATA.RGB_SEED_9_COLORS ||
			Trainer.myDATA == Trainer.DATA.RGB_SEED_ALL_COLORS ||
			Trainer.myDATA == Trainer.DATA.RGB_SEED_RED ||
			Trainer.myDATA == Trainer.DATA.IRIS || 
			Trainer.myDATA == Trainer.DATA.TWO_TO_ONE_DIM) 
		{

			// Vorbelegung mit Zufallszahlen zwischen 0 und 1
			for (int i=0; i<dim; i++) {
				weights.addElement(Math.random());
			}
		}
		else {
		// bei anderen Eingabedaten werden Zufallszahlen zwischen 0,45 und 0,55 verwendet
			for (int i=0; i<dim; i++) {
					double rand = Math.random();
				while (rand < 0.45 || rand > 0.55)
					rand =  Math.random();
				weights.addElement(rand);
			}			
				
		}
	}
	
	public void setX(int xpos) {
		xp = xpos;
	}
	
	public void setY(int ypos) {
		yp = ypos;
	}
	
	public int getX() {
		return xp;
	}
	
	public int getY() {
		return yp;
	}
	
	/** Distanz von einem Knoten zum anderen auf der Karte
	 * Metrik: quadratische Distanz
	 */
	public double distanceTo(Node n2) {
		int x, y;
		x = getX() - n2.getX();
		x *= x;
		y = getY() - n2.getY();
		y *= y;
		return Math.sqrt(x + y);
	}
	// Distanz auf eindimensionaler Karte
	public double distanceToOneDimension(Node n2) {
		int dist;
		// Node 1/1 ist in der zweiten Zeile, hat aber in eindimensionaler Karte die Nummer 41
		int pos1 = getX() + Lattice.WIDTH*getY();
		int pos2 = n2.getX() + Lattice.WIDTH*n2.getY();
		dist = pos1 - pos2;
		return Math.sqrt(dist*dist);
	}
	
	
	public MyVector getVector() {
		return weights;
	}
	
	public void adjustWeights(MyVector input, double learningRate, double distanceFalloff)	{
		double wt, vw;
		for (int w=0; w<weights.size(); w++) {
			wt = weights.elementAt(w);
			vw = input.elementAt(w);
			wt += distanceFalloff * learningRate * (vw - wt);
			weights.setElementAt(wt, w);
		}
	}
}