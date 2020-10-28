/*See Value Comments*/
public class TokenValue extends Value {

	double value;
	boolean sign_flipped;

	public TokenValue(int first_char_index, int last_char_index){
		this.first_char_index = first_char_index;
		this.last_char_index = last_char_index;
	}
	public TokenValue(int first_char_index, int last_char_index, double value){
		this.first_char_index = first_char_index;
		this.last_char_index = last_char_index;
		this.value = value;
	}
	//get the type of value
	public int getType(){
		return TOKEN; //1
	}

	public void flipSign(){
		if(sign_flipped){
			sign_flipped = false;
		} else {
			sign_flipped = true;
		}
	}

	public void setValue(double value){
		this.value = value;
	}

	public boolean signIsFlipped(){
		return sign_flipped;	
	}

	//This implementation simply asks another evaluated token what its value was and returns it.
	public double evaluate(double x){

		if(sign_flipped){
			return -value;
		}
		return value;
	}

	public String toString(){
		String negative_message = "";
		if(sign_flipped){
			negative_message = "Negative ";
		}
		return " ["+negative_message+"Token Begin="+this.first_char_index+" End="+this.last_char_index+"] ";
	}
}