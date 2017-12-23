package action;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTimePicker;

import application.Trigger;
import application.TriggerType;
import javafx.fxml.FXML;

public class TimeController implements TriggerController {
	@FXML
	JFXTimePicker afterTimer;

	@FXML
	JFXTimePicker beforeTimer;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public Trigger getTrigger(TriggerType tp) {
		Trigger t = null;

		// Verify timers different than null
		if (afterTimer.getValue() == null || beforeTimer.getValue() == null) {
			System.out.println("ERROR : Timers null");
		} else {
			t = new Trigger(tp, afterTimer.getValue().toString(), beforeTimer.getValue().toString());
		}

		return t;

	}




}
