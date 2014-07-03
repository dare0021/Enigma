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

public class CStaticImage extends ACItem{
	public double width, height, xScale, yScale;
	public Image image;
	private String address;
	
	public CStaticImage(CImageDef def, String name){
		super(name);
		x0 = def.x;
		y0 = def.y;
		width = def.width;
		height = def.height;
		xScale = 1;
		yScale = 1;
		setOpacity((float) def.opacity);
		setDepth(def.depth);
		setFile(def.address);
	}protected CStaticImage(CImageDef def, int currentFrame, String name){
		super(name);
		x0 = def.x;
		y0 = def.y;
		width = def.width;
		height = def.height;
		xScale = 1;
		yScale = 1;
		setOpacity((float) def.opacity);
		setDepth(def.depth);
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
	
	public double getEffectiveX1(){return x0 + width*xScale;}
	public double getEffectiveY1(){return y0 + height*yScale;}
	public String getAddress(){return address;}
	public double getXscale(){return xScale;}
	public double getYscale(){return yScale;}
	
	/**
	 * Resets the image with a new file
	 * The old image is removed automatically
	 */
	public void setAddress(String url){setFile(url);}
	public void setXscale(double xs){xScale = xs;}
	public void setYscale(double ys){yScale = ys;}
}
