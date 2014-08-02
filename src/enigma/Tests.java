package enigma;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import sound.BigClip;
import jld.JLD;
import jld.JldGetter;
import jld.JldSetter;
import canvasItems.*;
import canvasItems.CJifImage.RoundBehavior;

public class Tests implements IConstantsUI {
	public static class gridTest implements ITest {
		private final GUILogic parent;
		public gridTest(GUILogic caller){parent = caller;}
		
		public Object run(){
			final String dir = "GUI Test/"; 
			CanvasFactory cf = new CanvasFactory();
			ArrayList<ACItem> items = new ArrayList<ACItem>();
			
			cf.fillColor = Color.WHITE;
			cf.font = new Font("Ariel", Font.PLAIN, 12);
			for(int i=0; i<APPWIDTH; i+=50){
				items.add(new CLine(i, -1*APPHEIGHT, i, APPHEIGHT*2, "GridTestyLine"+i));
				cf.msg = ""+i;
				cf.x = i;
				cf.y = 12;
				items.add(new CText(cf.createTextDef(), "GridTestyLabel"+i));
			}
			for(int i=0; i<APPHEIGHT; i+=50){
				items.add(new CLine(-1*APPWIDTH, i, APPWIDTH*2, i, "xAxisLine"+i));
				cf.msg = ""+i;
				cf.x = 0;
				cf.y = i;
				items.add(new CText(cf.createTextDef(), "GridTestxLabel"+i));
			}
	
			cf.setSize(300, 100);
			cf.url = dir + "GridTestA.png";
			cf.setLocation(100, 100);
			cf.depth = 1;
			//cf.opacity = 0.5;
			items.add(new CStaticImage(cf.createImageDef(), "GridTestA"));
			cf.url = dir + "GridTestB.png";
			cf.setLocation(200, 150);
			cf.opacity = 1;
			cf.depth = 0;
			items.add(new CStaticImage(cf.createImageDef(), "GridTestB"));
			cf.url = dir + "invalid path for testing purpose";
			cf.setLocation(100, 300);
			items.add(new CStaticImage(cf.createImageDef(), "GridTestInvalidImagePath"));
	
			cf.setLocation(500, 100);
			cf.setSize(100, 50);
			cf.url = dir + "GridTestInactive.png";
			cf.bgimage = new CStaticImage(cf.createImageDef(), "dummy");
			cf.url = dir + "GridTestActive.png";
			cf.hoverimage = new CStaticImage(cf.createImageDef(), "dummy");
			cf.url = dir + "GridTestClick.png";
			cf.clickimage = new CStaticImage(cf.createImageDef(), "dummy");
			items.add(new CButton(cf.createButtonDef(), "GridTestButton1", "GridTest Action"));
			cf.x += 100;
			cf.url = dir + "AnimButton/";
			cf.hoverimage = new CJifImage(cf.createImageDef(), 10, 8, "AnimButton");
			((CJifImage) cf.hoverimage).start();
			items.add(new CButton(cf.createButtonDef(), "GridTestButton2", "GridTest Action Fade"));
			
			cf.setLocation(500, 150);
			cf.setSize(300, 50);
			cf.url = dir + "txtBG.png";
			cf.bgimage = new CStaticImage(cf.createImageDef(), "dummy");
			cf.url = dir + "txtHover.png";
			cf.hoverimage = new CStaticImage(cf.createImageDef(), "dummy");
			cf.fillColor = Color.BLACK;
			cf.length = 10;
			cf.font = new Font("Ariel", Font.BOLD, 30);
			items.add(new CTextBox(cf.createTextBoxDef(), "GridTest txt"));
			
			cf.fillColor = Color.WHITE;
			CRect rect = new CRect(520, 220, 100, 100, "GridTestRect");
			rect.setStroke(new Color(255, 255, 255, 100));
			rect.setFill(new Color(255, 0, 0, 200));
			rect.setThickness(10);
			items.add(rect);
			items.add(new CRoundRect(630, 220, 100, 100, 50, "GridTestRRect"));
			items.add(new CCircle(520, 330, 100, 50, "GridTestCircle"));
			
			cf.url = dir + "GridTestAnim/";
			cf.x = 250;
			cf.y = 300;
			cf.width = 50;
			cf.height = 50;
			cf.opacity = 1;
			ArrayList<CJifImage> jifimg = new ArrayList<CJifImage>();
			jifimg.add(new CJifImage(cf.createImageDef(), 1, 5, "GridTestJIF1"));
			cf.x += 50;
			CJifImage stopAfterRun = new CJifImage(cf.createImageDef(), .5, 5, 1, 2, 3, "GridTestJIF2");
			stopAfterRun.setRoundBehavior(RoundBehavior.STOP);
			jifimg.add(stopAfterRun);
			cf.x += 50;
			CJifImage deleteAfterRun =new CJifImage(cf.createImageDef(), 1, 5, "GridTestRO");
			deleteAfterRun.setRoundBehavior(RoundBehavior.DELETE);
			jifimg.add(deleteAfterRun);
			for(CJifImage img : jifimg)
				img.start();
			items.addAll(jifimg);
			
			CGroupNode group = new CGroupNode("testgrid");
			group.addChildren(items);
			parent.gui.getMasterNode().addChild(group);
			parent.gui.refreshControls();
			return null;
		}
		
