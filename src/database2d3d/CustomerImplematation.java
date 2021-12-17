package database2d3d;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.TwoDThreeDException;
import entity.Customer;
import javafx.scene.control.Label;
import service.CustomerService;
import unit.DBConection;

public class CustomerImplematation implements CustomerService {

	@Override
	public boolean deleteCustomer(String id) {
		String sql = "DELETE FROM `customer` WHERE customer_id=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	private String generateID(boolean in) {
		String sql = "SELECT customer_id FROM customer WHERE customer_id LIKE ? ORDER BY customer_id DESC LIMIT 1";
		int id = 1;
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql);) {
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

	@Override
	public boolean insertCustomerName(String name, boolean in, LocalDate date) {
		if (name.isEmpty()) {
			throw new TwoDThreeDException("PLease enter Agent name");
		} else {
			String getID = "";
			String getIDSQL = "SELECT customer_id FROM customer WHERE customer_id LIKE ? AND name=?";
			try (Connection con = DBConection.createConnection();
					PreparedStatement stmt = con.prepareStatement(getIDSQL)) {
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
				throw new TwoDThreeDException("Agent " + name + " is already register");
			} else {
				String getCID = generateID(in);
				String sql = "INSERT INTO `customer`(`customer_id`, `name`, `date`) VALUES (?,?,?)";
				try (Connection con = DBConection.createConnection();
						PreparedStatement stmt = con.prepareStatement(sql)) {
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

	@Override
	public ArrayList<Label> getAllCustomerData(boolean in) {
		String customerNamesQuery = "SELECT name FROM `customer` WHERE customer_id LIKE ? AND name!='Special Customer' ORDER BY `name`";
		ArrayList<Label> tempNamesStored = new ArrayList<>();
		try (Connection con = DBConection.createConnection();
				PreparedStatement pre = con.prepareStatement(customerNamesQuery);) {
			pre.setString(1, in ? "I%" : "O%");
			ResultSet rs = pre.executeQuery();
			while (rs.next()) {
				Label l = new Label(rs.getString(1));
				l.setStyle(
						in ? "-fx-text-fill:black;-fx-font-weight: bold;" : "-fx-text-fill:red;-fx-font-weight: bold;");
				tempNamesStored.add(l);
			}
			return tempNamesStored;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getCustomerId(String name) {
		String id = "";
		String sql = "SELECT customer_id FROM customer where name=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				id = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public int getTotalCustomerNumber(boolean in) {
		int count = 0;
		String sql = "SELECT COUNT(*) as TotalCustomerNumber FROM customer WHERE customer_id LIKE ? AND customer_id!='O-00001'";
		try (Connection con = DBConection.createConnection(); PreparedStatement pre = con.prepareStatement(sql);) {
			pre.setString(1, in ? "I%" : "O%");
			ResultSet rs = pre.executeQuery();
			while (rs.next()) {
				count = rs.getInt("TotalCustomerNumber");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public String getCustomerIdByNumber(int playNumber, LocalDate date, String me) {
		String id = "";
		String sql = "SELECT customer.customer_id FROM customer,twod_user_play WHERE customer.customer_id=twod_user_play.customer_id AND twod_user_play.play_number=? AND twod_user_play.me=? AND twod_user_play.date=?;";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, playNumber);
			stmt.setString(2, me);
			stmt.setDate(3, Date.valueOf(date));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public String getCustomerName(String id) {
		String cid = "";
		String sql = "SELECT name FROM customer where customer_id=?";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				cid = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cid;
	}

	@Override
	public List<Customer> searchByName(String name) {
		List<Customer> list = new ArrayList<Customer>();
		String sql = "SELECT * FROM customer WHERE name LIKE ? AND customer_id!='O-00001'";
		try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, name.concat("%"));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Customer customer = new Customer();
				customer.setCustomerID(rs.getString(1));
				Label getName = new Label(rs.getString(2));
				if (rs.getString(1).contains("I")) {
					getName.setStyle("-fx-text-fill:black;-fx-font-size: 17;");
				} else {
					getName.setStyle("-fx-text-fill:red;-fx-font-size: 17;");
				}
				customer.setName(getName);

				Label getDate = new Label(rs.getString(3));
				if (rs.getString(1).contains("I")) {
					getDate.setStyle("-fx-text-fill:black;-fx-font-size: 17;");
				} else {
					getDate.setStyle("-fx-text-fill:red;-fx-font-size: 17;");
				}
				customer.setDate(getDate);
				list.add(customer);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

}
