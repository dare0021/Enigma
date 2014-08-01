package canvasItems;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

/**
 * Use the exposed .field member to use JTextField features
 * xmargin is from x0 with a default value of 10
 * ymargin is from the bottom with (width-fontsize)/2 as default
 */
public class CTextBox extends ACItem implements ActionListener{
	public String actionCommand; 
	public double x1, y1, xmargin, ymargin;
	public int size;
	public boolean showHover, enabled;
	public CStaticImage bgimage, hoverimage;

	private boolean focus, cursorVisible;
	private CText field;
	private Timer timer;

	public CTextBox(CTextBoxDef def, String name){
		super(name);
		bgimage = def.bgimage;
		hoverimage = def.hoverimage;
		field = new CText(new CTextDef("", def.x, def.y, def.textColor, def.textStroke, def.strokeThickness, def.font, def.opacity, def.depth), name+".field");
		field.moveForward();
		setOpacity(def.opacity);
		setDepth(def.depth);
		actionCommand = null;
		enabled = true;
		focus = false;
		cursorVisible = false;
		timer = new Timer(500, this);
		size = def.size;
		setX0(def.x);
		x1 = def.x + def.width;
		setY0(def.y);
		y1 = def.y + def.height;
		xmargin = 10;
		ymargin = (getHeight()-def.font.getSize())/2;
	}
	
	public CStaticImage getImage(){
		if(showHover || focus)
			return hoverimage;
		else
			return bgimage;
	}

	public double getX0(){return x0;}
	public double getY0(){return y0;}
	public double getWidth(){return x1-x0;}
	public double getHeight(){return y1-y0;}
	public int getMaxLength(){return size;}
	public boolean isEnabled(){return enabled;}
	public boolean hasFocus(){return focus;}
	
	public void setX0(double x){x0 = x; field.setX(x);}
	public void setY0(double y){y0 = y; field.setY(y);}
	public void setX1(double x){x1 = x;}
	public void setY1(double y){y1 = y;}
	public void setMaxLength(int len){size = len;}
	public void setEnabled(boolean e){enabled = e;}
	public void setFocus(boolean f){
		focus = f; 
		if(f){
			cursorVisible = true;
			timer.start();
		}else{
			timer.stop();
			cursorVisible = false;
		}
	}
	
	public void setX(double x){
		double width = getWidth();
		x0 = x;
		field.setX(x);
		x1 = x0 + width;
	}public void setY(double y){
		double height = getHeight();
		y0 = y;
		field.setY(y);
		y1 = y0 + height;
	}
	
	public void setOpacity(double o){
		super.setOpacity(o);
		field.setOpacity(o);
	}
	
	//field vars
	public Font getFont(){return field.font;}
	public Color getRawColor(){return field.fillColor;}
	public int getOffset(){return field.offset;}
	public int getLength(){return getText().length();}
	public double getTextX(){return getX0() + xmargin;}
	public double getTextY(){return getY0() + getHeight() - ymargin;}
	public int getPrintLength(){return ((getLength()==getMaxLength()+1)&&!cursorVisible)?getMaxLength():getLength();}
	public String getText(){return field.text+(cursorVisible?"|":"");}
	
	public void setFont(Font font){field.font = font;}
	public void setRawColor(Color color){field.fillColor = color;}
	public void setOffset(int o){field.offset = o;}
	public void setText(String txt){
		if(txt.length() <= getMaxLength())
			field.text = txt;
		else
			field.text = txt.substring(0, getMaxLength());
		field.len = field.text.length();
	}
	
	public Color getEffectiveColor(){
		return new Color(field.fillColor.getRed(), field.fillColor.getGreen(), field.fillColor.getBlue(), (int)(field.fillColor.getAlpha()*getOpacity()));
	}
	
	//Key Event
	/**
	 * Receptive of any non-control chars, including Unicode 
	 */
	private boolean isChar(char c){
		if(Character.isISOControl(c))
			return false;
		return true;
	}
	/**
	 * Returns true if a known key combo was pressed
	 * Will not work with keyTyped. Use keyPressed
	 */
	public boolean isValidControlChar(java.awt.event.KeyEvent e){
		//CTRL
		if(e.isControlDown() && !e.isAltDown() && !e.isShiftDown()){
			if(e.getKeyCode() == java.awt.event.KeyEvent.VK_V){
				Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
				try {
					appendText((String)(cb.getData(DataFlavor.stringFlavor)));
				} catch (UnsupportedFlavorException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return true;
			}else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_C){
				Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
				timer.stop();
				cursorVisible = false;
				StringSelection s = new StringSelection(getText());
				cb.setContents(s, s);
				timer.start();
				return true;
			}
		}else{ //Other, such as backspace
			if(e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE)
				removeChar();
		}
		return false;
	}
	public void keyEvent(java.awt.event.KeyEvent e){
		if(isChar(e.getKeyChar()))
			appendChar(e.getKeyChar());
	}
	public void appendText(String txt){
		for (char c : txt.toCharArray())
			appendChar(c);
	}
	/**
	 * All appends should to through this
	 */
	public void appendChar(char c){
		timer.stop();
		cursorVisible = false;
		setText(getText()+c);
		timer.start();
	}
	public void removeChar(){
		timer.stop();
		cursorVisible = false;
		if(getLength()<1){
			timer.start();
			return;
		}
		setText(getText().substring(0, getLength()-1));
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		cursorVisible = !cursorVisible;
	}
}
