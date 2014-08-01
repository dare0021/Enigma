package canvasItems;

import java.awt.Shape;

/**
 * Container for any arbitrary Java Graphics.Shape
 */
public class CShape extends ACShape {
	public Shape shape;
	
	public CShape(String name) {
		super(name);
	}

	@Override
	public Shape getShape() {
		return shape;
	}
}
