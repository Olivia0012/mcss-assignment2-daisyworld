/**
 * main class for testing.
 *
 * @author Lu Wang
 * @Number 1054195
 * 
 *
 */

public class main {
	public static void main(String [] args){
		CsvWriter csvWriter = new CsvWriter(args[0]);
		DaisyWorld dw = new DaisyWorld(2, csvWriter); // the number is the type of solar luminosity. 2: our luminosity = 1.0.
		dw.execution(); // patches updating funtion.
	
	}
}
