package canvasItems;

import java.awt.Color;
import java.awt.Font;

public class CTextBoxDef {
	public double x, y, width, height, opacity;
	public CStaticImage bgimage, hoverimage;
	public int size;
	public Font font;
	public Color color;
	
	public CTextBoxDef(double xpos, double ypos, double w, double h, double o,
						CStaticImage bg, CStaticImage hover, Font _font, Color textColor, int length) {
		x = xpos;
		y = ypos;
		width = w;
		height = h;
		opacity = o;
		bgimage = bg;
		hoverimage = hover;
		font = _font;
		color = textColor;
		size = length; 
	}
}
