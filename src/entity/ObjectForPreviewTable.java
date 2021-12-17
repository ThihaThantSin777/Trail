package entity;

public class ObjectForPreviewTable {
	private String playNumber;
	private int balance;

	public ObjectForPreviewTable(String playNumber, int balance) {
		super();
		this.playNumber = playNumber;
		this.balance = balance;
	}

	public String getPlayNumber() {
		return playNumber;
	}

	public void setPlayNumber(String playNumber) {
		this.playNumber = playNumber;
	}

	public int getBalance() {
		return balance;
	}

	@Override
	public String toString() {
		return "ObjectForPreviewTable [playNumber=" + playNumber + "]";
	}

}
