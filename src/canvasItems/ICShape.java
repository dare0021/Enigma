package canvasItems;

import java.awt.Color;
import java.awt.Shape;

/**
 * Common interface for shape objects used in this project
 * Color can be RGBA
 */
public interface ICShape extends ICItem {
	public void moveRelative(double dx, double dy);
	public void moveTo(double tx, double ty);
	
	public Shape getShape();
	
	public double getX1();
	public double getY1();
	public double getWidth();
	public double getHeight();
	public double getThickness();
	public Color getStroke(double groupOpacity);
	public Color getRawStroke();
	public Color getFill(double groupOpacity);
	public Color getRawFill();
	
	public void setX1(double x1);
	public void setY1(double y1);
	public void setWidth(double w);
	public void setHeight(double h);
	public void setThickness(double t);
	public void setStroke(Color s);
	public void setFill(Color s);
}