		public void clickEvent(MouseEvent e, String actionCommand){
			if(actionCommand == null)
				return;
			CGroupNode testgrid = (CGroupNode) parent.gui.getMasterNode().findChild("testgrid");
			switch (actionCommand){
			case "GridTest Action":
				//parent.destructiveJitter(testgrid, 20, 5, 20, 0, 20, 50);
				parent.jitter(testgrid, 20, 5, 20, 0, 20, 50);
				//parent.rmoveRelative(testgrid, 100, 0, 1000);
				//parent.accMove(testgrid, 100, 0, 1000, 1, 1);
				break;
			case "GridTest Action Fade":
				int dopac;
				if(testgrid.getOpacity() > 0)
					dopac = -1;
				else
					dopac = 1;
				//parent.fading(testgrid, dopac, 1000);
				parent.accFading(testgrid, dopac, 1000, 1);
			}
		}

		@Override
		public void dragEvent(MouseEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyTyped(KeyEvent e) {}
	}
	
	public static class jldTest implements ITest{
		public jldTest(){}
		
		public Object run(){
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
			LinkedHashMap<String, String> hme = new LinkedHashMap<String, String>();
			hme.put("c1", "30");
			hme.put("c2", "31");
			jld.put("c", hme);
			LinkedHashMap<String, ArrayList<String>> hmv = new LinkedHashMap<String, ArrayList<String>>();
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
			return null;
		}

		@Override
		public void clickEvent(MouseEvent e, String actionCommand) {}

		@Override
		public void dragEvent(MouseEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {}

		@Override
		public void keyTyped(KeyEvent e) {}
	}
	
	public static class soundTest implements ITest{
		public soundTest(){}

		@Override
		public Object run() {return null;}

		@Override
		public void clickEvent(MouseEvent e, String actionCommand) {}

		@Override
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
	
	public static class dialogRenderTest implements ITest{
		private final GUILogic parent;
		private final String time;
		private boolean ran = false;
		CDialogSeq cds;
		private String name = "";
		public dialogRenderTest(GUILogic caller){
			parent = caller;
			time = "_"+System.currentTimeMillis();
		}
		
