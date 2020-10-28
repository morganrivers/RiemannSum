/*
Value is the lowest component of functions, and belongs to a token. Values can be constructed and evaluated.
Values come in one of the following value types:
1: NUMBER, which is saved as an internal value upon creation of the value object.
2: VARIABLE, which is x, and is specified as a number upon calling the evaluate function.
3: TOKEN, which is the token_location of a token that the value does not belong to. The token_location of the token is set upon construction of the value.
The rest are of type OperatorValue
4: EXPONENT
5: MULTIPLIER
6: DIVISOR
7: MINUS
8: PLUS
See class Token comments.
*/
import java.util.*;
import java.util.ArrayList;// Import ArrayList 
public abstract class Value {
	public static final int NUMBER = 0;
	public static final int VARIABLE = 1;
	public static final int TOKEN = 2;
	public static final int EXPONENT = 3;
	public static final int MULTIPLIER = 4;
	public static final int DIVISOR = 5;
	public static final int BACK_DIVISOR = 6;
	public static final int MINUS = 7;
	public static final int PLUS = 8;
	public static final int START_PARENTHESIS = 9;
	public static final int END_PARENTHESIS = 10;

	public ArrayList<Token> tokens = new ArrayList<Token>();

	public int last_char_index; //used to locate this value's last character's location in the user inputted function string.
	public int first_char_index; //used to locate this value's first character's location in the user inputted function string.
	//allow the values to be linked to operations in the value string.

	//get the type of value
	public int getFirstCharIndex(){
		return this.first_char_index;
	}

	//get the type of value
	public int getLastCharIndex(){
		return this.last_char_index;
	}

	//initialize the tokens arraylist
	public void initializeTokens(ArrayList<Token> tokens){
        this.tokens = tokens;
   	}

	public abstract boolean signIsFlipped();
	public abstract void setValue(double value);
	public abstract double evaluate(double x);
	public abstract int getType();
	public abstract void flipSign();

}