package celltraffic_vorlage;

import java.util.*;

public class TrafficSimulation {

	static final int SIZE = 100; // Anzahl an Zellen
	static final int MAXTIME = 1000; // Anzahl an Generationen z.B. 100
	static final int VMAX = 5; // maximale Geschwindigkeit der Fahrzeuge
	static final int FREQUENCY = 17; // Wie viel Prozent Fahrzeuge, z.B. 30 = 30%

	static int anzahlFahrzeuge = 1; // Anzahl an Fahrzeugen (mind. eins, wird automatisch erhöht)
	static double geschwAktDurchschnitt = 0; // aktuelle Durchschnittsgeschwindigkeit
	static final boolean troedelnAktiv = true; // Trödelfaktor (aktiv/inaktiv)
	static final double troedelP = 0.1; // Trödel-Wahrscheinlichkeit

	static final int delay = 5; // Anzeige-Pause zwischen Generationen 200 ms
	static Random randomGenerator = new Random(1);

	// Fahrzeuge auf der Piste, null = kein Fahrzeug
	static Auto[] S = new Auto[SIZE];

	// Daten der nächsten Generation
	static Auto[] SNew = new Auto[SIZE];

	// wie viele Runden hat das schwarze Auto absolviert?
	static int anzahlRundenSchwarz = 0;

	// Berechne die Anzahl der freien Zellen vor dem Fahrzeug, maximal VMAX
	static int freeCells(int i) {
		int free = 0;
		forLoop: for (int f = 1; f <= VMAX; f++) {
			// Nachbar rechts: normalerweise um 1 erhöhen, falls am rechten Rand, dann
			// wieder links anfangen
			int index = (i + f) % SIZE;

			// Zähler erhöhen:
			if (S[index] == null) {
				free++;
			} else { // bis zum nächsten Fahrzeug
				break forLoop;
			}
		}
		return free;
	}

	// Berechne Zellen der nächsten Iteration
	static void nextGeneration() {
		for (int i = 0; i < SIZE; i++) {
			SNew[i] = null;
		}

		double vSumme = 0; // Summe aller Geschwindigkeiten
		for (int i = 0; i < SIZE; i++) {
			if (S[i] != null) {
				Auto merkeAuto = S[i];
				int free = freeCells(i);
				int geschwNeu = 0; // neue Geschwindigkeit

				geschwNeu = Math.min(S[i].geschw + 1, VMAX); // 1. Regel Beschleunigen
				if (geschwNeu > free) // 2. Regel Abbremsen
					geschwNeu = free;

				double zufall = randomGenerator.nextDouble();
				if (troedelnAktiv && zufall < troedelP) {
					geschwNeu = Math.max(geschwNeu - 1, 0); // Beim Trödeln Geschw. vermindern
				}

				// Neue Position, nachdem das Auto die Regel Fahren durchgeführt hat
				int posNeu = (merkeAuto.pos + geschwNeu) % SIZE; // Fahren

					// Rundenwechsel falls Position von nahe 100 auf nahe 0 zurückspringt
					if (merkeAuto.pos > posNeu) {
						anzahlRundenSchwarz++;
					}

				// neue Position mit neuer Geschw
				SNew[posNeu] = merkeAuto;
				SNew[posNeu].pos = posNeu;
				SNew[posNeu].geschw = geschwNeu;
				vSumme += geschwNeu;
			}
		}

		// Übernehme neue Daten
		for (int i = 0; i < SIZE; i++)
			S[i] = SNew[i];

		// Durchschnittsgeschwindigkeit berechnen
		geschwAktDurchschnitt = (double) vSumme / anzahlFahrzeuge;
	}

	// Ausgabe Konsole
	static void printPopulation(int t) {
		System.out.print("Generation " + t + ": ");
		for (int i = 0; i < SIZE; i++) {
			if (S[i] != null)
				System.out.print(S[i].geschw);
			else
				System.out.print(".");
		}
		System.out.println();
	}

	public static void initialisiereFeld() {
		for (int i = 0; i < SIZE; i++) {
			S[i] = null;
			SNew[i] = null;
		}

		// ein schwarzes Auto soll es immer geben
		S[0] = new Auto(0, randomGenerator.nextInt(VMAX + 1));
		S[0].kennzeichen = "beobachtung"; // schwarzes Kästchen zur Beobachtung

		for (int i = 1; i < SIZE * FREQUENCY / 100; i++) {
			boolean freiePositionGefunden = false;
			int zufallsPosition = randomGenerator.nextInt(SIZE);
			do {
				if (S[zufallsPosition] == null) {
					freiePositionGefunden = true;
					// Geschwindigkeit 0..5
					S[zufallsPosition] = new Auto(zufallsPosition, randomGenerator.nextInt(VMAX + 1));
				} else {
					// System.out.println("Suche freie Position - " + zufallsPosition + " belegt");
					// neue Zufallszahl generieren
					zufallsPosition = randomGenerator.nextInt(SIZE);
				}
			} while (!freiePositionGefunden);

			anzahlFahrzeuge += 1;
		}
	}

	public static void main(String[] args) throws Exception {
		initialisiereFeld();
		System.out.println("Anzahl Fahrzeuge: " + anzahlFahrzeuge);
		System.out.println("Trödeln aktiv: " + troedelnAktiv);
		printPopulation(0);

		RenderTraffic anzeige = new RenderTraffic();
		for (int t = 1; t < MAXTIME; t++) {
			nextGeneration();
			// printPopulation(t);
			anzeige.repaint();
			Thread.sleep(delay);
		}
		System.out.println("Ergebnis Anzahl Runden schwarzes Auto: " + anzahlRundenSchwarz);
	}
}
