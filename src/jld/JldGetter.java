package jld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.management.BadStringOperationException;

/**
 * Opens and parses the JLD data in a file
 */
public class JldGetter {
	public JldGetter(){}
	
	/**
	 * Returns the raw downloaded data
	 */
	public Vector<String> download(String url){
		BufferedReader buffreader = null;
		try{
			buffreader = new BufferedReader(new InputStreamReader(new FileInputStream(openFile(url)), "utf-8"));
			String temp;
			Vector<String> out = new Vector<String>();
			while((temp=buffreader.readLine()) != null){
				out.add(temp);
			}
			buffreader.close();
			return out;
		}catch(IOException e){
			System.out.println("ERR: download failure "+e);
			return null;
		}
	}
	
	protected File openFile(String url){
		File f = null;
		try {
			f = new File(getClass().getResource("/enigma/data/"+url).toURI());
		}catch (Exception e){
			System.out.println("Cannot find "+url);
			e.printStackTrace();
		}
		return f;
	}
	
	/**
	 * Public access function
	 */ 
	public JLD getValues(String url){
		Vector<String> raw = download(url);
		raw = stripper(raw);
		String input = "";
		for(String s : raw)
			input += s;
		return new JLD(parseDoc(input));
	}
	
	private HashMap parseDoc(String raw){
		HashMap<String, Object> out = null; 
		try{
			if(raw.charAt(0) == '{'){ //document
				boolean nextIsVal = false;
				String key = null;
				out = new HashMap<String, Object>();
				for(raw=raw.substring(1).trim(); true; raw=raw.trim()){
					if(raw.charAt(0) == ':'){ //new value
						raw = raw.substring(1).trim();
						if(nextIsVal){
							Object val = null;
							if(raw.charAt(0) == '"'){ //new single item value
								val = stringBetween(raw, '"');
								raw = stringAfter(raw, '"');
							}else if(raw.charAt(0) == '['){ //new list value
								val = parseList(stringBetween(raw, '[', ']'));
								raw = stringAfter(raw, '[', ']');
							}else if(raw.charAt(0) == '{'){ //new doc value
								val = parseDoc(stringBetweenInclusive(raw, '{', '}'));
								raw = stringAfter(raw, '{', '}');
							}else{
								throw new BadStringOperationException("Malformed JLD: invalid value");
							}
							raw = raw.substring(raw.indexOf(',')+1);
							out.put(key, val);
							nextIsVal = false;
						}else{
							throw new BadStringOperationException("Malformed JLD: parser expected a value after :");
						}
					}else if(raw.charAt(0) == '"'){
						if(!nextIsVal){ //new key
							key = stringBetween(raw, '"');
							raw = raw.substring(1);
							raw = raw.substring(raw.indexOf('"')+1);
							nextIsVal = true;
						}else{
							throw new BadStringOperationException("Malformed JLD: parser expected a key");
						}
					}else if(raw.charAt(0) == '}'){
						if(!nextIsVal){
							break;
						}else{
							throw new BadStringOperationException("Malformed JLD: parser expected a value instead of }");
						}
					}else{
						throw new BadStringOperationException("Malformed JLD: unhandled case: "+raw+"\r\nDid you forget a comma?");
					}
				}
			}else{
				throw new BadStringOperationException("Malformed JLD: no document present");
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return out;
	}
	
	/**
	 * Returns a map of values
	 * Cannot parse fields containing objects
	 * Example input: "Id","101010100","Name"
	 */
	private Vector parseList(String raw) throws IndexOutOfBoundsException{
		String orig = raw;
		Vector<Object> out = new Vector<Object>();
		while(raw.indexOf('"')>=0){
			if(raw.charAt(0) == '"'){
				out.add(stringBetween(raw, '"'));
				raw = stringAfter(raw, '"');
			}else if(raw.charAt(0) == '['){
				out.add(parseList(stringBetween(raw, '[', ']')));
				raw = stringAfter(raw, '[', ']');
			}else if(raw.charAt(0) == '{'){
				out.add(parseDoc(stringBetweenInclusive(raw, '{', '}')));
				raw = stringAfter(raw, '{', '}');
			}else{
				break;
			}
			raw = raw.substring(raw.indexOf(',')+1).trim();
		}
		return out;
	}
	
	/**
	 * Strips comments such as // and /*
	 * Used only by JLD offline documents
	 * Actual JSON strings do not contain comments...or lines
	 */
	private Vector<String> stripper(Vector<String> in){
		Vector<String> out = new Vector<String>();
		boolean blockquote = false;
		for(String s : in){
			for(int i=0; i<s.length()-1; i++){
				String st = s.substring(i, i+2);
				if(blockquote){
					if(st.equals("*/")){
						s = s.substring(i+2);
						blockquote = false;
						if(s.length()>=2)
							st = s.substring(0, 2);
					}
				}else if(st.equals("//")){
					s = s.substring(0, i);
				}else if(st.equals("/*")){
					blockquote = true;
				}
			}
			s = s.trim();
			if(blockquote || s.isEmpty())
				continue;
			else
				out.add(s);
		}
		return out;
	}
	
	/**
	 * No bounds checking
	 * No checking of any kind! (sort of)
	 */
	private String stringBetween(String raw, char c) throws IndexOutOfBoundsException{
		raw = raw.substring(raw.indexOf(c)+1);
		raw = raw.substring(0,raw.indexOf(c));
		return raw;
	}
	private String stringBetween(String raw, char a, char b) throws IndexOutOfBoundsException{
		String out = raw.substring(raw.indexOf(a)+1);
		int open = 1;
		int i;
		for(i=0; open>0; i++){
			if(out.charAt(i) == a){
				open++;
			}else if(out.charAt(i) == b){
				open--;
			}else if(out.charAt(i) == '"'){
				i++;
				while(out.charAt(i) != '"')
					i++;
			}
		}
		out = out.substring(0,i-1);
		return out;
	}
	private String stringBetweenInclusive(String raw, char a, char b) throws IndexOutOfBoundsException{
		return a+stringBetween(raw, a, b)+b;
	}
	private String stringAfter(String raw, char a, char b) throws IndexOutOfBoundsException{
		String sub = stringBetweenInclusive(raw, a, b);
		return raw.substring(sub.length());
	}
	private String stringAfter(String raw, char c) throws IndexOutOfBoundsException{
		int i = raw.indexOf('"');
		for(i++; raw.charAt(i)!='"'; i++)
			;
		return raw.substring(i+1);
	}
}
