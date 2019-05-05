package controller;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.GramaticInFNC;

public class MainController {

	@FXML
	private TextField txtVariables;
	
	@FXML
	private TextField txtTerminals;
	
	@FXML
	private AnchorPane rootPane;
	
	private GramaticInFNC gramatic;
	
	@FXML
	public void confirmGramatic(ActionEvent e) throws IOException {
		gramatic = new GramaticInFNC();
		String[] variables = txtVariables.getText().split(",");
		String [] terminals = txtTerminals.getText().split(",");
		
		gramatic.addVariable(variables[0].charAt(0), true);
		
		for (int i = 1; i < variables.length; i++) {
			gramatic.addVariable(variables[i].charAt(0), false);
		}
		
		for (String terminal : terminals) {
			gramatic.addTerminal(terminal.charAt(0));
		}
		
		changeFrame(gramatic);
	}
	
	private void changeFrame(GramaticInFNC gram) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProductionsFrame.fxml"));
		AnchorPane pane = loader.load();
		ControllerGramatic cont = loader.getController();
		cont.setGramatic(gram);
		rootPane.getChildren().setAll(pane);
		cont.fillLabels();
		cont.fillComboBoxes();
	}
}
