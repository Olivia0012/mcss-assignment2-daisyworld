
/**
 * JAVA Daisy World simulator class.
 * Assumption: the daisy world is round.
 * Functions in this class including seeding daisies, updating patches 
 * and calulating the number of daisies as well as global temperature.
 *
 * @author Lu Wang
 * @Number 1054195
 * 
 *
 */

import java.util.Random;

public class DaisyWorld {
	private int step_number = 0;
	private CsvWriter csv_writer;

	private double globalTemp; // global temperature
	private int num_black; // number of balck daisies.
	private int num_white; // number of white daisies.
	private int empty; // number of empty patches.
	private double solar_lum; // the current solar luminosity.
	private int scenario = 0; // 4 scenarios in the system. showing in the constructor.
	private Patch patches[][] = new Patch[Params.PATCH_X_Y_NUM][Params.PATCH_X_Y_NUM];
	static Random rnd = new Random();

	// constructor of the class.
	public DaisyWorld(int scenario, CsvWriter csv_writer) {
		this.csv_writer = csv_writer;
		this.scenario = scenario;
		if (scenario == 0) {
			// the luminosity will change during ticks.
			this.solar_lum = Params.RAMP_UP_RAMP_DOWN;
		} else if (scenario == 1) {
			this.solar_lum = Params.LOW_SOLAR_LUMINOSITY;
		} else if (scenario == 2) {
			this.solar_lum = Params.OUR_SOLAR_LUMINOSITY;
		} else if (scenario == 3) {
			this.solar_lum = Params.HIGH_SOLAR_LUMINOSITY;
		}

	}

	// execution for testing.
	public void execution() {
		this.step_number = 0; // reset step_number

		// initialise the patches with empty patch.
		for (int x = 0; x < Params.PATCH_X_Y_NUM; x++)
			for (int y = 0; y < Params.PATCH_X_Y_NUM; y++) {
				patches[x][y] = new Patch(null, solar_lum, x, y, Params.INI_SOIL_HYDRATION);
				patches[x][y].getLocal_temp();
			}

		getGlobalTemp();// get initial global temperature.

		// Seeding two daisies randomly in the patches.
		seedDaisyRandomly(1, Params.INI_BLACK);
		seedDaisyRandomly(0, Params.INI_WHITE);

		// Count the number of daisies and empty patches.
		getDaisy_Num();

		// Output current simulation state to CSV file
		csv_writer.WriteToCsv(new ExperimentResult(
			this.step_number, 
			this.num_white, 
			this.num_black, 
			this.empty, 
			this.globalTemp,
			this.patches
			));

		/*
		 * Update the patches for Params.TICKS times and record, calculate the gobal
		 * temperature after each update, and record the changes of the daisy numbers.
		 */
		for (this.step_number = 1; this.step_number < Params.TICKS; this.step_number++) {
			if (scenario == 0) {
				if (this.step_number > 200 && this.step_number <= 400) {
					solar_lum += 0.005;
				}
				if (this.step_number > 600 && this.step_number <= 850) {
					solar_lum -= 0.0025;
				}
			}

			daisyConsumeWater(); // Daisies consume water

			checkSurvival();// Sprout new daisies.

			// Count the number of daisies and empty patches.
			getDaisy_Num();

			double global_temp = getGlobalTemp();// get initial global temperature.
			setGlobalTemp(global_temp); // set the global temperature

			// Update hydration
			updateHydration();

			// Update the rain
			moveRain();

			// Output current simulation state to CSV file
			csv_writer.WriteToCsv(new ExperimentResult(
				this.step_number, 
				this.num_white, 
				this.num_black, 
				this.empty, 
				this.globalTemp,
				this.patches
				));
		}

	}

	// seeding daisies randomly at the begining.
	public void seedDaisyRandomly(int color, double initNum) {
		for (int i = 0; i < initNum; i++) {
			int x = rnd.nextInt(Params.PATCH_X_Y_NUM);
			int y = rnd.nextInt(Params.PATCH_X_Y_NUM);

			// find a patch randomly without daisies.
			while (patches[x][y].getDaisy() == null) {
				if (color == 0) {
					// white daisy.
					Daisy wDaisy = new Daisy(0, rnd.nextInt(Params.MAX_AGE));
					patches[x][y] = new Patch(wDaisy, solar_lum, x, y, patches[x][y].getWaterLevel());
				} else {
					// black daisy.
					Daisy bDaisy = new Daisy(1, rnd.nextInt(Params.MAX_AGE));
					patches[x][y] = new Patch(bDaisy, solar_lum, x, y, patches[x][y].getWaterLevel());
				}
			}

		}
	}

