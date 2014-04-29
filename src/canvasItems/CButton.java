package canvasItems;

/**
 * Changing the width/height data will not affect the draw size
 * It is for coordinate calculation only
 * 
 * The hover image will have the same properties of the bg image
 * 
 * This class is not scalable
 * 		why would you scale a UI fixture
 */

public class CButton {
	public double x, y, x1, y1, width, height;
	private float opacity;
	public String bgimage, hoverimage;
	public String name, actionCommand;
	
	public CButton(CImageDef def, String _name, String actionCmd){
		init(def.x, def.y, def.width, def.height, def.address, def.address, def.opacity, _name, actionCmd);
	}public CButton(CImageDef def, String hoverurl, String _name, String actionCmd){
		init(def.x, def.y, def.width, def.height, def.address, hoverurl, def.opacity, _name, actionCmd);
	}
	
	private void init(double xpos, double ypos, double w, double h, String bg, String hover, double o, String _name, String cmd){
		x = xpos;
		y = ypos;
		width = w;
		height = h;
		opacity = (float) o;
		x1 = x + w;
		y1 = y + h;
		bgimage = bg;
		hoverimage = hover;
		name = _name;
		actionCommand = cmd;
	}
	
	public float getOpacity() {return opacity;}
	public void setOpacity(double o) {
		if(o<0)
			o = 0;
		else if(o>1)
			o = 1;
		opacity = (float)o;
	}
}