		public CDialogSeq run(){
			final String dir = "Dialog Test/"; 
			CanvasFactory cf = new CanvasFactory();
	
			cf.x = 0;
			cf.y = 0;
			cf.width = -1;
			cf.height = -1;
			cf.fillColor = Color.BLACK;
			cf.opacity = 1;
			cf.depth = 0;
			cf.font = new Font("맑은 고딕", Font.BOLD, 20);
			cf.msg = "";
			cf.strokeColor = Color.WHITE;
			cf.strokeThickness = 2;
			CDialogSeq seq = new CDialogSeq("DialogTestSeq", cf.createTextDef());
			CDialogBox box = new CDialogBox("DialogTest A", seq);
			cf.url = dir + "bg.png";
			CStaticImage img = new CStaticImage(cf.createImageDef(), "DialogTest BG");
			seq.bg = img;
			cf.url = dir + "dialog.png";
			img = new CStaticImage(cf.createImageDef(), "DialogTest frame");
			seq.frame = img;
			cf.url = dir + "boyLarge.png";
			img = new CStaticImage(cf.createImageDef(), "DialogTest boy");
			img.xScale = img.yScale = 0.5;
			img.xScale *= -1;
			CStaticImage boy = img;
			box.setCharacterImage(img);
			box.setCharacterHeight(-100);
			box.setContentOffset(10, 230);
			box.setCharNameOffset(10, 210);
			box.setCharacterName("boy♂");
			box.setContent("누구세요?");
			seq.setRoot(box);
			
			box = new CDialogBox("DialogTest B", seq, box);
			cf.url = dir + "dak.png";
			img = new CStaticImage(cf.createImageDef(), "DialogTest dak");
			CStaticImage dak = img;
			box.setCharacterImage(img);
			box.setCharacterName("ヴァン");
			box.setContent("촉촉하게 만들어 주지!!!");
			box.setPrev(seq.currentNode);
			seq.currentNode.setNext(box);
			seq.stepNext();
			
			box = new CDialogOpt("DialogText C", seq, box);
			box.setCharacterImage(boy);
			box.setCharacterName("boy♂");
			box.setContent("어쩌지?!");
			for (int i=1; i<4; i++){
				cf.url = dir + i +".png";
				cf.bgimage = new CStaticImage(cf.createImageDef(), "Opt " + i);
				cf.url = dir + i +"-click.png";
				cf.clickimage = new CStaticImage(cf.createImageDef(), "Opt " + i + " click");
				cf.url = dir + i +"-hover.png";
				cf.hoverimage = new CStaticImage(cf.createImageDef(), "Opt " + i + " hover");
				((CDialogOpt)box).btns.add(new CButton(cf.createButtonDef(), "Opt " + i + " button", ""+i+time));
			}
			box.setPrev(seq.currentNode);
			seq.currentNode.setNext(box);
			seq.stepNext();
			
			box = new CDialogBox("DialogText 1", seq, box);
			box.setContent("이상한 변태를 따돌린 듯 하다...\r\n운이 좋았다...");
			box.setPrev(seq.currentNode);
			((CDialogOpt)seq.currentNode).results.add(box);
			
			box = new CDialogBox("DialogText 2", seq, box);
			box.setCharacterImage(dak);
			box.setCharacterName("ヴァン");
			box.setContent("이런이런~ 소방차는 빨간불에도 멈추지 않아 Boy♂");
			box.setPrev(seq.currentNode);
			((CDialogOpt)seq.currentNode).results.add(box);
			CDialogBox cdb2 = box;
			
			box = new CDialogBox("DialogText 3", seq, box);
			box.setContent("더욱더 매끄럽게!");
			box.setPrev(seq.currentNode);
			((CDialogOpt)seq.currentNode).results.add(box);
			
			box = new CDialogBox("DialogText end", seq, box);
			cf.url = dir + "boySmallCrying.png";
			img = new CStaticImage(cf.createImageDef(), "DialogTest crying");
			box.setCharacterImage(img);
			box.setCharacterName("boy♂");
			box.setContent("거...거긴 안돼...");
			box.setPrev(cdb2);
			cdb2.setNext(box);
			
			seq.currentNode = seq.getRoot();
			cds = seq;
			ran = true;
			refresh();
			return seq;
		}
		
		private void refresh(){
			if(!name.isEmpty()){
				parent.gui.getMasterNode().removeChild(name);
			}
			CGroupNode disp = Renderer.paintDialog(cds, new Rectangle(500, 300));
			name = disp.getName();
			parent.gui.getMasterNode().addChild(disp);
			parent.gui.refreshControls();
		}
	
		public void clickEvent(MouseEvent e, String actionCommand){
			if(!ran)
				return;
			if(actionCommand != null){
				if(actionCommand.matches("[0-9]"+time)){
					cds.stepSelect(Integer.parseInt(actionCommand.substring(0, 1))-1);
					refresh();
				}
			}else if(cds.currentNode.getNext() != null && !(cds.currentNode instanceof CDialogOpt)){
				cds.stepNext();
				refresh();
			}
		}

		@Override
		public void dragEvent(MouseEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			if(!ran)
				return;
			if(e.getKeyCode() == KeyEvent.VK_LEFT && cds.currentNode.getPrev() != null){
				cds.stepPrev();
				refresh();
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT && cds.currentNode.getNext() != null && !(cds.currentNode instanceof CDialogOpt)){
				cds.stepNext();
				refresh();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {}
	}

	public static class dialogPlayerTest implements ITest{
		private GUILogic parent;
		private DialogPlayer player;
		
		public dialogPlayerTest(GUILogic caller){
			parent = caller;
		}

		@Override
		public Object run() {
			player = new DialogPlayer(parent);
			JLD jld = new JldGetter().getValues("static/dialogPlayerTest.jld");
			player.init(jld);
			player.refresh();
			return null;
		}

		@Override
		public void clickEvent(MouseEvent e, String actionCommand) {
			if(actionCommand != null)
				player.clickButton(actionCommand);
			else
				player.stepNext();
		}

		@Override
		public void dragEvent(MouseEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT && player != null){
				player.stepPrev();
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT && player != null){
				player.stepNext();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {}
	}
}
