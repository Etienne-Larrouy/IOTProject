package application;

import action.TriggerController;

public class TriggerType {
	
	private String name = null;
	private String fileName = null;
	private TriggerController tc = null;
	private int id;
	
	public TriggerType(int id) {
		this.id = id;
	}
	
	public TriggerType(String name) {
		this.name = name;
	}
	
	public TriggerType(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
	}
	
	
	public int getId() {
		return id;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setId(int id) {
		this.id = id;
	}


	public TriggerController getTriggerController() {
		return tc;
	}

	public void setTriggerController(TriggerController tc) {
		this.tc = tc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileParametersName() {
		return fileName;
	}

}
