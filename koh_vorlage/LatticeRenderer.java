package koh_vorlage;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.net.URL;

// KLasse zum Zeichnen und zur Ausgabe der Kohonen-Karte
public class LatticeRenderer extends JPanel {

	static final long serialVersionUID = 1;

	private BufferedImage img = null;
	private Font arialFont = new Font("Arial", Font.BOLD, 12);
	private Lattice lattice;
	// Größe des Panels zur Darstellung
	private final int PANELWIDTH = 400;
	private final int PANELHEIGHT = 400;

	public LatticeRenderer(Lattice lat) {
		lattice = lat;
		addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				panelMouseMoved(evt);
			}
		});
	}

	public void paint(Graphics g) {
		if (img == null)
			super.paint(g);
		else
			g.drawImage(img, 0, 0, this);
	}

	// Neue Daten im Renderer anmelden, z.B. bei neuer Initialisierung
	public void registerLattice(Lattice lat) {
		lattice = lat;
		this.render();
	}

	public void render() {
		if (Trainer.myDATA.equals(Trainer.DATA.RECTANGLE) || Trainer.myDATA.equals(Trainer.DATA.CIRCLE)
				|| Trainer.myDATA.equals(Trainer.DATA.RING) || Trainer.myDATA.equals(Trainer.DATA.BALL)
				|| Trainer.myDATA.equals(Trainer.DATA.BALL_SURFACE) || Trainer.myDATA.equals(Trainer.DATA.SHAPE))
			this.renderTwoDimensions();
		if (Trainer.myDATA.equals(Trainer.DATA.RGB_SEED_9_COLORS)
				|| Trainer.myDATA.equals(Trainer.DATA.RGB_SEED_ALL_COLORS)
				|| Trainer.myDATA.equals(Trainer.DATA.RGB_SEED_RED))
			this.renderRGB();
		if (Trainer.myDATA.equals(Trainer.DATA.IRIS))
			this.renderIRIS();
		if (Trainer.myDATA.equals(Trainer.DATA.TWO_TO_ONE_DIM))
			this.renderOneDimension();
	}

	private void showIterations(Graphics2D g2) {
		g2.setFont(arialFont);
		g2.setColor(Color.white);
		g2.fillRect(0, getHeight() - 40, getWidth(), 15);
		g2.setColor(Color.black);
		g2.drawString("Iteration: " + Trainer.getIteration(), 5, getHeight() - 28);
	}

	/*
	 * x/y ---- x+1/y
	 * 
	 * 
	 * x/y+1 ---x+1/y+1
	 */
	public void renderOneDimension() {
		Graphics2D g2 = img.createGraphics();
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, PANELWIDTH, PANELHEIGHT);

		for (int x = 0; x < Lattice.WIDTH - 1; x++) {
			for (int y = 0; y < Lattice.HEIGHT - 1; y++) {
				int xpos1 = (int) ((lattice.getNode(x, y).getVector().get(0)) * PANELWIDTH);
				int ypos1 = (int) ((lattice.getNode(x, y).getVector().get(1)) * PANELHEIGHT);
				int xpos2 = (int) ((lattice.getNode(x + 1, y).getVector().get(0)) * PANELWIDTH);
				int ypos2 = (int) ((lattice.getNode(x + 1, y).getVector().get(1)) * PANELHEIGHT);
				int xpos3 = (int) ((lattice.getNode(x, y + 1).getVector().get(0)) * PANELWIDTH);
				int ypos3 = (int) ((lattice.getNode(x, y + 1).getVector().get(1)) * PANELHEIGHT);
				int xpos4 = (int) ((lattice.getNode(x + 1, y + 1).getVector().get(0)) * PANELWIDTH);
				int ypos4 = (int) ((lattice.getNode(x + 1, y + 1).getVector().get(1)) * PANELHEIGHT);
				// System.out.println(xpos1 + "/" + ypos1 + " " + xpos2 + "/" + ypos2);

				// Kreis für den Knoten = Neuron befüllen
				g2.setColor(Color.RED);
				g2.fillOval(xpos1 - 2, ypos1 - 2, 4, 4);
				g2.fillOval(xpos4 - 2, ypos4 - 2, 4, 4);

				g2.setColor(Color.black);
				// waagerechte Verbindung von x/y zum nächsten Knoten x+1/y zeichnen:
				g2.drawLine(xpos1, ypos1, xpos2, ypos2);

				// Verbindung vom letzten Knoten einer Zeile zum ersten der nächsten Zeile
				if (x == Lattice.WIDTH - 2) {
					int xposNextLine = (int) ((lattice.getNode(0, y + 1).getVector().get(0)) * PANELWIDTH);
					int yposNextLine = (int) ((lattice.getNode(0, y + 1).getVector().get(1)) * PANELHEIGHT);
					g2.drawLine(xpos2, ypos2, xposNextLine, yposNextLine);
				}

				// waagerechte Verbindung von x/y+1 nach x+1/y+1
				g2.drawLine(xpos3, ypos3, xpos4, ypos4);

			}
		}
		this.showIterations(g2);
		g2.dispose();
		repaint();
	}

	// Yeah, it's ugly. But it works.
	// Erzeugt gemäß den Gewichtsvektoren der Knoten mit Farbe befüllte Rechtecke
	public void renderRGB() {
		// System.out.println (getWidth() + " Höhe: " +getHeight());
		// Größe pro Zelle: 10 Pixel (400 / 40 = 10)
		int cellWidth = PANELWIDTH / Lattice.WIDTH;
		int cellHeight = PANELHEIGHT / Lattice.HEIGHT;
		// System.out.println (cellWidth + " ZellenHöhe: " +cellHeight);

		double r, g, b;
		Graphics2D g2 = img.createGraphics();
		for (int x = 0; x < Lattice.WIDTH; x++) {
			for (int y = 0; y < Lattice.HEIGHT; y++) {
				// Rahmen zeichnen:
				g2.setColor(Color.black);
				g2.drawRect((int) (x * cellWidth), (int) (y * cellHeight), (int) cellWidth, (int) cellHeight);
				// Farbe innerhalb zeichnen:
				r = lattice.getNode(x, y).getVector().get(0);
				g = lattice.getNode(x, y).getVector().get(1);
				b = lattice.getNode(x, y).getVector().get(2);
				g2.setColor(new Color((float) r, (float) g, (float) b));
				g2.fillRect((int) (x * cellWidth) + 1, (int) (y * cellHeight) + 1, (int) cellWidth - 1,
						(int) cellHeight - 1);
			}
		}
		this.showIterations(g2);
		g2.dispose();
		repaint();
	}

	/*
	 * Yeah, it's ugly, too. Erzeugt gemäß den Gewichtsvektoren der Knoten die
	 * Verbindungsgeraden von benachbarten Knoten x/y ---- x+1/y | | | | x/y+1
	 * ---x+1/y+1
	 */
	public void renderTwoDimensions() {
		Graphics2D g2 = img.createGraphics();
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, PANELWIDTH, PANELHEIGHT);

		for (int x = 0; x < Lattice.WIDTH - 1; x++) {
			for (int y = 0; y < Lattice.HEIGHT - 1; y++) {
				g2.setColor(Color.black);
				int xpos1 = (int) ((lattice.getNode(x, y).getVector().get(0)) * PANELWIDTH);
				int ypos1 = (int) ((lattice.getNode(x, y).getVector().get(1)) * PANELHEIGHT);
				int xpos2 = (int) ((lattice.getNode(x + 1, y).getVector().get(0)) * PANELWIDTH);
				int ypos2 = (int) ((lattice.getNode(x + 1, y).getVector().get(1)) * PANELHEIGHT);
				int xpos3 = (int) ((lattice.getNode(x, y + 1).getVector().get(0)) * PANELWIDTH);
				int ypos3 = (int) ((lattice.getNode(x, y + 1).getVector().get(1)) * PANELHEIGHT);
				int xpos4 = (int) ((lattice.getNode(x + 1, y + 1).getVector().get(0)) * PANELWIDTH);
				int ypos4 = (int) ((lattice.getNode(x + 1, y + 1).getVector().get(1)) * PANELHEIGHT);
				// System.out.println(xpos1 + "/" + ypos1 + " " + xpos2 + "/" + ypos2);

				// Kreis für den Knoten = Neuron befüllen
				g2.fillOval(xpos1 - 2, ypos1 - 2, 4, 4);
				g2.fillOval(xpos4 - 2, ypos4 - 2, 4, 4);

				// waagerechte Verbindung von x/y zum nächsten Knoten x+1/y zeichnen:
				g2.drawLine(xpos1, ypos1, xpos2, ypos2);

				// senkrechte Verbindung von x/y nach x/y+1:
				g2.drawLine(xpos1, ypos1, xpos3, ypos3);

				// waagerechte Verbindung von x/y+1 nach x+1/y+1
				g2.drawLine(xpos3, ypos3, xpos4, ypos4);

				// senkrechte Verbindung von x+1/y nach x+1/y+1:
				g2.drawLine(xpos2, ypos2, xpos4, ypos4);

			}
		}
		this.showIterations(g2);
		g2.dispose();
		repaint();
	}

	// IRIS-Daten werden farbig markiert: Gelb für die 1. Sorte, Rot für die 2.
	// Sorte und Blau für die 3. Sorte
	public void renderIRIS() {
		// Größe pro Zelle: 10 Pixel (400 / 40 = 10)
		int cellWidth = PANELWIDTH / Lattice.WIDTH;
		int cellHeight = PANELHEIGHT / Lattice.HEIGHT;

		Graphics2D g2 = img.createGraphics();
		for (int x = 0; x < Lattice.WIDTH; x++) {
			for (int y = 0; y < Lattice.HEIGHT; y++) {
				// Rahmen zeichnen:
				g2.setColor(Color.BLACK);
				g2.drawRect((int) (x * cellWidth), (int) (y * cellHeight), (int) cellWidth, (int) cellHeight);
				g2.setColor(Color.WHITE);
				// sauberes Rechteck mit weißen Zellen initialisieren
				g2.fillRect((int) (x * cellWidth) + 1, (int) (y * cellHeight) + 1, (int) cellWidth - 1,
						(int) cellHeight - 1);
			}
		}
		// Jetzt die Farben auf die Karte: Suche BMU für die Daten und färbe ein
		try {
			// String workingDir = System.getProperty("user.dir");
			URL url = this.getClass().getResource("koh_vorlage/IrisDataSet.txt");
			BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
			String line = "";
			while (br.ready()) {
				line = br.readLine();
				String[] parts = line.split(",");
				Double num1 = Double.parseDouble(parts[0]);
				Double num2 = Double.parseDouble(parts[1]);
				Double num3 = Double.parseDouble(parts[2]);
				Double num4 = Double.parseDouble(parts[3]);
				String s = parts[4];
				MyVector tempVec = new MyVector();
				tempVec.add(num1);
				tempVec.add(num2);
				tempVec.add(num3);
				tempVec.add(num4);
				Node bmu = lattice.getBMU(tempVec);
				int x = bmu.getX();
				int y = bmu.getY();
				// System.out.println (x + "/" +y);
				if (s.equals("Iris-setosa"))
					g2.setColor(Color.YELLOW);
				if (s.equals("Iris-versicolor"))
					g2.setColor(Color.RED);
				if (s.equals("Iris-virginica"))
					g2.setColor(Color.BLUE);
				g2.fillRect((int) (x * cellWidth) + 1, (int) (y * cellHeight) + 1, (int) cellWidth - 1,
						(int) cellHeight - 1);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.showIterations(g2);
		g2.dispose();
		repaint();
	}

	public BufferedImage getImage() {
		if (img == null)
			img = (BufferedImage) createImage(getWidth(), getHeight());

		return img;
	}

	// Falls man mit der Maus über einen Knoten geht, wird der Inhalt des
	// Gewichtsvektors angezeigt
	private void panelMouseMoved(java.awt.event.MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		// System.out.println (x + " y: " + y);
		int cellWidth = PANELWIDTH / Lattice.WIDTH;
		int cellHeight = PANELHEIGHT / Lattice.HEIGHT;
		// Welche Zelle von 0 bis 39 wurde getroffen?
		int cellX = x / cellWidth;
		int cellY = y / cellHeight;
		// System.out.println ("cellY: " + cellX + " cellY: " + cellY);
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		g2.setFont(arialFont);
		StringBuffer sb = new StringBuffer("Gewichtsvektor eines Knoten: ");
		for (int i = 0; i < Lattice.DIM; i++) {
			double myDouble = 0;
			// hole die Daten aus dem Gitter
			if (cellX < Lattice.WIDTH && cellY < Lattice.HEIGHT)
				myDouble = lattice.getNode(cellX, cellY).getVector().get(i);
			// zeige den Inhalt des Gewichtsvektors an
			DecimalFormat df = new DecimalFormat("0.00");
			String s = df.format(myDouble);
			sb.append(s);
			sb.append("  ");
		}
		g2.setColor(Color.white);
		g2.fillRect(0, getHeight() - 20, getWidth(), 15);
		g2.setColor(Color.black);
		g2.drawString(sb.toString(), 5, getHeight() - 8);
		g2.dispose();
		repaint();
	}
}
