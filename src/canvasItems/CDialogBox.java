package canvasItems;

import enigma.IConstantsUI;

/**
 * Forms an instance in a dialog sequence
 * Composed of a text, text background/border, name,
 * background, speaker picture, speaker name, speaker height, and
 * also allows other ACItems.
 */
public class CDialogBox extends CGroupNode {
	private final CDialogSeq parent;
	private CDialogBox prev, next;
	protected CStaticImage bg, frame;
	private CStaticImage character;
	private String content, characterName;
	/** Additional height beneath the image */
	private double characterHeight, frameHeight;
	/** Relative offset off of frame's coords */
	private double charNameOffsetX, charNameOffsetY, contentOffsetX, contentOffsetY;

	public CDialogBox(String _name, CDialogSeq _parent) {
		super(_name);
		parent = _parent;
		prev = next = null;
		bg = frame = character = null;
		content = characterName = _name + " is not initialized";
		charNameOffsetX = charNameOffsetY = contentOffsetX = contentOffsetY = characterHeight = 0;
		frameHeight = DIALOG_FRAME_HEIGHT;
	}
	/** 
	  * Creates a copy of the CDialogBox with the exception of prev and next.
	  * Note: This is not a deep copy.
	  */
	public CDialogBox(String _name, CDialogSeq _parent, CDialogBox orig) {
		super(_name);
		parent = _parent;
		prev = next = null;
		bg = orig.bg;
		frame = orig.frame;
		character = orig.getCharacterImage();
		content = orig.getContent();
		characterName = orig.getCharacterName();
		characterHeight = orig.getCharacterHeight();
		frameHeight = orig.getFrameHeight();
		charNameOffsetX = orig.getCharNameOffsetX();
		charNameOffsetY = orig.getCharNameOffsetY();
		contentOffsetX = orig.getContentOffsetX();
		contentOffsetY = orig.getContentOffsetY();
	}
	
	@Override
	public CDialogSeq getParent(){
		return parent;
	}

	public void setNext(CDialogBox _next){
		next = _next;
	}public CDialogBox getNext(){
		return next;
	}public void setPrev(CDialogBox _prev){
		prev = _prev;
	}public CDialogBox getPrev(){
		return prev;
	}
	
	public void setBG(CStaticImage _bg){
		bg = _bg;
	}public CStaticImage getBG(){
		if(bg != null)
			return bg;
		return parent.bg;
	}public void setFrame(CStaticImage _frame){
		frame = _frame;
	}public CStaticImage getFrame(){
		if(frame != null)
			return frame;
		return parent.frame;
	}public void setCharacterImage(CStaticImage cha){
		character = cha;
	}public CStaticImage getCharacterImage(){
		return character;
	}
	
	public void setContent(String text){
		content = text;
	}public String getContent(){
		return content;
	}public void setCharacterName(String _name){
		characterName = _name;
	}public String getCharacterName(){
		return characterName;
	}
	
	public void setCharacterHeight(double h){
		characterHeight = h;
	}public double getCharacterHeight(){
		return characterHeight;
	}public void setFrameHeight(double fh){
		frameHeight = fh;
	}public double getFrameHeight(){
		return frameHeight;
	}

	public void setCharNameOffset(double x, double y){
		charNameOffsetX = x;
		charNameOffsetY = y;
	}public void setCharNameOffsetX(double cnox){
		charNameOffsetX = cnox;
	}public double getCharNameOffsetX(){
		return charNameOffsetX;
	}public void setCharNameOffsetY(double cnoy){
		charNameOffsetY = cnoy;
	}public double getCharNameOffsetY(){
		return charNameOffsetY;
	}
	public void setContentOffset(double x, double y){
		contentOffsetX = x;
		contentOffsetY = y;
	}public void setContentOffsetX(double cox){
		contentOffsetX = cox;
	}public double getContentOffsetX(){
		return contentOffsetX;
	}public void setContentOffsetY(double coy){
		contentOffsetY = coy;
	}public double getContentOffsetY(){
		return contentOffsetY;
	}
}
