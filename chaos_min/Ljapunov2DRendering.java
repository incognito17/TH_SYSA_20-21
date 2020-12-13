package chaos_min;

import java.awt.*;
import javax.swing.*;

public class Ljapunov2DRendering extends JPanel {
    static int WIDTH = 800;
    static int HEIGHT = 600;// 600
    // Skalierungsfaktor (Größenänderung)
    static int SCALE = 200;

    // Verschiebung x-Koordinaten
    static int OFFSET_X = -200;
    static int OFFSET_Y = -200;
    static final long serialVersionUID = 1;

    Ljapunov2DRendering() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(WIDTH, HEIGHT);
        f.add(this);
        f.setVisible(true);
    }

    // Darstellung der x-Koordinate: Größenskaliert und Nullpunkt um Offset nach
    // rechts verschoben
    int screenX(double value) {
        int returnValue = 0;
        returnValue = (int) Math.round((value * SCALE) + OFFSET_X);
        return returnValue;
    }

    // Darstellung der y-Koordinate auf dem Bildschirm: Skaliert und
    // y-Koordinate nach oben statt wie in Java nach unten
    int screenY(double value) {
        int returnValue = 0;
        returnValue = (int) Math.round(HEIGHT - (value * SCALE) - OFFSET_Y);
        return returnValue;
    }

    private void renderLjapunov2D(Graphics2D g) {
        Ljapunov2DFuncLogist myFun = new Ljapunov2DFuncLogist();
        double pqValue = 0;
        double increment = 0.005;
        for (double p = 2.0; p < 4.0; p = p + increment) {
            for (double q = 2.0; q < 4.0; q = q + increment) {
                pqValue = myFun.lambdaPQ(p, q);
                // Bifurkation
                if (Math.abs(pqValue) <= 0.1)
                    g.setColor(Color.BLACK);
                // zwischen -1 und -0.1 ziemlich geordnet: Rot fest/ Grünstufen
                // variabel/ Blau fest
                if (pqValue > -1.0 && pqValue < -0.1)
                    g.setColor(new Color(0.5f, -(float) pqValue, 0.5f));
                if (pqValue <= -1.0) {
                    float value = -(float) pqValue;
                    value = Math.min(1, value / 5); // Obergrenze für Grün
                    g.setColor(new Color(0.5f, value, 0.5f));
                }

                // zwischen 0.1 und 1 ziemlich chaotisch, Rot fest/ Grün fest/
                // Blaustufen variabel
                if (pqValue > 0.1 && pqValue < 1.0)
                    g.setColor(new Color(0.5f, 0.5f, (float) pqValue));
                if (pqValue >= 1.0) {
                    float value = Math.min(1, (float) pqValue / 5); // Obergrenze
                    // Blau
                    g.setColor(new Color(0.5f, 0.0f, value));
                }
                // System.out.println(p + "\t" + q+ "\t Lambda: " + pqValue);
                g.fillRect(screenX(p), screenY(q), 1, 1);

            }
        }

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // altes Bild mit weißem Rechteck löschen:
        g.setColor(Color.WHITE);
        // fillRect-Parameter: x, y, width, height
        g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());

        renderLjapunov2D(g2);
    }

    public static void main(String[] args) {
        new Ljapunov2DRendering();
    }
}
