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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import canvasItems.*;

class GUIcanvas extends JPanel implements IConstantsUI, IStringsGUI, MouseMotionListener, MouseListener, KeyListener {
	private CTextBox focusedTextBox;
	Vector<ICShape> shapes; 
	Vector<CText> texts; 
	Vector<CButton> buttons;
	Vector<CTextBox> textboxes;
	Vector<CStaticImage> staticimgs;
	Vector<CJifImage> jifimgs;
	ExampleLogic logic;
	
	public GUIcanvas(){
		this.setPreferredSize(new Dimension(APPWIDTH, APPHEIGHT));
		this.setBackground(Color.BLACK);
		shapes = new Vector<ICShape>();
		texts = new Vector<CText>();
		buttons = new Vector<CButton>();
		textboxes = new Vector<CTextBox>();
		staticimgs = new Vector<CStaticImage>();
		jifimgs = new Vector<CJifImage>();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
	}public void linkLogic(ExampleLogic parent){
		logic = parent;
	}
	
	/**
	 * Deletes ALL gui elements
	 */
	public void clear(){
		shapes.clear();
		texts.clear();
		buttons.clear();
		textboxes.clear();
		staticimgs.clear();
		jifimgs.clear();
	}
		
	public void delete(String ... items){
		for(String item : items){
			shapes.remove(item);
			texts.remove(item);
			buttons.remove(item);
			textboxes.remove(item);
			staticimgs.remove(item);
			jifimgs.remove(item);
		}
	}
	
	/**
	 * Possibility of heap pollution < Ease of use gained
	 * Heap pollution: array given instead of a list of vars 
	 * will not be catched, even if the array contains
	 * incompatible data.
	 */
	@SuppressWarnings("unchecked")
	public void assignGroup(String group, Vector<? extends ICItem> ... items){
		for (Vector<? extends ICItem> vect : items)
			for (ICItem iter : vect)
				iter.setGroup(group);
	}
	
	/**
	 * Only returns a single item, which can be used
	 * as a representation of a group
	 */
	private ICItem findIn(CTargetAgent def, Vector<? extends ICItem> vect){
		switch (def.which){
		case NAME:
			for(ICItem iter : vect){
				if(iter.getName().equals(def.name))
					return iter;
			}
			break;
		case GROUP:
			for(ICItem iter : vect){
				if(iter.getGroup().equals(def.group))
					return iter;
			}
			break;
		case BOTH:
			for(ICItem iter : vect){
				if(iter.getName().equals(def.name) && iter.getGroup().equals(def.group))
					return iter;
			}
			break;
		default:
			System.out.println("ERR: GUIcanvas.findIn unhandled type "+def.which);
			new Exception().printStackTrace();
			break;
		}
		return null;
	}

	private Vector<ICItem> findAllIn(CTargetAgent def, Vector<? extends ICItem> vect){
		Vector<ICItem> out = new Vector<ICItem>();
		switch (def.which){
		case NAME:
			for(ICItem iter : vect){
				if(iter.getName().equals(def.name))
					out.add(iter);
			}
			break;
		case GROUP:
			for(ICItem iter : vect){
				if(iter.getGroup().equals(def.group))
					out.add(iter);
			}
			break;
		case BOTH:
			for(ICItem iter : vect){
				if(iter.getName().equals(def.name) && iter.getGroup().equals(def.group))
					out.add(iter);
			}
			break;
		default:
			System.out.println("ERR: GUIcanvas.findIn unhandled type "+def.which);
			new Exception().printStackTrace();
			break;
		}
		return out;
	}
	private Vector<ICItem> collateVectors(){
		Vector<ICItem> out = new Vector<ICItem>();
		out.addAll(shapes);
		out.addAll(texts);
		out.addAll(buttons);
		out.addAll(textboxes);
		out.addAll(staticimgs);
		out.addAll(jifimgs);
		return out;
	}public ICItem retrieveItem(CTargetAgent def){
		return findIn(def, collateVectors());
	}public Vector<ICItem> retrieveAll(CTargetAgent def){
		return findAllIn(def, collateVectors());
	}
	public ICShape retrieveShape(CTargetAgent def){return (ICShape) findIn(def, shapes);}
	public CText retrieveText(CTargetAgent def){return (CText) findIn(def, texts);}
	public CButton retrieveButton(CTargetAgent def){return (CButton) findIn(def, buttons);}
	public CTextBox retrieveTextBox(CTargetAgent def){return (CTextBox) findIn(def, textboxes);}
	public CStaticImage retrieveStaticImg(CTargetAgent def){return (CStaticImage) findIn(def, staticimgs);}
	public CJifImage retrieveJifImg(CTargetAgent def){return (CJifImage) findIn(def, jifimgs);}
	
