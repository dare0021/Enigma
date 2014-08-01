package canvasItems;
import java.awt.Color;
import java.awt.Font;

public class CText extends ACItem{
	public int len, offset;
	public double strokeThickness;
	public String text;
	public Color fillColor, strokeColor;
	public Font font;
	
	public CText(CTextDef def, String name){
		super(name);
		init(def.text, def.x, def.y, def.text.length(), 0, def.fillColor, def.strokeColor, def.strokeThickness, def.font);
		setOpacity(def.opacity);
		setDepth(def.depth);
	}
	
	private void init(String prompt, double x, double y, int length, int startat, Color fill, Color stroke, double thickness, Font fnt){
		x0 = x;
		y0 = y;
		len = length;
		offset = startat;
		text = prompt;
		fillColor = fill;
		strokeColor = stroke;
		strokeThickness = thickness;
		font = fnt;
	}
	
	public Color getEffectiveFill(double groupOpacity) {return new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)(fillColor.getAlpha()*getOpacity()*groupOpacity));}
	public Color getEffectiveFill() {return getEffectiveFill(1);}
	public Color getEffectiveStroke(double groupOpacity) {return new Color(strokeColor.getRed(), strokeColor.getGreen(), strokeColor.getBlue(), (int)(strokeColor.getAlpha()*getOpacity()*groupOpacity));}
	public Color getEffectiveStroke() {return getEffectiveStroke(1);}
}
