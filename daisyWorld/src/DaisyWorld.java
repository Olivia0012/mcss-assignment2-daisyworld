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
	private double solar_lum;
	private Patch patches[][] = new Patch[Params.PATCH_X_Y_NUM][Params.PATCH_X_Y_NUM];

	static Random rnd = new Random();

	public DaisyWorld(int solar) {
		if(solar == 0) {
			
		}else if(solar == 1) {
			this.solar_lum = Params.LOW_SOLAR_LUMINOSITY;
		}else if(solar == 2) {
			this.solar_lum = Params.OUR_SOLAR_LUMINOSITY;
		}else if(solar == 3) {
			this.solar_lum = Params.HIGH_SOLAR_LUMINOSITY;
		}
			
	}
	
	public void execution() {
		seedDaisyRandomly(true, 180);
		seedDaisyRandomly(false, 180);
		getGlobalTemp(); // initial global tempertature.
		

	}

	// seeding daisies randomly at the begining.
	public void seedDaisyRandomly(Boolean color, double initNum) {
		for (int i = 0; i < initNum; i++) {
			int x = rnd.nextInt(Params.PATCH_X_Y_NUM);
			int y = rnd.nextInt(Params.PATCH_X_Y_NUM);

			while (patches[x][y] == null) {
				if (color) {
					Daisy wDaisy = new Daisy(true, rnd.nextInt(Params.MAX_AGE));
					patches[x][y] = new Patch(wDaisy, solar_lum);
				} else {
					Daisy bDaisy = new Daisy(true, rnd.nextInt(Params.MAX_AGE));
					patches[x][y] = new Patch(bDaisy, solar_lum);
				}
			}
			if (color)
				System.out.println(x + "-" + y + "-" + "white" + " "+ solar_lum);
			else
				System.out.println(x + "-" + y + "-" + "black");
		}
	}
	
	public void checkSurvival() {
		double seed_threhold = 0;
		for (int i = 0; i < Params.PATCH_X_Y_NUM; i++)
			for (int j = 0; j < Params.PATCH_X_Y_NUM; j++) {
				if(patches[i][j] != null) {
				//	seed_threhold = 
				}
			}
	}

	public int getNum_black() {
		return num_black;
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
				if (patches[i][j] == null) {
					cal_temp += getLocal_temp(patches[i][j]);
					System.out.println("cal_temp Temperature: " + cal_temp);
					tempDif += diffuse(patches[i][j], i, j);
				}
			}
		globalTemp = cal_temp / (Params.PATCH_X_Y_NUM * Params.PATCH_X_Y_NUM);
		System.out.println("Global Temperature: " + globalTemp);
		System.out.println("Global diffused Temperature: " + tempDif/ (Params.PATCH_X_Y_NUM * Params.PATCH_X_Y_NUM));
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
		} else {
			if (patch.getDaisy().getColor()) {
				absorbed_luminosity = (1 - Params.ALBEDO_WHITE) * solar_lum;
			} else {
				absorbed_luminosity = (1 - Params.ALBEDO_BLACK) * solar_lum;
			}
		}
		if (absorbed_luminosity > 0) {
			local_heat = 72 * Math.log(absorbed_luminosity) + 80;
		} else {
			local_heat = 80;
		}
		local_temp = (local_temp + local_heat) / 2;
		
		return local_temp;
	}
	
	public double diffuse(Patch patch, int x, int y) {
		double difTemp = 0;
		double local_temp = getLocal_temp(patch);
		difTemp = local_temp/2;
		
		if(x > 1)
			difTemp += getLocal_temp(patches[x-1][y])/16;
		if(x < Params.PATCH_X_Y_NUM - 1)
			difTemp += getLocal_temp(patches[x+1][y])/16;
		if(y > 1)
			difTemp += getLocal_temp(patches[x][y-1])/16;
		if(y < Params.PATCH_X_Y_NUM -1)
			difTemp += getLocal_temp(patches[x][y+1])/16;
		if(y > 1 && x > 1)
			difTemp += getLocal_temp(patches[x-1][y-1])/16;
		if(x < Params.PATCH_X_Y_NUM - 1 && y < Params.PATCH_X_Y_NUM -1)
			difTemp += getLocal_temp(patches[x+1][y+1])/16;
		if(x > 1 && y < Params.PATCH_X_Y_NUM -1)
			difTemp += getLocal_temp(patches[x-1][y+1])/16;
		if(y > 1 && x < Params.PATCH_X_Y_NUM -1)
			difTemp += getLocal_temp(patches[x+1][y-1])/16;
		
		return difTemp;
	}

}
