package action;

import java.net.URL;
import java.util.ResourceBundle;

import application.Trigger;
import application.TriggerType;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class MovementSensorController implements TriggerController{
	
	@FXML
	CheckBox activated;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Trigger getTrigger(TriggerType tp) {
		Trigger t = new Trigger(tp, Boolean.toString(activated.isSelected()));

		return t;
	}

}
