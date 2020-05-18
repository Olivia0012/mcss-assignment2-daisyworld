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
  Patch patches[][];
  
  public ExperimentResult(
    int step_number,
    int number_white, 
    int number_black, 
    int number_empty,
    double global_temperature,
    Patch[][] patches){
    this.step_number = step_number;
    this.number_white = number_white;
    this.number_black = number_black;
    this.number_empty = number_empty;
    this.global_temperature = global_temperature;
    this.patches = patches;
  }

  public int getStepNumber() {return this.step_number;}
  public int getNumberWhite() {return this.number_white;}
  public int getNumberBlack() {return this.number_black;}
  public int getNumberEmpty() {return this.number_empty;}
  public double getGlobalTemp() {return this.global_temperature;}
  public String getFormattedRainMap() {
    String rain_volumes = "";
    for(int i=0; i<this.patches.length; i++){
      rain_volumes += "[ ";
      for(int j=0; j<this.patches[i].length; j++){
        rain_volumes+=this.patches[i][j].getRainVolume()+" ";
      }
      rain_volumes += "] ";
    }
    return rain_volumes;
  }
}