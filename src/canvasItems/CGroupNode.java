package canvasItems;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Serves to provide a structure, thus facilitating the implementation of
 * depth, grouping, search, scene management, etc
 * 
 * The group's location is that of its AABB
 * 
 * All behavior is in BFS
 */
public class CGroupNode extends ACItem {
	private ArrayList<ACItem> children;
	private int size;
	
	public CGroupNode(String _name) {
		super(_name);
		children = new ArrayList<ACItem>();
		size = 0;
	}
	
	public void addChild(ACItem node, boolean refreshOnExit){
		children.add(node);
		node.setParent(this);
		if(refreshOnExit){
			recount();
			Collections.sort(children);
		}
	}public void addChild(ACItem node){
		addChild(node, true);
	}public void addChildren(ACItem ... nodes){
		for (ACItem node : nodes)
			this.addChild(node, false);
		recount();
	}public void addChildren(ArrayList<ACItem> nodes){
		for (ACItem node : nodes)
			this.addChild(node, false);
		recount();
	}
	
	public ACItem findChild(ACItem toFind){
		for(ACItem iter : children)
			if(iter.equals(toFind))
				return iter;
		for(ACItem item : children)
			if(item instanceof CGroupNode)
				if(((CGroupNode)item).findChild(toFind) != null)
					return ((CGroupNode)item).findChild(toFind);
		return null;
	}public ACItem findChild(String name){
		for(ACItem iter : children)
			if(iter.getName().equals(name))
				return iter;
		for(ACItem item : children)
			if(item instanceof CGroupNode)
				if(((CGroupNode)item).findChild(name) != null)
					return ((CGroupNode)item).findChild(name);
		return null;
	}
	
	/**
	 * Prevents addition to the list without sorting afterwards
	 */
	public ArrayList<ACItem> getChildrenCopy(){return new ArrayList<ACItem>(children);}
	
	public int getStoredSize(){return size;}
	
	//Separation prevents infinite loop and increases performance
	public void recount(){
		int out = 0;
		for(ACItem iter : children){
			if(iter instanceof CGroupNode)
				out += ((CGroupNode) iter).getStoredSize();
			else
				out++;
		}
		size = out;
		if(this.getParent() != null)
			this.getParent().recount();
	}
	
	public boolean removeChild(ACItem toRemove){
		for(ACItem item : children)
			if(item.equals(toRemove)){
				item.setParent(null);
				recount();
				return children.remove(toRemove);
			}
		for(ACItem item : children)
			if(item instanceof CGroupNode && ((CGroupNode)item).removeChild(toRemove)){
				recount();
				return true;
			}
		recount();
		return false;
	}public boolean removeChild(String name){
		return removeChild(findChild(name));
	}
	/**
	 * Returns the number of items successfully removed
	 */
	public int removeChild(ACItem ... items){
		int out = 0;
		for(ACItem item : items){
			if(children.indexOf(item) > -1){
				children.get(children.indexOf(item)).setParent(null);
				children.remove(children.indexOf(item));
				out++;
			}
		}
		recount();
		return out;
	}public int removeChild(ArrayList<ACItem> items){
		int out = 0;
		for(ACItem item : items){
			if(children.indexOf(item) > -1){
				children.get(children.indexOf(item)).setParent(null);
				children.remove(children.indexOf(item));
				out++;
			}
		}
		recount();
		return out;
	}
	
	@Override
	public double getX(){
		double out = Double.MAX_VALUE;
		for (ACItem iter : children)
			if(iter.getX() < out)
				out = iter.getX();
		return out;
	}
	
	@Override
	public double getY(){
		double out = Double.MAX_VALUE;
		for (ACItem iter : children)
			if(iter.getY() < out)
				out = iter.getX();
		return out;
	}
	
	@Override
	/**
	 * Note that this is used for sorting as well
	 */
	public double getDepth(){
		double out = Double.MAX_VALUE;
		for (ACItem iter : children)
			if(iter.getDepth() < out)
				out = iter.getDepth();
		return out;
	}
	
	@Override
	public void setX(double x){
		double dx = x - this.getX();
		for (ACItem iter : children){
			iter.setX(iter.getX()+dx);
		}
	}
	
	@Override
	public void setY(double y){
		double dy = y - this.getY();
		for (ACItem iter : children){
			iter.setY(iter.getY()+dy);
		}
	}
	
	@Override
	public void setDepth(double z){
		double dz = z - this.getDepth();
		for (ACItem iter : children){
			iter.setDepth(iter.getDepth()+dz);
		}
	}
	
	@Override
	public void moveForward(){
		for (ACItem iter : children){
			iter.moveForward();
		}
	}
	
	@Override
	public void moveBackward(){
		for (ACItem iter : children){
			iter.moveBackward();
		}
	}
	
	@Override
	public void moveRelative(double dx, double dy){
		for (ACItem iter : children){
			iter.moveRelative(dx, dy);
		}
	}
	
	@Override
	public void moveTo(double tx, double ty){
		this.setX(tx);
		this.setY(ty);
	}
	
	/**
	 * Returns the opacity after accounting for the parents' opacities
	 */
	public double getEffectiveOpacity(){
		if(parent != null)
			return getOpacity()*parent.getEffectiveOpacity();
		else
			return getOpacity();
	}
}
