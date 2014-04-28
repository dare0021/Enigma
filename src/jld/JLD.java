package jld;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 * The in-code representation of a JLD
 * Because .find()
 */
public class JLD{
	private HashMap<String, Object> hm = null;
	public JLD(){super();}
	public JLD(HashMap h){super(); hm = h;}
	
	public Object get(String raw){
		return hm.get(raw);
	}public void put(String key, Object val){
		hm.put(key, val);
	}public void clear(){
		hm.clear();
	}public String toString(){
		return hm.toString();
	}public Set<String> keySet(){
		return hm.keySet();
	}
	
	/**
	 * Evaluates the given string to extract a map value
	 * e.g. eval(hm, "e.g") is equal to hm.get("e").get("g")
	 *      eval(hm, "e:2") is equal to ((List)(hm.get("e")))(2)
	 * Does not do error handling. 
	 * 	If the key does not exist, null is returned.
	 * 		Null return value does not always mean key DNE, it could also mean the value is actually null.
	 * 	If the index is out of bounds, an exception occurs.
	 */
	public Object find(String raw){
		Object ot = hm;
		int i = 0;
		for(String st=""; i<raw.length(); i++){ //find first key
			if(raw.charAt(i) == '.' || raw.charAt(i) == ':' || i+2>raw.length()){ //key
				if(i+2>raw.length())
					st += raw.charAt(i);
				ot = ((HashMap)ot).get(st);
				break;
			}else{
				st += raw.charAt(i);
			}
		}
		boolean isKey = false; //if false, is Vector element
		for(String st=""; i<raw.length(); i++){
			if((!st.isEmpty() && (raw.charAt(i)=='.' || raw.charAt(i)==':')) || i+2>raw.length()){
				if(i+2>raw.length())
					st += raw.charAt(i);
				if(isKey){
					ot = ((HashMap)ot).get(st);
					st = "";
				}else{
					ot = ((Vector)ot).get(Integer.parseInt(st));
					st = "";
				}
			}
			if(raw.charAt(i) == '.'){
				isKey = true;
			}else if(raw.charAt(i) == ':'){
				isKey = false;
			}else{
				st += raw.charAt(i);
			}
		}
		return ot; 
	}
}