	/**
	 * Moves said item by the specified amount, relatively
	 * Will move all items with the shared name
	 */
	public void translate(CTargetAgent def, double dx, double dy){
		switch (def.which){
		case NAME:
			for(ICItem iter : collateVectors()){
				if(iter.getName().equals(def.name)){
					iter.setX(iter.getX()+dx);
					iter.setY(iter.getY()+dy);
				}
			}
			break;
		case GROUP:
			for(ICItem iter : collateVectors()){
				if(iter.getGroup().equals(def.group)){
					iter.setX(iter.getX()+dx);
					iter.setY(iter.getY()+dy);
				}
			}
			break;
		case BOTH:
			for(ICItem iter : collateVectors()){
				if(iter.getName().equals(def.name) && iter.getGroup().equals(def.group)){
					iter.setX(iter.getX()+dx);
					iter.setY(iter.getY()+dy);
				}
			}
			break;
		default:
			System.out.println("ERR: GUIcanvas.translate unhandled type "+def.which);
			new Exception().printStackTrace();
			break;
		}
	}
	
	public void opacChange(CTargetAgent def, double dx){
		switch (def.which){
		case NAME:
			for(ICItem iter : collateVectors()){
				if(iter.getName().equals(def.name)){
					iter.setOpacity(iter.getOpacity()+dx);
				}
			}
			break;
		case GROUP:
			for(ICItem iter : collateVectors()){
				if(iter.getGroup().equals(def.group)){
					iter.setOpacity(iter.getOpacity()+dx);
				}
			}
			break;
		case BOTH:
			for(ICItem iter : collateVectors()){
				if(iter.getName().equals(def.name) && iter.getGroup().equals(def.group)){
					iter.setOpacity(iter.getOpacity()+dx);
				}
			}
			break;
		default:
			System.out.println("ERR: GUIcanvas.opacChange unhandled type "+def.which);
			new Exception().printStackTrace();
			break;
		}
	}
	
	/**
	 * Moves said item to the coordinate, non-relative.
	 * 
	 * Moving a group will move the whole group to that point
	 * without preserving the relative displacement of 
	 * items within the group
	 */
	public void moveTo(CTargetAgent def, double x, double y){
		switch (def.which){
		case NAME:
			for(ICItem iter : collateVectors()){
				if(iter.getName().equals(def.name)){
					iter.setX(x);
					iter.setY(y);
				}
			}
			break;
		case GROUP:
			for(ICItem iter : collateVectors()){
				if(iter.getGroup().equals(def.group)){
					iter.setX(x);
					iter.setY(y);
				}
			}
			break;
		case BOTH:
			for(ICItem iter : collateVectors()){
				if(iter.getName().equals(def.name) && iter.getGroup().equals(def.group)){
					iter.setX(x);
					iter.setY(y);
				}
			}
			break;
		default:
			System.out.println("ERR: GUIcanvas.moveTo unhandled type "+def.which);
			new Exception().printStackTrace();
			break;
		}
	}
	
