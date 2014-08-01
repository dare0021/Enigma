package enigma;

/**
 * The parent class for canvases used in the GUI
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import canvasItems.*;

class GUIcanvas extends JPanel implements IConstantsUI, IStringsGUI, MouseMotionListener, MouseListener, KeyListener {
	private CTextBox focusedTextBox;
	private CGroupNode masterNode;
	private ArrayList<CButton> buttons;
	private ArrayList<CTextBox> textboxes;
	GUILogic logic;
	
	public GUIcanvas(){
		this.setPreferredSize(new Dimension(APPWIDTH, APPHEIGHT));
		this.setBackground(Color.BLACK);
		reset();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
	}public void linkLogic(ExampleLogic parent){
		logic = parent;
	}
	
	/**
	 * Deletes ALL gui elements
	 */
	public void reset(){
		masterNode = new CGroupNode(MASTERNODE_DESC);
		masterNode.parent = null;
		buttons = new ArrayList<CButton>();
		textboxes = new ArrayList<CTextBox>();
	}
	
	public CGroupNode getMasterNode(){
		return masterNode;
	}
	
	/**
	 * Trawls the object tree to check for new interactive GUI elements
	 * 
	 * They are tracked independently to make the UI more responsive
	 * (a small array traversal is faster than a large tree traversal)
	 */
	public void refreshControls(){
		buttons.clear();
		textboxes.clear();
		registerControls(getMasterNode());
	}
	private void registerControls(CGroupNode root){
		for(ACItem node : root.getChildrenCopy()){
			if(node instanceof CButton){
				buttons.add((CButton) node);
			}else if(node instanceof CTextBox){
				textboxes.add((CTextBox) node);
			}else if(node instanceof CGroupNode){
				registerControls((CGroupNode) node);
			}
		}
	}
	
	/**
	 * Possibility of heap pollution < Ease of use gained
	 * Heap pollution: array given instead of a list of vars 
	 * will not be catched, even if the array contains
	 * incompatible data.
	 */
	@SuppressWarnings("unchecked")
	public void assignParent(CGroupNode parent, ArrayList<? extends ACItem> ... items){
		for (ArrayList<? extends ACItem> vect : items)
			for (ACItem iter : vect){
				parent.addChild(iter);
			}
	}
	
	/**
	 * Should never be called directly
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, graphicsQuality);
		Renderer r = new Renderer(g2d);
		r.run(this.getMasterNode());
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
		for(CButton btn : buttons){
			if(btn.isEnabled() && isBetween(btn.getX(), e.getX(), btn.x1) && isBetween(btn.getY(), e.getY(), btn.y1)){
				btn.showHover = true;
			}else
				btn.showHover = false;
		}for(CTextBox txt : textboxes){
			if(txt.isEnabled() && ((txt.hasFocus()) || 
									(isBetween(txt.getX(), e.getX(), txt.x1) && isBetween(txt.getY(), e.getY(), txt.y1)))){
				txt.showHover = true;
			}else{
				txt.showHover = false;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean actionSent = false;
		for(CButton btn : new ArrayList<CButton>(buttons)){ //prevent concurrent modification exception
			if(btn.isEnabled() && isBetween(btn.getX(), e.getX(), btn.x1) && isBetween(btn.getY(), e.getY(), btn.y1)){
				logic.clickEvent(e, btn.actionCommand);
				actionSent = true;
				focusedTextBox = null;
			}
		}
		for(CTextBox txt : new ArrayList<CTextBox>(textboxes)){
			if(txt.isEnabled() && isBetween(txt.getX(), e.getX(), txt.x1) && isBetween(txt.getY(), e.getY(), txt.y1)){
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
			if(btn.isEnabled() && e.getX()>btn.getX() && e.getX()<btn.x1 && e.getY()>btn.getY() && e.getY()<btn.y1)
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
	
	private boolean isBetween(double a, double val, double b){
		return (a < val && val < b) || (a > val && val > b);
	}
}
