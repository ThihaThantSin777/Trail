package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import application.TwoDThreeDException;
import database2d3d.ExceedingImplementation;
import database2d3d.TwoDImplementation;
import database2d3d.WinNumberImplementation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import service.ExceedingService;
import service.TwoDService;
import service.WinNumberService;
import unit.AlertShow;
import unit.DataPass;

public class SpecificDateController implements Initializable {
	@FXML
	private DatePicker from;
	@FXML
	private DatePicker to;

	TwoDService twoDService;
	ExceedingService exceedingService;
	WinNumberService winNumberService;

	private void checkError() {
		if (to.getValue() == null) {
			throw new TwoDThreeDException("Please choose to date");
		}
		if (from.getValue().isAfter(to.getValue())) {
			throw new TwoDThreeDException("errr");
		}

	}

	public void delete() {
		try {
			checkError();
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete?", ButtonType.YES,
					ButtonType.NO);
			Optional<ButtonType> op = alert.showAndWait();
			if (op.get() == ButtonType.NO) {
				alert.close();
			} else if (op.get() == ButtonType.YES) {
				twoDService.deleteWithDate(from.getValue(), to.getValue(), DataPass.getMeDataPass());
				exceedingService.deleteWithDate(from.getValue(), to.getValue(), DataPass.getMeDataPass());
				winNumberService.deleteWithDate(from.getValue(), to.getValue(), DataPass.getMeDataPass());
				AlertShow.showInfo("Delete Success");
				from.getScene().getWindow().hide();
			}
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		to.setValue(LocalDate.now());
		twoDService = new TwoDImplementation();
		exceedingService = new ExceedingImplementation();
		winNumberService = new WinNumberImplementation();

		LocalDate current = LocalDate.now();
		to.setValue(current);
		from.setValue(twoDService.getFirstLineDate());
		from.setDayCellFactory(dayCellFactory);
		to.setDayCellFactory(dayCellFactory);
		from.valueProperty().addListener((a, b, c) -> {
			to.setDayCellFactory(d ->

			new DateCell() {
				@Override
				public void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item.isBefore(c) || twoDService.getFirstLineDate().isAfter(item)
							|| LocalDate.now().isBefore(item)) {
						setDisable(true);
					}

				}
			});
		});
		to.valueProperty().addListener((ov, oldValue, newValue) -> {
			from.setDayCellFactory(d ->

			new DateCell() {
				@Override
				public void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item.isAfter(newValue) || twoDService.getFirstLineDate().isAfter(item)) {
						setDisable(true);
					}

				}
			});
		});

	}

	final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
		public DateCell call(final DatePicker datePicker) {
			return new DateCell() {
				@Override
				public void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);

					if (twoDService.getFirstLineDate().isAfter(item) || LocalDate.now().isBefore(item)) {
						setDisable(true);
					}
				}
			};
		}
	};

}