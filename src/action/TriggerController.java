package action;


import application.Trigger;
import application.TriggerType;
import javafx.fxml.Initializable;

public interface TriggerController extends Initializable {

	public Trigger getTrigger(TriggerType tp);
	
}
