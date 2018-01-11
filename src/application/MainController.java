package application;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import action.ActionController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class MainController implements Initializable {
	/* MenuBar */	     
	@FXML
	MenuItem menuTransfer;
	@FXML
	MenuItem menuOpen;
	@FXML
	MenuItem menuSave;
	@FXML
	MenuItem menuSaveAs;
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

	public Socket clientSocket;
	public ObservableList<Action> listAction = FXCollections.observableArrayList();;

	BooleanProperty notSaved = new SimpleBooleanProperty(false);
	BooleanProperty fileOpen = new SimpleBooleanProperty(false);

	File openedFile = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MainController mn = this;

		/* BIND DISABLE MENU */
		menuSave.disableProperty().bind((				
				Bindings.size(listAction).greaterThan(0).and(notSaved).and(fileOpen).not()
				));

		menuSaveAs.disableProperty().bind((				
				Bindings.size(listAction).greaterThan(0)
				).not());

		menuTransfer.disableProperty().bind((				
				Bindings.size(listAction).greaterThan(0)
				).not());

		menuAddPeripheral.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

			}
		});

		menuRemovePeripheral.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

			}
		});

		menuTransfer.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				try {
					mn.clientSocket = new Socket("192.168.4.2", 22);
					String oldPath = null;
					if(openedFile != null)
						oldPath = openedFile.getAbsolutePath();

					mn.openedFile= new File("temp.xml");
					mn.save();
					byte [] mybytearray  = new byte [(int)openedFile.length()];

					BufferedInputStream inFromUser = new BufferedInputStream(new FileInputStream(openedFile));
					inFromUser.read(mybytearray,0,mybytearray.length);

					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

					outToServer.write(mybytearray,0,mybytearray.length);
					outToServer.flush();
					inFromUser.close();
					
					clientSocket.close();
					openedFile.delete();
				

					if(oldPath == null)
						openedFile = null;
					else
						openedFile = new File(oldPath);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});



		menuExit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				if(notSaved.getValue() && gridPaneListAction.getRowCount()>1) {
					/* ASK USER IF HE WANTS T DELETE CURRENT ACTIONS */
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Existing actions");
					alert.setHeaderText(null);
					alert.setContentText("Warning !\nWould you like to save what you did before exiting ?");


					ButtonType ok = new ButtonType("Save");
					ButtonType cancel = new ButtonType("Exit");

					alert.getButtonTypes().setAll(ok,cancel);


					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ok){
						mn.save();
					}
				}

				mn.exit();


			}

		});
		/* SET ACTION ON MENU ITEMS */
		menuOpen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				boolean proceed = true;
				if(gridPaneListAction.getRowCount()>1) {
					/* ASK USER IF HE WANTS T DELETE CURRENT ACTIONS */
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Existing actions");
					alert.setHeaderText("Warning - Existing actions");
					alert.setContentText("Opening file will remove existing actions. Would you like to continue ?");


					ButtonType ok = new ButtonType("Ok");
					ButtonType cancel = new ButtonType("Cancel");

					alert.getButtonTypes().setAll(ok,cancel);


					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ok){
						proceed = true;
					}
					else {
						proceed = false;
					}
				}

				if(proceed) {

					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Open file");

					fileChooser.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter("XML", "*.xml")
							);

					fileChooser.setInitialDirectory(
							new File(System.getProperty("user.dir")+"/save")
							); 

					openedFile = fileChooser.showOpenDialog(gridPaneListAction.getScene().getWindow());

					if (openedFile != null) {

						XMLInputFactory factory = XMLInputFactory.newInstance();
						XMLEventReader r;
						try {
							r = factory.createXMLEventReader(new FileReader(openedFile));
							String current = null;
							Action a = null;
							Trigger t = null;
							TriggerType tp = null;
							Actuator act = null;
							while(r.hasNext()) {
								XMLEvent e;

								e = r.nextEvent();

								switch (e.getEventType()) {
								case XMLStreamConstants.START_ELEMENT:
									StartElement startElement = e.asStartElement();

									if(startElement.getName().getLocalPart().equals("action")) {
										a = new Action();
										current=startElement.getName().getLocalPart();
									}
									else if(startElement.getName().getLocalPart().equals("trigger")) {
										Attribute idAttr = startElement.getAttributeByName(new QName("id"));
										tp = new TriggerType(Integer.parseInt(idAttr.getValue()));
										current=startElement.getName().getLocalPart();
										t = new Trigger(tp);
									}else if(startElement.getName().getLocalPart().equals("name")) {
										e = r.nextEvent();
										if(current.equals("action")) {
											a.setName(e.asCharacters().getData());
										}
										else if(current.equals("trigger")) {
											tp.setName(e.asCharacters().getData());

										}
										else if(current.equals("actuator")) {
											act.setName(e.asCharacters().getData());
										}
									}
									else if(startElement.getName().getLocalPart().equals("state")) {
										e = r.nextEvent();
										act.setStateOnOff(Boolean.parseBoolean(e.asCharacters().getData()));
									}
									else if(startElement.getName().getLocalPart().equals("actuator")) {
										Attribute idAttr = startElement.getAttributeByName(new QName("id"));
										act = new Actuator(Integer.parseInt(idAttr.getValue()));
										current=startElement.getName().getLocalPart();
									}
									else if(startElement.getName().getLocalPart().equals("parameter")) {
										e = r.nextEvent();
										t.addParameters(e.asCharacters().getData());
									}
									break;

								case XMLStreamConstants.END_ELEMENT:
									EndElement endElement = e.asEndElement();
									if(endElement.getName().getLocalPart().equals("trigger")) {
										a.addTrigger(t);
									}
									else if(endElement.getName().getLocalPart().equals("action")) {
										mn.addAction(a);
									}
									else if(endElement.getName().getLocalPart().equals("actuator")) {
										a.setActionActuator(act);;
									}
									break;
								}

							}
							fileOpen.set(true);
							notSaved.set(false);;
						} catch (FileNotFoundException | XMLStreamException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}

			}
		});

		menuSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				mn.save();
			}
		});

		menuSaveAs.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				mn.save();
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
		notSaved.set(true);
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
		RowConstraints row = new RowConstraints(30);
		gridPaneListAction.getRowConstraints().add(row);
		notSaved.set(true);

	}

	public void save() {

		if(openedFile == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(
					new File(System.getProperty("user.dir")+"/save")
					); 
			fileChooser.setTitle("Save actions");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML (*.xml)", "*.xml"));
			openedFile = fileChooser.showSaveDialog(gridPaneListAction.getScene().getWindow());
		}

		if(openedFile != null) {
			try {

				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				// root elements
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("actions");
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

					actuator.setAttribute("id", ""+a.getActionActuator().getId());

					Element actuatorName = doc.createElement("name");
					actuatorName.appendChild(doc.createTextNode(a.getActionActuator().getName()));
					actuator.appendChild(actuatorName);

					Element state = doc.createElement("state");
					state.appendChild(doc.createTextNode(Boolean.toString(a.getActionActuator().isStateOnOff())));
					actuator.appendChild(state);
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
				StreamResult result = new StreamResult(openedFile);

				transformer.transform(source, result);
				fileOpen.set(true);
				notSaved.set(false);
				System.out.println("File saved!");

			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			}
		}



	}


	public void exit() {
		Stage stage = (Stage) gridPaneListAction.getScene().getWindow();
		stage.close();
	}

}
