package database2d3d;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import controller.TwoDThreeDHomeController;
import entity.ObjectForCurrentTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import unit.DBConection;

public class QueriesForTowDMorning {
	TwoDThreeDHomeController twoDHome = new TwoDThreeDHomeController();
//	private int inCustomerCount = 0;
//	private int outCustomerCount = 0;

	private final String url = "jdbc:mysql://localhost:3306/2d3d";
	private final String userName = "root";
	private final String password = "123#asdFF";

	public Connection createConnection() {
		try {
			return DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getAllTotalData(String me, LocalDate date) {
		String totalAmountForSpecificNum = "SELECT SUM(amount) FROM twod_user_play WHERE me=? AND date=?";
		try (Connection con = DBConection.createConnection();
				PreparedStatement pre = con.prepareStatement(totalAmountForSpecificNum);) {
			pre.setString(1, me);
			pre.setDate(2, Date.valueOf(date));
			ResultSet rs = pre.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getTotalPlayCount(String me, LocalDate date) {
		String totalplaycount = "SELECT COUNT(DISTINCT play_number) as total from twod_user_play WHERE me=? AND date=? AND amount>0";
		try (Connection con = DBConection.createConnection();
				PreparedStatement pre = con.prepareStatement(totalplaycount);) {
			pre.setString(1, me);
			pre.setDate(2, Date.valueOf(date));
			ResultSet rs = pre.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean updateIDandAmount(int amount, String customerID, int playNumber, LocalDate date, String me) {
		String sql = "UPDATE `twod_user_play` SET `amount`=?, `customer_id`=? WHERE customer_id='O-00001' AND play_number=? AND date=? AND me=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, amount);
			stmt.setString(2, customerID);
			stmt.setInt(3, playNumber);
			stmt.setDate(4, Date.valueOf(date));
			stmt.setString(5, me);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	public void insertCustomerName(String name, boolean in, LocalDate date) {

		String insertQuery = "INSERT INTO customer VALUES (?, ?,?)";
		String countRows = "SELECT COUNT(*) FROM `customer`";
		String customerNames = "SELECT name FROM customer";
		ArrayList<String> tempNamesStored = new ArrayList<>();

		try (Connection con = createConnection();
				PreparedStatement pre = con.prepareStatement(insertQuery);
				PreparedStatement pre1 = con.prepareStatement(countRows);
				PreparedStatement pre2 = con.prepareStatement(customerNames);
				ResultSet numbersOfRowReturn = pre1.executeQuery();
				ResultSet customerNamesReturn = pre2.executeQuery();) {

			while (numbersOfRowReturn.next()) {

				int numbersOfRow = numbersOfRowReturn.getInt(1);


				while (customerNamesReturn.next()) {
					tempNamesStored.add(customerNamesReturn.getString(1));
				}

				if (tempNamesStored.contains(name)) {
					twoDHome.ErrorAlert(name + "'s name already exists");
				} else {
					if (in) {
						String inCustomerID = String.format("I-%05d", ++numbersOfRow);
						pre.setString(1, inCustomerID);
						pre.setString(2, name);
						pre.setDate(3, Date.valueOf(date));
						pre.execute();
					} else {
						String outCustomerID = String.format("O-%05d", ++numbersOfRow);
						pre.setString(1, outCustomerID);
						pre.setString(2, name);
						pre.setDate(3, Date.valueOf(date));
						pre.execute();
					}
					twoDHome.informAlert(name + " is registered.");
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> addCustomerNamesIntoCombo() {
		String customerNamesQuery = "SELECT name FROM customer WHERE name!='O-00001'";
		ArrayList<String> tempNamesStored = new ArrayList<>();

		try (Connection con = createConnection();
				PreparedStatement pre = con.prepareStatement(customerNamesQuery);
				ResultSet rs = pre.executeQuery();) {

			while (rs.next()) {
				tempNamesStored.add(rs.getString(1));
			}
//			System.out.println(tempNamesStored + " ResultSet");
			return tempNamesStored;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void insertDataIntoTwoDUserPlayTable(String name, String playNumber, int amount, String me, LocalDate date) {

		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);
		String secondNumForNAN = "";
		if (playNumber.length() == 3) {
			secondNumForNAN = String.valueOf(playNumber.charAt(1));
		}
//		CALL insertDataForFormula("I-00001", 200, "Morning", 10-15-2021);

		if (playNumber.equalsIgnoreCase("ss")) {

			String insertQuery = "{CALL insertDataForSSFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("mm")) {

			String insertQuery = "{CALL insertDataForMMFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("sm")) {

			String insertQuery = "{CALL insertDataForSMFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("ms")) {

			String insertQuery = "{CALL insertDataForMSFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("sp")) {

			String insertQuery = "{CALL insertDataForSPFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("mp")) {

			String insertQuery = "{CALL insertDataForMPFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("**")) {

			String insertQuery = "{CALL insertDataForAAFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL insertDataForANFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, String.valueOf(playNumber.charAt(1)));
					pre1.setInt(6, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL insertDataForNAFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.setInt(6, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("B")) {

			String firstNum = playNumber.charAt(0) + "";
			int a = 0;
			int k = Integer.parseInt("1" + firstNum);

			String insertQuery = "{CALL insertDataForBFormula(?, ?, ?, ?, ?, ?)}";
			String insertQueryForEach = "{CALL insertForEachPlayNumber(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					CallableStatement pre2 = con.prepareCall(insertQueryForEach);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.setInt(6, 0);
					pre1.executeUpdate();

					pre2.setString(1, customerIDResult.getString(1));
					pre2.setInt(2, amount);
					pre2.setString(3, me);
					pre2.setDate(4, sqlDate);
					while (a <= Integer.parseInt("1" + firstNum)) {
						if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
							pre2.setString(5, String.valueOf(a).concat(String.valueOf(k)));
							pre2.setInt(6, 0);
							pre2.executeUpdate();
						}
						a++;
						k--;
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.length() == 2 && playNumber.endsWith("+")) {

			String insertQueryForAN = "{CALL insertDataForANFormula(?, ?, ?, ?, ?, ?)}";
			String insertQueryForNA = "{CALL insertDataForNAFormula(?, ?, ?, ?, ?, ?)}";
			String deleteDuplicate = "{CALL deleteDuplicateNumForPlus(?, ?, ?, ?, ?)}";

			String firstNum = playNumber.charAt(0) + "";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQueryForAN);
					CallableStatement pre2 = con.prepareCall(insertQueryForNA);
					CallableStatement pre3 = con.prepareCall(deleteDuplicate);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, firstNum);
					pre1.setInt(6, 0);
					pre1.executeUpdate();

					pre2.setString(1, customerIDResult.getString(1));
					pre2.setInt(2, amount);
					pre2.setString(3, me);
					pre2.setDate(4, sqlDate);
					pre2.setString(5, firstNum);
					pre2.setInt(6, 0);
					pre2.executeUpdate();

					pre3.setString(1, customerIDResult.getString(1));
					pre3.setString(2, me);
					pre3.setDate(3, sqlDate);
					pre3.setInt(4, amount);
					pre3.setString(5, firstNum);
					pre3.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("n")) {

			String insertQuery = "{CALL insertDataForNFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("p")) {

			String insertQuery = "{CALL insertDataForPFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("k")) {
			System.out.println(playNumber.substring(0, playNumber.length() - 1) + " without k");
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL insertForEachPlayNumber(?, ?, ?, ?, ?, ?)}";

			if (playNumWithoutK.endsWith("*")) {
				String[] khwayNumArr = playNumWithoutK.split("");
				for (int i = 0; i < khwayNumArr.length - 1; i++) {
					for (int j = 0; j < khwayNumArr.length - 1; j++) {

						try (Connection con = DriverManager.getConnection(url, userName, password);
								PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
								CallableStatement pre1 = con.prepareCall(insertQuery);
								ResultSet customerIDResult = pre.executeQuery();) {

							if (customerIDResult.next()) {
								pre1.setString(1, customerIDResult.getString(1));
								pre1.setInt(2, amount);
								pre1.setString(3, me);
								pre1.setDate(4, sqlDate);
								pre1.setString(5, khwayNumArr[i].concat(khwayNumArr[j]));
								pre1.setInt(6, 0);
								pre1.executeUpdate();

							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				String[] khwayNumArr = playNumWithoutK.split("");
				for (int i = 0; i < khwayNumArr.length; i++) {
					for (int j = 0; j < khwayNumArr.length; j++) {
						if (i == j) {
							continue;
						}
						try (Connection con = DriverManager.getConnection(url, userName, password);
								PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
								CallableStatement pre1 = con.prepareCall(insertQuery);
								ResultSet customerIDResult = pre.executeQuery();) {

							if (customerIDResult.next()) {
								pre1.setString(1, customerIDResult.getString(1));
								pre1.setInt(2, amount);
								pre1.setString(3, me);
								pre1.setDate(4, sqlDate);
								pre1.setString(5, khwayNumArr[i].concat(khwayNumArr[j]));
								pre1.setInt(6, 0);
								pre1.executeUpdate();

							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		} else if (playNumber.equals("***")) {

			String insertQuery = "{CALL insertForEachPlayNumber(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%d%d", i, i, i);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, formatedPlayNum);
						pre1.setInt(6, 0);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (playNumber.startsWith("*") && me.equals("3D")) {

			String secondNum = String.valueOf(playNumber.charAt(1));
			String thirdNum = String.valueOf(playNumber.charAt(2));
			String insertQuery = "{CALL insertForEachPlayNumber(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%s%s", i, secondNum, thirdNum);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, formatedPlayNum);
						pre1.setInt(6, 0);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (secondNumForNAN.equals("*")) {
			String firstNum = String.valueOf(playNumber.charAt(0));
			String thirdNum = String.valueOf(playNumber.charAt(2));
			String insertQuery = "{CALL insertForEachPlayNumber(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%d%s", firstNum, i, thirdNum);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, formatedPlayNum);
						pre1.setInt(6, 0);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (playNumber.endsWith("*") && me.equals("3D")) {

			String firstNum = String.valueOf(playNumber.charAt(0));
			String secondNum = String.valueOf(playNumber.charAt(1));
			String insertQuery = "{CALL insertForEachPlayNumber(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%s%d", firstNum, secondNum, i);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, formatedPlayNum);
						pre1.setInt(6, 0);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else {

			String insertQuery = "{CALL insertForEachPlayNumber(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, playNumber);
					pre1.setInt(6, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertDataIntoTwoDUserPlayForCurTable(String name, String playNumber, int amount, String me,
			LocalDate date) {

		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);
//		CALL insertDataForFormula("I-00001", 200, "Morning", 10-15-2021);

		if (playNumber.equals("ss")) {

			String insertQuery = "{CALL insertDataForSSForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mm")) {

			String insertQuery = "{CALL insertDataForMMForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sm")) {

			String insertQuery = "{CALL insertDataForSMForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("ms")) {

			String insertQuery = "{CALL insertDataForMSForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sp")) {

			String insertQuery = "{CALL insertDataForSPForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mp")) {

			String insertQuery = "{CALL insertDataForMPForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("**")) {

			String insertQuery = "{CALL insertDataForAAForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL insertDataForANForCurTableProcedure(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, String.valueOf(playNumber.charAt(1)));
					pre1.setInt(6, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL insertDataForNAForCurTableProcedure(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.setInt(6, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("b")) {

			String insertQuery = "{CALL insertDataForBForCurTableProcedure(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.setInt(6, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("n")) {

			String insertQuery = "{CALL insertDataForNForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("p")) {

			String insertQuery = "{CALL insertDataForPForCurTableProcedure(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setInt(5, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("k")) {
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL insertDataForKhwayFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, playNumWithoutK);
					pre1.setInt(6, 0);
					pre1.executeUpdate();

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
//			String playNumWithout1 = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL insertDataForEachNumForCurTableProcedure(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
//					if(playNumber.endsWith("1")) {
//						pre1.setString(5, playNumWithout1);
//					} else {
					pre1.setString(5, playNumber);
//					}
					pre1.setInt(6, 0);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertDataIntoTwoDUserPlayForCurTableForLast(String name, String playNumber, int amount, String me,
			LocalDate date) {

		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);
//		CALL insertDataForFormula("I-00001", 200, "Morning", 10-15-2021);

		String insertQuery = "INSERT INTO twod_user_play_for_cur_table_for_last VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";

		try (Connection con = DriverManager.getConnection(url, userName, password);
				PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
				CallableStatement pre1 = con.prepareCall(insertQuery);
				ResultSet customerIDResult = pre.executeQuery();) {

			if (customerIDResult.next()) {
				pre1.setString(1, customerIDResult.getString(1));
				pre1.setString(2, playNumber);
				pre1.setInt(3, amount);
				pre1.setString(4, me);
				pre1.setDate(5, sqlDate);
				pre1.setInt(6, 0);
				pre1.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteDataIntoTwoDUserPlay(String name, String playNumber, int amount, String me, LocalDate date,
			String allLast) {

		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);
//		CALL insertDataForFormula("I-00001", 200, "Morning", 10-15-2021);
		String secondNumForNAN = "";
		if (playNumber.length() == 3) {
			secondNumForNAN = String.valueOf(playNumber.charAt(1));
		}

		if (playNumber.equalsIgnoreCase("ss")) {

			String insertQuery = "{CALL deleteDataForSSFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mm")) {

			String insertQuery = "{CALL deleteDataForMMFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sm")) {

			String insertQuery = "{CALL deleteDataForSMFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("ms")) {

			String insertQuery = "{CALL deleteDataForMSFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sp")) {

			String insertQuery = "{CALL deleteDataForSPFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mp")) {

			String insertQuery = "{CALL deleteDataForMPFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("**")) {

			String insertQuery = "{CALL deleteDataForAAFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL deleteDataForANFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.setString(6, String.valueOf(playNumber.charAt(1)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL deleteDataForNAFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.setString(6, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("B")) {

			String firstNum = playNumber.charAt(0) + "";
			int a = 0;
			int k = Integer.parseInt("1" + firstNum);

			String insertQuery = "{CALL deleteDataForBFormula(?, ?, ?, ?, ?, ?)}";
			String insertQueryForEach = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					CallableStatement pre2 = con.prepareCall(insertQueryForEach);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.setString(6, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();

					pre2.setString(1, customerIDResult.getString(1));
					pre2.setString(2, me);
					pre2.setDate(3, sqlDate);
					pre2.setString(4, allLast);
					pre2.setInt(5, amount);
					while (a <= Integer.parseInt("1" + firstNum)) {
						if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
							pre2.setString(6, String.valueOf(a).concat(String.valueOf(k)));
							pre2.executeUpdate();
						}
						a++;
						k--;
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.length() == 2 && playNumber.endsWith("+")) {

			String firstNum = String.valueOf(playNumber.charAt(0));

			ArrayList<String> arrayListANNA = new ArrayList<>();
			arrayListANNA.clear();
			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%s", i, firstNum);
				arrayListANNA.add(formatedPlayNum);
			}
			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%d", firstNum, i);
				arrayListANNA.add(formatedPlayNum);
			}
			Set<String> set = new LinkedHashSet<>(arrayListANNA);
			arrayListANNA.clear();
			arrayListANNA.addAll(set);

			String insertQuery = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					PreparedStatement pre1 = con.prepareStatement(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					for (int i = 0; i < arrayListANNA.size(); i++) {
						pre1.setString(6, arrayListANNA.get(i));
						pre1.executeUpdate();
					}

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (playNumber.equalsIgnoreCase("n")) {

			String insertQuery = "{CALL deleteDataForNFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("p")) {

			String insertQuery = "{CALL deleteDataForPFormula(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("k")) {
			System.out.println(playNumber.substring(0, playNumber.length() - 1) + " without k in delete");
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			if (playNumWithoutK.endsWith("*")) {
				String[] khwayNumArr = playNumWithoutK.split("");
				for (int i = 0; i < khwayNumArr.length - 1; i++) {
					for (int j = 0; j < khwayNumArr.length - 1; j++) {

						try (Connection con = DriverManager.getConnection(url, userName, password);
								PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
								CallableStatement pre1 = con.prepareCall(insertQuery);
								ResultSet customerIDResult = pre.executeQuery();) {

							if (customerIDResult.next()) {
								pre1.setString(1, customerIDResult.getString(1));
								pre1.setString(2, me);
								pre1.setDate(3, sqlDate);
								pre1.setString(4, allLast);
								pre1.setInt(5, amount);
								pre1.setString(6, khwayNumArr[i].concat(khwayNumArr[j]));
								pre1.executeUpdate();

							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				String[] khwayNumArr = playNumWithoutK.split("");
				for (int i = 0; i < khwayNumArr.length; i++) {
					for (int j = 0; j < khwayNumArr.length; j++) {
						if (i == j) {
							continue;
						}
						try (Connection con = DriverManager.getConnection(url, userName, password);
								PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
								CallableStatement pre1 = con.prepareCall(insertQuery);
								ResultSet customerIDResult = pre.executeQuery();) {

							if (customerIDResult.next()) {
								pre1.setString(1, customerIDResult.getString(1));
								pre1.setString(2, me);
								pre1.setDate(3, sqlDate);
								pre1.setString(4, allLast);
								pre1.setInt(5, amount);
								pre1.setString(6, khwayNumArr[i].concat(khwayNumArr[j]));
								pre1.executeUpdate();

							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		} else if (playNumber.equals("***")) {

			String insertQuery = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%d%d", i, i, i);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setString(2, me);
						pre1.setDate(3, sqlDate);
						pre1.setString(4, allLast);
						pre1.setInt(5, amount);
						pre1.setString(6, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (playNumber.startsWith("*") && me.equals("3D")) {

			String secondNum = String.valueOf(playNumber.charAt(1));
			String thirdNum = String.valueOf(playNumber.charAt(2));
			String insertQuery = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%s%s", i, secondNum, thirdNum);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setString(2, me);
						pre1.setDate(3, sqlDate);
						pre1.setString(4, allLast);
						pre1.setInt(5, amount);
						pre1.setString(6, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (secondNumForNAN.equals("*")) {
			String firstNum = String.valueOf(playNumber.charAt(0));
			String thirdNum = String.valueOf(playNumber.charAt(2));
			String insertQuery = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%d%s", firstNum, i, thirdNum);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setString(2, me);
						pre1.setDate(3, sqlDate);
						pre1.setString(4, allLast);
						pre1.setInt(5, amount);
						pre1.setString(6, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (playNumber.endsWith("*") && me.equals("3D")) {

			String firstNum = String.valueOf(playNumber.charAt(0));
			String secondNum = String.valueOf(playNumber.charAt(1));
			String insertQuery = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%s%d", firstNum, secondNum, i);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setString(2, me);
						pre1.setDate(3, sqlDate);
						pre1.setString(4, allLast);
						pre1.setInt(5, amount);
						pre1.setString(6, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else {

			String insertQuery = "{CALL deleteDataForEachNumberFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setString(4, allLast);
					pre1.setInt(5, amount);
					pre1.setString(6, playNumber);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteDataIntoTwoDUserPlayForCurTable(String name, String playNumber, int amount, String me,
			LocalDate date, String allLast) {

		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);
//		CALL insertDataForFormula("I-00001", 200, "Morning", 10-15-2021);

		System.out.println(amount);

		if (playNumber.equalsIgnoreCase("ss")) {

			String insertQuery = "{CALL deleteDataForSSFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mm")) {

			String insertQuery = "{CALL deleteDataForMMFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sm")) {

			String insertQuery = "{CALL deleteDataForSMFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("ms")) {

			String insertQuery = "{CALL deleteDataForMSFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sp")) {

			String insertQuery = "{CALL deleteDataForSPFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mp")) {

			String insertQuery = "{CALL deleteDataForMPFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("**")) {

			String insertQuery = "{CALL deleteDataForAAFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*")) {

			String insertQuery = "{CALL deleteDataForANFormulaForCurTable(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, String.valueOf(playNumber.charAt(1)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL deleteDataForNAFormulaForCurTable(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("b")) {

			String insertQuery = "{CALL deleteDataForBFormulaForCurTable(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("N")) {

			String insertQuery = "{CALL deleteDataForNFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("p")) {

			String insertQuery = "{CALL deleteDataForPFormulaForCurTable(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("k")) {
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL deleteDataForEachNumberForCurTable(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, playNumWithoutK);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String insertQuery = "{CALL deleteDataForEachNumberForCurTable(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, playNumber);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteDataIntoTwoDUserPlayForCurTableForLastForR(String name, String playNumber, int amount, String me,
			LocalDate date, String allLast, int index) {
		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);

		String insertQuery = "{CALL deleteDataForRForCurTableForLast(?, ?, ?, ?, ?)}";

		try (Connection con = DriverManager.getConnection(url, userName, password);
				PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
				CallableStatement pre1 = con.prepareCall(insertQuery);
				ResultSet customerIDResult = pre.executeQuery();) {

			if (customerIDResult.next()) {
				pre1.setString(1, customerIDResult.getString(1));
				pre1.setString(2, me);
				pre1.setDate(3, sqlDate);
				pre1.setInt(4, amount);
				pre1.setInt(5, index);
				pre1.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteDataIntoTwoDUserPlayForCurTableForLast(String name, String playNumber, int amount, String me,
			LocalDate date, String allLast) {

		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);
//		CALL insertDataForFormula("I-00001", 200, "Morning", 10-15-2021);

		System.out.println(amount);

		if (playNumber.equals("ss")) {

			String insertQuery = "{CALL deleteDataForSSFormulaForCurTableForLast(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("mm")) {

			String insertQuery = "{CALL deleteDataForMMFormulaForCurTableForLast(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("sm")) {

			String insertQuery = "{CALL deleteDataForSMFormulaForCurTableForLast(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("ms")) {

			String insertQuery = "{CALL deleteDataForMSFormulaForCurTableForLast(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("sp")) {

			String insertQuery = "{CALL deleteDataForSPFormulaForCurTableForLast(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("mp")) {

			String insertQuery = "{CALL deleteDataForMPFormulaForCurTableForLast(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equals("**")) {

			String insertQuery = "{CALL deleteDataForAAFormulaForCurTableForLast(?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*")) {

			String insertQuery = "{CALL deleteDataForANFormulaForCurTableForLast(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, String.valueOf(playNumber.charAt(1)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {

			String insertQuery = "{CALL deleteDataForNAFormulaForCurTableForLast(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("b")) {

			String insertQuery = "{CALL deleteDataForBFormulaForCurTableForLast(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("k")) {
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL deleteDataForEachPlayNumberForCurTableForLast(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, playNumWithoutK);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String insertQuery = "{CALL deleteDataForEachPlayNumberForCurTableForLast(?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setString(2, me);
					pre1.setDate(3, sqlDate);
					pre1.setInt(4, amount);
					pre1.setString(5, playNumber);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateDataIntoTwoDUserPlayTable(String name, String playNumber, int amount, String me, LocalDate date,
			String allLast, int toExtractFromTotal) {
//		UPDATE twoD_user_play SET amount = 3600 WHERE play_number = '99' AND customer_id = 'I-00001' AND date = '2021-10-18' AND me = 'Morning';
		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);
		String secondNumForNAN = "";
		if (playNumber.length() == 3) {
			secondNumForNAN = String.valueOf(playNumber.charAt(1));
		}

		if (playNumber.equalsIgnoreCase("ss")) {
			System.out.println(amount + " from queries");
			String insertQuery = "{CALL updateDataForSSFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (playNumber.equalsIgnoreCase("mm")) {
			System.out.println(amount + " from queries");
			String insertQuery = "{CALL updateDataForMMFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sm")) {
			String insertQuery = "{CALL updateDataForSMFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("ms")) {
			String insertQuery = "{CALL updateDataForMSFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sp")) {
			String insertQuery = "{CALL updateDataForSPFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mp")) {
			String insertQuery = "{CALL updateDataForMPFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("**")) {
			String insertQuery = "{CALL updateDataForAAFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*") && !me.equals("3D")) {
			String insertQuery = "{CALL updateDataForANFormula(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(1)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {
			String insertQuery = "{CALL updateDataForNAFormula(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		else if (playNumber.endsWith("B")) {
			String firstNum = playNumber.charAt(0) + "";
			int a = 0;
			int k = Integer.parseInt("1" + firstNum);
			String insertQuery = "{CALL updateDataForBFormula(?, ?, ?, ?, ?, ?, ?)}";
			String insertQueryForEach = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)};";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					CallableStatement pre2 = con.prepareCall(insertQueryForEach);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();

					pre2.setString(1, customerIDResult.getString(1));
					pre2.setInt(2, amount);
					pre2.setString(3, me);
					pre2.setDate(4, sqlDate);
					pre2.setString(5, allLast);
					pre2.setInt(6, toExtractFromTotal);
					while (a <= Integer.parseInt("1" + firstNum)) {
						if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
							pre2.setString(7, String.valueOf(a).concat(String.valueOf(k)));
							pre2.executeUpdate();
						}
						a++;
						k--;
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		else if (playNumber.length() == 2 && playNumber.endsWith("+")) {
			String firstNum = String.valueOf(playNumber.charAt(0));

			ArrayList<String> arrayListANNA = new ArrayList<>();
			arrayListANNA.clear();
			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%s", i, firstNum);
				arrayListANNA.add(formatedPlayNum);
			}
			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%d", firstNum, i);
				arrayListANNA.add(formatedPlayNum);
			}
			Set<String> set = new LinkedHashSet<>(arrayListANNA);
			arrayListANNA.clear();
			arrayListANNA.addAll(set);

			String insertQuery = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)};";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					PreparedStatement pre1 = con.prepareStatement(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					for (int i = 0; i < arrayListANNA.size(); i++) {
						pre1.setString(7, arrayListANNA.get(i));
						pre1.executeUpdate();
					}

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (playNumber.equalsIgnoreCase("n")) {
			String insertQuery = "{CALL updateDataForNFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("p")) {
			String insertQuery = "{CALL updateDataForPFormula(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("k")) {
			System.out.println(playNumber.substring(0, playNumber.length() - 1) + " without k");
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)}";

			if (playNumWithoutK.endsWith("*")) {
				String[] khwayNumArr = playNumWithoutK.split("");
				for (int i = 0; i < khwayNumArr.length - 1; i++) {
					for (int j = 0; j < khwayNumArr.length - 1; j++) {

						try (Connection con = DriverManager.getConnection(url, userName, password);
								PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
								CallableStatement pre1 = con.prepareCall(insertQuery);
								ResultSet customerIDResult = pre.executeQuery();) {

							if (customerIDResult.next()) {
								pre1.setString(1, customerIDResult.getString(1));
								pre1.setInt(2, amount);
								pre1.setString(3, me);
								pre1.setDate(4, sqlDate);
								pre1.setString(5, allLast);
								pre1.setInt(6, toExtractFromTotal);
								pre1.setString(7, khwayNumArr[i].concat(khwayNumArr[j]));
								pre1.executeUpdate();

							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				String[] khwayNumArr = playNumWithoutK.split("");
				for (int i = 0; i < khwayNumArr.length; i++) {
					for (int j = 0; j < khwayNumArr.length; j++) {
						if (i == j) {
							continue;
						}
						try (Connection con = DriverManager.getConnection(url, userName, password);
								PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
								CallableStatement pre1 = con.prepareCall(insertQuery);
								ResultSet customerIDResult = pre.executeQuery();) {

							if (customerIDResult.next()) {
								pre1.setString(1, customerIDResult.getString(1));
								pre1.setInt(2, amount);
								pre1.setString(3, me);
								pre1.setDate(4, sqlDate);
								pre1.setString(5, allLast);
								pre1.setInt(6, toExtractFromTotal);
								pre1.setString(7, khwayNumArr[i].concat(khwayNumArr[j]));
								pre1.executeUpdate();

							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		} else if (playNumber.equals("***")) {

			String insertQuery = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)};";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%d%d", i, i, i);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, allLast);
						pre1.setInt(6, toExtractFromTotal);
						pre1.setString(7, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (playNumber.startsWith("*") && me.equals("3D")) {

			String secondNum = String.valueOf(playNumber.charAt(1));
			String thirdNum = String.valueOf(playNumber.charAt(2));
			String insertQuery = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)};";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%d%s%s", i, secondNum, thirdNum);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, allLast);
						pre1.setInt(6, toExtractFromTotal);
						pre1.setString(7, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (secondNumForNAN.equals("*")) {
			String firstNum = String.valueOf(playNumber.charAt(0));
			String thirdNum = String.valueOf(playNumber.charAt(2));
			String insertQuery = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)};";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%d%s", firstNum, i, thirdNum);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, allLast);
						pre1.setInt(6, toExtractFromTotal);
						pre1.setString(7, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (playNumber.endsWith("*") && me.equals("3D")) {

			String firstNum = String.valueOf(playNumber.charAt(0));
			String secondNum = String.valueOf(playNumber.charAt(1));
			String insertQuery = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)};";

			for (int i = 0; i < 10; i++) {
				String formatedPlayNum = String.format("%s%s%d", firstNum, secondNum, i);

				try (Connection con = DriverManager.getConnection(url, userName, password);
						PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
						CallableStatement pre1 = con.prepareCall(insertQuery);
						ResultSet customerIDResult = pre.executeQuery();) {

					if (customerIDResult.next()) {
						pre1.setString(1, customerIDResult.getString(1));
						pre1.setInt(2, amount);
						pre1.setString(3, me);
						pre1.setDate(4, sqlDate);
						pre1.setString(5, allLast);
						pre1.setInt(6, toExtractFromTotal);
						pre1.setString(7, formatedPlayNum);
						pre1.executeUpdate();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else {

			String insertQuery = "{CALL updateForEachPlayNumber(?, ?, ?, ?, ?, ?, ?)};";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					PreparedStatement pre1 = con.prepareStatement(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, playNumber);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void updateDataIntoTwoDUserPlayTableForCurTable(String name, String playNumber, int amount, String me,
			LocalDate date, String allLast, int toExtractFromTotal) {
//		UPDATE twoD_user_play SET amount = 3600 WHERE play_number = '99' AND customer_id = 'I-00001' AND date = '2021-10-18' AND me = 'Morning';
		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);

		if (playNumber.equalsIgnoreCase("ss")) {
			System.out.println(amount + " from queries");
			String insertQuery = "{CALL updateDataForSSFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (playNumber.equalsIgnoreCase("mm")) {
			System.out.println(amount + " from queries");
			String insertQuery = "{CALL updateDataForMMFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sm")) {
			String insertQuery = "{CALL updateDataForSMFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("ms")) {
			String insertQuery = "{CALL updateDataForMSFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sp")) {
			String insertQuery = "{CALL updateDataForSPFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mp")) {
			String insertQuery = "{CALL updateDataForMPFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("**")) {
			String insertQuery = "{CALL updateDataForAAFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*") && !me.equals("3D")) {
			String insertQuery = "{CALL updateDataForANFormulaForCurTable(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(1)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {
			String insertQuery = "{CALL updateDataForNAFormulaForCurTable(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("b")) {
			String insertQuery = "{CALL updateDataForBFormulaForCurTable(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("n")) {
			String insertQuery = "{CALL updateDataForNFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("p")) {
			String insertQuery = "{CALL updateDataForPFormulaForCurTable(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL updateForEachPlayNumberForCurTable(?, ?, ?, ?, ?, ?, ?)};";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					PreparedStatement pre1 = con.prepareStatement(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					if (playNumber.endsWith("k")) {
						pre1.setString(7, playNumWithoutK);
					} else {
						pre1.setString(7, playNumber);
					}
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void updateDataIntoTwoDUserPlayTableForCurTableForLastForR(String name, String playNumber, int amount,
			String me, LocalDate date, String allLast, int toExtractFromTotal, int index) {
		String insertQuery = "{CALL updateForRForCurTableForLast(?, ?, ?, ?, ?, ?, ?, ?)};";
		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);

		try (Connection con = DriverManager.getConnection(url, userName, password);
				PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
				PreparedStatement pre1 = con.prepareStatement(insertQuery);
				ResultSet customerIDResult = pre.executeQuery();) {

			if (customerIDResult.next()) {
				pre1.setString(1, customerIDResult.getString(1));
				pre1.setInt(2, amount);
				pre1.setString(3, me);
				pre1.setDate(4, sqlDate);
				pre1.setString(5, allLast);
				pre1.setInt(6, toExtractFromTotal);
				pre1.setString(7, playNumber);
				pre1.setInt(8, index);
				pre1.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateDataIntoTwoDUserPlayTableForCurTableForLast(String name, String playNumber, int amount, String me,
			LocalDate date, String allLast, int toExtractFromTotal) {
//		UPDATE twoD_user_play SET amount = 3600 WHERE play_number = '99' AND customer_id = 'I-00001' AND date = '2021-10-18' AND me = 'Morning';
//		SELECT t.amount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number
//		= 'sp' AND t.customer_id = 'I-00001' AND t.date = '2021-11-02' AND t.me = 'Morning' AND amount = 2000 LIMIT 1;
		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
		Date sqlDate = Date.valueOf(date);

		if (playNumber.equalsIgnoreCase("ss")) {
			System.out.println(amount + " from queries");
			String insertQuery = "{CALL updateDataForSSFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				while (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}
//				pre1.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (playNumber.equalsIgnoreCase("mm")) {
			System.out.println(amount + " from queries");
			String insertQuery = "{CALL updateDataForMMFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sm")) {
			String insertQuery = "{CALL updateDataForSMFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("ms")) {
			String insertQuery = "{CALL updateDataForMSFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("sp")) {
			String insertQuery = "{CALL updateDataForSPFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("mp")) {
			String insertQuery = "{CALL updateDataForMPFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("**")) {
			String insertQuery = "{CALL updateDataForAAFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.startsWith("*") && !playNumber.endsWith("*") && !me.equals("3D")) {
			String insertQuery = "{CALL updateDataForANFormulaForCurTableForLast(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(1)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (!playNumber.startsWith("*") && playNumber.endsWith("*") && !me.equals("3D")) {
			String insertQuery = "{CALL updateDataForNAFormulaForCurTableForLast(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.endsWith("b")) {
			String insertQuery = "{CALL updateDataForBFormulaForCurTableForLast(?, ?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.setString(7, String.valueOf(playNumber.charAt(0)));
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("n")) {
			String insertQuery = "{CALL updateDataForNFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (playNumber.equalsIgnoreCase("p")) {
			String insertQuery = "{CALL updateDataForPFormulaForCurTableForLast(?, ?, ?, ?, ?, ?)}";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					CallableStatement pre1 = con.prepareCall(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String playNumWithoutK = playNumber.substring(0, playNumber.length() - 1);
			String insertQuery = "{CALL updateForEachPlayNumberForCurTableForLast(?, ?, ?, ?, ?, ?, ?)};";

			try (Connection con = DriverManager.getConnection(url, userName, password);
					PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
					PreparedStatement pre1 = con.prepareStatement(insertQuery);
					ResultSet customerIDResult = pre.executeQuery();) {

				if (customerIDResult.next()) {
					pre1.setString(1, customerIDResult.getString(1));
					pre1.setInt(2, amount);
					pre1.setString(3, me);
					pre1.setDate(4, sqlDate);
					pre1.setString(5, allLast);
					pre1.setInt(6, toExtractFromTotal);
					if (playNumber.endsWith("k")) {
						pre1.setString(7, playNumWithoutK);
					} else {
						pre1.setString(7, playNumber);
					}
					pre1.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public int toShowAmount(int checkNum, LocalDate date) {

		String totalAmountForSpecificNum = String.format(
				"SELECT SUM(amount) FROM twod_user_play as t WHERE t.play_number = %d AND t.date = '%s';", checkNum,
				date);

		try (Connection con = createConnection();
				PreparedStatement pre = con.prepareStatement(totalAmountForSpecificNum);
				ResultSet rs = pre.executeQuery();) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public ObservableList<ObjectForCurrentTable> extractDataForAllOption(String name, LocalDate date, String me) {
		Date sqlDate = Date.valueOf(date);
		String insertQuery = String.format(
				"SELECT play_number, amount FROM twod_user_play_for_cur_table as t INNER JOIN customer as c ON t.customer_id = c.customer_id WHERE name = '%s' AND t.date = '%s' AND me = '%s';",
				name, String.valueOf(sqlDate), me);
		ObservableList<ObjectForCurrentTable> list = FXCollections.observableArrayList();

		try (Connection con = DriverManager.getConnection(url, userName, password);
				PreparedStatement pre = con.prepareStatement(insertQuery);
				ResultSet rs = pre.executeQuery();) {

			while (rs.next()) {
				list.add(new ObjectForCurrentTable(rs.getString(1), rs.getInt(2)));
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public ObservableList<ObjectForCurrentTable> extractDataForLastOption(String name, LocalDate date, String me) {
		Date sqlDate = Date.valueOf(date);
		String insertQuery = String.format(
				"SELECT play_number, amount FROM twod_user_play_for_cur_table_for_last as t INNER JOIN customer as c ON t.customer_id = c.customer_id WHERE name = '%s' AND t.date = '%s' AND me = '%s';",
				name, String.valueOf(sqlDate), me);
		ObservableList<ObjectForCurrentTable> list = FXCollections.observableArrayList();

		try (Connection con = DriverManager.getConnection(url, userName, "123#asdFF");
				PreparedStatement pre = con.prepareStatement(insertQuery);
				ResultSet rs = pre.executeQuery();) {

			while (rs.next()) {
				list.add(new ObjectForCurrentTable(rs.getString(1), rs.getInt(2)));
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void deleteToTwoDUserPlayForCurTableForLast() {
//		System.out.println("Delete into table before");
		String deleteQuery = "DELETE FROM twod_user_play_for_cur_table_for_last";

		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(deleteQuery);) {

			pre.executeUpdate();
			System.out.println("Delete into table After");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getTotalCustomerNumber() {
		int count = 0;
		String sql = "SELECT COUNT(*) as TotalCustomerNumber FROM customer;";
		try (Connection con = createConnection();
				PreparedStatement pre = con.prepareStatement(sql);
				ResultSet rs = pre.executeQuery();) {
			while (rs.next()) {
				count = rs.getInt("TotalCustomerNumber");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

//	public void insertKhwayIntoForLastTable(String name, String playNumber, int amount, String me, LocalDate date) {
//
//		String extractSpecificCustomerID = String.format("SELECT customer_id FROM customer WHERE name = '%s'", name);
//		Date sqlDate = Date.valueOf(date);
////		CALL insertDataForFormula("I-00001", 200, "Morning", 10-15-2021);
//
//		String insertQuery = "INSERT INTO twod_user_play_for_cur_table_for_last VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";
//
//		try (Connection con = DriverManager.getConnection(url, userName, "");
//				PreparedStatement pre = con.prepareStatement(extractSpecificCustomerID);
//				CallableStatement pre1 = con.prepareCall(insertQuery);
//				ResultSet customerIDResult = pre.executeQuery();) {
//
//			if (customerIDResult.next()) {
//				pre1.setString(1, customerIDResult.getString(1));
//				pre1.setInt(2, amount);
//				pre1.setString(3, me);
//				pre1.setDate(4, sqlDate);
//				pre1.setString(5, playNumber);
//				pre1.setInt(6, 0);
//				pre1.executeUpdate();
//
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

//	public static void main(String[] args) {
//		QueriesForTowDMorning twoD = new QueriesForTowDMorning();
//		twoD.insertKhwayIntoForLastTable("Hla Aung", "123*", 1, "Morning", LocalDate.now());
//	}

}
