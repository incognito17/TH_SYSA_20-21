package koh_vorlage;

import java.util.*;
import java.io.*;
import java.net.URL;

// Lernalgorithmus zum Trainieren der Karte
public class Trainer implements Runnable {
    // Die Implementierung basiert auf einer Vorlage von Alanter Kintar bzw.
    // http://www.ai-junkie.com/ann/som/som1.html
    // Konstanten zum Ablauf des Lernalgorithmus
    private final double START_LEARNING_RATE = 0.1, END_LEARNING_RATE = 0.025;
    private final int NUM_ITERATIONS = 2 * Lattice.HEIGHT * Lattice.WIDTH;
    private final double LATTICE_RADIUS = Math.max(Lattice.WIDTH, Lattice.HEIGHT) / 2;

    private LatticeRenderer renderer;
    private Lattice lattice;
    public static ArrayList<MyVector> inputs;
    private boolean running;
    private Thread runner;
    // Zähler für Lerniterationen
    private static int iteration = 0;

    // Welche Daten sollen verarbeitet werden?
    public enum DATA {
        RECTANGLE, CIRCLE, RING, BALL, BALL_SURFACE, SHAPE, RGB_SEED_9_COLORS, RGB_SEED_ALL_COLORS, RGB_SEED_RED, IRIS,
        TWO_TO_ONE_DIM
    }

    ;

    public static DATA myDATA = DATA.RECTANGLE;

    // Auswahl der Trainingsdaten
    public static void setDATA(DATA newData) {
        // Setze die Dimension der Gewichtsvektoren
        myDATA = newData;
        switch (myDATA) {
            case RECTANGLE:
                Lattice.DIM = 2;
                break;
            case CIRCLE:
                Lattice.DIM = 2;
                break;
            case RING:
                Lattice.DIM = 2;
                break;
            case BALL:
                Lattice.DIM = 3;
                break;
            case BALL_SURFACE:
                Lattice.DIM = 3;
                break;
            case SHAPE:
                Lattice.DIM = 2;
                break;
            case RGB_SEED_9_COLORS:
                Lattice.DIM = 3;
                break;
            case RGB_SEED_ALL_COLORS:
                Lattice.DIM = 3;
                break;
            case RGB_SEED_RED:
                Lattice.DIM = 3;
                break;
            case IRIS:
                Lattice.DIM = 4;
                break;
            case TWO_TO_ONE_DIM:
                Lattice.DIM = 2;
                break;
            default:
                break;
        }
        System.out.println(myDATA);
    }

    public Trainer(Lattice latToTrain, LatticeRenderer latticeRenderer) {
        running = true;
        initTrainingData();
        lattice = latToTrain;
        renderer = latticeRenderer;

        if (lattice != null) {
            runner = new Thread(this);
            runner.setPriority(Thread.MIN_PRIORITY);
            runner.start();
        }
    }

