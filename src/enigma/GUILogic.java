package enigma;

/**
 * The parent class for logic classes used with GUI classes
 * Handles more advanced GUI functions such as animations
 * Most actual logic processing is done in child classes
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.Timer;

import canvasItems.ACItem;
import canvasItems.CAnimation;
import canvasItems.CDialogBox;
import canvasItems.CFade;

public class GUILogic implements IConstantsUI, ActionListener, KeyListener{
	GUIcanvas gui;
	Timer timer;
	ArrayList<CAnimation> animations;
	ArrayList<CFade> fadeanims;
	
	public GUILogic(){
		animations = new ArrayList<CAnimation>();
		fadeanims = new ArrayList<CFade>();
		timer = new Timer(REFRESHRATE, this);
	}
	
	/**
	 * Moves the item by that much, relative to its current position
	 * length is time in ms
	 * Adding an item already being moved results in acceleration/decceleration
	 * use length=0 for instant move
	 */
	public void moveRelative(ACItem item, double dx, double dy, int length){
		CAnimation anim = new CAnimation(item, dx, dy, length);
		animations.add(anim);
	}
	
	public void fading(ACItem item, double dx, int length){
		CFade anim = new CFade(item, dx, length);
		fadeanims.add(anim);
	}
	
	/**
	 * Performs a fade where each dx is described using a curve 
	 * f(x)=1/(acc*x).
	 * length describes the time in ms
	 */
	public void accFading(ACItem item, double dx, int length, double accx){
		ConcurrentLinkedQueue<CFade> cont = new ConcurrentLinkedQueue<CFade>();
		CFade out = null;
		double ix, tx = 0;
		double cx = accx*dx/(Math.log(length+1));
		for (int t=1; t <= length/REFRESHRATE; t++){
			ix = cx/(accx*(t-0.5));
			tx += ix;
			cont.add(new CFade(item, ix, 1));
		}
		out = new CFade(item, dx-tx, 1);
		out.continuation = cont;
		fadeanims.add(out);
	}
	
	/**
	 * Performs a move where each dx dy are described using a curve 
	 * f(x)=1/(acc*x).
	 * length describes the time in ms
	 */
	public void accMove(ACItem item, double dx, double dy, int length, double accx, double accy){
		ConcurrentLinkedQueue<CAnimation> cont = new ConcurrentLinkedQueue<CAnimation>();
		CAnimation out = null;
		double ix, iy, tx = 0, ty = 0;
		double cx = accx*dx/(Math.log(length+1));
		double cy = accy*dy/(Math.log(length+1));
		for (int t=1; t <= length/REFRESHRATE; t++){
			ix = cx/(accx*(t-0.5));
			tx += ix;
			iy = cy/(accy*(t-0.5));
			ty += iy;
			cont.add(new CAnimation(item, ix, iy, 1));
		}
		out = new CAnimation(item, dx-tx, dy-ty, 1);
		out.continuation = cont;
		animations.add(out);
	}
	
	/**
	 * Creates a shaking motion with the item going in a random direction in a random amount.
	 * maxdx and maxdy describes the maximum difference in each dx and dy element, with mindx and mindy being the minimum
	 * and dx+randomness and dy+randomness being the maximum
	 * length describes the number of shakes, not the time in ms. There is one movement per tickrate.
	 * interval is the amount of time in ms that each interval takes
	 * As is with the other animations, effect intensifies when overlapped.
	 */
	public void destructiveJitter(ACItem item, double mindx, double mindy, double adddx, double adddy, int length, int interval){
		ConcurrentLinkedQueue<CAnimation> cont = new ConcurrentLinkedQueue<CAnimation>();
		int ix, iy; //displacement this shake
		CAnimation out = null;
		Random rng = new Random();
		ix = 0;
		iy = 0;
		for(int i=0; i<length; i++){
			ix = (int)(mindx + adddx*Math.random());
			iy = (int)(mindy + adddy*Math.random());
			if(rng.nextBoolean())
				ix *= -1;
			if(rng.nextBoolean())
				iy *= -1;
			cont.add(new CAnimation(item, ix, iy, interval));
		}
		out = cont.poll();
		out.continuation = cont;
		animations.add(out);
	}
	
	/**
	 * Non-destructive jitter
	 */
	public void jitter(ACItem item, double mindx, double mindy, double adddx, double adddy, int length, int interval){
		ConcurrentLinkedQueue<CAnimation> cont = new ConcurrentLinkedQueue<CAnimation>();
		int tx, ty; //total displacement
		int ix, iy; //displacement this shake
		CAnimation out = null;
		Random rng = new Random();
		tx = 0;
		ty = 0;
		ix = 0;
		iy = 0;
		for(int i=0; i<length/2; i++){
			ix = (int)(mindx + adddx*Math.random());
			iy = (int)(mindy + adddy*Math.random());
			if(rng.nextBoolean())
				ix *= -1;
			if(rng.nextBoolean())
				iy *= -1;
			cont.add(new CAnimation(item, ix, iy, interval));
		}
		ConcurrentLinkedQueue<CAnimation> b = new ConcurrentLinkedQueue<CAnimation>();
		for(CAnimation anim : cont){
			b.add(new CAnimation(item, -1*anim.dx_left(), -1*anim.dy_left(), interval));
		}
		cont.addAll(b);
		out = cont.poll();
		out.continuation = cont;
		animations.add(out);
	}
	
	/**
	 * timer tick means repaint
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		{
			ArrayList<CAnimation> next = new ArrayList<CAnimation>();
			Iterator<CAnimation> i = animations.iterator();
			CAnimation iter;
			while(i.hasNext()){
				CAnimation anim = i.next();
				anim.ticks++;
				gui.getMasterNode().findChild(anim.item).moveRelative(anim.dx, anim.dy);
				if(anim.ticks >= anim.target){
					if((iter = anim.next()) != null)
						next.add(iter);
					i.remove(); //foreach loop does not support removal during loop
				}
			}
			for(CAnimation anim : next){
				animations.add(anim);
			}
		}
		{
			ArrayList<CFade> next = new ArrayList<CFade>();
			Iterator<CFade> i = fadeanims.iterator();
			CFade iter;
			while(i.hasNext()){
				CFade anim = i.next();
				anim.ticks++;
				if(anim.ticks >= anim.target){
					ACItem target = gui.getMasterNode().findChild(anim.item);
					if(target == null){
						continue;
					}
					target.opacChange(anim.dx+anim.fx);
					if((iter = anim.next()) != null)
						next.add(iter);
					i.remove(); //foreach loop does not support removal during loop
				}else{
					gui.getMasterNode().findChild(anim.item).opacChange(anim.dx);
				}
			}
			for(CFade anim : next){
				fadeanims.add(anim);
			}
		}
		
		gui.repaint(); //draws on top of everything else
		gui.requestFocusInWindow();
	}
	
	public void dialogEvent(CDialogBox e) {}

	public void clickEvent(MouseEvent e, String actionCommand) {}
	
	public void dragEvent(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
