package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Customer;
import javafx.scene.control.Label;

public interface CustomerService {
	public boolean insertCustomerName(String name, boolean in, LocalDate date);

	public ArrayList<Label> getAllCustomerData(boolean in);

	public String getCustomerId(String name);

	public String getCustomerName(String id);

	public String getCustomerIdByNumber(int playNumber, LocalDate date, String me);

	public int getTotalCustomerNumber(boolean in);

	public List<Customer> searchByName(String name);

	public boolean deleteCustomer(String id);

}