	/**
	 * Does not check for pre-existing items with the
	 * same name/group combination 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void addItem(Vector<? extends ICItem> item){
		switch (item.firstElement().getType()){
		case TEXT:
			texts.addAll((Vector<CText>) item);
			break;
		case BUTTON:
			buttons.addAll((Vector<CButton>) item);
			break;
		case STATICIMAGE:
			staticimgs.addAll((Vector<CStaticImage>) item);
			break;
		case JIFIMAGE:
			jifimgs.addAll((Vector<CJifImage>) item);
			break;
		case CIRCLE:
		case LINE:
		case RECT:
		case ROUNDRECT:
			shapes.addAll((Vector<ICShape>) item);
			break;
		case TEXTBOX:
			textboxes.addAll((Vector<CTextBox>) item);
			break;
		default:
			System.out.println("ERR: GUIcanvas.addItem unhandled type "+item);
			new Exception().printStackTrace();
			break;
		}
	}
	
	/**
	 * Should never be called directly
	 */
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, graphicsQuality);
		for(CText label : texts){
			g2d.setFont(label.font);
			g2d.setColor(label.getEffectiveColor());
			g2d.drawChars(label.text.toCharArray(), label.offset, label.len, (int)label.x0, (int)label.y0);
		}
		for(ICShape shape : shapes){
			g2d.setColor(shape.getFill());
			g2d.fill(shape.getShape());
	        g2d.setColor(shape.getStroke());
	        g2d.setStroke(new BasicStroke((int)shape.getThickness()));
	        g2d.draw(shape.getShape());
		}
		for(CStaticImage img : staticimgs){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) img.getOpacity()));
			g2d.drawImage(img.image, (int)img.x0, (int)img.y0, (int)(img.getEffectiveX1()), (int)(img.getEffectiveY1()), 0, 0, (int)img.width, (int)img.height, null);
		}
		Vector<CJifImage> clone = new Vector<CJifImage>(jifimgs); //required to circumvent concurrent modification exception
		for(CJifImage img : clone){
			if(img.isDone())
				jifimgs.remove(img);
			else{
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) img.getOpacity()));
				g2d.drawImage(img.image, (int)img.x0, (int)img.y0, (int)(img.getEffectiveX1()), (int)(img.getEffectiveY1()), 0, 0, (int)img.width, (int)img.height, null);
			}
		}
		for(CButton btn : buttons){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) btn.getOpacity()));
			g2d.drawImage(btn.getImage().image, (int)btn.x0, (int)btn.y0, null);
		}
		for(CTextBox txt : textboxes){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) txt.getOpacity()));
			g2d.drawImage(txt.getImage().image, (int)txt.x0, (int)txt.y0, null);
			
			g2d.setFont(txt.getFont());
			g2d.setColor(txt.getEffectiveColor());
			g2d.drawChars(txt.getText().toCharArray(), txt.getOffset(), txt.getPrintLength(), (int)txt.getTextX(), (int)txt.getTextY());
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
		//System.out.println(this.retrieveButton(new CTargetAgent("","testgrid",GROUP)).x0);
		//System.out.println(this.retrieveButton(new  CTargetAgent("","testgrid",GROUP)).opacity);
		for(CButton btn : buttons){
			if(btn.isEnabled() && e.getX()>btn.x0 && e.getX()<btn.x1 && e.getY()>btn.y0 && e.getY()<btn.y1){
				btn.showHover = true;
			}else
				btn.showHover = false;
		}for(CTextBox txt : textboxes){
			if(txt.isEnabled() && ((txt.hasFocus()) || 
									(e.getX()>txt.x0 && e.getX()<txt.x1 && e.getY()>txt.y0 && e.getY()<txt.y1))){
				txt.showHover = true;
			}else{
				txt.showHover = false;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean actionSent = false;
		for(CButton btn : buttons){
			if(btn.isEnabled() && e.getX()>btn.x0 && e.getX()<btn.x1 && e.getY()>btn.y0 && e.getY()<btn.y1){
				logic.clickEvent(e, btn.actionCommand);
				actionSent = true;
				focusedTextBox = null;
			}
		}
		for(CTextBox txt : textboxes){
			if(txt.isEnabled() && (e.getX()>txt.x0 && e.getX()<txt.x1 && e.getY()>txt.y0 && e.getY()<txt.y1)){
				txt.setFocus(true);
				logic.clickEvent(e, txt.actionCommand);
				actionSent = true;
				focusedTextBox = txt;
			}else{
				txt.setFocus(false);
			}
		}
		if(!actionSent){
			focusedTextBox = null;
			logic.clickEvent(e, null);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {} //mouse entered the window
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent e) {
		for(CButton btn : buttons){
			if(btn.isEnabled() && e.getX()>btn.x0 && e.getX()<btn.x1 && e.getY()>btn.y0 && e.getY()<btn.y1)
				btn.showClicked = true;
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		for(CButton btn : buttons){
			btn.showClicked = false;
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(focusedTextBox != null){
			focusedTextBox.isValidControlChar(e);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {
		if(focusedTextBox != null){
			focusedTextBox.keyEvent(e);
		}
	}
}
