package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;

import EasyXLS.ExcelDocument;
import EasyXLS.ExcelStyle;
import EasyXLS.ExcelTable;
import EasyXLS.ExcelWorksheet;
import EasyXLS.Constants.Alignment;
import EasyXLS.Constants.DataType;
import application.TwoDThreeDApplication;
import application.TwoDThreeDException;
import database2d3d.BackupImplementation;
import database2d3d.CustomerImplematation;
import database2d3d.ExceedingImplementation;
import database2d3d.OutSourceImplementation;
import database2d3d.QueriesForTowDMorning;
import database2d3d.SetAmountImplementation;
import database2d3d.TwoDImplementation;
import database2d3d.WinNumberImplementation;
import entity.Exceeding;
import entity.ObjectForCurrentTable;
import entity.ObjectForPreviewTable;
import entity.OutSource;
import entity.TwoDEntity;
import entity.WeekNo;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.BackupService;
import service.CustomerService;
import service.ExceedingService;
import service.OutSourceService;
import service.TwoDService;
import service.WinNumberService;
import unit.AlertShow;
import unit.DataPass;
import unit.LoadView;

public class TwoDThreeDHomeController implements Initializable {

	private ObservableList<Exceeding> exceeding = null;
	int counterForAmountOneAndTwo = 0;
	private Exceeding withoutObj;
	boolean cond = true;
	@FXML
	private TextField searchCurrentKey;
	@FXML
	private TextField searchMaxKey;
	@FXML
	private Label currentTotal;
	@FXML
	private VBox mmmCurrentTableBox;
	@FXML
	private VBox thihaCurrentTableBox;
	@FXML
	private HBox vbHb;
	@FXML
	private VBox previewVb;
	@FXML
	private Label totalAll;

	@FXML
	private TextField tfkhway;
	@FXML
	private ToggleGroup inOutGroup;
	@FXML
	private JFXRadioButton in;
	@FXML
	private JFXRadioButton out;
	@FXML
	private ToggleGroup allLastGroup;
	@FXML
	private JFXRadioButton all;
	@FXML
	private JFXRadioButton last;
	@FXML
	private JFXDatePicker date;
	@FXML
	private ListView<HBox> list1;
	@FXML
	private TableColumn<ObjectForPreviewTable, Integer> tCbalanceColumn;
	@FXML
	private ListView<HBox> list2;
	@FXML
	private ListView<HBox> list3;
	@FXML
	private ListView<HBox> list4;
	@FXML
	private ListView<HBox> list5;
	@FXML
	private ListView<HBox> list6;
	@FXML
	private ListView<HBox> list7;
	@FXML
	private ListView<HBox> list8;
	@FXML
	private ListView<HBox> list9;
	@FXML
	private ListView<HBox> list10;
	@FXML
	private Label title;
	@FXML
	private JFXButton summaryBtn;
	@FXML
	private SVGPath svg;
	@FXML
	private FlowPane fp;
	@FXML
	private TableView<WeekNo> weekNo;
	@FXML
	private TextField setAmount;
	@FXML
	private TableView<TwoDEntity> deatils;
	@FXML
	private TableView<OutSource> currentTable;
	@FXML
	private Label totalCustomerAmount;
	@FXML
	private Label totalplaycount;
	@FXML
	private Label totalplaycounttwo;
	@FXML
	private TextField tfCustomerName;
	@FXML
	private JFXComboBox<Label> cbChooseCustomer;
	@FXML
	private TextField tfPlayNumber;
	@FXML
	private TextField tfKeyWord;
	@FXML
	private TextField tfAmount;
	@FXML
	private TableView<OutSource> outList;
	@FXML
	private HBox outListHbox;
	@FXML
	private Label outTotal;
	@FXML
	private Label customerCount;
	@FXML
	private Label dateailTotal;
	@FXML
	private VBox vb;
	@FXML
	private TextField tfRemain;
	@FXML
	private TextField tfRemove;
	@FXML
	private JFXButton setResult;
	@FXML
	private JFXButton setAmountBtn;
	@FXML
	private TextField tfResult;
	@FXML
	private Label maxExceedTotal;
	@FXML
	private TableView<Exceeding> maxTable;
	@FXML
	private TableView<ObjectForCurrentTable> tVCurrentTable;
	@FXML
	private TableColumn<ObjectForCurrentTable, String> tCPlayNumber;
	@FXML
	private TableColumn<ObjectForCurrentTable, Integer> tCAmount;
	@FXML
	private StackPane sp;
	@FXML
	private Label totalAmountCurTable;
	@FXML
	private JFXButton delete;
	@FXML
	private VBox deatilsBox;
	@FXML
	private Label detailsTableNumber;
	@FXML
	private VBox outTableAndoutTotal;
	@FXML
	private ScrollBar sb;
	@FXML
	private TableView<ObjectForPreviewTable> previewTable;
	@FXML
	private TableColumn<ObjectForPreviewTable, String> tCPlayNumberForPreview;
	@FXML
	private HBox hb;
	boolean isDelete = false;

	@FXML
	private FlowPane fpForTenButtons;

	private CustomerService customerService;
	private TwoDService twoDService;
	private WinNumberService winNumberService;
	private ExceedingService exceedingService;
	private OutSourceService outSourceService;

	int amount;
	int total = 0;
	int setAmountMulti = 0;
	int playNumber = 0;

	private BackupService backupService;

	DecimalFormat df = new DecimalFormat("###,###,###");

	String prompPreviousText = "";
	String morE = DataPass.getMeDataPass().equals("Morning") ? "Morning"
			: DataPass.getMeDataPass().equals("Evening") ? "Evening" : "3D";
	String format = (morE.equals("Morning") || morE.equals("Evening")) ? "%02d" : "%03d";

	ObservableList<ObjectForPreviewTable> listSS = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listMM = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listSM = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listMS = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listSP = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listMP = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listAA = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listAN = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listNA = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listN = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listP = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listB = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listKhway = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listAAA = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listANN = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listNAN = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listNNA = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listANNA = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> listForKwayInPreview = FXCollections.observableArrayList();

	ArrayList<String> arrayListSS = new ArrayList<>();
	ArrayList<String> arrayListMM = new ArrayList<>();
	ArrayList<String> arrayListSM = new ArrayList<>();
	ArrayList<String> arrayListMS = new ArrayList<>();
	ArrayList<String> arrayListSP = new ArrayList<>();
	ArrayList<String> arrayListMP = new ArrayList<>();
	ArrayList<String> arrayListAA = new ArrayList<>();
	ArrayList<String> arrayListAN = new ArrayList<>();
	ArrayList<String> arrayListNA = new ArrayList<>();
	ArrayList<String> arrayListN = new ArrayList<>();
	ArrayList<String> arrayListP = new ArrayList<>();
	ArrayList<String> arrayListB = new ArrayList<>();
	ArrayList<String> arrayListKhway = new ArrayList<>();
	ArrayList<String> arrayListAAA = new ArrayList<>();
	ArrayList<String> arrayListANN = new ArrayList<>();
	ArrayList<String> arrayListNAN = new ArrayList<>();
	ArrayList<String> arrayListNNA = new ArrayList<>();
	ArrayList<String> arrayListANNA = new ArrayList<>();
	ArrayList<OutSource> outListDul;

	ObservableList<ObjectForCurrentTable> toAddDataIntoCurTable = FXCollections.observableArrayList();
	ObservableList<ObjectForPreviewTable> toAddDataIntoPreviewTable = FXCollections.observableArrayList();
	FilteredList<ObjectForCurrentTable> filteredData = new FilteredList<>(toAddDataIntoCurTable, i -> true);

	int count = 0;
	int toExtractForUpdate;
	String recentOne = "0";
	String recentAmountOne = "0";
	String recentAmountTwo = "0";
	String controllerForRecentOne = "0";
	String controllerForRememberingAmount = "0";
	String recentOneFor3DR = "0";
	String storeGet0FromEveryNum = "0";
	int countToAddToR = 0;

	ArrayList<String> listForRemovedItems = new ArrayList<>();
	ArrayList<String> list = new ArrayList<>();
	ArrayList<String> listForEveryNum = new ArrayList<>();
	ArrayList<String> listForR = new ArrayList<>();
	ArrayList<String> listForEveryNumForR = new ArrayList<>();

	boolean firstTimeController = true;

