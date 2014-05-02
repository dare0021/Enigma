package canvasItems;

/**
 * Contains the data on what to target 
 */
public class CTargetAgent {
	public final String name, group;
	public final int which;

	public CTargetAgent(String _name, String _group, int selflag){
		name = _name;
		group = _group;
		which = selflag;
	}
}
