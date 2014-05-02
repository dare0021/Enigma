package canvasItems;

public class CImageDef {
	public double x, y, width, height, opacity;
	public String address;

	public CImageDef(String url, double xpos, double ypos, double w, double h, double o){
		address = url;
		x = xpos;
		y = ypos;
		width = w;
		height = h;
		opacity = o;
	}
}
