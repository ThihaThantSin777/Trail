package database2d3d;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.TwoDEntity;
import service.TwoDService;
import unit.DBConection;

public class TwoDImplementation implements TwoDService {
	@Override
	public LocalDate getFirstLineDate() {
		String sql = "SELECT date FROM twod_user_play ORDER BY date LIMIT 1";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getDate(1).toLocalDate();
			}
		} catch (Exception e) {
		}
		return LocalDate.now();
	}

	@Override
	public boolean deleteWithDate(LocalDate from, LocalDate to, String me) {
		if (me.equals("3D")) {
			String sql = "DELETE FROM twod_user_play WHERE me=?";
			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, me);
				stmt.executeUpdate();
			} catch (Exception e) {
				e.getStackTrace();
			}

			String sql2 = "DELETE FROM twod_user_play_for_cur_table WHERE me=?";
			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql2)) {
				stmt.setString(1, me);
				stmt.executeUpdate();
				return true;
			} catch (Exception e) {
				e.getStackTrace();
			}
		} else {
			String sql = "DELETE FROM twod_user_play WHERE me=? and date BETWEEN ? AND ?";
			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, me);
				stmt.setDate(2, Date.valueOf(from));
				stmt.setDate(3, Date.valueOf(to));
				stmt.executeUpdate();
			} catch (Exception e) {
				e.getStackTrace();
			}
			String sql2 = "DELETE FROM twod_user_play_for_cur_table WHERE me=? and date BETWEEN ? AND ?";
			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql2)) {
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
	public boolean insertDataIntoTwoDUserPlayTable(String customerID, TwoDEntity twoDEntity) {

		String sql = "INSERT INTO `twod_user_play`(`customer_id`, `play_number`, `amount`, `me`, `date`, `extract_amount`) VALUES (?,?,?,?,?,?)";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, customerID);
			stmt.setInt(2, twoDEntity.getPlayNumber());
			stmt.setInt(3, twoDEntity.getAmount());
			stmt.setString(4, twoDEntity.getMe());
			stmt.setDate(5, Date.valueOf(twoDEntity.getDate()));
			stmt.setInt(6, twoDEntity.getExtract_amount());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public int toShowAmount(int checkNum, String me, LocalDate date) {
		String totalAmountForSpecificNum = "SELECT SUM(amount),extract_amount FROM twod_user_play WHERE play_number = ? AND me=? AND date=?";

		try (Connection con = DBConection.createConnection();
				PreparedStatement pre = con.prepareStatement(totalAmountForSpecificNum);) {
			pre.setInt(1, checkNum);
			pre.setString(2, me);
			pre.setDate(3, Date.valueOf(date));
			ResultSet rs = pre.executeQuery();
			if (rs.next()) {
				int temp = rs.getInt(1) - rs.getInt(2);
				return temp;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public ArrayList<TwoDEntity> getDetails(TwoDEntity twoDEntity) {
		ArrayList<TwoDEntity> list = new ArrayList<TwoDEntity>();
		String sql = "SELECT customer.name,SUM(twod_user_play.amount) FROM customer,twod_user_play WHERE customer.customer_id=twod_user_play.customer_id AND twod_user_play.play_number=? AND twod_user_play.date=? AND me=? AND twod_user_play.amount>0  AND customer.customer_id!='O-00001' GROUP BY customer.name";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, twoDEntity.getPlayNumber());
			stmt.setDate(2, Date.valueOf(twoDEntity.getDate()));
			stmt.setString(3, twoDEntity.getMe());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TwoDEntity dEntity = new TwoDEntity(rs.getString(1), rs.getInt(2));
				list.add(dEntity);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return list;
	}

	@Override
	public int isAlreadyLottery(TwoDEntity twoDEntity) {
		String sql = "SELECT twod_user_play.amount FROM customer,twod_user_play WHERE customer.customer_id=twod_user_play.customer_id AND customer.name=? AND twod_user_play.me=? AND twod_user_play.date=? AND play_number=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, twoDEntity.getCustomerName());
			stmt.setString(2, twoDEntity.getMe());
			stmt.setDate(3, Date.valueOf(twoDEntity.getDate()));
			stmt.setInt(4, twoDEntity.getPlayNumber());
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
	public boolean updateDataIntoTwoDUserPlayTable(String customerID, TwoDEntity twoDEntity) {
		String sql = "UPDATE `twod_user_play` SET `amount`=? WHERE customer_id=? AND me=? AND date=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, twoDEntity.getAmount());
			stmt.setString(2, customerID);
			stmt.setString(3, twoDEntity.getMe());
			stmt.setDate(4, Date.valueOf(twoDEntity.getDate()));
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean updateExtract_amount(TwoDEntity twoDEntity) {
		int previousExtractAmount = 0;
		String getAmount = "SELECT `extract_amount` FROM twod_user_play WHERE me=? AND date=? AND play_number=?";
		try (Connection con = DBConection.createConnection();
				PreparedStatement stmt = con.prepareStatement(getAmount)) {
			stmt.setString(1, twoDEntity.getMe());
			stmt.setDate(2, Date.valueOf(twoDEntity.getDate()));
			stmt.setInt(3, twoDEntity.getPlayNumber());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				previousExtractAmount = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		String sql = "UPDATE `twod_user_play` SET `extract_amount`=? WHERE me=? AND date=? AND play_number=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, twoDEntity.getExtract_amount() + previousExtractAmount);
			stmt.setString(2, twoDEntity.getMe());
			stmt.setDate(3, Date.valueOf(twoDEntity.getDate()));
			stmt.setInt(4, twoDEntity.getPlayNumber());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public boolean isExtractAmountZero(int playNumber, int setAmount) {
		boolean cond = false;
		String sql = "SELECT SUM(amount) FROM twod_user_play WHERE play_number=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, playNumber);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) < setAmount) {
					cond = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cond;
	}

	@Override
	public int getExtractAmount(int playNumber, String me, LocalDate date) {
		String sql = "SELECT extract_amount FROM twod_user_play WHERE play_number=? AND me=? AND date=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, playNumber);
			stmt.setString(2, me);
			stmt.setDate(3, Date.valueOf(date));
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
	public ArrayList<TwoDEntity> getAllData(int playAmt, String me, LocalDate date) {
		ArrayList<TwoDEntity> list = new ArrayList<TwoDEntity>();
		String sql = "SELECT SUM(amount),play_number,extract_amount FROM twod_user_play WHERE me=? AND date=? GROUP BY play_number ";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, me);
			stmt.setDate(2, Date.valueOf(date));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int temp = rs.getInt(1) - rs.getInt(3);

				if (temp > playAmt) {
					int result = temp - playAmt;
					list.add(new TwoDEntity(result, rs.getInt(2)));
				}
//				if (temp > playAmt) {
//					int result = temp - playAmt;
//					list.add(new TwoDEntity(result, rs.getInt(2)));
//				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(new TwoDImplementation().toShowAmount(91, "Morning", LocalDate.now()));
	}

	@Override
	public boolean updateExtract_amountZero(TwoDEntity twoDEntity) {
		String sql = "UPDATE `twod_user_play` SET `extract_amount`=? WHERE me=? AND date=? AND play_number=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, twoDEntity.getExtract_amount());
			stmt.setString(2, twoDEntity.getMe());
			stmt.setDate(3, Date.valueOf(twoDEntity.getDate()));
			stmt.setInt(4, twoDEntity.getPlayNumber());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public String getCustomerID(TwoDEntity dEntity) {
		String sql = "SELECT customer.customer_id FROM customer,twod_user_play WHERE twod_user_play.customer_id=customer.customer_id AND twod_user_play.play_number=? AND twod_user_play.me=? AND twod_user_play.date=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, dEntity.getPlayNumber());
			stmt.setString(2, dEntity.getMe());
			stmt.setDate(3, Date.valueOf(dEntity.getDate()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public int isAlreadyLotteryNumber(TwoDEntity twoDEntity) {
		String sql = "SELECT twod_user_play.amount FROM customer,twod_user_play WHERE customer.customer_id=twod_user_play.customer_id AND twod_user_play.me=? AND twod_user_play.date=? AND play_number=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, twoDEntity.getMe());
			stmt.setDate(2, Date.valueOf(twoDEntity.getDate()));
			stmt.setInt(3, twoDEntity.getPlayNumber());
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
