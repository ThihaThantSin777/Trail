package database2d3d;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import unit.DBConection;

public class SetAmountImplementation {
	public boolean updateAmount(int amount) {
		String sql = "UPDATE `setamount` SET `setamount`=" + amount;
		try (Connection con = DBConection.createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.executeUpdate();
			return true;

		} catch (Exception e) {
		}
		return false;
	}

	public int getamount() {
		String sql = "SELECT setamount from `setamount`;";
		try (Connection con = DBConection.createConnection();
				PreparedStatement pre = con.prepareStatement(sql);
				ResultSet rs = pre.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
		}
		return -1;
	}

}