	/*
	 * Check daisies on each patch, and sprout a same color daisy on one of the
	 * empty neighbour patch.
	 */
	public void checkSurvival() {
		double seed_threshold;
		for (int x = 0; x < Params.PATCH_X_Y_NUM; x++)
			for (int y = 0; y < Params.PATCH_X_Y_NUM; y++) {

				// when the patch with a daisy.
				if (patches[x][y].getDaisy() != null) {
					// if there is a drought, daisy dies
					if(patches[x][y].getWaterLevel() <= Params.DROUGHT_HYDRATION_LEVEL){
						patches[x][y].setDaisy(null);
						continue;
					}

					// if there is a flood, daisy dies
					if(patches[x][y].getWaterLevel() >= Params.FLOOD_HYDRATION_LEVEL){
						patches[x][y].setDaisy(null);
						continue;
					}

					// increment its age.
					int daisyAge = patches[x][y].getDaisy().getAge() + 1;
					// update the daisy's age.
					patches[x][y].getDaisy().setAge(daisyAge);

					// when the daisy's age has not reached to 25.
					if (daisyAge < Params.MAX_AGE) {
						// when the daiy is a new born, it will not reproduct.
						if (daisyAge < 2)
							break;

						double temp = patches[x][y].getLocal_temp();

						// calculate the wether the temperature is suitable for reproduction.
						seed_threshold = 0.1457 * temp - 0.0032 * temp * temp - 0.6443;
						double randomValue = rnd.nextDouble();
						if (randomValue < seed_threshold) {
							Patch seedPlace = seed_place(x, y); // find am empty patch for seeding.
							if (seedPlace != null) {
								Daisy daisy = new Daisy(patches[x][y].getDaisy().getColor(), 0); // new born daisy.
								seedPlace.setDaisy(daisy);
								// seeding the new born to the suitable patches.
								patches[seedPlace.getX()][seedPlace.getY()] = seedPlace; 
							}
						}
					} else {
						// the daisy dies when it reachs 25.
						patches[x][y].setDaisy(null);// this daisy reached the max age.
					}
				}
			}
	}
	

