package enigma;

/**
 * The parent class for canvases used in the GUI
 */

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import canvasItems.*;

class GUIcanvas extends JPanel implements IConstantsUI, IStringsGUI, MouseMotionListener, MouseListener {
	HashMap<String, Vector<ICShape>> shapes; //Each vector should be a single element for easy update/removal
	HashMap<String, Vector<CText>> texts; //Should have the same name as the JLine counterpart for the same reason
	HashMap<String, Vector<CButton>> buttons; //data list
	HashMap<String, CStaticImage> buttonimgs; //display list
	HashMap<String, Vector<CStaticImage>> staticimgs;
	HashMap<String, Vector<CJifImage>> jifimgs;
	ShipBattleLogic logic;
	
	public GUIcanvas(){
		this.setPreferredSize(new Dimension(APPWIDTH, APPHEIGHT));
		this.setBackground(Color.BLACK);
		shapes = new HashMap<String, Vector<ICShape>>();
		texts = new HashMap<String, Vector<CText>>();
		buttons = new HashMap<String, Vector<CButton>>();
		buttonimgs = new HashMap<String, CStaticImage>();	//Modified via MouseMoved()
		staticimgs = new HashMap<String, Vector<CStaticImage>>();
		jifimgs = new HashMap<String, Vector<CJifImage>>();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}public void linkLogic(ShipBattleLogic parent){
		logic = parent;
	}

	/**
	 * Being in the same class runs the risk of locking the GUI.
	 * Beware of the possibility while adding new tests.
	 */
	public void drawTestGrid(){
		CanvasFactory cf = new CanvasFactory();
		Vector<ICShape> grid = new Vector<ICShape>();
		Vector<CText> gridlbl = new Vector<CText>();
		Vector<CStaticImage> gridimg = new Vector<CStaticImage>();
		Vector<CJifImage> jifimg = new Vector<CJifImage>();
		Vector<CButton> btns = new Vector<CButton>();
		
		cf.fillColor = Color.WHITE;
		cf.font = new Font("Ariel", Font.PLAIN, 12);
		for(int i=0; i<APPWIDTH; i+=50){
			grid.add(new CLine(i, -1*APPHEIGHT, i, APPHEIGHT*2));
			cf.msg = ""+i;
			cf.x = i;
			cf.y = 12;
			gridlbl.add(new CText(cf.createTextDef()));
		}
		for(int i=0; i<APPHEIGHT; i+=50){
			grid.add(new CLine(-1*APPWIDTH, i, APPWIDTH*2, i));
			cf.msg = ""+i;
			cf.x = 0;
			cf.y = i;
			gridlbl.add(new CText(cf.createTextDef()));
		}

		cf.setSize(300, 100);
		cf.url = "GridTestA.png";
		cf.setLocation(100, 100);
		cf.opacity = 0.5;
		gridimg.add(new CStaticImage(cf.createImageDef()));
		cf.url = "GridTestB.png";
		cf.setLocation(200, 150);
		cf.opacity = 1;
		gridimg.add(new CStaticImage(cf.createImageDef()));
		cf.url = "invalid path for testing purpose";
		cf.setLocation(100, 300);
		gridimg.add(new CStaticImage(cf.createImageDef()));

		cf.setLocation(500, 100);
		cf.setSize(100, 50);
		cf.url = "GridTestInactive.png";
		btns.add(new CButton(cf.createImageDef(), "GridTestActive.png", "GridTest Button 1", "GridTest Action"));
		cf.x += 100;
		btns.add(new CButton(cf.createImageDef(), "GridTestActive.png", "GridTest Button 2", "GridTest Action Fade"));
		
		CRect rect = new CRect(520, 220, 100, 100);
		rect.setStroke(new Color(255, 255, 255, 100));
		rect.setFill(new Color(255, 0, 0, 200));
		rect.setThickness(10);
		grid.add(rect);
		grid.add(new CRoundRect(630, 220, 100, 100, 50));
		grid.add(new CCircle(520, 330, 100, 50));
		
		cf.url = "GridTestAnim/";
		cf.x = 250;
		cf.y = 300;
		cf.width = 50;
		cf.height = 50;
		cf.opacity = 1;
		jifimg.add(new CJifImage(cf.createImageDef(), 1, 5));
		cf.x += 50;
		jifimg.add(new CJifImage(cf.createImageDef(), 5, 5, 1, 2, 3));
		for(CJifImage img : jifimg)
			img.start();
		
		addItem("testgrid", grid);
		addItem("testgrid", gridlbl);
		addItem("testgrid", gridimg);
		addItem("testgrid", btns);
		addItem("testgrid", jifimg);
	}
	
