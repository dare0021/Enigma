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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class CStaticImage extends ACItem{
	public double xScale, yScale;
	public BufferedImage image;
	private String address;
	
	public CStaticImage(CImageDef def, String name){
		super(name);
		x0 = def.x;
		y0 = def.y;
		setOpacity((float) def.opacity);
		setDepth(def.depth);
		setFile(def.address);
		if(def.width < 0 && def.height < 0){
			xScale = yScale = 1;
			return;
		}
		xScale = def.width / image.getWidth();
		yScale = def.height/ image.getHeight();
	}protected CStaticImage(CImageDef def, int currentFrame, String name){
		super(name);
		x0 = def.x;
		y0 = def.y;
		setOpacity((float) def.opacity);
		setDepth(def.depth);
		setFile(def.address + currentFrame + ".png");
		if(def.width < 0 && def.height < 0){
			xScale = yScale = 1;
			return;
		}
		xScale = def.width / image.getWidth();
		yScale = def.height/ image.getHeight();
	}
	
	protected void setFile(String url){
		try{
			image = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(url));
		}catch(Exception e){
			e.printStackTrace();
			try {
				image = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("dummy.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public double getRawWidth(){return image.getWidth();}
	public double getRawHeight(){return image.getHeight();}
	public double getEffectiveWidth(){return getRawWidth()*xScale;}
	public double getEffectiveHeight(){return getRawHeight()*yScale;}
	public double getEffectiveX1(){return x0 + getEffectiveWidth();}
	public double getEffectiveY1(){return y0 + getEffectiveHeight();}
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

	/**
	 * Moves the image as if its anchor is on its top left
	 * even if that is not true because scale < 0
	 */
	public void negativeScaleNormalize(){
			if(xScale < 0)
				moveRelative(-1*getEffectiveX1(), 0);
			if(yScale < 0)
				moveRelative(0, -1*getEffectiveY1());
	}
}
