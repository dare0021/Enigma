package enigma;

public class EnigmaLogic {
	EnigmaGUI formgui;
	ShipBattleLogic displogic;
	
	public EnigmaLogic(EnigmaGUI enigmagui){
		formgui = enigmagui;
		displogic = new ShipBattleLogic(formgui.gui);
	}
}
