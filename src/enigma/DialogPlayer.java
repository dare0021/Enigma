package enigma;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.Timer;

import jld.JLD;
import canvasItems.ACItem;
import canvasItems.CDialogBox;
import canvasItems.CDialogSeq;
import canvasItems.CGroupNode;
import canvasItems.CStaticImage;
import canvasItems.CanvasFactory;

/**
 * Uses CDialogSeq information to display their information
 * as interactive GUI elements.
 */
public class DialogPlayer implements IConstantsUI, ActionListener {
	public GUILogic parent;
	public CDialogSeq currentSeq;
	public CGroupNode dispCache;
	public String prev, next;
	public double fadeAcc;
	public int fadeLen;
	
	private boolean canrun;
	private Timer fadeTimer;
	private CGroupNode toDelete;
	private final String fadeGroupName;
	private final String bgsuffix;
	private final String charsuffix;
	private final String framesuffix;
	
	public DialogPlayer(GUILogic _parent){
		parent = _parent;
		String time = "_"+System.currentTimeMillis(); //prevent accidental fading but no need for security
		fadeGroupName = "fadeGroup" + time;
		bgsuffix = "_bg" + time;
		charsuffix = "_character" + time;
		framesuffix = "_frame" + time;
		canrun = false;
		toDelete = dispCache = null;
		prev = next = null;
		fadeLen = 300;
		fadeAcc = 1;
		fadeTimer = new Timer(fadeLen, this);
	}
	
	public CDialogSeq init(JLD input){
		CDialogSeq seq = null;
		CanvasFactory cf = new CanvasFactory();
		cf.x = cf.y = 0;
		cf.width = cf.height = -1;
		{
			String name = (String)input.get("name");
			
			String font = (String)input.get("font");
			int size    = Integer.parseInt((String)input.get("size"));
			int style   = JLD.getAsInt((String)input.get("style"), 0);
			cf.font = new Font(font, style, size);
			
			cf.strokeThickness = JLD.getAsInt((String)input.get("thickness"), 2);
			
			cf.fillColor = JLD.getAsColor((ArrayList<String>)input.get("fill"), Color.WHITE);
			cf.strokeColor = JLD.getAsColor((ArrayList<String>)input.get("stroke"), Color.BLACK);
			
			seq = new CDialogSeq(name, cf.createTextDef());
			
			String temp = (String)input.get("bg");
			if(temp != null){
				cf.url = temp;
				seq.bg = new CStaticImage(cf.createImageDef(), name + bgsuffix);
			}
			
			temp = (String)input.get("frame");
			if(temp != null){
				cf.url = temp;
				seq.frame = new CStaticImage(cf.createImageDef(), name + framesuffix);
			}
		}
		
		ArrayList<LinkedHashMap<String, String>> tmparr = (ArrayList<LinkedHashMap<String, String>>)input.get("boxes");
		if(tmparr == null || tmparr.size() < 1){
			System.out.println("ERR: no dialog boxes found for DialogPlayer.parse");
			return null;
		}
		prev = (String)input.get("prev");
		next = (String)input.get("next");
		CDialogBox lastBox;
		{//First element
			LinkedHashMap<String, String> hm = tmparr.get(0);
			String tmpstr = hm.get("name");
			if(tmpstr == null)
				tmpstr = seq.getName() + 0;
			CDialogBox box = new CDialogBox(tmpstr, seq);
			seq.setRoot(box);
			
			tmpstr = hm.get("content");
			if(tmpstr == null)
				tmpstr = "";
			box.setContent(tmpstr);

			tmpstr = hm.get("charName");
			if(tmpstr == null)
				tmpstr = "";
			box.setCharacterName(tmpstr);

			box.setCharacterHeight(JLD.getAsDouble(hm.get("charHeight"), 0));
			box.setFrameHeight(JLD.getAsDouble(hm.get("frameHeight"), DIALOG_FRAME_HEIGHT));
			box.setCharNameOffset(	JLD.getAsDouble(hm.get("charNameOffsetX"), 0),
									JLD.getAsDouble(hm.get("charNameOffsetY"), 0));
			box.setContentOffset(	JLD.getAsDouble(hm.get("contentOffsetX"), 0),
									JLD.getAsDouble(hm.get("contentOffsetY"), 0));
			
			String temp = hm.get("bg");
			if(temp != null){
				cf.url = temp;
				box.setBG(new CStaticImage(cf.createImageDef(), box.getName() + bgsuffix));
			}
			
			temp = hm.get("frame");
			if(temp != null){
				cf.url = temp;
				box.setFrame(new CStaticImage(cf.createImageDef(), box.getName() + framesuffix));
			}
			
			temp = hm.get("character");
			if(temp != null){
				cf.url = temp;
				box.setCharacterImage(new CStaticImage(cf.createImageDef(), box.getName() + charsuffix));
			}
			lastBox = box;
		}
		for(int i=1; i<tmparr.size(); i++){
			LinkedHashMap<String, String> hm = tmparr.get(i);
			String tmpstr = hm.get("name");
			if(tmpstr == null)
				tmpstr = seq.getName() + i;
			CDialogBox box = new CDialogBox(tmpstr, seq);
			box.setPrev(seq.currentNode);
			seq.currentNode.setNext(box);
			
			tmpstr = hm.get("content");
			if(tmpstr == null)
				tmpstr = "";
			box.setContent(tmpstr);

			tmpstr = hm.get("charName");
			if(tmpstr == null)
				tmpstr = lastBox.getCharacterName();
			box.setCharacterName(tmpstr);

			box.setCharacterHeight(JLD.getAsDouble(hm.get("charHeight"), lastBox.getCharacterHeight()));
			box.setFrameHeight(JLD.getAsDouble(hm.get("frameHeight"), lastBox.getFrameHeight()));
			box.setCharNameOffset(	JLD.getAsDouble(hm.get("charNameOffsetX"), lastBox.getCharNameOffsetX()),
									JLD.getAsDouble(hm.get("charNameOffsetY"), lastBox.getCharNameOffsetY()));
			box.setContentOffset(	JLD.getAsDouble(hm.get("contentOffsetX"), lastBox.getContentOffsetX()),
									JLD.getAsDouble(hm.get("contentOffsetY"), lastBox.getContentOffsetY()));
			
			String temp = hm.get("bg");
			if(temp != null && !temp.isEmpty()){
				cf.url = temp;
				box.setBG(new CStaticImage(cf.createImageDef(), box.getName() + bgsuffix));
			}else{
				box.setBG(lastBox.getBG());
			}
			
			temp = hm.get("frame");
			if(temp != null && !temp.isEmpty()){
				cf.url = temp;
				box.setFrame(new CStaticImage(cf.createImageDef(), box.getName() + framesuffix));
			}else{
				box.setFrame(lastBox.getFrame());
			}
			
			temp = hm.get("character");
			if(temp != null && !temp.isEmpty()){
				cf.url = temp;
				box.setCharacterImage(new CStaticImage(cf.createImageDef(), box.getName() + charsuffix));
			}else{
				box.setCharacterImage(lastBox.getCharacterImage());
			}
			
			seq.stepNext();
			lastBox = box;
		}
		seq.currentNode = seq.getRoot();
		currentSeq = seq;
		canrun = true;
		System.out.println(seq.currentNode.getName() + " " + seq.currentNode.getNext().getName());
		return seq;
	}
	
