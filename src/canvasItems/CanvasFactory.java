package canvasItems;

import java.awt.Color;
import java.awt.Font;

/**
 * Constructs canvas definitions.
 * Definitions contain the minimal number of setting required to create
 * a meaningful object. More fancy items use more information
 * in conjunction with the definition. 
 * 
 * Note that the factory is optional.
 * It might be more efficient to create a definition without using the factory.
 * Another alternative is to have a dummy master class and use replicator
 * constructors to create new instances.
 */

public class CanvasFactory{
	public CanvasFactory(){}
	
	public double width, height, x, y, opacity;
	public Color fillColor;
	public Font font;
	public String msg;
	public String url;
	public CStaticImage bgimage, hoverimage, clickimage;
	public int length;
	
	public void setLocation(double xpos, double ypos){x = xpos; y = ypos;}
	public void setSize(double w, double h){width = w; height = h;}
	
	public CTextDef createTextDef(){return new CTextDef(msg, x, y, fillColor, font);}
	public CImageDef createImageDef(){return new CImageDef(url, x, y, width, height, opacity);}
	public CButtonDef createButtonDef(){return new CButtonDef(x, y, width, height, opacity, bgimage, hoverimage, clickimage);}
	public CTextBoxDef createTextBoxDef(){return new CTextBoxDef(x, y, width, height, opacity, bgimage, hoverimage, font, fillColor, length);}
}
