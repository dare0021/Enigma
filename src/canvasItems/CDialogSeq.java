package canvasItems;

/**
 * Works as a CDialogBox factory and as a
 * Liaison between the CDialogBoxes and the logic class.
 * It is recommended that a separate logic class be created
 * for each dialog tree.
 * May only contain one child (the root CDialogBox)
 */
public class CDialogSeq extends CGroupNode {
	private CDialogBox children;
	public CDialogBox currentNode;
	/** Override to be used if currentNode lacks one. */
	public CStaticImage bg, frame;
	public CTextDef txtdef;

	/** rootNode may be null */
	public CDialogSeq(String _name, CTextDef def) {
		super(_name);
		currentNode = children = null;
		txtdef = def;
	}
	
	public CDialogBox createNewDialog(String name, String text){
		CDialogBox out = new CDialogBox(name, this);
		out.setPrev(getLast());
		getLast().setNext(out);
		return out;
	}
	
	public boolean removeDialog(String name){
		CDialogBox iter = getRoot();
		while(!iter.getName().equals(name)){
			if(iter.getNext() == null)
				return false;
			iter = iter.getNext();
		}
		iter.getPrev().setNext(iter.getNext());
		iter.getNext().setPrev(iter.getPrev());
		return true;
	}
	
	public CDialogBox getRoot(){
		return children;
	}public void setRoot(CDialogBox root){
		children = root;
		if(currentNode == null)
			currentNode = root;
	}
	
	/** Returns the root node */
	public CDialogBox getFirst(){
		return getRoot();
	}
	public CDialogBox getLast(){
		CDialogBox iter = getRoot();
		while(iter.getNext() != null){
			iter = iter.getNext();
		}
		return iter;
	}

	/**
	 * Replaces currentNode with currentNode.next
	 */
	public void stepNext(){
		currentNode = currentNode.getNext();
	}/** Performs stepNext int times.
	   * Negative values are ignored. */
	public void stepNext(int i){
		for(int j=0; j<i; j++)
			stepNext();
	}

	/**
	 * Replaces currentNode with currentNode.prev
	 */
	public void stepPrev(){
		currentNode = currentNode.getPrev();
	}/** Performs stepPrev int times.
	   * Negative values are ignored. */
	public void stepPrev(int i){
		for(int j=0; j<i; j++)
			stepPrev();
	}

	/**
	 * Selects the ith option assuming currentNode is CDialogOpt
	 */
	public void stepSelect(int i){
		if(!(currentNode instanceof CDialogOpt)){
			System.out.println("WARN: currentNode is not a CDialogOpt");
			new Exception().printStackTrace();
			return;
		}CDialogOpt node = (CDialogOpt)currentNode;
		if(node.results.size() <= i){
			System.out.println("WARN: currentNode.results is of size "+node.results.size()+", which is less than "+i);
			new Exception().printStackTrace();
			return;
		}
		currentNode = node.results.get(i);
	}
}
