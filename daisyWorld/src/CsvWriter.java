import java.io.*;

public class CsvWriter {
  FileWriter file_writer;
  
  public CsvWriter(String fileName){
    try{
      this.file_writer = new FileWriter(fileName,true); 
    } catch( Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void WriteToCsv(String message){
    try{
      this.file_writer.write(message+"\n");
      this.file_writer.flush();
    } catch( Exception e) {
      System.out.println(e.getMessage());
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
