package enigma;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;

import canvasItems.*;

public class Renderer implements IConstantsUI{
	private Graphics2D g2d;
	private double groupOpacity;
	
	public Renderer(Graphics2D g){
		g2d = g;
	}
	
	public void run(CGroupNode root){
		ArrayList<ACItem> deleteOnExit = new ArrayList<ACItem>();
		ArrayList<CGroupNode> runBeforeExit = new ArrayList<CGroupNode>();
		groupOpacity = root.getEffectiveOpacity();
		for (ACItem node : root.getChildrenCopy()){
			if(node instanceof CText){
				paintText((CText) node);
			}else if(node instanceof ACShape){
				paintShape((ACShape) node);
			}else if(node instanceof CJifImage){ //must come before CStaticImage due to inheritance
				if(((CJifImage) node).isDone())
					deleteOnExit.add(node);
				else
					paintJifImage((CJifImage) node);
			}else if(node instanceof CStaticImage){
				paintStaticImage((CStaticImage) node);
			}else if(node instanceof CButton){
				paintButton((CButton) node);
			}else if(node instanceof CTextBox){
				paintTextBox((CTextBox) node);
			}else if(node instanceof CGroupNode){
				runBeforeExit.add((CGroupNode) node);
			}else{
				System.out.println("ERR: Renderer.run unhandled type "+node.getType().getDesc());
				new Exception().printStackTrace();
			}
		}
		for (CGroupNode node : runBeforeExit){
			this.run(node);
		}
		root.removeChild(deleteOnExit);
	}
	
	private void paintText(CText label){
		g2d.setFont(label.font);
		g2d.setColor(label.getEffectiveColor(groupOpacity));
		g2d.drawChars(label.text.toCharArray(), label.offset, label.len, (int)label.x0, (int)label.y0);
	}
	
	private void paintShape(ACShape shape){
		g2d.setColor(shape.getFill(groupOpacity));
		g2d.fill(shape.getShape());
        g2d.setColor(shape.getStroke(groupOpacity));
        g2d.setStroke(new BasicStroke((int)shape.getThickness()));
        g2d.draw(shape.getShape());
	}
	
	private void paintStaticImage(CStaticImage img){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (img.getOpacity()*groupOpacity)));
		g2d.drawImage(img.image, (int)img.x0, (int)img.y0, (int)(img.getEffectiveX1()), (int)(img.getEffectiveY1()), 0, 0, (int)img.width, (int)img.height, null);
	}
	
	private void paintJifImage(CJifImage img){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (img.getOpacity()*groupOpacity)));
		g2d.drawImage(img.image, (int)img.x0, (int)img.y0, (int)(img.getEffectiveX1()), (int)(img.getEffectiveY1()), 0, 0, (int)img.width, (int)img.height, null);
	}
	
	private void paintButton(CButton btn){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (btn.getOpacity()*groupOpacity)));
		g2d.drawImage(btn.getImage().image, (int)btn.x0, (int)btn.y0, null);
	}
	
	private void paintTextBox(CTextBox txt){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (txt.getOpacity()*groupOpacity)));
		g2d.drawImage(txt.getImage().image, (int)txt.x0, (int)txt.y0, null);
		
		g2d.setFont(txt.getFont());
		g2d.setColor(txt.getEffectiveColor());
		g2d.drawChars(txt.getText().toCharArray(), txt.getOffset(), txt.getPrintLength(), (int)txt.getTextX(), (int)txt.getTextY());
	}
}
