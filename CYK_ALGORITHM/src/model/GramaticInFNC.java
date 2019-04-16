package model;
/**
 * Represents a gramatic in normal form of Chomsky
 * @author Steven
 *
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GramaticInFNC {
	/**
	 * Variables of the gramatic and their productions
	 */
	private HashMap<Character, Variable> variables;
	/**
	 * Constructor
	 */
	public GramaticInFNC() {
		variables= new HashMap<Character, Variable>();
	}
	
	public boolean isInFNC() {
		//TODO
		return false;
	}
	public void addVariable(Character v, ArrayList<String> productions) {
		//TODO
	}
	public boolean CYK(String p) {
		//TOODO
		return false;
	}
	
	
}
