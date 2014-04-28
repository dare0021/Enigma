package canvasItems;

import java.awt.Color;

public abstract class ACShape implements ICShape {
	double x0, x1, y0, y1, thickness;
	Color fill, stroke;
	private float opacity;
	
	public void moveRelative(double dx, double dy){
		setX(getX()+dx);
		setX1(getX1()+dx);
		setY(getY()+dy);
		setY1(getY1()+dy);
	}
	public void moveTo(double tx, double ty){
		double w = getWidth();
		double h = getHeight();
		setX(tx);
		setWidth(w);
		setY(ty);
		setHeight(h);
	}
	private Color getEffective(Color color){
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha()*getOpacity()));
	}

	public double getX() {return x0;}
	public double getY() {return y0;}
	public double getX1() {return x1;}
	public double getY1() {return y1;}
	public double getWidth() {return x1-x0;}
	public double getHeight() {return y1-y0;}
	public double getThickness() {return thickness;}
	public Color getRawStroke() {return stroke;}
	public Color getStroke() {return getEffective(getRawStroke());}
	public Color getRawFill() {return fill;}
	public Color getFill() {return getEffective(getRawFill());}
	public float getOpacity() {return opacity;}

	public void setX(double x) {x0 = x;}
	public void setY(double y) {y0 = y;}
	public void setX1(double x) {x1 = x;}
	public void setY1(double y) {y1 = y;}
	public void setWidth(double w) {x1 = x0 + w;}
	public void setHeight(double h) {y1 = y0 + h;}
	public void setThickness(double t) {thickness = t;}
	public void setStroke(Color color) {stroke = color;}
	public void setFill(Color color) {fill = color;}
	public void setOpacity(double o) {
		if(o<0)
			o = 0;
		else if(o>1)
			o = 1;
		opacity = (float)o;
	}
}
