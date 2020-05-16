
/**
 * JAVA Daisy World simulator class.
 *
 * @author Lu Wang
 * @Number 1054195
 * 
 *
 */

import java.util.Random;
import java.lang.Math;

public class DaisyWorld {
	private double globalTemp;
	private int num_black;
	private int num_white;
	private int empty;
	private double localTemp_empty;
	private double solar_lum;
	private int scenario = 0;
	private Patch patches[][] = new Patch[Params.PATCH_X_Y_NUM][Params.PATCH_X_Y_NUM];

	static Random rnd = new Random();

	public DaisyWorld(int scenario) {
		this.scenario = scenario;
		if (scenario == 0) {
			this.solar_lum = Params.RAMP_UP_RAMP_DOWN;
		} else if (scenario == 1) {
			this.solar_lum = Params.LOW_SOLAR_LUMINOSITY;
		} else if (scenario == 2) {
			this.solar_lum = Params.OUR_SOLAR_LUMINOSITY;
		} else if (scenario == 3) {
			this.solar_lum = Params.HIGH_SOLAR_LUMINOSITY;
		}

	}

	private double getRandomSoilQuality() {
		return rnd.nextDouble();
	}

	public void execution() {
	//	getGlobalTemp();
		for (int x = 0; x < Params.PATCH_X_Y_NUM; x++)
			for (int y = 0; y < Params.PATCH_X_Y_NUM; y++) {
				double soil_quality = getRandomSoilQuality();
				patches[x][y] = new Patch(null, solar_lum, soil_quality, x, y);
				patches[x][y].getLocal_temp();
			}
		getGlobalTemp();
		seedDaisyRandomly(1, 60);
		seedDaisyRandomly(0, 50);
		
		getDaisy_Num();
		System.out.println("White: " + num_white);
		System.out.println("Black: " + num_black);
		System.out.println("empty: " + empty);
		
		for (int i = 0; i < Params.TICKS; i++) {
			if(scenario == 0.8) {
				if(i > 200 && i <= 400) {
					solar_lum += 0.005;
				}
				if(i > 600 && i <= 850) {
					solar_lum -= 0.0025;
				}
			}
			checkSurvival();
			getDaisy_Num();
			System.out.println("White: " + num_white);
			System.out.println("Black: " + num_black);
			getGlobalTemp();
		}

	}


	// seeding daisies randomly at the begining.
	public void seedDaisyRandomly(int color, double initNum) {
		for (int i = 0; i < initNum; i++) {
			int x = rnd.nextInt(Params.PATCH_X_Y_NUM);
			int y = rnd.nextInt(Params.PATCH_X_Y_NUM);

			while (patches[x][y].getDaisy() == null) {
				if (color == 0) {
					Daisy wDaisy = new Daisy(0, rnd.nextInt(Params.MAX_AGE));
					double soil_quality = getRandomSoilQuality();
					patches[x][y] = new Patch(wDaisy, solar_lum, soil_quality, x, y);
				} else {
					Daisy bDaisy = new Daisy(1, rnd.nextInt(Params.MAX_AGE));
					double soil_quality = getRandomSoilQuality();
					patches[x][y] = new Patch(bDaisy, solar_lum, soil_quality, x, y);
				}
			}

		}
	}

	public void checkSurvival() {
		double seed_threshold;
		for (int x = 0; x < Params.PATCH_X_Y_NUM; x++)
			for (int y = 0; y < Params.PATCH_X_Y_NUM; y++) {
				if (patches[x][y].getDaisy() != null) {
					int daisyAge = patches[x][y].getDaisy().getAge() + 1;
					double soil_quality = patches[x][y].getSoilQuality();
					patches[x][y].getDaisy().setAge(daisyAge);
					if (daisyAge < Params.MAX_AGE) {
						if(daisyAge < 2) break;
						double temp = patches[x][y].getLocal_temp();
						seed_threshold = 0.1457 * temp - 0.0032 * temp * temp - 0.6443;
						double randomValue = rnd.nextDouble();
						if (randomValue < seed_threshold) {
							Patch seedPlace = seed_place(x, y);
							if (seedPlace != null) {

								// Extended the model: 
								// Soil quality has to be good enough for new daisies to grow
								if(soil_quality>Params.GOOD_SOIL_QUALITY){
									// Good soil quality
									// System.out.println("Good soil quality: " + soil_quality);
									Daisy daisy = new Daisy(patches[x][y].getDaisy().getColor(),
									0);
									seedPlace.setDaisy(daisy);
									patches[seedPlace.getX()][seedPlace.getY()] = seedPlace;
								}else {
									// Bad soil quality
									// System.out.println("Bad soil quality: " + soil_quality);
								}
							}
						}
					} else {
						patches[x][y].setDaisy(null);// this daisy reached the max age.
					}
				}
			}
	}

