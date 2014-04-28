package canvasItems;

import java.util.concurrent.LinkedBlockingQueue;

import enigma.IConstantsUI;

/**
 * Contains the instructions for a fading animation
 */
public class CFade implements IConstantsUI{
	public String item;
	public double dx, fx;
	public int ticks, target;
	public LinkedBlockingQueue<CFade> continuation;
	
	/**
	 * length is time in ms
	 */
	public CFade(String name, double _dx, int length){
		init(name, _dx, length, null);
	}public CFade(String name, double _dx, int length, CFade cont){
		LinkedBlockingQueue<CFade> q = new LinkedBlockingQueue<CFade>();
		q.add(cont);
		init(name, _dx, length, q);
	}public CFade(String name, double _dx, int length, LinkedBlockingQueue<CFade> cont){
		init(name, _dx, length, cont);
	}
	
	private void init(String name, double _dx, int length, LinkedBlockingQueue<CFade> cont){
		item = name;
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
