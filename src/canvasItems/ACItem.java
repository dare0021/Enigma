package canvasItems;

import java.util.Vector;

import enigma.IConstantsUI;

/**
 * Note that names and group names are not checked for uniqueness
 * threshold describes the threshold for fully transparent/opaque objects
 * e.g. an object that is <threshold opaque is fully transparent, and
 * 		an object that is >threshold opaque is fully opaque
 */
public abstract class ACItem implements ICItem, IConstantsUI{
	public final String name;
	private final double threshold = OPACITY_THRESHOLD;
	public String group;
	public double x0, y0, opacity;
	protected ECItemType type;
	
	protected ACItem(String _name){
		name = _name;
	}
	
	public String getName(){return name;}
	public String getGroup(){return group;}
	public void setGroup(String g){group = g;}
	public double getX(){return x0;}
	public void setX(double x){x0 = x;}
	public double getY(){return y0;}
	public void setY(double y){y0 = y;}
	
	public double getOpacity(){return opacity;}
	public void setOpacity(double o) {
		if(o<threshold)
			o = 0;
		else if(o>1-threshold)
			o = 1;
		opacity = o;
	}
	
	@SuppressWarnings("unused")
	public ECItemType getType(){
		Object comp = this;
		if(false){ //so it's easy to swap around the other cases
		}else if(comp instanceof CText){
			return ECItemType.TEXT;
		}else if(comp instanceof CButton){
			return ECItemType.BUTTON;
		}else if(comp instanceof CJifImage){
			return ECItemType.JIFIMAGE;
		}else if(comp instanceof CStaticImage){
			return ECItemType.STATICIMAGE;
		}else if(comp instanceof CRect){
			return ECItemType.RECT;
		}else if(comp instanceof CCircle){
			return ECItemType.CIRCLE;
		}else if(comp instanceof CLine){
			return ECItemType.LINE;
		}else if(comp instanceof CRoundRect){
			return ECItemType.ROUNDRECT;
		}else if(comp instanceof CTextBox){
			return ECItemType.TEXTBOX;
		}else{
			System.out.println("ERR: ECItemType.getType unhandled type "+this);
			new Exception().printStackTrace();
		}
		return null;		
	}
	
	public void moveRelative(double dx, double dy){
		x0 += dx;
		y0 += dy;
	}public void moveTo(double tx, double ty){
		x0 = tx;
		y0 = ty;
	}
}
