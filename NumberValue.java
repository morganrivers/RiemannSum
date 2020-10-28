/*See Value Comments*/
public class NumberValue extends Value {

	double value;

	public NumberValue(int first_char_index, int last_char_index, double value){
		this.value = value;
		this.first_char_index = first_char_index;
		this.last_char_index = last_char_index;

	}

	//get the type of value
	public int getType(){
		return NUMBER; //1
	}
	
	public void setValue(double value){
		this.value = value;
	}

	public void flipSign(){
		this.value = -this.value;
	}

	public double evaluate(double x){
		return this.value;
	}
	
	public String toString(){
		return " [Number value= "+this.value+" Begin="+this.first_char_index+" End="+this.last_char_index+"] ";
	}
	public boolean signIsFlipped(){
		return false;
	}

}