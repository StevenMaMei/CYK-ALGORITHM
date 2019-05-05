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
	 * Terminals that belongs to the gramatic.
	 */
	private HashSet<Character> terminals;
	
	private Variable firstVariable;
	
	/**
	 * Constructor
	 */
	public GramaticInFNC() {
		variables= new HashMap<Character, Variable>();
		terminals = new HashSet<Character>();
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
		if(!allVariablesCanBeReached()) {
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
	public boolean allVariablesCanBeReached() {
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
	
	/**
	 * Adds a new variable that belongs to the gramatic
	 * @param v
	 */
	public void addVariable(Character v, boolean init) {
		Variable var = new Variable(v, init);
		variables.put(v, var);
		if (init) firstVariable = var;
	}
	
	/**
	 * Adds a new terminal that belongs to the gramatic
	 * @param term
	 */
	public void addTerminal(Character term) {
		terminals.add(term);
	}
	
	/**
	 * Adds a production to a specified variable. The production gives as result two variables.
	 * (In the form A --> BC)
	 * @param from The variable that will have the new production
	 * @param to1 The first variable of the result of the production. It is a variable that belongs to the gramatic
	 * @param to2 The second variable of the result of the production. It is a variable that belongs to the gramatic
	 */
	public void addProduction(char from, char to1, char to2) {
		Variable var = variables.get(from);
		var.addProduction(to1, to2);
	}
	
	/**
	 * Adds a production to a specified variable. The production gives as result one terminal.
	 * (In the form A --> a)
	 * @param from The variable that will have the new production. 
	 * @param terminal The result of the production. It is a terminal that belongs to the gramatic.
	 */
	public void addProduction(char from, char terminal) {
		Variable var = variables.get(from);
		var.addProduction(terminal);
	}
	
	/**
	 * The CYK Algorithm. It verifies if a given string p belongs or not to the current gramatic.
	 * @param p The string that we want to verify if belongs to the gramatic.
	 * @return true if the string belongs to the gramatic, false if not.
	 */
	public boolean CYK(String p) {
		char [] characters = p.toCharArray();
		HashSet<Variable> [][] memo = (HashSet<Variable>[][]) new HashSet [p.length()][p.length()];
		initialIterationCYK(memo, characters);
		for (int j = 1; j < characters.length; j++) {
			repeatableIterationsCYK(memo, characters, j);
		}
		boolean producesString = false;
		for (Variable v : memo[0][p.length()-1]) {
			producesString = producesString || v.isTheInitial();
		}
		return producesString;
	}
	
	/**
	 * Fills the first column of the memo. That is, for each row, it adds al the variables that produces
	 * the ith terminal of the String.
	 * @param memo The matrix where the CYK algortithm is being completed.
	 * @param characters An array corresponding to the chars of the string p we want to verify if belongs to the gramatic.
	 */
	private void initialIterationCYK(HashSet<Variable>[][] memo, char [] characters) {
		int j = 0;
		for (int i = 0; i < characters.length; i++) {
			memo [i][j] = new HashSet<Variable>();
			char actChar = characters[i];
			for (Variable v : variables.values()) {
				boolean contains = false;
				for (ProductionOfGramaticInFNC prod : v.getProductions()) {
					contains = contains || prod.getVariable1() == actChar;
				}
				if (contains)
					memo[i][j].add(v);
			}
		}
	}
	
	/**
	 * The iterable part of the CYK algorithm. For every column (Except the first), it adds the variables,
	 * for every row i where 0 <= i < n-j, that satisfies the fact that: A is added if the production A -> BC exists and 
	 * B belongs to X(i,k) and C belongs to X(i+k,j-k) for every 0 <= k < j-1
	 * @param memo The matrix where the CYK algortithm is being completed.
	 * @param characters An array corresponding to the chars of the string p we want to verify if belongs to the gramatic.
	 * @param j The length of the string we want to check if its produced (The iteration).
	 */
	private void repeatableIterationsCYK(HashSet<Variable>[][] memo, char [] characters, int j) {
		for (int i = 0; i < characters.length -j; i++) {
			memo [i][j] = new HashSet<Variable>();
			System.out.println(i + " " + j);
			for (int k = 0; k < j; k++) {
				for (Variable v: variables.values()) {
					boolean contains = false;
					for (ProductionOfGramaticInFNC prod : v.getProductions()) {
						contains = contains ||( prod.getVariable2() != null && memo[i][k].contains(variables.get(prod.getVariable1()))
								&& memo[i+k+1][j-k-1].contains(variables.get(prod.getVariable2())));
					}
					if (contains) {
						memo [i][j].add(v);
					}
				}
			}
		}
	}
	
	public Variable getFirstVariable() {
		return firstVariable;
	}
	public HashMap<Character, Variable> getVariables() {
		return variables;
	}
	public HashSet<Character> getTerminals() {
		return terminals;
	}
	
	
}
