package canvasItems;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * or ellipse...whatev
 */
public class CCircle extends CRect {
	public CCircle(double xFrom, double yFrom, double width, double height){
		super(xFrom, yFrom, width, height);
	}public CCircle(CCircle orig){
		super(orig);
	}
	
	public Shape getShape(){
		return new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
	}
}
