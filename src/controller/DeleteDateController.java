package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import unit.LoadView;

public class DeleteDateController implements Initializable {
	@FXML
	private HBox hb1;
	@FXML
	private HBox hb2;
	@FXML
	private VBox vb;

	public void customize() {
		new LoadView().loadViewWithoutStage("CustomizeDateView", vb);
		hb1.setStyle("-fx-border-color: #ffbb00;" + "-fx-border-width: 0 0 2 0");
		hb2.setStyle("-fx-border-color: white;" + "-fx-border-width: 0 0 2 0");
	}

	public void specific() {
		new LoadView().loadViewWithoutStage("SpecificDateView", vb);
		hb2.setStyle("-fx-border-color: #ffbb00;" + "-fx-border-width: 0 0 2 0");
		hb1.setStyle("-fx-border-color: white;" + "-fx-border-width: 0 0 2 0");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customize();

	}

}
