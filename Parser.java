import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Comparator;
import java.util.Collection;
import java.util.*;
import java.util.ArrayList;// Import ArrayList 
/*An interpreter needs 3 things to work:
1.A means of being loaded with instructions
	In this case a string of the function
2.A module format, for storing instructions to be executed. This will be a parse tree.
	In this case an arraylist of values, and the tokens arraylist which contains the evaluation structure
3.A model or environment for interacting with the host program
	The console. This could be improved.
4.Error finding
	The error finder class and built in error finders. This could be improved.
*/

public class Parser {
	ArrayList<Value> function_values = new ArrayList<Value>();//create an array of values that will come to represent the function in a list of numbers and variables 
	public ArrayList<Token> tokens = new ArrayList<Token>();
	public static ArrayList<Value> token_values = new ArrayList<Value>();

	Pattern numbers;
	Pattern variables;
	Pattern operators;
	
	Matcher number_matcher;
	Matcher variable_matcher;
	Matcher operator_matcher;
	
	String function;
	
	public String allowed_characters = "1234567890-+xX^*\\/().";

 	public Parser(){
	 	//This value finder finds all strings described by:
	 	 // an optional negative sign then 
	 	 // zero or more numbers then 
	 	 // possibly a decimal point then 
	 	 // one or more numbers
		numbers = Pattern.compile("[0-9]*[\\.]?[0-9]+");
		 // x or X
		variables = Pattern.compile("[xX]");
		 // a negative sign, division or subtraction, multiplication, exponents, division, or parenthesis
		operators = Pattern.compile("\\-|\\\\|/|\\*|\\^|\\+|\\)|\\(");
 	}

 	public double evaluate(double x){
 		int value;
		int last_index = tokens.size()-1;
 		return tokens.get(last_index).evaluate(x); //the last token references each token recursively, eventually coming out to the final value
 	}

 	//gives the list of things to evaluate to another function to evaluate it.
 	public ArrayList<Token> getTokens(){
 		return tokens; //the last token references each token recursively, eventually coming out to the final value
 	}

 	//gives the list of tokenvalues that point to tokens.
 	public ArrayList<Value> getTokenValues(){
 		return token_values; //the last token references each token recursively, eventually coming out to the final value
 	}

 	public ArrayList<Value> parse(String function){
 		this.function = function;
 		int start_index = 0;
 		double number_value;
 		number_matcher = numbers.matcher(this.function);
 		variable_matcher = variables.matcher(this.function);
 		operator_matcher = operators.matcher(this.function);

		//creates an array of number values in the string that can be easily manipulated.
 		while (number_matcher.find()) {
 			number_value = this.evaluateNumber(number_matcher.group());
 			
 			function_values.add(
 				new NumberValue(
 					number_matcher.start(),
 					number_matcher.end(),
 					number_value
 				)
 			);
		}

		//adds to the end of the previous array any variables, not considering if they're negative
		while (variable_matcher.find()) {
			function_values.add(new VariableValue(variable_matcher.start(),variable_matcher.end()));
		} 

		//adds to the end of the previous array any operators
		while (operator_matcher.find()) {
			function_values.add(new OperatorValue(operator_matcher.start(),operator_matcher.end(),operator_matcher.group().charAt(0)));
		}
		Collections.sort(function_values,new ValueComparer());

		start_index = this.findParenthesisGroupStart(function_values);//finds out where to start parsing
		
		//we're running a parenthesis eliminator. Get the function in the parenthesis, then take that as a new function,
		//and convert it to tokens. Then repeat until have no more parentheses, and everything has been parsed down to one tokenvalue referencing an arraylist of tokens
		int last_index = function_values.size()-1;
		int type=-1;//in case it fails, put in nonsense number.
		
		boolean done = false;
		while(!done){
			start_index = this.findParenthesisGroupStart(function_values);//finds out where to start parsing
			function_values = this.parseParenthesisGroup(start_index);//parse out a parenthesis group, and return the result
			if(function_values.size() == 1){
				done = true;
			}
			last_index = function_values.size()-1;
		}
		return function_values;
	}


