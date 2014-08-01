package canvasItems;

import java.awt.Color;
import java.awt.Font;

public class CTextBoxDef {
	public double x, y, width, height, opacity, depth, strokeThickness;
	public CStaticImage bgimage, hoverimage;
	public int size;
	public Font font;
	public Color textColor, textStroke;
	
	public CTextBoxDef(double xpos, double ypos, double w, double h, double o, double z,
						CStaticImage bg, CStaticImage hover, Font _font, 
						Color _textColor, Color strokeColor, double thickness, int length) {
		x = xpos;
		y = ypos;
		width = w;
		height = h;
		opacity = o;
		depth = z;
		bgimage = bg;
		hoverimage = hover;
		font = _font;
		textColor = _textColor;
		textStroke = strokeColor;
		strokeThickness = thickness;
		size = length; 
	}
}
