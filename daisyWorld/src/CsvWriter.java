import java.io.*;

public class CsvWriter {
  FileWriter file_writer;

  public CsvWriter(String fileName){
    try{
      this.file_writer = new FileWriter(fileName,true); 
      this.file_writer.write("NumberWhite,NumberBlack,NumberEmpty,GlobalTemp\n");
      this.file_writer.flush();
    } catch( Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void WriteToCsv(ExperimentResult message){
    try{
      this.file_writer.write(
        message.getNumberWhite()+","+
        message.getNumberBlack()+","+
        message.getNumberEmpty()+","+
        message.getGlobalTemp()+
        "\n");
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
