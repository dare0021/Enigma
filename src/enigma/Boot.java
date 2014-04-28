package enigma;

public class Boot {
	public static void main(String args[]){
		Boot boot = new Boot();
	}
	
	public Boot(){
		EnigmaGUI gui = new EnigmaGUI();
		EnigmaLogic logic = new EnigmaLogic(gui);
	}

}
