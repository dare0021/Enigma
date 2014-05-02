package canvasItems;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 * X, Y, W, H, Stroke width are stored in Shape
 */
public class CLine extends ACShape{
	public CLine(double xFrom, double yFrom, double xTo, double yTo, String name){
		super(name);
		init(xFrom, yFrom, xTo, yTo, Color.WHITE, 1);
	}public CLine(CLine orig, String name){
		super(name);
		init(orig.x0, orig.y0, orig.x1, orig.y1, orig.stroke, orig.thickness);
	}
	
	private void init(double xFrom, double yFrom, double xTo, double yTo, Color fill, double stroke){
		setX(xFrom);
		setX1(xTo);
		setY(yFrom);
		setY1(yTo);
		setStroke(fill);
		setThickness(stroke);
		setOpacity(1);
	}
	
	public Shape getShape(){
		return new Line2D.Double(getX(), getY(), getX1(), getY1());
	}

	public Color getFill() {return getStroke();}
	public Color getRawFill() {return getRawStroke();}
	public void setFill(Color s) {setStroke(s);}
}
