package ant;

import java.awt.Point;
import java.util.*;

public class Tabu {
	//Tabu-Liste von Städten, die durch die k-te Ameise noch nicht besucht wurden
	// die Städte werden mit ihrem Index gekennzeichnet, d.h. 2 ist die dritte Stadt der Liste
	ArrayList <Integer> tabu;

	Tabu () {
		tabu = new ArrayList <Integer>();
	}
	
	public void setNew (Integer newEntry) {
		tabu.add(newEntry);
	}

	public int getSize () {
		return tabu.size();
	}
	
	public boolean contains (Integer which) {
		return tabu.contains(which);
	}
	
	public Integer get (int index) {
		return tabu.get(index);
	}
	
	public String toString () {
		//return tabu.toString();
		String s = "";
		Iterator<Integer> myIterator = tabu.iterator();
		while (myIterator.hasNext()) {
			Integer anElement = myIterator.next();
			s = s + " " + anElement;
		}
		return s;
	}
	
	public static double euclideanDist(Point p, Point q) {
		double xDist = p.getX() - q.getX();
		double yDist = p.getY() - q.getY();
		double sum = xDist*xDist + yDist*yDist;
		return Math.sqrt(sum);
	}
	
	// Distanz einer Route, d.h. die Summe aller Entfernungen
	public double calcRouteDist (ArrayList<Point> cities) {
		double result = 0;
		int index1 = 0;
		int index2 = 0;
		Iterator<Integer> myIterator = tabu.iterator();
		// ersten und zweiten Eintrag holen
		index1 = myIterator.next();
		// falls kein zweiter Eintrag existiert, gibt es noch keine Entfernungen, d.h. Distanz = 0
		if (myIterator.hasNext()) 
			index2 = myIterator.next();
		else 
			return 0;
		while (myIterator.hasNext()) {
			Point p1 = cities.get(index1);
			Point p2 = cities.get(index2);
			result = result + euclideanDist (p1, p2);
			index1 = index2;
			index2 = myIterator.next();
		}
		//letzten Punkt zum Schließen des Kreises berücksichtigen:
		Point p1 = cities.get(index1);
		Point p2 = cities.get(index2);
		result = result + euclideanDist (p1, p2);		
		return result;	
	}

}
