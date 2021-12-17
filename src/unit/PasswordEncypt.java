package unit;

import java.util.Base64;

public class PasswordEncypt {
	public static void main(String[] args) {
		String password = "iwkalocuse";
		String decode = encode(password);
		System.out.println("Encrypt:" + decode);
		System.out.println("Decode:" + decode(decode));
	}

	public static String encode(String password) {
		return Base64.getEncoder().encodeToString(password.getBytes());
	}

	public static String decode(String encrypt) {
		return new String(Base64.getDecoder().decode(encrypt));
	}

}
