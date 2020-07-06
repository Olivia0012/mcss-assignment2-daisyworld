/**
 * CsvWriter
 * This class is used to control how the program writes the results to a csv file
 *
 */

import java.io.*;
import java.util.ArrayList;

public class CsvWriter {
  String file_name;
  FileWriter file_writer;

  public CsvWriter(String file_name){
    this.file_name = file_name;
  }

  public boolean CreateFile(){
    try{
      // delete file if already exists
      File file = new File(this.file_name);
      if(file.exists()) file.delete();

      // create new csv file to write the data to
      this.file_writer = new FileWriter(this.file_name, true); 

      // csv column names
      ArrayList<String> column_names = new ArrayList<>();
      column_names.add("StepNumber");
      column_names.add("NumberWhite");
      column_names.add("NumberBlack");
      column_names.add("NumberEmpty");
      column_names.add("GlobalTemp");
      if(Params.SHOW_DAISY_MAP) column_names.add("DaisyMap");

      // display rain columns
      if(Params.RAIN_ENABLED){
        if(Params.SHOW_RAIN_MAP) column_names.add("RainMap");
        if(Params.SHOW_WATER_LEVEL) column_names.add("WaterLevel");
      }

      // write to file
      write(column_names);

      // let the caller know that the file was created successfully
      return true;

    } catch( Exception e) {
      System.out.println("Error: could not create file. "+e.getMessage());

      // let the caller know that the file was not able to be created
      return false;
    }
  }

  public void WriteToCsv(ExperimentResult message){
    // values to write to file
    ArrayList<String> values = new ArrayList<>();
    values.add(Integer.toString(message.getStepNumber()));
    values.add((Integer.toString(message.getNumberWhite())));
    values.add((Integer.toString(message.getNumberBlack())));
    values.add((Integer.toString(message.getNumberEmpty())));
    values.add(Double.toString(message.getGlobalTemp()));
    if(Params.SHOW_DAISY_MAP) values.add(message.getFormattedDaisies());
    
    // display rain output
    if(Params.RAIN_ENABLED){
      if(Params.SHOW_RAIN_MAP) values.add(message.getFormattedRainMap());
      if(Params.SHOW_WATER_LEVEL) values.add(message.getFormattedWaterLevel());
    }

    // write to file
    write(values);
  }

  public void CloseWriter(){
    try{
      this.file_writer.close();
    } catch( Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void write(ArrayList<String> values){
    try{
      // write simulation result to file
      this.file_writer.write(buildCsvString(values)+"\n");
      this.file_writer.flush();
    } catch( Exception e) {
      System.out.println("Error: could not append to file. "+e.getMessage());
    }
  }

  private String buildCsvString(ArrayList<String> values){
    StringBuilder csvBuilder = new StringBuilder();
    for(int i=0; i<values.size(); i++) {
      csvBuilder.append(values.get(i));
      if(i!=values.size()-1) csvBuilder.append(",");
    }
    return csvBuilder.toString();
  }
}
