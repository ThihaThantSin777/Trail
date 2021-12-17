package database2d3d;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import service.BackupService;
import unit.DBConection;
import unit.PasswordEncypt;

public class BackupImplementation implements BackupService {

	@Override
	public boolean customerBackup(File file) {
		String sql = "SELECT * FROM customer";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();) {

			Path path = Paths.get(file.getAbsolutePath());
			String data = "";
			while (rs.next()) {

				String text = rs.getString(1) + "," + rs.getString(2) + "," + rs.getDate(3).toString();
				String enCode = PasswordEncypt.encode(text) + "\n";
				data += enCode;
			}

			try {

				Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

			} catch (IOException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;

	}

	@Override
	public boolean twoDThreeDBackup(File file) {
		String sql = "SELECT * FROM twod_user_play";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();) {
			Path path = Paths.get(file.getAbsolutePath());
			String data = "";
			while (rs.next()) {

				String text = rs.getInt(1) + "," + rs.getString(2) + "," + rs.getInt(3) + "," + rs.getInt(4) + ","
						+ rs.getString(5) + "," + rs.getDate(6).toString() + "," + rs.getInt(7);
				String enCode = PasswordEncypt.encode(text) + "\n";

				data += enCode;

			}
			try {
				Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public boolean exceedingBackup(File file) {
		String sql = "SELECT * FROM exceeding";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();) {

			Path path = Paths.get(file.getAbsolutePath());
			String data = "";
			while (rs.next()) {

				String text = rs.getInt(1) + "," + rs.getInt(2) + "," + rs.getInt(3) + "," + rs.getDate(4).toString()
						+ "," + rs.getString(5) + "," + rs.getInt(6);
				String enCode = PasswordEncypt.encode(text) + "\n";
				data += enCode;

			}
			try {
				Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public boolean outSourceBackup(File file) {
		String sql = "SELECT outsource.outsource_id,outsource.customer_id,outsource.exceeding_id,outsource.currentAmount FROM outsource,exceeding WHERE outsource.exceeding_id=exceeding.exceeding_id;";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();) {

			Path path = Paths.get(file.getAbsolutePath());
			String data = "";
			while (rs.next()) {

				String text = rs.getInt(1) + "," + rs.getString(2) + "," + rs.getInt(3) + "," + rs.getInt(4);
				String enCode = PasswordEncypt.encode(text) + "\n";
				data += enCode;
			}
			try {
				Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public boolean winNumBackup(File file) {
		String sql = "SELECT * FROM win_num";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();) {

			Path path = Paths.get(file.getAbsolutePath());
			String data = "";
			while (rs.next()) {

				String text = rs.getInt(1) + "," + rs.getInt(2) + "," + rs.getString(3) + "," + rs.getDate(4).toString()
						+ "," + rs.getString(5);

				String enCode = PasswordEncypt.encode(text) + "\n";
				data += enCode;
			}

			try {
				Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

}