	/**
	 * Deletes ALL gui elements
	 */
	public void clear(){
		shapes.clear();
		texts.clear();
		buttons.clear();
		buttonimgs.clear();
		staticimgs.clear();
		jifimgs.clear();
	}
		
	public void delete(String ... items){
		for(String item : items){
			shapes.remove(item);
			texts.remove(item);
			buttons.remove(item);
			buttonimgs.remove(item);
			staticimgs.remove(item);
			jifimgs.remove(item);
		}
	}
	
	public Vector<ICShape> retrieveShape(String item){return shapes.get(item);}
	public Vector<CText> retrieveText(String item){return texts.get(item);}
	public Vector<CButton> retrieveButton(String item){return buttons.get(item);}
	public Vector<CStaticImage> retrieveStaticImg(String item){return staticimgs.get(item);}
	public Vector<CJifImage> retrieveJifImg(String item){return jifimgs.get(item);}
	
	/**
	 * Moves said item by the specified amount, relatively
	 */
	public void translate(String item, double dx, double dy){
		for(String key : texts.keySet()){
			for(CText label : texts.get(key)){
				label.x += dx;
				label.y += dy;
			}
		}
		for(String key : shapes.keySet()){
			for(ICShape shape : shapes.get(key)){
				shape.moveRelative(dx, dy);
			}
		}
		for(String key : staticimgs.keySet()){
			for(CStaticImage img : staticimgs.get(key)){
				img.x += dx;
				img.y += dy;
			}
		}
		for(String key : jifimgs.keySet()){
			for(CJifImage img : jifimgs.get(key)){
				img.x += dx;
				img.y += dy;
			}
		}
		for(String key : buttons.keySet()){
			for(CButton btn : buttons.get(key)){
				btn.x += dx;
				btn.y += dy;
				buttonimgs.remove(key);
				buttonimgs.put(btn.name, new CStaticImage(new CImageDef(btn.bgimage, btn.x, btn.y, btn.width, btn.height, btn.getOpacity())));
			}
		}
	}
	
	public void opacChange(String item, double dx){
		for(String key : texts.keySet()){
			for(CText label : texts.get(key)){
				label.setOpacity(label.getOpacity() + dx);
			}
		}
		for(String key : shapes.keySet()){
			for(ICShape shape : shapes.get(key)){
				shape.setOpacity(shape.getOpacity() + dx);
			}
		}
		for(String key : staticimgs.keySet()){
			for(CStaticImage img : staticimgs.get(key)){
				img.setOpacity(img.getOpacity() + dx);
			}
		}
		for(String key : jifimgs.keySet()){
			for(CJifImage img : jifimgs.get(key)){
				img.setOpacity(img.getOpacity() + dx);
			}
		}
		for(String key : buttons.keySet()){
			for(CButton btn : buttons.get(key)){
				btn.setOpacity(btn.getOpacity() + dx);
				buttonimgs.remove(btn.name);
				buttonimgs.put(btn.name, new CStaticImage(new CImageDef(btn.bgimage, btn.x, btn.y, btn.width, btn.height, btn.getOpacity())));
			}
		}
	}
	
	/**
	 * Moves said item to the coordinate, non-relative.
	 */
	public void moveto(String item, double x, double y){
		for(String key : texts.keySet()){
			for(CText label : texts.get(key)){
				label.x = x;
				label.y = y;
			}
		}
		for(String key : shapes.keySet()){
			for(ICShape shape : shapes.get(key)){
				shape.moveTo(x, y);
			}
		}
		for(String key : staticimgs.keySet()){
			for(CStaticImage img : staticimgs.get(key)){
				img.x = x;
				img.y = y;
			}
		}
		for(String key : jifimgs.keySet()){
			for(CJifImage img : jifimgs.get(key)){
				img.x = x;
				img.y = y;
			}
		}
		for(String key : buttons.keySet()){
			for(CButton btn : buttons.get(key)){
				btn.x = x;
				btn.y = y;
				buttonimgs.remove(btn.name);
				buttonimgs.put(btn.name, new CStaticImage(new CImageDef(btn.bgimage, btn.x, btn.y, btn.width, btn.height, btn.getOpacity())));
			}
		}
	}
	
