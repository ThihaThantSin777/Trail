package entity;

import unit.DataPass;

public class OutSource {
	private String customer_id;
	private int exceeding_id;
	private int exceedingAmount;
	private String exceedingNumber;
	private int currentAmt;
	private String customername;

	public OutSource(String customer_id, int exceeding_id, int currentAmt) {
		super();
		this.customer_id = customer_id;
		this.exceeding_id = exceeding_id;
		this.currentAmt = currentAmt;
	}

	public OutSource(int exceedingNumber, int currentAmt) {
		super();
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.exceedingNumber = String.format("%" + "0" + twoOrThree + "d", exceedingNumber);
		this.currentAmt = currentAmt;
	}

	public String getCustomername() {
		return customername;
	}

	public String getExceedingNumber() {
		return exceedingNumber;
	}

	public int getExceedingAmount() {
		return exceedingAmount;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public int getExceeding_id() {
		return exceeding_id;
	}

	public int getCurrentAmt() {
		return currentAmt;
	}

	@Override
	public String toString() {
		return "OutSource [customer_id=" + customer_id + ", exceeding_id=" + exceeding_id + ", exceedingAmount="
				+ exceedingAmount + ", exceedingNumber=" + exceedingNumber + ", currentAmt=" + currentAmt
				+ ", customername=" + customername + "]";
	}

}
