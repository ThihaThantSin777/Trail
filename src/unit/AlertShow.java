package unit;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertShow {
	public static void showInfo(String message) {
		new Alert(AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
	}

	public static void showError(String message) {
		new Alert(AlertType.ERROR, message, ButtonType.OK).showAndWait();
	}

	public static void showWarning(String message) {
		new Alert(AlertType.WARNING, message, ButtonType.OK).showAndWait();
	}
}
