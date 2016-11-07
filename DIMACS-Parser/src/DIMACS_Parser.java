import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
* DIMACS_Parse in Java
* Diese Klasse liesst dateien im DIMACS format ein und parst diese in 3 verschiedene Bereiche:
* 1. Kommentare beginnend mit einem 'c'
* 2. Die Problemzeile ( hier preamble ) beginnend mit 'p'
* 3. Klauseln, die mit '0' enden  
* @author Enes Poyraz,Jonas Bollgruen 
* @version 1.0
*/
public class DIMACS_Parser {
	
	public ArrayList<String> getComments() {
		return comments;
	}

	public ArrayList<String> getClauses() {
		return clauses;
	}

	public String getPreamble() {
		return preamble;
	}

	ArrayList<String> comments;
	ArrayList<String> clauses;
	String preamble;
	
	public DIMACS_Parser() {
		comments = new ArrayList<String>();
		clauses = new ArrayList<String>();
		preamble = "";
	}
	
	
	public void parse(String filename){
		try (BufferedReader br = new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.startsWith("c "))
				{
					comments.add(sCurrentLine);
				}
				// preamble.isEmpty ensures, that this line only occurs once
				if(sCurrentLine.startsWith("p ") && preamble.isEmpty()){
					preamble = sCurrentLine;
				}
				// if line starts with negative sign or a digit
				if(sCurrentLine.startsWith("-") || Character.isDigit(sCurrentLine.charAt(0))){
					clauses.add(sCurrentLine);
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
