package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import database2d3d.ExceedingImplementation;
import database2d3d.TwoDImplementation;
import database2d3d.WinNumberImplementation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import service.ExceedingService;
import service.TwoDService;
import service.WinNumberService;
import unit.AlertShow;
import unit.DBConection;
import unit.DataPass;

public class CustomizeDateController implements Initializable {
	TwoDService twoDService;
	ExceedingService exceedingService;
	WinNumberService winNumberService;
	LocalDate to = LocalDate.now();
	@FXML
	VBox vb;

	public void oneWeek() {
		LocalDate from = getFirstLineDate();
		to = from.plusDays(7);
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete?", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> op = alert.showAndWait();
		if (op.get() == ButtonType.NO) {
			alert.close();
		} else if (op.get() == ButtonType.YES) {
			twoDService.deleteWithDate(from, to, DataPass.getMeDataPass());
			exceedingService.deleteWithDate(from, to, DataPass.getMeDataPass());
			winNumberService.deleteWithDate(from, to, DataPass.getMeDataPass());
			AlertShow.showInfo("Delete Success");
		}
		vb.getScene().getWindow().hide();
	}

	public void oneMonth() {
		LocalDate from = getFirstLineDate();
		to = from.plusMonths(1);
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete?", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> op = alert.showAndWait();
		if (op.get() == ButtonType.NO) {
			alert.close();
		} else if (op.get() == ButtonType.YES) {
			twoDService.deleteWithDate(from, to, DataPass.getMeDataPass());
			exceedingService.deleteWithDate(from, to, DataPass.getMeDataPass());
			winNumberService.deleteWithDate(from, to, DataPass.getMeDataPass());
			AlertShow.showInfo("Delete Success");
		}
		vb.getScene().getWindow().hide();
	}

	public void sixMonths() {
		LocalDate from = getFirstLineDate();
		to = from.plusMonths(6);
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete?", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> op = alert.showAndWait();
		if (op.get() == ButtonType.NO) {
			alert.close();
		} else if (op.get() == ButtonType.YES) {
			twoDService.deleteWithDate(from, to, DataPass.getMeDataPass());
			exceedingService.deleteWithDate(from, to, DataPass.getMeDataPass());
			winNumberService.deleteWithDate(from, to, DataPass.getMeDataPass());
			AlertShow.showInfo("Delete Success");
		}
		vb.getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		twoDService = new TwoDImplementation();
		exceedingService = new ExceedingImplementation();
		winNumberService = new WinNumberImplementation();
	}

	private LocalDate getFirstLineDate() {
		String sql = "SELECT date FROM twod_user_play ORDER BY date LIMIT 1";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getDate(1).toLocalDate();
			}
		} catch (Exception e) {
		}
		return LocalDate.now();
	}
}
