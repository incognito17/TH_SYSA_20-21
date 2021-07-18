package boid_vorlage;

import java.util.*;

public class BoidList {
    static final int NUM_BOIDS = 10;
    // Ab diesem Abstand werden Boids abgestoßen
    static final int DISTANCE = 20;

    private ArrayList<BoidVollständig> bl;

    public BoidList() {
        bl = new ArrayList<BoidVollständig>();
        if (NUM_BOIDS == 2) {
            BoidVollständig b = new BoidVollständig();
            Vector<Double> pos = new Vector<Double>();
            pos.add(0.0);
            pos.add(0.0);
            b.setPos(pos);
            Vector<Double> velo = new Vector<Double>();
            velo.add(0.0);
            velo.add(0.0);
            b.setVelocity(velo);
            bl.add(b);

            b = new BoidVollständig();
            pos = new Vector<Double>();
            pos.add(0.0);
            pos.add(100.0);
            b.setPos(pos);
            velo = new Vector<Double>();
            velo.add(0.0);
            velo.add(0.0);
            b.setVelocity(velo);
            bl.add(b);
        } else {
            for (int i = 0; i < NUM_BOIDS; i++) {
                BoidVollständig b = new BoidVollständig();
                bl.add(b);
            }
        }
    }

    public ArrayList<BoidVollständig> getBl() {
        return bl;
    }

    public void addBoid(BoidVollständig b) {
        if (b != null)
            bl.add(b);
    }

    // Berechnet für einen Boid den Vektor zum Verschieben ins Zentrum
    public Vector<Double> moveCloser(BoidVollständig b) {
        Vector<Double> massCentre = new Vector<>();
        double massCentreX = 0;
        double massCentreY = 0;
        // Berechne das "Zentrum", d.h. die gewichtete Position aller Boids (ohne sich
        // selbst)
        for (int i = 0; i < bl.size(); i++) {
            if (b != bl.get(i)) {
                massCentreX = massCentreX + bl.get(i).getPos().get(0);
                massCentreY = massCentreY + bl.get(i).getPos().get(1);
            }
        }
        massCentreX = massCentreX / (bl.size() - 1);
        massCentreY = massCentreY / (bl.size() - 1);
        massCentre.add(massCentreX);
        massCentre.add(massCentreY);
        // System.out.println("Zentrum x: " + massCentreX + " Zentrum y: " + massCentreY
        // );
        Vector<Double> v = new Vector<>();
        // Berechne den Vektor zum Verschieben: 1 Prozent in Richtung Zentrum
        double OnePercentXDrift = (massCentre.get(0) - b.getPos().get(0)) / 100;
        double OnePercentYDrift = (massCentre.get(1) - b.getPos().get(1)) / 100;
        v.add(OnePercentXDrift);
        v.add(OnePercentYDrift);
        return v;
    }

    // Berechnet für einen Boid den Vektor, damit sich zwei Boids nicht zu nahe
    // kommen (Vermeidung Kollision)
    public Vector<Double> moveAway(BoidVollständig b) {
        Vector<Double> v = new Vector<>();
        v.add(0.0);
        v.add(0.0);
        for (int i = 0; i < bl.size(); i++) {
            BoidVollständig compareBoid = bl.get(i);
            if (b != compareBoid) {
                double distance = Boid.euclideanDist(b.getPos(), compareBoid.getPos());
                // falls sich zwei boids zu nahe kommen, dann abstoßen
                if (distance < DISTANCE) {
                    double xdist = (b.getPos().get(0) - compareBoid.getPos().get(0)) / 10;
                    double ydist = (b.getPos().get(1) - compareBoid.getPos().get(1)) / 10;
                    // Vektor zeigt in die Gegenrichtung
                    v.set(0, (v.get(0) + xdist));
                    v.set(1, (v.get(1) + ydist));
                }
            }
        }
        return v;
    }

    // Berechnet für einen Boid den Vektor zum Anpassen der Geschwindigkeit an
    // andere Boids
    public Vector<Double> adjustVelo(BoidVollständig b) {
        double averageVeloX = 0;
        double averageVeloY = 0;
        // Berechne die Durchschnittsgeschwindigkeit aller Boids (ohne sich selbst)
        for (int i = 0; i < bl.size(); i++) {
            if (b != bl.get(i)) {
                averageVeloX = averageVeloX + bl.get(i).getVelocity().get(0);
                averageVeloY = averageVeloY + bl.get(i).getVelocity().get(1);
            }
        }
        averageVeloX = averageVeloX / (bl.size() - 1);
        averageVeloY = averageVeloY / (bl.size() - 1);
        // System.out.println(averageVelo);

        Vector<Double> v = new Vector<Double>();
        // Berechne den Vektor: Orientierung in Richtung Durchschnittsgeschwindigkeit
        double bVeloX = b.getVelocity().get(0);
        double bVeloY = b.getVelocity().get(1);
        double XDrift = 0.05 * (averageVeloX - bVeloX);
        double YDrift = 0.05 * (averageVeloY - bVeloY);
        v.add(XDrift);
        v.add(YDrift);
        return v;
    }

    // neues Boid erzeugen oder löschen, falls im kritischen Bereich
    // Streifen von 10 Pixeln oben bzw. unten
    public void erzeugenOderLoeschen(BoidVollständig b) {
        BoidVollständig newB = new BoidVollständig();
        if (b.getPos().get(0) > 100 && b.getPos().get(0) < 170 && b.getPos().get(1) > 100 && b.getPos().get(1) < 170) {
            bl.remove(b);
            System.out.println(bl.size());
        }

        if (b.getPos().get(0) > 600 && b.getPos().get(0) < 670 && b.getPos().get(1) > 400 && b.getPos().get(1) < 470) {
            bl.add(newB);
            System.out.println(bl.size());
        }


    }

    public static void main(String[] args) {
        BoidList boidList = new BoidList();
        Rendering r = new Rendering(boidList.getBl());
        System.out.println("Anzahl Boids: " + NUM_BOIDS);

        // Parallelisierung
        // Folgende Variante ist schneller als manuelles Scheduling
        // boidList.getBl().parallelStream().forEach(b -> {
        // });
        while (true) {

            for (int i = 0; i < boidList.getBl().size(); i++) {
                BoidVollständig b = boidList.getBl().get(i);

                Vector<Double> v1 = boidList.moveCloser(b);
                Vector<Double> v2 = boidList.moveAway(b);
                Vector<Double> v3 = boidList.adjustVelo(b);
                b.addAllVelos(v1, v2, v3);

                // Neue Position als Resultat der Geschwindigkeitsvektoren etc.
                b.addPos(b.getVelocity());
                b.updatePosition();

                boidList.erzeugenOderLoeschen(b);
            };
            // Ein bisschen Pause machen
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
            r.repaint();
        }
    }
}
