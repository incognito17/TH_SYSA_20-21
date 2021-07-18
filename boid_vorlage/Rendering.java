package boid_vorlage;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Rendering extends JPanel  {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static final long serialVersionUID =1;

	private ArrayList<BoidVollständig> bl = null;

	Rendering(ArrayList<BoidVollständig> boidlist) {
		this.bl = boidlist;
		JFrame f = new JFrame();     
		f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);    
		f.add(this);
		f.pack();
		f.setVisible (true); 
	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
    
	public void paintComponent(Graphics g) { 
		// altes Bild mit wei?em Rechteck l?schen:
		g.setColor(Color.WHITE);
		//fillRect-Parameter: x, y, width, height
		g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());
		for (int i = 0; i < bl.size(); i++) {
			BoidVollständig b = bl.get(i);
			g.setColor(b.getColor());
			double xPos = b.getPos().get(0);
			double yPos = b.getPos().get(1);
			g.fillOval((int)Math.round(xPos), (int)Math.round(yPos), 10, 10);

			// bef?lle Kreise mit abnehmender Gr??e als "Anhaengsel" (Historie) der aktuellen Position
			// die Historie 0 ist die aelteste Spur mit dem kleinsten Kreis
			// die Historie 4 ist die juengste Spur mit dem groe?ten Kreis
			double[][] oldPos = b.getOldPositions();
			for (int n = 0; n < oldPos[0].length; n++) 
				g.fillOval((int)Math.round(oldPos[0][n]), (int)Math.round (oldPos[1][n]), n*2+2, n*2+2);	
			
			// Zwei Streifen zur Anzeige des kritischen Bereichs
			/*
			g.setColor(Color.RED);
			g.fillRect(0, 0, WIDTH, 10);
			g.setColor(Color.GREEN);
			g.fillRect(0, HEIGHT-10, WIDTH, 10);
			*/

			g.setColor(Color.BLUE);
			g.fillOval(300, 200, 100, 100);

			g.setColor(Color.RED);
			g.fillRect(100, 100, 70, 70);

			g.setColor(Color.GREEN);
			g.fillRect(600, 400, 70, 70);


		}
	}
}
