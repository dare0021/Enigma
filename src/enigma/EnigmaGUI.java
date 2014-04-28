package enigma;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class EnigmaGUI extends JFrame implements IConstantsUI, IStringsEnigma {
	ShipBattleGUI gui;
	
	public EnigmaGUI(){
		this.setTitle(TITLE);
		
		gui = new ShipBattleGUI();
		this.add(gui, BorderLayout.CENTER);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}

}
