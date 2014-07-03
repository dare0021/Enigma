package canvasItems;
import java.awt.Color;
import java.awt.Font;

public class CText extends ACItem{
	public int len, offset;
	public String text;
	public Color color;
	public Font font;
	
	public CText(CTextDef def, String name){
		super(name);
		init(def.text, def.x, def.y, def.text.length(), 0, def.color, def.font);
		setOpacity(def.opacity);
		setDepth(def.depth);
	}
	
	private void init(String prompt, double x, double y, int length, int startat, Color fill, Font fnt){
		x0 = x;
		y0 = y;
		len = length;
		offset = startat;
		text = prompt;
		color = fill;
		font = fnt;
	}
	
	public Color getEffectiveColor(double groupOpacity) {return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha()*getOpacity()*groupOpacity));}
	public Color getEffectiveColor() {return getEffectiveColor(1);}
}
