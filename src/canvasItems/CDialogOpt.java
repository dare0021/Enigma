package canvasItems;

import java.util.ArrayList;

public class CDialogOpt extends CDialogBox {
	public ArrayList<CButton> btns;
	public ArrayList<CDialogBox> results;

	public CDialogOpt(String _name, CDialogSeq _parent) {
		super(_name, _parent);
		btns = new ArrayList<CButton>();
		results = new ArrayList<CDialogBox>();
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
}
