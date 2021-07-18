package ant;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Rendering extends JPanel  {
	static final long serialVersionUID =1;
	Rendering () {	 		 
		JFrame f = new JFrame();     
		f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);    
		f.getContentPane ().setLayout (new BorderLayout ());
		f.setSize(500, 500);
		this.setPreferredSize(new Dimension(1800,1800));
		JScrollPane scroll = new JScrollPane(this);
		f.add(scroll);
		f.setVisible (true); 
	}
	
	public void paintComponent(Graphics g) {
		double scale = 0.4;
		if (Ant.DATA == 0)
			scale = 100;
		// altes Bild mit weißem Rechteck löschen:
		g.setColor(Color.WHITE);
		//fillRect-Parameter: x, y, width, height
		g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());
		//Neue Farbe für Inhalt
		g.setColor(Color.BLUE);
		Iterator<Point> myIterator = Ant.cities.iterator();
		Integer counter = 0;
		while (myIterator.hasNext()) {
			Point p = myIterator.next();
			g.fillRect((int)(p.x*scale-3), (int)(p.y*scale-3), 6, 6);
			g.drawString(counter.toString(), (int)(p.x*scale), (int)(p.y*scale-3));
			counter++;
		}
		Tabu bestRoute = Ant.bestRoute;

		int index1 = 0;
		int index2 = 0;
		for (int i = 0; i < bestRoute.getSize() - 1; i++) {
			index1 = bestRoute.get(i);
			index2 = bestRoute.get(i+1);
			Point p1 = Ant.cities.get(index1);
			Point p2 = Ant.cities.get(index2);
			g.drawLine((int)(p1.x*scale), (int)(p1.y*scale), (int)(p2.x*scale), (int)(p2.y*scale));
		}
		
		
	}

}
