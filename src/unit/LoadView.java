package unit;

import java.io.IOException;
import java.util.Optional;

import application.TwoDThreeDApplication;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoadView {
	private static Stage mStage;
	private static Stage eStage;
	private static Stage threeDStage;

	private static Stage mStageSummary;
	private static Stage eStageSummary;
	private static Stage threeDStageSummary;

	public void loadViewWithNormalStage(String fxml, String title) {
		Stage stage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/controller/".concat(fxml.concat(".fxml"))));
			Scene sc = new Scene(root);
			sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
			stage.setScene(sc);
			stage.setTitle(title);
//			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void loadViewWithStage(String fxml, String title) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/controller/".concat(fxml.concat(".fxml"))));
			if (title.equals("Morning")) {
				DataPass.title("Morning",
						"M12 6.5c3.79 0 7.17 2.13 8.82 5.5-1.65 3.37-5.02 5.5-8.82 5.5S4.83 15.37 3.18 12C4.83 8.63 8.21 6.5 12 6.5m0-2C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zm0 5c1.38 0 2.5 1.12 2.5 2.5s-1.12 2.5-2.5 2.5-2.5-1.12-2.5-2.5 1.12-2.5 2.5-2.5m0-2c-2.48 0-4.5 2.02-4.5 4.5s2.02 4.5 4.5 4.5 4.5-2.02 4.5-4.5-2.02-4.5-4.5-4.5z");
				if (mStage == null) {
					mStage = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					mStage.setScene(sc);
					mStage.setMaximized(true);
					mStage.getIcons().add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					mStage.setTitle("2D Morning");
					mStage.initStyle(StageStyle.UNDECORATED);
					exitRequest(mStage);
					mStage.show();
				} else if (mStage.isShowing()) {
					mStage.toFront();
				} else {
					mStage.show();
				}

			} else if (title.equals("Evening")) {
				DataPass.title("Evening",
						"M12 6c3.79 0 7.17 2.13 8.82 5.5-.59 1.22-1.42 2.27-2.41 3.12l1.41 1.41c1.39-1.23 2.49-2.77 3.18-4.53C21.27 7.11 17 4 12 4c-1.27 0-2.49.2-3.64.57l1.65 1.65C10.66 6.09 11.32 6 12 6zm-1.07 1.14L13 9.21c.57.25 1.03.71 1.28 1.28l2.07 2.07c.08-.34.14-.7.14-1.07C16.5 9.01 14.48 7 12 7c-.37 0-.72.05-1.07.14zM2.01 3.87l2.68 2.68C3.06 7.83 1.77 9.53 1 11.5 2.73 15.89 7 19 12 19c1.52 0 2.98-.29 4.32-.82l3.42 3.42 1.41-1.41L3.42 2.45 2.01 3.87zm7.5 7.5l2.61 2.61c-.04.01-.08.02-.12.02-1.38 0-2.5-1.12-2.5-2.5 0-.05.01-.08.01-.13zm-3.4-3.4l1.75 1.75c-.23.55-.36 1.15-.36 1.78 0 2.48 2.02 4.5 4.5 4.5.63 0 1.23-.13 1.77-.36l.98.98c-.88.24-1.8.38-2.75.38-3.79 0-7.17-2.13-8.82-5.5.7-1.43 1.72-2.61 2.93-3.53z");
				if (eStage == null) {
					eStage = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					eStage.setScene(sc);
					eStage.setMaximized(true);
					eStage.getIcons().add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					eStage.setTitle("2D Evening");
					eStage.initStyle(StageStyle.UNDECORATED);
					exitRequest(eStage);
					eStage.show();
				} else if (eStage.isShowing()) {
					eStage.toFront();
				} else {
					eStage.show();
				}

			} else if (title.equals("3D")) {
				DataPass.title("3D",
						"M12 6.5c3.79 0 7.17 2.13 8.82 5.5-1.65 3.37-5.02 5.5-8.82 5.5S4.83 15.37 3.18 12C4.83 8.63 8.21 6.5 12 6.5m0-2C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zm0 5c1.38 0 2.5 1.12 2.5 2.5s-1.12 2.5-2.5 2.5-2.5-1.12-2.5-2.5 1.12-2.5 2.5-2.5m0-2c-2.48 0-4.5 2.02-4.5 4.5s2.02 4.5 4.5 4.5 4.5-2.02 4.5-4.5-2.02-4.5-4.5-4.5z");
				if (threeDStage == null) {
					threeDStage = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					threeDStage.setScene(sc);
					threeDStage.setMaximized(true);
					threeDStage.getIcons()
							.add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					threeDStage.setTitle("3D");
					threeDStage.initStyle(StageStyle.UNDECORATED);
					exitRequest(threeDStage);
					threeDStage.show();
				} else if (threeDStage.isShowing()) {
					threeDStage.toFront();
				} else {
					threeDStage.show();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void loadViewWithStageSummary(String fxml, String title) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/controller/".concat(fxml.concat(".fxml"))));
			if (title.equals("Morning")) {
				DataPass.title("Morning",
						"M12 6.5c3.79 0 7.17 2.13 8.82 5.5-1.65 3.37-5.02 5.5-8.82 5.5S4.83 15.37 3.18 12C4.83 8.63 8.21 6.5 12 6.5m0-2C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zm0 5c1.38 0 2.5 1.12 2.5 2.5s-1.12 2.5-2.5 2.5-2.5-1.12-2.5-2.5 1.12-2.5 2.5-2.5m0-2c-2.48 0-4.5 2.02-4.5 4.5s2.02 4.5 4.5 4.5 4.5-2.02 4.5-4.5-2.02-4.5-4.5-4.5z");
				if (mStageSummary == null) {
					mStageSummary = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					mStageSummary.setScene(sc);
					mStageSummary.setMaximized(true);
					mStageSummary.getIcons()
							.add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					mStageSummary.setTitle("2D Morning Summary");
					exitRequest(mStageSummary);
					mStageSummary.show();
				} else if (mStageSummary.isShowing()) {
					mStageSummary.toFront();
				} else {
					mStageSummary = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					mStageSummary.setScene(sc);
					mStageSummary.setMaximized(true);
					mStageSummary.getIcons()
							.add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					mStageSummary.setTitle("2D Morning Summary");
					exitRequest(mStageSummary);
					mStageSummary.show();
				}

			} else if (title.equals("Evening")) {
				DataPass.title("Evening",
						"M12 6c3.79 0 7.17 2.13 8.82 5.5-.59 1.22-1.42 2.27-2.41 3.12l1.41 1.41c1.39-1.23 2.49-2.77 3.18-4.53C21.27 7.11 17 4 12 4c-1.27 0-2.49.2-3.64.57l1.65 1.65C10.66 6.09 11.32 6 12 6zm-1.07 1.14L13 9.21c.57.25 1.03.71 1.28 1.28l2.07 2.07c.08-.34.14-.7.14-1.07C16.5 9.01 14.48 7 12 7c-.37 0-.72.05-1.07.14zM2.01 3.87l2.68 2.68C3.06 7.83 1.77 9.53 1 11.5 2.73 15.89 7 19 12 19c1.52 0 2.98-.29 4.32-.82l3.42 3.42 1.41-1.41L3.42 2.45 2.01 3.87zm7.5 7.5l2.61 2.61c-.04.01-.08.02-.12.02-1.38 0-2.5-1.12-2.5-2.5 0-.05.01-.08.01-.13zm-3.4-3.4l1.75 1.75c-.23.55-.36 1.15-.36 1.78 0 2.48 2.02 4.5 4.5 4.5.63 0 1.23-.13 1.77-.36l.98.98c-.88.24-1.8.38-2.75.38-3.79 0-7.17-2.13-8.82-5.5.7-1.43 1.72-2.61 2.93-3.53z");
				if (eStageSummary == null) {
					eStageSummary = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					eStageSummary.setScene(sc);
					eStageSummary.setMaximized(true);
					eStageSummary.getIcons()
							.add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					eStageSummary.setTitle("2D Evening Summary");
					exitRequest(eStageSummary);
					eStageSummary.show();
				} else if (eStageSummary.isShowing()) {
					eStageSummary.toFront();
				} else {
					eStageSummary = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					eStageSummary.setScene(sc);
					eStageSummary.setMaximized(true);
					eStageSummary.getIcons()
							.add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					eStageSummary.setTitle("2D Evening Summary");
					exitRequest(eStageSummary);
					eStageSummary.show();
				}

			} else if (title.equals("3D")) {
				DataPass.title("3D",
						"M12 6.5c3.79 0 7.17 2.13 8.82 5.5-1.65 3.37-5.02 5.5-8.82 5.5S4.83 15.37 3.18 12C4.83 8.63 8.21 6.5 12 6.5m0-2C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zm0 5c1.38 0 2.5 1.12 2.5 2.5s-1.12 2.5-2.5 2.5-2.5-1.12-2.5-2.5 1.12-2.5 2.5-2.5m0-2c-2.48 0-4.5 2.02-4.5 4.5s2.02 4.5 4.5 4.5 4.5-2.02 4.5-4.5-2.02-4.5-4.5-4.5z");
				if (threeDStageSummary == null) {
					threeDStageSummary = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					threeDStageSummary.setScene(sc);
					threeDStageSummary.setMaximized(true);
					threeDStageSummary.getIcons()
							.add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					threeDStageSummary.setTitle("3D Summary");
					exitRequest(threeDStageSummary);
					threeDStageSummary.show();
				} else if (threeDStageSummary.isShowing()) {
					threeDStageSummary.toFront();
				} else {
					threeDStageSummary = new Stage();
					Scene sc = new Scene(root);
					sc.getStylesheets().add(getClass().getResource("/controller/layout.css").toExternalForm());
					threeDStageSummary.setScene(sc);
					threeDStageSummary.setMaximized(true);
					threeDStageSummary.getIcons()
							.add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
					threeDStageSummary.setTitle("3D Summary");
					exitRequest(threeDStageSummary);
					threeDStageSummary.show();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void loadViewWithoutStage(String fxml, VBox vb) {
		TranslateTransition tt = new TranslateTransition(Duration.seconds(0.8), vb);
		TranslateTransition tt2 = new TranslateTransition(Duration.seconds(0.8), vb);
		SequentialTransition st = new SequentialTransition(tt, tt2);
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/controller/".concat(fxml.concat(".fxml"))));
			tt.setFromX(0);
			tt.setToX(vb.getWidth());
			tt.setOnFinished(e -> {
				vb.getChildren().clear();
				vb.getChildren().add(root);

			});

			tt2.setFromX(vb.getWidth());
			tt2.setToX(0);

			st.play();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void exitRequest(Stage stage) {
		stage.setOnCloseRequest(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to exit?", ButtonType.YES,
					ButtonType.NO);
			Optional<ButtonType> op = alert.showAndWait();
			if (op.get() == ButtonType.NO) {
				e.consume();
				alert.close();
			} else if (op.get() == ButtonType.YES) {
				if (DataPass.getMeDataPass().equals("Morning")) {
					if (CheckBackupTime.isOver1Hour()) {

					} else {

					}
				} else {
					if (CheckBackupTime.isOver5Hour()) {

					} else {

					}
				}
			}
		});
	}
}
