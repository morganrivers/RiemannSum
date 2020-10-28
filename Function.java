import java.util.Arrays;
//cleans out disallowed characters from string. Also 
public class EquationParser {

	public Parser(String allowed_characters,){
		this.allowed_characters = allowed_characters;

		//break into character arrays so the function can be analyzed character by character.

		//create an object out of the characters
		char[] function = toCharArray(charArray);

	}

	public Token[] parse(){
		Token[] y;

		return y;
	}
	
	//remove illegal characters in the function
	public String cleanFunction(String function) {

		//go from first character to last character
		for(int index=0;index < function.length; index++){
			
			//remove the offending characters
			if(this.allowed_characters.indexOf(function.charAt(index)) != -1){
				String clean_function += function.charAt(index);			
			}
		}
		
		return clean_function;
	}
}