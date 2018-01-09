package application;
	
import java.io.IOException;

import action.ActionController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainApplication extends Application {
		
	private MainController mainController;
	@Override
	public void start(Stage primaryStage) {
		try {
			mainController = new MainController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainApplication.fxml"));
			loader.setController(mainController);
		
			BorderPane root = loader.load();
			primaryStage.setScene(new Scene(root));  
			primaryStage.show();
			
			primaryStage.setTitle("IOT");
	        
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
