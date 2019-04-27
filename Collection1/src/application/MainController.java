package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.contact.entity.Contact;
import org.contact.entity.HibernateUtil;
import org.hibernate.Session;

import com.sun.istack.logging.Logger;

import javafx.beans.binding.ListBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

public class MainController {

	@FXML
	private Button browseImage;

	@FXML
	private ListView<String> listView;

	@FXML
	private Button addNew;

	@FXML
	private Button update;

	@FXML
	private Button delete;

	@FXML
	private Button back;

	@FXML
	private Button backward;

	@FXML
	private Button forward;

	@FXML
	private Button upfront;

	@FXML
	private Button exportButton, importButton;

	private CheckBox selectAllCheckBox;

// --------------------------------------	

	@FXML
	private TextField objetIdField;

	@FXML
	private TextField identificationField;

	@FXML
	private TextField prefixeMuseeField;

	@FXML
	private TextField inventaireField;

	@FXML
	private TextField localisationField;
	
	@FXML
	private TextField searchField;

	// --------------------------------------

	@FXML
	private TableView<Contact> table;

//	@FXML
//	TableColumn<Contact, Integer> objetIdCol;
//
//	@FXML
//	TableColumn<Contact, CheckBox> selectCol;
//
//	@FXML
//	TableColumn<Contact, String> identificationCol;
//
//	@FXML
//	TableColumn<Contact, String> prefixeMuseeCol;
//
//	@FXML
//	TableColumn<Contact, String> inventaireCol;
//
//	@FXML
//	TableColumn<Contact, String> localisationCol;
//
//	@FXML
//	TableColumn<Contact, byte[]> imageCol;

	// --------------------------------------

//	final ObservableList<Contact> data = FXCollections.observableArrayList();
	
	// -----------------------------------------
	private ContactController controller = new ContactController();

	@FXML
	private ImageView imv;

	@FXML
	private static int index;

	private byte[] imageBytes;

	ObservableList<String> prefixeList = FXCollections.observableArrayList("", "ADN", "CEC", "CG04",
			"EXPO", "FOR", "MAR", "MDLV", "MGD", "MMHV", "MMV", "MPGV", "MST", "SIST", "SLG", "UBAY");

	@FXML
	private ChoiceBox<String> prefixeBox;

//	private TableColumn<Contact, Integer> active;

	@FXML
	private void initialize() {
		prefixeBox.setValue(null);
		prefixeBox.setItems(prefixeList);
	}

	@FXML
	public void handle(ActionEvent t) throws IOException {

		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image files", "*.png", "*.jpg", "*.gif"),
				new ExtensionFilter("All files", "*.*"));

		File file = fileChooser.showOpenDialog(null);

		listView.getItems().add(file.getName());
		String imageURL = (file.toURI().toString());
		Image image = new Image(imageURL);
//	            	Alternative of the 2 lines just above:
//	                BufferedImage bufferedImage = ImageIO.read(file);
//	                Image image = SwingFXUtils.toFXImage(bufferedImage, null);

		imv.setImage(image);
		imv.setFitWidth(130);
		imv.setFitHeight(130);
		imv.setPreserveRatio(true);

		byte[] imageInBytes = new byte[(int) file.length()]; // image convert in byte form
		FileInputStream inputStream = new FileInputStream(file); // input stream object create to read the file
		inputStream.read(imageInBytes); // here inputstream object read the file
		inputStream.close();

