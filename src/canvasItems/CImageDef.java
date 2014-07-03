package canvasItems;

public class CImageDef {
	public double x, y, width, height, opacity, depth;
	public String address;

	public CImageDef(String url, double xpos, double ypos, double w, double h, double o, double z){
		address = url;
		x = xpos;
		y = ypos;
		width = w;
		height = h;
		opacity = o;
		depth = z;
	}
}
