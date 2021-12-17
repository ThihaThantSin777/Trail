package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.TwoDThreeDException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import unit.LoadView;

public class LoginController {
	@FXML
	private JFXTextField userName;
	@FXML
	private JFXPasswordField password;

	@FXML
	private VBox vb;
	private static final String fixUserName = "a";
	private static final String fixPassword = "123";

	private void checkError() {
		String uN = userName.getText();
		String pD = password.getText();

		if (uN.isEmpty()) {
			throw new TwoDThreeDException("Please enter user name");
		} else if (pD.isEmpty()) {
			throw new TwoDThreeDException("Please enter Password");
		} else if (!(uN.equals(fixUserName) && pD.equals(fixPassword))) {
			throw new TwoDThreeDException("Username or Password is wrong");
		}
	}

	public void passwordFocus() {
		if (!userName.getText().isEmpty()) {
			password.requestFocus();
		}else {
			Alert alert = new Alert(AlertType.ERROR, "Enter User Name", ButtonType.OK);
			alert.show();
		}
	}

	public void login() {
		if (!password.getText().isEmpty()) {
			try {
				checkError();
				new LoadView().loadViewWithoutStage("ChooseView", vb);
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
				alert.show();
			}
		}else {
			Alert alert = new Alert(AlertType.ERROR, "Enter Password", ButtonType.OK);
			alert.show();
		}
	}
}