		imageBytes = imageInBytes;

	}

	@SuppressWarnings("resource")
	@FXML
	private void handleExport(ActionEvent event) {

		try {
//			System.out.print("employees Selected : [");
//			for (Contact contact : table.getItems()) {
//				if (contact.getSelected()) {
//					System.out.print(contact.getImage() + ", ");
//				}
//			}
//			System.out.print(" ]\n");

			XSSFWorkbook wb = new XSSFWorkbook(); // for earlier version use HSSF
			XSSFSheet sheet = wb.createSheet("Objets selection");
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Objet ID");
			header.createCell(1).setCellValue("Identification");
			header.createCell(2).setCellValue("Préfixe Muséee");
			header.createCell(3).setCellValue("Inventaire");
			header.createCell(4).setCellValue("Localisation");

			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);

			int index = 1;
			for (Contact contact : table.getItems())
				if (contact.getSelected()) {
					XSSFRow row = sheet.createRow(index);
					row.createCell(0).setCellValue(contact.getObjetId().toString());
					row.createCell(1).setCellValue(contact.getIdentification());
					row.createCell(2).setCellValue(contact.getPrefixeMusee());
					row.createCell(3).setCellValue(contact.getInventaire());
					row.createCell(4).setCellValue(contact.getLocalisation());
					index++;
				}

			FileOutputStream fileOut = new FileOutputStream("ObjetSelection1.xlsx"); // before 2007 version xls
			wb.write(fileOut);
			fileOut.close();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Objets Selection has been exported as an Excel sheet");
			alert.showAndWait();

		} catch (FileNotFoundException e) {
			Logger.getLogger(MainController.class.getName(), null).log(Level.SEVERE, null, e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	@FXML
	private void handleImport(ActionEvent event) throws IOException {

		try {

			Session s = HibernateUtil.openSession();

			FileInputStream fileIn = new FileInputStream(new File("ObjetInfo.xlsx"));

			XSSFWorkbook wb = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = wb.getSheetAt(0);
			Row row;
			for (int i = 1; i < sheet.getLastRowNum(); i++) {
				row = (Row) sheet.getRow(i);

//				String objetId;
//				if (row.getCell(0) == null) {
//					objetId = "0";
//				} else
//					objetId = row.getCell(0).toString();

				String identification;
				if (row.getCell(1) == null) {
					identification = "null";
				} // suppose excel cell is empty then its set to 0 the variable
				else
					identification = row.getCell(1).toString(); // else copies cell data to name variable

				String prefixeMusee;
				if (row.getCell(2) == null) {
					prefixeMusee = "null";
				} else
					prefixeMusee = row.getCell(2).toString();

				String inventaire;
				if (row.getCell(3) == null) {
					inventaire = "null";
				} else
					inventaire = row.getCell(3).toString();

				String localisation;
				if (row.getCell(4) == null) {
					localisation = "null";
				} else
					localisation = row.getCell(4).toString();

				s.beginTransaction();
				Contact c = new Contact();
//				c.setObjetId(Integer.parseInt(objetId));
				c.setIdentification(identification);
				c.setPrefixeMusee(prefixeMusee);
				c.setInventaire(inventaire);
				c.setLocalisation(localisation);
				System.out.println(c.getObjetId() + " " + c.getIdentification() + " " + c.getPrefixeMusee() + " "
						+ c.getInventaire() + " " + c.getLocalisation());
				s.saveOrUpdate(c);
				s.getTransaction().commit();
			}

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Objets Details imported from Excel sheet to Database");
			alert.showAndWait();

			fileIn.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void handleAdd(ActionEvent event) {

		Contact c = new Contact();

		c.setObjetId(111);
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		c.setInventaire(inventaireField.getText());
		c.setLocalisation(localisationField.getText());
		c.setImage(imageBytes);

		if (validateFields()) {
		controller.addContact(c);
		populate();
		}
	}

	@FXML
	public void handleUpdate(ActionEvent event) {

		Contact c = new Contact();

		c.setObjetId(Integer.parseInt(objetIdField.getText()));
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		c.setInventaire(inventaireField.getText());
		c.setLocalisation(localisationField.getText());
		c.setImage(imageBytes);

//		if (validateFields()) {
		controller.updateContact(c);
		System.out.println("update button clicked");
		populate();
//		}
	}

	@FXML
	public void handleDelete(ActionEvent event) {

		Contact c = (Contact) controller.getContactList().get(index);
		controller.removeContact(c.getObjetId());

		populate();
	}

	@FXML
	public void handleBack(ActionEvent event) {
		index = 0;
		populate();
	}

	@FXML
	public void handleBackward(ActionEvent event) {

		if (index > 0) {
			index--;
		} else
			event.consume();
		populate();
	}

	@FXML
	public void handleForward(ActionEvent event) {

		if (index < controller.getContactList().size() - 1) {
			index++;
		} else
			event.consume();
		populate();
	}

	@FXML
	public void handleUpfront(ActionEvent event) {

		index = controller.getContactList().size() - 1;
		populate();
	}

	private void populate() {

		populateForm(index);
		populateTable();

	}

	private void populateForm(int i) {
		if (controller.getContactList().isEmpty())
			return;
		Contact c = (Contact) controller.getContactList().get(i);
		objetIdField.setText(c.getObjetId().toString());

		identificationField.setText(c.getIdentification());
		prefixeBox.setValue(c.getPrefixeMusee());
		inventaireField.setText(c.getInventaire());
		localisationField.setText(c.getLocalisation());
//	    imv.setImage()(c.getImage());

		byte[] getImageInBytes = c.getImage(); // image convert in byte form

		try {
			FileOutputStream outputstream = new FileOutputStream(new File("photo.jpg"));
			outputstream.write(getImageInBytes);

			Image image = new Image("file:photo.jpg");
			imv.setImage(image);
			imv.setFitWidth(130);
			imv.setFitHeight(130);
			imv.setPreserveRatio(true);

			outputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void populateTable() {
		table.getItems().clear();
		listView.getItems().clear();
		table.setItems(controller.getContactList());
		TableColumn<Contact, Integer> objetIdCol = new TableColumn<Contact, Integer>("Objet ID");
		objetIdCol.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("objetId"));
		TableColumn<Contact, String> identificationCol = new TableColumn<Contact, String>("Identification");
		identificationCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("identification"));
		TableColumn<Contact, String> prefixeMuseeCol = new TableColumn<Contact, String>("Prefixe Musée");
		prefixeMuseeCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("prefixeMusee"));
		TableColumn<Contact, String> inventaireCol = new TableColumn<Contact, String>("Inventaire");
		inventaireCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("inventaire"));
		TableColumn<Contact, String> localisationCol = new TableColumn<Contact, String>("Localisation");
		localisationCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("localisation"));

		TableColumn<Contact, byte[]> imageCol = new TableColumn<Contact, byte[]>("Image");
		imageCol.setCellValueFactory(new PropertyValueFactory<Contact, byte[]>("image"));

		TableColumn<Contact, Boolean> selectCol = new TableColumn<Contact, Boolean>("");
		selectCol.setMinWidth(50);
		selectCol.setGraphic(getSelectAllCheckBox());
		selectCol.setCellValueFactory(new PropertyValueFactory<Contact, Boolean>("selected"));

		selectCol.setCellFactory(new Callback<TableColumn<Contact, Boolean>, TableCell<Contact, Boolean>>() {
			public TableCell<Contact, Boolean> call(TableColumn<Contact, Boolean> p) {
				final TableCell<Contact, Boolean> cell = new TableCell<Contact, Boolean>() {
					@Override
					public void updateItem(final Boolean item, boolean empty) {
						if (item == null)
							return;
						super.updateItem(item, empty);
						if (!isEmpty()) {
							final Contact c = getTableView().getItems().get(getIndex());
							CheckBox checkBox = new CheckBox();
							checkBox.selectedProperty().bindBidirectional(c.selectedProperty());
							setGraphic(checkBox);
							getExportButton().setDisable(!checkBox.isSelected());
						}
					}
				};
				cell.setAlignment(Pos.CENTER);
				return cell;
			}
		});

		table.getColumns().setAll(objetIdCol, selectCol, identificationCol, prefixeMuseeCol, inventaireCol,
				localisationCol, imageCol);

// -------------------------------------------------------------------------------------------------------------		
		
		ListBinding<Boolean> lb = new ListBinding<Boolean>() {
			{
				bind(table.getItems());
			}

			@Override
			protected ObservableList<Boolean> computeValue() {
				ObservableList<Boolean> list = FXCollections.observableArrayList();
				for (Contact c : table.getItems()) {
					list.add(c.getSelected());
				}
				return list;
			}
		};
		
// -------------------------------------------------------------------------------------------------------------		
		lb.addListener(new ChangeListener<ObservableList<Boolean>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<Boolean>> arg0, ObservableList<Boolean> arg1,
					ObservableList<Boolean> l) {
			}
		});

