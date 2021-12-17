package entity;

import java.time.LocalDate;

import unit.DataPass;

public class WeekNo {
	private String dayOfWeek;
	private String winNo;
	private String me;
	private LocalDate date;

	public WeekNo(String dayOfWeek, int winNo, String me, LocalDate date) {
		this.dayOfWeek = dayOfWeek;
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.winNo = String.format("%" + "0" + twoOrThree + "d", winNo);
		this.me = me;
		this.date = date;
	}

	public WeekNo(int winNo, LocalDate date) {
		super();
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.winNo = String.format("%" + "0" + twoOrThree + "d", winNo);
		this.date = date;
	}

	public WeekNo(String dayOfWeek, int winNo) {
		super();
		this.dayOfWeek = dayOfWeek;
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		this.winNo = String.format("%" + "0" + twoOrThree + "d", winNo);
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getWinNo() {
		return winNo;
	}

	public String getMe() {
		return me;
	}

}
