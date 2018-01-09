package application;

import java.io.File;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import action.ActionController;


public class MainController implements Initializable {
	/* MenuBar */	     
	@FXML
    MenuItem menuTransfer;
	@FXML
    MenuItem menuOpen;
    @FXML
    MenuItem menuSave;
    @FXML
    MenuItem menuExit;
    @FXML
    MenuItem menuCreateAction;
    @FXML
    MenuItem menuAddPeripheral;
    @FXML
    MenuItem menuRemovePeripheral;
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
		Text text = new Text(a.getName());
		text.setStyle("-fx-font: 14 arial;"
				+ "-fx-fill: #e9e5db;");
		gridPaneListAction.addRow(gridPaneListAction.getRowCount(),text, view, remove);
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MainController mn = this;
		
		
		
		/* SET ACTION ON MENU ITEMS */
		menuOpen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open file");
				File file = fileChooser.showOpenDialog(gridPaneListAction.getScene().getWindow());
				
				if (file != null) {
                    
                }
			}
	    });
		
		menuSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				  try {

						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

						// root elements
						Document doc = docBuilder.newDocument();
						Element rootElement = doc.createElement("action");
						doc.appendChild(rootElement);

						for(Action a : listAction) {
							Element action = doc.createElement("action");
							rootElement.appendChild(action);
							
							// name element
							Element actionName = doc.createElement("name");
							actionName.appendChild(doc.createTextNode(a.getName()));
							action.appendChild(actionName);
							
							// actuator element
							Element actuator = doc.createElement("actuator");
							actuator.appendChild(doc.createTextNode(a.getActionActuator().getName()));
							actuator.setAttribute("id", ""+a.getActionActuator().getId());
							action.appendChild(actuator);
							
							Element triggers = doc.createElement("triggers");
							action.appendChild(triggers);
							
							// trigger element
							for(Trigger t : a.getActionTrigger()) {
								Element trigger = doc.createElement("trigger");
								trigger.setAttribute("id", ""+t.getTriggerType().getId());
								triggers.appendChild(trigger);
								
								Element triggerName = doc.createElement("name");
								triggerName.appendChild(doc.createTextNode(t.getTriggerType().getName()));
								trigger.appendChild(triggerName);
								
								if(!(t.getParameters().isEmpty())) {
									Element parameters = doc.createElement("parameters");
									trigger.appendChild(parameters);
								
									for(String s : t.getParameters()) {
										Element parameter = doc.createElement("parameter");
										parameter.appendChild(doc.createTextNode(s));
										parameters.appendChild(parameter);
									}
								}
								
								
							}
							
						}

						// write the content into xml file
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);
						StreamResult result = new StreamResult(new File("file.xml"));

						// Output to console for testing
						// StreamResult result = new StreamResult(System.out);

						transformer.transform(source, result);

						System.out.println("File saved!");

					  } catch (ParserConfigurationException pce) {
						pce.printStackTrace();
					  } catch (TransformerException tfe) {
						tfe.printStackTrace();
					  }
			}
	    });
		
        
		menuTransfer.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent event) {
        		 SerialPortCom sp = new SerialPortCom("192.168.4.1", 22);
        		 sp.sendData("Bite");
        		 sp.close();
        	}
        });

        menuCreateAction.setOnAction(new EventHandler<ActionEvent>() {
       	 		
	            public void handle(ActionEvent event) {
	            	try {
	            		//Set child to actionController and Parent to this
	            		Stage stage = new Stage();
	            		ActionController currentController = new ActionController(mn, stage);
	    				FXMLLoader loader = new FXMLLoader(getClass().getResource("../Action/CreateAction.fxml"));
	    				loader.setController(currentController);
	    				Parent root1 = (Parent) loader.load();
	    				Scene scene = new Scene(root1);
	    
	    				stage.setTitle("Create action");
						stage.setScene(scene);  
						stage.show();
	                    
	            	} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                  
	      
	            }
	        }
		);
   	}
	
	/* View button action handler */
	public void displayAction(int i) {
		ViewActionController viewActionController = new ViewActionController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAction.fxml"));
		loader.setController(viewActionController);
		
		Stage stage = new Stage();
		BorderPane root;
		stage.setTitle("View action");
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
