package enigma;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
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
			if(node instanceof CBasicText){
				paintText((CBasicText) node);
			}else if(node instanceof ACShape){
				paintShape((ACShape) node);
			}else if(node instanceof CJifImage){ //must come before CStaticImage due to inheritance
				if(((CJifImage) node).toBeRemoved())
					deleteOnExit.add(node);
				else
					paintJifImage((CJifImage) node);
			}else if(node instanceof CStaticImage){
				paintStaticImage((CStaticImage) node);
			}else if(node instanceof CButton){
				paintButton((CButton) node);
			}else if(node instanceof CTextBox){
				paintTextBox((CTextBox) node);
			}else if(node instanceof CDialogSeq || node instanceof CDialogBox){
				System.out.println("WARN: Renderer.run found a data storage type "+node.getType().getDesc()); //Data storage classes
				System.out.println("Use the proper container to display the data stored here");
				new Exception().printStackTrace();
			}else if(node instanceof CGroupNode){
				runBeforeExit.add((CGroupNode) node); //run last to prevent contaminating groupOpacity global variable
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
	
	private void paintText(CBasicText label){
		g2d.setFont(label.font);
		int i=0;
		for(String str : label.text.substring(label.offset, label.len).split("\n")){
			paintLineOfText(label, str, label.lineSeparation*i);
			i++;
		}
	}
	
	private void paintLineOfText(CBasicText label, String text, double yoffset){
		int y = (int)(label.getY()+yoffset);
		if(label.strokeThickness > 0){
			g2d.setColor(label.getEffectiveStroke(groupOpacity));
			for(int i=0; i<label.strokeThickness; i++){
				g2d.drawString(text, (int)label.getX()+i, y);
				g2d.drawString(text, (int)label.getX(), y+i);
				g2d.drawString(text, (int)label.getX()-i, y);
				g2d.drawString(text, (int)label.getX(), y-i);
				g2d.drawString(text, (int)label.getX()+i, y+i);
				g2d.drawString(text, (int)label.getX()+i, y-i);
				g2d.drawString(text, (int)label.getX()-i, y-i);
				g2d.drawString(text, (int)label.getX()-i, y+i);
			}
		}
		g2d.setColor(label.getEffectiveFill(groupOpacity));
		g2d.drawString(text, (int)label.getX(), y);
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
		if(img.getRawWidth() < 0 && img.getRawHeight() < 0){
			g2d.drawImage(img.image, (int)img.getX(), (int)img.getY(), null);
			System.out.println(img.name);
			return;
		}
		g2d.drawImage(img.image, (int)img.getX(), (int)img.getY(), (int)(img.getEffectiveX1()), (int)(img.getEffectiveY1()), 0, 0, (int)img.getRawWidth(), (int)img.getRawHeight(), null);
	}
	
	private void paintJifImage(CJifImage img){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (img.getOpacity()*groupOpacity)));
		if(img.getRawWidth() < 0 && img.getRawHeight() < 0){
			g2d.drawImage(img.image, (int)img.getX(), (int)img.getY(), null);
			return;
		}
		g2d.drawImage(img.image, (int)img.getX(), (int)img.getY(), (int)(img.getEffectiveX1()), (int)(img.getEffectiveY1()), 0, 0, (int)img.getRawWidth(), (int)img.getRawHeight(), null);
	}
	
	/**
	 * Also handles text buttons
	 */
	private void paintButton(CButton btn){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (btn.getOpacity()*groupOpacity)));
		Image image = btn.getImage().image;
		if(btn instanceof CTextButton){
			if(image != null)
				g2d.drawImage(btn.getImage().image, (int)btn.getX(), (int)btn.getY(), null);
			paintText(((CTextButton)btn).getTextObject());
		}else{
			g2d.drawImage(btn.getImage().image, (int)btn.getX(), (int)btn.getY(), null);
		}
	}
	
	private void paintTextBox(CTextBox txt){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (txt.getOpacity()*groupOpacity)));
		g2d.drawImage(txt.getImage().image, (int)txt.getX(), (int)txt.getY(), null);
		
		g2d.setFont(txt.getFont());
		g2d.setColor(txt.getEffectiveColor());
		g2d.drawChars(txt.getText().toCharArray(), txt.getOffset(), txt.getPrintLength(), (int)txt.getTextX(), (int)txt.getTextY());
	}
	
	/**
	 * Uses the given CDialogSeq to paint the current node.
	 * CButton registration for CDialogOpt is not done here,
	 * and should be done explicitly.
	 * The same goes for moving to the next dialog box.
	 * 
	 * CGroupNode is returned instead of being painted directly. 
	 *
	 * Constituent coordinate information are discarded.
	 */
	public static CGroupNode paintDialog(CDialogSeq cds, Rectangle screen){
		if(cds == null){
			System.out.println("ERR: CDialogSeq is null");
			new Exception().printStackTrace();
			return null;
		}else if(cds.currentNode == null){
			System.out.println("ERR: CDialogSeq.currentNode is null");
			new Exception().printStackTrace();
			return null;
		}
		CDialogBox box = cds.currentNode;
		int screenx0, screeny0, screeny1; //screenx1, 
		{
 			screenx0 = screen.x;
//			screenx1 = screen.x + screen.width;
			screeny0 = screen.y;
			screeny1 = screen.y + screen.height;
		}
		ArrayList<ACItem> items = new ArrayList<ACItem>();
		int z = 0;
		int z_increment = DEPTH_FROM_SCREEN ? 1 : -1; 

		CStaticImage img = box.getBG();
		if(img != null){
			if(img.getRawHeight() < 0 && img.getRawWidth() < 0){
				System.out.println("WARN: CDialogBox bg has no height info");
				new Exception().printStackTrace();
				img.moveTo(screenx0 + img.getX(), screeny0);
			}else{	
				img.moveTo(screenx0, screeny1 - img.getEffectiveHeight());
			}
			img.negativeScaleNormalize();
			img.setDepth(z += z_increment);
			items.add(img);
		}
		
		img = box.getCharacterImage();
		if(img != null){
			if(img.getRawHeight() < 0 && img.getRawWidth() < 0){
				System.out.println("WARN: CDialogBox char has no height info");
				new Exception().printStackTrace();
				img.moveTo(screenx0, screeny0);
			}else{	
				img.moveTo(screenx0, screeny1 - img.getEffectiveHeight() - box.getCharacterHeight());
			}
			img.negativeScaleNormalize();
			img.setDepth(z += z_increment);
			items.add(img);
		}
		
		img = box.getFrame();
		double framedx = 0 , framedy = 0;
		if(img != null){
			if(img.getRawHeight() < 0 && img.getRawWidth() < 0){
				System.out.println("WARN: CDialogBox frame has no height info");
				new Exception().printStackTrace();
				img.moveTo(screenx0, screeny0);
			}else{
				img.moveTo(screenx0, screeny1 - img.getEffectiveHeight() - box.getFrameHeight());
			}
			img.negativeScaleNormalize();
			img.setDepth(z += z_increment);
			items.add(img);
			framedx = img.getX();
			framedy = img.getY();
		}

		cds.txtdef.text = box.getCharacterName();
		CBasicText txt = new CBasicText(cds.txtdef, box.getCharacterName());
		txt.moveTo(framedx + box.getCharNameOffsetX(), framedy + box.getCharNameOffsetY());
		txt.setDepth(z += z_increment);
		items.add(txt);

		cds.txtdef.text = box.getContent();
		txt = new CBasicText(cds.txtdef, box.getContent());
		txt.moveTo(framedx + box.getContentOffsetX(), framedy + box.getContentOffsetY());
		txt.setDepth(z += z_increment);
		items.add(txt);
		
		CGroupNode drawGroup = new CGroupNode(cds.getName() + "->" + box.getName());
		drawGroup.setOpacity(cds.getEffectiveOpacity());
		drawGroup.addChildren(items);
		if(box instanceof CDialogOpt){
			CGroupNode btns = new CGroupNode(cds.getName()+"_buttons");
			paintDialogOpt((CDialogOpt) box, btns, screen);
			drawGroup.addChild(btns);
		}
		return drawGroup;
	}
	
	private static void paintDialogOpt(CDialogOpt cdo, CGroupNode drawGroup, Rectangle screen){
		if(cdo == null){
			System.out.println("ERR: CDialogOpt is null");
			new Exception().printStackTrace();
			return;
		}else if(cdo.btns.size() < 1){
			System.out.println("WARN: CDialogOpt is empty");
			new Exception().printStackTrace();
			return;
		}
		ArrayList<ACItem> items = new ArrayList<ACItem>();
		for(int i=0; i<cdo.btns.size(); i++){
			CButton btn = cdo.btns.get(i);
			btn.setX((screen.width-btn.getApparentWidth())/2 + screen.x);
			btn.setY((screen.height-cdo.btns.get(cdo.btns.size()-1).getApparentHeight()-cdo.getOptionHeight())/(cdo.btns.size()-1) * i + screen.y);
			items.add(btn);
		}
		drawGroup.addChildren(items);
	}
}