    public MyVector getTrainingData() {
        MyVector tempVec = null;
        // Für Rechteck als Input erzeuge zweidimensionale Zufallszahl
        if (myDATA == DATA.RECTANGLE || myDATA == DATA.TWO_TO_ONE_DIM) {
            inputs = new ArrayList<MyVector>();
            tempVec = new MyVector();
            double x = Math.random();
            double y = Math.random();
            tempVec.add(x);
            tempVec.add(y);
            inputs.add(tempVec);
            // System.out.println(tempVec.get(0) + "/" + tempVec.get(1));
        }
        if (myDATA == DATA.CIRCLE) {
            // hier die Erzeugung von Trainingsdaten ergänzen
            // Beispiel: Zufallszahlen x und y solange generieren,
            // bis x^2 + y^2 im Inneren des Kreises liegen

            inputs = new ArrayList<MyVector>();
            tempVec = new MyVector();
            double x = Math.random();
            double y = Math.random();
            while ((Math.pow((0.5 - x), 2) + Math.pow((0.5 - y), 2)) >= Math.pow(0.5, 2)) {
                x = Math.random();
                y = Math.random();
            }

            tempVec.add(x);
            tempVec.add(y);
            inputs.add(tempVec);
        }
        if (myDATA == DATA.BALL) {
            // hier die Erzeugung von Trainingsdaten ergänzen
        }
        if (myDATA == DATA.RING) {
            // hier die Erzeugung von Trainingsdaten ergänzen

            inputs = new ArrayList<MyVector>();
            tempVec = new MyVector();
            double x = Math.random();
            double y = Math.random();
            while ((Math.pow((0.5 - x), 2) + Math.pow((0.5 - y), 2)) <= Math.pow(0.4, 2) ||
                    (Math.pow((0.5 - x), 2) + Math.pow((0.5 - y), 2)) >= Math.pow(0.5, 2)) {

                x = Math.random();
                y = Math.random();
            }
            tempVec.add(x);
            tempVec.add(y);
            inputs.add(tempVec);
        }
        if (myDATA == DATA.SHAPE) {
            // hier die Erzeugung von Trainingsdaten ergänzen
            // falls die Input-Daten dreidimensional sind, dann auch
            // noch Lattice.DIM = 3; in setDATA() ändern.

            inputs = new ArrayList<MyVector>();
            tempVec = new MyVector();
            double x = Math.random();
            double y = Math.random();
            while ((Math.pow(x, 2) + Math.pow(y, 2)) > 1) {
                x = Math.random();
                y = Math.random();
            }

            tempVec.add(x);
            tempVec.add(y);
            inputs.add(tempVec);
        }

        if (myDATA == DATA.RGB_SEED_ALL_COLORS) {
            // Beliebige Inputfarben (RGB Input mit Zufallszahlen zwischen 0 und 1)
            inputs = new ArrayList<MyVector>();
            tempVec = new MyVector();
            double r = Math.random();
            double b = Math.random();
            double g = Math.random();
            tempVec.add(r);
            tempVec.add(b);
            tempVec.add(g);
            inputs.add(tempVec);
        }
        if (myDATA == DATA.RGB_SEED_9_COLORS || myDATA == DATA.RGB_SEED_RED || myDATA == DATA.IRIS) {
            // Zufallszahl zwischen 0 und Anzahl Trainingsdaten
            double indexGenerated = Math.random() * inputs.size();
            int index = (int) indexGenerated;
            // wähle eine der festen Vektoren aus den Inputdaten
            tempVec = inputs.get(index);
        }
        return tempVec;
    }

