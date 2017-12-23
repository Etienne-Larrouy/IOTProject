package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController implements Initializable {

	@FXML
	VBox VBoxlistAction;
	@FXML
	VBox VBoxbuttonList;
	@FXML
	GridPane gridPaneListAction;

	public ArrayList<Action> listAction = new ArrayList<Action>();

	public void addAction(Action a) {
		for (Trigger t : a.getActionTrigger()) {
			System.out.print(t.getTriggerType().getName().toString());
			System.out.println(t.getParameters().toString());
		}
		
		
		//Button allowing to view the action
		Button view = new Button("View");
		
		view.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	            	displayAction(GridPane.getRowIndex(view)-1);
	            }
	        }
		);
		
		//Button allowing to remove the action
		Button remove = new Button("Remove");
		
		remove.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
	            	removeAction(GridPane.getRowIndex(remove));
	            }
	        }
		);

		// Add the freshly created action to the list
		listAction.add(a);
		gridPaneListAction.addRow(gridPaneListAction.getRowCount(),new Text(a.getName()), view, remove);
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
	
	/* View button action handler */
	public void displayAction(int i) {
		ViewActionController viewActionController = new ViewActionController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAction.fxml"));
		loader.setController(viewActionController);
		
		Stage stage = new Stage();
		BorderPane root;
		try {
			root = loader.load();
			stage.setScene(new Scene(root));  
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		viewActionController.actionName.setText(listAction.get(i).getName());
		viewActionController.actuatorName.setText(listAction.get(i).getActionActuator().getName());
		viewActionController.actuatorOnOff.setSelected(listAction.get(i).getActionActuator().isStateOnOff());
		
		for(Trigger t : listAction.get(i).getActionTrigger()) {
			viewActionController.addTrigger(t);
		}
		
	}
	
	public void removeAction(int i) {
	
		listAction.remove(i-1);
		
		ArrayList<Node> deleteNodes = new ArrayList<Node>();
		
		 for (Node child : gridPaneListAction.getChildren()) {
		        // get index from child
		        Integer rowIndex = GridPane.getRowIndex(child);

		        // handle null values for index=0
		        int r = rowIndex == null ? 0 : rowIndex;

		        if (r > i) {
		            // decrement rows for rows after the deleted row
		            GridPane.setRowIndex(child, rowIndex-1);
		        } else if (r == i) {
		            // collect matching rows for deletion
		            deleteNodes.add(child);
		        }
		    }
		 
		gridPaneListAction.getChildren().removeAll(deleteNodes);
	}

}
