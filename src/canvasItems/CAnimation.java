package canvasItems;

import java.util.concurrent.LinkedBlockingQueue;

import enigma.IConstantsUI;

/**
 * Contains the instructions for an animation
 */
public class CAnimation implements IConstantsUI{
	public String item;
	public double dx, dy, fx, fy;
	public int ticks, target;
	public LinkedBlockingQueue<CAnimation> continuation;
	
	/**
	 * length is time in ms
	 */
	public CAnimation(String name, double _dx, double _dy, int length){
		init(name, _dx, _dy, length, null);
	}public CAnimation(String name, double _dx, double _dy, int length, CAnimation cont){
		LinkedBlockingQueue<CAnimation> q = new LinkedBlockingQueue<CAnimation>();
		q.add(cont);
		init(name, _dx, _dy, length, q);
	}public CAnimation(String name, double _dx, double _dy, int length, LinkedBlockingQueue<CAnimation> cont){
		init(name, _dx, _dy, length, cont);
	}
	
	private void init(String name, double _dx, double _dy, int length, LinkedBlockingQueue<CAnimation> cont){
		item = name;
		ticks = 0;
		target = length / REFRESHRATE;
		if(target<1)target=1;
		dx = _dx / target;
		dy = _dy / target;
		double fudge = ((double)length/REFRESHRATE) - target;
		if(target==1)return;
		fx = dx * fudge;
		fy = dy * fudge;
		continuation = cont;
	}
	
	public CAnimation next(){
		if(continuation == null){
			return null;
		}
		CAnimation out = null;
		try {
			if(continuation.peek() == null)
				return null;
			out = continuation.take();
			out.continuation = continuation;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return out;
	}
}
