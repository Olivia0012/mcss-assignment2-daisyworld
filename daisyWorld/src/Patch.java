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
	private int rain_volume; // current local rain volume.
	
	public Patch(Daisy daisy, double luminosity, int x, int y) {
		this.daisy = daisy;
		this.luminosity = luminosity;
		this.x = x;
		this.y = y;
		this.rain_volume = 0;
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

	public int getRainVolume() {
		return this.rain_volume;
	}

	public void setRainVolume(int volume) {
		this.rain_volume = volume;
	}

	/*
	 * calculate the local temperature based on luminosity with different albedo 
	 * for daisies and empty patches.
	 */
	public double getLocal_temp() {
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
		return local_temp;
	}


	public void setLocal_temp(double local_temp) {
		this.local_temp = local_temp;
	}
	
}
