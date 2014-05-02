package canvasItems;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class CRoundRect extends ACShape {
	double arcwidth, archeight;
	
	public CRoundRect(double xFrom, double yFrom, double width, double height, double rounding, String name){
		super(name);
		init(xFrom, yFrom, width, height, Color.WHITE, new Color(0,0,0,0), 1, rounding, rounding);
	}public CRoundRect(CRoundRect orig, String name){
		super(name);
		init(orig.x0, orig.y0, orig.x1-orig.x0, orig.y1-orig.y0, orig.stroke, orig.fill, orig.thickness, orig.arcwidth, orig.archeight);
	}
	
	private void init(double xFrom, double yFrom, double width, double height, Color strokecolor, Color fillcolor, double strokewidth, double roundwidth, double roundheight){
		setX(xFrom);
		setWidth(width);
		setY(yFrom);
		setHeight(height);
		setStroke(strokecolor);
		setFill(fillcolor);
		setThickness(strokewidth);
		setArcWidth(roundwidth);
		setArcHeight(roundheight);
		setOpacity(1);
	}
	
	public Shape getShape(){
		if(getHeight()<0 || getWidth()<0){
			System.out.println("WARN: height|width < 0");
			//new Exception().printStackTrace();
		}
		return new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
	}

	public double getArcWidth() {return arcwidth;}
	public double getArcHeight() {return archeight;}
	
	public void setArcWidth(double w) {arcwidth = w;}
	public void setArcHeight(double h) {archeight = h;}
}
