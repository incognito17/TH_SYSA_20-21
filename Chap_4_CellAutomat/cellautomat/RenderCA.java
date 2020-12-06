package cellautomat;

import java.awt.*;

import javax.swing.*;

public class RenderCA extends JPanel  {
	static final long serialVersionUID =1;
	// Breite und Höhe des Ausgabefensters
	private int WIDTH = 600;
	private int HEIGHT = 600;
	// Daten zum Anzeigen:
	private int[][] S;
	private int LENGTH;
	private int MAXTIME;
	
	RenderCA (int[][] S, int length, int maxtime) {	 		 
		JFrame f = new JFrame();     
		f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);    
		f.setSize(WIDTH, HEIGHT);
		this.setPreferredSize(new Dimension(1800,1800));
		JScrollPane scroll = new JScrollPane(this);
		f.add(scroll);

		f.setVisible (true); 
		this.S = S;
		this.LENGTH = length;
		this.MAXTIME = maxtime;
	}
	
	public void paintComponent(Graphics g) { 
		g.setColor(Color.WHITE);
		//fillRect-Parameter: x, y, width, height
		g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());

		//int SQUARE_SIZE = 20;
		int SQUARE_SIZE = WIDTH/LENGTH;
		for (int t = 0; t < MAXTIME; t++) {
			for (int i = 0; i < LENGTH; i++) {
				if (S[t][i] == 1) {
					g.setColor(Color.BLUE);
				}
				else {
					g.setColor(Color.WHITE);
				}
				g.fillRect (i*SQUARE_SIZE, t*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				g.setColor(Color.BLACK);
				g.drawRect(i*SQUARE_SIZE, t*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
			}
		}		
	}
}
