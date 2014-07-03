package canvasItems;

import java.awt.Color;
import java.awt.Font;

public class CTextDef {
	public double x, y, opacity, depth;
	public String text;
	public Color color;
	public Font font;
	
	public CTextDef(String msg, double x0, double y0, Color fillColor, Font fnt, double o, double z){
		x = x0;
		y = y0;
		color = fillColor;
		font = fnt;
		text = msg;
		opacity = o;
		depth = z;
	}
}
