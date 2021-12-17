package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import database2d3d.CustomerImplematation;
import entity.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import service.CustomerService;
import unit.DBConection;

public class CustomerDetailsController implements Initializable {
	@FXML
	private TableView<Customer> table;
	@FXML
	private TextField searchName;
	@FXML
	private Label totalCustommer1;
	@FXML
	private Label totalInCustomer1;
	@FXML
	private Label totalOutCustommer1;
	CustomerService customerService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customerService = new CustomerImplematation();
		table.getItems().addAll(changeColor(loadData()));
		searchName.textProperty().addListener((a, b, c) -> {
			search(c);
			total();
		});
		total();

		table.setOnMouseClicked(e -> {
			Customer customer = table.getSelectionModel().getSelectedItem();
			if (customer != null) {
				if (e.getClickCount() == 2) {
					Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete?", ButtonType.YES,
							ButtonType.NO);
					Optional<ButtonType> op = alert.showAndWait();
					if (op.get() == ButtonType.NO) {
						alert.close();
					} else if (op.get() == ButtonType.YES) {
						customerService.deleteCustomer(customer.getCustomerID());
						table.getItems().clear();
						table.getItems().addAll(changeColor(loadData()));
//						totalInCustomer.getScene().getWindow().hide();
						total();
					}
				}
			}
		});

	}

	public void total() {
		int in = customerService.getTotalCustomerNumber(true);
		int out = customerService.getTotalCustomerNumber(false);
		int toatlAll = in + out;
		int inTotal = 0;
		int outTotal = 0;
		List<Customer> list = table.getItems();
		for (Customer customer : list) {
			if (customer.getCustomerID().contains("I")) {
				inTotal++;
			} else {
				outTotal++;
			}
		}
		totalCustommer1.setText(toatlAll + " PERSONS");
		totalInCustomer1.setText(inTotal + " PERSONS");
		totalOutCustommer1.setText(outTotal + " PERSONS");
	}

	private void search(String name) {
		List<Customer> list = customerService.searchByName(name);
		table.getItems().clear();
		table.getItems().addAll(list);
	}

	private List<Customer> loadData() {
		List<Customer> list = new ArrayList<Customer>();
		String sql = "SELECT * FROM customer WHERE customer_id !='O-00001'";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Customer customer = new Customer();
				customer.setCustomerID(rs.getString(1));
				customer.setName(new Label(rs.getString(2)));
				customer.setDate(new Label(rs.getDate(3).toString()));
				list.add(customer);

			}
		} catch (Exception e) {
			System.err.println("1:" + e.getMessage());
		}

		return list;
	}

	private List<Customer> changeColor(List<Customer> list) {
		List<Customer> result = new ArrayList<Customer>();
		for (Customer commercial : list) {
			if (commercial.getCustomerID().contains("O")) {
				commercial.getName().setStyle("-fx-text-fill:red;-fx-font-size: 17;");
				commercial.getDate().setStyle("-fx-text-fill:red;-fx-font-size: 17;");
			} else {
				commercial.getName().setStyle("-fx-text-fill:black;-fx-font-size: 17;");
				commercial.getDate().setStyle("-fx-text-fill:black;-fx-font-size: 17;");

			}
			result.add(commercial);
		}
		return result;
	}

}