	public void informAlert(String message) {
		new Alert(AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
	}

	public void ErrorAlert(String message) {
		new Alert(AlertType.ERROR, message, ButtonType.OK).showAndWait();
	}

	public void generatePDFForMMCurretntTable() {
		List<ObjectForCurrentTable> list = tVCurrentTable.getItems();
		if (list != null) {
			File f = null;
			try {
				FileChooser fc = new FileChooser();
				fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
				fc.setInitialFileName(LocalDate.now().format(DateTimeFormatter.ofPattern("E-MMM-dd-yyyy")) + "-"
						+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh-mm-ss-a"))
						+ " of IN-Process-current-table" + ".pdf");
				f = fc.showSaveDialog(null);
				Document document = new Document();

				PdfPTable table = new PdfPTable(2);
				table.setWidthPercentage(20);

				PdfWriter.getInstance(document, new FileOutputStream(f));
				document.open();
				Phrase p = new Phrase("                                                        "
						+ LocalDate.now().format(DateTimeFormatter.ofPattern("E,MMM-dd-yyyy")) + ","
						+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss:a")));

				document.add(p);
				PdfPCell c1 = new PdfPCell(new Phrase("No"));
				table.addCell(c1);

				c1 = new PdfPCell(new Phrase("Amount"));
				table.addCell(c1);

				for (ObjectForCurrentTable ofc : list) {
					PdfPCell mmmPlayNumber = new PdfPCell(new Phrase(ofc.getPlayNumber()));
					PdfPCell mmmAmount = new PdfPCell(new Phrase(String.valueOf(ofc.getAmount())));

					table.addCell(mmmPlayNumber);
					table.addCell(mmmAmount);

				}
				document.add(table);
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			AlertShow.showInfo("PDF create success\nLocation:" + f.getAbsolutePath());
		}

	}

	public void generatePDFForMax() {
		List<Exceeding> list = maxTable.getItems();
		if (list != null) {
			File f = null;
			try {
				FileChooser fc = new FileChooser();
				fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
				fc.setInitialFileName(LocalDate.now().format(DateTimeFormatter.ofPattern("E-MMM-dd-yyyy")) + "-"
						+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh-mm-ss-a"))
						+ "of MAX-Process-current-table" + ".pdf");
				f = fc.showSaveDialog(null);
				Document document = new Document();

				PdfPTable table = new PdfPTable(2);
				table.setWidthPercentage(20);

				PdfWriter.getInstance(document, new FileOutputStream(f));
				document.open();
				Phrase p = new Phrase("                                                        "
						+ LocalDate.now().format(DateTimeFormatter.ofPattern("E,MMM-dd-yyyy")) + ","
						+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss:a")));

				document.add(p);
				PdfPCell c1 = new PdfPCell(new Phrase("No"));
				table.addCell(c1);

				c1 = new PdfPCell(new Phrase("Amount"));
				table.addCell(c1);

				for (Exceeding exceeding : list) {

					PdfPCell maxPlayNumber = new PdfPCell(new Phrase(exceeding.getExceedingNumber()));
					PdfPCell maxAmount = new PdfPCell(new Phrase(String.valueOf(exceeding.getExceedingAmount())));

					table.addCell(maxPlayNumber);
					table.addCell(maxAmount);

				}
				document.add(table);
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			AlertShow.showInfo("PDF create success\nLocation:" + f.getAbsolutePath());
		}
	}

	public void generatePDFForCurrentTable() {
		List<OutSource> list = currentTable.getItems();
		if (list != null) {
			File f = null;
			try {
				FileChooser fc = new FileChooser();
				fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
				fc.setInitialFileName(LocalDate.now().format(DateTimeFormatter.ofPattern("E-MMM-dd-yyyy")) + "-"
						+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh-mm-ss-a"))
						+ " of OUT-Process-current-table" + ".pdf");
				f = fc.showSaveDialog(null);
				Document document = new Document();

				PdfPTable table = new PdfPTable(2);
				table.setWidthPercentage(20);

				PdfWriter.getInstance(document, new FileOutputStream(f));
				document.open();
				Phrase p = new Phrase("                                                        "
						+ LocalDate.now().format(DateTimeFormatter.ofPattern("E,MMM-dd-yyyy")) + ","
						+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss:a")));

				document.add(p);
				PdfPCell c1 = new PdfPCell(new Phrase("No"));
				table.addCell(c1);

				c1 = new PdfPCell(new Phrase("Amount"));
				table.addCell(c1);

				for (OutSource outSource : list) {

					PdfPCell tttPlayNumber = new PdfPCell(new Phrase(outSource.getExceedingNumber()));
					PdfPCell tttAmount = new PdfPCell(new Phrase(String.valueOf(outSource.getCurrentAmt())));

					table.addCell(tttPlayNumber);
					table.addCell(tttAmount);

				}
				document.add(table);
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			AlertShow.showInfo("PDF create success\nLocation:" + f.getAbsolutePath());
		}
	}

	public void inoutOnAction() {
		if (in.isSelected() || out.isSelected()) {
			tfPlayNumber.setDisable(true);
			tfKeyWord.setDisable(true);
			tfAmount.setDisable(true);
			tfkhway.setDisable(true);
			cbChooseCustomer.requestFocus();
		}
	}

	public void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to exit?", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> op = alert.showAndWait();
		if (op.get() == ButtonType.NO) {
			alert.close();
		} else if (op.get() == ButtonType.YES) {
			tfPlayNumber.getScene().getWindow().hide();
		}
	}

	public void minius() {
		Stage stage = (Stage) tfAmount.getScene().getWindow();
		stage.setIconified(true);
	}

	private List<String> getNums(int index) {
		List<String> list = new ArrayList<String>();
		int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening") ? 2
				: 3;

		int value = twoDService.toShowAmount(index, title.getText(), date.getValue());
		list.add(String.format("%" + "0" + twoOrThree + "d", index));
		list.add(df.format(value));
		return list;
	}

	public void print() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Xlsx Files", "*.xlsx"));
		File file = fileChooser.showSaveDialog(tfPlayNumber.getScene().getWindow());
		ExcelDocument workbook = new ExcelDocument(1);

		ExcelTable xlsFirstTable = ((ExcelWorksheet) workbook.easy_getSheetAt(0)).easy_getExcelTable();
		int lastIndex = title.getText().equals("3D") ? 1000 : 100;
		for (int row = 0; row < lastIndex; row++) {
			List<String> list = getNums(row);
			xlsFirstTable.easy_getCell(row + 1, 0).setValue(list.get(0));
			xlsFirstTable.easy_getCell(row + 1, 1).setValue(list.get(1));
		}
		ExcelStyle xlsStyleData = new ExcelStyle();
		xlsStyleData.setHorizontalAlignment(Alignment.ALIGNMENT_LEFT);
		xlsStyleData.setWrap(false);
		xlsStyleData.setDataType(DataType.STRING);
		xlsFirstTable.easy_setRangeStyle("A2:E101", xlsStyleData);
		workbook.easy_WriteXLSXFile(file.getAbsolutePath().toString());
		if (workbook.easy_getError().equals("")) {
			AlertShow.showInfo("Print success at " + file.getAbsolutePath());
		} else {
			AlertShow.showError("Error encountered: " + workbook.easy_getError());
		}
		workbook.Dispose();
	}

	private void checkAmountNumber() {
		if (tfAmount.getText().isEmpty()) {
			throw new TwoDThreeDException("Please enter play amount");
		}
		try {
			Integer.parseInt(tfAmount.getText());
		} catch (NumberFormatException e) {
			throw new TwoDThreeDException("Please enter play amount in integer");
		}
		tfAmount.requestFocus();
		tfAmount.selectAll();
		int number = Integer.parseInt(tfAmount.getText());
		if (number < 0) {
			throw new TwoDThreeDException("Please enter amount in positive digit");
		}
		if (exceeding == null) {
			List<Exceeding> withoutSelect = maxTable.getItems();
			if (withoutSelect.isEmpty()) {
				withoutObj = new Exceeding(tfPlayNumber.getText(), Integer.parseInt(tfAmount.getText()));
			} else {
				if (!tfPlayNumber.getText().isEmpty()) {
					if (out.isSelected()) {
						for (int i = 0; i < withoutSelect.size(); i++) {
							if (Integer.parseInt(tfPlayNumber.getText()) == Integer
									.parseInt(withoutSelect.get(i).getExceedingNumber())) {
								withoutObj = withoutSelect.get(i);
							}
						}
						if (withoutObj == null) {
							withoutObj = new Exceeding(tfPlayNumber.getText(), Integer.parseInt(tfAmount.getText()));
						}
					}
				}
			}
		}
	}

	private void inVisibleButton(String winNumber) {
		if (!winNumber.isEmpty()) {
			String array[] = winNumber.split("");
			for (Node node : fpForTenButtons.getChildren()) {
				if (node instanceof Button) {
					Button b = (Button) node;
					if (b.getText().equals(array[0])) {

						b.setVisible(false);
					}
					if (b.getText().equals(array[1])) {
						b.setVisible(false);
					}
					if (title.getText().equals("3D")) {
						if (b.getText().equals(array[2])) {
							b.setVisible(false);
						}
					}
				}
			}
		}
	}

	private HBox buildHboxLayOut(int amount, int i) {

		Label l1 = new Label(String.format(format, i));
		Label l2 = new Label(df.format(amount));
		int spacing = 18;
		int prefwid = 71;
		int font = 18;
		if (title.getText().equals("3D")) {
			spacing = 10;
			prefwid = 69;
			font = 16;
		}
		HBox hb = new HBox(spacing);
		Font f = Font.font("Arial", 18);
		Font f2 = Font.font("Arial", FontWeight.BOLD, font);
		l2.setPrefWidth(prefwid);
		l2.setAlignment(Pos.TOP_RIGHT);
		l2.setFont(f);
		l1.setFont(f2);
		if (amount < 0) {
			l2.setStyle("-fx-text-fill:red");
		} else {
			l2.setStyle("-fx-text-fill:black");
		}
		hb.getChildren().addAll(l1, l2);
		return hb;
	}

	private void checkSetResultError() {
		if (tfResult.getText().isEmpty()) {
			throw new TwoDThreeDException("Please enter win number");
		}
		try {
			Integer.parseInt(tfResult.getText());
		} catch (NumberFormatException e) {
			throw new TwoDThreeDException("Please enter win number in integer");
		}
		List<WeekNo> list = weekNo.getItems();
		list.forEach(e -> {
			if (Integer.parseInt(e.getWinNo()) == Integer.parseInt(tfResult.getText())) {
				throw new TwoDThreeDException(tfResult.getText() + "is already win number");
			}
			if (winNumberService.isWinListValid(morE, date.getValue()) && setResult.getText().equals("Result")) {
				throw new TwoDThreeDException("Win number for today is already register");
			}
		});
	}

	private void getWinNumbers() {
		ArrayList<WeekNo> list = winNumberService.getWinNumbers(morE, date.getValue());
		weekNo.getItems().clear();
		weekNo.getItems().addAll(list);

	}

	private void checkRemainError() {
		if (tfRemain.getText().isEmpty()) {
			throw new TwoDThreeDException("Please enter remain number");
		}
		try {
			Integer.parseInt(tfRemain.getText());
		} catch (NumberFormatException e) {
			throw new TwoDThreeDException("Please enter remain number in digit");
		}
		int value = Integer.parseInt(tfRemain.getText());
		if (value < 0) {
			throw new TwoDThreeDException("Please enter positive number");
		}
		if (!(value >= 0 && value <= 9)) {
			throw new TwoDThreeDException("Please enter number between 0 to 9");
		}
	}

	private void checkRemoveError() {
		if (tfRemove.getText().isEmpty()) {
			throw new TwoDThreeDException("Please enter remove number");
		}
		try {
			Integer.parseInt(tfRemove.getText());
		} catch (NumberFormatException e) {
			throw new TwoDThreeDException("Please enter remove number in digit");
		}
		int value = Integer.parseInt(tfRemove.getText());
		if (value < 0) {
			throw new TwoDThreeDException("Please enter positive number");
		}
		if (!(value >= 0 && value <= 9)) {
			throw new TwoDThreeDException("Please enter number between 0 to 9");
		}
	}

	private void checkSetAmountError() {
		if (setAmount.getText().isEmpty()) {
			throw new TwoDThreeDException("Please enter set Amount value");
		}
		try {
			Integer.parseInt(setAmount.getText());
		} catch (NumberFormatException e) {
			throw new TwoDThreeDException("Please enter set amount number in digit");
		}
		if (Integer.parseInt(setAmount.getText()) < 0) {
			throw new TwoDThreeDException("Please enter set amount number in positive digit");
		}
	}

	private void setDatainMaxTable() {
		ArrayList<Exceeding> list = exceedingService.getExceedingList(new Exceeding(date.getValue(), morE));
		if (list != null) {
			maxTable.getItems().clear();
			maxTable.getItems().addAll(list);
			int total = 0;
			for (Exceeding exceeding : list) {
				total += exceeding.getExceedingAmount();
			}
			maxExceedTotal.setText(df.format(total));
		}
	}

	private void selectMaxtable() {
		maxTable.setOnMouseClicked(e -> {
			exceeding = maxTable.getSelectionModel().getSelectedItems();
			if (exceeding != null) {
				if (exceeding.size() > 1) {
					setDataInMultiAmountTextFieldFromMaxTable();
				} else {
					setDataInAmountTextFieldFromMaxTable(exceeding.get(0));
				}
			}
		});
	}

	private void setDataInMultiAmountTextFieldFromMaxTable() {
		out.setSelected(true);
		tfPlayNumber.setText(String.valueOf(exceeding.get(0).getExceedingNumber()));
		for (Exceeding exceeding2 : exceeding) {
			setAmountMulti += exceeding2.getExceedingAmount();
		}
		tfAmount.setText(String.valueOf(setAmountMulti));
		tfPlayNumber.setEditable(false);
		tfAmount.setEditable(maxTable.getItems().size() == 1 ? true : false);
//		tfAmount.requestFocus();
//		tfAmount.selectAll();
		currentTable.getSelectionModel().clearSelection();
		tfPlayNumber.setDisable(false);
		tfKeyWord.setDisable(false);
		tfAmount.setDisable(false);
		setAmountMulti = 0;
	}

	private void setDataInAmountTextFieldFromMaxTable(Exceeding exceeding) {
		if (exceeding != null) {
			out.setSelected(true);
			tfPlayNumber.setText(String.valueOf(exceeding.getExceedingNumber()));
			tfAmount.setText(String.valueOf(exceeding.getExceedingAmount()));
			tfPlayNumber.setEditable(false);
//			tfAmount.requestFocus();
//			tfAmount.selectAll();
			currentTable.getSelectionModel().clearSelection();
			tfPlayNumber.setDisable(false);
			tfKeyWord.setDisable(false);
			tfAmount.setDisable(false);
		}

	}

	private void outSelectedMultiProcess(List<Exceeding> list, String customerID) {
		if (list != null) {
			for (Exceeding exceeding2 : exceeding) {
				list.add(exceeding2);
			}
			list.forEach(e -> {
				playNumber = Integer.parseInt(e.getExceedingNumber());
				Exceeding getExceedingID = new Exceeding(Integer.parseInt(e.getExceedingNumber()),
						e.getExceedingAmount(), date.getValue(), morE);
				int exeID = exceedingService.getExceedingID(getExceedingID);
				OutSource insertIntoOutSourcetable = new OutSource(customerID, exeID, e.getExceedingAmount());
				int getExeID = outSourceService.isValidExceedID(customerID, playNumber, morE, date.getValue());
				if (getExeID == -1) {
					outSourceService.insertIntoOutSourcetable(insertIntoOutSourcetable);
				} else {
					outSourceService.updateIntoOutSourcetable(getExeID, amount);
				}
				TwoDEntity updateExtractAmount = new TwoDEntity(morE, date.getValue(), playNumber,
						e.getExceedingAmount());
				twoDService.updateExtract_amount(updateExtractAmount);
				Exceeding updateAmount = new Exceeding(Integer.parseInt(e.getExceedingNumber()), 0, date.getValue(),
						morE);
				exceedingService.updateAmount(updateAmount);
				Exceeding updateNoted = new Exceeding(Integer.parseInt(e.getExceedingNumber()), morE, date.getValue());
				exceedingService.updateNoted(updateNoted, 1);
				insertIntoList(true, playNumber);
			});
		}
	}

	private void outSelectedProcess(String customerID, Exceeding e) {
		String customerName = customerService.getCustomerName("O-00001");
		TwoDEntity isAlready = new TwoDEntity(customerName, morE, date.getValue(),
				Integer.parseInt(e.getExceedingNumber()));
		if (twoDService.isAlreadyLotteryNumber(isAlready) == -1) {
			String specialCustomerID = customerService.getCustomerId("Special Customer");
			TwoDEntity insertintoTwoDTable = new TwoDEntity(0, morE, date.getValue(),
					Integer.parseInt(e.getExceedingNumber()), 0);
			twoDService.insertDataIntoTwoDUserPlayTable(specialCustomerID, insertintoTwoDTable);
			exceedingService.insert(new Exceeding(Integer.parseInt(e.getExceedingNumber()), e.getExceedingAmount(),
					date.getValue(), morE, false));
		} else if (twoDService.isAlreadyLotteryNumber(isAlready) != -1) {
			System.out.println("Out Insert");
			if (exceedingService
					.isExceed(new Exceeding(Integer.parseInt(e.getExceedingNumber()), morE, date.getValue())) == -1) {
				exceedingService.insert(new Exceeding(Integer.parseInt(e.getExceedingNumber()), e.getExceedingAmount(),
						date.getValue(), morE, false));
			}
		}
		int amount = Integer.parseInt(tfAmount.getText());
		int playNumber = Integer.parseInt(tfPlayNumber.getText());
		Exceeding getExceedingID = new Exceeding(Integer.parseInt(e.getExceedingNumber()), e.getExceedingAmount(),
				date.getValue(), morE);
		int exeID = exceedingService.getExceedingID(getExceedingID);
		System.out.println("Exe ID: " + exeID);
		OutSource insertIntoOutSourcetable = new OutSource(customerID, exeID, amount);
		int getExeID = outSourceService.isValidExceedID(customerID, playNumber, morE, date.getValue());

		if (getExeID == -1) {
			outSourceService.insertIntoOutSourcetable(insertIntoOutSourcetable);
		} else {
			outSourceService.updateIntoOutSourcetable(getExeID, amount);
		}

		TwoDEntity updateExtractAmount = new TwoDEntity(morE, date.getValue(), playNumber, amount);
		twoDService.updateExtract_amount(updateExtractAmount);
		int currentExceedingAmount = e.getExceedingAmount();
		int current = currentExceedingAmount - amount;
		Exceeding updateAmount = new Exceeding(Integer.parseInt(e.getExceedingNumber()), current, date.getValue(),
				morE);
		exceedingService.updateAmount(updateAmount);

		if (currentExceedingAmount == amount || current < 0) {
			Exceeding updateNoted = new Exceeding(Integer.parseInt(e.getExceedingNumber()), morE, date.getValue());
			exceedingService.updateNoted(updateNoted, 1);
		}
		insertIntoList(true, playNumber);
	}

	private void setDataToCurrentTable() {
		currentTable.getItems().clear();
//		HERE I removed getText() suffix from getValue() and l
		Label l = cbChooseCustomer.getValue();
		if (l != null) {
			ArrayList<OutSource> list = outSourceService.getOutSourceData(l.getText(), date.getValue(),
					title.getText());
			if (list != null) {
				currentTable.getItems().clear();
				currentTable.getItems().addAll(list);
			}
			int total = 0;
			for (OutSource outSource : list) {
				total += outSource.getCurrentAmt();
			}
			currentTotal.setText(df.format(total));
			System.out.println("Total:" + total);
		}
	}

	private void checkSummary() {
		if (!winNumberService.isWinListValid(morE, date.getValue())) {
			throw new TwoDThreeDException("Win number is not declear yet");
		}
	}

	private void checkFile() {
		try {
			Path path = Paths.get("C://Backup");

			BackupService.buildFile(Paths.get("C://Backup").toFile());
			Path pathCustomer = Paths.get(path + "/" + BackupService.CUSTOMER_FOLDER_NAME + "/" + LocalDate.now()
					+ " of-customer-backup.csv");
			Path pathTwoDThreeD = Paths.get(path + "/" + BackupService.TWOD_THREED_FOLDER_NAME + "/" + LocalDate.now()
					+ " of-twodthreed-backup.csv");
			Path pathExceeding = Paths.get(path + "/" + BackupService.EXCEEDING_FOLDER_NAME + "/" + LocalDate.now()
					+ " of-exceeding-backup.csv");
			Path pathOutSource = Paths.get(path + "/" + BackupService.OUTSOURCE_FOLDER_NAME + "/" + LocalDate.now()
					+ " of-outsource-backup.csv");
			Path pathWinner = Paths.get(list.get(0) + "/" + BackupService.WIN_NUMBER_FOLDER_NAME + "/" + LocalDate.now()
					+ " of-winnumber-backup.csv");
			backupService.customerBackup(pathCustomer.toFile());
			backupService.twoDThreeDBackup(pathTwoDThreeD.toFile());
			backupService.exceedingBackup(pathExceeding.toFile());
			backupService.outSourceBackup(pathOutSource.toFile());
			backupService.winNumBackup(pathWinner.toFile());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void summary() {
		checkFile();
		try {
			checkSummary();
			DataPass.summaryDataPass(date.getValue(), winNumberService.getWinNumberForToday(morE, date.getValue()));
			new LoadView().loadViewWithStageSummary("Summary", title.getText());
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
	}

	public void loadCurrentData() {
		cbChooseCustomer.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				tfkhway.setDisable(false);
				tfPlayNumber.setDisable(false);
				tfPlayNumber.setEditable(true);
				tfkhway.setEditable(true);
				tfPlayNumber.setOpacity(1);
				tfPlayNumber.requestFocus();
			}
		});
		if (out.isSelected()) {
			setDataToCurrentTable();
		}
	}

	public void loadDataWithDate() {
		insertIntoList(false, 0);
		ArrayList<WeekNo> winList = winNumberService.getWinNumbers(morE, date.getValue());
		if (!winList.isEmpty()) {
			winList.forEach(e -> {
				inVisibleButton(String.valueOf(e.getWinNo()));
			});
		} else {
			addnewButton();
		}
		getWinNumbers();
		setDatainMaxTable();

		if (LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY
				|| LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
			AlertShow.showWarning("Today is " + LocalDate.now().getDayOfWeek() + "\nPlease aware.");
			setDisable();
		}
		if (!cond) {
			deatilsBox.getChildren().remove(3);
			cond = true;
		}
		// setDataToCurrentTable();
		showDetailsAndOutList("");
		in.setSelected(true);
		totalAll.setText("Total   :  " + df.format(new QueriesForTowDMorning().getAllTotalData(morE, date.getValue()))
				+ "  MMK");
		totalplaycountset();
	}

	private void setDisable() {
		tfPlayNumber.setDisable(true);
		tfKeyWord.setDisable(true);
		tfAmount.setDisable(true);
		tfkhway.setDisable(true);
		setAmount.setDisable(true);
		tfResult.setDisable(true);
		tfRemain.setDisable(true);
		tfRemove.setDisable(true);
		setAmountBtn.setDisable(true);
		setResult.setDisable(true);
	}

	public void setResult() {
		try {
			checkSetResultError();
			WeekNo weekNo = new WeekNo(date.getValue().getDayOfWeek().toString(), Integer.parseInt(tfResult.getText()),
					DataPass.getMeDataPass(), date.getValue());
			if (setResult.getText().equals("Edit")) {
				WeekNo no = this.weekNo.getSelectionModel().getSelectedItem();
				for (Node node : fpForTenButtons.getChildren()) {
					if (node instanceof Button) {
						Button b = (Button) node;
						for (String result : String.valueOf(no.getWinNo()).split("")) {
							if (b.getText().equals(result)) {
								b.setVisible(true);
							}
						}
					}
				}
				winNumberService.updateDataIntoTwoDUserPlayTable(weekNo);
			} else {
				winNumberService.insertWinNumber(weekNo);
			}
			getWinNumbers();
			inVisibleButton(tfResult.getText());
			showDetailsAndOutList(tfResult.getText());
			tfResult.clear();
			setResult.setText("Result");
			summaryBtn.requestFocus();
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
	}

	public void editWinNumber() {
		WeekNo weekNoTemp = weekNo.getSelectionModel().getSelectedItem();
		if (weekNoTemp != null) {
			setResult.setText("Edit");
			tfResult.setText(String.valueOf(weekNoTemp.getWinNo()));
			tfResult.requestFocus();
			tfResult.selectAll();
		}
	}

	public void setAmount() {
		try {
			checkSetAmountError();
			if (setAmountBtn.getText().equals("Un Set")) {
				setAmount.setDisable(false);
				setAmountBtn.setText("Set");
				setAmount.requestFocus();
				setAmount.selectAll();
			} else {
				setAmount.setDisable(true);
				setAmountBtn.setText("Un Set");
				new SetAmountImplementation().updateAmount(Integer.parseInt(setAmount.getText()));
				if (!setAmount.getText().isEmpty()) {
					int setAmt = new SetAmountImplementation().getamount();
					ArrayList<TwoDEntity> twoDlist = twoDService.getAllData(setAmt, morE, date.getValue());
					List<Exceeding> maxList = maxTable.getItems();
					if (maxList.isEmpty()) {
						for (TwoDEntity twoD : twoDlist) {
							exceedingService.insert(new Exceeding(twoD.getPlayNumber(), twoD.getAmount(),
									date.getValue(), morE, false));
						}
					} else {
						for (Exceeding exceeding : maxList) {
							exceeding.setDate(date.getValue());
							exceeding.setMe(morE);
							exceedingService.deletExceeding(exceeding);
						}
						for (TwoDEntity twoD : twoDlist) {
							exceedingService.insert(new Exceeding(twoD.getPlayNumber(), twoD.getAmount(),
									date.getValue(), morE, false));
						}
					}

				}
			}
			setDatainMaxTable();
			// setDataToCurrentTable();
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
	}

	public void loadCustomerDetails() {
		AlertShow.showWarning("Not Available for Trial Version:");
//		loadViewWithNormalStage("CustomerDetailsView", "Customer Deatails");
	}

	public void addCustomer() {
		try {
			if (customerService.insertCustomerName(tfCustomerName.getText(), in.isSelected(), date.getValue())) {
				AlertShow.showInfo("Agent " + tfCustomerName.getText() + " is register");
			}
			loadCustomer();
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
	}

	private void loadCustomer() {
		tfCustomerName.clear();
		ArrayList<Label> customerNames = customerService.getAllCustomerData(in.isSelected());
		cbChooseCustomer.getItems().clear();
		cbChooseCustomer.getItems().addAll(customerNames);
		totalCustomerAmount.setText("Agent -" + customerService.getTotalCustomerNumber(in.isSelected()));
		cbChooseCustomer.requestFocus();
	}

	public void enterNumber() {
		try {
			checkNumberError();
			String lastPlayNum = "";
			if (!toAddDataIntoCurTable.isEmpty()) {
				lastPlayNum = toAddDataIntoCurTable.get(toAddDataIntoCurTable.size() - 1).getPlayNumber();
			}
			if ((title.getText().equals("3D") && tfAmount.getPromptText().isEmpty()
					&& tfPlayNumber.getText().equals("+"))
					|| (tfPlayNumber.getText().length() == 4 && tfPlayNumber.getText().endsWith("*")
							&& recentAmountTwo.equals("0") && !lastPlayNum.endsWith("R"))) {
				ErrorAlert("Invalid play number");
				System.out.println("Hi");
				tfAmount.setDisable(true);
				tfPlayNumber.requestFocus();
				tfPlayNumber.selectAll();
			} else if (!recentAmountOne.equals("0") && !recentAmountTwo.equals("0") && in.isSelected()
					&& tfPlayNumber.getText().length() == 4 && tfPlayNumber.getText().endsWith("*")) {
				System.out.println(recentAmountOne + " recentAmountOne");
				System.out.println(recentAmountTwo + " recentAmountTwo");
				tfAmount.setDisable(false);
				tfAmount.setText(tfAmount.getPromptText());
				amountfocus();
				inProcess(false, recentAmountOne, recentAmountTwo);
			} else if (tfPlayNumber.getText().length() == 4 && tfPlayNumber.getText().endsWith("*")
					&& recentAmountTwo.equals("0") && lastPlayNum.endsWith("R")) {
				tfAmount.setDisable(false);
				tfAmount.setText(tfAmount.getPromptText());
				amountfocus();
				inProcess(false, "0", "0");
			} else if (title.getText().equals("3D") && tfAmount.getPromptText().isEmpty()) {
				tfAmount.setDisable(false);
				tfAmount.requestFocus();
				amountfocus();
			} else {
				tfAmount.setDisable(false);
				tfKeyWord.requestFocus();
				amountfocus();
			}
//			if (title.getText().equals("3D") && tfAmount.getPromptText().isEmpty()) {
//				tfAmount.setDisable(false);
//				amountfocus();
//			}
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
	}

	public void enterKeyWord() {
		if (cbChooseCustomer.getValue() != null) {
			tfAmount.requestFocus();
			if (amount != 0) {
				tfAmount.setText(String.valueOf(amount));
				tfAmount.selectAll();
			}
			if (tfPlayNumber.getText().length() == 2 && in.isSelected()) {
				if (tfKeyWord.getText().equals("*")) {
					if (tfPlayNumber.getText().charAt(0) != tfPlayNumber.getText().charAt(1)) {
						if (tfkhway.getText().isEmpty() && !recentAmountOne.equals("0") && !recentAmountTwo.equals("0")
								&& in.isSelected()) {
							tfAmount.setDisable(false);
							tfAmount.setText(tfAmount.getPromptText());
							tfKeyWord.setText("*");
							inProcess(true, recentAmountOne, recentAmountTwo);
						} else if (!toAddDataIntoCurTable.isEmpty()) {
							tfAmount.setDisable(false);
							System.out.println("!* false");
							tfAmount.setText(tfAmount.getPromptText());
							inProcess(false, "0", "0");
						} else {
							ErrorAlert("Invalid Function");
							tfAmount.setDisable(true);
							tfKeyWord.requestFocus();
							tfKeyWord.clear();
						}
					} else {
						ErrorAlert("Play number must be different");
						tfKeyWord.requestFocus();
					}
				} else if (tfKeyWord.getText().equals("+")) {
					amountfocus();
				}
			}
			if (!tfAmount.getPromptText().isEmpty() && in.isSelected()) {
				tfAmount.setText(tfAmount.getPromptText());
				enterAmount();
			}

			LocalDate dateFromPicker = date.getValue();
			QueriesForTowDMorning twoDMorning = new QueriesForTowDMorning();

			String choseName = cbChooseCustomer.getSelectionModel().getSelectedItem().getText();

			ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
					dateFromPicker, title.getText());
			tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));

			toCalculateTotalAmountForCurrentTable();
			last.setSelected(true);
		} else {
			ErrorAlert("Please choose a customer");
		}
	}

	public void onEnterTfKhway() {
		if ((tfkhway.getText() != null) && (tfkhway.getText().length() > 2)) {
			amountfocus();
		} else if (tfkhway.getText().length() < 3) {
			ErrorAlert("Invalid Khway No.");
		}
	}

	public void inProcess(boolean starOrNot, String recentAmountOnePara, String recentAmountTwoPara) {
//		String strPlayNumber = tfPlayNumber.getText().toLowerCase();
		String strPlayNumber = tfPlayNumber.getText().toUpperCase();
		String strAmount = tfAmount.getText();
		LocalDate dateFromPicker = date.getValue();
		QueriesForTowDMorning twoDMorning = new QueriesForTowDMorning();
		System.out.println(strAmount + " strAmount");
		System.out.println(tfAmount.getPromptText() + " tfAmount.getPromptText()");
		if (cbChooseCustomer.getValue() == null) {
			ErrorAlert("Please choose a customer");
		} else if (strPlayNumber.isEmpty() && tfPlayNumber.isEditable() && tfkhway.getText().isEmpty()
				&& tfkhway.isEditable()) {
			tfPlayNumber.requestFocus();
//		ErrorAlert("Play number or khway number must be filled");
		} else if (strAmount.isEmpty()) {
			tfAmount.requestFocus();
			ErrorAlert("Amount must be filled");
		} else if (strPlayNumber.length() != 3 && tfPlayNumber.getPromptText().length() != 4
				&& tfPlayNumber.getPromptText().length() != 3 && strPlayNumber.length() != 4
				&& title.getText().equals("3D") && !strPlayNumber.equals("+")
				&& !tfPlayNumber.getPromptText().equals("R(5)") && !tfPlayNumber.getPromptText().equals("R(4)")
				&& !tfPlayNumber.getPromptText().equals("R(3)") && !tfPlayNumber.getPromptText().equals("R(2)")) {
			tfPlayNumber.requestFocus();
			System.out.println("Hello from in process");
			ErrorAlert("Invalid play number..");
			tfAmount.setPromptText(tfAmount.getText());
			tfAmount.clear();
		} else if (tfkhway.getText().length() < 3 && !tfkhway.getText().endsWith("*") && !title.getText().equals("3D")
				&& tfkhway.getPromptText().length() < 3 && strPlayNumber.isEmpty()
				&& tfPlayNumber.getPromptText().isEmpty()) {
			tfkhway.requestFocus();
			ErrorAlert("Khway number must be at least 3 numbers");
		} else if (tfkhway.getText().length() < 4 && tfkhway.getText().endsWith("*") && !title.getText().equals("3D")
				&& tfkhway.getPromptText().length() < 3 && strPlayNumber.isEmpty()
				&& tfPlayNumber.getPromptText().isEmpty()) {
			tfkhway.requestFocus();
			ErrorAlert("Khway number must be at least 3 numbers");
		} else {
			checkAmountNumber();
			String choseName = cbChooseCustomer.getSelectionModel().getSelectedItem().getText();
			int amount = Integer.parseInt(strAmount);

			if (!tfkhway.getText().isEmpty() && !tfPlayNumber.getText().isEmpty()) {
				ErrorAlert("Cannot put play number and khway number at the same time");
			} else if (tfKeyWord.getText().equals("+")) {
				String strPlayNumberReverse = String.valueOf(strPlayNumber.charAt(1))
						.concat(String.valueOf(strPlayNumber.charAt(0)));
				int playNumberReverse = Integer.parseInt(strPlayNumberReverse);

				twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, amount, title.getText(),
						dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, amount, title.getText(),
						dateFromPicker);
				insertIntoList(true, Integer.parseInt(strPlayNumber));

				twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumberReverse, amount, title.getText(),
						dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumberReverse, amount,
						title.getText(), dateFromPicker);

				twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName,
						String.format("%s R", strPlayNumber), amount, title.getText(), dateFromPicker);
				insertIntoList(true, playNumberReverse);

				toAddDataIntoCurTable.clear();
				ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
						dateFromPicker, title.getText());
				toAddDataIntoCurTable.addAll(dataReturnFromDB);
				tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				tfPlayNumber.clear();
				tfKeyWord.clear();
				tfAmount.clear();
				tfPlayNumber.requestFocus();
			} else if (starOrNot == true && strPlayNumber.length() == 2) {
				int playNumber = Integer.parseInt(strPlayNumber);
				int intAmountOne = Integer.parseInt(recentAmountOnePara);
				int intAmountTwo = Integer.parseInt(recentAmountTwoPara);

				String strPlayNumberReverse = String.valueOf(strPlayNumber.charAt(1))
						.concat(String.valueOf(strPlayNumber.charAt(0)));
				int playNumberReverse = Integer.parseInt(strPlayNumberReverse);

				twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, intAmountOne, title.getText(),
						dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, intAmountOne,
						title.getText(), dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumber, intAmountOne,
						title.getText(), dateFromPicker);
				insertIntoList(true, playNumber);

				twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumberReverse, intAmountTwo,
						title.getText(), dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumberReverse, intAmountTwo,
						title.getText(), dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumberReverse, intAmountTwo,
						title.getText(), dateFromPicker);
				insertIntoList(true, playNumberReverse);

				toAddDataIntoCurTable.clear();
				ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
						dateFromPicker, title.getText());
				toAddDataIntoCurTable.addAll(dataReturnFromDB);
				tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				recentOne = "0";

				tfPlayNumber.clear();
				tfKeyWord.clear();
				tfAmount.clear();
				tfPlayNumber.requestFocus();
			} else if (!tfkhway.getText().isEmpty()) {
				ArrayList<String> temp = generateForKhway(tfkhway.getText());
				twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, tfkhway.getText().concat("k"), amount,
						title.getText(), dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, tfkhway.getText().concat("k"), amount,
						title.getText(), dateFromPicker);
				twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, tfkhway.getText(), amount,
						title.getText(), dateFromPicker);
				for (int i = 0; i < temp.size(); i++) {

					int playNumFromStr = Integer.parseInt(temp.get(i));
					insertIntoList(true, playNumFromStr);
				}
				toAddDataIntoCurTable.clear();
				ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
						dateFromPicker, title.getText());
				toAddDataIntoCurTable.addAll(dataReturnFromDB);
				tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				if (tfkhway.isEditable() && all.isSelected()) {
					ObservableList<ObjectForCurrentTable> dataReturnFromDB1 = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					tVCurrentTable.setItems(dataReturnFromDB1.filtered(obj -> obj.getAmount() != 0));
				}
				tfkhway.setOpacity(1);
				tfkhway.setPromptText("");
				tfkhway.setEditable(true);

				tfkhway.clear();
				tfKeyWord.clear();
				tfAmount.clear();
				tfkhway.requestFocus();
			} else if (strPlayNumber.length() == 2) {

				String firstNum = String.valueOf(strPlayNumber.charAt(0));
				String secondNum = String.valueOf(strPlayNumber.charAt(1));

				if (strPlayNumber.equalsIgnoreCase("ss")) {
					ArrayList<String> temp = generateForSS();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "ss", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "SS", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "SS", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				} else if (strPlayNumber.equalsIgnoreCase("mm")) {
					ArrayList<String> temp = generateForMM();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "MM", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "MM", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "MM", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase("sm")) {
					ArrayList<String> temp = generateForSM();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "sm", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "SM", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "SM", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase("ms")) {
					ArrayList<String> temp = generateForMS();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "ms", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "MS", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "MS", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase("sp")) {
					ArrayList<String> temp = generateForSP();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "sp", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "SP", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "SP", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase("mp")) {
					ArrayList<String> temp = generateForMP();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "mp", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "MP", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "MP", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase("**")) {
					ArrayList<String> temp = generateForAA();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "**", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "**", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "**", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase(String.format("*%s", secondNum))) {
					ArrayList<String> temp = generateForAN(secondNum);
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, String.format("*%s", secondNum), amount,
							title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, String.format("*%s", secondNum),
							amount, title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, String.format("*%s", secondNum),
							amount, title.getText(), dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase(String.format("%s*", firstNum))) {
					ArrayList<String> temp = generateForNA(firstNum);
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, String.format("%s*", firstNum), amount,
							title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, String.format("%s*", firstNum), amount,
							title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, String.format("%s*", firstNum),
							amount, title.getText(), dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase(String.format("%sB", firstNum))) {

					ArrayList<String> temp = generateForB(firstNum);
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, String.format("%sB", firstNum), amount,
							title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, String.format("%sB", firstNum), amount,
							title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, String.format("%sB", firstNum),
							amount, title.getText(), dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase(String.format("%s+", firstNum))) {
					ArrayList<String> temp = generateForANNA(firstNum);
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, String.format("%s+", firstNum), amount,
							title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, String.format("%s+", firstNum), amount,
							title.getText(), dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, String.format("%s+", firstNum),
							amount, title.getText(), dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else {
					String lastPlayNum = "";
					int playNumber = Integer.parseInt(strPlayNumber);
					if (!toAddDataIntoCurTable.isEmpty()) {
						lastPlayNum = toAddDataIntoCurTable.get(toAddDataIntoCurTable.size() - 1).getPlayNumber();
					}
					System.out.println("Last Play Number Test " + lastPlayNum);

					if (strPlayNumber.charAt(0) != strPlayNumber.charAt(1) && tfKeyWord.getText().equals("*")
							&& lastPlayNum.endsWith("R")) {
						System.out.println("New * Test ." + lastPlayNum);
						String strPlayNumberReverse = String.valueOf(strPlayNumber.charAt(1))
								.concat(String.valueOf(strPlayNumber.charAt(0)));
						int playNumberReverse = Integer.parseInt(strPlayNumberReverse);

						twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, amount, title.getText(),
								dateFromPicker);
						twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, amount,
								title.getText(), dateFromPicker);
						insertIntoList(true, Integer.parseInt(strPlayNumber));

						twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumberReverse, amount,
								title.getText(), dateFromPicker);
						twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumberReverse, amount,
								title.getText(), dateFromPicker);

						twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName,
								String.format("%s R", strPlayNumber), amount, title.getText(), dateFromPicker);
						insertIntoList(true, playNumberReverse);

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

						tfPlayNumber.clear();
						tfKeyWord.clear();
						tfAmount.clear();
						tfPlayNumber.requestFocus();
					} else if (strPlayNumber.length() == 2 && strPlayNumber.charAt(0) == strPlayNumber.charAt(1)
							&& tfKeyWord.getText().equals("*")) {
						AlertShow.showError("Invalid Play Number");
						tfPlayNumber.requestFocus();
						tfPlayNumber.selectAll();
					} else {
						twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, amount, title.getText(),
								dateFromPicker);
						twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, amount,
								title.getText(), dateFromPicker);
						twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumber, amount,
								title.getText(), dateFromPicker);
						insertIntoList(true, playNumber);
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
						if (!recentOne.equals("0") && controllerForRecentOne.equals("1")) {
							recentOne = "0";
						} else if (tfKeyWord.getText().equals(".") || controllerForRecentOne.equals("0")) {
							recentOne = strPlayNumber;
						}
						if (tfKeyWord.getText().equals(".") && counterForAmountOneAndTwo == 0) {
							recentAmountOne = amount + "";
							System.out.println(recentAmountOne + " first");
							counterForAmountOneAndTwo = 1;
						} else if (tfKeyWord.getText().equals(".") && counterForAmountOneAndTwo != 0) {
							recentAmountTwo = amount + "";
							System.out.println(recentAmountTwo + " second");
							counterForAmountOneAndTwo = 0;
						}
					}
				}
				if (tfPlayNumber.isEditable() && all.isSelected()) {
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));
					last.setSelected(true);
				}
				tfPlayNumber.setOpacity(1);
				tfPlayNumber.setPromptText("");
				tfPlayNumber.setEditable(true);
				tfAmount.setPromptText("");
				tfPlayNumber.clear();
				tfKeyWord.clear();
				tfAmount.clear();
				tfPlayNumber.requestFocus();
			} else if (strPlayNumber.length() == 1 && !title.getText().equals("3D")) {
				if (strPlayNumber.equalsIgnoreCase("n")) {
					ArrayList<String> temp = generateForN();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "n", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "N", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "N", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {
						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.equalsIgnoreCase("p")) {
					ArrayList<String> temp = generateForP();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "p", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "P", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "P", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				}
				if (tfPlayNumber.isEditable() && all.isSelected()) {
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));
					last.setSelected(true);
				}
				tfPlayNumber.setOpacity(1);
				tfPlayNumber.setPromptText("");
				tfPlayNumber.setEditable(true);
				tfPlayNumber.clear();
				tfKeyWord.clear();
				tfAmount.clear();
				tfPlayNumber.requestFocus();
			} else if (strPlayNumber.length() == 4 && !strPlayNumber.endsWith("+") && !strPlayNumber.endsWith("*")
					&& recentAmountTwo.equals("0")) {
				System.out.println("Hello from in cond");
				ErrorAlert("Invalid play number.");
				tfPlayNumber.requestFocus();
				tfPlayNumber.selectAll();
			} else if (((strPlayNumber.length() == 3
					|| (strPlayNumber.length() == 4 && (strPlayNumber.endsWith("+") || strPlayNumber.endsWith("*"))))
					|| strPlayNumber.equals("+")) && title.getText().equals("3D")) {
				String firstNum = "";
				String secondNum = "";
				String thirdNum = "";
				System.out.println("inside 3d");
				if (!strPlayNumber.equals("+")) {
					firstNum = String.valueOf(strPlayNumber.charAt(0));
					secondNum = String.valueOf(strPlayNumber.charAt(1));
					thirdNum = String.valueOf(strPlayNumber.charAt(2));
				}
				if (strPlayNumber.equals("***")) {
					ArrayList<String> temp = generateForAAA();
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, "***", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, "***", amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, "***", amount, title.getText(),
							dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {
						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.startsWith("*")) {
					ArrayList<String> temp = generateForANN(secondNum, thirdNum);
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumber, amount,
							title.getText(), dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (secondNum.equals("*")) {
					ArrayList<String> temp = generateForNAN(firstNum, thirdNum);
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumber, amount,
							title.getText(), dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (strPlayNumber.endsWith("*") && strPlayNumber.length() == 3) {
					ArrayList<String> temp = generateForNNA(firstNum, secondNum);
					twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, amount, title.getText(),
							dateFromPicker);
					twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumber, amount,
							title.getText(), dateFromPicker);
					for (int i = 0; i < temp.size(); i++) {
						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else {
					System.out.println("inside else");
					if (!strPlayNumber.equals("+") && strPlayNumber.length() == 3) {
						String khwayNumFor3D = strPlayNumber;
						String[] khwayNumArrFor3D = khwayNumFor3D.split("");
						if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1)
								&& khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)
								&& khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
							list.clear();
						} else if ((khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(1)
								&& khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(2)
								&& khwayNumFor3D.charAt(1) != khwayNumFor3D.charAt(2))
								&& (storeGet0FromEveryNum.equalsIgnoreCase(strPlayNumber)
										|| storeGet0FromEveryNum.equals("0") || !listForEveryNum.contains(strPlayNumber)
										|| listForRemovedItems.contains(strPlayNumber))) {
							list.clear();
							if (!listForEveryNum.isEmpty()) {
								storeGet0FromEveryNum = listForEveryNum.get(0);
							}
							listForEveryNum.clear();
							firstTimeController = false;
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
								|| khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)
								|| khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)))
								&& (storeGet0FromEveryNum.equals(strPlayNumber) || storeGet0FromEveryNum.equals("0")
										|| !listForEveryNum.contains(strPlayNumber)
										|| listForRemovedItems.contains(strPlayNumber))) {
							list.clear();
							if (!listForEveryNum.isEmpty()) {
								storeGet0FromEveryNum = listForEveryNum.get(0);
							}
							listForEveryNum.clear();
							firstTimeController = false;
							for (int i = 0; i < khwayNumArrFor3D.length; i++) {
								for (int j = 0; j < khwayNumArrFor3D.length; j++) {
									aa: for (int k = 0; k < khwayNumArrFor3D.length; k++) {
										String formulatedNum = khwayNumArrFor3D[i].concat(khwayNumArrFor3D[j])
												.concat(khwayNumArrFor3D[k]);
										String sameThree = "";
										for (int a = 0; a < khwayNumFor3D.length(); a++) {

											sameThree = String.valueOf(khwayNumFor3D.charAt(a))
													+ String.valueOf(khwayNumFor3D.charAt(a))
													+ String.valueOf(khwayNumFor3D.charAt(a));

											listForEveryNum.add(formulatedNum);
											if (sameThree.equals(formulatedNum)
													|| formulatedNum.equals(khwayNumFor3D)) {
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

//		HERE && !listForEveryNum.get(0).equals(strPlayNumber) storeGet0FromEveryNum
						if (!listForRemovedItems.contains(strPlayNumber) && !storeGet0FromEveryNum.equals(strPlayNumber)
								&& listForEveryNum.contains(strPlayNumber)) {
							for (int a = 0; a < listForEveryNum.size(); a++) {
								if (strPlayNumber.equals(listForEveryNum.get(a))) {
									countToAddToR++;
//		listForRemovedItems.clear();
									listForRemovedItems.add(listForEveryNum.get(a));
								}
							}
						} else {
							countToAddToR = 0;
							listForRemovedItems.clear();
							listForRemovedItems.add(strPlayNumber);
						}

						if (!listForRemovedItems.isEmpty()) {
							for (int a = 0; a < list.size(); a++) {
								for (int b = 0; b < listForRemovedItems.size(); b++) {
									if (list.get(a).equals(listForRemovedItems.get(b))) {
										list.remove(list.get(a));
										System.out.println(list.size() + " list size");
										break;
									}
								}
							}
						}
					}
					if (list.isEmpty() && strPlayNumber.equals("+")) {
						ErrorAlert("Invalid play number");
					}
					if (tfPlayNumber.getText().length() == 3) {
						recentOne = strPlayNumber;
						counterForAmountOneAndTwo = 0;
					}
					if (tfPlayNumber.getText().length() == 3 && counterForAmountOneAndTwo == 0) {
						recentAmountOne = amount + "";
						System.out.println(recentAmountOne + " first");
						counterForAmountOneAndTwo = 1;
					} else if (tfPlayNumber.getText().equals("+") && counterForAmountOneAndTwo != 0) {
						recentAmountTwo = amount + "";
						System.out.println(recentAmountTwo + " second");
						counterForAmountOneAndTwo = 0;
					}
					String lastPlayNum = "";
					if (!toAddDataIntoCurTable.isEmpty()) {
						lastPlayNum = toAddDataIntoCurTable.get(toAddDataIntoCurTable.size() - 1).getPlayNumber();
					}
					if (strPlayNumber.length() == 4 && strPlayNumber.endsWith("*") && !lastPlayNum.endsWith("R")) {
						String strPlayNumWithoutStar = strPlayNumber.substring(0, strPlayNumber.length() - 1);
						if (strPlayNumWithoutStar.charAt(0) == strPlayNumWithoutStar.charAt(1)
								&& strPlayNumWithoutStar.charAt(0) == strPlayNumWithoutStar.charAt(2)
								&& strPlayNumWithoutStar.charAt(1) == strPlayNumWithoutStar.charAt(2)) {
							ErrorAlert("Invalid play number-");
						} else {
							ArrayList<String> listFromMethod = methodToDerive3DKway(strPlayNumWithoutStar);

							twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumWithoutStar,
									Integer.parseInt(recentAmountOnePara), title.getText(), dateFromPicker);
							twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumWithoutStar,
									Integer.parseInt(recentAmountOnePara), title.getText(), dateFromPicker);
							twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumWithoutStar,
									Integer.parseInt(recentAmountOnePara), title.getText(), dateFromPicker);
							insertIntoList(true, Integer.parseInt(strPlayNumWithoutStar));

							if (strPlayNumWithoutStar.charAt(0) != strPlayNumWithoutStar.charAt(1)
									&& strPlayNumWithoutStar.charAt(0) != strPlayNumWithoutStar.charAt(2)
									&& strPlayNumWithoutStar.charAt(1) != strPlayNumWithoutStar.charAt(2)) {
								listFromMethod.remove(0);
							} else if (strPlayNumWithoutStar.charAt(0) == strPlayNumWithoutStar.charAt(1)
									|| strPlayNumWithoutStar.charAt(0) == strPlayNumWithoutStar.charAt(2)
									|| strPlayNumWithoutStar.charAt(1) == strPlayNumWithoutStar.charAt(2)) {
								if (strPlayNumWithoutStar.charAt(0) == strPlayNumWithoutStar.charAt(1)
										|| strPlayNumWithoutStar.charAt(1) == strPlayNumWithoutStar.charAt(2)) {
									listFromMethod.remove(0);
								} else if (strPlayNumWithoutStar.charAt(0) == strPlayNumWithoutStar.charAt(2)) {
									listFromMethod.remove(1);
								}
								System.out.println(listFromMethod + " listFromMethod");
							}
							System.out.println(listFromMethod + " listFromMethod");
							for (int i = 0; i < listFromMethod.size(); i++) {
								twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, listFromMethod.get(i),
										Integer.parseInt(recentAmountTwoPara), title.getText(), dateFromPicker);
								twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, listFromMethod.get(i),
										Integer.parseInt(recentAmountTwoPara), title.getText(), dateFromPicker);
								insertIntoList(true, Integer.parseInt(listFromMethod.get(i)));
							}
							twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName,
									String.format("R(%d)", listFromMethod.size()),
									Integer.parseInt(recentAmountTwoPara), title.getText(), dateFromPicker);

							toAddDataIntoCurTable.clear();
							ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
									.extractDataForLastOption(choseName, dateFromPicker, title.getText());
							toAddDataIntoCurTable.addAll(dataReturnFromDB);
							tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
							listFromMethod.clear();
						}
					} else if ((strPlayNumber.length() == 4 && strPlayNumber.endsWith("+"))
							|| (strPlayNumber.length() == 4 && strPlayNumber.endsWith("*")
									&& lastPlayNum.endsWith("R"))) {
						String strPlayNumWithoutPlus = strPlayNumber.substring(0, strPlayNumber.length() - 1);
						ArrayList<String> listFromMethod = methodToDerive3DKway(strPlayNumWithoutPlus);
						for (int i = 0; i < listFromMethod.size(); i++) {
							twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, listFromMethod.get(i), amount,
									title.getText(), dateFromPicker);
							twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, listFromMethod.get(i), amount,
									title.getText(), dateFromPicker);
							insertIntoList(true, Integer.parseInt(listFromMethod.get(i)));
						}
						twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName,
								String.format("%s R", strPlayNumWithoutPlus), amount, title.getText(), dateFromPicker);
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
						listFromMethod.clear();
					} else if (!tfPlayNumber.getText().equals("+")) {
						System.out.println("inside not +");
						int playNumber = Integer.parseInt(strPlayNumber);
						twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, strPlayNumber, amount, title.getText(),
								dateFromPicker);
						twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, strPlayNumber, amount,
								title.getText(), dateFromPicker);
						twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, strPlayNumber, amount,
								title.getText(), dateFromPicker);
						insertIntoList(true, playNumber);
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getText().equals("+") && list.size() != 0) {
						System.out.println("inside + ");
						for (int i = 0; i < list.size(); i++) {
//		System.out.println("inside + loop");
							twoDMorning.insertDataIntoTwoDUserPlayTable(choseName, list.get(i), amount, title.getText(),
									dateFromPicker);
							twoDMorning.insertDataIntoTwoDUserPlayForCurTable(choseName, list.get(i), amount,
									title.getText(), dateFromPicker);
//		twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, list.get(i), amount,
//		title.getText(), dateFromPicker);
							insertIntoList(true, Integer.parseInt(list.get(i)));
						}
						if (list.size() == 1) {
							twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName, list.get(0), amount,
									title.getText(), dateFromPicker);
						} else {
							twoDMorning.insertDataIntoTwoDUserPlayForCurTableForLast(choseName,
									String.format("R(%d)", list.size()), amount, title.getText(), dateFromPicker);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
						list.clear();
						listForRemovedItems.clear();
					}
					System.out.println(listForEveryNum + " listForEveryNum");
					System.out.println(list + " list");
					System.out.println(listForRemovedItems + " listForRemovedItems");
				}
				tfPlayNumber.setOpacity(1);
				tfPlayNumber.setPromptText("");
				tfPlayNumber.setEditable(true);
				tfPlayNumber.clear();
				tfAmount.clear();
				tfPlayNumber.requestFocus();
			} else if (!tfPlayNumber.isEditable()) {
				String playNumPromp = tfPlayNumber.getPromptText();
				if (playNumPromp.contains("R")) {
					for (int i = 0; i < previewTable.getItems().size(); i++) {
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName,
								previewTable.getItems().get(i).getPlayNumber(), amount, title.getText(), dateFromPicker,
								"last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName,
								previewTable.getItems().get(i).getPlayNumber(), amount, title.getText(), dateFromPicker,
								"last", toExtractForUpdate);
						insertIntoList(true, Integer.parseInt(previewTable.getItems().get(i).getPlayNumber()));
					}
					twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLastForR(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last", toExtractForUpdate,
							tVCurrentTable.getSelectionModel().getSelectedIndex());
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					previewTable.setItems(null);
				} else if (playNumPromp.length() == 2) {
					String firstNum = String.valueOf(playNumPromp.charAt(0));
					String secondNum = String.valueOf(playNumPromp.charAt(1));

					if (tfPlayNumber.getPromptText().equalsIgnoreCase("ss")) {
						ArrayList<String> temp = generateForSS();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("mm")) {
						ArrayList<String> temp = generateForMM();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("sm")) {
						ArrayList<String> temp = generateForSM();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("ms")) {
						ArrayList<String> temp = generateForMS();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("sp")) {
						ArrayList<String> temp = generateForSP();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("mp")) {
						ArrayList<String> temp = generateForMP();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("**")) {
						ArrayList<String> temp = generateForAA();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase(String.format("*%s", secondNum))) {
						ArrayList<String> temp = generateForAN(secondNum);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase(String.format("%s*", firstNum))) {
						ArrayList<String> temp = generateForNA(firstNum);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase(String.format("%sb", firstNum))) {
						ArrayList<String> temp = generateForNA(firstNum);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase(String.format("%s+", firstNum))) {
						ArrayList<String> temp = generateForANNA(firstNum);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else {
						int playNumber = Integer.parseInt(tfPlayNumber.getPromptText());
//		HERE UPDATE
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						insertIntoList(true, playNumber);

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					}
				} else if (playNumPromp.length() == 1) {
					if (tfPlayNumber.getPromptText().equalsIgnoreCase("n")) {
						ArrayList<String> temp = generateForN();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("p")) {
						ArrayList<String> temp = generateForP();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					}
				} else if (playNumPromp.length() == 3 && title.getText().equals("3D")) {
					String firstNum = String.valueOf(playNumPromp.charAt(0));
					String secondNum = String.valueOf(playNumPromp.charAt(1));
					String thirdNum = String.valueOf(playNumPromp.charAt(2));
					if (playNumPromp.equals("***")) {
						ArrayList<String> temp = generateForAAA();
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (playNumPromp.startsWith("*")) {
						ArrayList<String> temp = generateForANN(secondNum, thirdNum);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (secondNum.equals("*")) {
						ArrayList<String> temp = generateForNAN(firstNum, thirdNum);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else if (playNumPromp.endsWith("*")) {
						ArrayList<String> temp = generateForNNA(firstNum, secondNum);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						for (int i = 0; i < temp.size(); i++) {
							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					} else {
						int playNumber = Integer.parseInt(playNumPromp);
						twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last", toExtractForUpdate);
						insertIntoList(true, playNumber);
						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					}
				}
				if (!tfPlayNumber.isEditable() && all.isSelected()) {
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));
				}
				tfPlayNumber.setOpacity(1);
				tfPlayNumber.setPromptText("");
				tfPlayNumber.setEditable(true);

				tfPlayNumber.clear();
				tfKeyWord.clear();
				tfAmount.clear();
				tfPlayNumber.requestFocus();
			} else if (!tfkhway.isEditable()) {
				String khwayNumPromp = tfkhway.getPromptText();
				ArrayList<String> temp = generateForKhway(khwayNumPromp);
				twoDMorning.updateDataIntoTwoDUserPlayTable(choseName, khwayNumPromp.concat("k"), amount,
						title.getText(), dateFromPicker, "last", toExtractForUpdate);
				twoDMorning.updateDataIntoTwoDUserPlayTableForCurTable(choseName, khwayNumPromp.concat("k"), amount,
						title.getText(), dateFromPicker, "last", toExtractForUpdate);
				twoDMorning.updateDataIntoTwoDUserPlayTableForCurTableForLast(choseName, khwayNumPromp, amount,
						title.getText(), dateFromPicker, "last", toExtractForUpdate);
				for (int i = 0; i < temp.size(); i++) {

					int playNumFromStr = Integer.parseInt(temp.get(i));
					insertIntoList(true, playNumFromStr);
				}

				toAddDataIntoCurTable.clear();
				ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
						dateFromPicker, title.getText());
				toAddDataIntoCurTable.addAll(dataReturnFromDB);
				tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				if (!tfkhway.isEditable() && all.isSelected()) {
					ObservableList<ObjectForCurrentTable> dataReturnFromDB1 = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					tVCurrentTable.setItems(dataReturnFromDB1.filtered(obj -> obj.getAmount() != 0));
				}
				tfkhway.setOpacity(1);
				tfkhway.setPromptText("");
				tfkhway.setEditable(true);
				tfKeyWord.setOpacity(1);
				tfKeyWord.setPromptText("");
				tfKeyWord.setEditable(true);
				tfkhway.clear();
				tfKeyWord.clear();
				tfAmount.clear();
				tfkhway.requestFocus();
				tfPlayNumber.setDisable(false);
			}
			toCalculateTotalAmountForCurrentTable();
		}
		tfAmount.setPromptText(strAmount);
		tVCurrentTable.scrollTo(tVCurrentTable.getItems().size());
		totalvalueset();
		totalplaycountset();
	}// inprocess end

	private void checkNumberError() {
		if (cbChooseCustomer.getValue() == null) {
			throw new TwoDThreeDException("Please choose customer name");
		}
		if (tfPlayNumber.getText().isEmpty() && tfkhway.getText().isEmpty()) {
			throw new TwoDThreeDException("Please enter play number or khway number");
		}
		try {
			if (!tfPlayNumber.getText().isEmpty()) {
				String strPlayNum = tfPlayNumber.getText();
				if (!strPlayNum.equalsIgnoreCase("ss") && !strPlayNum.equalsIgnoreCase("mm")
						&& !strPlayNum.equalsIgnoreCase("sm") && !strPlayNum.equalsIgnoreCase("ms")
						&& !strPlayNum.equalsIgnoreCase("sp") && !strPlayNum.equalsIgnoreCase("mp")
						&& !strPlayNum.equalsIgnoreCase("**") && !strPlayNum.startsWith("*")
						&& !strPlayNum.endsWith("*") && !strPlayNum.equalsIgnoreCase("p")
						&& !strPlayNum.equalsIgnoreCase("n") && !strPlayNum.endsWith("B")
						&& !title.getText().equalsIgnoreCase("3D")) {
					Integer.parseInt(tfPlayNumber.getText());
				}
			}
		} catch (NumberFormatException e) {
			throw new TwoDThreeDException("Wrong input for play number");
		}
		tfPlayNumber.requestFocus();
		tfPlayNumber.selectAll();

		if (!tfPlayNumber.getText().isEmpty()) {
			String strPlayNum = tfPlayNumber.getText();
			if (!strPlayNum.equalsIgnoreCase("ss") && !strPlayNum.equalsIgnoreCase("mm")
					&& !strPlayNum.equalsIgnoreCase("sm") && !strPlayNum.equalsIgnoreCase("ms")
					&& !strPlayNum.equalsIgnoreCase("sp") && !strPlayNum.equalsIgnoreCase("mp")
					&& !strPlayNum.equalsIgnoreCase("**") && !strPlayNum.startsWith("*") && !strPlayNum.endsWith("*")
					&& !strPlayNum.equalsIgnoreCase("p") && !strPlayNum.equalsIgnoreCase("n")
					&& !strPlayNum.endsWith("B") && !title.getText().equalsIgnoreCase("3D")) {
				int number = Integer.parseInt(tfPlayNumber.getText());
				if (!title.getText().equalsIgnoreCase("3D")) {
					if (!(number >= 0 && number <= 99)) {
						throw new TwoDThreeDException("Please enter play number between 0 to 99");
					}
				}
			}
		}

	}

	private void edit(OutSource outSource, String customerID) {
		outSource = currentTable.getSelectionModel().getSelectedItem();
		int getExtractAmount = twoDService.getExtractAmount(Integer.parseInt(tfPlayNumber.getText()), morE,
				date.getValue());
		int playNumber = Integer.parseInt(outSource.getExceedingNumber());
		int tableCurrentAmount = outSource.getCurrentAmt();
		int enterCurrentAmouont = Integer.parseInt(tfAmount.getText());
		int temp = tableCurrentAmount - enterCurrentAmouont;
		int result = getExtractAmount - temp;
		System.out.println(outSourceService.updateCurrentAmount(customerID, playNumber, morE, date.getValue(),
				tableCurrentAmount, enterCurrentAmouont));
		twoDService.updateExtract_amountZero(new TwoDEntity(morE, date.getValue(), playNumber, result));
		setDataToCurrentTable();
		insertIntoList(true, playNumber);

	}

