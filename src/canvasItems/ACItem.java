package canvasItems;

import enigma.IConstantsUI;

/**
 * Note that names and group names are not checked for uniqueness
 * threshold describes the threshold for fully transparent/opaque objects
 * e.g. an object that is <threshold opaque is fully transparent, and
 * 		an object that is >threshold opaque is fully opaque
 */
public abstract class ACItem implements ICItem, Comparable<ACItem>, IConstantsUI{
	public final String name;
	private final double opacity_threshold = OPACITY_THRESHOLD;
	private final double depth_threshold = DEPTH_THRESHOLD;
	private final double z_increment = DEPTH_INCREMENT;
	private final int z_polarity = DEPTH_FROM_SCREEN ? 1 : -1;
	private double depth, opacity;
	public CGroupNode parent;
	protected double x0, y0;
	protected ECItemType type;
	
	protected ACItem(String _name){
		name = _name;
		opacity = 1;
		depth = 0;
	}
	
	public String getName(){return name;}
	public CGroupNode getParent(){return parent;}
	protected void setParent(CGroupNode g){parent = g;}
	public double getX(){return x0;}
	public void setX(double x){x0 = x;}
	public double getY(){return y0;}
	public void setY(double y){y0 = y;}
	
	public double getDepth(){return depth;}
	public void setDepth(double z){depth = z;}
	/** 
	 * Slightly increases depth
	 */
	public void moveForward(){setDepth(getDepth() + z_increment * z_polarity);} 
	/** performs moveForward int times 
	  * negative values count as moveBackward */
	public void moveForward(int i){setDepth(getDepth() + z_increment * z_polarity * i);}/** Slightly decreases depth */
	public void moveBackward(){setDepth(getDepth() - z_increment * z_polarity);} 
	/** performs moveBackward int times 
	  * negative values count as moveForward */
	public void moveBackward(int i){setDepth(getDepth() - z_increment * z_polarity * i);}
	
	public double getOpacity(){return opacity;}
	public void setOpacity(double o) {
		if(o<opacity_threshold)
			o = 0;
		else if(o>1-opacity_threshold)
			o = 1;
		opacity = o;
	}
	
	@SuppressWarnings("unused")
	public ECItemType getType(){
		Object comp = this;
		if(false){ //so it's easy to swap around the other cases
		}else if(comp instanceof CBasicText){
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
		}else if(comp instanceof CDialogBox){
			return ECItemType.DIALOGBOX;
		}else if(comp instanceof CDialogSeq){
			return ECItemType.DIALOGSEQ;
		}else if(comp instanceof CDialogOpt){
			return ECItemType.DIALOGOPT;
		}else if(comp instanceof CGroupNode){
			return ECItemType.GROUPNODE;
		}else{
			System.out.println("ERR: ECItemType.getType unhandled type "+this);
			new Exception().printStackTrace();
		}
		return null;		
	}
	
	public void moveRelative(double dx, double dy){
		setX(getX()+dx);
		setY(getY()+dy);
	}public void moveTo(double tx, double ty){
		setX(tx);
		setY(ty);
	}
	
	/**
	 * Takes the current coordinates and
	 * reassigns them by adding the parent's coordinates
	 * thus allowing an instance to be created with
	 * relative coordinates.
	 * Depth is missing as depth is always relative 
	 * within a group.
	 */
	public void resolveRelativeCoord(){
		setX(parent.getX()+getX());
		setY(parent.getY()+getY());
	}
	
	public void opacChange(double dx){
		setOpacity(getOpacity()+dx);
	}
	
	/**
	 * Uses an offset of 1 from the zero to prevent ((int)0.xxxx) == 0
	 */
	@Override
	public int compareTo(ACItem b){
		if(b == null)
			return 1; //Java internal tests includes compareTo(null) is int
		double diff = (this.getDepth()-b.getDepth())*z_polarity;
		if(diff > depth_threshold/2)
			return ((int)diff)+1;
		else if(diff < -1*depth_threshold/2)
			return ((int)diff)-1;
		else
			return 0;
	}
}
