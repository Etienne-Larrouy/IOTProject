package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXToggleButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ViewActionController implements Initializable{
	
	@FXML
	Text actionName;
	
	@FXML
	Text actuatorName;
	
	@FXML
	JFXToggleButton actuatorOnOff;
	
	@FXML
	VBox listTriggers;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void addTrigger(Trigger t) {		
		Text textTrigger = new Text(t.getParameters().toString());
		textTrigger.setStyle("-fx-font: 14 arial;"
				+ "-fx-fill: #e9e5db;");
		listTriggers.getChildren().add(textTrigger);
	}

}
