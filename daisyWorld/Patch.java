/**
 * Details of each patch.
 *
 * @author Lu Wang
 * @Number 1054195
 * 
 *
 */


public class Patch {
	private Daisy daisy = null; // when there is no daisy in the patch, this will be null.
	private int x; // number of the row.
	private int y; // number of the column.
	private double local_temp; // local temperature of each patch.
	private double luminosity; // current luminosity.
	private boolean is_raining; // current local rain volume.
	private int water_level; // current amount of water in the soil.
	
	public Patch(Daisy daisy, double luminosity, int x, int y, int water_level) {
		this.daisy = daisy;
		this.luminosity = luminosity;
		this.x = x;
		this.y = y;
		this.is_raining = false;
		this.water_level = water_level;
	}
	
	// Getter and setter functions of variables.
	public Daisy getDaisy() {
		return daisy;
	}

	public void setDaisy(Daisy daisy) {
		this.daisy = daisy;
	}
	
	
	public double getLuminosity() {
		return luminosity;
	}

	public void setLuminosity(double luminosity) {
		this.luminosity = luminosity;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isRaining() {
		return this.is_raining;
	}

	public void setIsRaining(boolean raining) {
		this.is_raining = raining;
	}

	public int getWaterLevel(){
		return this.water_level;
	}

	public void setWaterLevel(int water_level){
		this.water_level = water_level;
	}

	
	public double getLocal_temp() {
		return this.local_temp;
	}

	/*
	 * calculate the local temperature based on luminosity with different albedo 
	 * for daisies and empty patches.
	 */
	public void cal_localTemp() {
		double absorbed_luminosity = 0;
		double local_heat = 0;
		if (daisy == null) {
			absorbed_luminosity = (1 - Params.ALBEDO_SURFACE) * luminosity;
		} else {
			if (daisy.getColor() == 0) {
				absorbed_luminosity = (1 - Params.ALBEDO_WHITE) * luminosity;
			} else {
				absorbed_luminosity = (1 - Params.ALBEDO_BLACK) * luminosity;
			}
		}
			
		if (absorbed_luminosity > 0) {
			local_heat = 72 * Math.log(absorbed_luminosity) + 80;
		} else {
			local_heat = 80;
		}
		local_temp = (local_temp + local_heat) / 2;
		setLocal_temp(local_temp);
	}

	public void setLocal_temp(double local_temp) {
		this.local_temp = local_temp;
	}
	
}
