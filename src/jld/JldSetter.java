package jld;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Vector;

/**
 * Saves a HashMap as a JLD file
 */
public class JldSetter {
	private final int spacePerTab = 3;
	public JldSetter(){}
	
	/**
	 * Public setter for the class
	 */
	public void setValues(String url, JLD raw){
		saveFile(url, parseMap(raw, 0));
	}
	
	/**
	 * Parses the given map in to human-readable strings
	 */
	private Vector<String> parseMap(JLD raw, int layer){
		Vector<String> out = new Vector<String>();
		String st;
		out.add(indent(layer) + "{");
		layer++;
		for(String key : raw.keySet()){
			st = indent(layer) + "\"" + key + "\" : ";
			Object val = raw.get(key);
			if(val instanceof Vector){
				st += parseVector((Vector)val, layer+1);
				st += ",";
			}else if(val instanceof HashMap){
				out.add(st);
				JLD jldt = new JLD((HashMap)val);
				Vector<String> nested = parseMap(jldt, layer+1);
				st = "";
				for(String s : nested){
					st += s;
				}
				out.add(st);
				st = out.lastElement() + ",";
				out.remove(out.size()-1);
			}else{
				st += "\"" + val + "\",";
			}
			out.add(st);
		}
		out.set(out.size()-1, out.lastElement().substring(0, out.lastElement().length()-1));
		for(int i=0; i<out.size(); i++){
			out.set(i,out.elementAt(i)+"\r\n");
		}
		layer--;
		out.add(indent(layer)+"}");
		return out;
	}
	
	private String parseVector(Vector raw, int layer){
		String out = "[";
		boolean first = true;
		for(Object val : raw){
			if(val instanceof Vector){
				if(!first)
					out += ", ";
				else
					first = false;
				out += parseVector((Vector)val, layer+1);
			}else if(val instanceof HashMap){
				if(!first)
					out += ",";
				else
					first = false;
				out += "\r\n";
				JLD jldt = new JLD((HashMap)val);
				Vector<String> nested = parseMap(jldt, layer+1);
				for(String s : nested){
					out += s;
				}
			}else{
				if(!first)
					out += ", ";
				else
					first = false;
				out += "\"" + val + "\"";
			}
		}
		return out+"]";
	}
	
	private void saveFile(String url, Vector<String> raw){
		File f = new File("./src/enigma/data/"+url); //TODO: find a real solution to this
		f.mkdirs();
		BufferedWriter bw = null;
		try {
			while(!f.createNewFile()){ //if already present
				f.delete();
				System.out.println("File "+url+" already present. Attempting deletion.");
			}
		} catch (IOException e) {
			System.out.println("Failed to create file(1, IOException): "+url);
			//e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
		} catch (FileNotFoundException e) {
			System.out.println("Failed to create file(2): "+url);
			//e.printStackTrace();
		}
		for(String s : raw){
			try {
				bw.write(s);
			} catch (IOException e) {
				System.out.println("Failed to write: "+s);
				System.out.println("to file "+url);
				//e.printStackTrace();
			}
		}
		try {
			bw.close();
		} catch (IOException e) {
			System.out.println("Failed to close file: "+url);
			//e.printStackTrace();
		}
		try {
			System.out.println(f.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String indent(int layer){
		String out = "";
		for(int i=0; i<layer; i++){
			for(int j=0; j<spacePerTab; j++){
				out += " ";
			}
		}
		return out;
	}
}
