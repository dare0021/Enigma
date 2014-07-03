package canvasItems;

public class CButtonDef {
	public double x, y, width, height, opacity, depth;
	public CStaticImage bgimage, hoverimage, clickimage;
	
	public CButtonDef(double xpos, double ypos, double w, double h, double o, double z,
						CStaticImage bg, CStaticImage hover, CStaticImage click) {
		x = xpos;
		y = ypos;
		width = w;
		height = h;
		opacity = o;
		depth = z;
		bgimage = bg;
		hoverimage = hover;
		clickimage = click;
	}
}
