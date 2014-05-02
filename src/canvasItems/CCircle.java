package canvasItems;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * or ellipse...whatev
 */
public class CCircle extends CRect {
	public CCircle(double xFrom, double yFrom, double width, double height, String name){
		super(xFrom, yFrom, width, height, name);
	}public CCircle(CCircle orig, String name){
		super(orig, name);
	}
	
	public Shape getShape(){
		return new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
	}
}
