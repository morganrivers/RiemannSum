/*
Token is a combination of two values, either taken from another token, number values, or a variable, and an operation, 
which determines how to modify the values based on each other. Tokens also have a location, which determines
when the token is executed. Tokens are placed in an arraylist by location, then simplified, then used to 
evaluate in an array. 

Token token
 - int location
 - Value value1 //if of token type, must have a location before the token's location.
 	+int value_type
 		*NUMBER = 0; //if true, it will use double value
 		*VARIABLE = 1;//will ignore value
 		*TOKEN = 2//will use the location int as a token location. only possible if the associated token comes first in the array.
 	+double value
 	+int location
 - Value value2
 - int operation
 	+can be exponent, multiplication, division, addition, subtraction. 
 - boolean simplifiable

 For example:
   EQUATION     ARRAY    |Values
 (x+2)(x^2-2^3)          |(x)
      |__|      T0:x^2   |
           |__| T1:2^3   |
      |_______| T2:T0-T1 |
 |____|         T3:x+2   |
 |____________| T4:T2*T4 |
first the 'x^2' is tokenized, with:
	
Token T0
 - Value value1
	+int value_type = 1;
 - Value value2
 	+int value_type = 0;
 	+double value = 2.0;
 - int operation = POWER

*/
import java.util.*;
import java.util.ArrayList;// Import ArrayList 
public class Token {
	double value;
	Value value1;
	Value value2;
	Value operation;
	boolean simplifiable;
	int token_value_location;

	public Token(Value value1, Value value2,Value operation,int token_value_location){
		this.value1 = value1;
		this.value2 = value2;
		this.operation = operation;
		this.token_value_location = token_value_location;
	}

	public double evaluate(double x){
		switch(this.operation.getType()) {
			case Value.EXPONENT: //POWER
				this.value = Math.pow(this.value1.evaluate(x),this.value2.evaluate(x));
				break;
			case Value.MULTIPLIER: //multiply
				this.value = this.value1.evaluate(x)*this.value2.evaluate(x);
				break;
			case Value.DIVISOR: //divide 1 by 2
				this.value = this.value1.evaluate(x)/this.value2.evaluate(x);
				break;
			case Value.BACK_DIVISOR: //divide 2 by 1
				this.value = this.value2.evaluate(x)/this.value1.evaluate(x);
				break;
			case Value.PLUS: //add
				this.value = this.value1.evaluate(x)+this.value2.evaluate(x);
				break;
			case Value.MINUS: //subtract 1 from 2
				this.value = this.value1.evaluate(x)-this.value2.evaluate(x);
				break;
			default:
				System.out.println("Error: impossible operator type.\n");
				System.exit(0);
		}

		Parser.token_values.get(token_value_location).setValue(this.value);
        Value token_value = Parser.token_values.get(token_value_location);

		return this.value;

	}


	public boolean canSimplify(){
		if((this.value1.getType() == Value.NUMBER)&&(this.value2.getType() == Value.NUMBER)){
			return true;
		}
		return false;
	}
	public String toString(){
		String type_symbol = "";
		switch(this.operation.getType()) {
			case Value.EXPONENT: //POWER
				type_symbol = "^";
				break;
			case Value.MULTIPLIER: //multiply
				type_symbol = "*";
				break;
			case Value.DIVISOR: //divide 1 by 2
				type_symbol = "/";
				break;
			case Value.BACK_DIVISOR: //divide 2 by 1
				type_symbol = "\\";
				break;
			case Value.PLUS: //add
				type_symbol = "+";
				break;
			case Value.MINUS: //subtract 1 from 2
				type_symbol = "-";
				break;
			default:
				System.out.println("Error: impossible operator type.\n");
				System.exit(0);
		}
		return "{ TokenValue"+token_value_location+" "+value1.toString()+" "+type_symbol+" "+value2.toString()+"}";
	}
}