//For Trial Version
	public void loadDeleteDate() {
		AlertShow.showWarning("Not Available for Trial Version: ");
//		if (title.getText().equals("3D")) {
//			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete?", ButtonType.YES,
//					ButtonType.NO);
//			Optional<ButtonType> op = alert.showAndWait();
//			if (op.get() == ButtonType.NO) {
//				alert.close();
//			} else if (op.get() == ButtonType.YES) {
//				twoDService.deleteWithDate(null, null, DataPass.getMeDataPass());
//				exceedingService.deleteWithDate(null, null, DataPass.getMeDataPass());
//				winNumberService.deleteWithDate(null, null, DataPass.getMeDataPass());
//				AlertShow.showInfo("Delete Success");
//				setDatainMaxTable();
//				getWinNumbers();
//				insertIntoList(false, 0);
//				currentTable.getItems().clear();
//				tVCurrentTable.setItems(null);
//				totalAll.setText("Total   :  "
//						+ df.format(new QueriesForTowDMorning().getAllTotalData(morE, date.getValue())) + "  MMK");
//				totalplaycount.setText("0");
//				totalAmountCurTable.setText("0");
//			}
//		} else {
//			loadViewWithNormalStage("DeleteDateView", "Date Customization");
//		}
	}

	public void enterAmount() {
		try {
			if (in.isSelected()) {
				inProcess(false, "0", "0");
				previewTable.setItems(null);
			} else {
				checkAmountNumber();
				if (cbChooseCustomer.getValue() != null) {
					String customerID = customerService.getCustomerId(cbChooseCustomer.getValue().getText());
					if (tfPlayNumber.isDisable()) {
						OutSource outSource = currentTable.getSelectionModel().getSelectedItem();
						if (outSource != null) {
							edit(outSource, customerID);
						}
					} else {
						if (exceeding != null) {
							if (exceeding.size() > 1) {
								List<Exceeding> list = new ArrayList<Exceeding>();
								outSelectedMultiProcess(list, customerID);
								exceeding = null;
							} else {
								outSelectedProcess(customerID, exceeding.get(0));
								exceeding = null;
							}
						} else {
							outSelectedProcess(customerID, withoutObj);
							withoutObj = null;
						}
					}
				} else {
					ErrorAlert("Please choose customer");
					cbChooseCustomer.requestFocus();
				}
				setDataToCurrentTable();
				tfAmount.clear();
				tfKeyWord.clear();
				tfPlayNumber.clear();
				tfPlayNumber.requestFocus();
				tfPlayNumber.setEditable(true);
				tfPlayNumber.setOpacity(1);
				tfAmount.setEditable(true);
				tfKeyWord.setDisable(true);
				setDatainMaxTable();
			}
//			totalvalueset();
//			totalAll.setText("Total   :  "
//					+ df.format(new QueriesForTowDMorning().getAllTotalData(morE, date.getValue())) + "  MMK");
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
			tfPlayNumber.requestFocus();
			tfPlayNumber.selectAll();
			tfAmount.setPromptText(tfAmount.getText());
			tfAmount.clear();
		}
	}

	public void addComboBoxtoCustomerInOut(boolean condition) {
		ArrayList<Label> customerNames = customerService.getAllCustomerData(condition);
		cbChooseCustomer.getItems().clear();
		cbChooseCustomer.getItems().setAll(customerNames);
		totalCustomerAmount.setText("Agent - " + customerService.getTotalCustomerNumber(condition));
		tfAmount.clear();
		tfKeyWord.clear();
		tfPlayNumber.clear();
	}

	public void loadMorinigView() {
		DataPass.title("Morning",
				"M12 6.5c3.79 0 7.17 2.13 8.82 5.5-1.65 3.37-5.02 5.5-8.82 5.5S4.83 15.37 3.18 12C4.83 8.63 8.21 6.5 12 6.5m0-2C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zm0 5c1.38 0 2.5 1.12 2.5 2.5s-1.12 2.5-2.5 2.5-2.5-1.12-2.5-2.5 1.12-2.5 2.5-2.5m0-2c-2.48 0-4.5 2.02-4.5 4.5s2.02 4.5 4.5 4.5 4.5-2.02 4.5-4.5-2.02-4.5-4.5-4.5z");
		new LoadView().loadViewWithStage("TwoDThreeDHome", DataPass.getMeDataPass());
	}

	public void loadEveingView() {
		DataPass.title("Evening",
				"M12 6c3.79 0 7.17 2.13 8.82 5.5-.59 1.22-1.42 2.27-2.41 3.12l1.41 1.41c1.39-1.23 2.49-2.77 3.18-4.53C21.27 7.11 17 4 12 4c-1.27 0-2.49.2-3.64.57l1.65 1.65C10.66 6.09 11.32 6 12 6zm-1.07 1.14L13 9.21c.57.25 1.03.71 1.28 1.28l2.07 2.07c.08-.34.14-.7.14-1.07C16.5 9.01 14.48 7 12 7c-.37 0-.72.05-1.07.14zM2.01 3.87l2.68 2.68C3.06 7.83 1.77 9.53 1 11.5 2.73 15.89 7 19 12 19c1.52 0 2.98-.29 4.32-.82l3.42 3.42 1.41-1.41L3.42 2.45 2.01 3.87zm7.5 7.5l2.61 2.61c-.04.01-.08.02-.12.02-1.38 0-2.5-1.12-2.5-2.5 0-.05.01-.08.01-.13zm-3.4-3.4l1.75 1.75c-.23.55-.36 1.15-.36 1.78 0 2.48 2.02 4.5 4.5 4.5.63 0 1.23-.13 1.77-.36l.98.98c-.88.24-1.8.38-2.75.38-3.79 0-7.17-2.13-8.82-5.5.7-1.43 1.72-2.61 2.93-3.53z");
		new LoadView().loadViewWithStage("TwoDThreeDHome", DataPass.getMeDataPass());
	}

	public void load3DView() {
		DataPass.title("3D",
				"M12 6.5c3.79 0 7.17 2.13 8.82 5.5-1.65 3.37-5.02 5.5-8.82 5.5S4.83 15.37 3.18 12C4.83 8.63 8.21 6.5 12 6.5m0-2C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zm0 5c1.38 0 2.5 1.12 2.5 2.5s-1.12 2.5-2.5 2.5-2.5-1.12-2.5-2.5 1.12-2.5 2.5-2.5m0-2c-2.48 0-4.5 2.02-4.5 4.5s2.02 4.5 4.5 4.5 4.5-2.02 4.5-4.5-2.02-4.5-4.5-4.5z");
		new LoadView().loadViewWithStage("TwoDThreeDHome", DataPass.getMeDataPass());
	}

	public void remain() {
		try {
			checkRemainError();
			for (Node node : fpForTenButtons.getChildren()) {
				if (node instanceof Button) {
					Button b = (Button) node;
					if (b.getText().equals(tfRemain.getText())) {
						b.setVisible(true);
					}
				}
			}
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
		tfRemain.clear();
	}

	public void remove() {
		try {
			checkRemoveError();
			for (Node node : fpForTenButtons.getChildren()) {
				if (node instanceof Button) {
					Button b = (Button) node;
					if (b.getText().equals(tfRemove.getText())) {
						b.setVisible(false);
					}
				}
			}
		} catch (Exception e) {
			AlertShow.showError(e.getMessage());
		}
		tfRemove.clear();
	}

	public void addnewButton() {
		fpForTenButtons.getChildren().clear();
		for (int i = 0; i < 10; i++) {
			Button b = new Button();
			b.setStyle("-fx-font-weight: bold;-fx-background-color: #ffbb00;");
			if (i == 9) {
				b.setText("0");
			} else {
				b.setText(String.valueOf(i + 1));
			}
			fpForTenButtons.getChildren().add(b);
		}
	}

	private void insertIntoList(boolean isPlayNumberEmpty, int playNumber) {
		if (!isPlayNumberEmpty) {
			list1.getItems().clear();
			list2.getItems().clear();
			list3.getItems().clear();
			list4.getItems().clear();
			list5.getItems().clear();
			list6.getItems().clear();
			list7.getItems().clear();
			list8.getItems().clear();
			list9.getItems().clear();
			list10.getItems().clear();
			if (!morE.equals("3D")) {
				for (int i = 0; i < 100; i++) {
					int amount = twoDService.toShowAmount(i, morE, date.getValue());
					HBox hb = buildHboxLayOut(amount, i);
					if (i >= 0 && i < 10) {
						list1.getItems().add(hb);
					} else if (i >= 10 && i < 20) {
						list2.getItems().add(hb);
					} else if (i >= 20 && i < 30) {
						list3.getItems().add(hb);
					} else if (i >= 30 && i < 40) {
						list4.getItems().add(hb);
					} else if (i >= 40 && i < 50) {
						list5.getItems().add(hb);
					} else if (i >= 50 && i < 60) {
						list6.getItems().add(hb);
					} else if (i >= 60 && i < 70) {
						list7.getItems().add(hb);
					} else if (i >= 70 && i < 80) {
						list8.getItems().add(hb);
					} else if (i >= 80 && i < 90) {
						list9.getItems().add(hb);
					} else if (i >= 90 && i < 100) {
						list10.getItems().add(hb);
					}
				}
			} else {
				for (int i = 0; i < 910; i += 100) {
					for (int j = 0; j < 10; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list1.getItems().add(hb);
					}
				}
				for (int i = 0; i < 920; i += 100) {
					for (int j = 10; j < 20; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list2.getItems().add(hb);
					}
				}
				for (int i = 0; i < 930; i += 100) {
					for (int j = 20; j < 30; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list3.getItems().add(hb);
					}
				}
				for (int i = 0; i < 940; i += 100) {
					for (int j = 30; j < 40; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list4.getItems().add(hb);
					}
				}
				for (int i = 0; i < 950; i += 100) {
					for (int j = 40; j < 50; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list5.getItems().add(hb);
					}
				}
				for (int i = 0; i < 960; i += 100) {
					for (int j = 50; j < 60; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list6.getItems().add(hb);
					}
				}
				for (int i = 0; i < 970; i += 100) {
					for (int j = 60; j < 70; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list7.getItems().add(hb);
					}
				}
				for (int i = 0; i < 980; i += 100) {
					for (int j = 70; j < 80; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list8.getItems().add(hb);
					}
				}
				for (int i = 0; i < 990; i += 100) {
					for (int j = 80; j < 90; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list9.getItems().add(hb);
					}
				}
				for (int i = 0; i < 1000; i += 100) {
					for (int j = 90; j < 100; j++) {
						int amount = twoDService.toShowAmount(i + j, morE, date.getValue());
						HBox hb = buildHboxLayOut(amount, i + j);
						list10.getItems().add(hb);
					}
				}
			}
		} else {
			int amount = twoDService.toShowAmount(playNumber, morE, date.getValue());

			int getSetData = Integer.parseInt(setAmount.getText());
			if (amount > getSetData) {
				int isHaveExceed = exceedingService.isExceed(new Exceeding(playNumber, morE, date.getValue()));
				int exceed = amount - getSetData;
				if (isHaveExceed == -1) {
					System.out.println("Insert");
					exceedingService.insert(new Exceeding(playNumber, exceed, date.getValue(), morE, false));
				} else {
					System.out.println("Update");
					exceedingService.updateAmount(new Exceeding(playNumber, exceed, date.getValue(), morE));
				}
			} else if (amount <= getSetData) {
				int isHaveExceed = exceedingService.isExceed(new Exceeding(playNumber, morE, date.getValue()));
				if (isHaveExceed != -1 && isDelete) {
					System.out.println("Delete");
					exceedingService.deletExceeding(new Exceeding(playNumber, morE, date.getValue()));
					isDelete = false;
				}
			}

			if (!morE.equals("3D")) {
				if (playNumber >= 0 && playNumber < 10) {
					loop(list1, 0, 10, playNumber, amount);
				} else if (playNumber >= 10 && playNumber < 20) {
					loop(list2, 10, 20, playNumber, amount);
				} else if (playNumber >= 20 && playNumber < 30) {
					loop(list3, 20, 30, playNumber, amount);
				} else if (playNumber >= 30 && playNumber < 40) {
					loop(list4, 30, 40, playNumber, amount);
				} else if (playNumber >= 40 && playNumber < 50) {
					loop(list5, 40, 50, playNumber, amount);
				} else if (playNumber >= 50 && playNumber < 60) {
					loop(list6, 50, 60, playNumber, amount);
				} else if (playNumber >= 60 && playNumber < 70) {
					loop(list7, 60, 70, playNumber, amount);
				} else if (playNumber >= 70 && playNumber < 80) {
					loop(list8, 70, 80, playNumber, amount);
				} else if (playNumber >= 80 && playNumber < 90) {
					loop(list9, 80, 90, playNumber, amount);
				} else if (playNumber >= 90 && playNumber < 100) {
					loop(list10, 90, 100, playNumber, amount);
				}
			} else {
				String format = String.format("%03d", playNumber);
				String playSplit[] = String.valueOf(format).split("");
				int middleValue = Integer.parseInt(playSplit[1]);
				if (middleValue == 0) {
					loop3D(list1, 0, 910, 0, 10, playNumber, amount);
				} else if (middleValue == 1) {
					loop3D(list2, 0, 920, 10, 20, playNumber, amount);
				} else if (middleValue == 2) {
					loop3D(list3, 0, 930, 20, 30, playNumber, amount);
				} else if (middleValue == 3) {
					loop3D(list4, 0, 940, 30, 40, playNumber, amount);
				} else if (middleValue == 4) {
					loop3D(list5, 0, 950, 40, 50, playNumber, amount);
				} else if (middleValue == 5) {
					loop3D(list6, 0, 960, 50, 60, playNumber, amount);
				} else if (middleValue == 6) {
					loop3D(list7, 0, 970, 60, 70, playNumber, amount);
				} else if (middleValue == 7) {
					loop3D(list8, 0, 980, 70, 80, playNumber, amount);
				} else if (middleValue == 8) {
					loop3D(list9, 0, 990, 80, 90, playNumber, amount);
				} else if (middleValue == 9) {
					loop3D(list10, 0, 1000, 90, 100, playNumber, amount);
				}
			}
			setDatainMaxTable();
		}
	}

	private void loop(ListView<HBox> list, int start, int end, int pN, int twoDAmount) {

		int index = 0;
		for (int i = start; i < end; i++) {
			if (i == pN) {
				HBox hb = buildHboxLayOut(twoDAmount, pN);
				list.getItems().set(index, hb);
				index = 0;
				break;
			}
			index++;
		}
	}

	private void loop3D(ListView<HBox> list, int upStart, int upEnd, int start, int end, int pN, int twoDAmount) {
		int index = 0;
		for (int i = upStart; i < upEnd; i += 100) {
			for (int j = start; j < end; j++) {
				if (i + j == pN) {
					HBox hb = buildHboxLayOut(twoDAmount, pN);
					list.getItems().set(index, hb);
					index = 0;
					break;
				}
				index++;
			}
		}
	}

	private void disSelectListView(ListView<HBox> list) {
		if (list.equals(list1)) {
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list2)) {
			list1.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list3)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list4)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list5)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list6)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list7)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list8)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list9)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list10.getSelectionModel().clearSelection();
		} else if (list.equals(list10)) {
			list1.getSelectionModel().clearSelection();
			list2.getSelectionModel().clearSelection();
			list3.getSelectionModel().clearSelection();
			list4.getSelectionModel().clearSelection();
			list5.getSelectionModel().clearSelection();
			list6.getSelectionModel().clearSelection();
			list7.getSelectionModel().clearSelection();
			list8.getSelectionModel().clearSelection();
			list9.getSelectionModel().clearSelection();
		}
	}

	private void setInMouseClickonListView(ListView<HBox> list) {
		list.setOnMouseClicked(e -> {
			disSelectListView(list);
			if (e.getClickCount() == 2) {
				HBox result = list.getSelectionModel().getSelectedItem();
				Label getPlayNumber = (Label) result.getChildren().get(0);
				Label getAmount = (Label) result.getChildren().get(1);
				String playNumber = getPlayNumber.getText();
				String playAmount = getAmount.getText();
				if (playAmount.contains(",")) {
					playAmount = playAmount.replace(",", "");
				}
				showDetailsAndOutList(playNumber);
			}
		});
	}

	private void showDetailsAndOutList(String text) {
		if (!text.isEmpty()) {
			ArrayList<TwoDEntity> listTemp = twoDService
					.getDetails(new TwoDEntity(morE, date.getValue(), Integer.parseInt(text)));
			deatils.getItems().clear();
			deatils.getItems().addAll(listTemp);
			customerCount.setText("Customer-" + listTemp.size());
			int twoOrThree = DataPass.getMeDataPass().equals("Morning") || DataPass.getMeDataPass().equals("Evening")
					? 2
					: 3;
			String format = String.format("%" + "0" + twoOrThree + "d", Integer.parseInt(text));
			detailsTableNumber.setText("Number-" + format);
			int temp2 = 0;
			for (TwoDEntity twoDEntity : listTemp) {
				temp2 += twoDEntity.getAmount();
			}
			dateailTotal.setText(String.valueOf(df.format(temp2)));
			ArrayList<OutSource> outListDul = outSourceService.getOutsourcesData(Integer.parseInt(text), morE,
					date.getValue());
			this.outList.getItems().clear();
			this.outList.getItems().addAll(outListDul);
			int temp = 0;
			for (OutSource outSource : outListDul) {
				temp += outSource.getCurrentAmt();
			}
			outTotal.setText(df.format(temp));
			if (deatilsBox.getChildren().size() == 2 && !outListDul.isEmpty()) {
				deatilsBox.getChildren().add(2, outTableAndoutTotal);
			} else if (deatilsBox.getChildren().size() == 3 && outListDul.isEmpty()) {
				deatilsBox.getChildren().remove(2);
			}
		}
	}

	public void amountfocus() {
		tfAmount.requestFocus();
		tfAmount.setText(tfAmount.getPromptText());
		tfAmount.selectAll();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfPlayNumber.setDisable(true);
		tfKeyWord.setDisable(true);
		tfAmount.setDisable(true);
		tfkhway.setDisable(true);
		LocalDate now = LocalDate.now();
		LocalDate day1 = null;
		setAmount.setText(new SetAmountImplementation().getamount() + "");
		int monthOfNow = now.getMonthValue();
//		HERE
		if (monthOfNow == 12) {
			monthOfNow = 1;
			day1 = LocalDate.of(now.getYear(), monthOfNow, 1);
		} else {
			day1 = LocalDate.of(now.getYear(), monthOfNow + 1, 1);
		}

		LocalDate day1SameMonth = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
		LocalDate day16 = LocalDate.of(now.getYear(), now.getMonthValue(), 16);

		if (DataPass.getSvg() != null) {
			svg.setContent(DataPass.getSvg());
		}

		if (((now.isAfter(day1SameMonth) && now.isBefore(day16)) || now.isEqual(day16))
				&& title.getText().equals("3D")) {
			date.setValue(day16);
		} else if (((now.isAfter(day16) && now.isBefore(day1)) || now.isEqual(day1)) && title.getText().equals("3D")) {
			date.setValue(day1);
		} else {
			date.setValue(LocalDate.now());
		}

		if (morE == ("3D")) {
			totalplaycounttwo.setText("1000");
		} else {
			totalplaycounttwo.setText("100");
		}

		customerService = new CustomerImplematation();
		twoDService = new TwoDImplementation();
		winNumberService = new WinNumberImplementation();
		exceedingService = new ExceedingImplementation();
		outSourceService = new OutSourceImplementation();
		backupService = new BackupImplementation();
		totalCustomerAmount.setText("Agent - " + customerService.getTotalCustomerNumber(in.isSelected()));
		addComboBoxtoCustomerInOut(in.isSelected());
		delete.setDisable(true);
		setAmount.setDisable(true);
		setAmountBtn.setText("Un Set");

		if (DataPass.getMeDataPass() != null) {
			title.setText(DataPass.getMeDataPass());
			if (title.getText().equals("3D")) {
				fp.getChildren().remove(1);
				hb.getChildren().remove(0);
				tfPlayNumber.setPrefWidth(100);
				tfAmount.setPrefWidth(150);
			}
		}

		loadDataWithDate();
		// Search Update
		searchCurrentKey.textProperty().addListener((a, b, c) -> {
			if (out.isSelected()) {
				List<OutSource> list = new ArrayList<OutSource>();
				List<OutSource> maxList = outSourceService.getOutSourceData(cbChooseCustomer.getValue().getText(),
						date.getValue(), title.getText());
				if (maxList != null) {
					for (OutSource exceeding : maxList) {
						String key = exceeding.getExceedingNumber();
						if (c.length() == 1) {
							key = key.substring(0, 1);
						} else if (c.length() == 2) {
							key = key.substring(0, 2);
						} else if (c.length() == 3) {
							key = key.substring(0, 3);
						}
						if (key.equalsIgnoreCase(c)) {
							list.add(exceeding);
						}
					}
					currentTable.getItems().clear();
					currentTable.getItems().addAll(list);

					int total = 0;
					for (OutSource exceeding : list) {
						total += exceeding.getCurrentAmt();
					}
					currentTotal.setText(df.format(total));
				}
				if (c.isEmpty()) {
					currentTable.getItems().clear();
					currentTable.getItems().addAll(maxList);

					int total = 0;
					for (OutSource exceeding : maxList) {
						total += exceeding.getCurrentAmt();
					}
					currentTotal.setText(df.format(total));
				}
			} else {
				System.out.println(toAddDataIntoCurTable + " toAddDataIntoCurTable");
				filteredData.setPredicate(obj -> {
					if (c == null || c.isEmpty()) {
						return true;
					}
					String lowerCaseFilter = c.toLowerCase();
					if (obj.getPlayNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true;
					} else {
						return false;
					}
				});
				SortedList<ObjectForCurrentTable> sortedData = new SortedList<>(filteredData);
				sortedData.comparatorProperty().bind(tVCurrentTable.comparatorProperty());
				tVCurrentTable.setItems(sortedData);

			}
		});

		searchMaxKey.textProperty().addListener((a, b, c) -> {
			List<Exceeding> list = new ArrayList<Exceeding>();
			List<Exceeding> maxList = exceedingService.getExceedingList(new Exceeding(date.getValue(), morE));
			if (maxList != null) {
				for (Exceeding exceeding : maxList) {
					String key = exceeding.getExceedingNumber();
					if (c.length() == 1) {
						key = key.substring(0, 1);
					} else if (c.length() == 2) {
						key = key.substring(0, 2);
					} else if (c.length() == 3) {
						key = key.substring(0, 3);
					}
					if (key.equalsIgnoreCase(c)) {
						list.add(exceeding);
					}
				}
				maxTable.getItems().clear();
				maxTable.getItems().addAll(list);

				int total = 0;
				for (Exceeding exceeding : list) {
					total += exceeding.getExceedingAmount();
				}
				maxExceedTotal.setText(df.format(total));
			}
			if (c.isEmpty()) {
				maxTable.getItems().clear();
				maxTable.getItems().addAll(maxList);

				int total = 0;
				for (Exceeding exceeding : maxList) {
					total += exceeding.getExceedingAmount();
				}
				maxExceedTotal.setText(df.format(total));
			}
		});

		sb.valueProperty().addListener(e -> {
			if (e instanceof DoubleProperty) {
				DoubleProperty d = (DoubleProperty) e;
				list1.scrollTo((int) d.get());
				list2.scrollTo((int) d.get());
				list3.scrollTo((int) d.get());
				list4.scrollTo((int) d.get());
				list5.scrollTo((int) d.get());
				list6.scrollTo((int) d.get());
				list7.scrollTo((int) d.get());
				list8.scrollTo((int) d.get());
				list9.scrollTo((int) d.get());
				list10.scrollTo((int) d.get());
			}
		});
		maxTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tVCurrentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		currentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		currentTable.setOnMouseClicked(e -> {
			OutSource outSource = currentTable.getSelectionModel().getSelectedItem();
			if (outSource != null) {
				tfkhway.setText("");
				delete.setDisable(false);
				tfPlayNumber.setDisable(true);
				tfPlayNumber.setText(outSource.getExceedingNumber());
				tfKeyWord.setDisable(true);
				tfAmount.setText(String.valueOf(outSource.getCurrentAmt()));
//				tfAmount.requestFocus();
				maxTable.getSelectionModel().clearSelection();
			}
		});

		setDatainMaxTable();
		selectMaxtable();
		setInMouseClickonListView(list1);
		setInMouseClickonListView(list2);
		setInMouseClickonListView(list3);
		setInMouseClickonListView(list4);
		setInMouseClickonListView(list5);
		setInMouseClickonListView(list6);
		setInMouseClickonListView(list7);
		setInMouseClickonListView(list8);
		setInMouseClickonListView(list9);
		setInMouseClickonListView(list10);
		deatilsBox.getChildren().remove(2);

		QueriesForTowDMorning twoDMorning = new QueriesForTowDMorning();
//		String strPlayNum = tfPlayNumber.getText().toLowerCase();
		String strPlayNum = tfPlayNumber.getText().toUpperCase();

		sp.getChildren().remove(0);
		in.selectedProperty().addListener((a, b, c) -> {
			System.out.println("in");
			addComboBoxtoCustomerInOut(c);
			if (c) {
				exceeding = null;
				maxTable.getSelectionModel().clearSelection();
				tfPlayNumber.setEditable(true);
				tfAmount.setEditable(true);
				currentTable.getItems().clear();
				totalAmountCurTable.setText("0");
				tfkhway.setDisable(false);
				sp.getChildren().remove(0);
				sp.getChildren().add(0, mmmCurrentTableBox);
				if (vbHb.getChildren().size() == 4) {
					vbHb.getChildren().add(1, previewVb);
				}
			} else {
				previewTable.setItems(null);
				sp.getChildren().remove(0);
				sp.getChildren().add(0, thihaCurrentTableBox);
				totalAmountCurTable.setText("0");
				if (vbHb.getChildren().size() == 5) {
					vbHb.getChildren().remove(previewVb);
				}
				tfkhway.setDisable(true);
				setDatainMaxTable();
				twoDMorning.deleteToTwoDUserPlayForCurTableForLast();
				tVCurrentTable.setItems(null);
			}
		});

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (LocalDate.now().isBefore(item) || twoDService.getFirstLineDate().isAfter(item)) {
							setDisable(true);
						}

					}
				};
			}
		};

		date.setDayCellFactory(dayCellFactory);
		// loadDataWithDate();
		totalvalueset();
		totalplaycountset();