	//uses the last beginning parenthesis in the remaining function.
	//returns the index in the value array the parenthesis ValueOperator  is.
	public int findParenthesisGroupStart(ArrayList<Value> function_values){
		int last_function_index = function_values.size()-1;

		for(int index = last_function_index;index>=0;index--){
			//looks for an end parenthesis. 
			if(function_values.get(index).getType() == Value.START_PARENTHESIS){
				return index+1;
			}
		}
		return -1;
	}
		


	//gets the next parenthesis group to tokenize. Already parsed parentheses are removed.
	//assigns a array of TokenValues in place of the tokenized values generated. 
	//returns a list of values and modifies the Value array to have a TokenValue placed inside the parenthesis.
	//Remove any parentheses around the group once done.
	public ArrayList<Value> parseParenthesisGroup(int start_index){
		boolean has_parens = true;
		if(start_index == -1){
			has_parens = false;
		}
		int end_index = -1;

		//find where to end the parsing inside the enclosed subfunction
		if(has_parens){
			int function_size = function_values.size();
			//loop through the values array and stop when encounter end of array or an end parenthesis.
			for(int count = start_index;count<=function_size;count++){

				//looks for an end parenthesis. 
				if(function_values.get(count).getType() == Value.END_PARENTHESIS){
					end_index = count-1;//subtract one because the get function acts diffrently than the other arraylist parsers
					break;
				}
			}				
		} else { //if no parentheses in the function
			start_index = 0;
			end_index = function_values.size()-1;
		}

		ArrayList<Value> new_values_array;

		new_values_array = new ArrayList<Value>(function_values.subList(start_index,end_index+1));
		
		if(has_parens){//if there are parentheses to remove
			function_values.subList(start_index-1,end_index+2).clear();
		} else {
			function_values.subList(start_index,end_index+1).clear();
		}

		new_values_array = parseExponents(new_values_array);//parses power sign
		new_values_array = parseMultiplyDivide(new_values_array);//parses multiplication and division. Treats values of number, token, and variable values adjacent as multiplication
		new_values_array = parseAddSubtract(new_values_array);//parses addition and subtraction		
		if(has_parens){//if there were parentheses removed
			function_values.add(start_index-1,new_values_array.get(0));//should return the token for the tokens arraylist thus far
		} else {
			function_values.add(start_index,new_values_array.get(0));//should return the token for the tokens arraylist thus far
		}

		return function_values;
	}


	// take all exponents and adjacent values, tokenizes them, replacing them with tokenvalues in the function_values arrayl, and creating new tokens.
	//returns a new value list with values that reference the created tokens
	public ArrayList<Value> parseExponents(ArrayList<Value> function_values){ 
		ArrayList<Value> subfunction = function_values;
		int last_index = subfunction.size()-1;
		int type=-1;//in case it fails, put in nonsense number.
		for(int index = 1;index<=last_index;index++){
			type = subfunction.get(index).getType();
			int value = last_index-index;
			if(type == Value.EXPONENT){
				Value operator = subfunction.get(index);//exponent type operator for token

				Value value1 = subfunction.get(index-1);

				Value value2 = subfunction.get(index+1);		

				//ensure that if next  value is a minus sign, and the value
				//after that is a number value, make the value negative.
				if(subfunction.size()-index>2){
					Value value3 = subfunction.get(index+2);
					int value3_type = value3.getType();
					if((value3_type==Value.NUMBER||value3_type==Value.VARIABLE||value3_type==Value.TOKEN)&&(value2.getType() == Value.MINUS)){
						value2 = value3;//set the value to the number. not to a minus sign.
						value2.flipSign();
						subfunction.remove(index+1);//get rid of the parsed minus sign
					}
				}			

				index--;

				subfunction.remove(index);
				subfunction.remove(index);
				subfunction.remove(index);

				Token exponent_token = new Token(value1, value2, operator, token_values.size());
				tokens.add(exponent_token);

				Value token_value = new TokenValue(value1.getFirstCharIndex(),value2.getLastCharIndex());//the last added token is the location of the token referenced by the tokenvalue.

				subfunction.add(index, token_value);//put the tokenvalue where the removed values were.~~~~AS IF NOTHING EVER HAPPENED~~~~
				token_values.add(token_value);

				last_index = subfunction.size()-1;
			}
		}

		return subfunction;
	}

