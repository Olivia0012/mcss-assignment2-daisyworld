/**
 * CsvWriter
 * This class is used to control how the program writes the results to a csv file
 *
 */

import java.io.*;

public class CsvWriter {
  String file_name;
  FileWriter file_writer;

  public CsvWriter(String file_name){
    this.file_name = file_name;
  }

  public Boolean CreateFile(){
    try{
      // delete file if already exists
      File file = new File(this.file_name);
      if(file.exists()) file.delete();

      // create new csv file to write the data to
      this.file_writer = new FileWriter(this.file_name, true); 
      this.file_writer.write("StepNumber,NumberWhite,NumberBlack,NumberEmpty,GlobalTemp\n");
      this.file_writer.flush();

      // let the caller know that the file was created successfully
      return true;

    } catch( Exception e) {
      System.out.println("Error: could not create file. "+e.getMessage());

      // let the caller know that the file was not able to be created
      return false;
    }
  }

  public void WriteToCsv(ExperimentResult message){
    try{
      // write simulation result to file
      this.file_writer.write(
        message.getStepNumber()+","+
        message.getNumberWhite()+","+
        message.getNumberBlack()+","+
        message.getNumberEmpty()+","+
        message.getGlobalTemp()+
        "\n");
      this.file_writer.flush();
    } catch( Exception e) {
      System.out.println("Error: could not append to file. "+e.getMessage());
    }
  }

  public void CloseWriter(){
    try{
      this.file_writer.close();
    } catch( Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
