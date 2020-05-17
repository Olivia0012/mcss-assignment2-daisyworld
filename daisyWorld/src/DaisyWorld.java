
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
import java.lang.Math;

public class DaisyWorld {
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
		// initialise the patches with empty patch.
		for (int x = 0; x < Params.PATCH_X_Y_NUM; x++)
			for (int y = 0; y < Params.PATCH_X_Y_NUM; y++) {
				patches[x][y] = new Patch(null, solar_lum, x, y);
				patches[x][y].getLocal_temp();
			}

		getGlobalTemp();// get initial global temperature.

		// Seeding two daisies randomly in the patches.
		seedDaisyRandomly(1, Params.INI_BLACK);
		seedDaisyRandomly(0, Params.INI_WHITE);

		// Count the number of daisies and empty patches.
		getDaisy_Num();
		csv_writer.WriteToCsv(new ExperimentResult(num_white, num_black, empty, this.globalTemp));

		/*
		 * Update the patches for Params.TICKS times and record, calculate the gobal
		 * temperature after each update, and record the changes of the daisy numbers.
		 */
		for (int i = 0; i < Params.TICKS; i++) {
			if (scenario == 0) {
				if (i > 200 && i <= 400) {
					solar_lum += 0.005;
				}
				if (i > 600 && i <= 850) {
					solar_lum -= 0.0025;
				}
			}
			checkSurvival();// Sprout new daisies.

			// Count the number of daisies and empty patches.
			getDaisy_Num();
			csv_writer.WriteToCsv(new ExperimentResult(num_white, num_black, empty, this.globalTemp));

			getGlobalTemp();// get initial global temperature.
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
					patches[x][y] = new Patch(wDaisy, solar_lum, x, y);
				} else {
					// black daisy.
					Daisy bDaisy = new Daisy(1, rnd.nextInt(Params.MAX_AGE));
					patches[x][y] = new Patch(bDaisy, solar_lum, x, y);
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
				emptyPatches[0] = new Patch(null, solar_lum, m, n);
			}
		} else {
			if (patches[x][y - 1].getDaisy() == null) {
				m = x;
				n = y - 1;
				emptyPatches[0] = new Patch(null, solar_lum, m, n);
			}
		}

		// below: the below one.
		if (y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x][y + 1].getDaisy() == null) {
				m = x;
				n = y + 1;
				emptyPatches[1] = new Patch(null, solar_lum, m, n);
			}

		} else {
			if (patches[x][0].getDaisy() == null) {
				m = x;
				n = 0;
				emptyPatches[1] = new Patch(null, solar_lum, m, n);
			}
		}

		// left: the left one.
		if (x == 0) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y;
				emptyPatches[2] = new Patch(null, solar_lum, m, n);
			}
		} else {
			if (patches[x - 1][y].getDaisy() == null) {
				m = x - 1;
				n = y;
				emptyPatches[2] = new Patch(null, solar_lum, m, n);
			}
		}

		// left-up: the one above the left.
		if (x > 0 && y > 0) {
			if (patches[x - 1][y - 1].getDaisy() == null) {
				m = x - 1;
				n = y - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n);
			}
		}else if (x > 0 && y == 0) {
			if (patches[x - 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x - 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n);
			}
		} else if (x == 0 && y > 0) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y - 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n);
			}
		} else {
			if (patches[Params.PATCH_X_Y_NUM - 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[3] = new Patch(null, solar_lum, m, n);
			}
		}
		// left-down: the one below the left.
		if (x > 0 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x - 1][y + 1].getDaisy() == null) {
				m = x - 1;
				n = y + 1;
				emptyPatches[4] = new Patch(null, solar_lum, m, n);
			}
		}else if (x > 0 && y == Params.PATCH_X_Y_NUM - 1) {
			if (patches[x - 1][0].getDaisy() == null) {
				m = x - 1;
				n = 0;
				emptyPatches[4] = new Patch(null, solar_lum, m, n);
			}
		} else if (x == 0 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y + 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y + 1;
				emptyPatches[4] = new Patch(null, solar_lum, m, n);
			}
		} else {
			if (patches[Params.PATCH_X_Y_NUM - 1][0].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = 0;
				emptyPatches[4] = new Patch(null, solar_lum, m, n);
			}
		}

		// right: the one on the right.
		
		// when the position is in the last row, then the right 
		// neighbour would be the one in the same column in the first row.
		if (x < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][y].getDaisy() == null) {
				m = x + 1;
				n = y;
				emptyPatches[5] = new Patch(null, solar_lum, m, n);
			}
		}else {
			if (patches[0][y].getDaisy() == null) {
				m = 0;
				n = y;
				emptyPatches[5] = new Patch(null, solar_lum, m, n);
			}
		}

		// right-up: the one above the right.
		if (x < Params.PATCH_X_Y_NUM - 1 && y > 0) {
			if (patches[x + 1][y - 1].getDaisy() == null) {
				m = x + 1;
				n = y - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n);
			}
		} else if (x < Params.PATCH_X_Y_NUM - 1 && y == 0) {
			if (patches[x + 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x + 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n);
			}
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y > 0) {
			if (patches[0][y - 1].getDaisy() == null) {
				m = 0;
				n = y - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n);
			}
		} else {
			if (patches[0][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = 0;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[6] = new Patch(null, solar_lum, m, n);
			}
		}
		
		// right-down: the one below the right.
		if (x < Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][y + 1].getDaisy() == null) {
				m = x + 1;
				n = y + 1;
				emptyPatches[7] = new Patch(null, solar_lum, m, n);
			}
		} else if (x < Params.PATCH_X_Y_NUM - 1 && y == Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][0].getDaisy() == null) {
				m = x + 1;
				n = 0;
				emptyPatches[7] = new Patch(null, solar_lum, m, n);
			}
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[0][y + 1].getDaisy() == null) {
				m = 0;
				n = y + 1;
				emptyPatches[7] = new Patch(null, solar_lum, m, n);
			}
		} else {
			if (patches[0][0].getDaisy() == null) {
				m = 0;
				n = 0;
				emptyPatches[7] = new Patch(null, solar_lum, m, n);
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
		
		csv_writer.WriteToCsv(new ExperimentResult(num_white, num_black, empty, globalTemp));

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

}
