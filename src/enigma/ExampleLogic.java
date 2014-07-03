package enigma;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.ArrayList;

import canvasItems.CGroupNode;
import jld.JLD;
import jld.JldGetter;
import jld.JldSetter;
import sound.BigClip;

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
		GUITest test = new GUITest(gui);
		test.drawGridTest();
		//JLD import test
		JldGetter jg = new JldGetter();
		JLD jld = jg.getValues("static/example.jld");
		ArrayList v = (ArrayList) jld.get("key3");
		v = (ArrayList) v.get(0);
		System.out.print("JLD IO test: ");
		System.out.println(jld);
		//JLD export test
		JldSetter js = new JldSetter();
		js.setValues("temp/ExampleOutput"+System.currentTimeMillis()+".jld", jld);
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
		HashMap<String, ArrayList<String>> hmv = new HashMap<String, ArrayList<String>>();
		ArrayList<String> v1 = new ArrayList<String>();
		v1.add("40");
		v1.add("41");
		ArrayList<String> v2 = new ArrayList<String>();
		v2.add("45");
		v2.add("46");
		hmv.put("d1", v1);
		hmv.put("d2", v2);
		jld.put("d", hmv);
		ArrayList<Object> vv = new ArrayList<Object>();
		ArrayList<String> vi = new ArrayList<String>();
		vi.add("51");
		vi.add("52");
		vv.add(vi);
		vv.add(new Integer(55));
		jld.put("e", vv);
		System.out.println("jld: "+jld);
		System.out.println("find: "+jld.find("d.d1:1")); //41
	}

	public void clickEvent(MouseEvent e, String actionCommand) {
		if(actionCommand == null)
			return;
		CGroupNode testgrid = (CGroupNode) gui.getMasterNode().findChild("testgrid");
		switch (actionCommand){
		case "GridTest Action":
			//destructiveJitter(testgrid, 20, 5, 20, 0, 20, 50);
			jitter(testgrid, 20, 5, 20, 0, 20, 50);
			//moveRelative(testgrid, 100, 0, 1000);
			//accMove(testgrid, 100, 0, 1000, 1, 1);
			break;
		case "GridTest Action Fade":
			int dopac;
			if(testgrid.getOpacity() > 0)
				dopac = -1;
			else
				dopac = 1;
			//fading(testgrid, dopac, 1000);
			accFading(testgrid, dopac, 1000, 1);
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
