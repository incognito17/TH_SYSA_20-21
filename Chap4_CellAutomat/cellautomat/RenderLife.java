package cellautomat;

import java.awt.*;
import javax.swing.*;

public class RenderLife extends JPanel  {
	static final long serialVersionUID =1;
	int[][] S;
	RenderLife (int[][] S) {	 		 
		JFrame f = new JFrame();     
		f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);    
		f.getContentPane ().setLayout (new BorderLayout ());
		f.setSize(700, 500);
		f.add(this);
		f.setVisible (true); 
		this.S = S;
	}
	
	public void paintComponent(Graphics g) { 
		g.setColor(Color.WHITE);
		int SIZE = S.length;
		//fillRect-Parameter: x, y, width, height
		g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());
		int SQ_SIZE = 20;
		// i ist die Zeile (d.h. y-Koordinate), j die Spalte (d.h. x-Koordinate)
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (S[i][j] == 1) {
					g.setColor(Color.GREEN);
				}
				else {
					g.setColor(Color.LIGHT_GRAY);
				}
				g.fillRect (j*SQ_SIZE, i*SQ_SIZE, SQ_SIZE, SQ_SIZE);
				g.setColor(Color.BLACK);
				g.drawRect(j*SQ_SIZE, i*SQ_SIZE, SQ_SIZE, SQ_SIZE);
			}
		}		
	}
}
