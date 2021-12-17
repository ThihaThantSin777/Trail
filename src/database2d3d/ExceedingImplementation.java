package database2d3d;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.Exceeding;
import service.ExceedingService;
import unit.DBConection;

public class ExceedingImplementation implements ExceedingService {
	@Override
	public boolean deleteWithDate(LocalDate from, LocalDate to, String me) {
		if (me.equals("3D")) {
			String sql = "DELETE FROM exceeding WHERE me=?";
			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, me);
				stmt.executeUpdate();
				return true;
			} catch (Exception e) {
				e.getStackTrace();
			}
		} else {
			String sql = "DELETE FROM exceeding WHERE me=? AND date BETWEEN ? AND ?";
			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, me);
				stmt.setDate(2, Date.valueOf(from));
				stmt.setDate(3, Date.valueOf(to));
				stmt.executeUpdate();
				return true;
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean insert(Exceeding exceeding) {
		String sql = "INSERT INTO `exceeding`(`exceeded_number`, `exceeded_amount`, `date`, `me`, `is_noted`) VALUES (?,?,?,?,?)";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setInt(2, exceeding.getExceedingAmount());
			stmt.setDate(3, Date.valueOf(exceeding.getDate()));
			stmt.setString(4, exceeding.getMe());
			stmt.setBoolean(5, exceeding.isNoted());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public ArrayList<Exceeding> getExceedingList(Exceeding exceeding) {
		ArrayList<Exceeding> list = new ArrayList<Exceeding>();
		String sql = "SELECT exceeded_number,exceeded_amount,is_noted FROM exceeding WHERE is_noted=0 AND date=? AND me=? ORDER BY exceeded_amount DESC";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setDate(1, Date.valueOf(exceeding.getDate()));
			stmt.setString(2, exceeding.getMe());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Exceeding(rs.getInt(1), rs.getInt(2), rs.getBoolean(3)));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public int getTotalExceedAmount(String me, LocalDate date) {
		String sql = "SELECT SUM(exceeded_amount) FROM exceeding WHERE me=? AND date=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, me);
			stmt.setDate(2, Date.valueOf(date));
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
	public boolean updateAmount(Exceeding exceeding) {
		String sql = "UPDATE `exceeding` SET `exceeded_amount`=? WHERE exceeded_number=? AND me=? AND date=? AND is_noted=0";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, exceeding.getExceedingAmount());
			stmt.setInt(2, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setString(3, exceeding.getMe());
			stmt.setDate(4, Date.valueOf(exceeding.getDate()));
			System.out.println(stmt.executeUpdate());
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateNoted(Exceeding exceeding, int i) {
		String sql = "UPDATE `exceeding` SET `is_noted`=" + i + " WHERE exceeded_number=? AND me=? AND date=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setString(2, exceeding.getMe());
			stmt.setDate(3, Date.valueOf(exceeding.getDate()));
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public int isExceed(Exceeding exceeding) {
		String sql = "SELECT exceeded_amount FROM exceeding WHERE exceeded_number=? AND me=? AND date=? AND is_noted=0";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setString(2, exceeding.getMe());
			stmt.setDate(3, Date.valueOf(exceeding.getDate()));
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
	public int getExceedingID(Exceeding exceeding) {
		String sql = "SELECT exceeding_id FROM exceeding WHERE exceeded_number=? AND exceeded_amount=? AND me=? AND date=? AND is_noted=0";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setInt(2, exceeding.getExceedingAmount());
			stmt.setString(3, exceeding.getMe());
			stmt.setDate(4, Date.valueOf(exceeding.getDate()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {

		}
		return 0;
	}

	@Override
	public int getExceddingAmount(Exceeding exceeding) {
		int amount = 0;
		String sql = "SELECT exceeded_amount FROM exceeding WHERE exceeded_number=? AND me=? AND date=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setString(2, exceeding.getMe());
			stmt.setDate(3, Date.valueOf(exceeding.getDate()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				amount = rs.getInt(1);
				return amount;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getExceedingIDByAmount(int playNumber) {
		String sql = "SELECT exceeding_id FROM exceeding WHERE exceeded_number=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
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
	public boolean deletExceeding(Exceeding exceeding) {

		String sql = "DELETE FROM `exceeding` WHERE exceeded_number=? AND me=? AND date=? ";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setString(2, exceeding.getMe());
			stmt.setDate(3, Date.valueOf(exceeding.getDate()));
			System.out.println("Delete:" + stmt.executeUpdate());
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public int getExceedingIDOnlyWithPlayNumber(Exceeding exceeding) {
		// TODO Auto-generated method stub
		String sql = "SELECT exceeding_id FROM exceeding WHERE exceeded_number=? AND me=? AND date=? AND is_noted=1 ORDER BY exceeding_id DESC LIMIT 1;";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(exceeding.getExceedingNumber()));
			stmt.setString(2, exceeding.getMe());
			stmt.setDate(3, Date.valueOf(exceeding.getDate()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {

		}
		return 0;
	}

}
