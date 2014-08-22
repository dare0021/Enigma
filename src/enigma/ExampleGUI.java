package enigma;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import canvasItems.CBasicText;
import canvasItems.CButtonDef;
import canvasItems.CStaticImage;
import canvasItems.CTextButton;
import canvasItems.CTextDef;
import canvasItems.CanvasFactory;

class ExampleGUI extends GUIcanvas implements IConstantsUI {
	public ExampleGUI(){
		super();
	}

	public void showInitialUI(){
		String dir = "Test aux/";
		CanvasFactory cf = new CanvasFactory();
		cf.x = cf.y = 0;
		cf.width = cf.height = -1;
		cf.url = dir + "bg.png";
		CStaticImage image = new CStaticImage(cf.createImageDef(), "initUIbg");
		image.moveBackward();
		this.getMasterNode().addChild(image);
		cf.url = dir + "btn.png";
		cf.bgimage = new CStaticImage(cf.createImageDef(), "initUIbtnbg");
		cf.url = dir + "btn-hov.png";
		cf.hoverimage = new CStaticImage(cf.createImageDef(), "initUIbtnhov");
		cf.url = dir + "btn-click.png";
		cf.clickimage = new CStaticImage(cf.createImageDef(), "initUIbtnclick");
		CButtonDef btndef = cf.createButtonDef();
		cf.strokeColor = Color.BLACK;
		cf.fillColor = Color.WHITE;
		cf.strokeThickness = 2;
		cf.font = new Font("Verdana", Font.BOLD, 15);
		cf.msg = "";
		CTextDef txtdef = cf.createTextDef();
		ArrayList<CTextButton> btns = new ArrayList<CTextButton>();
		txtdef.text = "gridTest";
		btns.add(new CTextButton(btndef, new CBasicText(txtdef, "initUItxt1")));
		txtdef.text = "jldTest";
		btns.add(new CTextButton(btndef, new CBasicText(txtdef, "initUItxt2")));
		txtdef.text = "soundTest";
		btns.add(new CTextButton(btndef, new CBasicText(txtdef, "initUItxt3")));
		txtdef.text = "dialogRenderTest";
		btns.add(new CTextButton(btndef, new CBasicText(txtdef, "initUItxt4")));
		txtdef.text = "dialogPlayerTest";
		btns.add(new CTextButton(btndef, new CBasicText(txtdef, "initUItxt5")));
		double x = (APPWIDTH - btns.get(0).getWidth()) / 2;
		double dy = (APPHEIGHT - btns.get(0).getHeight()) / (btns.size()-1);
		int i = 0;
		for(CTextButton b : btns){
			b.setX(x);
			b.setY(i * dy);
			this.getMasterNode().addChild(b);
			i++;
		}
		this.refreshControls();
	}
}
