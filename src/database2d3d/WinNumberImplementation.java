package database2d3d;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.WeekNo;
import service.WinNumberService;
import unit.DBConection;

public class WinNumberImplementation implements WinNumberService {
	@Override
	public boolean deleteWithDate(LocalDate from, LocalDate to, String me) {
		if (me.equals("3D")) {
			String sql = "DELETE FROM  win_num WHERE me=?";
			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, me);
				stmt.executeUpdate();
				return true;
			} catch (Exception e) {
				e.getStackTrace();
			}
		} else {
			String sql = "DELETE FROM  win_num WHERE me=? AND date BETWEEN ? AND ?";

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
	public boolean updateDataIntoTwoDUserPlayTable(WeekNo weekNo) {
		String sql = "UPDATE `win_num` SET `win_number`=? WHERE win_num.me=? AND win_num.date=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(weekNo.getWinNo()));
			stmt.setString(2, weekNo.getMe());
			stmt.setDate(3, Date.valueOf(weekNo.getDate()));
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public String get1to5(LocalDate now) {
		LocalDate date = now;
		int value = date.getDayOfWeek().getValue();
		LocalDate start = null;
		LocalDate end = null;
		switch (value) {
		case 1:
			start = date;
			end = date.plusDays(4);
			break;
		case 2:
			start = date.minusDays(1);
			end = date.plusDays(3);
			break;
		case 3:
			start = date.minusDays(2);
			end = date.plusDays(2);
			break;
		case 4:
			start = date.minusDays(3);
			end = date.plusDays(1);
			break;
		case 5:
			start = date.minusDays(4);
			end = date;
			break;

		default:
			break;
		}
		return "SELECT day_of_week,win_number FROM win_num WHERE me=? AND date BETWEEN " + "'" + start + "'" + " AND "
				+ "'" + end + "'" + " ORDER BY date";
	}

	@Override
	public boolean insertWinNumber(WeekNo weekNo) {
		String sql = "INSERT INTO `win_num`( `win_number`, `day_of_week`, `date`, `me`) VALUES (?,?,?,?)";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, Integer.parseInt(weekNo.getWinNo()));
			stmt.setString(2, weekNo.getDayOfWeek().toString().substring(0, 3));
			stmt.setDate(3, Date.valueOf(weekNo.getDate()));
			stmt.setString(4, weekNo.getMe());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public ArrayList<WeekNo> getWinNumbers(String me, LocalDate date) {

		ArrayList<WeekNo> list = new ArrayList<WeekNo>();
		String sql = get1to5(date);
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, me);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				WeekNo weekNo = new WeekNo(rs.getString(1), rs.getInt(2));
				list.add(weekNo);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public boolean isWinListValid(String me, LocalDate date) {
		String sql = "SELECT day_of_week FROM win_num WHERE me=? AND date=?";

		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, me);
			stmt.setDate(2, Date.valueOf(date));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public int getWinNumberForToday(String me, LocalDate date) {
		String sql = "SELECT win_number FROM win_num WHERE me=? AND date=?";

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
		return -1;
	}

}
