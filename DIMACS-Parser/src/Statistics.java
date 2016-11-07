import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
* Statistiken zu einer DIMACS Datei
* Loesung zur Aufgabe 2.4
* @author Enes Poyraz,Jonas Bollgruen 
* @version 1.0
*/

public class Statistics {
	
	public static void main(String[] args) {
		String [] files = {	"aim-100-1_6-no-1.cnf",
							"aim-200-2_0-yes1-2.cnf", 
							"barrel5-no.cnf", 
							"goldb-heqc-k2mul.cnf",
							"hanoi4-yes.cnf",
							"hole8-no.cnf",
							"longmult6-no.cnf",
							"miza-sr06-md5-47-03.cnf",
							"ssa7552-160-yes.cnf"};
		
		for(String file:files){
			new Statistics("files/"+file);
		}
	}
	
	String filename;

	int varsInPreamble = 0;
	int clausesInPreamble = 0;

	int variablecount = 0;
	int clausecount = 0;
	int literalcount = 0;
	int maxoccurence = Integer.MIN_VALUE;

	ArrayList<Integer> maxoccurVariables = new ArrayList<>();
	ArrayList<Integer> positivePure = new ArrayList<>();
	ArrayList<Integer> negativePure = new ArrayList<>();
	ArrayList<Integer> unitClauses = new ArrayList<>();

	Set<String> variables = new HashSet<>();
	Set<String> all_literals = new HashSet<>();

	public Statistics(String filename) {
		this.filename = filename;

		DIMACS_Parser parser = new DIMACS_Parser();
		parser.parse(this.filename);

		ArrayList<String> clauses = parser.getClauses();
		String preamble = parser.getPreamble();

		getVarsAndClausesInPreamble(preamble);
		getLiterals(clauses);
		getUnitClauses(clauses);
		getVariables(clauses);
		getMaximalOccurencesOfAVariable(variables, clauses);

		getPositivePureLiterals();
		getNegativePureLiterals();

		literalcount = all_literals.size();
		variablecount = variables.size();
		clausecount = clauses.size();

		print();

	}

	private void getNegativePureLiterals() {
		for (String s : all_literals) {
			if (s.contains("-")) {
				if (!all_literals.contains(s.replace("-", ""))) {
					negativePure.add(Integer.parseInt(s));
				}
			}
		}
	}

	private void getPositivePureLiterals() {
		for (String s : all_literals) {
			if (Character.isDigit(s.charAt(0))) {
				String negative = "-" + s;
				if (!all_literals.contains(negative) && !s.equals("0"))
					positivePure.add(Integer.parseInt(s));
			}
		}
	}

	private void getVariablesWithMaximumNumberOfOccurences(int[] occurences, int maxoccurence) {
		for (int i = 1; i < occurences.length; i++) {
			if (occurences[i] == maxoccurence) {
				maxoccurVariables.add(i);
			}
		}
	}

	private void getMaximalOccurencesOfAVariable(Set<String> variables, ArrayList<String> clauses) {
		int[] occurences = new int[variables.size() + 1];
		for (String clause : clauses) {
			String[] literals = clause.replace("-", "").split("\\s+");
			for (String literal : literals) {
				occurences[Integer.parseInt(literal)]++;
			}
		}
		maxoccurence = getMax(occurences);
		getVariablesWithMaximumNumberOfOccurences(occurences, maxoccurence);
	}

	private int getMax(int[] occurences) {
		int max = 0;
		for (int i = 1; i < occurences.length; i++) {
			if (occurences[i] > max) {
				max = occurences[i];
			}
		}
		return max;
	}

	private void getVariables(ArrayList<String> clauses) {
		for (String clause : clauses) {
			String clause_without_negation = clause.replace("-", "");
			for (String literal : clause_without_negation.split("\\s+")) {
				variables.add(literal);
			}
		}
		variables.remove("0");
	}

	private void getUnitClauses(ArrayList<String> clauses) {		
		for(int i=0;i <clauses.size();i++){
			String clause = clauses.get(i);
			if (clause.split("\\s+").length == 2) {
				unitClauses.add(i);
			}
		}
	}

	private void getLiterals(ArrayList<String> clauses) {
		for (String clause : clauses) {
			String[] literals = clause.split("\\s+");
			for (String literal : literals) {
				all_literals.add(literal);
			}
		}
		all_literals.remove("0");
	}

	private void getVarsAndClausesInPreamble(String preamble) {
		varsInPreamble = Integer.parseInt(preamble.split("\\s+")[2]);
		clausesInPreamble = Integer.parseInt(preamble.split("\\s+")[3]);
	}

	private void print() {
		Path p = Paths.get(filename);
		String file = p.getFileName().toString();
		
		System.out.println("File : " + file);
		System.out.println("Problem line : #vars = " + varsInPreamble + ", #clauses = " + clausesInPreamble);
		System.out.println("Variable count : " + variablecount);
		System.out.println("Clause count : " + clausecount);
		System.out.println("Literal count : " + literalcount);
		System.out.println("Maximal occurencs of a variable : " + maxoccurence);
		System.out.println(
				"Variables with maximum number of occurences : " + Arrays.toString(maxoccurVariables.toArray()));
		System.out.println("Positive pure literals : " + Arrays.toString(positivePure.toArray()));
		System.out.println("Negative pure literals : " + Arrays.toString(negativePure.toArray()));
		System.out.println("Unit clauses : " + Arrays.toString(unitClauses.toArray()));
	}

}
