package canvasItems;

/**
 * Opacity only works on files with alpha channels
 * Opacity is from 0 to 1, 1 being fully opaque.
 */

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class CStaticImage{
	public double x, y, width, height;
	private float opacity;
	public Image image;
	private String address;
	
	public CStaticImage(CImageDef def){
		x = def.x;
		y = def.y;
		width = def.width;
		height = def.height;
		opacity = def.opacity;
		setFile(def.address);
	}protected CStaticImage(CImageDef def, int currentFrame){
		x = def.x;
		y = def.y;
		width = def.width;
		height = def.height;
		opacity = def.opacity;
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
	
	public String getAddress(){return address;}
	public float getOpacity(){return opacity;}
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
}
