package canvasItems;

import java.util.concurrent.ConcurrentLinkedQueue;

import enigma.IConstantsUI;

/**
 * Contains the instructions for a fading animation
 */
public class CFade implements IConstantsUI{
	public ACItem item;
	public double dx, fx;
	public int ticks, target;
	public ConcurrentLinkedQueue<CFade> continuation;
	
	/**
	 * length is time in ms
	 */
	public CFade(ACItem item, double _dx, int length){
		init(item, _dx, length, null);
	}public CFade(ACItem item, double _dx, int length, CFade cont){
		ConcurrentLinkedQueue<CFade> q = new ConcurrentLinkedQueue<CFade>();
		q.add(cont);
		init(item, _dx, length, q);
	}public CFade(ACItem item, double _dx, int length, ConcurrentLinkedQueue<CFade> cont){
		init(item, _dx, length, cont);
	}
	
	private void init(ACItem _item, double _dx, int length, ConcurrentLinkedQueue<CFade> cont){
		item = _item;
		ticks = 0;
		target = length / REFRESHRATE;
		if(target<1)target=1;
		dx = _dx / target;
		double fudge = ((double)length/REFRESHRATE) - target;
		if(target==1)return;
		fx = dx * fudge;
		continuation = cont;
	}
	
	public CFade next(){
		if(continuation == null){
			return null;
		}
		CFade out = null;
		if(continuation.peek() == null)
			return null;
		out = continuation.poll();
		out.continuation = continuation;
		return out;
	}
}
