import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
	
	public static void main(String[] args) {
		
		String filename = "aim-100-1_6-no-1.cnf";
		int vars = 0;
		int clauses = 0;
		
		int varcount = 0;
		int clausecount = 0;
		int literalcount = 0;
		int maxoccurence = Integer.MIN_VALUE;
		
		int [] helper = new int[20000];

		ArrayList<Integer> maxoccurVariables = new ArrayList<>();
		ArrayList<Integer> positivePure = new ArrayList<>();
		ArrayList<Integer> negativePure = new ArrayList<>();
		ArrayList<Integer> unitClauses = new ArrayList<>();
		
		Set<Integer> variables= new HashSet<>();


		try (BufferedReader br = new BufferedReader(new FileReader("files/"+filename)))
		{

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.startsWith("p ")){
					vars = Integer.parseInt(sCurrentLine.split(" ")[2]);
					clauses = Integer.parseInt(sCurrentLine.split(" ")[3]);

					//System.out.println("Problem line");
					//System.out.println(sCurrentLine);	
				}
				if(sCurrentLine.startsWith("c ")){
					//System.out.println("Comment:");
					//System.out.println(sCurrentLine);
				}
				if(sCurrentLine.startsWith("-") || Character.isDigit(sCurrentLine.charAt(0))){
					
					String line_without_minus = sCurrentLine.replace("-","");
					String [] vars_in_line = line_without_minus.split(" ");
					for(String var:vars_in_line){
						
						int varint = Integer.parseInt(var); 
						helper[varint]++;
						variables.add(varint);
					}
					clausecount++;
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		helper[0] = 0;
		
		for(int i = 0; i < helper.length; i++) {
		      if(helper[i] > maxoccurence) {
		    	  maxoccurence = helper[i];
		      }
		}
		
		for(int i = 0; i < helper.length; i++) {
			if(helper[i] == maxoccurence)
				maxoccurVariables.add(i);
		}		
		
		varcount = variables.size()-1;
				
		System.out.println("File : " + filename);
		System.out.println("Problem line : #vars = " + vars + ", #clauses = " + clauses);
		System.out.println("Variable count : " + varcount);
		System.out.println("Clause count : " + clausecount);
		System.out.println("Literal count : " + literalcount);
		System.out.println("Maximal occurencs of a variable : " + maxoccurence);
		System.out.println("Variables with maximum number of occurences : " + Arrays.toString(maxoccurVariables.toArray()));
		System.out.println("Positive pure literals : " + Arrays.toString(positivePure.toArray()));
		System.out.println("Negative pure literals : " + Arrays.toString(negativePure.toArray()));
		System.out.println("Unit clauses : " + Arrays.toString(unitClauses.toArray()));
	}
	
	
	

}
