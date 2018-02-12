package action;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTimePicker;

import application.Trigger;
import application.TriggerType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TimeController implements TriggerController {
	@FXML
	JFXTimePicker afterTimer;

	@FXML
	JFXTimePicker beforeTimer;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		afterTimer.getEditor().setStyle("-fx-text-fill: white; -fx-font-size: 14;");
		beforeTimer.getEditor().setStyle("-fx-text-fill: white; -fx-font-size: 14;");
	}

	@Override
	public Trigger getTrigger(TriggerType tp) {
		Trigger t = null;

		// Verify timers different than null
		if (afterTimer.getValue() == null || beforeTimer.getValue() == null) {
			System.out.println("ERROR : Timers null");
		} else {
			if(beforeTimer.getValue().isBefore(afterTimer.getValue())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Timers");
				alert.setHeaderText(null);
				alert.setContentText("Error - Second timer needs to be after the first");
				alert.show();
			}
			else
				t = new Trigger(tp, afterTimer.getValue().toString(), beforeTimer.getValue().toString());
		}

		return t;

	}




}
