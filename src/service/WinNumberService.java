package service;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.WeekNo;

public interface WinNumberService {
	public boolean updateDataIntoTwoDUserPlayTable(WeekNo weekNo);

	public boolean insertWinNumber(WeekNo weekNo);

	public ArrayList<WeekNo> getWinNumbers(String me, LocalDate date);

	public boolean isWinListValid(String me, LocalDate date);

	public int getWinNumberForToday(String me, LocalDate date);

	public boolean deleteWithDate(LocalDate from, LocalDate to, String me);
}