// -------------------------------------------------------------------------------------------------------------			
		
	}


	public CheckBox getSelectAllCheckBox() {
		if (selectAllCheckBox == null) {
			final CheckBox selectAllCheckBox = new CheckBox();

			// Adding EventHandler to the CheckBox to select/deselect all employees in
			// table.
			selectAllCheckBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Setting the value in all the employees.
					for (Contact item : table.getItems()) {
						item.setSelected(selectAllCheckBox.isSelected());
					}
					getExportButton().setDisable(!selectAllCheckBox.isSelected());
				}
			});

			this.selectAllCheckBox = selectAllCheckBox;
		}
		return selectAllCheckBox;
	}

	public Button getExportButton() {
		if (this.exportButton == null) {
			final Button exportButton = new Button();
			exportButton.setDisable(true);
			this.exportButton = exportButton;
		}
		return this.exportButton;
	}
	

	
	@FXML
	private void handleSearch() {
		
		populate();
		
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<Contact> filteredData = new FilteredList<>(controller.getContactList(), e -> true);
	
		searchField.textProperty().addListener((observableValue, oldValue, newValue) ->{
			filteredData.setPredicate(contact -> {
				// If filter text is empty, display all contacts.
				if (newValue == null || newValue.isEmpty()){
					return true;
				}
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if(contact.getIdentification().toLowerCase().contains(lowerCaseFilter)){
					return true;// Filter matches identification.
				}
				else if(contact.getInventaire().toLowerCase().contains(lowerCaseFilter)) {
					return true;// Filter matches inventaire.
				}
				return false;// Does not match.
			});
		});
		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Contact> sortedData = new SortedList<> (filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		
		table.setItems(sortedData);
		
}
	
	private boolean validateFields() {
		
		if(identificationField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Identification est vide");
			alert.showAndWait();
		return false;
		}
		
		else if(prefixeBox.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le Préfixe du Musée n'a pas été sélèctionné");
			alert.showAndWait();
		return false;
		}
		
		else if(inventaireField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Inventaire est vide");
			alert.showAndWait();
		return false;
		}
		else if(localisationField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Localisation est vide");
			alert.showAndWait();
		return false;
		}
		else if(listView.getItems().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("La photo n'a pas été sélectionnée pour cet objet");
			alert.showAndWait();
		return false;
		}
		
		return true;
		
	}

}