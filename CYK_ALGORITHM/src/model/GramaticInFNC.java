package model;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.omg.CosNaming.IstringHelper;

/**
 * Represents a gramatic in normal form of Chomsky
 * @author Steven
 *
 */
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
	/**
	 * Checks if the current gramatic is in FNC
	 * @return
	 */
	public boolean isInFNC() {
		
		if(!thereAreNotUnitaryVariables()) {
			return false;
		}
		if(!thereAreNotLambdaExcepInTheInitial()) {
			return false;
		}
		if(!allVariblesCanBeReached()) {
			return false;
		}
		if(!allVariablesAreTerminal()) {
			return false;
		}
		return true;
		
	}
	/**
	 * Checks if all variables are terminal
	 * @return true if all of variables are terminal
	 */
	public boolean allVariablesAreTerminal() {
		int countB=-1;
		int countA=0;
		ArrayList<Variable> terminals= new ArrayList<Variable>();
		for(Variable v: variables.values()) {
			ArrayList<ProductionOfGramaticInFNC> productions = v.getProductions();
			for(int i=0;i<productions.size();i++) {
				if(productions.get(i).toString().equals(productions.get(i).toString().toLowerCase())) {
					terminals.add(v);
				}
			}
		}
		HashSet<Character> variablesAlreadyAdded= new HashSet<Character>(); 
		while(countB !=countA) {
			for(Variable v: variables.values()) {
				if(!variablesAlreadyAdded.contains(v.getId())) {
					boolean check= true;
					ArrayList<ProductionOfGramaticInFNC> productions= v.getProductions();
					for(int i=0;i<v.getProductions().size();i++) {
						Character v1= productions.get(i).getVariable1();
						Character v2= productions.get(i).getVariable2();
						if(v1.isUpperCase(v1)&&productions.contains(v1)) {
							if(v2!= null&&Character.isUpperCase(v2) &&productions.contains(v2)) {
								variablesAlreadyAdded.add(v.getId());
								terminals.add(v);
								countA++;
							}//Don't need to check when is only one variable and it's not terminal, because it will not happen
						}else if(Character.isLowerCase(v1)) {
							if(v2== null) {
								variablesAlreadyAdded.add(v.getId());
								terminals.add(v);
								countA++;								
							}else if( v2 != null && variablesAlreadyAdded.contains(v2)) {
								variablesAlreadyAdded.add(v.getId());
								terminals.add(v);
								countA++;
							}
						}
					}
				}
			}
			countB++;
		}
		for(Variable v: variables.values()) {
			if(!variablesAlreadyAdded.contains(v.getId())) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Checks if all variables can be reached
	 * @return true if all variables can be reached
	 */
	public boolean allVariblesCanBeReached() {
		ArrayList<Variable> reached= new ArrayList<Variable>();
		HashSet<Character> alreadyReached= new HashSet<Character>();
		HashSet<Character> alreadyChecked= new HashSet<Character>();
		for(Variable v: variables.values()) {
			if(v.isTheInitial()) {
				reached.add(v);
				alreadyReached.add(v.getId());
			}
		}
		boolean added=true;
		while(added) {
			added= false;
			for(Variable v: reached) {
				if(!alreadyChecked.contains(v.getId())) {
					alreadyChecked.add(v.getId());
					ArrayList<ProductionOfGramaticInFNC> productions= v.getProductions();
					for(ProductionOfGramaticInFNC p: productions) {
						if(Character.isUpperCase(p.getVariable1())&&!alreadyReached.contains(p.getVariable1())) {
							alreadyReached.add(p.getVariable1());
							reached.add(variables.get(p.getVariable1()));
							added=true;
						}
						if(p.getVariable2()!= null && Character.isUpperCase(p.getVariable2())&&!alreadyReached.contains(p.getVariable2())) {
							alreadyReached.add(p.getVariable1());
							reached.add(variables.get(p.getVariable1()));
							added=true;
						}
					}
					
				}
			}
		}
		for(Variable v: variables.values()) {
			if(!alreadyReached.contains(v.getId())) {
				return false;
			}
		}
		return true;
		
	}
	/**
	 * Checks if there is not a nullable variable that is not the initial
	 * @return
	 */
	public boolean thereAreNotLambdaExcepInTheInitial() {
		for(Variable v:variables.values()) {
			if(!v.isTheInitial()&& v.isThereThisProduction("")) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Checks if there is not unitary variables
	 * @return
	 */
	public boolean thereAreNotUnitaryVariables() {
		for(Variable v:variables.values()) {
			ArrayList<ProductionOfGramaticInFNC> productions= v.getProductions();
			for(ProductionOfGramaticInFNC p: productions) {
				if(Character.isUpperCase(p.getVariable1())&& p.getVariable2()==null) {
					return false;
				}
			}
		}
		return true;
	}
	public void addVariable(Character v) {
		variables.put(v, new Variable(v));
	}
	
	public void addProduction(char from, char to1, char to2) {
		Variable var = variables.get(from);
		var.addProduction(to1, to2);
	}
	
	public void addProduction(char from, char terminal) {
		Variable var = variables.get(from);
		var.addProduction(terminal);
	}
	
	public boolean CYK(String p) {
		char [] characters = p.toCharArray();
		HashSet<Variable> [][] memo = (HashSet<Variable>[][]) new HashSet [p.length()][p.length()];
		initialIterationCYK(memo, characters);
		for (int j = 1; j < characters.length; j++) {
			repeatableIterationsCYK(memo, characters, j);
		}
		boolean producesString = false;
		for (Variable v : memo[p.length()-1][p.length()-1]) {
			producesString = producesString || v.isTheInitial();
		}
		return producesString;
	}
	
	private void initialIterationCYK(HashSet<Variable>[][] memo, char [] characters) {
		int j = 0;
		for (int i = 0; i < characters.length; i++) {
			memo [i][j] = new HashSet<Variable>();
			char actChar = characters[i];
			for (Variable v : variables.values()) {
				boolean contains = false;
				for (ProductionOfGramaticInFNC prod : v.getProductions()) {
					contains = prod.getVariable1() == actChar;
				}
				if (contains)
					memo[i][j].add(v);
			}
		}
	}
	
	private void repeatableIterationsCYK(HashSet<Variable>[][] memo, char [] characters, int j) {
		for (int i = 0; i < characters.length -j; i++) {
			memo [i][j] = new HashSet<Variable>();
			for (int k = 0; k < j - 1; k++) {
				for (Variable v: variables.values()) {
					boolean contains = false;
					for (ProductionOfGramaticInFNC prod : v.getProductions()) {
						contains = prod.getVariable2() != null && memo[i][k].contains(variables.get(prod.getVariable1()))
								&& memo[i+k][j-k].contains(variables.get(prod.getVariable2()));
					}
					if (contains) {
						memo [i][j].add(v);
					}
				}
			}
		}
	}
	
	
}
