import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//finds the (value)(operation)(value) strings and splits them up into tokens.
//values are first turned into Value objects, and the operator and values are converted to tokens.
//These tokens are added to the tokens array, for future processing.
//first find all number or variable token like objects. (character x or negative sign followed by sequence
// or decimal sequence or number decimal sequence or number sequence)
//the value matches have a last character, and if the character after the last character is an exponent, do the pattern matching.
//The 
class NumberFinder{
    public static void main(String args[]) {
    	String string = "Matches:";
    	String start = "Starts: ";
    	String end = "Ends: ";

		Scanner scan = new Scanner(System.in);
		String function = scan.nextLine();
		Pattern values = Pattern.compile("\\-|\\\\|/|\\*|\\^|\\+");
		//finds all strings described by:
		// an optional negative sign then
		// zero or more numbers then
		// possibly a decimal point then
		// one or more numbers,
		// or a possible negative sign then x or X.
		
		Matcher matcher = values.matcher(function);
		while (matcher.find()) {
		    string += matcher.group()+" "; // print entire matched substring
		    start += matcher.start()+" ";
		    end += matcher.end()+" ";
		}

		System.out.println(string);
		System.out.println(start);
		System.out.println(end);


    }
}