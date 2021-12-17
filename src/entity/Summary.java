package entity;

import javafx.scene.control.Label;

public class Summary {
	private Label customerID;
	private Label customerName;
	private Label gameNo;
	private Label totalAmt;
	private Label gameAmt;
	private Label netTotal;
	private Label date;
	private Label round;

	public Summary() {
		// TODO Auto-generated constructor stub
	}

	public Label getRound() {
		return round;
	}

	public void setRound(Label round) {
		this.round = round;
	}

	public Label getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Label customerID) {
		this.customerID = customerID;
	}

	public Label getCustomerName() {
		return customerName;
	}

	public void setCustomerName(Label customerName) {
		this.customerName = customerName;
	}

	public Label getGameNo() {
		return gameNo;
	}

	public void setGameNo(Label gameNo) {
		this.gameNo = gameNo;
	}

	public Label getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Label totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Label getGameAmt() {
		return gameAmt;
	}

	public void setGameAmt(Label gameAmt) {
		this.gameAmt = gameAmt;
	}

	public Label getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Label netTotal) {
		this.netTotal = netTotal;
	}

	public Label getDate() {
		return date;
	}

	public void setDate(Label date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return customerID.getText() + "," + customerName.getText() + "," + gameNo.getText() + "," + totalAmt.getText()
				+ "," + gameAmt.getText() + "," + netTotal.getText() + "," + date.getText() + "," + round.getText()
				+ "\n";
	}

}
