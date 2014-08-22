package enigma;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.Timer;

import jld.JLD;
import jld.JldGetter;
import canvasItems.ACItem;
import canvasItems.CButton;
import canvasItems.CDialogBox;
import canvasItems.CDialogOpt;
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
	public double fadeAcc;
	public int fadeLen;
	
	private boolean canrun;
	private Timer fadeTimer;
	private CGroupNode toDelete;
	/** Saves the state the last seq was in to avoid starting the seq from the beginning when backing in to it */
	private CDialogBox savedState;
	private final String time;
	private final String fadeGroupName;
	private final String bgsuffix;
	private final String charsuffix;
	private final String framesuffix;
	
	public DialogPlayer(GUILogic _parent){
		parent = _parent;
		time = "_"+System.currentTimeMillis(); //prevent accidental fading but no need for security
		fadeGroupName = "fadeGroup" + time;
		bgsuffix = "_bg" + time;
		charsuffix = "_character" + time;
		framesuffix = "_frame" + time;
		canrun = false;
		savedState = null; 
		toDelete = dispCache = null;
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
		
		ArrayList<LinkedHashMap<String, Object>> tmparr = (ArrayList<LinkedHashMap<String, Object>>)input.get("boxes");
		if(tmparr == null || tmparr.size() < 1){
			System.out.println("ERR: no dialog boxes found for DialogPlayer.parse");
			return null;
		}
		ArrayList<CDialogBox> boxes = new ArrayList<CDialogBox>();//leave interconnection till last
		CDialogBox lastBox = null;
		for(int i=0; i<tmparr.size(); i++){
			lastBox = parseBox(i, tmparr.get(i), seq, lastBox, cf);
			boxes.add(lastBox);
		}
		seq.setRoot(boxes.get(0));
		
		//piece together references
		for(int i=0; i<boxes.size(); i++){
			CDialogBox box = boxes.get(i);
			if(box.getPrev() == null && i > 0)
				box.setPrev(boxes.get(i-1));
			else if(box.getPrev() != null && box.getPrev().getName().isEmpty())
				box.setPrev(null);
			else if(box.getPrev() != null)
				box.setPrev(find(boxes, box.getPrev().getName()));
			if(box instanceof CDialogOpt){
				ArrayList<CDialogBox> arrtmp = new ArrayList<CDialogBox>();
				for(CDialogBox res : ((CDialogOpt)box).results){
					arrtmp.add(find(boxes, res.getName()));
				}
				((CDialogOpt)box).results = arrtmp;
			}else{
				if(box.getNext() == null && i < boxes.size()-1)
					box.setNext(boxes.get(i+1)); 
				else if(box.getNext() != null && box.getNext().getName().isEmpty())
					box.setNext(null);
				else if(box.getNext() != null)
					box.setNext(find(boxes, box.getNext().getName()));
			}
		}

		currentSeq = seq;
		canrun = true;
		return seq;
	}
	
	/**
	 * @param i 		Used for default name creation
	 * @param hm		Raw data
	 * @param seq		Parent
	 * @param cdbref	Used for default values
	 * @param cf		For persistent parameter sharing
	 * @return			Parsed CDialogBox
	 */
	private CDialogBox parseBox(int i, LinkedHashMap<String, Object> hm, CDialogSeq seq, CDialogBox cdbref, CanvasFactory cf){
		if(cdbref == null)
			cdbref = new CDialogBox(null, new CDialogSeq(null, null));
		String tmpstr = (String) hm.get("name");
		if(tmpstr == null)
			tmpstr = seq.getName() + i;
		CDialogBox box = new CDialogBox(tmpstr, seq);
		
		tmpstr = (String) hm.get("content");
		if(tmpstr == null)
			tmpstr = "";
		box.setContent(JLD.parseControlChars(tmpstr));

		tmpstr = (String) hm.get("charName");
		if(tmpstr == null)
			tmpstr = cdbref.getCharacterName();
		box.setCharacterName(JLD.parseControlChars(tmpstr));

		box.setCharacterHeight(	JLD.getAsDouble((String) hm.get("charHeight"), cdbref.getCharacterHeight()));
		box.setFrameHeight(		JLD.getAsDouble((String) hm.get("frameHeight"), cdbref.getFrameHeight()));
		box.setCharNameOffset(	JLD.getAsDouble((String) hm.get("charNameOffsetX"), cdbref.getCharNameOffsetX()),
								JLD.getAsDouble((String) hm.get("charNameOffsetY"), cdbref.getCharNameOffsetY()));
		box.setContentOffset(	JLD.getAsDouble((String) hm.get("contentOffsetX"), cdbref.getContentOffsetX()),
								JLD.getAsDouble((String) hm.get("contentOffsetY"), cdbref.getContentOffsetY()));
		
		tmpstr = (String) hm.get("bg");
		if(tmpstr != null && !tmpstr.isEmpty()){
			cf.url = tmpstr;
			box.setBG(new CStaticImage(cf.createImageDef(), box.getName() + bgsuffix));
		}else{
			box.setBG(cdbref.getBG());
		}
		
		tmpstr = (String) hm.get("frame");
		if(tmpstr != null && !tmpstr.isEmpty()){
			cf.url = tmpstr;
			box.setFrame(new CStaticImage(cf.createImageDef(), box.getName() + framesuffix));
		}else{
			box.setFrame(cdbref.getFrame());
		}
		
		tmpstr = (String) hm.get("character");
		if(tmpstr != null && !tmpstr.isEmpty()){
			cf.url = tmpstr;
			box.setCharacterImage(new CStaticImage(cf.createImageDef(), box.getName() + charsuffix));
		}else{
			box.setCharacterImage(cdbref.getCharacterImage());
		}
		
		ArrayList<LinkedHashMap<String, String>> btnarr = (ArrayList<LinkedHashMap<String, String>>) hm.get("buttons"); 
		if(btnarr != null){
			box = new CDialogOpt(box.getName(), box.getParent(), box);
			((CDialogOpt)box).setOptionHeight(JLD.getAsDouble((String) hm.get("optionHeight"), 0));
			String lastBG = null;
			for(int j=0; j<btnarr.size(); j++){
				LinkedHashMap<String, String> btn = btnarr.get(j);
				String name = btn.get("name");
				if(name == null)
					name = box.getName() + "_" + j;
				cf.url = btn.get("bg");
				if(cf.url != null)
					lastBG = cf.url;
				else
					cf.url = lastBG;
				cf.bgimage = new CStaticImage(cf.createImageDef(), name + "_bg");
				tmpstr = btn.get("hover");
				if(tmpstr != null)
					cf.url = tmpstr;
				cf.hoverimage = new CStaticImage(cf.createImageDef(), name + "_hover");
				tmpstr = btn.get("click");
				if(tmpstr != null)
					cf.url = tmpstr;
				cf.clickimage = new CStaticImage(cf.createImageDef(), name + "_click");
				tmpstr = btn.get("command");
				if(tmpstr == null)
					tmpstr = name;
				((CDialogOpt)box).btns.add(new CButton(cf.createButtonDef(), name, tmpstr));
			}
			ArrayList<String> resarr = (ArrayList<String>) hm.get("results");
			if(resarr != null){
				if(!(box instanceof CDialogOpt))
					box = new CDialogOpt(box.getName(), box.getParent(), box);
				for(int j=0; j<resarr.size(); j++){
					((CDialogOpt)box).results.add(new CDialogBox(resarr.get(j), null)); //temp data
				}
			}
		}

		tmpstr = (String) hm.get("prev");
		if(tmpstr != null)
			box.setPrev(new CDialogBox(tmpstr, null)); //temp data
		tmpstr = (String) hm.get("next");
		if(tmpstr != null){
			box.setNext(new CDialogBox(tmpstr, null)); //temp data
		}
				
		box.setPrevSeq((String) hm.get("prevSeq"));
		box.setNextSeq((String) hm.get("nextSeq"));
		return box;
	}
	
	private CDialogBox find(ArrayList<CDialogBox> arr, String name){
		for(CDialogBox box : arr){
			if(box.getName().equals(name))
				return box;
		}
		return null;
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
		parent.gui.refreshControls();
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
		}else if(currentSeq.currentNode.getNextSeq() != null){
			savedState = currentSeq.currentNode;
			JLD jld = new JldGetter().getValues(currentSeq.currentNode.getNextSeq());
			init(jld);
			refresh();
		}
	}
	
	public void stepPrev(){
		if(!canrun)
			return;
		if(currentSeq.currentNode.getPrev() != null){
			currentSeq.stepPrev();
			refresh();
		}else if(currentSeq.currentNode.getPrevSeq() != null){
			JLD jld = new JldGetter().getValues(currentSeq.currentNode.getPrevSeq());
			init(jld);
			currentSeq.currentNode = savedState;
			refresh();
		}
	}
	
	public void clickButton(String actionCommand){
		if(!canrun || !(currentSeq.currentNode instanceof CDialogOpt) || ((CDialogOpt)currentSeq.currentNode).results.size() < 1)
			return;
		CDialogOpt opt = (CDialogOpt)currentSeq.currentNode;
		if(opt.results.size() != opt.btns.size()){
			System.out.println("WARN: mismatching number of results and buttons " + opt.results.size() + " : " + opt.btns.size());
			new Exception().printStackTrace();
		}
		for(int i=0; i<opt.btns.size(); i++){
			CButton btn = opt.btns.get(i);
			if(btn.actionCommand.equals(actionCommand)){
				currentSeq.currentNode = opt.results.get(i);
				refresh();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		parent.gui.getMasterNode().removeChild(toDelete);
		fadeTimer.stop();
	}
	
	private void printBoxDetails(CDialogBox b){
		System.out.println("");
		System.out.println("name "+b.getName());
		System.out.println("address "+b);
		System.out.println("parent address "+b.getParent());
		System.out.println("prev address "+b.getPrev());
		System.out.println("next address "+b.getNext());
		if(b.getParent() != null){
			System.out.println("bg address "+b.getBG());
			System.out.println("frame address "+b.getFrame());
		}
		System.out.println("character address "+b.getCharacterImage());
		System.out.println("content "+b.getContent());
		System.out.println("charname "+b.getCharacterName());
		System.out.println("prevSeq "+b.getPrevSeq());
		System.out.println("nextSeq "+b.getNextSeq());
		System.out.println("charheight "+b.getCharacterHeight());
		System.out.println("frameheight "+b.getFrameHeight());
		System.out.println("charnamex "+b.getCharNameOffsetX());
		System.out.println("charnamey "+b.getCharNameOffsetY());
		System.out.println("contentx "+b.getContentOffsetX());
		System.out.println("contenty "+b.getContentOffsetY());
		System.out.println("");
	}
}
