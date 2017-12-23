package application;

import java.util.ArrayList;
import java.util.Arrays;

public class Trigger {

	private TriggerType tp = null;
	private ArrayList<String> parameters = new ArrayList<String>();
	
	public Trigger(TriggerType tp, String... p) {
		this.parameters = new ArrayList<String>(Arrays.asList(p));
		this.tp = tp;
	}

	public TriggerType getTriggerType() {
		return tp;
	}
	
	public void addParameters(String p) {
		parameters.add(p);
	}
	
	public void removeParameters(int i) {
		parameters.remove(i);
	}
	
	public ArrayList<String> getParameters() {
		return parameters;
	}


}
