package entity;

import java.time.LocalDate;

import unit.DataPass;

public class Exceeding {
	private String exceedingNumber;
	private int exceedingAmount;
	private LocalDate date;
	private String me;
	private boolean isNoted;

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setMe(String me) {
		this.me = me;
	}

	public Exceeding(String exceedingNumber, int exceedingAmount) {
		super();
		this.exceedingNumber = exceedingNumber;
		this.exceedingAmount = exceedingAmount;
	}

	public Exceeding(int exceedingNumber, int exceedingAmount, LocalDate date, String me, boolean isNoted) {
		super();
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.exceedingNumber = String.format("%" + "0" + twoOrThree + "d", exceedingNumber);
		this.exceedingAmount = exceedingAmount;
		this.date = date;
		this.me = me;
		this.isNoted = isNoted;
	}

	public Exceeding(int exceedingNumber, int exceedingAmount, boolean isNoted) {
		super();
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.exceedingNumber = String.format("%" + "0" + twoOrThree + "d", exceedingNumber);
		this.exceedingAmount = exceedingAmount;
		this.isNoted = isNoted;
	}

	public Exceeding(int exceedingNumber, int exceedingAmount, LocalDate date, String me) {
		super();
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.exceedingNumber = String.format("%" + "0" + twoOrThree + "d", exceedingNumber);
		this.exceedingAmount = exceedingAmount;
		this.date = date;
		this.me = me;
	}

	public Exceeding(int exceedingNumber, String me, LocalDate date) {
		super();
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.exceedingNumber = String.format("%" + "0" + twoOrThree + "d", exceedingNumber);
		this.date = date;
		this.me = me;
	}

	public Exceeding(LocalDate date, String me) {
		super();
		this.date = date;
		this.me = me;
	}

	public String getExceedingNumber() {
		return exceedingNumber;
	}

	public int getExceedingAmount() {
		return exceedingAmount;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getMe() {
		return me;
	}

	public boolean isNoted() {
		return isNoted;
	}

}