//		totalAll.setText("Total   :  " + df.format(new QueriesForTowDMorning().getAllTotalData(morE, date.getValue()))
//				+ "  MMK");

		tCPlayNumber.setCellValueFactory(new PropertyValueFactory<ObjectForCurrentTable, String>("playNumber"));
		tCAmount.setCellValueFactory(new PropertyValueFactory<ObjectForCurrentTable, Integer>("amount"));

		tCPlayNumberForPreview
				.setCellValueFactory(new PropertyValueFactory<ObjectForPreviewTable, String>("playNumber"));
		tCbalanceColumn.setCellValueFactory(new PropertyValueFactory<ObjectForPreviewTable, Integer>("balance"));

		if (strPlayNum != null) {
			tfPlayNumber.textProperty().addListener((ob, ov, nv) -> {
				// 2D Condition
				if (!title.getText().equalsIgnoreCase("3D")) {
					if (nv.equalsIgnoreCase("+") && recentOne.equals("0")) {
						ErrorAlert("Invalid Play Number2d");
						tfPlayNumber.requestFocus();
						tfPlayNumber.selectAll();
					}
					if (nv.length() > 0) {
						tfkhway.setDisable(true);
						tfKeyWord.setDisable(false);
						tfAmount.setDisable(false);
					} else {
						tfkhway.setDisable(false);
						tfKeyWord.setDisable(true);
						tfAmount.setDisable(true);
					}
					if (nv.length() == 2) {
						String firstNum = String.valueOf(nv.charAt(0));
						String secondNum = String.valueOf(nv.charAt(1));
						if (out.isSelected()) {
							tfKeyWord.setDisable(true);
							amountfocus();
						}
						if (nv.equals("00") || nv.equals("11") || nv.equals("22") || nv.equals("33") || nv.equals("44")
								|| nv.equals("55") || nv.equals("66") || nv.equals("77") || nv.equals("88")
								|| nv.equals("99")) {
							tfKeyWord.setDisable(true);
							amountfocus();
						}

						if (nv.equalsIgnoreCase("ss")) {
							listSS.clear();
							ArrayList<String> temp = generateForSS();

							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;

								listSS.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listSS);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase("mm")) {
							listMM.clear();
							ArrayList<String> temp = generateForMM();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;

								listMM.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listMM);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase("sm")) {
							listSM.clear();
							ArrayList<String> temp = generateForSM();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;

								listSM.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listSM);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase("ms")) {
							listMS.clear();
							ArrayList<String> temp = generateForMS();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;

								listMS.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listMS);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase("sp")) {
							listSP.clear();
							ArrayList<String> temp = generateForSP();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listSP.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listSP);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase("mp")) {
							listMP.clear();
							ArrayList<String> temp = generateForMP();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listMP.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listMP);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase("**")) {
							listAA.clear();
							ArrayList<String> temp = generateForAA();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listAA.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listAA);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase(String.format("*%s", secondNum))) {
							listAN.clear();
							ArrayList<String> temp = generateForAN(secondNum);
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listAN.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listAN);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase(String.format("%s*", firstNum))) {
							listNA.clear();
							ArrayList<String> temp = generateForNA(firstNum);
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listNA.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listNA);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase(String.format("%sb", firstNum))) {
							listB.clear();
							ArrayList<String> temp = generateForB(firstNum);
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listB.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listB);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase(String.format("%s+", firstNum))) {
							listANNA.clear();
							ArrayList<String> temp = generateForANNA(firstNum);
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listANNA.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listANNA);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else {
							tfKeyWord.requestFocus();
						}
						if (!tfAmount.getPromptText().isEmpty() && (nv.equalsIgnoreCase("ss")
								|| nv.equalsIgnoreCase("mm") || nv.equalsIgnoreCase("sm") || nv.equalsIgnoreCase("ms")
								|| nv.equalsIgnoreCase("sp") || nv.equalsIgnoreCase("mp") || nv.equalsIgnoreCase("**")
								|| nv.startsWith("*") || nv.endsWith("*") || nv.endsWith("B"))) {
							amountfocus();
						}
					} else if (nv.length() == 1) {
						previewTable.setItems(null);
						if (nv.equalsIgnoreCase("N")) {
							listN.clear();
							ArrayList<String> temp = generateForN();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listN.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listN);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equalsIgnoreCase("P")) {
							listP.clear();
							ArrayList<String> temp = generateForP();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listP.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listP);
							tfKeyWord.setDisable(true);
							amountfocus();

						} else if (nv.equals("+") && !recentOne.equals("0")
								&& (recentOne.charAt(0) != recentOne.charAt(1))) {
							String newOv = String.valueOf(recentOne.charAt(1))
									.concat(String.valueOf(recentOne.charAt(0)));
							controllerForRecentOne = "1";
							tfPlayNumber.setText(newOv);
							tfKeyWord.setText(".");
							amountfocus();
						}
						if (!nv.equals("+")) {
							controllerForRecentOne = "0";
						}
						if (!tfAmount.getPromptText().isEmpty()
								&& (nv.equalsIgnoreCase("P") || nv.equalsIgnoreCase("N"))) {
							amountfocus();
						}
					}
