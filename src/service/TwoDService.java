package service;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.TwoDEntity;

public interface TwoDService {
	public boolean insertDataIntoTwoDUserPlayTable(String customerID, TwoDEntity twoDEntity);

	public boolean updateDataIntoTwoDUserPlayTable(String customerID, TwoDEntity twoDEntity);

	public boolean updateExtract_amount(TwoDEntity twoDEntity);

	public boolean updateExtract_amountZero(TwoDEntity twoDEntity);

	public int toShowAmount(int checkNum, String me, LocalDate date);

	public ArrayList<TwoDEntity> getDetails(TwoDEntity twoDEntity);

	public int isAlreadyLottery(TwoDEntity twoDEntity);

	public int isAlreadyLotteryNumber(TwoDEntity twoDEntity);

	public boolean isExtractAmountZero(int playNumber, int setAmount);

	public int getExtractAmount(int playNumber, String me, LocalDate date);

	public ArrayList<TwoDEntity> getAllData(int playAmt, String me, LocalDate date);

	public String getCustomerID(TwoDEntity dEntity);
	
	public boolean deleteWithDate(LocalDate from, LocalDate to, String me);
	
	public LocalDate getFirstLineDate();
	}
