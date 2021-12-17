package database2d3d;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.OutSource;
import service.OutSourceService;
import unit.DBConection;

public class OutSourceImplementation implements OutSourceService {

	@Override
	public boolean insertIntoOutSourcetable(OutSource outSource) {
		String sql = "INSERT INTO `outsource`(`customer_id`, `exceeding_id`, `currentAmount`) VALUES (?,?,?)";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, outSource.getCustomer_id());
			stmt.setInt(2, outSource.getExceeding_id());
			stmt.setInt(3, outSource.getCurrentAmt());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	@Override
	public ArrayList<OutSource> getOutSourceData(String name, LocalDate date, String morning) {
		ArrayList<OutSource> list = new ArrayList<OutSource>();
		String sql = "SELECT exceeding.exceeded_number,SUM(outsource.currentAmount) FROM exceeding,outsource,customer WHERE exceeding.exceeding_id=outsource.exceeding_id AND outsource.customer_id=customer.customer_id AND customer.name=? AND exceeding.date=? AND exceeding.me=? GROUP BY outsource.exceeding_id ORDER BY outsource.outsource_id";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setDate(2, Date.valueOf(date));
			stmt.setString(3, morning);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new OutSource(rs.getInt(1), rs.getInt(2)));

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public int getTotal(String name) {
		String sql = "SELECT SUM(currentAmount) FROM outsource,customer WHERE customer.customer_id=outsource.customer_id AND customer.name=? ";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getTotalSumOfExceedAmount(int exceedID) {
		String sql = "SELECT SUM(currentAmount) FROM outsource WHERE exceeding_id=? GROUP BY(exceeding_id)";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setInt(1, exceedID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public ArrayList<OutSource> getOutsourcesData(int playNumber, String me, LocalDate date) {
		ArrayList<OutSource> list = new ArrayList<OutSource>();
		String sql = "SELECT customer.name,exceeding.exceeded_number,SUM(outsource.currentAmount) FROM customer,exceeding,outsource WHERE customer.customer_id=outsource.customer_id AND exceeding.exceeding_id=outsource.exceeding_id AND exceeding.exceeded_number=? and exceeding.date=? AND exceeding.me=? GROUP BY customer.name;";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, playNumber);
			stmt.setDate(2, Date.valueOf(date));
			stmt.setString(3, me);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new OutSource(rs.getString(1), rs.getInt(2), rs.getInt(3)));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public int getTotalExceedAmount(int playNumber) {
		String sql = "SELECT SUM(outsource.currentAmount) FROM outsource,exceeding WHERE exceeding.exceeding_id=outsource.exceeding_id AND exceeding.exceeded_number=? GROUP BY(outsource.exceeding_id);";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setInt(1, playNumber);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public boolean updateIntoOutSourcetable(int execeedingID, int currentAmount) {
		int currentAmt = 0;
		String preSql = "SELECT currentAmount FROM outsource WHERE exceeding_id=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(preSql)) {
			stmt.setInt(1, execeedingID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				currentAmt = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		String sql = "UPDATE `outsource` SET currentAmount=? WHERE exceeding_id=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, currentAmt + currentAmount);
			stmt.setInt(2, execeedingID);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public int deleteOutSource(int execeedingID, String customerID) {
		String sql = "DELETE FROM `outsource` WHERE exceeding_id=? AND customer_id=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, customerID);
			stmt.setInt(2, execeedingID);
			stmt.executeUpdate();
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
	}

	@Override
	public int deleteOutSource(int outID) {

		String sql = "DELETE FROM outsource WHERE outsource_id=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, outID);
			System.out.println("Delete Status:" + stmt.executeUpdate());
			return 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());

		}
		return -1;
	}

	@Override
	public int getOutsourceID(String cID, int exeID) {
		String sql = "SELECT outsource_id FROM outsource WHERE customer_id=? AND exceeding_id=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, cID);
			stmt.setInt(2, exeID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());

		}
		return -1;
	}

	@Override
	public int isValidExceedID(String customerID, int playNumber, String me, LocalDate date) {
		String sql = "SELECT outsource.exceeding_id FROM outsource,exceeding WHERE outsource.exceeding_id=exceeding.exceeding_id AND outsource.customer_id=? AND exceeding.exceeded_number=? AND exceeding.date=? AND exceeding.me=?;";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, customerID);
			stmt.setInt(2, playNumber);
			stmt.setDate(3, Date.valueOf(date));
			stmt.setString(4, me);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
	}

	@Override
	public boolean updateCurrentAmount(String customerID, int exceedingNumber, String me, LocalDate date,
			int previousCurrentAmount, int nowCurrentAmount) {
		String sql = "UPDATE outsource,exceeding SET currentAmount=? WHERE outsource.exceeding_id=exceeding.exceeding_id AND exceeding.me=? AND exceeding.date=? AND outsource.customer_id=? AND exceeding.exceeded_number=? AND outsource.currentAmount=?;";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, nowCurrentAmount);
			stmt.setString(2, me);
			stmt.setDate(3, Date.valueOf(date));
			stmt.setString(4, customerID);
			stmt.setInt(5, exceedingNumber);
			stmt.setInt(6, previousCurrentAmount);
			System.out.println("Update Outsource:" + stmt.executeUpdate());
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

}