//					@SuppressWarnings("rawtypes")
//					ObservableList[] listsToClear = { listSS, listMM, listSM, listMS, listSP, listMP, listAA, listAN,
//							listNA, listN, listP, listB };
//					for (int i = 0; i < listsToClear.length; i++) {
//						listsToClear[i].clear();
//					}
				} // 3D Condition
				else if (title.getText().equalsIgnoreCase("3D")) {
					if (nv.length() == 1) {
						if (nv.equals("+") && !list.isEmpty()) {
							previewTable.setItems(null);
							listForKwayInPreview.clear();
							for (int i = 0; i < list.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amountFromDb = twoDService.toShowAmount(Integer.parseInt(list.get(i)),
										title.getText(), date.getValue());
								int result = setAmountValue - amountFromDb;
								listForKwayInPreview.add(new ObjectForPreviewTable(list.get(i), result));
							}
							previewTable.setItems(listForKwayInPreview);

							tfAmount.setDisable(false);
							tfAmount.setEditable(true);
							amountfocus();
						} else if (nv.equalsIgnoreCase("+") && list.isEmpty()) {
							ErrorAlert("Invalid Play Number3d");
							tfPlayNumber.clear();
						}
					} else if (nv.length() == 2) {
						previewTable.setItems(null);
					}
					if (nv.length() == 3) {
						tfAmount.setDisable(false);
						tfAmount.setEditable(true);
						String firstNum = String.valueOf(nv.charAt(0));
						String secondNum = String.valueOf(nv.charAt(1));
						String thirdNum = String.valueOf(nv.charAt(2));

						@SuppressWarnings("rawtypes")
						ObservableList[] listsToClear = { listAAA, listANN, listNAN, listNNA };
						for (int i = 0; i < listsToClear.length; i++) {
							listsToClear[i].clear();
						}
						if (nv.equals("***")) {
							listAAA.clear();
							ArrayList<String> temp = generateForAAA();
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listAAA.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listAAA);
							amountfocus();
						} else if (nv.startsWith("*") && !secondNum.equals("*") && !thirdNum.equals("*")) {
							listANN.clear();
							ArrayList<String> temp = generateForANN(secondNum, thirdNum);
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listANN.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listANN);
							amountfocus();
						} else if (nv.charAt(1) == '*' && !nv.startsWith("*") && !nv.endsWith("*")) {
							listNAN.clear();
							ArrayList<String> temp = generateForNAN(firstNum, thirdNum);
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listNAN.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listNAN);
							amountfocus();
						} else if (nv.endsWith("*") && !secondNum.equals("*") && !firstNum.equals("*")) {
							listNNA.clear();
							ArrayList<String> temp = generateForNNA(firstNum, secondNum);
							for (int i = 0; i < temp.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE,
										date.getValue());
								int result = setAmountValue - amount;
								listNNA.add(new ObjectForPreviewTable(temp.get(i), result));
							}
							previewTable.setItems(listNNA);
							amountfocus();
						}
					} else if (nv.length() == 4) {
						char fourthNum = nv.charAt(3);
						if (fourthNum != '+' && fourthNum != '*') {
							tfPlayNumber.setText(nv.substring(0, nv.length() - 1));
							tfAmount.setDisable(false);
							tfAmount.setEditable(true);
							tfAmount.setText(String.valueOf(fourthNum));
							tfAmount.requestFocus();
							tfAmount.selectAll();
							tfAmount.deselect();
						} else if (fourthNum == '*') {
							if (!toAddDataIntoCurTable.isEmpty()) {
								String lastPlayNum = toAddDataIntoCurTable.get(toAddDataIntoCurTable.size() - 1)
										.getPlayNumber();
								if (lastPlayNum.length() == 4 && lastPlayNum.endsWith("R")) {
									enterNumber();
								} else if (in.isSelected() && tfPlayNumber.getText().length() == 4
										&& tfPlayNumber.getText().endsWith("*")) {
									System.out.println(recentAmountOne + " recentAmountOne");
									System.out.println(recentAmountTwo + " recentAmountTwo");
									tfAmount.setDisable(false);
									tfAmount.setText(tfAmount.getPromptText());
									amountfocus();
									inProcess(false, recentAmountOne, recentAmountTwo);
								}
							}
//							enterNumber();
//							tfAmount.clear();
//							tfPlayNumber.requestFocus();
						} else if (fourthNum == '+') {
							ArrayList<String> listforR = methodToDerive3DKway(nv.substring(0, nv.length() - 1));
							previewTable.setItems(null);
							listForKwayInPreview.clear();
							for (int i = 0; i < listforR.size(); i++) {
								int setAmountValue = Integer.parseInt(setAmount.getText());
								int amountFromDb = twoDService.toShowAmount(Integer.parseInt(listforR.get(i)),
										title.getText(), date.getValue());
								int result = setAmountValue - amountFromDb;
								listForKwayInPreview.add(new ObjectForPreviewTable(listforR.get(i), result));
							}
							previewTable.setItems(listForKwayInPreview);
							amountfocus();

						}
					}
				}
			});
		}

		String khwayStr = tfkhway.getText();
		if (khwayStr != null) {
			tfkhway.textProperty().addListener((ob, ov, nv) -> {
				if (nv.length() > 0 || !nv.isEmpty()) {
					tfPlayNumber.setDisable(true);
					tfKeyWord.setDisable(true);
				} else if (nv.length() == 0) {
					tfKeyWord.setDisable(true);
					tfAmount.setDisable(true);
				} else {
					tfPlayNumber.setDisable(false);
					tfKeyWord.setDisable(false);
				}
				if (nv.contains("*") && nv.length() > 2) {
					amountfocus();
					listKhway.clear();
					ArrayList<String> temp = generateForKhway(nv);
					for (int i = 0; i < temp.size(); i++) {
						int setAmountValue = Integer.parseInt(setAmount.getText());
						int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE, date.getValue());
						int result = setAmountValue - amount;
						listKhway.add(new ObjectForPreviewTable(temp.get(i), result));
					}
					previewTable.setItems(listKhway);
				} else if (!nv.endsWith("*") && nv.length() > 2) {
					tfAmount.setDisable(false);
					listKhway.clear();
					ArrayList<String> temp = generateForKhway(nv);
					for (int i = 0; i < temp.size(); i++) {
						int setAmountValue = Integer.parseInt(setAmount.getText());
						int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE, date.getValue());
						int result = setAmountValue - amount;
						listKhway.add(new ObjectForPreviewTable(temp.get(i), result));
					}
					previewTable.setItems(listKhway);
				} else if (nv.length() > 2) {
					tfAmount.setDisable(false);
					listKhway.clear();
					ArrayList<String> temp = generateForKhway(nv);
					for (int i = 0; i < temp.size(); i++) {
						int setAmountValue = Integer.parseInt(setAmount.getText());
						int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE, date.getValue());
						int result = setAmountValue - amount;
						listKhway.add(new ObjectForPreviewTable(temp.get(i), result));
					}
					previewTable.setItems(listKhway);
				} else if (nv.length() < 3) {
					tfAmount.setDisable(true);
					listKhway.clear();
				}
			});
		}

		String keyWordStr = tfKeyWord.getText();
		if (keyWordStr != null) {
			tfKeyWord.textProperty().addListener((ob, ov, nv) -> {
				if (nv.equals("+") && (!tfPlayNumber.getText().isEmpty())) {
					tfAmount.setDisable(false);
					amountfocus();
				}
//				if(nv.equalsIgnoreCase(anotherString))
				if (!toAddDataIntoCurTable.isEmpty() && nv.equalsIgnoreCase("*") && tfPlayNumber.getLength() == 2
						&& (tfPlayNumber.getText().charAt(0) != tfPlayNumber.getText().charAt(1)) && in.isSelected()) {
					tfAmount.setDisable(false);
					String lastPlayNum = toAddDataIntoCurTable.get(toAddDataIntoCurTable.size() - 1).getPlayNumber();
					if (lastPlayNum.length() > 2 && lastPlayNum.endsWith("R")) {
						System.out.println("TFKeyword IF Test .");
						tfAmount.setText(tfAmount.getPromptText());
						inProcess(false, "0", "0");
					} else if (!recentAmountOne.equals("0") && !recentAmountTwo.equals("0")) {
						tfAmount.setText(tfAmount.getPromptText());
						tfKeyWord.setText("*");
						inProcess(true, recentAmountOne, recentAmountTwo);
					}
				} else if (toAddDataIntoCurTable.isEmpty() && nv.equalsIgnoreCase("*")) {
					tfAmount.setDisable(true);
					tfKeyWord.requestFocus();
//					tfKeyWord.selectAll();
					ErrorAlert("Invalid Function");
					tfKeyWord.clear();
				}

				if (nv.equals("*") && !tfkhway.getText().isEmpty()) {
					tfAmount.setDisable(false);
					listKhway.clear();
					ArrayList<String> temp = generateForKhway(tfkhway.getText());
					for (int i = 0; i < temp.size(); i++) {
						int setAmountValue = Integer.parseInt(setAmount.getText());
						int amount = twoDService.toShowAmount(Integer.parseInt(temp.get(i)), morE, date.getValue());
						int result = setAmountValue - amount;
						listKhway.add(new ObjectForPreviewTable(temp.get(i), result));
					}
					if (!tfkhway.getText().endsWith("*")) {
						tfkhway.setText(tfkhway.getText().concat("*"));
					}
					previewTable.setItems(listKhway);
				} else if (!nv.equals("*") && !tfkhway.getText().isEmpty()) {
					tfAmount.setDisable(false);
					ObservableList<ObjectForPreviewTable> toRemovePuu = FXCollections.observableArrayList();
					for (int i = 0; i < tVCurrentTable.getItems().size(); i++) {
						String eachPlayNum = tVCurrentTable.getItems().get(i).getPlayNumber();
						if (eachPlayNum.charAt(0) == eachPlayNum.charAt(1)) {
							toRemovePuu.add(previewTable.getItems().get(i));
						}
					}
					if (tfkhway.getText().endsWith("*")) {
						tfkhway.setText(tfkhway.getText().substring(0, tfkhway.getText().length() - 1));
					}
					previewTable.getItems().removeAll(toRemovePuu);
				}
				if (!nv.equalsIgnoreCase("*") && !nv.equalsIgnoreCase("+") && !nv.isEmpty()) {
					tfAmount.setDisable(false);
					tfAmount.requestFocus();
					if (!ov.equals(nv)) {
						tfAmount.setText(ov);
					} else {
						tfAmount.setText(nv);
					}
					tfKeyWord.setText(".");
					tfAmount.selectAll();
					tfAmount.deselect();
				}
			});
		}
		ArrayList<String> removedItemsForR = new ArrayList<>();
		tVCurrentTable.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
			removedItemsForR.clear();
			if (nv != null) {
				tfAmount.setDisable(false);
				ObjectForCurrentTable curObject = tVCurrentTable.getSelectionModel().getSelectedItem();
				String playNumber = curObject.getPlayNumber();
				String amount = String.valueOf(curObject.getAmount());

				delete.setDisable(false);
				if (curObject.getPlayNumber().startsWith("R") && title.getText().equals("3D")) {
					char listSize = curObject.getPlayNumber().charAt(2);
					System.out.println(listSize);
					if (listSize == '5') {
						int firstNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 1;
						String firstNum = toAddDataIntoCurTable.get(firstNumIndex).getPlayNumber();

						System.out.println(firstNum + " R(5)");
						removedItemsForR.add(firstNum);
						System.out.println(removedItemsForR + " removedItemsForR inside 5");
						methodFor3DKway(firstNum, removedItemsForR);
					} else if (listSize == '4') {
						int firstNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 2;
						int secondNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 1;
						String firstNum = toAddDataIntoCurTable.get(firstNumIndex).getPlayNumber();

						System.out.println(firstNum + " R(4)");
						removedItemsForR.add(firstNum);
						removedItemsForR.add(toAddDataIntoCurTable.get(secondNumIndex).getPlayNumber());
						System.out.println(removedItemsForR + " removedItemsForR inside 4");
						methodFor3DKway(firstNum, removedItemsForR);
					} else if (listSize == '3') {
						int firstNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 3;
						int secondNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 2;
						int thirdNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 1;
						String firstNum = toAddDataIntoCurTable.get(firstNumIndex).getPlayNumber();

						System.out.println(firstNum + " R(3)");
						removedItemsForR.add(firstNum);
						removedItemsForR.add(toAddDataIntoCurTable.get(secondNumIndex).getPlayNumber());
						removedItemsForR.add(toAddDataIntoCurTable.get(thirdNumIndex).getPlayNumber());
						System.out.println(removedItemsForR + " removedItemsForR inside 3");
						methodFor3DKway(firstNum, removedItemsForR);
					} else if (listSize == '2') {
						int firstNumIndexToDivide2 = tVCurrentTable.getSelectionModel().getSelectedIndex() - 1;
						String firstNumToDivide2 = toAddDataIntoCurTable.get(firstNumIndexToDivide2).getPlayNumber();
						System.out.println(firstNumToDivide2 + " firstNumToDivide2");
						if (firstNumToDivide2.charAt(0) == firstNumToDivide2.charAt(1)
								|| firstNumToDivide2.charAt(0) == firstNumToDivide2.charAt(2)
								|| firstNumToDivide2.charAt(1) == firstNumToDivide2.charAt(2)) {
							int firstNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 1;
//							int secondNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 1;
							String firstNum = toAddDataIntoCurTable.get(firstNumIndex).getPlayNumber();

							System.out.println(firstNum + " R(2) for 225");
							removedItemsForR.add(firstNum);
//							removedItemsForR.add(toAddDataIntoCurTable.get(secondNumIndex).getPlayNumber());
							System.out.println(removedItemsForR + " removedItemsForR inside 225");
							methodFor3DKway(firstNum, removedItemsForR);
						} else {
							int firstNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 4;
							int secondNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 3;
							int thirdNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 2;
							int fourthNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex() - 1;
							String firstNum = toAddDataIntoCurTable.get(firstNumIndex).getPlayNumber();

							System.out.println(firstNum + " R(2)");
							removedItemsForR.add(firstNum);
							removedItemsForR.add(toAddDataIntoCurTable.get(secondNumIndex).getPlayNumber());
							removedItemsForR.add(toAddDataIntoCurTable.get(thirdNumIndex).getPlayNumber());
							removedItemsForR.add(toAddDataIntoCurTable.get(fourthNumIndex).getPlayNumber());
							System.out.println(removedItemsForR + " removedItemsForR inside 2");
							methodFor3DKway(firstNum, removedItemsForR);
						}
					}
				} else if (curObject.getPlayNumber().endsWith("R") && title.getText().equals("3D")) {
					String strPlayNumWithoutR = curObject.getPlayNumber().substring(0,
							curObject.getPlayNumber().length() - 1);
					removedItemsForR.clear();
					methodFor3DKway(strPlayNumWithoutR, removedItemsForR);
				} else if (curObject.getPlayNumber().endsWith("R") && !title.getText().equals("3D")) {
					int firstNumIndex = tVCurrentTable.getSelectionModel().getSelectedIndex();
					String firstNum = toAddDataIntoCurTable.get(firstNumIndex).getPlayNumber();
					String strPlayNumWithoutR = firstNum.substring(0, firstNum.length() - 1);
					String firstNumReverse = String.valueOf(strPlayNumWithoutR.charAt(1))
							.concat(String.valueOf(strPlayNumWithoutR.charAt(0)));

					int setAmountValue = Integer.parseInt(setAmount.getText());
					int amountFromFirstNum = twoDService.toShowAmount(Integer.parseInt(strPlayNumWithoutR.trim()),
							title.getText(), date.getValue());
					int amountFromFirstNumRe = twoDService.toShowAmount(Integer.parseInt(firstNumReverse),
							title.getText(), date.getValue());
					int result = setAmountValue - amountFromFirstNum;
					int resultRe = setAmountValue - amountFromFirstNumRe;
//					firstNum
					listForKwayInPreview.clear();
					listForKwayInPreview.add(new ObjectForPreviewTable(strPlayNumWithoutR, result));
					listForKwayInPreview.add(new ObjectForPreviewTable(firstNumReverse, resultRe));
					previewTable.setItems(listForKwayInPreview);
				} else {
					previewTable.setItems(null);
				}
				if (playNumber.length() > 2 && !title.getText().equals("3D") && !playNumber.endsWith("R")) {
					tfkhway.clear();

					tfkhway.setEditable(false);
					tfkhway.setOpacity(0.6);

					tfKeyWord.setEditable(false);
					tfKeyWord.setOpacity(0.6);

					tfKeyWord.clear();
					tfPlayNumber.setPromptText("");
					tfkhway.setPromptText(playNumber);
					if (playNumber.endsWith("*")) {
						tfKeyWord.setPromptText(String.valueOf(playNumber.charAt(playNumber.length() - 1)));
					} else {
						tfKeyWord.setPromptText("");
					}
					tfAmount.setText(amount);
					toExtractForUpdate = curObject.getAmount();
					ArrayList<String> kwayList = generateForKhway(playNumber);
					listForKwayInPreview.clear();
					for (int i = 0; i < kwayList.size(); i++) {
						int setAmountValue = Integer.parseInt(setAmount.getText());
						int amountFromDb = twoDService.toShowAmount(Integer.parseInt(kwayList.get(i)), title.getText(),
								date.getValue());
						int result = setAmountValue - amountFromDb;

						listForKwayInPreview.add(new ObjectForPreviewTable(kwayList.get(i), result));
					}
					previewTable.setItems(listForKwayInPreview);

				} else {
					tfPlayNumber.setEditable(false);
					tfAmount.setEditable(true);
					tfPlayNumber.setOpacity(0.6);

					tfkhway.setPromptText("");
					tfPlayNumber.setPromptText(playNumber);
					tfAmount.setText(amount);
					toExtractForUpdate = curObject.getAmount();
				}
			}
		});

