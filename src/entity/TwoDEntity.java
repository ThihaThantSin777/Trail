package entity;

import java.time.LocalDate;

public class TwoDEntity {
	private String customerName;
	private int amount;
	private String me;
	private LocalDate date;
	private int playNumber;
	private int extract_amount;

	public TwoDEntity(String me, LocalDate date, int playNumber, int extract_amount) {
		super();
		this.me = me;
		this.date = date;
		this.playNumber = playNumber;
		this.extract_amount = extract_amount;
	}

	public TwoDEntity(int amount, String me, LocalDate date, int playNumber, int extract_amount) {
		super();
		this.amount = amount;
		this.me = me;
		this.date = date;
		this.playNumber = playNumber;
		this.extract_amount = extract_amount;
	}

	public TwoDEntity(String customerName, int amount) {
		super();
		this.customerName = customerName;
		this.amount = amount;
	}

	public TwoDEntity(int amount, String me, LocalDate date) {
		super();
		this.amount = amount;
		this.me = me;
		this.date = date;
	}

	public TwoDEntity(String customerName, String me, LocalDate date, int playNumber) {
		super();
		this.customerName = customerName;
		this.me = me;
		this.date = date;

		this.playNumber = playNumber;
	}

	public TwoDEntity(String me, LocalDate date, int playNumber) {
		super();
		this.me = me;
		this.date = date;

		this.playNumber = playNumber;
	}

	public TwoDEntity(int amount, int playNumber) {
		super();
		this.amount = amount;
		this.playNumber = playNumber;
	}

	public int getExtract_amount() {
		return extract_amount;
	}

	public int getPlayNumber() {
		return playNumber;
	}

	public String getMe() {
		return me;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getCustomerName() {
		return customerName;
	}

	public int getAmount() {
		return amount;
	}

}
