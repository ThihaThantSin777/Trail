package unit;

import java.time.LocalDate;

public class DataPass {
	private static String meDataPass;
	private static String svg;
	private static LocalDate date;
	private static String gameNo;

	public static void summaryDataPass(LocalDate dateNew, int gameNoNew) {
		date = dateNew;
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;
		gameNo = String.format("%" + "0" + twoOrThree + "d", gameNoNew);
	}
 
	public static void title(String newMeDataPass, String newSvg) {
		meDataPass = newMeDataPass;
		svg = newSvg;
	}

	public static String getMeDataPass() {
		return meDataPass;
	}

	public static String getSvg() {
		return svg;
	}

	public static LocalDate getDate() {
		return date;
	}

	public static String getGameNo() {
		return gameNo;
	}

}
