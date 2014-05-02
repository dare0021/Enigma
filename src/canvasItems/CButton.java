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
	public double x1, y1, width, height;
	public boolean showHover, showClicked;
	public final String actionCommand;
	public final CStaticImage bgimage, hoverimage, clickimage;
	
	public CButton(CButtonDef def, String name, String actionCmd){
		super(name);
		x0 = def.x;
		y0 = def.y;
		width = def.width;
		height = def.height;
		opacity = def.opacity;
		x1 = x0 + def.width;
		y1 = y0 + def.height;
		bgimage = def.bgimage;
		hoverimage = def.hoverimage;
		clickimage = def.clickimage;
		actionCommand = actionCmd;
		showHover = false;
		showClicked = false;
	}
	
	public CStaticImage getImage(){
		if(showClicked)
			return clickimage;
		else if(showHover)
			return hoverimage;
		else
			return bgimage;
	}
}
