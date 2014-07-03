package canvasItems;

/**
 * A common interface for all canvas items
 * except for the animation information containers
 */
public interface ICItem {
	public String getName();
	public CGroupNode getParent();
	public double getX();
	public double getY();
	public ECItemType getType();
	public double getOpacity();
	
	public void setX(double x);
	public void setY(double y);
	public void setOpacity(double o);

	public void moveRelative(double dx, double dy);
	public void moveTo(double tx, double ty);
}
