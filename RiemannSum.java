import java.util.Scanner;
import java.util.*;
import java.util.ArrayList;// Import ArrayList 
/**
 *
 * Java program to find riemann sums in  Java.
 * Basically contains all the math you would need to understand the 
 * Riemann sum mathematical modeling aspects of this program
 */
public class RiemannSum
{
    public static void main(String args[]) {
      
        //Used to get user input for values
        Scanner scanner = new Scanner(System.in);

        
        System.out.println("======Riemann Sum Calculator======"+
        	"\n This Program will calculate the left endpoint Riemann sum in terms of the variable x."+
        	"\n Enter the n value (number of boxes)");

        int n = scanner.nextInt();

        System.out.println("Supply the lower x bound, then press [ENTER]");

        double lower_bound = scanner.nextDouble();

       System.out.println("Supply the upper x bound, then press [ENTER]");

        double upper_bound = scanner.nextDouble();

        System.out.println("Supply a function of x, then press [ENTER]"+
        "\nFor Example: (x^2-3*x^4+5x/(-6))\\1"+
        "\nAllowed characters are: 1234567890.()+-*^xX\\/"+
        "\n");
		
        Scanner scan = new Scanner(System.in);

		String function = scan.nextLine(); //get the function to evaluate

        Parser equationParser = new Parser();
		equationParser.checkFunction(function);
        
        ArrayList<Value> values = new ArrayList<Value>();//create an array of values that will come to represent the function in a list of numbers and variables 
        ArrayList<Token> tokens = new ArrayList<Token>();
        ArrayList<Value> token_values = new ArrayList<Value>();

        values = equationParser.parse(function);
        tokens = equationParser.getTokens();
        token_values = equationParser.getTokenValues();

        int token_size = tokens.size();
        Value first_value = values.get(0);

     	double sum = 0; 
        double y=0;
        double x = lower_bound;
     	double rectangle_width = (upper_bound-lower_bound)/(n);
		System.out.println("width: "+rectangle_width);

     	for(int count = 1;count <= n;count++) {

            if(token_size != 0){
                for(int index = 0;index<token_size;index++){
                    y = tokens.get(index).evaluate(x);//keeps evaluating each token until y is replaced by the final value
                    if(first_value.signIsFlipped()){
                        y = -y;
                    }
                }
            }else{    
                y = first_value.evaluate(x);
            }
            sum += y;

            x+=rectangle_width;
        }
        double riemann_sum = sum*rectangle_width;
     	System.out.println("\n===============Sum================"+
     	"\n Riemann Sum = "+ riemann_sum);
    }
}
