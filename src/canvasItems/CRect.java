package canvasItems;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class CRect extends ACShape {	
	public CRect(double xFrom, double yFrom, double width, double height, String name){
		super(name);
		init(xFrom, yFrom, width, height, Color.WHITE, new Color(0,0,0,0), 1);
	}public CRect(CRect orig, String name){
		super(name);
		init(orig.x0, orig.y0, orig.x1-orig.x0, orig.y1-orig.y0, orig.stroke, orig.fill, orig.thickness);
	}
	
	protected void init(double xFrom, double yFrom, double width, double height, Color strokecolor, Color fillcolor, double strokewidth){
		setX(xFrom);
		setWidth(width);
		setY(yFrom);
		setHeight(height);
		setStroke(strokecolor);
		setFill(fillcolor);
		setThickness(strokewidth);
	}
	
	public Shape getShape(){
		if(getHeight()<0 || getWidth()<0){
			System.out.println("WARN: height||width < 0");
			//new Exception().printStackTrace();
		}
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}
}
