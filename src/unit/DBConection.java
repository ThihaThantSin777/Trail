package unit;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConection {
	private static String url = "jdbc:mysql://192.168.100.157/2d3d";
	private static String username = "root";
	private static String password = "123#asdFF";

	public static Connection createConnection() {
		try {
//			String ipUrl = "jdbc:mysql://" + InetAddress.getLocalHost().getHostAddress() + "//2d3d";
//			
			return DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
//			e.printStackTrace();
			// AlertShow.showError("EXCEPTION ERROR");
		}
		return null;
	}
}
