package unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

public class Test {
	static DecimalFormat df = new DecimalFormat("###,###,###");

	public static void main(String[] args) {
		Path path = Paths.get("D:\\BB\\win number\\2021-12-15 of-winnumber-backup.csv");
		try {
			List<String> list = Files.readAllLines(path);
			for (String string : list) {
				System.out.println(PasswordEncypt.decode(string));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
