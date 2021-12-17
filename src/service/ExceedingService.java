package service;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Exceeding;

public interface ExceedingService {
	public boolean insert(Exceeding exceeding);

	public ArrayList<Exceeding> getExceedingList(Exceeding exceeding);

	public int getTotalExceedAmount(String me, LocalDate date);

	public boolean updateAmount(Exceeding exceeding);

	public boolean updateNoted(Exceeding exceeding, int i);

	public int isExceed(Exceeding exceeding);

	public int getExceedingID(Exceeding exceeding);

	public int getExceddingAmount(Exceeding exceeding);

	public int getExceedingIDByAmount(int playNumber);

	public boolean deletExceeding(Exceeding exceeding);

	public int getExceedingIDOnlyWithPlayNumber(Exceeding exceeding);
	
	public boolean deleteWithDate(LocalDate from, LocalDate to, String me);

}
