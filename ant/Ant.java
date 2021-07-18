package ant;
import java.util.*;
import java.awt.Point;
import java.text.DecimalFormat;
import java.io.*;
import java.net.URL;

public class Ant {
	public static int DATA = 1; // Wahl der Datenquelle
	public static ArrayList<Point> cities = new ArrayList<Point>();
	private static int MAX_IT = 20;
	// Setze später gleich Anzahl Städte:
	public static int NUM_ANTS = 0;
	// Bedeutung der Pheromone:
	private static double ALPHA = 1.0;
	// Bedeutung der Entfernung:
	private static double BETA = 5.0;
	//private static double BETA = 1.0;
	private static double RHO = 0.75;
	//Pheromongesamtmenge Q pro Durchlauf
	private static double Q = 500;
	
	// Pheromone von Stadt i zu Stadt j
	public static double[][] tau;
	
	//Liste aller Tabulisten, d.h. alle bereits besuchten Städte
	public static Tabu[] all_tabus;
	
	// Auswahl-Wahrscheinlichkeit von einer bestimmten Stadt zu anderen Städten
	private static double[] p;
	
	// Beste Rundreise, die gefunden wurde:
	public static Tabu bestRoute;


	// Reduziere die Pheromonintensität (Verdunstung)
	private static void reducePheromones () {
		for (int i = 0; i < NUM_ANTS; i++) {
			for (int j = 0; j < NUM_ANTS; j++) {
				// Verwitterung/Verdunstung vornehmen:
				tau[i][j] = RHO * tau[i][j];
			}
		}
	}
	
	// Auf der Route, die eine Ameise durchlaufen hat, werden Pheromone zurückgelassen
	private static void increasePheromones (Tabu route) {
		double actualDist = route.calcRouteDist(cities);
		//System.out.println("Distanz: " + actualDist + " für Route: " + route);
		double sum = 0;
		for (int n = 0; n < route.getSize() - 1; n++) {
			int i = route.get(n);
			int j = route.get(n+1);
			//System.out.println("i: " + i + " j: " + j + " Tau mit voriger Verdunstung: " + tau[i][j] );
			double actual1DivDist = 0;
			if (actualDist != 0) 
				actual1DivDist = 1/(actualDist+1);
			else actual1DivDist = 0;
			double increase = Q * actual1DivDist;
			sum = sum + increase;
			// Erhöhung vornehmen: Pheromonspur vermehren
			tau[i][j] = tau[i][j] + increase;	
			//evtl. Verbesserung durch symmetrische Erhöhung i/j = j/i?
			//tau[j][i] = tau[i][j];
			//System.out.println("i: " + i + " j: " + j + " Tau neu: " + tau[i][j] );
			//System.out.println();
		}
		//System.out.println("Summe der Erhöhung: "  + sum + "\n");

	}

	// Von der Route der Ameise k ausgehend wird gemäß Wahrscheinlichkeitsverteilung die nächste Stadt ausgewählt.
	// Beispiel: W-Dichte 0,00  0,51  0,38  0,11
	// Zufallszahl: 0,54, dann wird nextCity=2 ausgewählt
	// Zufallszahl: 0,50, dann wird nextCity=1 ausgewählt
	private static int selectNextCity (int k) {
		int nextCity = -1;
		Random r = new Random();
		double xRand = r.nextDouble();
		double sum = 0;
		for (int j = 0; j < NUM_ANTS; j++) {
			// Addiere Wahrscheinlichkeiten (Summe ist max 1), so dass irgendwann eine Stadt zum Zuge kommt
			sum = sum + p[j];
			if (xRand <= sum) {
				nextCity = j;
				return nextCity;
			}
		}
		System.out.println("Sollte nicht auftreten: k= " + k + " Zufallszahl: " + xRand + " Summe: " + sum);
		return nextCity;
		
	}
	
	@SuppressWarnings("unused")
	private static void printProbs () {
		System.out.print("Ausgabe p: ");
		double sum = 0;
		for (int j = 0; j < NUM_ANTS; j++) {
			DecimalFormat df = new DecimalFormat( "0.00" );
			double d = p[j];
			String s = df.format (d);
			System.out.print(s + "  ");
			sum = sum + p[j];
		}
		System.out.println(" Summe: " + sum);	
	}

