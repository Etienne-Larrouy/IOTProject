package action;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;

import application.Trigger;
import application.TriggerType;
import javafx.fxml.FXML;

public class MovementSensorController implements TriggerController{
	
	@FXML
	JFXSlider threshold;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Trigger getTrigger(TriggerType tp) {
		Trigger t = null;

		// Verify timers different than null
		if (threshold.getValue() == 0.0) {
			System.out.println("ERROR : Timers null");
		} else {
			t = new Trigger(tp, Double.toString(threshold.getValue()));
		}

		return t;
	}

}
