package canvasItems;

public enum ECItemType implements IStringsCanvas{
	ANIMATION("ANIMATION_DESC"),
	BUTTON("BUTTON_DESC"),
	CIRCLE("CIRCLE_DESC"),
	JIFIMAGE("JIFIMAGE_DESC"),
	LINE("LINE_DESC"),
	RECT("RECT_DESC"),
	ROUNDRECT("ROUNDRECT_DESC"),
	STATICIMAGE("STATICIMAGE_DESC"),
	TEXT("TEXT_DESC");
	
	private final String desc;
	
	ECItemType(String type){
		this.desc = type;
	}
	
	public String getDesc(){return this.desc;}
	
	/**
	 * Checks if this enum is a (list of descriptors)
	 * Is case sensitive
	 */
	public boolean isA(String ... args){
		for (String arg : args){
			if(this.getDesc().equals(arg))
				return true;
		}
		return false;
	}public boolean isA(ECItemType ... args){
		String[] descs = new String[args.length];
		for(int i=0; i<args.length; i++){
			descs[i] = args[i].getDesc();
		}
		return isA(descs);
	}
	
	public boolean isShape(){
		return isA("CIRCLE_DESC", "LINE_DESC", "RECT_DESC", "ROUNDRECT_DESC");
	}
	public boolean isImage(){
		return isA("JIFIMAGE_DESC", "STATICIMAGE_DESC");
	}
	public boolean isUI(){
		return isA("BUTTON_DESC");
	}
}