	/**
	 * A single name can have only one Object of a certain class
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void addItem(String name, Object item){
		Object comp;
		if(item instanceof Vector<?>){
			comp = ((Vector) item).firstElement();
		}else{
			comp = item;
		}
		if(false){ //so it's easy to swap around the other cases
		}else if(comp instanceof CText){
			if(texts.keySet().contains(name))
				texts.remove(name);
			texts.put(name, (Vector<CText>) item);
			return;
		}else if(comp instanceof CButton){
			if(buttons.keySet().contains(name))
				buttons.remove(name);
			buttons.put(name, (Vector<CButton>) item);
			for(CButton btn : (Vector<CButton>) item)
				buttonimgs.put(btn.name, new CStaticImage(new CImageDef(btn.bgimage, btn.x, btn.y, btn.width, btn.height, btn.getOpacity())));
			return;
		}else if(comp instanceof CJifImage){ //not to be put after CStaticImage, since CJifImage extends CStaticImage
			if(jifimgs.keySet().contains(name))
				jifimgs.remove(name);
			jifimgs.put(name, (Vector<CJifImage>) item);
			return;
		}else if(comp instanceof CStaticImage){
			if(staticimgs.keySet().contains(name))
				staticimgs.remove(name);
			staticimgs.put(name, (Vector<CStaticImage>) item);
			return;
		}else if(comp instanceof ICShape){
			if(shapes.keySet().contains(name))
				shapes.remove(name);
			shapes.put(name, (Vector<ICShape>) item);
			return;
		}else{
			System.out.println("ERR: ShipBattleGUI.addItem unhandled type "+item);
			new Exception().printStackTrace();
		}
	}
	
	/**
	 * Should never be called by itself
	 */
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(String key : texts.keySet()){
			for(CText label : texts.get(key)){
				g2d.setFont(label.font);
				g2d.setColor(label.getEffectiveColor());
				g2d.drawChars(label.text.toCharArray(), label.offset, label.len, (int)label.x, (int)label.y);
			}
		}
		for(String key : shapes.keySet()){
			for(ICShape shape : shapes.get(key)){
				g2d.setColor(shape.getFill());
				g2d.fill(shape.getShape());
		        g2d.setColor(shape.getStroke());
		        g2d.setStroke(new BasicStroke((int)shape.getThickness()));
		        g2d.draw(shape.getShape());
			}
		}
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		for(String key : staticimgs.keySet()){
			for(CStaticImage img : staticimgs.get(key)){
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, img.getOpacity()));
				g2d.drawImage(img.image, (int)img.x, (int)img.y, null);
			}
		}
		for(String key : jifimgs.keySet()){
			for(CJifImage img : jifimgs.get(key)){
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, img.getOpacity()));
				g2d.drawImage(img.image, (int)img.x, (int)img.y, null);
			}
		}
		for(String key : buttonimgs.keySet()){
			CStaticImage img = buttonimgs.get(key);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, img.getOpacity()));
			g2d.drawImage(img.image, (int)img.x, (int)img.y, null);
		}
	}

	/**
	 * Like mouseMoved, but called when mouse button is pressed while moving
	 * mouseMoved is not called while dragging
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		logic.dragEvent(e);
	}

	/**
	 * handles button rollover detection
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		for(String key : buttons.keySet()){
			for(CButton btn : buttons.get(key)){
				if(e.getX()>btn.x && e.getX()<btn.x1 && e.getY()>btn.y && e.getY()<btn.y1){
					buttonimgs.put(btn.name, new CStaticImage(new CImageDef(btn.hoverimage, btn.x, btn.y, btn.width, btn.height, btn.getOpacity())));
				}else{
					buttonimgs.put(btn.name, new CStaticImage(new CImageDef(btn.bgimage, btn.x, btn.y, btn.width, btn.height, btn.getOpacity())));
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(String key : buttons.keySet()){
			for(CButton btn : buttons.get(key)){
				if(e.getX()>btn.x && e.getX()<btn.x1 && e.getY()>btn.y && e.getY()<btn.y1){
					logic.clickEvent(e, btn.actionCommand);
				}else{
					logic.clickEvent(e, null);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
