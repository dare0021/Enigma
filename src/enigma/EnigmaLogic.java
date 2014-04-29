package enigma;

public class EnigmaLogic {
	EnigmaGUI formgui;
	ExampleLogic displogic;
	
	public EnigmaLogic(EnigmaGUI enigmagui){
		formgui = enigmagui;
		displogic = new ExampleLogic(formgui.gui);
	}
}
