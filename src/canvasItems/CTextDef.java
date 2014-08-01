package canvasItems;

import java.awt.Color;
import java.awt.Font;

public class CTextDef {
	public double x, y, opacity, depth, strokeThickness;
	public String text;
	public Color fillColor, strokeColor;
	public Font font;
	
	public CTextDef(String msg, double x0, double y0, Color fill, Color stroke, double thickness, Font fnt, double o, double z){
		x = x0;
		y = y0;
		fillColor = fill;
		strokeColor = stroke;
		strokeThickness = thickness;
		font = fnt;
		text = msg;
		opacity = o;
		depth = z;
	}
}
