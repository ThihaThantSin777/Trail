package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import application.TwoDThreeDException;
import database2d3d.CustomerImplematation;
import database2d3d.TwoDImplementation;
import entity.Commission;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import service.CustomerService;
import service.TwoDService;
import unit.AlertShow;
import unit.DBConection;
import unit.DataPass;

public class CommercialController implements Initializable {
	@FXML
	private TableView<Commission> table;
	@FXML
	private JFXDatePicker from;
	@FXML
	private JFXDatePicker to;
	@FXML
	private JFXComboBox<Label> cuName;
	@FXML
	private TextField overAll;
	@FXML
	private Label totalCustomer;
	@FXML
	private Label totalAmt;
	@FXML
	private Label totalCom;
	String morE = DataPass.getMeDataPass().equals("Morning") ? "Morning"
			: DataPass.getMeDataPass().equals("Evening") ? "Evening" : "3D";
	DecimalFormat df = new DecimalFormat("###,###,###");
	CustomerService customerService;
	TwoDService twoDService;

	public void search() {
		List<Commission> list = changeColor(loadData());
		table.getItems().clear();
		table.getItems().addAll(list);
		plus();
	}

	final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
		public DateCell call(final DatePicker datePicker) {
			return new DateCell() {
				@Override
				public void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);

					if (twoDService.getFirstLineDate().isAfter(item) || LocalDate.now().isBefore(item)
							|| from.getValue().isAfter(item)) {
						setDisable(true);
					}
				}
			};
		}
	};
	final Callback<DatePicker, DateCell> dayCellFactory2 = new Callback<DatePicker, DateCell>() {
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customerService = new CustomerImplematation();
		twoDService = new TwoDImplementation();
		Label all = new Label("All");
		all.setStyle("-fx-text-fill:black;-fx-font-weight: bold;");
		cuName.getItems().add(all);
		List<Label> inList = customerService.getAllCustomerData(true);
		List<Label> outList = customerService.getAllCustomerData(false);
		cuName.getItems().addAll(inList);
		cuName.getItems().addAll(outList);
		cuName.getSelectionModel().selectFirst();
		overAll.setStyle("-fx-text-fill: #025c53; -fx-font-weight: bold;");
		from.setValue(LocalDate.now());
		to.setValue(LocalDate.now());

		from.setDayCellFactory(dayCellFactory2);
		to.setDayCellFactory(dayCellFactory);
		from.valueProperty().addListener((a, b, c) -> {
			to.setDayCellFactory(d ->

			new DateCell() {
				@Override
				public void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item.isBefore(c) || twoDService.getFirstLineDate().isAfter(item)
							|| LocalDate.now().isBefore(item) || from.getValue().isAfter(item)) {
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

		table.getItems().addAll(changeColor(loadData()));
		plus();
		from.valueProperty().addListener((a, b, c) -> {
			List<Commission> dlist = changeColor(loadData());
			table.getItems().clear();
			table.getItems().addAll(dlist);
			plus();
		});
		to.valueProperty().addListener((a, b, c) -> {
			List<Commission> dlist = changeColor(loadData());
			table.getItems().clear();
			table.getItems().addAll(dlist);
			plus();
		});
		overAll.textProperty().addListener((a, b, c) -> {
			List<Commission> olist = changeColor(loadData());
			table.getItems().clear();
			table.getItems().addAll(olist);
			plus();
		});

	}

	private void plus() {
		int totalAmt = 0;
		int customerTotal = 0;
		int comTotal = 0;
		for (Commission commission : table.getItems()) {
			totalAmt += Integer.parseInt(commission.getTotalAmount().getText());
			comTotal += Integer.parseInt(commission.getPrice().getText());
			customerTotal++;
		}
		totalCom.setText(df.format(comTotal) + " MMK");
		totalCustomer.setText(df.format(customerTotal) + " PERSONS");
		this.totalAmt.setText(df.format(totalAmt) + " MMK");
	}

	private void checkError(String text) {
		if (text.isEmpty()) {
			return;
		}
		try {
			Integer.parseInt(text);
		} catch (Exception e) {
			throw new TwoDThreeDException("Please enter digit only");
		}

	}

	private List<Commission> loadData() {
		List<Commission> list = new ArrayList<Commission>();
		String sql = "SELECT customer.customer_id,customer.name,twod_user_play.play_number,SUM(twod_user_play.amount),twod_user_play.date FROM customer LEFT JOIN twod_user_play ON customer.customer_id=twod_user_play.customer_id WHERE twod_user_play.me=? AND twod_user_play.date BETWEEN ? AND ? ";
		if (!cuName.getValue().getText().equals("All")) {
			sql += "AND customer.name=? ";
		}
		sql += "GROUP BY customer.customer_id;";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, morE);
			stmt.setDate(2, Date.valueOf(from.getValue()));
			LocalDate temp = to.getValue() == null ? from.getValue() : to.getValue();
			stmt.setDate(3, Date.valueOf(temp));
			if (!cuName.getValue().getText().equals("All")) {
				stmt.setString(4, cuName.getValue().getText());
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				Commission commercial = new Commission();
				commercial.setCustomerID(rs.getString(1));
				commercial.setCustomerName(new Label(rs.getString(2)));
				commercial.setTotalAmount(new Label(String.valueOf(rs.getInt(4))));
				commercial.setDate(new Label(from.getValue().toString()));
				commercial.setDateTo(new Label(to.getValue().toString()));
				commercial.setPrice(new Label("0"));
				JFXTextField tf = new JFXTextField();
				tf.setText(overAll.getText().isEmpty() ? "" : overAll.getText());
				tf.setPromptText(" % ");
				tf.setAlignment(Pos.CENTER);
				tf.textProperty().addListener((a, b, c) -> {
					try {
						if (c.isEmpty()) {
							commercial.getPrice().setText("0");

						} else {
							checkError(c);
							int value1 = Integer.parseInt(c);
							int value2 = Integer.parseInt(commercial.getTotalAmount().getText()) / 100;
							commercial.getPrice().setText(String.valueOf(value1 * value2));

						}
						plus();
					} catch (Exception e) {
						AlertShow.showError(e.getMessage());
					}
				});
				commercial.setValue(tf);
				list.add(commercial);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String sql3 = "SELECT customer.customer_id,customer.name,exceeding.exceeded_number,SUM(outsource.currentAmount),exceeding.date FROM customer JOIN outsource ON outsource.customer_id=customer.customer_id JOIN exceeding ON exceeding.exceeding_id=outsource.exceeding_id WHERE exceeding.me=? AND customer.customer_id!='O-00001' AND exceeding.date BETWEEN ? AND ? ";
		if (!cuName.getValue().getText().equals("All")) {
			sql3 += "AND customer.name=? ";
		}
		sql3 += "GROUP BY customer.customer_id;";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql3)) {
			stmt.setString(1, morE);
			stmt.setDate(2, Date.valueOf(from.getValue()));
			LocalDate temp = to.getValue() == null ? from.getValue() : to.getValue();
			stmt.setDate(3, Date.valueOf(temp));
			if (!cuName.getValue().getText().equals("All")) {
				stmt.setString(4, cuName.getValue().getText());
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				Commission commercial = new Commission();
				commercial.setCustomerID(rs.getString(1));
				commercial.setCustomerName(new Label(rs.getString(2)));
				commercial.setTotalAmount(new Label(String.valueOf(rs.getInt(4))));
// commercial.setDate(new Label(LocalDate.parse(rs.getString(5)).toString()));
				commercial.setDate(new Label(from.getValue().toString()));
				commercial.setDateTo(new Label(to.getValue().toString()));
				commercial.setPrice(new Label("0"));
				JFXTextField tf = new JFXTextField();
				tf.setText(overAll.getText().isEmpty() ? "" : overAll.getText());
				tf.setPromptText(" % ");
				tf.setAlignment(Pos.CENTER);
				tf.textProperty().addListener((a, b, c) -> {
					try {
						if (c.isEmpty()) {
							commercial.getPrice().setText("0");
							return;
						}
						checkError(c);
						int value1 = Integer.parseInt(c);
						int value2 = Integer.parseInt(commercial.getTotalAmount().getText()) / 100;
						commercial.getPrice().setText(String.valueOf(value1 * value2));
						plus();
					} catch (Exception e) {
						AlertShow.showError(e.getMessage());
					}
				});
				commercial.setValue(tf);
				list.add(commercial);

			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return list.stream().filter(e -> !e.getCustomerID().equals("O-00001")).collect(Collectors.toList());
	}

	private List<Commission> changeColor(List<Commission> list) {
		List<Commission> result = new ArrayList<Commission>();
		for (Commission commercial : list) {
			if (commercial.getCustomerID().contains("O")) {
				commercial.getCustomerName().setStyle("-fx-text-fill:red;-fx-font-size: 17;");
				commercial.getTotalAmount().setStyle("-fx-text-fill:red;-fx-font-size: 17;");
				commercial.getDate().setStyle("-fx-text-fill:red;-fx-font-size: 17;");
				commercial.getPrice().setStyle("-fx-text-fill:red;-fx-font-size: 17;");
				commercial.getValue().setStyle("-fx-text-fill:#025c53;-fx-font-size: 17; -fx-font-weight: bold;");

			} else {
				commercial.getCustomerName().setStyle("-fx-text-fill:black;-fx-font-size: 17;");
				commercial.getTotalAmount().setStyle("-fx-text-fill:black;-fx-font-size: 17;");
				commercial.getDate().setStyle("-fx-text-fill:black;-fx-font-size: 17;");
				commercial.getPrice().setStyle("-fx-text-fill:black;-fx-font-size: 17;");
				commercial.getValue().setStyle("-fx-text-fill: #025c53;-fx-font-size: 17; -fx-font-weight: bold;");
			}
			result.add(commercial);
		}
		return result;
	}
}