/*See Value Comments*/
public class VariableValue extends Value {

	boolean sign_is_flipped = false;
	double value;

	public VariableValue(int first_char_index, int last_char_index){
		this.first_char_index = first_char_index;
		this.last_char_index = last_char_index;
	}

	//get the type of value
	public int getType(){
		return VARIABLE; //2
	}

	public void flipSign(){
		if(sign_is_flipped == false){
			sign_is_flipped = true;
		} else {
			sign_is_flipped = false;
		} 
	}

	//This implementation simply asks another evaluated token what its value was and returns it.
	public double evaluate(double x){
		if(sign_is_flipped){
			return -x;
		}
		return x;
	}

	public void setValue(double value){
		this.value = value;
	}
	
	public String toString(){
		return " [Variable x Begin="+this.first_char_index+" End="+this.last_char_index+"] ";
	}
	public boolean signIsFlipped(){
		return sign_is_flipped;
	}

}