	/* Finding an empty patch for seeding new daisies from one of the patch's 8 neighbours.
	 * when the patch is empty, put it into the candidate list, then randomly choose one 
	 * from this list.
	 * Description: This daisy world is round, so the last row will be the first row's neighbour,  
	 * the same as the columns, e.g.: when the position is in the first column, then the up 
	 * neighbour would be the same row in the last column, etc.
	 */
	public Patch seed_place(int x, int y) {
		Patch seed_Place = null;
		int m = 0;
		int n = 0;
		Patch emptyPatches[] = new Patch[8];
		
		// above: the above one.
		
		// when the position is in the first column, then the up 
		// neighbour would be the same row in the last column.
		if (y == 0) {
			// when the patch has no daisy on it, put it into the candidate list.
			if (patches[x][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[0] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else {
			if (patches[x][y - 1].getDaisy() == null) {
				m = x;
				n = y - 1;
				emptyPatches[0] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}

		// below: the below one.
		if (y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x][y + 1].getDaisy() == null) {
				m = x;
				n = y + 1;
				emptyPatches[1] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}

		} else {
			if (patches[x][0].getDaisy() == null) {
				m = x;
				n = 0;
				emptyPatches[1] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}

		// left: the left one.
		if (x == 0) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y;
				emptyPatches[2] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else {
			if (patches[x - 1][y].getDaisy() == null) {
				m = x - 1;
				n = y;
				emptyPatches[2] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}

		// left-up: the one above the left.
		if (x > 0 && y > 0) {
			if (patches[x - 1][y - 1].getDaisy() == null) {
				m = x - 1;
				n = y - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}else if (x > 0 && y == 0) {
			if (patches[x - 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x - 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else if (x == 0 && y > 0) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y - 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else {
			if (patches[Params.PATCH_X_Y_NUM - 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}
		// left-down: the one below the left.
		if (x > 0 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x - 1][y + 1].getDaisy() == null) {
				m = x - 1;
				n = y + 1;
				emptyPatches[4] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}else if (x > 0 && y == Params.PATCH_X_Y_NUM - 1) {
			if (patches[x - 1][0].getDaisy() == null) {
				m = x - 1;
				n = 0;
				emptyPatches[4] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else if (x == 0 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y + 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y + 1;
				emptyPatches[4] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else {
			if (patches[Params.PATCH_X_Y_NUM - 1][0].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = 0;
				emptyPatches[4] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}

		// right: the one on the right.
		
		// when the position is in the last row, then the right 
		// neighbour would be the one in the same column in the first row.
		if (x < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][y].getDaisy() == null) {
				m = x + 1;
				n = y;
				emptyPatches[5] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}else {
			if (patches[0][y].getDaisy() == null) {
				m = 0;
				n = y;
				emptyPatches[5] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}

		// right-up: the one above the right.
		if (x < Params.PATCH_X_Y_NUM - 1 && y > 0) {
			if (patches[x + 1][y - 1].getDaisy() == null) {
				m = x + 1;
				n = y - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else if (x < Params.PATCH_X_Y_NUM - 1 && y == 0) {
			if (patches[x + 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x + 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y > 0) {
			if (patches[0][y - 1].getDaisy() == null) {
				m = 0;
				n = y - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else {
			if (patches[0][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = 0;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}
		
		// right-down: the one below the right.
		if (x < Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][y + 1].getDaisy() == null) {
				m = x + 1;
				n = y + 1;
				emptyPatches[7] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else if (x < Params.PATCH_X_Y_NUM - 1 && y == Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][0].getDaisy() == null) {
				m = x + 1;
				n = 0;
				emptyPatches[7] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[0][y + 1].getDaisy() == null) {
				m = 0;
				n = y + 1;
				emptyPatches[7] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		} else {
			if (patches[0][0].getDaisy() == null) {
				m = 0;
				n = 0;
				emptyPatches[7] = new Patch(null, solar_lum, m, n, patches[m][n].getWaterLevel());
			}
		}
		
		//Choose one empty patch from the 8 neighbours.
		for (int i = 0; i < 8; i++) {
			int k = rnd.nextInt(7);
			while (emptyPatches[k] != null) {
				seed_Place = emptyPatches[k];
				break;
			}
		}

		return seed_Place;
	}

	
	// Calculate the number of daisies and empty patches.
	public void getDaisy_Num() {
		int countWhite = 0;
		int countBlack = 0;
		int empty = 0;
		for (int x = 0; x < Params.PATCH_X_Y_NUM; x++)
			for (int y = 0; y < Params.PATCH_X_Y_NUM; y++) {
				if (patches[x][y].getDaisy() != null) {
					if (patches[x][y].getDaisy().getColor() == 0)
						countWhite++;
					else
						countBlack++;
				} else
					empty++;
			}
		this.num_white = countWhite;
		this.num_black = countBlack;
		this.empty = empty;
	}

	public void setNum_black(int num_black) {
		this.num_black = num_black;
	}

	public int getNum_white() {
		return num_white;
	}

	public void setNum_white(int num_white) {
		this.num_white = num_white;
	}

	// Calculate global temperature.
	public double getGlobalTemp() {
		double tempDif = 0;

		for (int i = 0; i < Params.PATCH_X_Y_NUM; i++)
			for (int j = 0; j < Params.PATCH_X_Y_NUM; j++) {
				// get the total diffused temperature.
				tempDif += diffuse(patches[i][j], i, j);
			}
		
		globalTemp = tempDif / (Params.PATCH_X_Y_NUM * Params.PATCH_X_Y_NUM);

		return globalTemp; // return the global temperature after diffussion.
	}

	public void setGlobalTemp(double globalTemp) {
		this.globalTemp = globalTemp;
	}

	/*
	 * calculate the temperature with 50 % diffusion of daisies and patches around the daisy, 
	 * including the left, the left-up, the left-down, the right, the right-up, the right-down, the above
	 * and the below ones. 
	 * Description: the world is round, so the last row daisies would be those in the first row's neighbours,
	 * the same as column.
	 */
	public double diffuse(Patch patch, int x, int y) {
		double difTemp = 0;
		double local_temp = patch.getLocal_temp();
		difTemp = local_temp / 2; // 50% diffusion

		// above one:
		if (y == 0)
			difTemp += patches[x][Params.PATCH_X_Y_NUM - 1].getLocal_temp() / 16;
		else
			difTemp += patches[x][y - 1].getLocal_temp() / 16;

		// below one:
		if (y < Params.PATCH_X_Y_NUM - 1)
			difTemp += patches[x][y + 1].getLocal_temp() / 16;
		else
			difTemp += patches[x][0].getLocal_temp() / 16;

		// left:
		if (x == 0)
			difTemp += patches[Params.PATCH_X_Y_NUM - 1][y].getLocal_temp() / 16;
		else
			difTemp += patches[x - 1][y].getLocal_temp() / 16;

		// left-up
		if (x > 0 && y > 0)
			difTemp += patches[x - 1][y - 1].getLocal_temp() / 16;
		else if (x > 0 && y == 0) {
			difTemp += patches[x - 1][Params.PATCH_X_Y_NUM - 1].getLocal_temp() / 16;
		} else if (x == 0 && y > 0) {
			difTemp += patches[Params.PATCH_X_Y_NUM - 1][y - 1].getLocal_temp() / 16;
		} else {
			difTemp += patches[Params.PATCH_X_Y_NUM - 1][Params.PATCH_X_Y_NUM - 1].getLocal_temp() / 16;
		}

		// left-down
		if (x > 0 && y < Params.PATCH_X_Y_NUM - 1)
			difTemp += patches[x - 1][y + 1].getLocal_temp() / 16;
		else if (x > 0 && y == Params.PATCH_X_Y_NUM - 1) {
			difTemp += patches[x - 1][0].getLocal_temp() / 16;
		} else if (x == 0 && y < Params.PATCH_X_Y_NUM - 1) {
			difTemp += patches[Params.PATCH_X_Y_NUM - 1][y + 1].getLocal_temp() / 16;
		} else {
			difTemp += patches[Params.PATCH_X_Y_NUM - 1][0].getLocal_temp() / 16;
		}

		// right
		if (y == 0)
			difTemp += patches[x][Params.PATCH_X_Y_NUM - 1].getLocal_temp() / 16;
		else
			difTemp += patches[x][y - 1].getLocal_temp() / 16;

		// right-up
		if (x < Params.PATCH_X_Y_NUM - 1 && y > 0)
			difTemp += patches[x + 1][y - 1].getLocal_temp() / 16;
		else if (x < Params.PATCH_X_Y_NUM - 1 && y == 0) {
			difTemp += patches[x + 1][Params.PATCH_X_Y_NUM - 1].getLocal_temp() / 16;
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y > 0) {
			difTemp += patches[0][y - 1].getLocal_temp() / 16;
		} else {
			difTemp += patches[0][Params.PATCH_X_Y_NUM - 1].getLocal_temp() / 16;
		}
		// right-down
		if (x < Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1)
			difTemp += patches[x + 1][y + 1].getLocal_temp() / 16;
		else if (x < Params.PATCH_X_Y_NUM - 1 && y == Params.PATCH_X_Y_NUM - 1) {
			difTemp += patches[x + 1][0].getLocal_temp() / 16;
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1) {
			difTemp += patches[0][y + 1].getLocal_temp() / 16;
		} else {
			difTemp += patches[0][0].getLocal_temp() / 16;
		}
		patch.setLocal_temp(difTemp);
		return difTemp;
	}

  // Update rain locations
  private void moveRain(){
    for(int i=0; i<patches.length; i++)
      for(int j=0; j<patches[i].length; j++) {
				switch(Params.RAIN_SCENARIO){
					case ALWAYS_RAIN:
					patches[i][j].setIsRaining(true);
					break;
					case NEVER_RAIN:
					patches[i][j].setIsRaining(false);
					break;
					case RAIN_RANDOMLY:
					boolean rain = rnd.nextInt(100)<Params.POSSIBLITY_OF_RAIN;
					if(rain) patches[i][j].setIsRaining(true);
					else patches[i][j].setIsRaining(false);
					break;
				}
				
			}
  }

	// Update soil water level
	private void updateHydration(){
		for(int i=0; i<patches.length; i++)
			for(int j=0; j<patches[i].length; j++)
				if(patches[i][j].isRaining())
					patches[i][j].setWaterLevel(
						patches[i][j].getWaterLevel() + Params.HYDRATION_FROM_RAIN
					);
	}

	// Update the soil water because daisies are using it
	private void daisyConsumeWater(){
		for(int i=0; i<patches.length; i++)
			for(int j=0; j<patches[i].length; j++)
				if(patches[i][j].getDaisy() != null) {
					int new_water_level = patches[i][j].getWaterLevel() - Params.DAISY_WATER_CONSUMPTION;
					patches[i][j].setWaterLevel(
						new_water_level<0 ? 0 : new_water_level
					);
				}

	}
}