	// Berechne neue Wahrscheinlichkeiten (= Probabilities) ausgehend von der letzten besuchten Stadt
	private static void calcProbs (Tabu route) {
		for (int j = 0; j < NUM_ANTS; j++) {
			// Falls die Ameise den Ort j schon besucht hat, soll die W-keit 0 sein, diesen nochmal zu besuchen
			if (route.contains(j)) {
				p[j] = 0;
				continue; 
			}			
			p[j] = 0;
			// hole letzte besuchte Stadt
			int i = route.get(route.getSize()-1);
			//System.out.println("letzte besuchte Stadt: " +i);
			double actualTau = Math.pow(tau[i][j], ALPHA);
			double actualDist = Tabu.euclideanDist(cities.get(i), cities.get(j));
			double actual1DivDist = 0;
			if (actualDist != 0) 
				actual1DivDist = Math.pow(1/actualDist, BETA);
			else actual1DivDist = 0;
			
			double sum = 0;
			for (int k = 0; k < NUM_ANTS; k++) {
				double myTau = Math.pow(tau[i][k], ALPHA);
				double myDist = Tabu.euclideanDist(cities.get(i), cities.get(k));
				double my1DivDist = 0;
				if (myDist != 0) 
					my1DivDist = Math.pow(1/myDist, BETA);
				else my1DivDist = 0;
				// Nur in W-Vektor aufnehmen, falls nicht schon früher besucht
				if (!route.contains(k)) 
					sum = sum + myTau * my1DivDist;
			}
			if (sum != 0)
				p[j] = actualTau * actual1DivDist / sum;
		}	
	}
	
	
	private static void init () {
		if (DATA == 0) {
			// 4 feste Positionen als kleines Übungsbeispiel
			cities.add(new Point(0,0));
			cities.add(new Point(1,0));
			cities.add(new Point(1,2));
			cities.add(new Point(0,3));			
		}
		if (DATA == 1) {	
		try {
			URL url = Ant.class.getResource("data.txt");
			BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
			while (br.ready() ) {
				String line = br.readLine();
				String[] parts = line.split(" ");
				int num1 = (int)Double.parseDouble(parts[1]);
				int num2 = (int)Double.parseDouble(parts[2]);
				cities.add(new Point(num1,num2));
			}
			br.close();
		} catch (Exception e) {e.printStackTrace();	}
		}

		// es gibt genausoviele Ameisen wie Städte
		NUM_ANTS = cities.size();
		
		p = new double[NUM_ANTS];
		
		// Initialisiere Pheromonintensitäten (= tau) zwischen Ort i und Ort j mit kleinem Wert
		tau = new double[NUM_ANTS][NUM_ANTS];
		for (int i = 0; i < NUM_ANTS; i++) {
			for (int j = 0; j < NUM_ANTS; j++) {
				if (i != j) tau[i][j] = 0.1;
				else tau[i][j] = 0;
			}	
		}
		if (DATA == 0) {
		// Initialisierung der Pheromone gemäß Beispiel aus Vorlesung
		tau[0][1] = 0.3;
		tau[0][2] = 0.5;
		tau[0][3] = 0.2;
		tau[1][0] = 0.3;
		tau[2][0] = 0.5;
		tau[3][0] = 0.2;

		tau[1][2] = 0.5;
		tau[1][3] = 0.1;
		tau[2][1] = 0.5;
		tau[3][1] = 0.1;

		tau[2][3] = 0.7;
		tau[3][2] = 0.7;
		}

		all_tabus = new Tabu[NUM_ANTS];
	}
	
	// gebe beste Route aus
	public static Tabu findBest () {
		int numberBest = 0;
		double bestDistance = Double.MAX_VALUE;
		double actualValue = Double.MAX_VALUE;
		for (int k = 0; k < NUM_ANTS; k++) {
			actualValue = all_tabus[k].calcRouteDist(cities);
			if (actualValue < bestDistance) {
				bestDistance = actualValue;
				numberBest = k;
			}
		}
		//System.out.println("Beste Routendistanz: " + bestDistance + " Route: " + all_tabus[numberBest]);
		// Opt: 7542 units, siehe TSPLIB
		return all_tabus[numberBest];
	}
	
	
	public static void main(String[] args) {
		init();
		System.out.println("Anzahl Städte = Anzahl Ameisen: " + NUM_ANTS);
		int numIterations = 0;
		while (numIterations < MAX_IT) {
		
			for (int k = 0; k < NUM_ANTS; k++) {
				// leere Tabuliste
				all_tabus[k] = new Tabu();
				// Jede Ameise wird auf eine Stadt  0 bis NUM_ANTS -1 gesetzt
				int firstCity = k;
				// Stadt ist vergeben, deshalb auf Tabuliste aufnehmen:
				all_tabus[k].setNew(firstCity);
				//System.out.println("k= " + k + " Tabuliste-Inhalt: " + all_tabus[k]);
				// Berechne Wahrscheinlichkeiten für nächste Stadt
				calcProbs(all_tabus[k]);
				//printProbs();
				
				// Weitere Ameisen werden auf eine noch nicht verwendete Stadt gemäß W-keitsverteilung gesetzt
				boolean allUsed = false;
				while (!allUsed) {
					//Hole neue Stadt gemäß W-Verteilung
					int newCity = selectNextCity(k);
	
					if (!all_tabus[k].contains(newCity)) {
						// Stadt passt, deshalb auf Tabuliste aufnehmen:
						all_tabus[k].setNew(newCity);
						calcProbs(all_tabus[k]);
						//printProbs();
					}
					if (all_tabus[k].getSize() == NUM_ANTS) {
						// alle Städte sind abgearbeitet
						allUsed = true;
						// Route vom letzten zum ersten schließen
						firstCity = all_tabus[k].get(0);
						all_tabus[k].setNew(firstCity);				
					}
				}
				//System.out.println(" Tabuliste-Inhalt: " + all_tabus[k]);
			}
		
		reducePheromones();
		for (int k = 0; k < NUM_ANTS; k++)	
			increasePheromones(all_tabus[k]);
		
		Tabu currentRoute = findBest();
		double currentBest = currentRoute.calcRouteDist(cities);
		if (numIterations == 0) bestRoute = currentRoute;
		double absoluteBest = bestRoute.calcRouteDist(cities);

		if (currentBest < absoluteBest) {
			bestRoute = currentRoute;
			System.out.println("Durchlauf: " + numIterations + " Beste Distanz: " + currentBest + " Beste Route: " + currentRoute);
		}
		numIterations++;
		if (numIterations == MAX_IT) {
			new Rendering();
		}
		}
	}
}