	// take previous and next value and tokenize. Also ensured that if next value is a minus sign, and the value
	//after that is a negative number value, and if they both start on the same index in the function,
	//takes the two values and remove the negative sign.
	//returns a new value list with values that reference the created tokens
	public ArrayList<Value> parseMultiplyDivide(ArrayList<Value> function_values){
		ArrayList<Value> subfunction = function_values;
		int last_index = subfunction.size()-1;
		Value value3;
		int type=-1;// put in nonsense numbers so don't think its multiplication at first.
		int previous_type; //for remembering the last type operated on.
		for(int index = 0;index<=last_index;index++){
			//figure out if this is multiplication of things next to each other.
			previous_type = type;
			type = subfunction.get(index).getType();

			if((previous_type==Value.VARIABLE||previous_type==Value.TOKEN||previous_type==Value.NUMBER)
			    &&(type==Value.VARIABLE||type==Value.TOKEN||type==Value.NUMBER)
				){

				Value value1 = subfunction.get(index-1);
				
				int location = value1.getLastCharIndex();

				Value operator = new OperatorValue(location, location, '*');
				Value value2 = subfunction.get(index);

				index--;
				
				subfunction.remove(index);
				subfunction.remove(index);

				Token multiplier_token = new Token(value1, value2, operator, token_values.size());//create the appropriate token
				tokens.add(multiplier_token);
				
				Value token_value = new TokenValue(value1.getFirstCharIndex(),value2.getLastCharIndex());//the last added token is the location of the token referenced by the tokenvalue.
				token_values.add(token_value);

				subfunction.add(index, token_value);//put the tokenvalue where the removed values were.~~~~AS IF NOTHING EVER HAPPENED~~~~
				last_index = subfunction.size()-1;

			}

			if(type == Value.MULTIPLIER||type == Value.DIVISOR||type == Value.BACK_DIVISOR){
				Value operator = subfunction.get(index);//MULTIPLIER, DIVISOR, or BACK_DIVISOR type operator for token

				Value value1 = subfunction.get(index-1);
				Value value2 = subfunction.get(index+1);				

				//ensure that if the previous value is a minus sign or negative number, and the value
				//after that is a number value, make the value positive.
				if(subfunction.size()-index>2){
					value3 = subfunction.get(index+2);
					int value3_type = value3.getType();
					if((value2.getType() == Value.MINUS)&&(value3_type==Value.NUMBER||value3_type==Value.VARIABLE||value3_type==Value.TOKEN)){
						value2 = value3;//set the value to the number, not to a minus sign.
						value2.flipSign();
						subfunction.remove(index+1);//get rid of the parsed minus sign
					}
				}	

				index--;

				subfunction.remove(index);
				subfunction.remove(index);
				subfunction.remove(index);

				Token md_operator = new Token(value1, value2, operator, token_values.size());
				tokens.add(md_operator);

				Value token_value = new TokenValue(value1.getFirstCharIndex(),value2.getLastCharIndex());//the last added token is the location of the token referenced by the tokenvalue.
				subfunction.add(index, token_value);//put the tokenvalue where the removed values were.~~~~AS IF NOTHING EVER HAPPENED~~~~
				token_values.add(token_value);

				last_index = subfunction.size()-1;
			}
		}
		return subfunction;
	}

