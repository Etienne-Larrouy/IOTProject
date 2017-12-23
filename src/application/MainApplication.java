package application;
	
import java.io.IOException;

import action.ActionController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
			
				
			/* MenuBar */
			MenuBar menuBar = new MenuBar();
	        Menu menuFile = new Menu("File");
	        Menu menuPeripheral = new Menu("Peripheral");
	        Menu menuCreate = new Menu("Create");
	        
	        MenuItem transfer = new MenuItem("Transfer");
	        MenuItem save = new MenuItem("Save");
	        MenuItem exit = new MenuItem("Exit");
	        
	        MenuItem createAction = new MenuItem("Action");
	        
	        MenuItem addPeripheral = new MenuItem("Add");
	        MenuItem removePeripheral = new MenuItem("Remove");
	        
	        transfer.setOnAction(new EventHandler<ActionEvent>() {
	        	public void handle(ActionEvent event) {
	        		
	        	}
	        });

	        createAction.setOnAction(new EventHandler<ActionEvent>() {
	       	 
		            public void handle(ActionEvent event) {
		            	try {
		            		//Set child to actionController and Parent to this
		            		Stage stage = new Stage();
		            		ActionController currentController = new ActionController(mainController, stage);
		    				FXMLLoader loader = new FXMLLoader(getClass().getResource("../Action/CreateAction.fxml"));
		    				loader.setController(currentController);
		    			
		    				Parent root1 = (Parent) loader.load();
							stage.setScene(new Scene(root1));  
							stage.show();
		                    
		            	} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                  
		      
		            }
		        }
    		);
	        
	        menuCreate.getItems().add(createAction);
	        
	        menuPeripheral.getItems().add(addPeripheral);
	        menuPeripheral.getItems().add(removePeripheral);
	        
	        menuFile.getItems().add(transfer);
	        menuFile.getItems().add(save);
	        menuFile.getItems().add(exit);

	 
	        menuBar.getMenus().addAll(menuFile, menuPeripheral, menuCreate);
	        root.setTop(menuBar);
	        
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
