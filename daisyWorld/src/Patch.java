/**
 * Details of each patch.
 *
 * @author Lu Wang
 * @Number 1054195
 * 
 *
 */


public class Patch {
	private Daisy daisy = null;
	private double local_temp;
	private double luminosity;
	
	public Patch(Daisy daisy, double luminosity) {
		this.daisy = daisy;
		this.luminosity = luminosity;
	}
	
	
	public Daisy getDaisy() {
		return daisy;
	}

	public void setDaisy(Daisy daisy) {
		this.daisy = daisy;
	}
	
	

	public void setLocal_temp(double local_temp) {
		this.local_temp = local_temp;
	}

	public double getLuminosity() {
		return luminosity;
	}

	public void setLuminosity(double luminosity) {
		this.luminosity = luminosity;
	}
	
	
	
	
	
}
