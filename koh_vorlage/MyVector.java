package koh_vorlage;

import java.util.Vector;


public class MyVector extends Vector<Double> {
	
	static final long serialVersionUID =1;

		
	/** Berechne Distanz vom Vektor zum zweiten Vektor v2
	 * Falls die Größe der zwei Vektoren verschieden ist, dann Rückgabe 0
	 * Sonst: Euklidische Distanz.
	 */
	public double euclideanDist(MyVector v2) {
		if (v2.size() != this.size()) {
			System.out.println("Error in euclideanDist");
			return 0;
		}
		
		double summation = 0, temp;
		for (int x=0; x < this.size(); x++) {
			temp = get(x) - v2.get(x);
			temp *= temp;
			summation += temp;
		}
		return Math.sqrt(summation);
	}
	
}
