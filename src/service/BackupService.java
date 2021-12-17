package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface BackupService {
	static final String CUSTOMER_FOLDER_NAME = "customer";
	static final String TWOD_THREED_FOLDER_NAME = "twoD threeD";
	static final String WIN_NUMBER_FOLDER_NAME = "win number";
	static final String EXCEEDING_FOLDER_NAME = "exceeding";
	static final String OUTSOURCE_FOLDER_NAME = "outsource";

	public boolean customerBackup(File file);

	public boolean twoDThreeDBackup(File file);

	public boolean exceedingBackup(File file);

	public boolean outSourceBackup(File file);

	public boolean winNumBackup(File file);

	public static void buildFile(File file) {

		Path customerPath = Paths.get(file.getAbsolutePath().concat("//").concat(CUSTOMER_FOLDER_NAME));
		Path twoDThreeDPath = Paths.get(file.getAbsolutePath().concat("//").concat(TWOD_THREED_FOLDER_NAME));
		Path winNumberPath = Paths.get(file.getAbsolutePath().concat("//").concat(WIN_NUMBER_FOLDER_NAME));
		Path exceedingPath = Paths.get(file.getAbsolutePath().concat("//").concat(EXCEEDING_FOLDER_NAME));
		Path outSourcePath = Paths.get(file.getAbsolutePath().concat("//").concat(OUTSOURCE_FOLDER_NAME));

		if (!Files.exists(customerPath)) {
			try {
				Files.createDirectories(customerPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!Files.exists(twoDThreeDPath)) {
			try {
				Files.createDirectories(twoDThreeDPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!Files.exists(winNumberPath)) {
			try {
				Files.createDirectories(winNumberPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!Files.exists(exceedingPath)) {
			try {
				Files.createDirectories(exceedingPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!Files.exists(outSourcePath)) {
			try {
				Files.createDirectories(outSourcePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}