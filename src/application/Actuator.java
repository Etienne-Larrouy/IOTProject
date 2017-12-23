package application;

public class Actuator {
	private String name;
	private int id;
	private boolean StateOnOff;
	
	public Actuator(int id) {
		this.id = id;
	}

	public boolean isStateOnOff() {
		return StateOnOff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStateOnOff(boolean stateOnOff) {
		StateOnOff = stateOnOff;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