//		tfAmount.textProperty().addListener((ob, ov, nv) -> {
//			tfAmount.setPromptText(ov);
//		});
		all.selectedProperty().addListener((ob, ov, nv) -> {
			if (nv != null && cbChooseCustomer.getValue() != null) {
				if (nv.booleanValue()) {
					previewTable.setItems(null);
					String choseName = cbChooseCustomer.getSelectionModel().getSelectedItem().getText();
					LocalDate dateFromPicker = date.getValue();
					int totalAmount = 0;
					if (choseName != null) {
						if (dateFromPicker != null) {
							ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
									.extractDataForAllOption(choseName, dateFromPicker, title.getText());
							tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));

							for (int i = 0; i < dataReturnFromDB.size(); i++) {
								String middleNum = "";
								String playNumber = dataReturnFromDB.get(i).getPlayNumber();
								if (title.getText().equals("3D") && playNumber.length() == 3) {
									middleNum = playNumber.charAt(1) + "";
								}

								if (playNumber.equalsIgnoreCase("ss") || playNumber.equalsIgnoreCase("mm")
										|| playNumber.equalsIgnoreCase("sm") || playNumber.equalsIgnoreCase("ms")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 25) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("sp") || playNumber.equalsIgnoreCase("mp")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 5) + totalAmount;
								} else if (playNumber.length() > 2 && !playNumber.endsWith("*")
										&& !title.getText().equals("3D") && !playNumber.endsWith("R")) {
									ArrayList<String> list = generateForKhway(playNumber);
									totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
								} else if (playNumber.length() > 3 && playNumber.endsWith("*")) {
									ArrayList<String> list = generateForKhway(playNumber);
									totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("sp") || playNumber.startsWith("*")
										|| playNumber.endsWith("*") || playNumber.equalsIgnoreCase("p")
										|| playNumber.equalsIgnoreCase("n")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
								} else if (playNumber.endsWith("B")) {
									String firstNum = playNumber.charAt(0) + "";
									ArrayList<String> list = new ArrayList<>();

									int c = 0;
									int a = 0;
									int j = Integer.parseInt(firstNum);
									int k = Integer.parseInt("1" + firstNum);
									while (c <= Integer.parseInt(firstNum)) {
										list.add(String.valueOf(c).concat(String.valueOf(j)));
										c++;
										j--;
									}
									while (a <= Integer.parseInt("1" + firstNum)) {
										if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
											list.add(String.valueOf(a).concat(String.valueOf(k)));
										}
										a++;
										k--;
									}
									totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
								} else if (playNumber.endsWith("R") && !title.getText().equals("3D")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 2) + totalAmount;
								} else if (playNumber.endsWith("R") && title.getText().equals("3D")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 6) + totalAmount;
								} else if (playNumber.endsWith("+")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 19) + totalAmount;
								} else if (middleNum.equals("*")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
								} else {
									totalAmount = dataReturnFromDB.get(i).getAmount() + totalAmount;
								}
							}
							totalAmountCurTable.setText(df.format(totalAmount));
						} else {
							ErrorAlert("Please select date");
						}
					} else {
						last.setSelected(true);
						ErrorAlert("Please choose a customer");
					}
				} else {
					String choseName = cbChooseCustomer.getSelectionModel().getSelectedItem().getText();
					LocalDate dateFromPicker = date.getValue();
					int totalAmount = 0;

					if (choseName != null) {
						if (dateFromPicker != null) {
							ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
									.extractDataForLastOption(choseName, dateFromPicker, title.getText());
							tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));

							for (int i = 0; i < dataReturnFromDB.size(); i++) {
								String middleNum = "";
								String playNumber = dataReturnFromDB.get(i).getPlayNumber();
								if (title.getText().equals("3D") && playNumber.length() == 3) {
									middleNum = playNumber.charAt(1) + "";
								}

								if (playNumber.equalsIgnoreCase("ss") || playNumber.equalsIgnoreCase("mm")
										|| playNumber.equalsIgnoreCase("sm") || playNumber.equalsIgnoreCase("ms")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 25) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("sp") || playNumber.equalsIgnoreCase("mp")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 5) + totalAmount;
								} else if (playNumber.length() > 2 && !playNumber.endsWith("*")
										&& !title.getText().equals("3D") && !playNumber.endsWith("R")) {
									ArrayList<String> list = generateForKhway(playNumber);
									totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
								} else if (playNumber.length() > 3 && playNumber.endsWith("*")) {
									ArrayList<String> list = generateForKhway(playNumber);
									totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("sp") || playNumber.startsWith("*")
										|| playNumber.endsWith("*") || playNumber.equalsIgnoreCase("p")
										|| playNumber.equalsIgnoreCase("n")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("R(5)")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 5) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("R(4)")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 4) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("R(3)")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 3) + totalAmount;
								} else if (playNumber.equalsIgnoreCase("R(2)")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 2) + totalAmount;
								} else if (playNumber.endsWith("B")) {
									String firstNum = playNumber.charAt(0) + "";
									ArrayList<String> list = new ArrayList<>();

									int c = 0;
									int a = 0;
									int j = Integer.parseInt(firstNum);
									int k = Integer.parseInt("1" + firstNum);
									while (c <= Integer.parseInt(firstNum)) {
										list.add(String.valueOf(c).concat(String.valueOf(j)));
										c++;
										j--;
									}
									while (a <= Integer.parseInt("1" + firstNum)) {
										if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
											list.add(String.valueOf(a).concat(String.valueOf(k)));
										}
										a++;
										k--;
									}
									totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
								} else if (playNumber.endsWith("R") && !title.getText().equals("3D")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 2) + totalAmount;
								} else if (playNumber.endsWith("R") && title.getText().equals("3D")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 6) + totalAmount;
								} else if (playNumber.endsWith("+")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 19) + totalAmount;
								} else if (middleNum.equals("*")) {
									totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
								} else {
									totalAmount = dataReturnFromDB.get(i).getAmount() + totalAmount;
								}
							}
							totalAmountCurTable.setText(df.format(totalAmount));
						} else {
							ErrorAlert("Please select date");
						}
					} else {
						last.setSelected(true);
						ErrorAlert("Please choose a customer");
					}
				}
			}
			tfPlayNumber.setOpacity(1);
			tfkhway.setOpacity(1);
			tfKeyWord.setOpacity(1);

			tfPlayNumber.setPromptText("");
			tfkhway.setPromptText("");
			tfKeyWord.setPromptText("");

			tfPlayNumber.setEditable(true);
			tfkhway.setEditable(true);
			tfKeyWord.setEditable(true);

			tfPlayNumber.clear();
			tfkhway.clear();
			tfKeyWord.clear();
			tfAmount.clear();

		});

		cbChooseCustomer.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
			if (nv != null) {
				if (!nv.equals(ov)) {

					twoDMorning.deleteToTwoDUserPlayForCurTableForLast();
					LocalDate dateFromPicker = date.getValue();
					if (all.isSelected()) {
						previewTable.setItems(null);
						String choseName = cbChooseCustomer.getSelectionModel().getSelectedItem().getText();
						if (dateFromPicker != null) {
							toAddDataIntoCurTable.clear();
							ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
									.extractDataForAllOption(choseName, dateFromPicker, title.getText());
							toAddDataIntoCurTable.addAll(dataReturnFromDB);
							tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));

						} else {
							ErrorAlert("Please select date");
						}
					}
					toCalculateTotalAmountForCurrentTable();
				}
			}
		});
	}

	public ObservableList<ObjectForCurrentTable> toStoreDataForLastOption(ObjectForCurrentTable obj) {
		ObservableList<ObjectForCurrentTable> toAddDataIntoCurTableForLast = FXCollections.observableArrayList();
		toAddDataIntoCurTableForLast.add(obj);
		return toAddDataIntoCurTableForLast;
	}

	public ArrayList<String> generateForSS() {
		for (int i = 0; i < 100; i += 20) {
			for (int j = 0; j < 9; j += 2) {
				int playNumber = j + i;
				String formatedPlayNum = String.format("%02d", playNumber);
				arrayListSS.add(formatedPlayNum);
			}
		}
		Set<String> set = new LinkedHashSet<>(arrayListSS);
		arrayListSS.clear();
		arrayListSS.addAll(set);
		return arrayListSS;
	}

	public ArrayList<String> generateForMM() {
		for (int i = 1; i < 10; i += 2) {
			for (int j = 1; j < 10; j += 2) {
				String formatedPlayNum = String.format("%d%d", i, j);
				arrayListMM.add(formatedPlayNum);
			}
		}
		Set<String> set = new LinkedHashSet<>(arrayListMM);
		arrayListMM.clear();
		arrayListMM.addAll(set);
		return arrayListMM;
	}

	public ArrayList<String> generateForSM() {
		for (int i = 0; i < 100; i += 20) {
			for (int j = 1; j < 10; j += 2) {
				int playNumber = j + i;
				String formatedPlayNum = String.format("%02d", playNumber);
				arrayListSM.add(formatedPlayNum);
			}
		}
		Set<String> set = new LinkedHashSet<>(arrayListSM);
		arrayListSM.clear();
		arrayListSM.addAll(set);
		return arrayListSM;
	}

	public ArrayList<String> generateForMS() {
		for (int i = 1; i < 10; i += 2) {
			for (int j = 0; j < 9; j += 2) {
				String formatedPlayNum = String.format("%d%d", i, j);
				arrayListMS.add(formatedPlayNum);
			}
		}
		Set<String> set = new LinkedHashSet<>(arrayListMS);
		arrayListMS.clear();
		arrayListMS.addAll(set);
		return arrayListMS;
	}

	public ArrayList<String> generateForSP() {
		for (int i = 0; i < 9; i += 2) {
			String formatedPlayNum = String.format("%d%d", i, i);
			arrayListSP.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListSP);
		arrayListSP.clear();
		arrayListSP.addAll(set);
		return arrayListSP;
	}

	public ArrayList<String> generateForMP() {
		for (int i = 1; i < 10; i += 2) {
			String formatedPlayNum = String.format("%d%d", i, i);
			arrayListMP.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListMP);
		arrayListMP.clear();
		arrayListMP.addAll(set);
		return arrayListMP;
	}

	public ArrayList<String> generateForAA() {
		arrayListAA.clear();
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%d%d", i, i);
			arrayListAA.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListAA);
		arrayListAA.clear();
		arrayListAA.addAll(set);
		return arrayListAA;
	}

	public ArrayList<String> generateForAN(String secondNum) {
		arrayListAN.clear();
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%d%s", i, secondNum);
			arrayListAN.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListAN);
		arrayListAN.clear();
		arrayListAN.addAll(set);
		return arrayListAN;
	}

	public ArrayList<String> generateForNA(String firstNum) {
		arrayListNA.clear();
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%s%d", firstNum, i);
			arrayListNA.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListNA);
		arrayListNA.clear();
		arrayListNA.addAll(set);
		return arrayListNA;
	}

	public ArrayList<String> generateForN() {
		String[] array = { "07", "18", "24", "35", "69", "96", "53", "42", "81", "70" };
		for (int i = 0; i < array.length; i++) {
			arrayListN.add(array[i]);
		}
		Set<String> set = new LinkedHashSet<>(arrayListN);
		arrayListN.clear();
		arrayListN.addAll(set);
		return arrayListN;
	}

	public ArrayList<String> generateForP() {
		String[] array = { "05", "16", "27", "38", "49", "50", "61", "72", "83", "94" };
		for (int i = 0; i < array.length; i++) {
			arrayListP.add(array[i]);
		}
		Set<String> set = new LinkedHashSet<>(arrayListP);
		arrayListP.clear();
		arrayListP.addAll(set);
		return arrayListP;
	}

	public ArrayList<String> generateForB(String forSum) {
		ArrayList<String> list = new ArrayList<>();
		int i = 0;
		int a = 0;
		int j = Integer.parseInt(forSum);
		int k = Integer.parseInt("1" + forSum);
		while (i <= Integer.parseInt(forSum)) {
			list.add(String.valueOf(i).concat(String.valueOf(j)));
			i++;
			j--;
		}
		while (a <= Integer.parseInt("1" + forSum)) {
			if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
				list.add(String.valueOf(a).concat(String.valueOf(k)));
			}
			a++;
			k--;
		}
		arrayListB.clear();
		arrayListB.addAll(list);
		System.out.println("B List " + arrayListB);
		return arrayListB;
	}

	public ArrayList<String> generateForANNA(String firstNum) {
		arrayListANNA.clear();
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%d%s", i, firstNum);
			arrayListANNA.add(formatedPlayNum);
		}
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%s%d", firstNum, i);
			arrayListANNA.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListANNA);
		arrayListANNA.clear();
		arrayListANNA.addAll(set);

		return arrayListANNA;
	}

	public ArrayList<String> generateForAAA() {
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%d%d%d", i, i, i);
			arrayListAAA.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListAAA);
		arrayListAAA.clear();
		arrayListAAA.addAll(set);
		return arrayListAAA;
	}

	public ArrayList<String> generateForANN(String secondNum, String thirdNum) {
		arrayListANN.clear();
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%d%s%s", i, secondNum, thirdNum);
			arrayListANN.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListANN);
		arrayListANN.clear();
		arrayListANN.addAll(set);
		return arrayListANN;
	}

	public ArrayList<String> generateForNAN(String firstNum, String thirdNum) {
		arrayListNAN.clear();
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%s%d%s", firstNum, i, thirdNum);
			arrayListNAN.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListNAN);
		arrayListNAN.clear();
		arrayListNAN.addAll(set);
		return arrayListNAN;
	}

	public ArrayList<String> generateForNNA(String firstNum, String secondNum) {
		arrayListNNA.clear();
		for (int i = 0; i < 10; i++) {
			String formatedPlayNum = String.format("%s%s%d", firstNum, secondNum, i);
			arrayListNNA.add(formatedPlayNum);
		}
		Set<String> set = new LinkedHashSet<>(arrayListNNA);
		arrayListNNA.clear();
		arrayListNNA.addAll(set);
		return arrayListNNA;
	}

	public ArrayList<String> generateForKhway(String khwayNum) {
		if (!khwayNum.endsWith("*")) {
			String[] khwayNumArr = khwayNum.split("");
			arrayListKhway.clear();
			for (int i = 0; i < khwayNumArr.length; i++) {
				for (int j = 0; j < khwayNumArr.length; j++) {
					if (i == j) {
						continue;
					}
					arrayListKhway.add(khwayNumArr[i].concat(khwayNumArr[j]));
				}
			}
			Set<String> set = new LinkedHashSet<>(arrayListKhway);
			arrayListKhway.clear();
			arrayListKhway.addAll(set);
			return arrayListKhway;
		} else {
			String[] khwayNumArr = khwayNum.split("");
			arrayListKhway.clear();
			for (int i = 0; i < khwayNumArr.length - 1; i++) {
				for (int j = 0; j < khwayNumArr.length - 1; j++) {
					arrayListKhway.add(khwayNumArr[i].concat(khwayNumArr[j]));
				}
			}
			Set<String> set = new LinkedHashSet<>(arrayListKhway);
			arrayListKhway.clear();
			arrayListKhway.addAll(set);
			return arrayListKhway;
		}
	}

	public void loadDataIntoListsAtFirst(int startNum, int endNum, ListView<String> list) {

		QueriesForTowDMorning twoDMorning = new QueriesForTowDMorning();
		LocalDate dateFromPicker = date.getValue();
		String format = "%02d%20s";

		for (int i = startNum; i < endNum; i++) {
			int num = twoDMorning.toShowAmount(i, dateFromPicker);
			String text = df.format(num);
			list.getItems().add(String.format(format, i, text));
			list.setOnMouseClicked(e -> {
				if (e.getClickCount() == 2) {
					String result = list.getSelectionModel().getSelectedItem();
					result = result.replaceAll("\\s+", "-");
					String array[] = result.split("-");
					String show = "No: " + array[0] + "\n" + "Amount: " + array[1];
					informAlert(show);
				}
			});
		}
	}

	public void allClear() {
		list1.getSelectionModel().clearSelection();
		list2.getSelectionModel().clearSelection();
		list3.getSelectionModel().clearSelection();
		list4.getSelectionModel().clearSelection();
		list5.getSelectionModel().clearSelection();
		list6.getSelectionModel().clearSelection();
		list7.getSelectionModel().clearSelection();
		list8.getSelectionModel().clearSelection();
		list9.getSelectionModel().clearSelection();
		list10.getSelectionModel().clearSelection();
	}

	public void deleteOnAction(ActionEvent event) {
		if (in.isSelected()) {
			isDelete = true;
			QueriesForTowDMorning twoDMorning = new QueriesForTowDMorning();

			LocalDate dateFromPicker = date.getValue();
			String choseName = cbChooseCustomer.getSelectionModel().getSelectedItem().getText();
			int amount = tVCurrentTable.getSelectionModel().getSelectedItem().getAmount();

			String playNumPromp = tfPlayNumber.getPromptText();
			String khwayNumPromp = tfkhway.getPromptText();

			if (playNumPromp.contains("R")) {
				for (int i = 0; i < previewTable.getItems().size(); i++) {
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, previewTable.getItems().get(i).getPlayNumber(),
							amount, title.getText(), dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName,
							previewTable.getItems().get(i).getPlayNumber(), amount, title.getText(), dateFromPicker,
							"last");
					insertIntoList(true, Integer.parseInt(previewTable.getItems().get(i).getPlayNumber()));
				}
				twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLastForR(choseName, playNumPromp, amount,
						title.getText(), dateFromPicker, "last", tVCurrentTable.getSelectionModel().getSelectedIndex());
				toAddDataIntoCurTable.clear();
				ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
						dateFromPicker, title.getText());
				toAddDataIntoCurTable.addAll(dataReturnFromDB);
				tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				previewTable.setItems(null);
			} else if (playNumPromp.length() == 2) {
				String firstNum = String.valueOf(playNumPromp.charAt(0));
				String secondNum = String.valueOf(playNumPromp.charAt(1));

				if (all.isSelected()) {
					if (playNumPromp.equalsIgnoreCase("ss")) {
						ArrayList<String> temp = generateForSS();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable);

					} else if (playNumPromp.equalsIgnoreCase("mm")) {
						ArrayList<String> temp = generateForMM();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("sm")) {
						ArrayList<String> temp = generateForSM();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("ms")) {
						ArrayList<String> temp = generateForMS();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("sp")) {
						ArrayList<String> temp = generateForSP();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("mp")) {
						ArrayList<String> temp = generateForMP();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("**")) {
						ArrayList<String> temp = generateForAA();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("*%s", secondNum))) {
						ArrayList<String> temp = generateForAN(secondNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("%s*", firstNum))) {
						ArrayList<String> temp = generateForNA(firstNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("%sb", firstNum))) {
						ArrayList<String> temp = generateForNA(firstNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("%s+", firstNum))) {
						ArrayList<String> temp = generateForANNA(firstNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else {
						int playNumberInner = Integer.parseInt(playNumPromp);
//							HERE UPDATE
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						insertIntoList(true, playNumberInner);

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForAllOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					}
				} else {
					if (playNumPromp.equalsIgnoreCase("ss")) {
						ArrayList<String> temp = generateForSS();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("mm")) {
						ArrayList<String> temp = generateForMM();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("sm")) {
						ArrayList<String> temp = generateForSM();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("ms")) {
						ArrayList<String> temp = generateForMS();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("sp")) {
						ArrayList<String> temp = generateForSP();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("mp")) {
						ArrayList<String> temp = generateForMP();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase("**")) {
						ArrayList<String> temp = generateForAA();
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("*%s", secondNum))) {
						ArrayList<String> temp = generateForAN(secondNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("%s*", firstNum))) {
						ArrayList<String> temp = generateForNA(firstNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("%sb", firstNum))) {
						ArrayList<String> temp = generateForNA(firstNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else if (playNumPromp.equalsIgnoreCase(String.format("%s+", firstNum))) {
						ArrayList<String> temp = generateForANNA(firstNum);
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						for (int i = 0; i < temp.size(); i++) {

							int playNumFromStr = Integer.parseInt(temp.get(i));
							insertIntoList(true, playNumFromStr);
						}

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

					} else {
						int playNumberInner = Integer.parseInt(playNumPromp);
//							HERE UPDATE
						twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
								dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
								title.getText(), dateFromPicker, "last");
						insertIntoList(true, playNumberInner);

						toAddDataIntoCurTable.clear();
						ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
								.extractDataForLastOption(choseName, dateFromPicker, title.getText());
						toAddDataIntoCurTable.addAll(dataReturnFromDB);
						tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
					}
				}

			} else if (playNumPromp.length() == 1 && all.isSelected()) {

				if (tfPlayNumber.getPromptText().equalsIgnoreCase("n")) {
					ArrayList<String> temp = generateForN();
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}

					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("p")) {
					ArrayList<String> temp = generateForP();
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}

					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				}
			} else if (playNumPromp.length() == 1 && !all.isSelected()) {

				if (tfPlayNumber.getPromptText().equalsIgnoreCase("n")) {
					ArrayList<String> temp = generateForN();
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}

					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				} else if (tfPlayNumber.getPromptText().equalsIgnoreCase("p")) {
					ArrayList<String> temp = generateForP();
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}

					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

				}
			} else if (playNumPromp.length() == 3 && title.getText().equals("3D") && all.isSelected()) {
				String firstNum = String.valueOf(playNumPromp.charAt(0));
				String secondNum = String.valueOf(playNumPromp.charAt(1));
				String thirdNum = String.valueOf(playNumPromp.charAt(2));

				if (playNumPromp.equals("***")) {
					ArrayList<String> temp = generateForAAA();
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (playNumPromp.startsWith("*")) {
					ArrayList<String> temp = generateForANN(secondNum, thirdNum);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (secondNum.equals("*")) {
					ArrayList<String> temp = generateForNAN(firstNum, thirdNum);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (playNumPromp.endsWith("*")) {
					ArrayList<String> temp = generateForNNA(firstNum, secondNum);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else {
					int playNumberInt = Integer.parseInt(playNumPromp);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					insertIntoList(true, playNumberInt);
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForAllOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				}
			} else if (playNumPromp.length() == 3 && title.getText().equals("3D") && last.isSelected()) {
				String firstNum = String.valueOf(playNumPromp.charAt(0));
				String secondNum = String.valueOf(playNumPromp.charAt(1));
				String thirdNum = String.valueOf(playNumPromp.charAt(2));

				if (playNumPromp.equals("***")) {
					ArrayList<String> temp = generateForAAA();
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (playNumPromp.startsWith("*")) {
					ArrayList<String> temp = generateForANN(secondNum, thirdNum);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (secondNum.equals("*")) {
					ArrayList<String> temp = generateForNAN(firstNum, thirdNum);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else if (playNumPromp.endsWith("*")) {
					ArrayList<String> temp = generateForNNA(firstNum, secondNum);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					for (int i = 0; i < temp.size(); i++) {

						int playNumFromStr = Integer.parseInt(temp.get(i));
						insertIntoList(true, playNumFromStr);
					}
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				} else {
					int playNumberInt = Integer.parseInt(playNumPromp);
					twoDMorning.deleteDataIntoTwoDUserPlay(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, playNumPromp, amount, title.getText(),
							dateFromPicker, "last");
					twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, playNumPromp, amount,
							title.getText(), dateFromPicker, "last");
					insertIntoList(true, playNumberInt);
					toAddDataIntoCurTable.clear();
					ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning
							.extractDataForLastOption(choseName, dateFromPicker, title.getText());
					toAddDataIntoCurTable.addAll(dataReturnFromDB);
					tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
				}
			}
			if (khwayNumPromp.length() > 2 && all.isSelected()) {
				ArrayList<String> temp = generateForKhway(khwayNumPromp);
				twoDMorning.deleteDataIntoTwoDUserPlay(choseName, khwayNumPromp.concat("k"), amount, title.getText(),
						dateFromPicker, "last");
				twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, khwayNumPromp.concat("k"), amount,
						title.getText(), dateFromPicker, "last");
				twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, khwayNumPromp.concat("k"), amount,
						title.getText(), dateFromPicker, "last");
				for (int i = 0; i < temp.size(); i++) {

					int playNumFromStr = Integer.parseInt(temp.get(i));
					insertIntoList(true, playNumFromStr);
				}

				toAddDataIntoCurTable.clear();
				ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForAllOption(choseName,
						dateFromPicker, title.getText());
				toAddDataIntoCurTable.addAll(dataReturnFromDB);
				tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));

			} else if (khwayNumPromp.length() > 2 && !all.isSelected()) {
				ArrayList<String> temp = generateForKhway(khwayNumPromp);
				twoDMorning.deleteDataIntoTwoDUserPlay(choseName, khwayNumPromp.concat("k"), amount, title.getText(),
						dateFromPicker, "last");
				twoDMorning.deleteDataIntoTwoDUserPlayForCurTable(choseName, khwayNumPromp.concat("k"), amount,
						title.getText(), dateFromPicker, "last");
				twoDMorning.deleteDataIntoTwoDUserPlayForCurTableForLast(choseName, khwayNumPromp.concat("k"), amount,
						title.getText(), dateFromPicker, "last");
				for (int i = 0; i < temp.size(); i++) {

					int playNumFromStr = Integer.parseInt(temp.get(i));
					insertIntoList(true, playNumFromStr);
				}

				toAddDataIntoCurTable.clear();
				ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
						dateFromPicker, title.getText());
				toAddDataIntoCurTable.addAll(dataReturnFromDB);
				tVCurrentTable.setItems(toAddDataIntoCurTable.filtered(obj -> obj.getAmount() != 0));
			}
			toCalculateTotalAmountForCurrentTable();
			delete.setDisable(true);
			totalvalueset();
			totalplaycountset();
//			totalAll.setText("Total   :  "
//					+ df.format(new QueriesForTowDMorning().getAllTotalData(morE, date.getValue())) + "  MMK");
		} else {
			OutSource outSource = currentTable.getSelectionModel().getSelectedItem();
			if (outSource != null) {
				int playNumber = Integer.parseInt(outSource.getExceedingNumber());
				int playAmt = outSource.getCurrentAmt();

				String customerID = customerService.getCustomerId(cbChooseCustomer.getValue().getText());
				int exeID = outSourceService.isValidExceedID(customerID, playNumber, morE, date.getValue());
				int oID = outSourceService.getOutsourceID(customerID, exeID);
				outSourceService.deleteOutSource(oID);
				int getExectAmt = twoDService.getExtractAmount(playNumber, morE, date.getValue());
				int result = getExectAmt - playAmt;
				twoDService.updateExtract_amountZero(new TwoDEntity(morE, date.getValue(), playNumber, result));
				setDataToCurrentTable();
				insertIntoList(true, playNumber);
			}
		}
		tfPlayNumber.setOpacity(1);
		tfPlayNumber.setPromptText("");
		tfPlayNumber.setEditable(true);

		tfkhway.setOpacity(1);
		tfkhway.setPromptText("");
		tfkhway.setEditable(true);
		tfPlayNumber.setDisable(false);
		tfKeyWord.setDisable(false);

		tfKeyWord.setOpacity(1);
		tfKeyWord.setPromptText("");
		tfKeyWord.setEditable(true);

		tfPlayNumber.clear();
		tfkhway.clear();
		tfKeyWord.clear();
		tfAmount.clear();
	}// deleteonaction end

	public void methodFor3DKway(String kwayNum, ArrayList<String> listForRemovedItemsForR) {
		System.out.println(listForRemovedItemsForR + " listForRemovedItemsForR before");
		String khwayNumFor3D = kwayNum;
		String[] khwayNumArrFor3D = khwayNumFor3D.split("");
		if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1) && khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)
				&& khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
			listForR.clear();
		} else if (khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(1)
				&& khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(2)
				&& khwayNumFor3D.charAt(1) != khwayNumFor3D.charAt(2)) {
			listForR.clear();
			listForEveryNumForR.clear();
			firstTimeController = false;
			for (int i = 0; i < khwayNumArrFor3D.length; i++) {
				for (int j = 0; j < khwayNumArrFor3D.length; j++) {
					for (int k = 0; k < khwayNumArrFor3D.length; k++) {
						String formulatedNum = khwayNumArrFor3D[i].concat(khwayNumArrFor3D[j])
								.concat(khwayNumArrFor3D[k]);

						if (!khwayNumArrFor3D[i].equals(khwayNumArrFor3D[j])
								&& !khwayNumArrFor3D[j].equals(khwayNumArrFor3D[k])
								&& !khwayNumArrFor3D[i].equals(khwayNumArrFor3D[k])) {
							listForEveryNumForR.add(formulatedNum);
						}
						if (khwayNumArrFor3D[i].equals(khwayNumArrFor3D[j])
								|| khwayNumArrFor3D[j].equals(khwayNumArrFor3D[k])
								|| khwayNumArrFor3D[i].equals(khwayNumArrFor3D[k])
								|| formulatedNum.equals(khwayNumFor3D)) {
							continue;
						}
						listForR.add(formulatedNum);
					}
				}
			}
//		countToAddToR = 0;
//		listForRemovedItems.clear();

		} else if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1)
				|| khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)
				|| khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
			listForR.clear();
			listForEveryNumForR.clear();
			firstTimeController = false;
			for (int i = 0; i < khwayNumArrFor3D.length; i++) {
				for (int j = 0; j < khwayNumArrFor3D.length; j++) {
					aa: for (int k = 0; k < khwayNumArrFor3D.length; k++) {
						String formulatedNum = khwayNumArrFor3D[i].concat(khwayNumArrFor3D[j])
								.concat(khwayNumArrFor3D[k]);

						String sameThree = "";
						for (int a = 0; a < khwayNumFor3D.length(); a++) {

							sameThree = String.valueOf(khwayNumFor3D.charAt(a))
									+ String.valueOf(khwayNumFor3D.charAt(a)) + String.valueOf(khwayNumFor3D.charAt(a));

							listForEveryNumForR.add(formulatedNum);
							if (sameThree.equals(formulatedNum) || formulatedNum.equals(khwayNumFor3D)) {
								continue aa;
							}
						}
						listForR.add(formulatedNum);
					}
				}
			}
			Set<String> set = new LinkedHashSet<>(listForR);
			Set<String> setForEveryNum = new LinkedHashSet<>(listForEveryNumForR);

			listForR.clear();
			listForR.addAll(set);

			listForEveryNumForR.clear();
			listForEveryNumForR.addAll(setForEveryNum);
			if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1)
					|| khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)) {
				listForR.remove(listForR.size() - 4);
				listForR.remove(listForR.size() - 2);
				listForR.remove(listForR.size() - 1);

				listForEveryNumForR.remove(0);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 5);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 3);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 2);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 1);
			} else if (khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
				listForR.remove(listForR.size() - 5);
				listForR.remove(listForR.size() - 4);
				listForR.remove(listForR.size() - 3);

				listForEveryNumForR.remove(0);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 7);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 6);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 4);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 1);
			}
		}

		if (!listForRemovedItemsForR.isEmpty()) {
			System.out.println(listForEveryNumForR + " listForEveryNum before");
			System.out.println(listForRemovedItemsForR + " listForRemovedItemsForR before");
			listForEveryNumForR.removeAll(listForRemovedItemsForR);
		}
		listForKwayInPreview.clear();
		for (int i = 0; i < listForEveryNumForR.size(); i++) {
			int setAmountValue = Integer.parseInt(setAmount.getText());
			int amount = twoDService.toShowAmount(Integer.parseInt(listForEveryNumForR.get(i)), "3D", date.getValue());
			int result = setAmountValue - amount;

			listForKwayInPreview.add(new ObjectForPreviewTable(listForEveryNumForR.get(i), result));
		}
		previewTable.setItems(listForKwayInPreview);
		System.out.println(listForR + " listForR");
		System.out.println(listForEveryNumForR + " listForEveryNum");
		System.out.println(listForRemovedItemsForR + " listForRemovedItemsForR");
	}

	public void toCalculateTotalAmountForCurrentTable() {
		LocalDate dateFromPicker = date.getValue();
		QueriesForTowDMorning twoDMorning = new QueriesForTowDMorning();
		String choseName = cbChooseCustomer.getSelectionModel().getSelectedItem().getText();
		if (all.isSelected()) {
			ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForAllOption(choseName,
					dateFromPicker, title.getText());
			tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));

			int totalAmount = 0;

			for (int i = 0; i < dataReturnFromDB.size(); i++) {
				String middleNum = "";
				String playNumberForLoop = dataReturnFromDB.get(i).getPlayNumber();
				if (title.getText().equals("3D") && playNumberForLoop.length() == 3) {
					middleNum = playNumberForLoop.charAt(1) + "";
				}
				if (playNumberForLoop.equalsIgnoreCase("ss") || playNumberForLoop.equalsIgnoreCase("mm")
						|| playNumberForLoop.equalsIgnoreCase("sm") || playNumberForLoop.equalsIgnoreCase("ms")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 25) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("sp") || playNumberForLoop.equalsIgnoreCase("mp")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 5) + totalAmount;
				} else if (playNumberForLoop.length() > 2 && !playNumberForLoop.endsWith("*")
						&& !title.getText().equals("3D") && !playNumberForLoop.endsWith("R")) {
					ArrayList<String> list = generateForKhway(playNumberForLoop);
					totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
				} else if (playNumberForLoop.length() > 3 && playNumberForLoop.endsWith("*")) {
					ArrayList<String> list = generateForKhway(playNumberForLoop);
					totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("sp") || playNumberForLoop.startsWith("*")
						|| playNumberForLoop.endsWith("*") || playNumberForLoop.equalsIgnoreCase("p")
						|| playNumberForLoop.equalsIgnoreCase("n")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
				} else if (playNumberForLoop.endsWith("B")) {
					String firstNum = playNumberForLoop.charAt(0) + "";
					ArrayList<String> list = new ArrayList<>();

					int c = 0;
					int a = 0;
					int j = Integer.parseInt(firstNum);
					int k = Integer.parseInt("1" + firstNum);
					while (c <= Integer.parseInt(firstNum)) {
						list.add(String.valueOf(c).concat(String.valueOf(j)));
						c++;
						j--;
					}
					while (a <= Integer.parseInt("1" + firstNum)) {
						if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
							list.add(String.valueOf(a).concat(String.valueOf(k)));
						}
						a++;
						k--;
					}
					totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
				} else if (playNumberForLoop.endsWith(" R") && !title.getText().equals("3D")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 2) + totalAmount;
				} else if (playNumberForLoop.endsWith(" R") && title.getText().equals("3D")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 6) + totalAmount;
				} else if (playNumberForLoop.endsWith("+")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 19) + totalAmount;
				} else if (middleNum.equals("*")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
				} else {
					totalAmount = dataReturnFromDB.get(i).getAmount() + totalAmount;
				}
			}
			totalAmountCurTable.setText(df.format(totalAmount));
		} else {
			ObservableList<ObjectForCurrentTable> dataReturnFromDB = twoDMorning.extractDataForLastOption(choseName,
					dateFromPicker, title.getText());
			tVCurrentTable.setItems(dataReturnFromDB.filtered(obj -> obj.getAmount() != 0));

			int totalAmount = 0;

			for (int i = 0; i < dataReturnFromDB.size(); i++) {
				String middleNum = "";
				String playNumberForLoop = dataReturnFromDB.get(i).getPlayNumber();
				if (title.getText().equals("3D") && playNumberForLoop.length() == 3) {
					middleNum = playNumberForLoop.charAt(1) + "";
				}
				if (playNumberForLoop.equalsIgnoreCase("ss") || playNumberForLoop.equalsIgnoreCase("mm")
						|| playNumberForLoop.equalsIgnoreCase("sm") || playNumberForLoop.equalsIgnoreCase("ms")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 25) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("sp") || playNumberForLoop.equalsIgnoreCase("mp")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 5) + totalAmount;
				} else if (playNumberForLoop.length() > 2 && !playNumberForLoop.endsWith("*")
						&& !title.getText().equals("3D") && !playNumberForLoop.endsWith("R")) {
					ArrayList<String> list = generateForKhway(playNumberForLoop);
					totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
				} else if (playNumberForLoop.length() > 3 && playNumberForLoop.endsWith("*")) {
					ArrayList<String> list = generateForKhway(playNumberForLoop);
					totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("sp") || playNumberForLoop.startsWith("*")
						|| playNumberForLoop.endsWith("*") || playNumberForLoop.equalsIgnoreCase("p")
						|| playNumberForLoop.equalsIgnoreCase("n")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("R(5)")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 5) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("R(4)")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 4) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("R(3)")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 3) + totalAmount;
				} else if (playNumberForLoop.equalsIgnoreCase("R(2)")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 2) + totalAmount;
				} else if (playNumberForLoop.endsWith("B")) {
					String firstNum = playNumberForLoop.charAt(0) + "";
					ArrayList<String> list = new ArrayList<>();

					int c = 0;
					int a = 0;
					int j = Integer.parseInt(firstNum);
					int k = Integer.parseInt("1" + firstNum);
					while (c <= Integer.parseInt(firstNum)) {
						list.add(String.valueOf(c).concat(String.valueOf(j)));
						c++;
						j--;
					}
					while (a <= Integer.parseInt("1" + firstNum)) {
						if (String.valueOf(a).concat(String.valueOf(k)).length() != 3) {
							list.add(String.valueOf(a).concat(String.valueOf(k)));
						}
						a++;
						k--;
					}
					totalAmount = (dataReturnFromDB.get(i).getAmount() * list.size()) + totalAmount;
				} else if (playNumberForLoop.endsWith("R") && !title.getText().equals("3D")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 2) + totalAmount;
				} else if (playNumberForLoop.endsWith("R") && title.getText().equals("3D")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 6) + totalAmount;
				} else if (playNumberForLoop.endsWith("+")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 19) + totalAmount;
				} else if (middleNum.equals("*")) {
					totalAmount = (dataReturnFromDB.get(i).getAmount() * 10) + totalAmount;
				} else {
					totalAmount = dataReturnFromDB.get(i).getAmount() + totalAmount;
				}
			}
			totalAmountCurTable.setText(df.format(totalAmount));
		}
	}

	public ArrayList<String> methodToDerive3DKway(String kwayNum) {
		String khwayNumFor3D = kwayNum;
		String[] khwayNumArrFor3D = khwayNumFor3D.split("");
		if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1) && khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)
				&& khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
			listForR.clear();
		} else if (khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(1)
				&& khwayNumFor3D.charAt(0) != khwayNumFor3D.charAt(2)
				&& khwayNumFor3D.charAt(1) != khwayNumFor3D.charAt(2)) {
			listForR.clear();
			listForEveryNumForR.clear();
			firstTimeController = false;
			for (int i = 0; i < khwayNumArrFor3D.length; i++) {
				for (int j = 0; j < khwayNumArrFor3D.length; j++) {
					for (int k = 0; k < khwayNumArrFor3D.length; k++) {
						String formulatedNum = khwayNumArrFor3D[i].concat(khwayNumArrFor3D[j])
								.concat(khwayNumArrFor3D[k]);

						if (!khwayNumArrFor3D[i].equals(khwayNumArrFor3D[j])
								&& !khwayNumArrFor3D[j].equals(khwayNumArrFor3D[k])
								&& !khwayNumArrFor3D[i].equals(khwayNumArrFor3D[k])) {
							listForEveryNumForR.add(formulatedNum);
						}
						if (khwayNumArrFor3D[i].equals(khwayNumArrFor3D[j])
								|| khwayNumArrFor3D[j].equals(khwayNumArrFor3D[k])
								|| khwayNumArrFor3D[i].equals(khwayNumArrFor3D[k])
								|| formulatedNum.equals(khwayNumFor3D)) {
							continue;
						}
						listForR.add(formulatedNum);
					}
				}
			}
		} else if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1)
				|| khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)
				|| khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
			listForR.clear();
			listForEveryNumForR.clear();
			firstTimeController = false;
			for (int i = 0; i < khwayNumArrFor3D.length; i++) {
				for (int j = 0; j < khwayNumArrFor3D.length; j++) {
					aa: for (int k = 0; k < khwayNumArrFor3D.length; k++) {
						String formulatedNum = khwayNumArrFor3D[i].concat(khwayNumArrFor3D[j])
								.concat(khwayNumArrFor3D[k]);

						String sameThree = "";
						for (int a = 0; a < khwayNumFor3D.length(); a++) {

							sameThree = String.valueOf(khwayNumFor3D.charAt(a))
									+ String.valueOf(khwayNumFor3D.charAt(a)) + String.valueOf(khwayNumFor3D.charAt(a));

							listForEveryNumForR.add(formulatedNum);
							if (sameThree.equals(formulatedNum) || formulatedNum.equals(khwayNumFor3D)) {
								continue aa;
							}
						}
						listForR.add(formulatedNum);
					}
				}
			}
			Set<String> set = new LinkedHashSet<>(listForR);
			Set<String> setForEveryNum = new LinkedHashSet<>(listForEveryNumForR);

			listForR.clear();
			listForR.addAll(set);

			listForEveryNumForR.clear();
			listForEveryNumForR.addAll(setForEveryNum);
			if (khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(1)
					|| khwayNumFor3D.charAt(0) == khwayNumFor3D.charAt(2)) {
				listForR.remove(listForR.size() - 4);
				listForR.remove(listForR.size() - 2);
				listForR.remove(listForR.size() - 1);

				listForEveryNumForR.remove(0);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 5);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 3);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 2);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 1);
			} else if (khwayNumFor3D.charAt(1) == khwayNumFor3D.charAt(2)) {
				listForR.remove(listForR.size() - 5);
				listForR.remove(listForR.size() - 4);
				listForR.remove(listForR.size() - 3);

				listForEveryNumForR.remove(0);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 7);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 6);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 4);
				listForEveryNumForR.remove(listForEveryNumForR.size() - 1);
			}
		}
		return listForEveryNumForR;
	}

	public void commercialView() {
		new LoadView().loadViewWithNormalStage("CommercialView", "Commercial");
	}

	public void loadViewWithNormalStage(String fxml, String title) {
		Stage stage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/controller/".concat(fxml.concat(".fxml"))));
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.setOnHidden(e -> {
				loadCustomer();
				setDatainMaxTable();
				getWinNumbers();
				insertIntoList(false, 0);
				setDataToCurrentTable();
				tVCurrentTable.setItems(null);
				totalAmountCurTable.setText("0");
				totalvalueset();
				totalplaycountset();
//				totalAll.setText("Total   :  "
//						+ df.format(new QueriesForTowDMorning().getAllTotalData(morE, date.getValue())) + "  MMK");

			});
			stage.getIcons().add(new Image(TwoDThreeDApplication.class.getResourceAsStream("/img/logo.jpg")));
			stage.setResizable(false);
//			stage.setAlwaysOnTop(true);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void totalvalueset() {
		totalAll.setText("Total   :  " + df.format(new QueriesForTowDMorning().getAllTotalData(morE, date.getValue()))
				+ "  MMK");
	}

	public void totalplaycountset() {
		totalplaycount.setText((new QueriesForTowDMorning().getTotalPlayCount(morE, date.getValue())) + "");
	}

	public void changeLocation() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		File file = fileChooser.showDialog(null);
		if (file != null) {
			try {
				String previousFile = Files.readAllLines(Paths.get("backuplocation.txt")).get(0);
				Files.write(Paths.get("backuplocation.txt"), file.getAbsoluteFile().toString().getBytes(),
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				AlertShow.showInfo(
						"Previous Directory: " + previousFile + "\n" + "Current Directory: " + file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void showLocation() {
		try {
			AlertShow.showInfo("Current Location: " + Files.readAllLines(Paths.get("backuplocation.txt")).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void copyForCurrentTable() {
		List<OutSource> outSource = currentTable.getSelectionModel().getSelectedItems();
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		if (outSource != null) {

			currentTable.setOnKeyPressed(e -> {
				String copy = "";
				for (OutSource os : outSource) {
					copy += os.getExceedingNumber() + " - " + os.getCurrentAmt() + "\n";
				}
				content.putString(copy);
				clipboard.setContent(content);
			});
		}
	}

	public void copyForMMMCurrentTable() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		List<ObjectForCurrentTable> objectForCurrentTable = tVCurrentTable.getSelectionModel().getSelectedItems();
		if (objectForCurrentTable != null) {
			tVCurrentTable.setOnKeyPressed(e -> {
				String copy = "";
				for (ObjectForCurrentTable ofc : objectForCurrentTable) {
					copy += ofc.getPlayNumber() + " - " + ofc.getAmount() + "\n";
				}
				content.putString(copy);
				clipboard.setContent(content);
			});
		}
	}

	public void copyForMaxtable() {
		List<Exceeding> exceeding = maxTable.getSelectionModel().getSelectedItems();
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		if (exceeding != null) {
			maxTable.setOnKeyPressed(e -> {
				String copy = "";
				for (Exceeding ex : exceeding) {
					copy += ex.getExceedingNumber() + " - " + ex.getExceedingAmount() + "\n";
				}
				content.putString(copy);
				clipboard.setContent(content);
			});
		}
	}

}

//if not editable
//store amount in a list and when enter extract last index of that list
//that day that person that number that amount which is from the list update amount
