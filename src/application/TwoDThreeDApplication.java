package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import controller.LoginController;
import database2d3d.CreateDatabaseAndTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import unit.AlertShow;

public class TwoDThreeDApplication extends Application {
	@Override
	public void start(Stage stage) {
		LocalDate start = LocalDate.of(2021, 12, 10);
		LocalDate end = LocalDate.of(2022, 01, 10);
//		try {
//			Parent root = FXMLLoader.load(LoginController.class.getResource("Login.fxml"));
//			stage.setScene(new Scene(root));
//			stage.setTitle("Login");
//			stage.getIcons().add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
//			stage.setResizable(false);
//			stage.show();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {
			if (end.isBefore(LocalDate.now()) || start.isAfter(LocalDate.now())) {
				AlertShow.showWarning("This is Expire... \n Contact Your Administrator ...");
			} else {
				if (CreateDatabaseAndTable.check()) {
					if (!Files.exists(Paths.get("C://Backup"))) {
						Files.createDirectories(Paths.get("C://Backup"));
					}

					Parent root = FXMLLoader.load(LoginController.class.getResource("Login.fxml"));
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					stage.setScene(sc);
					stage.setTitle("Login");
					stage.getIcons().add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					stage.setResizable(false);

					stage.show();
				} else {
					AlertShow.showError("YOU ARE NOT ALLOWED TO USE!");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
