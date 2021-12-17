package service;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.OutSource;

public interface OutSourceService {
	public boolean insertIntoOutSourcetable(OutSource outSource);

	public boolean updateIntoOutSourcetable(int execeedingID, int currentAmount);

	public ArrayList<OutSource> getOutSourceData(String name, LocalDate date, String morning);

	public int getTotal(String name);

	public int getTotalSumOfExceedAmount(int exceedID);

	public ArrayList<OutSource> getOutsourcesData(int playNumber, String me, LocalDate date);

	public int getTotalExceedAmount(int playNumber);

	public int deleteOutSource(int execeedingID, String customerID);
	
	public int deleteOutSource(int outID);
	
	public int getOutsourceID(String cID, int exeID);
	
	public int isValidExceedID(String customerID, int playNumber, String me, LocalDate date);
	
	public boolean updateCurrentAmount(String customerID, int exceedingNumber, String me, LocalDate date,
			int previousCurrentAmount, int nowCurrentAmount);
}
