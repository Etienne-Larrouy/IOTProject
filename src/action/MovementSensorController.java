package action;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;

import application.Trigger;
import application.TriggerType;
import javafx.fxml.FXML;

public class MovementSensorController implements TriggerController{
	
	@FXML
	JFXSlider thresholdMovement;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Trigger getTrigger(TriggerType tp) {
		Trigger t = new Trigger(tp);

		return t;
	}

}
