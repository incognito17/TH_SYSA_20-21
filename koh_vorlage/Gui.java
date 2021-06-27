package koh_vorlage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gui extends JFrame {
	static final long serialVersionUID =1;
	private Trainer trainer;
	private Lattice lattice;

	private JSplitPane jSplitPane;
   
	// Anzeige Kohonen Karte links:
    private LatticeRenderer renderPanelLeft;
    
    private JComboBox choice;
    private JButton btnInitialize;
    private JButton btnStart;
    private JButton btnStop;
    private JPanel ControlsPanelRight;
 
	
	public Gui() {
    	super("Kohonen-Map");
		setDefaultCloseOperation (EXIT_ON_CLOSE); 
		
		// Model initialisieren, d.h. die Daten der Karte im Gitter:
		lattice = new Lattice();
		
		initComponents();
		renderPanelLeft.getImage();
	}

	// Initialisierung der Controls
    private void initComponents() {
        jSplitPane = new JSplitPane();
        jSplitPane.setDividerLocation(401);
        jSplitPane.setDividerSize(5);
        jSplitPane.setEnabled(false);
       
        //Fläche für die Karte links:
        renderPanelLeft = new LatticeRenderer(lattice);
        jSplitPane.setLeftComponent(renderPanelLeft);
        
        // Fläche für die Buttons rechts
        ControlsPanelRight = new JPanel();
        ControlsPanelRight.setLayout(new BoxLayout(ControlsPanelRight, BoxLayout.Y_AXIS));
        
        choice = new JComboBox(Trainer.DATA.values());
        choice.setSelectedItem(Trainer.DATA.RECTANGLE);
        Trainer.setDATA((Trainer.DATA)choice.getSelectedItem());
        choice.setAlignmentX(CENTER_ALIGNMENT);
        choice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        choice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	JComboBox cb = (JComboBox)evt.getSource();
                Trainer.DATA myChoice = (Trainer.DATA) cb.getSelectedItem();
            	Trainer.setDATA(myChoice);
            }
        });
        ControlsPanelRight.add(choice);
        
        
        btnInitialize = new JButton();
        btnInitialize.setText("Initialisiere Trainings-Daten");
        btnInitialize.setAlignmentX(CENTER_ALIGNMENT);
        btnInitialize.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        btnInitialize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	lattice = new Lattice();
            	renderPanelLeft.registerLattice(lattice);
             }
        });
        ControlsPanelRight.add(btnInitialize);

        
        btnStart = new JButton();
        btnStart.setText("Start Training");
        btnStart.setAlignmentX(CENTER_ALIGNMENT);
        btnStart.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	trainer = new Trainer(lattice, renderPanelLeft);
            	Trainer.setIteration(0);
            }
        });
        ControlsPanelRight.add(btnStart);

        
        btnStop = new JButton();
        btnStop.setText("Stop Training");
        btnStop.setAlignmentX(CENTER_ALIGNMENT);
        btnStop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	trainer.stop();
            }
        });
        ControlsPanelRight.add(btnStop);


        jSplitPane.setRightComponent(ControlsPanelRight);

        getContentPane().add(jSplitPane, BorderLayout.CENTER);

        pack();
        setSize(new Dimension(620, 480));
        setVisible(true);
    }



	public static void main(String[] args) {
		new Gui();	
	}
	

}
