package canvasItems;

/**
 * Data container for buttons
 * Changing the width/height data will not affect the draw size
 * It is for coordinate calculation only
 * 
 * The 3 images will have the same properties of the bg image
 * clickimage has precedence over hoverimage, which has precedence over bgimage
 * 
 * JIFIMAGES ARE NOT STARTED AUTOMATICALLY
 * 		They will remain on frame 0 until started at some point in the code
 * 
 * This class is not scalable
 * 		why would you scale a UI fixture
 */

public class CButton extends ACItem{
	public double x1, y1;
	public boolean showHover, showClicked, enabled;
	public String actionCommand;
	public CStaticImage bgimage, hoverimage, clickimage;
	
	public CButton(CButtonDef def, String name, String actionCmd){
		super(name);
		init(def, name, actionCmd);
	}
	/**Uses the name as the action command*/
	public CButton(CButtonDef def, String name){
		super(name);
		init(def, name, name);
	}
	
	private void init(CButtonDef def, String name, String actionCmd){
		x0 = def.x;
		y0 = def.y;
		if(def.width < 0 && def.height < 0){
			x1 = x0 + def.bgimage.image.getWidth();
			y1 = y0 + def.bgimage.image.getHeight();
		}else{
			x1 = x0 + def.width;
			y1 = y0 + def.height;
		}
		bgimage = def.bgimage;
		hoverimage = def.hoverimage;
		clickimage = def.clickimage;
		actionCommand = actionCmd;
		showHover = false;
		showClicked = false;
		enabled = true;
		setOpacity(def.opacity);
		setDepth(def.depth);
	}
	
	public CStaticImage getImage(){
		if(showClicked)
			return clickimage;
		else if(showHover)
			return hoverimage;
		else
			return bgimage;
	}

	/**Calls getX()**/
	public double getX0() {return getX();}
	/**Calls getY()**/
	public double getY0() {return getY();}
	public double getWidth() {return x1-x0;}
	public double getHeight() {return y1-y0;}
	public double getApparentWidth() {return getImage().image.getWidth();}
	public double getApparentHeight(){return getImage().image.getHeight();}
	public boolean isEnabled(){return enabled;}
	
	/**bypasses setX()'s auxiliary behaviors
	 * like setting x1 according to getWidth()
	 */
	public void setX0(double x) {x0 = x;}
	/**bypasses setY()'s auxiliary behaviors
	 * like setting y1 according to getHeight()
	 */
	public void setY0(double y) {y0 = y;}
	public void setEnabled(boolean e){enabled = e;}
	
	public void setX(double x){
		double width = getWidth();
		x0 = x;
		x1 = x0 + width;
	}public void setY(double y){
		double height = getHeight();
		y0 = y;
		y1 = y0 + height;
	}
}
