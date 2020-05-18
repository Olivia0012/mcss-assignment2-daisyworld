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
 		// create an initialise the csvWriter
		CsvWriter csv_writer = new CsvWriter(args[0]);

		// if file created correctly from the arguements
		if(csv_writer.CreateFile()){
			// the number is the type of solar luminosity. 2: our luminosity = 1.0.
			DaisyWorld dw = new DaisyWorld(2, csv_writer); 

			// patches updating funtion.
			dw.execution();

			// close the filewriter
			csv_writer.CloseWriter();

		} else{
			System.out.println("Simulation aborted because file could not be created. Please check your filepath and filename.");
		}
	}
}
