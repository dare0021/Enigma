package enigma;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class EnigmaGUI extends JFrame implements IConstantsUI, IStringsEnigma {
	ExampleGUI gui;
	
	public EnigmaGUI(){
		this.setTitle(TITLE);
		
		gui = new ExampleGUI();
		this.add(gui, BorderLayout.CENTER);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}

}
