package canvasItems;

import java.util.ArrayList;

public class CDialogOpt extends CDialogBox {
	public ArrayList<CButton> btns;
	public ArrayList<CDialogBox> results;
	/** Height off the bottom of the screen for the last option button */
	private double optionHeight;

	public CDialogOpt(String _name, CDialogSeq _parent) {
		super(_name, _parent);
		btns = new ArrayList<CButton>();
		results = new ArrayList<CDialogBox>();
		optionHeight = 0;
	}

	public CDialogOpt(String _name, CDialogSeq _parent, CDialogBox orig) {
		super(_name, _parent, orig);
		btns = new ArrayList<CButton>();
		results = new ArrayList<CDialogBox>();
	}	

	/**
	 * CDialogOpt ignores setNext()
	 */
	@Override
	public void setNext(CDialogBox _next){
		System.out.println("WARN: CDialogOpt ignores setNext()");
		new Exception().printStackTrace();
	}
	
	public void setOptionHeight(double h){
		optionHeight = h;
	}public double getOptionHeight(){
		return optionHeight;
	}
}
