package entity;

public class ObjectForCurrentTable {
	private String playNumber;
	private int amount;
	
	public ObjectForCurrentTable(String playNumber, int amount) {
		this.playNumber = playNumber;
		this.amount = amount;
	}

	public String getPlayNumber() {
		return playNumber;
	}

	public void setPlayNumber(String playNumber) {
		this.playNumber = playNumber;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "ObjectForCurrentTable [playNumber=" + playNumber + ", amount=" + amount + "]";
	}
	
}
