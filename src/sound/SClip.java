package sound;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Only for short clips
 * No support for large files or simultaneous output
 * Sound is done in the Logic thread or else the GUI cannot update while the sound is playing
 * Simultaneous output is possible
 */
public class SClip {
	private Clip clip;
	
	public SClip(String url){
		setFile(url);
	}
	
	protected void setFile(String url){
		File f = null;
		String errprompt = url;
		try {
			f = new File(getClass().getResource("/enigma/sounds/"+url).toURI());
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("No such file: "+errprompt);
		}
		try{
			AudioInputStream ais = AudioSystem.getAudioInputStream(f);
			clip = AudioSystem.getClip();
			clip.open(ais);
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("Cannot play file: "+errprompt);
		}
	}
	
	public Clip getClip(){return clip;}
	
	public void restart(){
		this.reset();
		this.run();
	}
	
	public void start(){
		this.run();
	}public void run(){
		clip.start();
	}
	
	/**
	 * @param count Use -1 for infinite loop
	 */
	public void loop(int count){
		if(count == -1)
			count = Clip.LOOP_CONTINUOUSLY;
		clip.loop(count);
	}
	
	public void pause(){
		if(clip.isRunning())
			clip.stop();
	}
	
	public void rewind(){
		clip.setFramePosition(0);
	}
	
	public void reset(){
		this.rewind();
		this.pause();
	}
}
