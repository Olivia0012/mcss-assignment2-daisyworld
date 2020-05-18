/**
 * Rain
 * This class controlls the rain
 *
 */

public class Rain {

  // This returns a new map with 
  public int[][] getNewRainVolumes(Patch[][] patches){
    int[][] rain_volumes = new int[patches.length][patches[0].length];

    for(int i=0; i<patches.length; i++){
      for(int j=0; j<patches[i].length; j++){
        // calculate what the new rain volume should be
        // int currentVolume = patches[i][j].getRainVolume();
        rain_volumes[i][j]=50;
      }
    }

    return rain_volumes;
  }

}