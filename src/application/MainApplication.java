package application;
	
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
			
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);  
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
