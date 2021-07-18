package boid_vorlage;
import java.awt.*;
import java.util.Vector;

public class BoidVollständig {
	// Aktuelle Position des Boids
	private Vector<Double> pos;
	private Color color;
	// Geschwindigkeit in x- und y-Richtung
	private Vector<Double> velocity;
	
	// Vergangene Positionen zum Zeichnen der Historie (= Anhängsel)
	// Erste Komponente ist x/y-Koordinate, zweite Komponente sind die 5 letzten Positionen
	private double[][] oldPositions = new double[2][5];
	
	public BoidVollständig () {
		pos = new Vector<Double>();
		// x- und y-Koordinate initialisieren
		pos.add(Math.random() * Rendering.WIDTH);
		pos.add(Math.random() * Rendering.HEIGHT);
		//System.out.println("pos x: " + pos.get(0) + " pos y: " + pos.get(1));
		velocity = new Vector<Double>();
		// Erster Wert ist Geschwindigkeit in x-Richtung, Zufallszahl zwischen -0,5 und + 0.5 
		velocity.add(Math.random()-0.5);
		// Zweiter Wert ist Geschwindigkeit in y-Richtung
		velocity.add(Math.random()-0.5);
		// Zufallsfarbe
		color = new Color((int) (Math.random() * 256 * 256 * 256));
	}
	
	// An den Grenzen wird das Boid zurück ins Spielfeld gesetzt
	// Dann: Alte Positionen der Koordinaten (= x/y-Koordinaten) werden verschoben, aktuelle Position befüllt
	public void updatePosition() {
		// Positionen bleiben im Spielfeld, Impuls leicht zurückstoßen
		// Entwicklung während des Unterrichts, hier auskommentiert
		if (pos.get(0) <= 0) {
			pos.set(0, 20.0);
			velocity.set(0, 0.1);
		}
		if (pos.get(0) >= Rendering.WIDTH) {
			pos.set(0, Rendering.WIDTH-20.0);
			velocity.set(0, -0.1);
		}
		if (pos.get(1) <= 0) {
			pos.set(1, 20.0);
			velocity.set(1, 0.1);
		}
		if (pos.get(1) > Rendering.HEIGHT) {
			pos.set(1, Rendering.HEIGHT-20.0);
			velocity.set(1, -0.1);
		}


		if (pos.get(1) > 200 && pos.get(1) < 290 && pos.get(0) > 300 && pos.get(0) < 390) {
			pos.set(1, Math.random() * (Rendering.HEIGHT - 10 + 1) + 10);
			pos.set(0, Math.random() * (Rendering.WIDTH - 10 + 1) + 10);
			velocity.set(1, -0.5);
		}
		
		
		// Merke alte Positionen
		// Idee übernommen von Ando Saabas http://www.cs.ioc.ee/~ando/index.php
		int sizeOldPos = oldPositions[0].length;
		// Historie 0 ist die älteste Position und fällt zuerst raus
		for (int i = 0; i < sizeOldPos-1; i++) {
			oldPositions[0][i] = oldPositions[0][i+1];
			oldPositions[1][i] = oldPositions[1][i+1];
		}
		// Historie 4 ist die jüngste Position
		oldPositions[0][sizeOldPos-1] = pos.get(0);
		oldPositions[1][sizeOldPos-1] = pos.get(1);			
	}

	public Vector<Double> getPos() {
		return pos;
	}

	public void setPos(Vector<Double> pos) {
		this.pos = pos;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vector<Double> getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector<Double> velocity) {
		this.velocity = velocity;
	}
	
	public double[][] getOldPositions() {
		return oldPositions;
	}

	public void setOldPositions(double[][] oldPositions) {
		this.oldPositions = oldPositions;
	}

	// Berechne eukldische Distanz von zwei Punkten v1 und v2
	public static double euclideanDist(Vector<Double> v1, Vector<Double> v2) {
		double xDist = v1.get(0) - v2.get(0);
		double yDist = v1.get(1) - v2.get(1);
		double sum = xDist*xDist + yDist*yDist;
		return Math.sqrt(sum);
	}

	// Addiere zur akt Geschwindigkeit den Vektor v1 bzw. weitere
	@SafeVarargs
	public final void addAllVelos (Vector<Double>... v) {
		double veloX = velocity.get(0);
		double veloY = velocity.get(1);
		double neueVeloX = veloX;
		double neueVeloY = veloY;
		for (int i = 0; i < v.length; i++) {
			neueVeloX = neueVeloX + v[i].get(0);
			neueVeloY = neueVeloY + v[i].get(1);
		}
		this.velocity.set(0, neueVeloX);
		this.velocity.set(1, neueVeloY);
	}
	
	// Addiere zur Position den Vektor velo
	public void addPos (Vector<Double> velo) {
		this.pos.set(0, this.getPos().get(0) + velo.get(0));
		this.pos.set(1, this.getPos().get(1) + velo.get(1));
	}
}
