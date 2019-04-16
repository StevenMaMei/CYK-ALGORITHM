package model;


import java.util.ArrayList;
/**
 * Represents a variable of a gramatic
 * @author Steven
 *
 */
public class Variable {

	/**
	 * the productions of the variable
	 */
	private ArrayList<ProductionOfGramaticInFNC> productions;
	/**
	 * Constructs a variable
	 */
	public Variable() {
		productions= new ArrayList<ProductionOfGramaticInFNC>();
	}
	/**
	 * Adds a production that has one variable
	 * @param v1 Character- variable
	 */
	public void addProduction(Character v1) {
		productions.add(new ProductionOfGramaticInFNC(v1));
	}
	/**
	 * Adds a production of a Gramatic in FNC
	 * @param v1
	 * @param v2
	 */
	public void addProduction(Character v1, Character v2) {
		productions.add(new ProductionOfGramaticInFNC(v1,v2));
	}
	/**
	 * Checks if one production exists in the variable
	 * @param p production
	 * @return true if this production exists
	 */
	public boolean isThereThisProduction(String p) {
		boolean ans=false;
		if(p.length()<=2) {
			for(int i=0;i<productions.size()&&!ans;i++) {
				if(productions.get(i).toString().equals(p)) {
					ans=true;
				}
			}
		}
		return ans;
	}
	
	
}
