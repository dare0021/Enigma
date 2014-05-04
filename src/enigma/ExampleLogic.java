package enigma;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.sound.sampled.LineUnavailableException;

import canvasItems.CTargetAgent;
import jld.JLD;
import jld.JldGetter;
import jld.JldSetter;
import sound.BigClip;
import sound.SClip;

public class ExampleLogic extends GUILogic implements KeyListener{
	public ExampleLogic(ExampleGUI shipbattlegui){
		gui = shipbattlegui;
		gui.linkLogic(this);
		gui.addKeyListener(this);
		timer.start();
		
		test();
	}
	
	private void test(){
		//GUI test
		gui.drawTestGrid();
		//JLD import test
		JldGetter jg = new JldGetter();
		JLD jld = jg.getValues("example.jld");
		Vector v = (Vector) jld.get("key3");
		v = (Vector) v.get(0);
		System.out.print("JLD IO test: ");
		System.out.println(jld);
		//JLD export test
		JldSetter js = new JldSetter();
		js.setValues("ExampleOutput"+System.currentTimeMillis()+".jld", jld);
		jld.clear();
		jld.put("a", "1");
		v.clear();
		v.add("20");
		v.add("21");
		v.add("22");
		jld.put("b", v);
		HashMap<String, String> hme = new HashMap<String, String>();
		hme.put("c1", "30");
		hme.put("c2", "31");
		jld.put("c", hme);
		HashMap<String, Vector<String>> hmv = new HashMap<String, Vector<String>>();
		Vector<String> v1 = new Vector<String>();
		v1.add("40");
		v1.add("41");
		Vector<String> v2 = new Vector<String>();
		v2.add("45");
		v2.add("46");
		hmv.put("d1", v1);
		hmv.put("d2", v2);
		jld.put("d", hmv);
		Vector<Object> vv = new Vector<Object>();
		Vector<String> vi = new Vector<String>();
		vi.add("51");
		vi.add("52");
		vv.add(vi);
		vv.add(new Integer(55));
		jld.put("e", vv);
		System.out.println("jld: "+jld);
		System.out.println("find: "+jld.find("d.d1:1")); //41
	}

	public void clickEvent(MouseEvent e, String actionCommand) {
		CTargetAgent def = new CTargetAgent("", "testgrid", GROUP);
		if(actionCommand == null)
			return;
		switch (actionCommand){
		case "GridTest Action":
			//jitter(def, 20, 5, 20, 0, 20);
			moveRelative(new CTargetAgent("", "testgrid", GROUP), 100, 0, 1000);
			break;
		case "GridTest Action Fade":
			if(gui.retrieveItem(def).getOpacity() > 0)
				fading(def, -1, 1000);
			else
				fading(def, 1, 1000);
			break;
		}
	}

	public void dragEvent(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		BigClip bc = new BigClip("testblip.wav");
		bc.start();
	}
	
}
