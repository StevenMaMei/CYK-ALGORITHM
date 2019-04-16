package model;
/**
 * Class that represents a production of a variable within a Gramatic
 * @author Steven
 *
 */
public class ProductionOfGramaticInFNC {
	/**
	 * Is the variable 1 of the gramatic
	 */
	private Character variable1;
	/**
	 * Is the possible variable 2 of the gramatic
	 */
	private Character variable2;
	
	/**
	 * Constructs a production that has only 1 variables
	 * @param variable1 Character- the variable 1
	 */
	public ProductionOfGramaticInFNC(Character variable1) {
		this.variable1= variable1;
		variable2= null;
	}
	/**
	 * Constructs a productions that has 2 varibles
	 * @param variable1
	 * @param variable2
	 */
	public ProductionOfGramaticInFNC(Character variable1, Character variable2) {
		this.variable1= variable1;
		this.variable2= variable2;
	}
	//Getters and setters
	public Character getVariable1() {
		return variable1;
	}
	public void setVariable1(Character variable1) {
		this.variable1 = variable1;
	}
	public Character getVariable2() {
		return variable2;
	}
	public void setVariable2(Character variable2) {
		this.variable2 = variable2;
	}
	//Finish of getters and setters
	
	@Override
	public String toString() {
		if(variable2!=null) {
			return variable1+variable2+"";
		}
		return variable1+"";
	}
	
	
}
