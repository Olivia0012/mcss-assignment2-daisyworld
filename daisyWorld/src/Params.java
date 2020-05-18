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
    static final int PATCH_X_Y_NUM = 5;
    
    //Initial mumber of white daisies.
    static final int INI_WHITE = 5;
    
    //Initial mumber of black daisies.
    static final int INI_BLACK = 5;
    
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

    // rain scenario
    static final RainScenario RAIN_SCENARIO = RainScenario.RAIN_RANDOMLY;

    // possibility of rain
    static final int POSSIBLITY_OF_RAIN = 20;

    // initial water level in the soil
    static final int INI_SOIL_HYDRATION = 10;
    
    // hydration from rain
    static final int HYDRATION_FROM_RAIN = 4;

    // drown from flooding
    static final int FLOOD_HYDRATION_LEVEL = 20;

    // die from drought
    static final int DROUGHT_HYDRATION_LEVEL = 0;
    
    // daily water consumption by a daisy
    static final int DAISY_WATER_CONSUMPTION = 1;

    //toggle csv output
    static final boolean SHOW_RAIN_MAP = true;
    static final boolean SHOW_WATER_LEVEL = true;
    static final boolean SHOW_DAISY_MAP = true;

}
