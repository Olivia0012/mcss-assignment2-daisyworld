/**
 * Details of each daisy.
 *
 * @author Lu Wang
 * @Number 1054195
 * 
 *
 */

public class Daisy {
	
	private Boolean color;
	private int age;
//	private Patch patch;

	public Daisy(Boolean color, int age) {
		this.color = color;
		this.age = age;
	//	this.patch = location;
	}
/*
	public Patch getPatch() {
		return patch;
	}

	public void setPatch(Patch patch) {
		this.patch = patch;
	}
*/
	public Boolean getColor() {
		return color;
	}

	public void setColor(Boolean color) {
		this.color = color;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	
}
