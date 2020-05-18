/**
 * Parameters for the JAVA Daisy World simulator.
 * Changes the value of parameters to do experiments.
 *
 * @author Lu Wang
 * @Number 1054195
 * 
 *
 */



class Params {
	
	//Number of x and y of patch array.
    static final int PATCH_X_Y_NUM = 20;
    
    //Initial mumber of white daisies.
    static final int INI_WHITE = 10;
    
    //Initial mumber of black daisies.
    static final int INI_BLACK = 10;
    
    // max age of daisy.
    static final int MAX_AGE = 25;

    // albedo of white daisy
    static final double ALBEDO_WHITE = 0.5;
    
    // albedo of black daisy
    static final double ALBEDO_BLACK = 0.2;
    
    // albedo of empty patch
    static final double ALBEDO_SURFACE = 0.3;
    
    // initial solar luminosity of ramp up ramp down
    static final double RAMP_UP_RAMP_DOWN = 0.8;
    
    // low solar luminosity
    static final double LOW_SOLAR_LUMINOSITY = 0.6;
    
    // our solar luminosity
    static final double OUR_SOLAR_LUMINOSITY = 1.0;
    
    //high soalr luminosity
    static final double HIGH_SOLAR_LUMINOSITY = 1.4;
    
    
    //total number of ticks
    static final int TICKS = 1000;
    
    
    //toggle csv output
    static final boolean SHOW_RAIN_MAP = false;

}
