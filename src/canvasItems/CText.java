package canvasItems;
import java.awt.Color;
import java.awt.Font;

public class CText {
	public double x, y;
	public int len, offset;
	public String text;
	public Color color;
	public Font font;
	private float opacity;
	
	public CText(CTextDef def){
		init(def.text, def.x, def.y, def.text.length(), 0, def.color, def.font);
	}
	
	private void init(String prompt, double x0, double y0, int length, int startat, Color fill, Font fnt){
		x = x0;
		y = y0;
		len = length;
		offset = startat;
		text = prompt;
		color = fill;
		font = fnt;
		opacity = 1;
	}
	
	public Color getEffectiveColor() {return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha()*opacity));}
	public float getOpacity() {return opacity;}
	public void setOpacity(double o) {
		if(o<0)
			o = 0;
		else if(o>1)
			o = 1;
		opacity = (float)o;
	}
}
