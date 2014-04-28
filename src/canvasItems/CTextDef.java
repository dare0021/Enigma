package canvasItems;

import java.awt.Color;
import java.awt.Font;

public class CTextDef {
	public double x, y;
	public String text;
	public Color color;
	public Font font;
	
	public CTextDef(String msg, double x0, double y0, Color fillColor, Font fnt){
		x = x0;
		y = y0;
		color = fillColor;
		font = fnt;
		text = msg;
	}
}
