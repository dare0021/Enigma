package canvasItems;

public class CTextButton extends CButton{
	private CBasicText text;
	private double xmargin, ymargin;

	public CTextButton(CButtonDef def, String name, String actionCmd, CBasicText _text) {
		super(def, name, actionCmd);
		init(_text);
	}/**Uses the text in the given text object as the action command
	    *Note that the action command is not the name**/
	public CTextButton(CButtonDef def, String name, CBasicText _text){
		super(def, name, _text.text);
		init(_text);
	}
	public CTextButton(CButtonDef def, CBasicText _text){
		super(def, _text.text);
		init(_text);
	}
	
	private void init(CBasicText _text){
		text = _text;
		setMargins();
		adjustForMargins();
	}
	
	public void setMargins(){
		if(text != null){
			setMargins(this.getWidth()/10, (this.getHeight() + text.font.getSize())/2);
		}
	}public void setMargins(double x, double y){
		xmargin = x;
		ymargin = y;
	}
	
	private void adjustForMargins(){
		text.setX(this.getX() + xmargin);
		text.setY(this.getY() + ymargin);
	}
	
	public double getXMargin(){return xmargin;}
	public double getYMargin(){return ymargin;}
	
	public void setText(CBasicText basicText){
		text = basicText;
	}public void setText(String txt){
		text.text = txt;
	}
	
	public CBasicText getTextObject(){return text;}
	public String getText(){return text.text;}
	
	@Override
	public void setX(double x){
		super.setX(x);
		adjustForMargins();
	}@Override
	public void setY(double y){
		super.setY(y);
		adjustForMargins();
	}
}
