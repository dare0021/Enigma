package enigma;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import canvasItems.*;

public class GUITest implements IConstantsUI {
	private final String dir = "GUI Test/"; 
	private final GUIcanvas canvas;
	public GUITest(GUIcanvas _canvas){
		canvas = _canvas;
	}

	public void drawGridTest(){
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
		jifimg.add(new CJifImage(cf.createImageDef(), 5, 5, 1, 2, 3, "GridTestJIF2"));
		cf.x += 50;
		CJifImage runonce =new CJifImage(cf.createImageDef(), 1, 5, "GridTestRO");
		runonce.setDeleteWhenDone(true);
		jifimg.add(runonce);
		for(CJifImage img : jifimg)
			img.start();
		items.addAll(jifimg);
		
		CGroupNode group = new CGroupNode("testgrid");
		group.addChildren(items);
		canvas.registerControls(group);
		canvas.getMasterNode().addChild(group);
	}
}
