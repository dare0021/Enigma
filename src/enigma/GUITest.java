package enigma;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

import canvasItems.CButton;
import canvasItems.CCircle;
import canvasItems.CJifImage;
import canvasItems.CLine;
import canvasItems.CRect;
import canvasItems.CRoundRect;
import canvasItems.CStaticImage;
import canvasItems.CText;
import canvasItems.CTextBox;
import canvasItems.CanvasFactory;
import canvasItems.ICShape;

public class GUITest implements IConstantsUI {
	private final String dir = "GUI Test/"; 
	private final GUIcanvas canvas;
	public GUITest(GUIcanvas _canvas){
		canvas = _canvas;
	}

	public void drawGridTest(){
		CanvasFactory cf = new CanvasFactory();
		Vector<ICShape> grid = new Vector<ICShape>();
		Vector<CText> gridlbl = new Vector<CText>();
		Vector<CStaticImage> gridimg = new Vector<CStaticImage>();
		Vector<CJifImage> jifimg = new Vector<CJifImage>();
		Vector<CButton> btns = new Vector<CButton>();
		Vector<CTextBox> boxes = new Vector<CTextBox>();
		
		cf.fillColor = Color.WHITE;
		cf.font = new Font("Ariel", Font.PLAIN, 12);
		for(int i=0; i<APPWIDTH; i+=50){
			grid.add(new CLine(i, -1*APPHEIGHT, i, APPHEIGHT*2, "GridTestyLine"+i));
			cf.msg = ""+i;
			cf.x = i;
			cf.y = 12;
			gridlbl.add(new CText(cf.createTextDef(), "GridTestyLabel"+i));
		}
		for(int i=0; i<APPHEIGHT; i+=50){
			grid.add(new CLine(-1*APPWIDTH, i, APPWIDTH*2, i, "xAxisLine"+i));
			cf.msg = ""+i;
			cf.x = 0;
			cf.y = i;
			gridlbl.add(new CText(cf.createTextDef(), "GridTestxLabel"+i));
		}

		cf.setSize(300, 100);
		cf.url = dir + "GridTestA.png";
		cf.setLocation(100, 100);
		cf.opacity = 0.5;
		gridimg.add(new CStaticImage(cf.createImageDef(), "GridTestA"));
		cf.url = dir + "GridTestB.png";
		cf.setLocation(200, 150);
		cf.opacity = 1;
		gridimg.add(new CStaticImage(cf.createImageDef(), "GridTestB"));
		cf.url = dir + "invalid path for testing purpose";
		cf.setLocation(100, 300);
		//gridimg.add(new CStaticImage(cf.createImageDef(), "GridTestInvalidImagePath"));

		cf.setLocation(500, 100);
		cf.setSize(100, 50);
		cf.url = dir + "GridTestInactive.png";
		cf.bgimage = new CStaticImage(cf.createImageDef(), "dummy");
		cf.url = dir + "GridTestActive.png";
		cf.hoverimage = new CStaticImage(cf.createImageDef(), "dummy");
		cf.url = dir + "GridTestClick.png";
		cf.clickimage = new CStaticImage(cf.createImageDef(), "dummy");
		btns.add(new CButton(cf.createButtonDef(), "GridTestButton1", "GridTest Action"));
		cf.x += 100;
		cf.url = dir + "AnimButton/";
		cf.hoverimage = new CJifImage(cf.createImageDef(), 10, 8, "AnimButton");
		((CJifImage) cf.hoverimage).start();
		btns.add(new CButton(cf.createButtonDef(), "GridTestButton2", "GridTest Action Fade"));
		
		cf.setLocation(500, 150);
		cf.setSize(300, 50);
		cf.url = dir + "txtBG.png";
		cf.bgimage = new CStaticImage(cf.createImageDef(), "dummy");
		cf.url = dir + "txtHover.png";
		cf.hoverimage = new CStaticImage(cf.createImageDef(), "dummy");
		cf.fillColor = Color.BLACK;
		cf.length = 10;
		cf.font = new Font("Ariel", Font.BOLD, 30);
		boxes.add(new CTextBox(cf.createTextBoxDef(), "GridTest txt"));
		
		cf.fillColor = Color.WHITE;
		CRect rect = new CRect(520, 220, 100, 100, "GridTestRect");
		rect.setStroke(new Color(255, 255, 255, 100));
		rect.setFill(new Color(255, 0, 0, 200));
		rect.setThickness(10);
		grid.add(rect);
		grid.add(new CRoundRect(630, 220, 100, 100, 50, "GridTestRRect"));
		grid.add(new CCircle(520, 330, 100, 50, "GridTestCircle"));
		
		cf.url = dir + "GridTestAnim/";
		cf.x = 250;
		cf.y = 300;
		cf.width = 50;
		cf.height = 50;
		cf.opacity = 1;
		jifimg.add(new CJifImage(cf.createImageDef(), 1, 5, "GridTestJIF1"));
		cf.x += 50;
		jifimg.add(new CJifImage(cf.createImageDef(), 5, 5, 1, 2, 3, "GridTestJIF2"));
		cf.x += 50;
		CJifImage runonce =new CJifImage(cf.createImageDef(), 1, 5, "GridTestRO");
		runonce.setDeleteWhenDone(true);
		jifimg.add(runonce);
		for(CJifImage img : jifimg)
			img.start();
		
		canvas.assignGroup("testgrid", grid, gridlbl, gridimg, btns, jifimg, boxes);
		canvas.addItem(grid);
		canvas.addItem(gridlbl);
		canvas.addItem(gridimg);
		canvas.addItem(btns);
		canvas.addItem(jifimg);
		canvas.addItem(boxes);
	}
}
