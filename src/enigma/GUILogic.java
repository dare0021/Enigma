package enigma;

/**
 * The parent class for logic classes used with GUI classes
 * Handles more advanced GUI functions such as animations
 * Most actual logic processing is done in child classes
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.Timer;

import canvasItems.CAnimation;
import canvasItems.CFade;
import canvasItems.CTargetAgent;

public class GUILogic implements IConstantsUI, ActionListener{
	GUIcanvas gui;
	Timer timer;
	Vector<CAnimation> animations;
	Vector<CFade> fadeanims;
	
	public GUILogic(){
		animations = new Vector<CAnimation>();
		fadeanims = new Vector<CFade>();
		timer = new Timer(REFRESHRATE, this);
	}
	
	/**
	 * Moves the item by that much, relative to its current position
	 * length is time in ms
	 * Adding an item already being moved results in acceleration/decceleration
	 * use length=0 for instant move
	 */
	public void moveRelative(CTargetAgent def, double dx, double dy, int length){
		CAnimation anim = new CAnimation(def, dx, dy, length);
		animations.add(anim);
	}
	
	/**
	 * Moves the item from A to B
	 * Beware of bunching
	 */
	public void move(CTargetAgent def, double x0, double y0, double x1, double y1, int length){
		moveTo(def, x0, y0);
		moveRelative(def, x1-x0, y0-y1, length);
	}
	
	public void moveTo(CTargetAgent def, double x, double y){
		gui.moveTo(def, x, y);
	}
	
	public void fade(CTargetAgent def, double x){
		gui.opacChange(def, x);
	}
	
	public void fading(CTargetAgent def, double dx, int length){
		CFade anim = new CFade(def, dx, length);
		fadeanims.add(anim);
	}
	
	/**
	 * Creates a shaking motion with the item going in a random direction in a random amount.
	 * The motion always stops at the object's original point.
	 * jitterX and jitterY describes the maximum difference in each dx and dy element, with dx and dy being the minimum
	 * and dx+randomness and dy+randomness being the maximum
	 * length describes the number of shakes
	 * As is with the other animations, effect intensifies when overlapped.
	 */
	public void jitter(CTargetAgent def, double dx, double dy, double jitterX, double jitterY, int length){
		LinkedBlockingQueue<CAnimation> cont = new LinkedBlockingQueue<CAnimation>();
		int tx, ty; //total displacement
		int ix, iy; //displacement this shake
		CAnimation out = null;
		Random rng = new Random();
		tx = 0;
		ty = 0;
		ix = 0;
		iy = 0;
		for(int i=0; i<length; i++){
			ix = (int)(dx + jitterX*Math.random());
			iy = (int)(dy + jitterY*Math.random());
			if(rng.nextBoolean())
				ix *= -1;
			if(rng.nextBoolean())
				iy *= -1;
			tx += ix;
			ty += iy;
			cont.add(new CAnimation(def, ix, iy, 0));
		}
		cont.add(new CAnimation(def, -1*tx, -1*ty, 0));
		try {
			out = cont.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		out.continuation = cont;
		animations.add(out);
	}
	
	/**
	 * timer tick means repaint
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		{
			Vector<CAnimation> next = new Vector<CAnimation>();
			Iterator<CAnimation> i = animations.iterator();
			CAnimation iter;
			while(i.hasNext()){
				CAnimation anim = i.next();
				anim.ticks++;
				if(anim.ticks >= anim.target){
					gui.translate(anim.def, anim.dx+anim.fx, anim.dy+anim.fy);
					if((iter = anim.next()) != null)
						next.add(iter);
					i.remove(); //foreach loop does not support removal during loop
				}else{
					gui.translate(anim.def, anim.dx, anim.dy);
				}
			}
			for(CAnimation anim : next){
				animations.add(anim);
			}
		}
		{
			Vector<CFade> next = new Vector<CFade>();
			Iterator<CFade> i = fadeanims.iterator();
			CFade iter;
			while(i.hasNext()){
				CFade anim = i.next();
				anim.ticks++;
				if(anim.ticks >= anim.target){
					gui.opacChange(anim.def, anim.dx+anim.fx);
					if((iter = anim.next()) != null)
						next.add(iter);
					i.remove(); //foreach loop does not support removal during loop
				}else{
					gui.opacChange(anim.def, anim.dx);
				}
			}
			for(CFade anim : next){
				fadeanims.add(anim);
			}
		}
		
		gui.repaint(); //draws on top of everything else
		gui.requestFocusInWindow();
	}

}
