package canvasItems;

import java.util.concurrent.ConcurrentLinkedQueue;

import enigma.IConstantsUI;

/**
 * Contains the instructions for an animation
 */
public class CAnimation implements IConstantsUI{
	public ACItem item;
	public double dx, dy;
	public int ticks, target;
	public ConcurrentLinkedQueue<CAnimation> continuation;
	
	/**
	 * length is time in ms
	 */
	public CAnimation(ACItem item, double _dx, double _dy, int length){
		init(item, _dx, _dy, length, null);
	}public CAnimation(ACItem item, double _dx, double _dy, int length, CAnimation cont){
		ConcurrentLinkedQueue<CAnimation> q = new ConcurrentLinkedQueue<CAnimation>();
		q.add(cont);
		init(item, _dx, _dy, length, q);
	}public CAnimation(ACItem item, double _dx, double _dy, int length, ConcurrentLinkedQueue<CAnimation> cont){
		init(item, _dx, _dy, length, cont);
	}
	
	private void init(ACItem _item, double _dx, double _dy, int length, ConcurrentLinkedQueue<CAnimation> cont){
		item = _item;
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
		if(continuation.peek() == null)
			return null;
		out = continuation.poll();
		out.continuation = continuation;
		return out;
	}
	
	/**
	 * Returns the amount of movement to be done
	 */
	public double dx_left(){
		return dx*(target-ticks);
	}
	public double dy_left(){
		return dy*(target-ticks);
	}
}
