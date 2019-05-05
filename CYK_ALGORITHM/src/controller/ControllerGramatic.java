package controller;




import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import model.GramaticInFNC;
import model.Variable;

public class ControllerGramatic implements Initializable{

	@FXML
	private VBox vboxProductions;
	
	@FXML
	private ComboBox<Character> cbInitialVariableToVariables;
	
	@FXML
	private ComboBox<Character> cbFirstResultVariable;
	
	@FXML
	private ComboBox<Character> cbSecondResultVariable;
	
	@FXML
	private ComboBox<Character> cbInitialVariableToTerminal;
	
	@FXML
	private ComboBox<Character> cbTerminals;
	
	private HashMap<Character, Integer> posInVbox;
	
	@FXML
	private TextField txtCadena;
	
	private GramaticInFNC gramatic;
	
	public void setGramatic (GramaticInFNC g) {
		gramatic = g;
	}
	
	public void fillLabels() {
		char firstVariable = gramatic.getFirstVariable().getId();
		vboxProductions.getChildren().add(0, new Label(firstVariable + " --> "));
		posInVbox.put(firstVariable, 0);
		int i = 1;
		for (Variable v : gramatic.getVariables().values()) {
			if (!v.isTheInitial()) {
				vboxProductions.getChildren().add(i, new Label(v.getId() + " --> "));
				posInVbox.put(v.getId(), i++);
			}
		}
	}
	
	public void fillComboBoxes() {
		ObservableList<Character> obs = FXCollections.observableArrayList();
		obs.add(gramatic.getFirstVariable().getId());
		for (Variable v : gramatic.getVariables().values()) {
			if (!v.isTheInitial()) {
				obs.add(v.getId());
			}
		}
		cbInitialVariableToTerminal.setItems(obs);
		cbInitialVariableToVariables.setItems(obs);
		cbFirstResultVariable.setItems(obs);
		cbSecondResultVariable.setItems(obs);
		
		ObservableList<Character> terminals = FXCollections.observableArrayList();
		for (Character term : gramatic.getTerminals()) {
			terminals.add(term);
		}
		
		cbTerminals.setItems(terminals);
	}
	
	@FXML
	public void addToVariables (ActionEvent e) {
		char initial = cbInitialVariableToVariables.getValue();
		char to1 = cbFirstResultVariable.getValue();
		char to2 = cbSecondResultVariable.getValue();
		
		gramatic.addProduction(initial, to1, to2);
		
		int pos = posInVbox.get(initial);
		Label lab = ((Label) vboxProductions.getChildren().get(pos));
		lab.setText(lab.getText() + " | " + to1 + "" + to2);
	}
	
	@FXML
	public void addToTerminals (ActionEvent e) {
		char initial = cbInitialVariableToTerminal.getValue();
		char to = cbTerminals.getValue();
		gramatic.addProduction(initial, to);
		int pos = posInVbox.get(initial);
		Label lab = ((Label) vboxProductions.getChildren().get(pos));
		lab.setText(lab.getText() + " | " + to);
	}
	
	@FXML
	public void confirmString (ActionEvent e) {
		String x = txtCadena.getText();
		System.out.println(x + "kkkkkk");
		boolean result = gramatic.CYK(x);
		
		Alert al;
		if (result) {
			al = new Alert(AlertType.CONFIRMATION);
			al.setHeaderText("Cadena pertenece!");
			al.setContentText("Felicidades! La cadena pertenece a la gramática");
		} else {
			al = new Alert(AlertType.ERROR);
			al.setHeaderText("No pertenece");
			al.setContentText("Lo lamento :( La cadena no pertenece a la gramática");
		}
		al.showAndWait();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		posInVbox = new HashMap<Character, Integer>();
		
	}
}
