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
    StringBuilder formatted_output = new StringBuilder();
    for(int i=0; i<this.patches.length; i++){
      formatted_output.append("[ ");
      for(int j=0; j<this.patches[i].length; j++){
        boolean raining = this.patches[i][j].isRaining();
        formatted_output.append(raining ? "Y" : "N");
        formatted_output.append(" ");
      }
      formatted_output.append("] ");
    }
    return formatted_output.toString();
  }

  public String getFormattedWaterLevel() {
    StringBuilder formatted_output = new StringBuilder();
    for(int i=0; i<this.patches.length; i++){
      formatted_output.append("[ ");
      for(int j=0; j<this.patches[i].length; j++){
        formatted_output.append(this.patches[i][j].getWaterLevel());
        formatted_output.append(" ");
      }
      formatted_output.append("] ");
    }
    return formatted_output.toString();
  }

  public String getFormattedDaisies() {
    StringBuilder formatted_output = new StringBuilder();
    for(int i=0; i<this.patches.length; i++){
      formatted_output.append("[ ");
      for(int j=0; j<this.patches[i].length; j++){
        Daisy daisy = this.patches[i][j].getDaisy();
        if(daisy != null){
          if(daisy.getColor() == 0){
            formatted_output.append("W");
          } else {
            formatted_output.append("B");
          }
        } else {
          formatted_output.append("_");
        }
        formatted_output.append(" ");
      }
      formatted_output.append("] ");
    }
    return formatted_output.toString();
  }
}