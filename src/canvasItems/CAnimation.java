package canvasItems;

import java.util.concurrent.LinkedBlockingQueue;

import enigma.IConstantsUI;

/**
 * Contains the instructions for an animation
 */
public class CAnimation implements IConstantsUI{
	public CTargetAgent def;
	public double dx, dy, fx, fy;
	public int ticks, target;
	public LinkedBlockingQueue<CAnimation> continuation;
	
	/**
	 * length is time in ms
	 */
	public CAnimation(CTargetAgent def, double _dx, double _dy, int length){
		init(def, _dx, _dy, length, null);
	}public CAnimation(CTargetAgent def, double _dx, double _dy, int length, CAnimation cont){
		LinkedBlockingQueue<CAnimation> q = new LinkedBlockingQueue<CAnimation>();
		q.add(cont);
		init(def, _dx, _dy, length, q);
	}public CAnimation(CTargetAgent def, double _dx, double _dy, int length, LinkedBlockingQueue<CAnimation> cont){
		init(def, _dx, _dy, length, cont);
	}
	
	private void init(CTargetAgent _def, double _dx, double _dy, int length, LinkedBlockingQueue<CAnimation> cont){
		def = _def;
		ticks = 0;
		target = length / REFRESHRATE;
		if(target<1)target=1;
		dx = _dx / target;
		dy = _dy / target;
		continuation = cont;
		if(target==1)return;
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