	/**
	 * Will be ignored if the new sequence and box names match
	 * with the cached version's, regardless of their contents' change.
	 */
	public void refresh(){
		if(!canrun)
			return;
		CGroupNode newDelete = dispCache;
		CGroupNode newDisp = Renderer.paintDialog(currentSeq, parent.gui.getBounds());
		CGroupNode fadeGroup = new CGroupNode(fadeGroupName);
		for(ACItem item : newDisp.getChildrenCopy()){
			if(!(item.getName().endsWith(bgsuffix) || item.getName().endsWith(framesuffix) || item.getName().endsWith(charsuffix))){
				fadeGroup.addChild(item);
				newDisp.removeChild(item);
			}
		}
		fadeGroup.setOpacity(0);
		newDisp.addChild(fadeGroup);
		if(newDelete != null && newDisp.getName().equals(newDelete.getName())){
			return;
		}
		dispCache = newDisp;
		parent.gui.getMasterNode().addChild(dispCache);
		parent.accFading(fadeGroup, 1, fadeLen, fadeAcc);
		if(newDelete != null){
			parent.accFading(newDelete, -1, fadeLen, fadeAcc);
			if(toDelete != null)
				actionPerformed(null);
			toDelete = newDelete;
			fadeTimer.start();
		}
	}
	
	public void stepNext(){
		if(!canrun)
			return;
		if(currentSeq.currentNode.getNext() != null){
			currentSeq.stepNext();
			refresh();
		}
	}
	
	public void stepPrev(){
		if(!canrun)
			return;
		if(currentSeq.currentNode.getPrev() != null){
			currentSeq.stepPrev();
			refresh();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		parent.gui.getMasterNode().removeChild(toDelete);
		fadeTimer.stop();
	}
}
