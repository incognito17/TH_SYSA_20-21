package cellautomat;

import java.awt.*;
import javax.swing.*;

public class RenderCyclic extends JPanel  {
	static final long serialVersionUID =1;
	int WIDTH = 400;
	int HEIGHT = 430;
	// Daten zum Anzeigen:
	int[][] S;
	
	RenderCyclic (int[][] S) {	 		 
		JFrame f = new JFrame();     
		f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);    
		f.getContentPane ().setLayout (new BorderLayout ());
		f.setSize(WIDTH, HEIGHT);
		f.add(this);
		f.setVisible (true); 
		this.S = S;
	}
	
	public void paintComponent(Graphics g) { 
		g.setColor(Color.WHITE);
		int SIZE = S.length;
		//fillRect-Parameter: x, y, width, height
		g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());
		// Gr��e der Quadrate in Abh�ngigkeit vom Platz und der Anzahl der Zellen
		int SQUARE_SIZE = WIDTH/SIZE;
		// i ist die Zeile (d.h. y-Koordinate), j die Spalte (d.h. x-Koordinate)
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				g.setColor(new Color(25*S[i][j], 25*S[i][j], 255));
				g.fillRect (j*SQUARE_SIZE, i*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				g.setColor(Color.BLACK);
				//g.drawRect(j*SQUARE_SIZE, i*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
			}
		}		
	}
}