	// take previous and next value and tokenize. Also ensured that if next value is a minus sign, and the value
	//after that is a negative number value, and if they both start on the same index in the function,
	//takes the two values and remove the negative sign.
	//returns a new value list with values that reference the created tokens
	public ArrayList<Value> parseAddSubtract(ArrayList<Value> function_values){
		ArrayList<Value> subfunction = function_values;
		int type = subfunction.get(0).getType();//the type of the first value in the subfunction.
		if(type==Value.MINUS){ //if the function begins with a minus sign, make the second index negative and tokenize and parse.
			Value value1 = subfunction.get(1);//a number, token, or variable
			int value1_type = value1.getType();

			value1.flipSign();

			subfunction.remove(0);//remove the first value in subfunction (in this case a minus sign)
			subfunction.remove(0);//remove the new first value in subfunction (in this case a number/token/variable)

			subfunction.add(0, value1);//put the value where the removed values were.				
		}

		int last_index = subfunction.size()-1;
		for(int index = 1;index<=last_index;index++){ //start at the second value in the index.(0 is the first) and end on the second to last
			type = subfunction.get(index).getType();			
			if(type == Value.PLUS|type == Value.MINUS){
				
				Value operator = subfunction.get(index);//operator for token
				Value value1 = subfunction.get(index-1);
				Value value2 = subfunction.get(index+1);		

				index--;

				subfunction.remove(index);
				subfunction.remove(index);
				subfunction.remove(index);
				Token as_token = new Token(value1, value2, operator, token_values.size());
				tokens.add(as_token);

				Value token_value = new TokenValue(value1.getFirstCharIndex(),value2.getLastCharIndex());//the last added token is the location of the token referenced by the tokenvalue.
				subfunction.add(index, token_value);//put the tokenvalue where the removed values were.~~~~AS IF NOTHING EVER HAPPENED~~~~
				token_values.add(token_value);

				last_index = subfunction.size()-1;
			}
		}
		return subfunction;
	}


	public double evaluateNumber(String number){
		int function_size = function_values.size();
		for(int index = 0;index<function_size;index++){

		}
		return Double.parseDouble(number);
	}

	 //E1)if consecutive combination of /, *, +, and ^ 
	 //E2)if .. or . then \, *, +, -, ^ 
	 //E3)if number of '(' != number of ')'
	 //remove illegal characters in the function. Runs error code if error in basic function syntax. 
	public boolean checkFunction(String function) {
		if(function.length() == 0){
			System.out.println("Error: function is of zero length");
			System.exit(0);
		}
		String not_allowed_message = "Errors:\n";
		boolean syntax_error = false;
		char current_character;
		char previous_character = function.charAt(0);
		int begin_parenthesis = 0;
		int end_parenthesis = 0;
		//go from first character to last character 
		for(int index=0;index < function.length();index++){
			current_character = function.charAt(index);
			//sum the parenthesis to check if they are balanced later 
			if(current_character == '('){
				begin_parenthesis++;
			} else if(current_character == ')') {
				end_parenthesis++;
			} //empty parenthesis aren't allowed 
			
			if(current_character == ')' && previous_character == '('){
				not_allowed_message += "Error at character "+index+": Empty parenthesis aren't allowed.\n";
			syntax_error = true;
			}
			if(index != 0) {
				//E1)if consecutive combination of /, *, +, and ^ 
				if(current_character == '/'||current_character == '\\'||current_character == '*'||current_character == '^'||current_character == '+') {
					if(previous_character == '/'||previous_character == '\\'||previous_character == '*'||previous_character == '^'||previous_character == '+'||previous_character == '-'){
						not_allowed_message += "Error at character "+index+": A '"+current_character + "' was unexpected after a '"+previous_character+"'\n";
						syntax_error = true;
					}
				}
			}
			//ignoring the offending characters, add the character to the end of the string 
			if(this.allowed_characters.indexOf(current_character) == -1){
				not_allowed_message += "Error at character "+index+": The character '"+current_character+"' is not allowed.\n";
				syntax_error = true;
			}
			previous_character = current_character;//to check the last character is appropriate for the next loop.
		}

		//E3)if number of '(' != number of ')'
		if(begin_parenthesis != end_parenthesis) {
			syntax_error = true;
			not_allowed_message += "Error: Number of end parenthesis doesn't match number of beginning parenthesis. \n";
		}
		if(syntax_error){
			System.out.println(not_allowed_message);
			System.exit(0);
		}
	return syntax_error;
	}
}

class ValueComparer implements Comparator<Value>{
	@Override 
	public int compare(Value value1, Value value2) {
		if(value1.getFirstCharIndex() > value2.getFirstCharIndex()){
			return 1;
		} else {
			return -1;
		}
	}
}