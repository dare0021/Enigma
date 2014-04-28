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
	
	public double width, height, x, y;
	public Color fillColor;
	public Font font;
	public String msg;
	public double opacity;
	public String url;
	
	public void setLocation(double xpos, double ypos){x = xpos; y = ypos;}
	public void setSize(double w, double h){width = w; height = h;}
	
	public CTextDef createTextDef(){return new CTextDef(msg, x, y, fillColor, font);}
	public CImageDef createImageDef(){return new CImageDef(url, x, y, width, height, opacity);}
}
