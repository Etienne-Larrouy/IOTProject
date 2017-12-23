package application;

import java.util.ArrayList;

public class Action {
	private ArrayList<Trigger> actionTrigger;
	private Actuator actionActuator;
	private String name;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Constructor with triggers
	public Action(Actuator a, Trigger... tTab) {
		
		this.actionTrigger = new ArrayList<Trigger>();
		for (Trigger t : tTab) {
			this.addTrigger(t);
		}

		this.actionActuator = a;
	}
	
	public Action() {
		this.actionTrigger = new ArrayList<Trigger>();
	}
	
	

	public ArrayList<Trigger> getActionTrigger() {
		return actionTrigger;
	}

	public void setActionTrigger(ArrayList<Trigger> actionTrigger) {
		this.actionTrigger = actionTrigger;
	}

	public Actuator getActionActuator() {
		return actionActuator;
	}

	public void setActionActuator(Actuator actionActuator) {
		this.actionActuator = actionActuator;
	}

	public Action(Actuator a) {
		this.actionTrigger = new ArrayList<Trigger>();
		this.actionActuator = a;
		this.actionTrigger = null;
	}


	// Add trigger
	public void addTrigger(Trigger t) {
		this.actionTrigger.add(t);
	}
}
