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
    static final int PATCH_X_Y_NUM = 30;
    
    //Initial mumber of white daisies.
    static final int INI_WHITE = 180;
    
    //Initial mumber of black daisies.
    static final int INI_BLACK = 180;
    
    // max age of daisy.
    static final int MAX_AGE = 25;

    // albedo of white daisy
    static final double ALBEDO_WHITE = 0.75;
    
    // albedo of black daisy
    static final double ALBEDO_BLACK = 0.25;
    
    // albedo of empty patch
    static final double ALBEDO_SURFACE = 1;
    
    // initial solar luminosity of ramp up ramp down
    static final double RAMP_UP_RAMP_DOWN = 0.8;
    
    // low solar luminosity
    static final double LOW_SOLAR_LUMINOSITY = 0.6;
    
    // our solar luminosity
    static final double OUR_SOLAR_LUMINOSITY = 0.8;
    
    //high soalr luminosity
    static final double HIGH_SOLAR_LUMINOSITY = 1.4;

    //total number of ticks
    static final int TICKS = 1000;

    //toggle csv output
    static final boolean SHOW_DAISY_MAP = false;


    // ==============================================

    // toggle rain extension
    static final boolean RAIN_ENABLED = false;

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
    static final boolean SHOW_RAIN_MAP = false;
    static final boolean SHOW_WATER_LEVEL = false;

}
