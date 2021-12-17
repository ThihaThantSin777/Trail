package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXDatePicker;

import application.TwoDThreeDException;
import database2d3d.WinNumberImplementation;
import entity.Summary;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import service.WinNumberService;
import unit.AlertShow;
import unit.DBConection;
import unit.DataPass;

public class SummaryController implements Initializable {
	@FXML
	private VBox vb;
	@FXML
	private JFXDatePicker date;
	@FXML
	private TextField gamePercentage;
	@FXML
	private TableView<Summary> tblview;
	@FXML
	private Label showGameNo;
	@FXML
	private Label totalAmount;
	@FXML
	private Label totalGameAmount;
	@FXML
	private Label totalNetAmount;
	@FXML
	private TableColumn<Summary, String> colName;

	DecimalFormat df = new DecimalFormat("###,###,###");
	WinNumberService winNumberService;
	String morE = DataPass.getMeDataPass().equals("Morning") ? "Morning"
			: DataPass.getMeDataPass().equals("Evening") ? "Evening" : "3D";

	private ArrayList<String> getKhway() {
		String khwayNumFor3D = showGameNo.getText();
		String[] khwayNumArrFor3D = khwayNumFor3D.split("");
		String storeGet0FromEveryNum = "0";
		String strPlayNumber = "312";
		ArrayList<String> listForEveryNum = new ArrayList<String>();
		ArrayList<String> listForRemovedItems = new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
//	boolean firstTimeController=true;
		if ((khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(1) && khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(2)
				&& khwayNumFor3D.charAt(1) != khwayNumFor3D.charAt(2))
				&& (storeGet0FromEveryNum.equals(strPlayNumber) || storeGet0FromEveryNum.equals("0")
						|| !listForEveryNum.contains(strPlayNumber) || listForRemovedItems.contains(strPlayNumber))) {
			list.clear();
			if (!listForEveryNum.isEmpty()) {
				storeGet0FromEveryNum = listForEveryNum.get(0);
			}
			listForEveryNum.clear();
			// firstTimeController = false;
			for (int i = 0; i < khwayNumArrFor3D.length; i++) {
				for (int j = 0; j < khwayNumArrFor3D.length; j++) {
					for (int k = 0; k < khwayNumArrFor3D.length; k++) {
						String formulatedNum = khwayNumArrFor3D[i].concat(khwayNumArrFor3D[j])
								.concat(khwayNumArrFor3D[k]);

						if (!khwayNumArrFor3D[i].equals(khwayNumArrFor3D[j])
								&& !khwayNumArrFor3D[j].equals(khwayNumArrFor3D[k])
								&& !khwayNumArrFor3D[i].equals(khwayNumArrFor3D[k])) {
							listForEveryNum.add(formulatedNum);
						}
						if (khwayNumArrFor3D[i].equals(khwayNumArrFor3D[j])
								|| khwayNumArrFor3D[j].equals(khwayNumArrFor3D[k])
								|| khwayNumArrFor3D[i].equals(khwayNumArrFor3D[k])
								|| formulatedNum.equals(khwayNumFor3D)) {
							continue;
						}
						list.add(formulatedNum);
					}
				}
			}

		} else if (((khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1)
				|| khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(2)
				|| khwayNumFor3D.charAt(1) != khwayNumFor3D.charAt(2)))
				&& (storeGet0FromEveryNum.equals(strPlayNumber) || storeGet0FromEveryNum.equals("0")
						|| !listForEveryNum.contains(strPlayNumber) || listForRemovedItems.contains(strPlayNumber))) {
			list.clear();
			if (!listForEveryNum.isEmpty()) {
				storeGet0FromEveryNum = listForEveryNum.get(0);
			}
			listForEveryNum.clear();
			// firstTimeController = false;
			for (int i = 0; i < khwayNumArrFor3D.length; i++) {
				for (int j = 0; j < khwayNumArrFor3D.length; j++) {
					aa: for (int k = 0; k < khwayNumArrFor3D.length; k++) {
						String formulatedNum = khwayNumArrFor3D[i].concat(khwayNumArrFor3D[j])
								.concat(khwayNumArrFor3D[k]);

						String sameThree = "";
						for (int a = 0; a < khwayNumFor3D.length(); a++) {

							sameThree = String.valueOf(khwayNumFor3D.charAt(a))
									+ String.valueOf(khwayNumFor3D.charAt(a)) + String.valueOf(khwayNumFor3D.charAt(a));

							listForEveryNum.add(formulatedNum);
							if (sameThree.equals(formulatedNum) || formulatedNum.equals(khwayNumFor3D)) {
								continue aa;
							}
						}
						list.add(formulatedNum);

					}
				}
			}
			Set<String> set = new LinkedHashSet<>(list);
			Set<String> setForEveryNum = new LinkedHashSet<>(listForEveryNum);

			list.clear();
			list.addAll(set);

			listForEveryNum.clear();
			listForEveryNum.addAll(setForEveryNum);
			if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1)
					|| khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)) {
				list.remove(list.size() - 4);
				list.remove(list.size() - 2);
				list.remove(list.size() - 1);

				listForEveryNum.remove(0);
				listForEveryNum.remove(listForEveryNum.size() - 5);
				listForEveryNum.remove(listForEveryNum.size() - 3);
				listForEveryNum.remove(listForEveryNum.size() - 2);
				listForEveryNum.remove(listForEveryNum.size() - 1);
			} else if (khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
				list.remove(list.size() - 5);
				list.remove(list.size() - 4);
				list.remove(list.size() - 3);

				listForEveryNum.remove(0);
				listForEveryNum.remove(listForEveryNum.size() - 7);
				listForEveryNum.remove(listForEveryNum.size() - 6);
				listForEveryNum.remove(listForEveryNum.size() - 4);
				listForEveryNum.remove(listForEveryNum.size() - 1);
			}
		}
		return list;
	}

	public void printNode(Node nodeToPrint) {
		List<Summary> list = tblview.getItems();
		File f = null;
		try {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
			fc.setInitialFileName(LocalDate.now().format(DateTimeFormatter.ofPattern("E-MMM-dd-yyyy")) + "-"
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh-mm-ss-a")) + ".pdf");
			f = fc.showSaveDialog(null);
			Document document = new Document();

			PdfPTable table = new PdfPTable(8);
			table.setWidthPercentage(100);
//			table.setWidths(new int[] { 4, 6, 5, 5, 5, 5, 6 });

			PdfWriter.getInstance(document, new FileOutputStream(f));
			document.open();
			Phrase p = new Phrase("    " + LocalDate.now().format(DateTimeFormatter.ofPattern("E,MMM-dd-yyyy")) + ","
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss:a")) + " of Summary Report");

			document.add(p);
			PdfPCell c1 = new PdfPCell(new Phrase("Agent ID"));
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Agent Name"));
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Game No"));
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Total Amount"));
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Game Total Amount"));
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Net Total Amount"));
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Round"));
			table.addCell(c1);

			c1 = new PdfPCell(new Phrase("Date"));
			table.addCell(c1);

			for (Summary summary : list) {
				Font red = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
				Font black = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				PdfPCell cID = new PdfPCell(new Phrase(summary.getCustomerID().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));
				PdfPCell cName = new PdfPCell(new Phrase(summary.getCustomerName().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));
				PdfPCell cGameNo = new PdfPCell(new Phrase(summary.getGameNo().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));
				PdfPCell cTotal = new PdfPCell(new Phrase(summary.getTotalAmt().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));
				PdfPCell cGameAMt = new PdfPCell(new Phrase(summary.getGameAmt().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));
				PdfPCell cNet = new PdfPCell(new Phrase(summary.getNetTotal().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));

				PdfPCell round = new PdfPCell(new Phrase(summary.getRound().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));
				PdfPCell cDate = new PdfPCell(new Phrase(summary.getDate().getText(),
						summary.getCustomerID().getText().contains("O") ? red : black));

				table.addCell(cID);
				table.addCell(cName);
				table.addCell(cGameNo);
				table.addCell(cTotal);
				table.addCell(cGameAMt);
				table.addCell(cNet);
				table.addCell(round);
				table.addCell(cDate);

			}
			document.add(table);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AlertShow.showInfo("PDF create success\nLocation:" + f.getAbsolutePath());

	}

	public List<Summary> loadData() {
		List<Summary> list = new ArrayList<Summary>();

		if (!gamePercentage.getText().isEmpty()) {
			String sql = "SELECT customer.customer_id,customer.name,twod_user_play.play_number,SUM(twod_user_play.amount),twod_user_play.date FROM customer LEFT JOIN twod_user_play ON customer.customer_id=twod_user_play.customer_id WHERE twod_user_play.me=? AND twod_user_play.date=? GROUP BY customer.customer_id;";

			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, morE);
				stmt.setDate(2, Date.valueOf(date.getValue()));
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {

					Summary summary = new Summary();
					summary.setCustomerID(new Label(rs.getString(1)));
					summary.setCustomerName(new Label(rs.getString(2)));
					summary.setGameNo(new Label(showGameNo.getText()));
					summary.setTotalAmt(new Label(String.valueOf(rs.getInt(4))));
					summary.setDate(new Label(LocalDate.parse(rs.getString(5)).toString()));
					summary.setGameAmt(new Label("0"));
					summary.setNetTotal(new Label(String.valueOf(rs.getInt(4))));
					summary.setRound(new Label("0"));
					list.add(summary);

				}
			} catch (Exception e) {
				System.err.println("1:" + e.getMessage());
			}

			String sql2 = "SELECT customer.customer_id,customer.name,twod_user_play.play_number,SUM(twod_user_play.amount),twod_user_play.date FROM customer LEFT JOIN twod_user_play ON customer.customer_id=twod_user_play.customer_id WHERE twod_user_play.me=? AND twod_user_play.date=? AND twod_user_play.play_number=? GROUP BY customer.customer_id;";

			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql2)) {
				stmt.setString(1, morE);
				stmt.setDate(2, Date.valueOf(date.getValue()));
				stmt.setInt(3, Integer.parseInt(showGameNo.getText()));
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					for (Summary summary : list) {
						if (summary.getCustomerID().getText().equals(rs.getString(1))) {
							summary.setGameAmt(new Label(String.valueOf(rs.getInt(4))));

						}
						if (summary.getGameAmt().getText().equals("0")) {
							summary.setNetTotal(new Label(summary.getTotalAmt().getText()));
						} else {
							summary.setNetTotal(new Label(String.valueOf((Integer.parseInt(gamePercentage.getText())
									* Integer.parseInt(summary.getGameAmt().getText()))
									- Integer.parseInt(summary.getTotalAmt().getText()))));
						}
					}
				}
			} catch (Exception e) {
				System.err.println("2:" + e.getMessage());
			}

			String sql3 = "SELECT customer.customer_id,customer.name,exceeding.exceeded_number,SUM(outsource.currentAmount),exceeding.date FROM customer JOIN outsource ON outsource.customer_id=customer.customer_id JOIN exceeding ON exceeding.exceeding_id=outsource.exceeding_id WHERE exceeding.me=? AND customer.customer_id!='O-00001' AND exceeding.date=? GROUP BY customer.customer_id;";

			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql3)) {
				stmt.setString(1, morE);
				stmt.setDate(2, Date.valueOf(date.getValue()));
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Summary summary = new Summary();
					summary.setCustomerID(new Label(rs.getString(1)));
					summary.setCustomerName(new Label(rs.getString(2)));
					summary.setGameNo(new Label(showGameNo.getText()));
					summary.setTotalAmt(new Label(String.valueOf(rs.getInt(4))));
					summary.setDate(new Label(LocalDate.parse(rs.getString(5)).toString()));
					summary.setGameAmt(new Label("0"));
					summary.setNetTotal(new Label(String.valueOf(rs.getInt(4))));
					summary.setRound(new Label("0"));
					list.add(summary);

				}

			} catch (Exception e) {
				System.err.println("3:" + e.getMessage());
			}

			String sql4 = "SELECT customer.customer_id,customer.name,exceeding.exceeded_number,SUM(outsource.currentAmount),exceeding.date FROM customer JOIN outsource ON outsource.customer_id=customer.customer_id JOIN exceeding ON exceeding.exceeding_id=outsource.exceeding_id WHERE exceeding.exceeded_number=? AND customer.customer_id!='O-00001' AND exceeding.me=? AND exceeding.date=? GROUP BY outsource.customer_id;";

			try (Connection con = DBConection.createConnection(); PreparedStatement stmt = con.prepareStatement(sql4)) {
				stmt.setInt(1, Integer.parseInt(showGameNo.getText()));
				stmt.setString(2, morE);
				stmt.setDate(3, Date.valueOf(date.getValue()));

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					for (Summary summary : list) {

						if (summary.getCustomerID().getText().equals(rs.getString(1))) {
							summary.setGameAmt(new Label(String.valueOf(rs.getInt(4))));
						}
						if (summary.getGameAmt().getText().equals("0")) {
							summary.setNetTotal(new Label(summary.getTotalAmt().getText()));
						} else {
							summary.setNetTotal(new Label(String.valueOf((Integer.parseInt(gamePercentage.getText())
									* Integer.parseInt(summary.getGameAmt().getText()))
									- Integer.parseInt(summary.getTotalAmt().getText()))));
						}

					}
				}

			} catch (Exception e) {
				System.err.println("4:" + e.getMessage());
			}
		}

		if (DataPass.getMeDataPass().equals("3D")) {
			ArrayList<String> listKhway = getKhway();
			int firstMinus = Integer.parseInt(showGameNo.getText()) - 1;
			int secondPlus = Integer.parseInt(showGameNo.getText()) + 1;
			String formatValue1 = String.format("%03d", firstMinus);
			String formatValue2 = String.format("%03d", secondPlus);
			listKhway.add(formatValue1);
			listKhway.add(formatValue2);
			String sql = "SELECT customer.customer_id,customer.name,twod_user_play.play_number,SUM(twod_user_play.amount),twod_user_play.date FROM customer LEFT JOIN twod_user_play ON customer.customer_id=twod_user_play.customer_id WHERE twod_user_play.me='3D' AND twod_user_play.date=? AND twod_user_play.play_number=? GROUP BY customer.customer_id;";
			for (String string : listKhway) {
				try (Connection con = DBConection.createConnection();
						PreparedStatement stmt = con.prepareStatement(sql)) {
					stmt.setDate(1, Date.valueOf(date.getValue()));
					stmt.setInt(2, Integer.parseInt(string));
					ResultSet rs = stmt.executeQuery();

					while (rs.next()) {
						for (Summary summary : list) {
							if (summary.getCustomerID().getText().equals(rs.getString(1))) {
								int temp = Integer.parseInt(summary.getRound().getText());
								temp += rs.getInt(4);
								summary.setRound(new Label(String.valueOf(temp)));
							}
						}
					}

				} catch (Exception e) {

				}
			}

			String sql2 = "SELECT customer.customer_id,customer.name,exceeding.exceeded_number,SUM(outsource.currentAmount),exceeding.date FROM customer JOIN outsource ON outsource.customer_id=customer.customer_id JOIN exceeding ON exceeding.exceeding_id=outsource.exceeding_id WHERE exceeding.exceeded_number=? AND customer.customer_id!='O-00001' AND exceeding.me='3D' AND exceeding.date=? GROUP BY outsource.customer_id;";
			for (String string : listKhway) {
				try (Connection con = DBConection.createConnection();
						PreparedStatement stmt = con.prepareStatement(sql2)) {

					stmt.setInt(1, Integer.parseInt(string));
					stmt.setDate(2, Date.valueOf(date.getValue()));
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						for (Summary summary : list) {

							System.out.println(summary);
							if (summary.getCustomerID().getText().equals(rs.getString(1))) {
								int temp = Integer.parseInt(summary.getRound().getText());
								System.out.println("Get 4" + rs.getInt(4));
								temp += rs.getInt(4);
								System.out.println("temp:" + temp);
								summary.setRound(new Label(String.valueOf(temp)));
							}
						}
					}

				} catch (Exception e) {
					System.err.print(e.getMessage());
				}
			}
		}
		return list.stream().filter(e -> !e.getCustomerID().getText().equals("O-00001")).collect(Collectors.toList());
	}

	public List<Summary> changeColor(List<Summary> list) {
		List<Summary> result = new ArrayList<Summary>();
		for (Summary summary : list) {
			if (summary.getCustomerID().getText().contains("O")) {
				summary.getCustomerID().setStyle("-fx-text-fill:red;");
				summary.getCustomerName().setStyle("-fx-text-fill:red;");
				summary.getTotalAmt().setStyle("-fx-text-fill:red;");
				summary.getGameAmt().setStyle("-fx-text-fill:red;");
				summary.getNetTotal().setStyle("-fx-text-fill:red;");
				summary.getGameNo().setStyle("-fx-text-fill:red;");
				summary.getDate().setStyle("-fx-text-fill:red;");
				summary.getRound().setStyle("-fx-text-fill:red;");
			} else {
				summary.getCustomerID().setStyle("-fx-text-fill:black;");
				summary.getCustomerName().setStyle("-fx-text-fill:black;");
				summary.getTotalAmt().setStyle("-fx-text-fill:black;");
				summary.getGameAmt().setStyle("-fx-text-fill:black;");
				summary.getNetTotal().setStyle("-fx-text-fill:black;");
				summary.getGameNo().setStyle("-fx-text-fill:black;");
				summary.getDate().setStyle("-fx-text-fill:black;");
				summary.getRound().setStyle("-fx-text-fill:black;");
			}
			result.add(summary);
		}
		return result;
	}

	public void close() {
		tblview.getScene().getWindow().hide();
	}

	public void print() {
		List<Summary> list = tblview.getItems();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		fileChooser.setInitialFileName(LocalDate.now().format(DateTimeFormatter.ofPattern("E-MMM-dd-yyyy")) + "-"
				+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh-mm-ss-a")) + ".csv");
		File file = fileChooser.showSaveDialog(showGameNo.getScene().getWindow());
		if (file != null) {
			fileChooser.setInitialDirectory(file);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String title = "Agent ID,Agent Name,Game No,Total Amount,Game Total Amount,Net Total Amount,Date,Round,\n";
			try {
				Files.write(Paths.get(file.getAbsolutePath()), title.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (Summary summary : list) {

				try {
					Files.write(Paths.get(file.getAbsolutePath()), summary.toString().getBytes(),
							StandardOpenOption.APPEND);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			AlertShow.showInfo("File create success\nLocation:" + file.getAbsolutePath());

			printNode(tblview);
		}

	}

	private void checkError() {
		if (gamePercentage.getText().isEmpty()) {
			tblview.getItems().clear();
		} else {
			try {
				Integer.parseInt(gamePercentage.getText());
			} catch (NumberFormatException e) {
				throw new TwoDThreeDException("Please enter game percentage in digit");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		winNumberService = new WinNumberImplementation();
		if (DataPass.getMeDataPass().equals("3D")) {
			gamePercentage.setText("500");
		} else {
			gamePercentage.setText("80");
		}
		date.setValue(DataPass.getDate());
		showGameNo.setText(DataPass.getGameNo());

		tblview.getItems().addAll(changeColor(loadData()));
		date.setDayCellFactory(dayCellFactory);
		List<Summary> list = tblview.getItems();
		gamePercentage.textProperty().addListener((a, b, c) -> {

			try {

				checkError();
				tblview.getItems().clear();
				tblview.getItems().addAll(changeColor(loadData()));
				int totalAmt = 0;
				int totalGameAmt = 0;
				int totalNetAmt = 0;
				for (Summary summary : list) {
					totalAmt += Integer.parseInt(summary.getTotalAmt().getText());
					totalGameAmt += Integer.parseInt(summary.getGameAmt().getText());
					totalNetAmt += Integer.parseInt(summary.getNetTotal().getText());
				}
				totalAmount.setText(df.format(totalAmt) + " MMK");
				totalGameAmount.setText(df.format(totalGameAmt) + " MMK");
				totalNetAmount.setText(df.format(totalNetAmt) + " MMK");
			} catch (Exception e) {
				AlertShow.showError(e.getMessage());
			}
		});
		date.valueProperty().addListener((a, b, c) -> {
			try {

				checkError();
				showGameNo.setText(String.valueOf(winNumberService.getWinNumberForToday(morE, c)));
				tblview.getItems().clear();
				tblview.getItems().addAll(changeColor(loadData()));
				int totalAmt = 0;
				int totalGameAmt = 0;
				int totalNetAmt = 0;

				for (Summary summary : list) {
					totalAmt += Integer.parseInt(summary.getTotalAmt().getText());
					totalGameAmt += Integer.parseInt(summary.getGameAmt().getText());
					totalNetAmt += Integer.parseInt(summary.getNetTotal().getText());
				}
				totalAmount.setText(df.format(totalAmt) + " MMK");
				totalGameAmount.setText(df.format(totalGameAmt) + " MMK");
				totalNetAmount.setText(df.format(totalNetAmt) + " MMK");
			} catch (Exception e) {
				AlertShow.showError(e.getMessage());
			}
		});
		int totalAmt = 0;
		int totalGameAmt = 0;
		int totalNetAmt = 0;
		for (Summary summary : list) {
			totalAmt += Integer.parseInt(summary.getTotalAmt().getText());
			totalGameAmt += Integer.parseInt(summary.getGameAmt().getText());
			totalNetAmt += Integer.parseInt(summary.getNetTotal().getText());
		}
		totalAmount.setText(df.format(totalAmt) + " MMK");
		totalGameAmount.setText(df.format(totalGameAmt) + " MMK");
		totalNetAmount.setText(df.format(totalNetAmt) + " MMK");
		tblview.setId("my-table");

	}

	final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
		public DateCell call(final DatePicker datePicker) {
			return new DateCell() {
				@Override
				public void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);

					if (LocalDate.now().isBefore(item)) {
						setDisable(true);
					}
				}
			};
		}
	};
}
