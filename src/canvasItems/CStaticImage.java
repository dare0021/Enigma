package canvasItems;

/**
 * Opacity only works on files with alpha channels
 * Opacity is from 0 to 1, 1 being fully opaque.
 * 
 * width and height are the original scales.
 * Runtime scaling is done by manipulating xScale and yScale. 
 * WIDTH AND HEIGHT WILL NOT STRETCH OR COMPRESS THE IMAGE
 * they will, however, clip the image
 */

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class CStaticImage{
	public double x, y, width, height, xScale, yScale;
	private float opacity;
	public Image image;
	private String address;
	
	public CStaticImage(CImageDef def){
		x = def.x;
		y = def.y;
		width = def.width;
		height = def.height;
		opacity = def.opacity;
		xScale = 1;
		yScale = 1;
		setFile(def.address);
	}protected CStaticImage(CImageDef def, int currentFrame){
		x = def.x;
		y = def.y;
		width = def.width;
		height = def.height;
		opacity = def.opacity;
		xScale = 1;
		yScale = 1;
		setFile(def.address + currentFrame + ".png");
	}
	
	protected void setFile(String url){
		File f = null;
		String errprompt = url;
		try {
			f = new File(getClass().getResource("/enigma/images/"+url).toURI());
			address = url;
		}catch (Exception e){
			e.printStackTrace();
			try {
				f = new File(getClass().getResource("/enigma/images/dummy.png").toURI());
				xScale = width/100;
				yScale = height/100;
				width = 100;
				height = 100;
				address = null;
				System.out.println("No such file: "+errprompt);
				System.out.println("ERR handled by inserting dummy image");
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println("Can't create stream for: "+errprompt);
			e.printStackTrace();
		}
	}
	
	public double getEffectiveX1(){return x + width*xScale;}
	public double getEffectiveY1(){return y + height*yScale;}
	public String getAddress(){return address;}
	public float getOpacity(){return opacity;}
	public double getXscale(){return xScale;}
	public double getYscale(){return yScale;}
	
	/**
	 * Resets the image with a new file
	 * The old image is removed automatically
	 */
	public void setAddress(String url){setFile(url);}
	public void setOpacity(double o) {
		if(o<0)
			o = 0;
		else if(o>1)
			o = 1;
		opacity = (float)o;
	}
	public void setXscale(double xs){xScale = xs;}
	public void setYscale(double ys){yScale = ys;}
}
