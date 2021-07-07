package celltraffic_vorlage;

import java.awt.*;
import javax.swing.*;

public class RenderTraffic extends JPanel {
	static final long serialVersionUID = 1;
	static final int SIZE = 1000;
	// in welcher Zeile wird die Historie gemalt?
	static int y_position = SIZE / 2;

	RenderTraffic() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(new BorderLayout());
		f.setSize(SIZE, SIZE);
		f.add(this);
		f.setVisible(true);
	}

	public Color ermittleFarbe(Auto aktAuto) {
		switch (aktAuto.geschw) {
		case 0:
			return Color.red;
		case 1:
			return Color.pink;
		case 2:
			return Color.orange;
		case 3:
			return Color.yellow;
		case 4:
			return new Color(180, 255, 180); // hellgrün
		case 5:
			return Color.green;
		default:
			return Color.white; // sollte nicht auftreten
		}
	}

	public void paintComponent(Graphics g) {
		// vorigen Inhalt weiß übermalen
		g.setColor(Color.white);
		// Streifen unten für Historie freilassen
		// fillRect-Parameter: x, y, width, height
		g.fillRect(0, 0, SIZE, SIZE - 500);

		final int groesseQuadrat = 15;

		for (int i = 0; i < TrafficSimulation.SIZE; i++) {
			if (TrafficSimulation.S[i] != null) {
				// Farbe je nach Geschwindigkeit setzen
				g.setColor(ermittleFarbe(TrafficSimulation.S[i]));
				// Ein Beispielauto ist schwarz
				if (TrafficSimulation.S[i].kennzeichen.equals("beobachtung"))
					g.setColor(Color.black);
			} else {
				g.setColor(Color.LIGHT_GRAY);
			}

			int xOffset = 0;
			int yOffset = 0;

			if (i < TrafficSimulation.SIZE / 4) {
				xOffset = i * groesseQuadrat;
				yOffset = groesseQuadrat;
			} else if (i < TrafficSimulation.SIZE / 2) {
				xOffset = TrafficSimulation.SIZE / 4 * groesseQuadrat;
				yOffset = groesseQuadrat + (i - TrafficSimulation.SIZE / 4) * groesseQuadrat;
			} else if (i < TrafficSimulation.SIZE * 3 / 4) {
				xOffset = TrafficSimulation.SIZE / 4 * groesseQuadrat
						- (i - TrafficSimulation.SIZE / 2) * groesseQuadrat;
				yOffset = TrafficSimulation.SIZE / 4 * groesseQuadrat + groesseQuadrat;
			} else {
				xOffset = 0 * groesseQuadrat;
				yOffset = groesseQuadrat + TrafficSimulation.SIZE / 4 * groesseQuadrat
						- (i - TrafficSimulation.SIZE * 3 / 4) * groesseQuadrat;
			}

			g.fillRect(xOffset, yOffset, groesseQuadrat, groesseQuadrat);
			g.setColor(Color.BLACK);
			g.drawRect(xOffset, yOffset, groesseQuadrat, groesseQuadrat);
			// g.drawString(String.valueOf(i), xOffset, yOffset);
		}

		g.setColor(Color.BLACK);
		g.drawString(
				"geschwAktDurchschnitt: " + (double) Math.round(TrafficSimulation.geschwAktDurchschnitt * 1000) / 1000,
				100, 100);

		// bei jedem Durchlauf neue Zeile für die Historie
		final int QUADRAT = 4; // Größe der Kästchen
		y_position = y_position + QUADRAT;
		// System.out.println("y-pos: " + y_position);
		for (int i = 0; i < TrafficSimulation.SIZE; i++) {
			g.setColor(Color.white);
			if (TrafficSimulation.S[i] != null) {
				g.setColor(ermittleFarbe(TrafficSimulation.S[i]));
			}
			g.fillRect(i * QUADRAT, y_position, QUADRAT, QUADRAT);
		}
	}
}
