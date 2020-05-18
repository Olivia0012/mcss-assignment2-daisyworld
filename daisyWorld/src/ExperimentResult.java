/**
 * ExperimentResult
 * Model used to store the experiment result in a format the CsvWriter requires
 *
 */

class ExperimentResult {
  int step_number;
  int number_white;
  int number_black;
  int number_empty;
  double global_temperature;
  
  public ExperimentResult(
    int step_number,
    int number_white, 
    int number_black, 
    int number_empty,
    double global_temperature){
    this.step_number = step_number;
    this.number_white = number_white;
    this.number_black = number_black;
    this.number_empty = number_empty;
    this.global_temperature = global_temperature;
  }

  public int getStepNumber() {return this.step_number;}
  public int getNumberWhite() {return this.number_white;}
  public int getNumberBlack() {return this.number_black;}
  public int getNumberEmpty() {return this.number_empty;}
  public double getGlobalTemp() {return this.global_temperature;}
}