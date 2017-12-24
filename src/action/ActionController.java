package action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import application.Action;
import application.Actuator;
import application.MainController;
import application.Trigger;
import application.TriggerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ActionController implements Initializable {

	private ArrayList<Trigger> listTrigger = new ArrayList<Trigger>();

	private TriggerController currentController;

	private ObservableList<TriggerType> listTriggerType = FXCollections.observableArrayList();

	private ObservableList<Actuator> listActuator = FXCollections.observableArrayList();

	private MainController parent;

	private Stage currentStage;

	private Actuator actionActuator;

	@FXML
	private ChoiceBox<Actuator> actuatorBox;
	@FXML
	private ChoiceBox<TriggerType> triggerBox;
	@FXML
	private VBox parametersPane;
	@FXML
	private Button addTrigger;
	@FXML
	private Button createAction;
	@FXML
	private Button createTrigger;
	@FXML
	private VBox listTriggers;
	@FXML
	JFXTextField actionName;
	@FXML
	JFXToggleButton actuatorOnOff;

	private Action a = new Action();

	public ActionController(MainController mainController, Stage stage) {
		this.parent = mainController;
		this.currentStage = stage;
	}

	@Override

	public void initialize(URL arg0, ResourceBundle arg1) {

		//Import Actuator and TriggerType data from the configuration file
		importDataActuator();
		importDataTriggerType();

		/* Disable create button if Name is empty */
		createAction.disableProperty().bind((				
				actionName.textProperty().isNotEmpty()
				).not());

		/* LIST ACTUATOR */

		actionActuator = listActuator.get(0);

		actuatorBox.setItems(FXCollections.observableArrayList(listActuator));

		// Event listener ChoiceBox
		actuatorBox.getSelectionModel().selectedItemProperty()
		.addListener((obs, oldV, newV) -> this.actionActuator = newV);

		// Display name instead of object ID
		actuatorBox.setConverter(new StringConverter<Actuator>() {

			// Display String
			@Override
			public String toString(Actuator ac) {
				return ac.getName();
			}

			// Find object with String
			@Override
			public Actuator fromString(String string) {
				return actuatorBox.getItems().stream().filter(ac -> ac.getName().equals(string)).findFirst().orElse(null);
			}

		});

		/* LIST TRIGGER */

		//Create and set trigger controller		
		triggerBox.setItems(FXCollections.observableArrayList(listTriggerType));

		// Event listener ChoiceBox
		triggerBox.getSelectionModel().selectedItemProperty()
		.addListener((obs, oldV, newV) -> this.insertParametersPane(newV));

		// Display name instead of object ID
		triggerBox.setConverter(new StringConverter<TriggerType>() {

			// Display String
			@Override
			public String toString(TriggerType t) {
				return t.getName();
			}

			// Find object with String
			@Override
			public TriggerType fromString(String string) {
				return triggerBox.getItems().stream().filter(t -> t.getName().equals(string)).findFirst().orElse(null);
			}

		});

		// Create action button
		createAction.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// Browse controllers in ArrayLis to construct action text file


				/* Add triggers to the new action */
				for(Trigger t : listTrigger)
					a.addTrigger(t);

				/* Set Actuator*/
				actionActuator.setStateOnOff(actuatorOnOff.isSelected());

				System.out.println(actuatorOnOff.isSelected());
				a.setActionActuator(actionActuator);

				//Set name of the action
				if(actionName.getText() != null) {
					a.setName(actionName.getText());

					//Send action to parent controller
					parent.addAction(a);
					System.out.println(actionName.getText());
					currentStage.close();

				}
				else {
					System.out.println("ERROR");
				}
			}
		});
	}

	//Import Actuator data from the configuration file
	private void importDataActuator() {

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		Actuator a = null;
		try {

			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(System.getProperty("user.dir")+"/conf/confActuator.xml"));
			while(xmlEventReader.hasNext()){
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				if (xmlEvent.isStartElement()){
					StartElement startElement = xmlEvent.asStartElement();
					if(startElement.getName().getLocalPart().equals("Actuator")){

						//Get the 'id' attribute from Employee element
						Attribute idAttr = startElement.getAttributeByName(new QName("id"));
						if(idAttr != null){
							a = new Actuator(Integer.parseInt(idAttr.getValue()));
						}
						//set the other variables from xml element
					}else if(startElement.getName().getLocalPart().equals("name")){
						xmlEvent = xmlEventReader.nextEvent();
						a.setName(xmlEvent.asCharacters().getData());
					}
				}
				//if Employee end element is reached, add employee object to list
				if(xmlEvent.isEndElement()){
					EndElement endElement = xmlEvent.asEndElement();
					if(endElement.getName().getLocalPart().equals("Actuator")){
						listActuator.add(a);
					}
				}
			}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}

	//Import Actuator data from the configuration file
	private void importDataTriggerType() {

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		TriggerType tp = null;
		try {
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(System.getProperty("user.dir")+"/conf/confTriggerType.xml"));
			while(xmlEventReader.hasNext()){
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				if (xmlEvent.isStartElement()){
					StartElement startElement = xmlEvent.asStartElement();
					if(startElement.getName().getLocalPart().equals("TriggerType")){

						//Get the 'id' attribute from Employee element
						Attribute idAttr = startElement.getAttributeByName(new QName("id"));
						if(idAttr != null){
							tp = new TriggerType(Integer.parseInt(idAttr.getValue()));
						}
						//set the other varaibles from xml element
					}else if(startElement.getName().getLocalPart().equals("name")){
						xmlEvent = xmlEventReader.nextEvent();
						tp.setName(xmlEvent.asCharacters().getData());
					}
					else if(startElement.getName().getLocalPart().equals("filename")){
						xmlEvent = xmlEventReader.nextEvent();
						tp.setFileName(xmlEvent.asCharacters().getData());
					}
					else if(startElement.getName().getLocalPart().equals("controller")){
						xmlEvent = xmlEventReader.nextEvent();
						@SuppressWarnings("rawtypes")
						Class controller;
						try {
							controller = Class.forName("action."+xmlEvent.asCharacters().getData());
							@SuppressWarnings("rawtypes")
							Constructor constructor = controller.getConstructor();
							tp.setTriggerController((TriggerController) constructor.newInstance());
						} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						} 		                   				  
					}
				}
				//if Employee end element is reached, add employee object to list
				if(xmlEvent.isEndElement()){
					EndElement endElement = xmlEvent.asEndElement();
					if(endElement.getName().getLocalPart().equals("TriggerType")){
						listTriggerType.add(tp);
					}
				}
			}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}

	}

	/* Change pane depending of the selected trigger */
	private void insertParametersPane(TriggerType tp1) {
		try {

			parametersPane.getChildren().clear();

			if (tp1.getFileParametersName() != null) {
				// Create fxmloader, pane and add it to the view
				currentController = tp1.getTriggerController();
				FXMLLoader loader = new FXMLLoader(getClass().getResource(tp1.getFileParametersName() + ".fxml"));
				loader.setController(currentController);
				AnchorPane newLoadedPane = loader.load();


				//Add trigger button 
				createTrigger = new Button("Add");
				createTrigger.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {
						if(currentController.getTrigger(tp1)!=null)
							addTrigger(currentController.getTrigger(tp1));
					}

				});


				parametersPane.getChildren().add(newLoadedPane);
				parametersPane.getChildren().add(createTrigger);
			}
		 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addTrigger(Trigger t) {
		listTrigger.add(t);
		Text textTrigger = new Text(t.getParameters().toString());
		listTriggers.getChildren().add(textTrigger);
	}


}