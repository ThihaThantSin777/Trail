/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database2d3d;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class CreateDatabaseAndTable {

	private static final String url = "jdbc:mysql://localhost/";
	private static final String username = "root";
	private static final String password = "123#asdFF";
	private static final String urlForProcedure = "jdbc:mysql://localhost/2d3d";

	public static boolean check() {
		String sql = "SHOW DATABASES";
		try (ResultSet rs = getResultSet(sql)) {
			while (rs.next()) {
				String r = rs.getString(1);
				if (r.equals("2d3d")) {
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public static PreparedStatement getPrepareStatement(String sql) throws Exception {
		return getConnection(url).prepareStatement(sql);
	}

	public static ResultSet getResultSet(String sql) throws Exception {
		return getPrepareStatement(sql).executeQuery();
	}

	public static Connection getConnection(String url) throws Exception {
		return DriverManager.getConnection(url, username, password);
	}

	public static Connection createConnection() {
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection createConnectionForProcedure() {
		try {
			return DriverManager.getConnection(urlForProcedure, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void createDatabase() {
		String sql = "CREATE DATABASE 2d3d";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
		}
	}

	private static String generateID(boolean in) {
		String sql = "SELECT customer_id FROM customer WHERE customer_id LIKE ? ORDER BY customer_id DESC LIMIT 1";
		int id = 1;
		try (Connection con = createConnectionCustomer(); PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setString(1, in ? "I%" : "O%");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String array[] = rs.getString(1).split("-");
				id = Integer.parseInt(array[1]) + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String inorOut = in ? "I-" : "O-";
		return String.format("%s%05d", inorOut, id);
	}

	public static Connection createConnectionCustomer() {

		try {
			String url = "jdbc:mysql://localhost/2d3d";
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean insertCustomerName(String name, boolean in, LocalDate date) {
		if (name.isEmpty()) {
			// throw new TwoDThreeDException("PLease enter Agent name");
		} else {
			String getID = "";
			String getIDSQL = "SELECT customer_id FROM customer WHERE customer_id LIKE ? AND name=?";
			try (Connection con = createConnectionCustomer(); PreparedStatement stmt = con.prepareStatement(getIDSQL)) {
				stmt.setString(1, in ? "I%" : "O%");
				stmt.setString(2, name);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					getID = rs.getString(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!getID.isEmpty()) {
				// throw new TwoDThreeDException("Agent " + name + " is already register");
			} else {
				String getCID = generateID(in);
				String sql = "INSERT INTO `customer`(`customer_id`, `name`, `date`) VALUES (?,?,?)";
				try (Connection con = createConnectionCustomer(); PreparedStatement stmt = con.prepareStatement(sql)) {
					stmt.setString(1, getCID);
					stmt.setString(2, name);
					stmt.setDate(3, Date.valueOf(date));
					stmt.executeUpdate();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;

	}

	public static void createsetAmtTable() {
		String sql = "CREATE TABLE `2d3d`.`setamount` ( `setamt_id` INT NOT NULL AUTO_INCREMENT , `setamount` INT NOT NULL , PRIMARY KEY (`setamt_id`));";
		String sqlinsert = "\r\n" + "INSERT INTO 2D3D.setamount VALUES (DEFAULT, 10000);";
		try (Connection con = createConnection();
				PreparedStatement pre = con.prepareStatement(sql);
				PreparedStatement pre1 = con.prepareStatement(sqlinsert);) {
			pre.execute();
			pre1.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createCustomerTable() {
		String sql = "CREATE TABLE `2d3d`.`customer` "
				+ "( `customer_id` VARCHAR(15) NOT NULL , `name` VARCHAR(50) NOT NULL , `date` DATE NOT NULL, PRIMARY KEY (`customer_id`));";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void create2DUserPlayTable() {
		String sql = "CREATE TABLE `2d3d`.`twoD_user_play`(twoD_user_play_id  INT "
				+ "PRIMARY KEY NOT NULL AUTO_INCREMENT, customer_id VARCHAR(15) NOT NULL, play_number INT NOT NULL, "
				+ "amount INT NOT NULL DEFAULT 0,  me VARCHAR(15) NOT NULL, date date not null , extract_amount INT NOT NULL, FOREIGN KEY (`customer_id`) "
				+ "REFERENCES `customer`(`customer_id`) ON UPDATE CASCADE ON DELETE CASCADE);";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void create2DUserPlayTableForCurTable() {
		String sql = "CREATE TABLE `2d3d`.`twoD_user_play_for_cur_table`(twoD_user_play_id  INT "
				+ "PRIMARY KEY NOT NULL AUTO_INCREMENT, customer_id VARCHAR(15) NOT NULL, play_number VARCHAR(15) NOT NULL, "
				+ "amount INT NOT NULL DEFAULT 0,  me VARCHAR(15) NOT NULL, date date not null , extract_amount INT NOT NULL, FOREIGN KEY (`customer_id`) "
				+ "REFERENCES `customer`(`customer_id`) ON UPDATE CASCADE ON DELETE CASCADE);";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void create2DUserPlayTableForCurTableForLast() {
		String sql = "CREATE TABLE `2d3d`.`twoD_user_play_for_cur_table_for_last`(twoD_user_play_id  INT "
				+ "PRIMARY KEY NOT NULL AUTO_INCREMENT, customer_id VARCHAR(15) NOT NULL, play_number VARCHAR(15) NOT NULL, "
				+ "amount INT NOT NULL DEFAULT 0,  me VARCHAR(15) NOT NULL, date date not null , extract_amount INT NOT NULL, FOREIGN KEY (`customer_id`) "
				+ "REFERENCES `customer`(`customer_id`) ON UPDATE CASCADE ON DELETE CASCADE);";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void create3DUserPlayTable() {
		String sql = "CREATE TABLE `2d3d`.`threeD_user_play`(threeD_user_play_id INT "
				+ "PRIMARY KEY NOT NULL AUTO_INCREMENT, customer_id VARCHAR(15) NOT NULL, play_number INT NOT NULL, "
				+ "amount INT NOT NULL DEFAULT 0, date date not null ,FOREIGN KEY (`customer_id`) "
				+ "REFERENCES `customer`(`customer_id`) ON UPDATE CASCADE ON DELETE CASCADE);";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createExceedingTable() {
		String sql = "CREATE TABLE `2d3d`.`exceeding` ( `exceeding_id` INT NOT NULL AUTO_INCREMENT , "
				+ "`exceeded_number` INT NOT NULL , `exceeded_amount` INT NOT NULL , `date` DATE NOT NULL, `me` VARCHAR(20) NOT NULL, `is_noted` TINYINT(1) NOT NULL , PRIMARY KEY (`exceeding_id`));";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createOutsourceTable() {
		String sql = "CREATE TABLE `2d3d`.`outsource` ( `outsource_id` INT NOT NULL AUTO_INCREMENT , "
				+ "`customer_id` VARCHAR(15) NOT NULL,  `exceeding_id` INT NOT NULL,`currentAmount` INT NOT NULL, PRIMARY KEY (`outsource_id`), "
				+ "FOREIGN KEY(`customer_id`) REFERENCES `customer`(`customer_id`) ON UPDATE CASCADE ON DELETE CASCADE, "
				+ "FOREIGN KEY(`exceeding_id`) REFERENCES `exceeding`(`exceeding_id`) ON UPDATE CASCADE ON DELETE CASCADE );";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createWinNumTable() {
		String sql = "CREATE TABLE `2d3d`.`win_num` ( `win_num_id` INT NOT NULL AUTO_INCREMENT , `win_number` INT NOT NULL , `day_of_week` VARCHAR(15) NOT NULL , `date` DATE NOT NULL ,`me` VARCHAR(20) NOT NULL, PRIMARY KEY (`win_num_id`));";
		try (Connection con = createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertSSFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForSSFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " WHILE i < 89 DO\r\n" + "\r\n" + "  IF i < 10 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 10 THEN SET i = 20;\r\n"
				+ "  END IF;\r\n" + "  \r\n" + "  IF i > 19 AND i < 29 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 30 THEN SET i = 40;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 39 AND i < 49 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 50 THEN SET i = 60;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 59 AND i < 69 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 70 THEN SET i = 80;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 79 AND i < 89 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 2;\r\n"
				+ " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); Statement pre = con.createStatement();) {
			pre.execute(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertMMFormulaProcedure() {
		String sql = "CREATE DEFINER=`root`@`localhost` PROCEDURE `insertDataForMMFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 11;\r\n"
				+ "\r\n" + " WHILE i < 100 DO\r\n" + "\r\n" + "  IF i < 20 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 21 THEN SET i = 31;\r\n"
				+ "  END IF;\r\n" + "  \r\n" + "  IF i > 30 AND i < 40 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 41 THEN SET i = 51;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 50 AND i < 60 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 61 THEN SET i = 71;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 70 AND i < 80 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 81 THEN SET i = 91;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 90 AND i < 100 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 2;\r\n"
				+ " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertSMFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForSMFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 1;\r\n" + "\r\n"
				+ " WHILE i < 90 DO\r\n" + "\r\n" + "  IF i < 10 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 11 THEN SET i = 21;\r\n"
				+ "  END IF;\r\n" + "  \r\n" + "  IF i > 20 AND i < 30 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 31 THEN SET i = 41;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 40 AND i < 50 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 51 THEN SET i = 61;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 60 AND i < 70 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 71 THEN SET i = 81;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 80 AND i < 90 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 2;\r\n"
				+ " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertMSFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForMSFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 10;\r\n"
				+ "\r\n" + " WHILE i < 99 DO\r\n" + "\r\n" + "  IF i < 19 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 20 THEN SET i = 30;\r\n"
				+ "  END IF;\r\n" + "  \r\n" + "  IF i > 29 AND i < 39 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 40 THEN SET i = 50;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 49 AND i < 59 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 60 THEN SET i = 70;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 69 AND i < 79 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + "  IF i = 80 THEN SET i = 90;\r\n"
				+ "  END IF;\r\n" + "\r\n" + "  IF i > 89 AND i < 99 THEN\r\n" + "\r\n"
				+ "   IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 2;\r\n"
				+ " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertSPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForSPFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " WHILE i < 89 DO\r\n" + "\r\n"
				+ "  IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  ELSE\r\n" + "\r\n"
				+ "   INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 22;\r\n" + " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertMPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForMPFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 11;\r\n"
				+ "\r\n" + " WHILE i < 100 DO\r\n" + "\r\n"
				+ "  IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  ELSE\r\n" + "\r\n"
				+ "   INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 22;\r\n" + " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertAAFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForAAFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " WHILE i < 100 DO\r\n" + "\r\n"
				+ "  IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  ELSE\r\n" + "\r\n"
				+ "   INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 11;\r\n" + " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertANFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForANFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `second_num` INT, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " WHILE i < 100 DO\r\n" + "\r\n"
				+ "  IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i + second_num AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i + second_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i + second_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  ELSE\r\n" + "\r\n"
				+ "   INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i + second_num, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 10;\r\n" + " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertNAFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForNAFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `first_num` INT, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n"
				+ " SET i = first_num * 10;\r\n" + "\r\n" + " WHILE i < (first_num + 1) * 10 DO\r\n" + "\r\n"
				+ "  IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  ELSE\r\n" + "\r\n"
				+ "   INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, i, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 1;\r\n" + " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertBFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForBFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `first_num` INT, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE j INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n"
				+ " SET i = 0;\r\n" + " SET j = first_num;\r\n" + "\r\n" + " WHILE i <= first_num DO\r\n" + "\r\n"
				+ "  IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = CONCAT(i, j) AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = CONCAT(i, j) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = CONCAT(i, j) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  ELSE\r\n" + "\r\n"
				+ "   INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, CONCAT(i, j), amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "  END IF;\r\n" + "\r\n" + " SET i = i + 1;\r\n" + " SET j = j - 1;\r\n"
				+ " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertNFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForNFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 07 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 07 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 07 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 07, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 18 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 18 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 18 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 18, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 24 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 24 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 24 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 24, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 35 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 35 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 35 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 35, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 69 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 69 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 69 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 69, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 96 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 96 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 96 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 96, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 53 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 53 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 53 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 53, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 42 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 42 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 42 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 42, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 81 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 81 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 81 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 81, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 70 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 70 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 70 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 70, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForPFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 05 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 05 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 05 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 05, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 16 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 16 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 16 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 16, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 27 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 27 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 27 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 27, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 38 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 38 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 38 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 38, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 49 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 49 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 49 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 49, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 50 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 50 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 50 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 50, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 61 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 61 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 61 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 61, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 72 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 72 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 72 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 72, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 83 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 83 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 83 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 83, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = 94 AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 94 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "  UPDATE twod_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 94 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, 94, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertKhwayFormulaProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForKhwayFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `play_num` INT, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, play_num, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertForEachPlayNumberProcedure() {
		String sql = "CREATE PROCEDURE `insertForEachPlayNumber`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `play_num` INT, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play VALUES (DEFAULT, customer_id, play_num, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateSSFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForSSFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 89 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 10 THEN SET i = 20;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 19 AND i < 29 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 30 THEN SET i = 40;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 39 AND i < 49 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 50 THEN SET i = 60;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 59 AND i < 69 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 70 THEN SET i = 80;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 79 AND i < 89 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " ELSE\r\n" + "\r\n" + "  WHILE i < 89 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 10 THEN SET i = 20;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 19 AND i < 29 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 30 THEN SET i = 40;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 39 AND i < 49 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 50 THEN SET i = 60;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 59 AND i < 69 THEN\r\n" + "   \r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 70 THEN SET i = 80;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 79 AND i < 89 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateMMFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForMMFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 11;\r\n"
				+ "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   IF i < 20 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 21 THEN SET i = 31;\r\n" + "   END IF;\r\n" + "  \r\n"
				+ "   IF i > 30 AND i < 40 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 41 THEN SET i = 51;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 50 AND i < 60 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 61 THEN SET i = 71;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 70 AND i < 80 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 81 THEN SET i = 91;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 90 AND i < 100 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + " SET i = i + 2;\r\n" + " END WHILE;\r\n" + "\r\n" + " ELSE\r\n"
				+ "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n" + "   IF i < 20 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 21 THEN SET i = 31;\r\n" + "   END IF;\r\n" + "  \r\n"
				+ "   IF i > 30 AND i < 40 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 41 THEN SET i = 51;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 50 AND i < 60 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 61 THEN SET i = 71;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 70 AND i < 80 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 81 THEN SET i = 91;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 90 AND i < 100 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + " SET i = i + 2;\r\n" + " END WHILE;\r\n" + "\r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateSMFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForSMFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 1;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 90 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 11 THEN SET i = 21;\r\n" + "   END IF;\r\n" + "  \r\n"
				+ "   IF i > 20 AND i < 30 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 31 THEN SET i = 41;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 40 AND i < 50 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 51 THEN SET i = 61;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 60 AND i < 70 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 71 THEN SET i = 81;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 80 AND i < 90 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " ELSE\r\n" + "\r\n" + "  WHILE i < 90 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 11 THEN SET i = 21;\r\n" + "   END IF;\r\n" + "  \r\n"
				+ "   IF i > 20 AND i < 30 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 31 THEN SET i = 41;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 40 AND i < 50 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 51 THEN SET i = 61;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 60 AND i < 70 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 71 THEN SET i = 81;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 80 AND i < 90 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "   \r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateMSFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForMSFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 10;\r\n"
				+ "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 99 DO\r\n" + "\r\n"
				+ "   IF i < 19 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 20 THEN SET i = 30;\r\n" + "   END IF;\r\n" + "  \r\n"
				+ "   IF i > 29 AND i < 39 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 40 THEN SET i = 50;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 49 AND i < 59 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 60 THEN SET i = 70;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 69 AND i < 79 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 80 THEN SET i = 90;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 89 AND i < 99 THEN\r\n" + "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " ELSE\r\n" + "\r\n" + "  WHILE i < 99 DO\r\n" + "\r\n" + "   IF i < 19 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 20 THEN SET i = 30;\r\n" + "   END IF;\r\n" + "  \r\n"
				+ "   IF i > 29 AND i < 39 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 40 THEN SET i = 50;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 49 AND i < 59 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 60 THEN SET i = 70;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 69 AND i < 79 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 80 THEN SET i = 90;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 89 AND i < 99 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateSPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForSPFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 89 DO\r\n" + "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  WHILE i < 89 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateMPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForMPFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 11;\r\n"
				+ "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateAAFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForAAFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 11;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 11;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateANFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForANFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `second_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i + second_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 10;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i + second_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i + second_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 10;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateBFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForBFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `first_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE j INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n"
				+ " SET i = 0;\r\n" + " SET j = first_num;\r\n" + "\r\n" + " WHILE i < first_num  DO\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = CONCAT(i, j) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "  UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = CONCAT(i, j) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " SET i = i + 1;\r\n" + " SET j = j - 1;\r\n" + " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateNFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForNFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 07 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 07 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 18 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 18 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 24 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 24 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 35 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 35 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 69 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 69 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 96 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 96 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 53 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 53 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 42 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 42 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 81 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 81 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 70 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 70 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdatePFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForPFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 05 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 05 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 16 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 16 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 27 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 27 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 38 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 38 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 49 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 49 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 50 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 50 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 61 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 61 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 72 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 72 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 83 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 83 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 94 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 94 AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateNAFormulaProcedure() {
		String sql = "CREATE PROCEDURE `updateDataForNAFormula`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `first_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n"
				+ " SET i = first_num * 10;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  WHILE i < (first_num + 1) * 10 DO\r\n" + "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 1;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  WHILE i < (first_num + 1) * 10 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = i AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "  SET i = i + 1;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateForEachPlayNumberProcedure() {
		String sql = "CREATE PROCEDURE `updateForEachPlayNumber`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n"
				+ "    \r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    \r\n"
				+ "   UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    \r\n" + "  END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForSSForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForSSForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'SS', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForMMForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForMMForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'MM', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForSMForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForSMForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'SM', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForMSForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForMSForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'MS', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForSPForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForSPForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'SP', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForMPForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForMPForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'MP', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForAAForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForAAForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, '**', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForANForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForANForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `play_num` VARCHAR(15), IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, CONCAT('*', play_num), amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForNAForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForNAForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `play_num` VARCHAR(15), IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, CONCAT(play_num, '*'), amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForBForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForBForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `play_num` VARCHAR(15), IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, CONCAT(play_num, 'b'), amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForNForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForNForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'N', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForPForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForPForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, 'P', amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForEachNumForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForEachNumForCurTableProcedure`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `play_num` VARCHAR(15), IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, play_num, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createInsertDataForKhwayForCurTableProcedure() {
		String sql = "CREATE PROCEDURE `insertDataForKhwayFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `play_num` VARCHAR(15), IN `extract_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " IF (EXISTS(SELECT play_number FROM twod_user_play_for_cur_table as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me)) THEN\r\n"
				+ "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount + amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "   ELSE\r\n" + "\r\n"
				+ "    INSERT INTO twod_user_play_for_cur_table VALUES (DEFAULT, customer_id, play_num, amount, me, date, extract_num);\r\n"
				+ "    \r\n" + "   END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSSFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForSSFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 89 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n"
				+ "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 10 THEN SET i = 20;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 19 AND i < 29 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 30 THEN SET i = 40;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 39 AND i < 49 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 50 THEN SET i = 60;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 59 AND i < 69 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 70 THEN SET i = 80;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 79 AND i < 89 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " ELSE\r\n" + "\r\n" + "  WHILE i < 89 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 10 THEN SET i = 20;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 19 AND i < 29 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 30 THEN SET i = 40;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 39 AND i < 49 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 50 THEN SET i = 60;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 59 AND i < 69 THEN\r\n" + "   \r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 70 THEN SET i = 80;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 79 AND i < 89 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMMFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForMMFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 11;\r\n"
				+ "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   IF i < 20 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 21 THEN SET i = 31;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 30 AND i < 40 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 41 THEN SET i = 51;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 50 AND i < 60 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 61 THEN SET i = 71;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 70 AND i < 80 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 81 THEN SET i = 91;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 90 AND i < 100 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " ELSE\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n" + "   IF i < 20 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 21 THEN SET i = 31;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 30 AND i < 40 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 41 THEN SET i = 51;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 50 AND i < 60 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 61 THEN SET i = 71;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 70 AND i < 80 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 81 THEN SET i = 91;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 90 AND i < 100 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSMFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForSMFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 1;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 90 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n"
				+ "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 11 THEN SET i = 21;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 20 AND i < 30 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 31 THEN SET i = 41;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 40 AND i < 50 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 51 THEN SET i = 61;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 60 AND i < 70 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 71 THEN SET i = 81;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 80 AND i < 90 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " ELSE\r\n" + "\r\n" + "  WHILE i < 90 DO\r\n" + "\r\n" + "   IF i < 10 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 11 THEN SET i = 21;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 20 AND i < 30 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 31 THEN SET i = 41;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 40 AND i < 50 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 51 THEN SET i = 61;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 60 AND i < 70 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 71 THEN SET i = 81;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 80 AND i < 90 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMSFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForMSFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 10;\r\n"
				+ "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 99 DO\r\n" + "\r\n"
				+ "   IF i < 19 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 20 THEN SET i = 30;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 29 AND i < 39 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 40 THEN SET i = 50;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 49 AND i < 59 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 60 THEN SET i = 70;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 69 AND i < 79 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 80 THEN SET i = 90;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 89 AND i < 99 THEN\r\n" + "\r\n"
				+ "    DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " ELSE\r\n" + "\r\n" + "  WHILE i < 99 DO\r\n" + "\r\n" + "   IF i < 19 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 20 THEN SET i = 30;\r\n" + "  END IF;\r\n" + "  \r\n"
				+ "   IF i > 29 AND i < 39 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 40 THEN SET i = 50;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 49 AND i < 59 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  END IF;\r\n" + "\r\n" + "   IF i = 60 THEN SET i = 70;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 69 AND i < 79 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "   IF i = 80 THEN SET i = 90;\r\n" + "   END IF;\r\n" + "\r\n"
				+ "   IF i > 89 AND i < 99 THEN\r\n" + "\r\n"
				+ "    SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "   END IF;\r\n" + "\r\n" + "  SET i = i + 2;\r\n" + "  END WHILE;\r\n" + "\r\n"
				+ " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForSPFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 89 DO\r\n" + "\r\n"
				+ "   DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  WHILE i < 89 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForMPFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 11;\r\n"
				+ "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 22;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForAAFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForAAFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 11;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 11;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForANFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForANFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `second_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " SET i = 0;\r\n" + "\r\n"
				+ " IF all_last = 'all' THEN\r\n" + "\r\n" + "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   DELETE FROM twoD_user_play WHERE play_number = i + second_num AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 10;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE\r\n" + "\r\n"
				+ "  WHILE i < 100 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i + second_num AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i + second_num AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 10;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForNAFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForNAFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `first_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n"
				+ " SET i = first_num * 10;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  WHILE i < (first_num + 1) * 10 DO\r\n" + "\r\n"
				+ "   DELETE FROM twoD_user_play WHERE play_number = i AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 1;\r\n" + "  END WHILE;\r\n" + "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  WHILE i < (first_num + 1) * 10 DO\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = i AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "  SET i = i + 1;\r\n" + "  END WHILE;\r\n" + "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForBFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForBFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `first_num` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE i INT;\r\n" + " DECLARE j INT;\r\n" + " DECLARE alreadyExistedAmount INT;\r\n"
				+ " SET i = 0;\r\n" + " SET j = first_num;\r\n" + "\r\n" + " WHILE i < first_num  DO\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = CONCAT(i, j) AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT(i, j) AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + " SET i = i + 1;\r\n" + " SET j = j - 1;\r\n" + " END WHILE;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForNFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForNFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 07 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 07 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 18 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 18 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 24 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 24 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 35 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 35 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 69 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 69 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 96 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 96 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 53 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 53 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 42 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 42 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 81 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 81 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 70 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 70 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForPFormulaProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForPFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 05 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 05 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 16 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 16 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 27 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 27 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 38 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 38 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 49 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 49 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 50 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 50 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 61 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 61 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 72 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 72 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 83 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 83 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = 94 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 94 AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForEachNumberProcedure() {
		String sql = "CREATE PROCEDURE `deleteDataForEachNumberFormula`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `pplay_number` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  DELETE FROM twoD_user_play WHERE play_number = pplay_number AND customer_id = pcustomer_id AND date = pdate AND me = pme;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = pplay_number AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = pplay_number AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForSSFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForSSFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForMMFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForMMFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForSMFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForSMFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForMSFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForMSFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForSPFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForSPFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForMPFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForMPFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForAAFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForAAFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForANFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForANFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForNAFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForNAFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForBFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForBFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForNFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForNFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForPFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `updateDataForPFormulaForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ " UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForEachPlayNumberForCurTable() {
		String sql = "CREATE PROCEDURE `updateForEachPlayNumberForCurTable`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n" + " IF all_last = 'all' THEN\r\n" + "\r\n"
				+ "  UPDATE twoD_user_play_for_cur_table as t SET t.amount = amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " ELSE \r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twoD_user_play_for_cur_table as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me;\r\n"
				+ "\r\n" + " END IF;\r\n" + "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSSFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForSSFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SS' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'SS' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMMFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForMMFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC NO SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MM' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'MM' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSMFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForSMFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SM' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'SM' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMSFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForMSFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN DECLARE alreadyExistedAmount INT; SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MS' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme; UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'MS' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme; END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSPFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForSPFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'SP' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'SP' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMPFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForMPFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN DECLARE alreadyExistedAmount INT; SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'MP' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme; UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'MP' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme; END\r\n"
				+ "";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForAAFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForAAFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = '**' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "  UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = '**' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForANFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForANFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "  UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForNAFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForNAFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "  UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForBFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForBFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "  UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForNFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForNFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'N' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "  UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'N' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForPFormulaForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForPFormulaForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = 'P' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "  UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'P' AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForEachNumberForCurTable() {
		String sql = "CREATE PROCEDURE `deleteDataForEachNumberForCurTable`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT, IN `pplay_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "  SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table as t WHERE t.play_number = pplay_num AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "  UPDATE twod_user_play_for_cur_table as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = pplay_num AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForSSFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForSSFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForMMFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForMMFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForSMFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForSMFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForMSFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForMSFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForSPFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForSPFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForMPFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForMPFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForAAFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForAAFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForANFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForANFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForNAFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForNAFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForBFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForBFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForNFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForNFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForPFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateDataForPFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForEachPlayNumberForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateForEachPlayNumberForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total + amount WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUpdateDataForRForCurTableForLast() {
		String sql = "CREATE PROCEDURE `updateForRForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `amount` INT, IN `me` VARCHAR(15), IN `date` DATE, IN `all_last` VARCHAR(15), IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15), IN `indexP` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedId INT;\r\n" + "\r\n"
				+ " SELECT twoD_user_play_id INTO alreadyExistedId FROM twod_user_play_for_cur_table_for_last LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = amount WHERE t.twoD_user_play_id = alreadyExistedId + indexP;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSSFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForSSFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'SS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMMFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE ` deleteDataForMMFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'MM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSMFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForSMFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'SM' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMSFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForMSFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'MS' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForSPFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForSPFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'SP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForMPFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForMPFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN DECLARE alreadyExistedAmount INT; SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1; UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'MP' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1; END\r\n"
				+ "";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForAAFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForAAFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = '**' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForANFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForANFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT('*', play_num) AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForNAFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForNAFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT(play_num, '*') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForBFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForBFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT(play_num, 'b') AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForNFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForNFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'N' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForPFormulaForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForPFormulaForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = 'P' AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForEachPlayNumberForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForEachPlayNumberForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT, IN `play_num` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + " \r\n"
				+ " SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play_for_cur_table_for_last as t WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = play_num AND t.customer_id = customer_id AND t.date = date AND t.me = me AND t.amount = to_extract_from_total LIMIT 1;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDataForRForCurTableForLast() {
		String sql = "CREATE PROCEDURE `deleteDataForRForCurTableForLast`(IN `customer_id` VARCHAR(15), IN `me` VARCHAR(15), IN `date` DATE, IN `to_extract_from_total` INT, IN `indexP` INT) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedId INT;\r\n" + "\r\n"
				+ " SELECT twoD_user_play_id INTO alreadyExistedId FROM twod_user_play_for_cur_table_for_last LIMIT 1;\r\n"
				+ "\r\n"
				+ " UPDATE twod_user_play_for_cur_table_for_last as t SET t.amount = 0 WHERE t.twoD_user_play_id = alreadyExistedId + indexP;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDeleteDuplicateNumForPlus() {
		String sql = "CREATE PROCEDURE `deleteDuplicateNumForPlus`(IN `pcustomer_id` VARCHAR(15), IN `pme` VARCHAR(15), IN `pdate` DATE, IN `to_extract_from_total` INT, IN `duplicateNum` VARCHAR(15)) NOT DETERMINISTIC CONTAINS SQL SQL SECURITY DEFINER BEGIN\r\n"
				+ "\r\n" + " DECLARE alreadyExistedAmount INT;\r\n" + "\r\n"
				+ "   SELECT t.amount INTO alreadyExistedAmount FROM twod_user_play as t WHERE t.play_number = CONCAT(duplicateNum, duplicateNum) AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n"
				+ "    UPDATE twoD_user_play as t SET t.amount = alreadyExistedAmount - to_extract_from_total WHERE t.play_number = CONCAT(duplicateNum, duplicateNum) AND t.customer_id = pcustomer_id AND t.date = pdate AND t.me = pme;\r\n"
				+ "\r\n" + "END";
		try (Connection con = createConnectionForProcedure(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ArrayList<String> databaseNames = new ArrayList<>();
		Connection con = createConnection();
		try {
			ResultSet rs = con.getMetaData().getCatalogs();
			while (rs.next()) {

				String catalogs = rs.getString(1);
				databaseNames.add(catalogs);

				if (!databaseNames.contains("2d3d")) {
					createDatabase();
					System.out.println("Database is created...");
					createCustomerTable();
					System.out.println("customer table is created...");
					insertCustomerName("Special Customer", false, LocalDate.now());
					create2DUserPlayTable();
					System.out.println("twod_user_play table is created...");
					create3DUserPlayTable();
					System.out.println("threed_user_play table is created...");
					createExceedingTable();
					System.out.println("exceeding table is created...");
					createOutsourceTable();
					System.out.println("outsource table is created...");
					createWinNumTable();
					System.out.println("WinNum table is created...");
					create2DUserPlayTableForCurTable();
					System.out.println("twod_user_play_for_cur_table table is created...");
					create2DUserPlayTableForCurTableForLast();
					System.out.println("twod_user_play_for_cur_table_for_last table is created...");
					createsetAmtTable();
					System.out.println("Set Amount table is created ...");
					createInsertSSFormulaProcedure();
					System.out.println("InsertSSFormulaProcedure is created...");
					createInsertMMFormulaProcedure();
					System.out.println("InsertMMFormulaProcedure is created...");
					createInsertSMFormulaProcedure();
					System.out.println("InsertSMFormulaProcedure is created...");
					createInsertMSFormulaProcedure();
					System.out.println("InsertMSFormulaProcedure is created...");
					createInsertSPFormulaProcedure();
					System.out.println("InsertSPFormulaProcedure is created...");
					createInsertMPFormulaProcedure();
					System.out.println("InsertMPFormulaProcedure is created...");
					createInsertAAFormulaProcedure();
					System.out.println("InsertAAFormulaProcedure is created...");
					createInsertANFormulaProcedure();
					System.out.println("InsertANFormulaProcedure is created...");
					createInsertNAFormulaProcedure();
					System.out.println("InsertNAFormulaProcedure is created...");
//					HERE
					createInsertBFormulaProcedure();
					System.out.println("createInsertBFormulaProcedure is created...");
					createInsertNFormulaProcedure();
					System.out.println("createInsertNFormulaProcedure is created...");
					createInsertPFormulaProcedure();
					System.out.println("createInsertPFormulaProcedure is created...");
					createInsertKhwayFormulaProcedure();
					System.out.println("createInsertKhwayFormulaProcedure is created...");

					createInsertForEachPlayNumberProcedure();
					System.out.println("InsertForEachPlayNumberProcedure is created...");

					createUpdateSSFormulaProcedure();
					System.out.println("UpdateSSFormulaProcedure is created...");
					createUpdateForEachPlayNumberProcedure();
					System.out.println("UpdateForEachPlayNumberProcedure is created...");
					createUpdateMMFormulaProcedure();
					System.out.println("UpdateMMFormulaProcedure is created...");
					createUpdateSMFormulaProcedure();
					System.out.println("UpdateSMFormulaProcedure is created...");
					createUpdateMSFormulaProcedure();
					System.out.println("UpdateMSFormulaProcedure is created...");
					createUpdateSPFormulaProcedure();
					System.out.println("UpdateSPFormulaProcedure is created...");
					createUpdateMPFormulaProcedure();
					System.out.println("UpdateMPFormulaProcedure is created...");
					createUpdateAAFormulaProcedure();
					System.out.println("UpdateAAFormulaProcedure is created...");
					createUpdateANFormulaProcedure();
					System.out.println("UpdateANFormulaProcedure is created...");

//					HERE
					createUpdateBFormulaProcedure();
					System.out.println("createUpdateBFormulaProcedure is created...");
					createUpdateNFormulaProcedure();
					System.out.println("createUpdateNFormulaProcedure is created...");
					createUpdatePFormulaProcedure();
					System.out.println("createUpdatePFormulaProcedure is created...");

					createUpdateNAFormulaProcedure();
					System.out.println("UpdateNAFormulaProcedure is created...");

					createInsertDataForSSForCurTableProcedure();
					System.out.println("createInsertDataForSSForCurTableProcedure is created...");
					createInsertDataForMMForCurTableProcedure();
					System.out.println("createInsertDataForMMForCurTableProcedure is created...");
					createInsertDataForSMForCurTableProcedure();
					System.out.println("createInsertDataForSMForCurTableProcedure is created...");
					createInsertDataForMSForCurTableProcedure();
					System.out.println("createInsertDataForMSForCurTableProcedure is created...");
					createInsertDataForSPForCurTableProcedure();
					System.out.println("createInsertDataForSPForCurTableProcedure is created...");
					createInsertDataForMPForCurTableProcedure();
					System.out.println("createInsertDataForMPForCurTableProcedure is created...");
					createInsertDataForAAForCurTableProcedure();
					System.out.println("createInsertDataForAAForCurTableProcedure is created...");
					createInsertDataForANForCurTableProcedure();
					System.out.println("createInsertDataForANForCurTableProcedure is created...");
					createInsertDataForNAForCurTableProcedure();
					System.out.println("createInsertDataForNAForCurTableProcedure is created...");

//					HERE
					createInsertDataForBForCurTableProcedure();
					System.out.println("createInsertDataForBForCurTableProcedure is created...");
					createInsertDataForNForCurTableProcedure();
					System.out.println("createInsertDataForNForCurTableProcedure is created...");
					createInsertDataForPForCurTableProcedure();
					System.out.println("createInsertDataForPForCurTableProcedure is created...");
					createInsertDataForKhwayForCurTableProcedure();
					System.out.println("createInsertDataForKhwayForCurTableProcedure is created...");

					createInsertDataForEachNumForCurTableProcedure();
					System.out.println("createInsertDataForEachNumForCurTableProcedure is created...");

					createDeleteDataForSSFormulaProcedure();
					System.out.println("createDeleteDataForSSFormulaProcedure is created...");
					createDeleteDataForMMFormulaProcedure();
					System.out.println("createDeleteDataForMMFormulaProcedure is created...");
					createDeleteDataForSMFormulaProcedure();
					System.out.println("createDeleteDataForSMFormulaProcedure is created...");
					createDeleteDataForMSFormulaProcedure();
					System.out.println("createDeleteDataForMSFormulaProcedure is created...");
					createDeleteDataForSPFormulaProcedure();
					System.out.println("createDeleteDataForSPFormulaProcedure is created...");
					createDeleteDataForMPFormulaProcedure();
					System.out.println("createDeleteDataForMPFormulaProcedure is created...");
					createDeleteDataForAAFormulaProcedure();
					System.out.println("createDeleteDataForAAFormulaProcedure is created...");
					createDeleteDataForANFormulaProcedure();
					System.out.println("createDeleteDataForANFormulaProcedure is created...");
					createDeleteDataForNAFormulaProcedure();
					System.out.println("createDeleteDataForNAFormulaProcedure is created...");

//					HERE
					createDeleteDataForBFormulaProcedure();
					System.out.println("createDeleteDataForNAFormulaProcedure is created...");
					createDeleteDataForNFormulaProcedure();
					System.out.println("createDeleteDataForNFormulaProcedure is created...");
					createDeleteDataForPFormulaProcedure();
					System.out.println("createDeleteDataForNFormulaProcedure is created...");

					createDeleteDataForEachNumberProcedure();
					System.out.println("createDeleteDataForEachNumberProcedure is created...");

					createUpdateDataForSSFormulaForCurTable();
					System.out.println("createUpdateDataForSSFormulaForCurTable is created...");
					createUpdateDataForMMFormulaForCurTable();
					System.out.println("createUpdateDataForMMFormulaForCurTable is created...");
					createUpdateDataForSMFormulaForCurTable();
					System.out.println("createUpdateDataForSMFormulaForCurTable is created...");
					createUpdateDataForMSFormulaForCurTable();
					System.out.println("createUpdateDataForMSFormulaForCurTable is created...");
					createUpdateDataForSPFormulaForCurTable();
					System.out.println("createUpdateDataForSPFormulaForCurTable is created...");
					createUpdateDataForMPFormulaForCurTable();
					System.out.println("createUpdateDataForMPFormulaForCurTable is created...");
					createUpdateDataForAAFormulaForCurTable();
					System.out.println("createUpdateDataForAAFormulaForCurTable is created...");
					createUpdateDataForANFormulaForCurTable();
					System.out.println("createUpdateDataForANFormulaForCurTable is created...");
					createUpdateDataForNAFormulaForCurTable();
					System.out.println("createUpdateDataForNAFormulaForCurTable is created...");

//					HERE
					createUpdateDataForBFormulaForCurTable();
					System.out.println("createUpdateDataForBFormulaForCurTable is created...");
					createUpdateDataForNFormulaForCurTable();
					System.out.println("createUpdateDataForNFormulaForCurTable is created...");
					createUpdateDataForPFormulaForCurTable();
					System.out.println("createUpdateDataForPFormulaForCurTable is created...");

					createUpdateDataForEachPlayNumberForCurTable();
					System.out.println("createUpdateDataForEachPlayNumberForCurTable is created...");

					createDeleteDataForSSFormulaForCurTable();
					System.out.println("createDeleteDataForSSFormulaForCurTable is created...");
					createDeleteDataForMMFormulaForCurTable();
					System.out.println("createDeleteDataForMMFormulaForCurTable is created...");
					createDeleteDataForSMFormulaForCurTable();
					System.out.println("createDeleteDataForSMFormulaForCurTable is created...");
					createDeleteDataForMSFormulaForCurTable();
					System.out.println("createDeleteDataForMSFormulaForCurTable is created...");
					createDeleteDataForSPFormulaForCurTable();
					System.out.println("createDeleteDataForSPFormulaForCurTable is created...");
					createDeleteDataForMPFormulaForCurTable();
					System.out.println("createDeleteDataForMPFormulaForCurTable is created...");
					createDeleteDataForAAFormulaForCurTable();
					System.out.println("createDeleteDataForAAFormulaForCurTable is created...");
					createDeleteDataForANFormulaForCurTable();
					System.out.println("createDeleteDataForANFormulaForCurTable is created...");
					createDeleteDataForNAFormulaForCurTable();
					System.out.println("createDeleteDataForNAFormulaForCurTable is created...");

//					HERE
					createDeleteDataForBFormulaForCurTable();
					System.out.println("createDeleteDataForBFormulaForCurTable is created...");
					createDeleteDataForNFormulaForCurTable();
					System.out.println("createDeleteDataForBFormulaForCurTable is created...");
					createDeleteDataForPFormulaForCurTable();
					System.out.println("createDeleteDataForPFormulaForCurTable is created...");

					createDeleteDataForEachNumberForCurTable();
					System.out.println("createDeleteDataForEachNumberForCurTable is created...");

					createUpdateDataForSSFormulaForCurTableForLast();
					System.out.println("createUpdateDataForSSFormulaForCurTableForLast is created...");
					createUpdateDataForMMFormulaForCurTableForLast();
					System.out.println("createUpdateDataForMMFormulaForCurTableForLast is created...");
					createUpdateDataForSMFormulaForCurTableForLast();
					System.out.println("createUpdateDataForSMFormulaForCurTableForLast is created...");
					createUpdateDataForMSFormulaForCurTableForLast();
					System.out.println("createUpdateDataForMSFormulaForCurTableForLast is created...");
					createUpdateDataForSPFormulaForCurTableForLast();
					System.out.println("createUpdateDataForSPFormulaForCurTableForLast is created...");
					createUpdateDataForMPFormulaForCurTableForLast();
					System.out.println("createUpdateDataForMPFormulaForCurTableForLast is created...");
					createUpdateDataForAAFormulaForCurTableForLast();
					System.out.println("createUpdateDataForAAFormulaForCurTableForLast is created...");
					createUpdateDataForANFormulaForCurTableForLast();
					System.out.println("createUpdateDataForANFormulaForCurTableForLast is created...");
					createUpdateDataForNAFormulaForCurTableForLast();
					System.out.println("createUpdateDataForNAFormulaForCurTableForLast is created...");
					createUpdateDataForBFormulaForCurTableForLast();
					System.out.println("createUpdateDataForBFormulaForCurTableForLast is created...");
					createUpdateDataForNFormulaForCurTableForLast();
					System.out.println("createUpdateDataForNFormulaForCurTableForLast is created...");
					createUpdateDataForPFormulaForCurTableForLast();
					System.out.println("createUpdateDataForPFormulaForCurTableForLast is created...");
					createUpdateDataForEachPlayNumberForCurTableForLast();
					System.out.println("createUpdateDataForEachPlayNumberForCurTableForLast is created...");
					createUpdateDataForRForCurTableForLast();
					System.out.println("createUpdateDataForRForCurTableForLast is created...");

					createDeleteDataForSSFormulaForCurTableForLast();
					System.out.println("createDeleteDataForSSFormulaForCurTableForLast is created...");
					createDeleteDataForMMFormulaForCurTableForLast();
					System.out.println("createDeleteDataForMMFormulaForCurTableForLast is created...");
					createDeleteDataForSMFormulaForCurTableForLast();
					System.out.println("createDeleteDataForSMFormulaForCurTableForLast is created...");
					createDeleteDataForMSFormulaForCurTableForLast();
					System.out.println("createDeleteDataForMSFormulaForCurTableForLast is created...");
					createDeleteDataForSPFormulaForCurTableForLast();
					System.out.println("createDeleteDataForSPFormulaForCurTableForLast is created...");
					createDeleteDataForMPFormulaForCurTableForLast();
					System.out.println("createDeleteDataForMPFormulaForCurTableForLast is created...");
					createDeleteDataForAAFormulaForCurTableForLast();
					System.out.println("createDeleteDataForAAFormulaForCurTableForLast is created...");
					createDeleteDataForANFormulaForCurTableForLast();
					System.out.println("createDeleteDataForANFormulaForCurTableForLast is created...");
					createDeleteDataForNAFormulaForCurTableForLast();
					System.out.println("createDeleteDataForNAFormulaForCurTableForLast is created...");

//					HERE
					createDeleteDataForBFormulaForCurTableForLast();
					System.out.println("createDeleteDataForNAFormulaForCurTableForLast is created...");
					createDeleteDataForNFormulaForCurTableForLast();
					System.out.println("createDeleteDataForNFormulaForCurTableForLast is created...");
					createDeleteDataForPFormulaForCurTableForLast();
					System.out.println("createDeleteDataForPFormulaForCurTableForLast is created...");

					createDeleteDataForEachPlayNumberForCurTableForLast();
					System.out.println("createDeleteDataForEachPlayNumberForCurTableForLast is created...");
					createDeleteDataForRForCurTableForLast();
					System.out.println("createDeleteDataForRForCurTableForLast is created...");
					createDeleteDuplicateNumForPlus();
					System.out.println("Database is created ... ");
					break;
				} else {
					System.out.println("Database already exist...");
				}
			}
//			createDeleteDataForSSFormulaForCurTableForLast();
//			createDeleteDataForMMFormulaForCurTableForLast();
//			createDeleteDataForSMFormulaForCurTableForLast();
//			createDeleteDataForMSFormulaForCurTableForLast();
//			createDeleteDataForSPFormulaForCurTableForLast();
//			createDeleteDataForMPFormulaForCurTableForLast();
//			createDeleteDataForAAFormulaForCurTableForLast();
//			createDeleteDataForANFormulaForCurTableForLast();
//			createDeleteDataForNAFormulaForCurTableForLast();
//			createDeleteDataForEachPlayNumberForCurTableForLast();

			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}