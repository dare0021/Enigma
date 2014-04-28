package canvasItems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * A GIF-like animation class
 * Can be used for static images, but really shouldn't be used for that
 * Uses url+frame#+.png
 * CStaticImage supports any file format
 * 
 * Animation is from start to end, inclusive
 * FrameTotal is cardinal (starts at 1) while all others are from 0
 */
public class CJifImage extends CStaticImage implements ActionListener{
	private double framesPerSecond;
	private int framesTotal, frameStart, frameCurrent, frameEnd;
	private Timer timer;
	private String addressroot;
	
	public CJifImage(CImageDef def, double fps, int n){
		super(def, 0);
		init(def.address, fps, n, 0, 0, n-1);
	}public CJifImage(CImageDef def,  double fps, int n, int start, int current, int end){
		super(def, current);
		init(def.address, fps, n, start, current, end);
	}private void init(String url, double fps, int n, int start, int current, int end){
		if(start > end){
			int temp = end;
			end = start;
			start = temp;
			printError("start > end", "swapping start with end");
		}if(start < 0){
			start = 0;
			printError("start < 0", "setting start to 0");
		}if(end >= n){
			end = n-1;
			printError("end >= total", "setting end to total-1");
		}
		addressroot = url;
		framesPerSecond = fps;
		framesTotal = n;
		frameStart = start;
		frameCurrent = current;
		frameEnd = end;
		timer = new Timer((int)(1000/fps), this);
	}private void printError(String what, String autocorrect){
		new Exception().printStackTrace();
		System.out.println("ERR: " + what);
		System.out.println("ERR handled by: " + autocorrect);
	}

	public double getFPS(){return framesPerSecond;}
	public int getNumFrames(){return framesTotal;}
	public int getStartFrame(){return frameStart;}
	public int getCurrentFrame(){return frameCurrent;}
	public int getEndFrame(){return frameEnd;}
	
	public void setFPS(double fps){
		framesPerSecond = fps;
	}
	public void setCurrentFrame(int current){
		if(current < frameStart){
			frameCurrent = frameStart;
			printError("current < start", "setting current to start");
		}
		if(current > frameEnd){
			frameCurrent = frameEnd;
			printError("current > end", "setting current to end");
		}
		frameCurrent = current;
	}
	public void setStartFrame(int start){
		if(start < 0){
			frameStart = 0;
			printError("start < 0", "setting start to 0");
		}
		if(start > frameEnd){
			frameStart = frameEnd;
			printError("start > end", "setting start to end");
		}
		frameStart = start;
	}
	public void setEndFrame(int end){
		if(end < frameStart){
			frameEnd = frameStart;
			printError("end < start", "setting end to start");
		}
		if(end >= framesTotal){
			frameEnd = framesTotal-1;
			printError("end >= total", "setting end to total-1");
		}
		frameEnd = end;
	}
	@Override
	/**
	 * Provided so behavior is as expected when used on an animated image
	 */
	public void setAddress(String url){
		setFile(url + "0.png"); 
		init(url, 0, 1, 0, 0, 0);
	}public void setAddress(String url, double fps, int n){
		setFile(url + "0.png");
		init(url, fps, n, 0, 0, n-1);
	}public void setAddress(String url, double fps, int n, int start, int current, int end){
		setFile(url + current + ".png");		
		init(url, fps, n, start, current, end);
	}
	
	public void start(){timer.start();}
	public void stop(){timer.stop();}

	public void actionPerformed(ActionEvent e) {
		super.setAddress(addressroot + frameCurrent + ".png");
		frameCurrent++;
		if(frameCurrent > frameEnd)
			frameCurrent = frameStart;
	}
}
