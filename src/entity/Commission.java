package entity;

import com.jfoenix.controls.JFXTextField;

import application.TwoDThreeDException;
import javafx.scene.control.Label;
import unit.AlertShow;

public class Commission {
	private String customerID;
	private Label customerName;
	private Label totalAmount;
	private Label price;
	private Label date;
	private Label dateTo;
	private JFXTextField value;

	public Label getDateTo() {
		return dateTo;
	}

	public void setDateTo(Label dateTo) {
		this.dateTo = dateTo;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public Label getCustomerName() {
		return customerName;
	}

	public void setCustomerName(Label customerName) {
		this.customerName = customerName;
	}

	public Label getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Label totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Label getPrice() {
		return price;
	}

	public void setPrice(Label price) {
		this.price = price;
	}

	public Label getDate() {
		return date;
	}

	public void setDate(Label date) {
		this.date = date;
	}

	public JFXTextField getValue() {
		return value;
	}

	private void checkError(String text) {

		try {
			Integer.parseInt(text);
		} catch (Exception e) {
			throw new TwoDThreeDException("Please enter digit only");
		}

	}

	public void setValue(JFXTextField value) {

		this.value = value;
		if (!value.getText().isEmpty()) {
			try {

				checkError(value.getText());
				int value1 = Integer.parseInt(value.getText());
				int value2 = Integer.parseInt(totalAmount.getText()) / 100;
				price.setText(String.valueOf(value1 * value2));
			} catch (Exception e) {
				AlertShow.showError(e.getMessage());
			}
		}
		value.textProperty().addListener((a, b, c) -> {
			try {
				if (c.isEmpty()) {
					price.setText("0");
					return;
				}
				checkError(c);
				int value1 = Integer.parseInt(c);
				int value2 = Integer.parseInt(totalAmount.getText()) / 100;
				price.setText(String.valueOf(value1 * value2));
			} catch (Exception e) {
				AlertShow.showError(e.getMessage());
			}
		});
	}

}