    public void initTrainingData() {
        MyVector tempVec;
        inputs = new ArrayList<MyVector>();
        if (myDATA == DATA.RGB_SEED_9_COLORS) {
            // Initialisierung von Grundfarben Red, Green, Blue, Yellow, Purple, Black,
            // White, Gray
            tempVec = new MyVector();
            tempVec.add(1.0);
            tempVec.add(0.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.0);
            tempVec.add(1.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.0);
            tempVec.add(0.0);
            tempVec.add(1.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(1.0);
            tempVec.add(1.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(1.0);
            tempVec.add(0.0);
            tempVec.add(1.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.0);
            tempVec.add(1.0);
            tempVec.add(1.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.0);
            tempVec.add(0.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(1.0);
            tempVec.add(1.0);
            tempVec.add(1.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.5);
            tempVec.add(0.5);
            tempVec.add(0.5);
            inputs.add(tempVec);
        }
        if (myDATA == DATA.RGB_SEED_RED) {
            // Mehrere Schattierungen von "Rot" als Seed wählen
            tempVec = new MyVector();
            tempVec.add(1.0);
            tempVec.add(0.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.7);
            tempVec.add(0.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.4);
            tempVec.add(0.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.1);
            tempVec.add(0.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
            tempVec = new MyVector();
            tempVec.add(0.0);
            tempVec.add(0.0);
            tempVec.add(0.0);
            inputs.add(tempVec);
        }
        if (myDATA == DATA.IRIS) {
            try {
                URL url = this.getClass().getResource("koh_vorlage/IrisDataSet.txt");
                BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
                while (br.ready()) {
                    String line = br.readLine();
                    String[] parts = line.split(",");
                    Double num1 = Double.parseDouble(parts[0]);
                    Double num2 = Double.parseDouble(parts[1]);
                    Double num3 = Double.parseDouble(parts[2]);
                    Double num4 = Double.parseDouble(parts[3]);
                    // String s = parts[4];
                    tempVec = new MyVector();
                    tempVec.add(num1);
                    tempVec.add(num2);
                    tempVec.add(num3);
                    tempVec.add(num4);
                    inputs.add(tempVec);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getNeighborhoodRadius(double iteration) {
        return (int) (LATTICE_RADIUS * Math.pow(1 / LATTICE_RADIUS, iteration / NUM_ITERATIONS));
    }

    private double getDistanceFalloff(double dist, double radius) {
        double nominator = -(dist * dist);
        double result = Math.exp(nominator / (2 * radius * radius));
        return result;
    }

    public static void setIteration(int newIteration) {
        iteration = newIteration;
    }

    public static int getIteration() {
        return iteration;
    }

    private double getLearningRate(double iteration) {
        // learningRate = START_LEARNING_RATE *
        // Math.exp(-(double)iteration/NUM_ITERATIONS);
        return (START_LEARNING_RATE * Math.pow(END_LEARNING_RATE / START_LEARNING_RATE, iteration / NUM_ITERATIONS));
    }

    public void run() {
        double dist, dFalloff;
        int nbhRadius;
        Node bmu = null, temp = null;
        MyVector curInput = null;
        double learningRate = START_LEARNING_RATE;

        while (iteration < NUM_ITERATIONS && running) {
            // hole neue Daten zum Training:
            curInput = getTrainingData();
            // Für jeden Eingabevektor suche die "best matching unit"
            bmu = lattice.getBMU(curInput);
            // BMU gefunden, jetzt Anpassung aller Neuronen im Umfeld (= Radius)
            nbhRadius = getNeighborhoodRadius(iteration);
            // System.out.println("Iteration:\t" + iteration + "\tRadius:\t" + nbhRadius);

            for (int x = 0; x < Lattice.WIDTH && myDATA != DATA.TWO_TO_ONE_DIM; x++) {
                for (int y = 0; y < Lattice.HEIGHT; y++) {
                    temp = lattice.getNode(x, y);
                    dist = bmu.distanceTo(temp);
                    // Falls Neuron innerhalb des Radius liegt, dann Gewichte anpassen
                    if (dist <= nbhRadius) {
                        // Gewichte in der Nähe des Gewinnerneurons stärker anpassen
                        dFalloff = getDistanceFalloff(dist, nbhRadius);
                        // System.out.println(bmu.getX() + "/" + bmu.getY() + "\tRadius:\t" + nbhRadius
                        // + "\t" + x + "\t" + y + "\tResult: \t" + dFalloff);
                        temp.adjustWeights(curInput, learningRate, dFalloff);
                    }
                }
            }
            if (myDATA == DATA.TWO_TO_ONE_DIM) {
                for (int x = 0; x < Lattice.WIDTH; x++) {
                    for (int y = 0; y < Lattice.HEIGHT; y++) {
                        temp = lattice.getNode(x, y);
                        dist = bmu.distanceToOneDimension(temp);
                        if (dist <= (nbhRadius * nbhRadius)) {
                            dFalloff = getDistanceFalloff(dist, nbhRadius);
                            temp.adjustWeights(curInput, learningRate, dFalloff);
                        }
                    }
                }
            }
            iteration++;

            learningRate = getLearningRate(iteration);
            // System.out.println(iteration + "\t" + learningRate);

            // Zeichne das Ergebnis auf den Bildschirm
            if (iteration % 5 == 0) {
                renderer.render();
                try {
                    //Thread.sleep(10);
                } catch (Exception e) {
                }
            }
        }
        running = false;
    }

    public void stop() {
        if (runner != null) {
            running = false;
            while (runner.isAlive()) {
            }
        }
    }

}
