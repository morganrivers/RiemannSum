/*See Value Comments*/
public class OperatorValue extends Value {

	int operator_type;
	String operator_type_name;

	public OperatorValue(int first_char_index, int last_char_index, char operator){
		this.first_char_index = first_char_index;
		this.last_char_index = last_char_index;
		
		switch(operator){
			case '^': 
				this.operator_type = EXPONENT;
				this.operator_type_name = "EXPONENT";

				break;
			case '*': 
				this.operator_type = MULTIPLIER;
				this.operator_type_name = "MULTIPLIER";
				break;
			case '/':
				this.operator_type = DIVISOR;
				this.operator_type_name = "DIVISOR";
				break;
			case '\\':
				this.operator_type = BACK_DIVISOR;
				this.operator_type_name = "BACK_DIVISOR";
				break;
			case '-':
				this.operator_type = MINUS;
				this.operator_type_name = "MINUS";
				break;
			case '+':
				this.operator_type = PLUS;
				this.operator_type_name = "PLUS";
				break;
			case '(':
				this.operator_type = START_PARENTHESIS;
				this.operator_type_name = "START_PARENTHESIS";
				break;
			case ')':
				this.operator_type = END_PARENTHESIS;
				this.operator_type_name = "END_PARENTHESIS";
				break;				
			default:
				System.out.println("Error: Attempted to define a value of unknown type.");
				System.exit(0);
		}
	}

	//get the type of value
	public int getType(){
		return this.operator_type;
	}
	public void flipSign(){
		System.out.println("Error: Oh no! You tried to flip the sign of an non-number! Try fixing your minus signs.");
		System.exit(0);
	}
	public void setValue(double value){
		System.out.println("Error: Oh no! You tried to set the value of an non-number! Try fixing your minus signs.");
	}
	public String toString(){
		return " [Operator type= "+this.operator_type_name+" Begin="+this.first_char_index+" End="+this.last_char_index+"] ";
	}
	public double evaluate(double x){
		System.out.println("Error: Cannot evaluate an operator");
		System.exit(0);	
		return (double)operator_type;
	}
	public boolean signIsFlipped(){
		System.out.println("Error: you cant make an operatr negative value");
		System.exit(0);
		return false;
	}

}
