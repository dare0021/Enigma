package canvasItems;

import java.awt.Color;

public abstract class ACShape extends ACItem implements ICShape {
	protected double x1, y1, thickness;
	Color fill, stroke;

	protected ACShape(String name) {
		super(name);
	}
	
	private Color getEffective(Color color, double groupOpacity){
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha()*getOpacity()*groupOpacity));
	}

	public double getX1() {return x1;}
	public double getY1() {return y1;}
	public double getWidth() {return x1-x0;}
	public double getHeight() {return y1-y0;}
	public double getThickness() {return thickness;}
	public Color getRawStroke() {return stroke;}
	public Color getStroke(double groupOpacity) {return getEffective(getRawStroke(), groupOpacity);}
	public Color getRawFill() {return fill;}
	public Color getFill(double groupOpacity) {return getEffective(getRawFill(), groupOpacity);}

	public void setX0(double x) {x0 = x;}
	public void setY0(double y) {y0 = y;}
	public void setX1(double x) {x1 = x;}
	public void setY1(double y) {y1 = y;}
	public void setWidth(double w) {x1 = x0 + w;}
	public void setHeight(double h) {y1 = y0 + h;}
	public void setThickness(double t) {thickness = t;}
	public void setStroke(Color color) {stroke = color;}
	public void setFill(Color color) {fill = color;}
	
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