	public Patch seed_place(int x, int y) {
		Patch seed_Place = null;
		int m = 0;
		int n = 0;
		Patch emptyPatches[] = new Patch[8];
		double soil_quality = getRandomSoilQuality();

		// up:
		if (y == 0) {
			if (patches[x][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[0] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}

		} else {
			if (patches[x][y - 1].getDaisy() == null) {
				m = x;
				n = y - 1;
				emptyPatches[0] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		}

		// down:
		if (y < Params.PATCH_X_Y_NUM - 1) {
			if(patches[x][y + 1].getDaisy() == null) {
				m = x;
				n = y + 1;
				emptyPatches[1] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		
		}else {
			if(patches[x][0].getDaisy() == null) {
				m = x;
				n = 0;
				emptyPatches[1] = new Patch(null, solar_lum, soil_quality, m, n);
				//return seed_Place;
			}
		}

		// left:
		if (x == 0) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y;
				emptyPatches[2] = new Patch(null, solar_lum, soil_quality, m, n);
				//return seed_Place;
			}
		} else {
			if (patches[x - 1][y].getDaisy() == null) {
				m = x - 1;
				n = y;
				emptyPatches[2] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		}

		// left-up
		if (x > 0 && y > 0) {
			if (patches[x - 1][y - 1].getDaisy() == null) {
				m = x - 1;
				n = y - 1;
				emptyPatches[3] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		}

		else if (x > 0 && y == 0) {
			if (patches[x - 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x - 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[3] = new Patch(null, solar_lum, soil_quality, m, n);
				//return seed_Place;
			}
		} else if (x == 0 && y > 0) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y - 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y - 1;
				emptyPatches[3] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		} else {
			if (patches[Params.PATCH_X_Y_NUM - 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[3] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		}
		// left-down
		if (x > 0 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x - 1][y + 1].getDaisy() == null) {
				m = x - 1;
				n = y + 1;
				emptyPatches[4] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		}

		else if (x > 0 && y == Params.PATCH_X_Y_NUM - 1) {
			if (patches[x - 1][0].getDaisy() == null) {
				m = x - 1;
				n = 0;
				emptyPatches[4] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		} else if (x == 0 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[Params.PATCH_X_Y_NUM - 1][y + 1].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = y + 1;
				emptyPatches[4] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		} else {
			if (patches[Params.PATCH_X_Y_NUM - 1][0].getDaisy() == null) {
				m = Params.PATCH_X_Y_NUM - 1;
				n = 0;
				emptyPatches[4] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		}

		// right
		if (x < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][y].getDaisy() == null) {
				m = x + 1;
				n = y;
				emptyPatches[5] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		}

		else {
			if (patches[0][y].getDaisy() == null) {
				m = 0;
				n = y;
				emptyPatches[5] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		}

		// right-up
		if (x < Params.PATCH_X_Y_NUM - 1 && y > 0) {
			if (patches[x + 1][y - 1].getDaisy() == null) {
				m = x + 1;
				n = y - 1;
				emptyPatches[6] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		} else if (x < Params.PATCH_X_Y_NUM - 1 && y == 0) {
			if (patches[x + 1][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = x + 1;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[6] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y > 0) {
			if (patches[0][y - 1].getDaisy() == null) {
				m = 0;
				n = y - 1;
				emptyPatches[6] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		} else {
			if (patches[0][Params.PATCH_X_Y_NUM - 1].getDaisy() == null) {
				m = 0;
				n = Params.PATCH_X_Y_NUM - 1;
				emptyPatches[6] = new Patch(null, solar_lum, soil_quality, m, n);
		//		return seed_Place;
			}
		}
		// right-down
		if (x < Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][y + 1].getDaisy() == null) {
				m = x + 1;
				n = y + 1;
				emptyPatches[7] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		} else if (x < Params.PATCH_X_Y_NUM - 1 && y == Params.PATCH_X_Y_NUM - 1) {
			if (patches[x + 1][0].getDaisy() == null) {
				m = x + 1;
				n = 0;
				emptyPatches[7] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		} else if (x == Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM - 1) {
			if (patches[0][y + 1].getDaisy() == null) {
				m = 0;
				n = y + 1;
				emptyPatches[7] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		} else {
			if (patches[0][0].getDaisy() == null) {
				m = 0;
				n = 0;
				emptyPatches[7] = new Patch(null, solar_lum, soil_quality, m, n);
			//	return seed_Place;
			}
		}
		for (int i = 0; i < 8; i++) {
			int k = rnd.nextInt(7);
			while(emptyPatches[k]!= null) {
				seed_Place = emptyPatches[k];
				break;
			}
		}
		
		return seed_Place;
	}

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
		// return num_black;
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
		double cal_temp = 0;
		double tempDif = 0;

		for (int i = 0; i < Params.PATCH_X_Y_NUM; i++)
			for (int j = 0; j < Params.PATCH_X_Y_NUM; j++) {
			// if (patches[i][j] == null) {
				cal_temp += patches[i][j].getLocal_temp();
				// System.out.println("cal_temp Temperature: " + cal_temp);
				tempDif += diffuse(patches[i][j], i, j);
				// }
			}
		globalTemp = cal_temp / (Params.PATCH_X_Y_NUM * Params.PATCH_X_Y_NUM);
		System.out.println("Global Temperature: " + globalTemp);
		System.out.println("Global diffused Temperature: " + tempDif / (Params.PATCH_X_Y_NUM * Params.PATCH_X_Y_NUM));
		return globalTemp;
	}

	public void setGlobalTemp(double globalTemp) {
		this.globalTemp = globalTemp;
	}

	// calculate the local temperature.
	public double getLocal_temp(Patch patch) {
		double absorbed_luminosity = 0;
		double local_heat = 0;
		double local_temp = 0;
		if (patch == null) {
			absorbed_luminosity = (1 - Params.ALBEDO_SURFACE) * solar_lum;
			local_temp = this.getLocalTemp_empty();
		} else {
			if (patch.getDaisy().getColor() == 0) {
				absorbed_luminosity = (1 - Params.ALBEDO_WHITE) * solar_lum;
			} else {
				absorbed_luminosity = (1 - Params.ALBEDO_BLACK) * solar_lum;
			}
			local_temp = patch.getLocal_temp();
		}
		if (absorbed_luminosity > 0) {
			local_heat = 72 * Math.log(absorbed_luminosity) + 80;
		} else {
			local_heat = 80;
		}
		local_temp = (local_temp + local_heat) / 2;
		return local_temp;
	}

	/*
	 * calculate the temperature with daisies around the daisy, including the left,
	 * the left-up, the left-down, the right, the right-up, the right-down, the up
	 * and the down ones.
	 */
	public double diffuse(Patch patch, int x, int y) {
		double difTemp = 0;
		double local_temp = patch.getLocal_temp();
		difTemp = local_temp / 2;

		
		// up:
		if (y == 0)
			difTemp += patches[x][Params.PATCH_X_Y_NUM - 1].getLocal_temp() / 16;
		else
			difTemp += patches[x][y - 1].getLocal_temp() / 16;

		// down:
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

	public double getLocalTemp_empty() {

		return localTemp_empty;
	}

	public void setLocalTemp_empty() {
		this.localTemp_empty = getLocal_temp(null);
	}

}
