package application;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.contact.entity.Contact;
import org.contact.entity.HibernateUtil;
import org.contact.service.ContactService;
import org.contact.service.ContactServiceImpl;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController implements Initializable {

	@FXML
	private JFXHamburger hamburger;

	@FXML
	private JFXDrawer drawer;

	@FXML
	private Button browseImage;

	@FXML
	private Button loadDatabase;

	@FXML
	private Button addNew;

	@FXML
	private Button updateFields;

	@FXML
	private Button updateImage;

	@FXML
	private Button deleteIndex;

	@FXML
	private Button deleteRow;

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

	@FXML
	private TextField imageNameField;

	// --------------------------------------

	@FXML
	private TableView<Contact> table;

	private Contact temp;

	private Date lastClickTime;

	// -----------------------------------------
	private ContactController controller = new ContactController();

	@FXML
	private ImageView imv;

	@FXML
	private static int index;

	private byte[] imageBytes;

	private byte[] imageByteUpdate;

	ObservableList<String> prefixeList = FXCollections.observableArrayList("", "ADN", "CEC", "CG04", "EXPO", "FOR",
			"MAR", "MDLV", "MGD", "MMHV", "MMV", "MPGV", "MST", "SIST", "SLG", "UBAY");

	@FXML
	private ChoiceBox<String> prefixeBox;

//	private TableColumn<Contact, Integer> active;

	private void enableButtons() {

//		update.setDisable(false);
		back.setDisable(false);
		backward.setDisable(false);
		forward.setDisable(false);
		upfront.setDisable(false);
	}

	private void disableButtons() {

//		update.setDisable(true);
		back.setDisable(true);
		backward.setDisable(true);
		forward.setDisable(true);
		upfront.setDisable(true);
	}

	@FXML
	private void enableDisableFiels() {

		addNew.setDisable(false);
		updateFields.setDisable(false);
		updateImage.setDisable(true);
	}

	@FXML
	private void enableDisableImage() {

//		addNew.setDisable(false);
		updateFields.setDisable(true);
//		updateImage.setDisable(false);
	}

	@FXML
	private void loadDatabase() {

		populate();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

//		prefixeBox.setValue(null);
		prefixeBox.setItems(prefixeList);
//		populate();
		imageNameField.clear();
		imv.setImage(null);
		identificationField.clear();
		inventaireField.clear();
		localisationField.clear();
		prefixeBox.setValue("");

		


		HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
		transition.setRate(-1);
		hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();

			if (drawer.isShown()) {
				drawer.close();
			} else {
				drawer.open();
				Image image2 = new Image("file:photo5.jpg");
				ImageView imv2 = new ImageView();
				imv2.setImage(image2);
				imv2.setFitWidth(140);
				imv2.setFitHeight(140);
				imv2.setPreserveRatio(true);

				drawer.setSidePane(imv2);
			}
		});

	}

	@FXML
	private void handleAutoComplete() {

		ContactService contactService = new ContactServiceImpl();

		TextFields.bindAutoCompletion(searchField, contactService.listIdentification());
		TextFields.bindAutoCompletion(searchField, contactService.listInventaire());
	}

	@FXML
	public void handle(ActionEvent t) throws IOException {

		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image files", "*.jpg"),
				new ExtensionFilter("All files", "*.*"));

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {

			updateImage.setDisable(false);
			addNew.setDisable(false);

			imageNameField.setText(file.getName());
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

		} else {
//			updateImage.setDisable(true);
			return;
		}
	}

	@SuppressWarnings("resource")
	@FXML
	private void handleExport(ActionEvent event) {

		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setTitle("Information Dialog");
		alert1.setHeaderText(null);
		alert1.setContentText("Veuiller choisir un dossier de destination");
		alert1.showAndWait();

		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(null);

		if (selectedDirectory == null) {
			// No Directory selected
		} else {

			try {
//				System.out.print("employees Selected : [");
//				for (Contact contact : table.getItems()) {
//					if (contact.getSelected()) {
//						System.out.print(contact.getImage() + ", ");
//					}
//				}
//				System.out.print(" ]\n");

				XSSFWorkbook wb = new XSSFWorkbook(); // for earlier version use HSSF

				CreationHelper helper = wb.getCreationHelper();

				// create sheet
				XSSFSheet sheet = wb.createSheet("Objets selection");

				// auto-size picture relative to its top-left corner

				XSSFRow header = sheet.createRow(0);
				header.createCell(0).setCellValue("Objet ID");
				header.createCell(1).setCellValue("Identification");
				header.createCell(2).setCellValue("Préfixe Muséee");
				header.createCell(3).setCellValue("Inventaire");
				header.createCell(4).setCellValue("Localisation");
				header.createCell(5).setCellValue("Image");

				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);
				int widthUnits = 8 * 256;
				sheet.setColumnWidth(5, widthUnits);

				int index = 1;
				for (Contact contact : table.getItems())
					if (contact.getSelected()) {
						XSSFRow row = sheet.createRow(index);
						row.setHeightInPoints(40);

						row.createCell(0).setCellValue(contact.getObjetId().toString());
						row.createCell(1).setCellValue(contact.getIdentification());
						row.createCell(2).setCellValue(contact.getPrefixeMusee());
						row.createCell(3).setCellValue(contact.getInventaire());
						row.createCell(4).setCellValue(contact.getLocalisation());

						// get image from the database to a file named "photo2.jpg"
						byte[] getImageInBytes2 = contact.getImage(); // image convert in byte form

						try {
							FileOutputStream outputstream = new FileOutputStream(new File("photo2.jpg"));

							outputstream.write(getImageInBytes2);

							outputstream.close();

						} catch (Exception e) {
							e.printStackTrace();
						}

						// resize this image before adding it to this workbook:

						BufferedImage originalImage = ImageIO.read(new File("photo2.jpg"));
						int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

						BufferedImage resizeImageJpg = resizeImage(originalImage, type, 50, 50);
						ImageIO.write(resizeImageJpg, "jpg", new File("photo3.jpg"));

						// add picture data to this workbook.
						InputStream is = new FileInputStream("photo3.jpg");
						byte[] bytes = IOUtils.toByteArray(is);
						int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
						is.close();

						// Create the drawing patriarch. This is the top level container for all shapes.
						@SuppressWarnings("rawtypes")
						Drawing drawing = sheet.createDrawingPatriarch();

						// add a picture shape
						ClientAnchor anchor = helper.createClientAnchor();
						// set top-left corner of the picture,
						// subsequent call of Picture#resize() will operate relative to it
//					    anchor.setCol1(3);
//					    anchor.setRow1(2);

//					    anchor.setCol1(1); //Column B
//					    anchor.setRow1(2); //Row 3
//					    anchor.setCol2(2); //Column C
//					    anchor.setRow2(3); //Row 4

						anchor.setCol1(5); // Column F

						anchor.setRow1(index); // Row 2

//					    anchor.setCol2(6); //Column C
//					    anchor.setRow2(2); //Row 3
						Picture pict = drawing.createPicture(anchor, pictureIdx);

						pict.resize();
						index++;
					}

				FileOutputStream fileOut = new FileOutputStream(
						selectedDirectory.getAbsolutePath() + "/ObjetSelection1.xlsx"); // before 2007 version xls
				wb.write(fileOut);
				fileOut.close();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Fichier Excel exporté");
				alert.showAndWait();

			} catch (FileNotFoundException e) {
				Logger.getLogger(MainController.class.getName(), null).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("resource")
	@FXML
	private void handleImport(ActionEvent event) throws IOException {

		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setTitle("Information Dialog");
		alert1.setHeaderText(null);
		alert1.setContentText("Veuiller choisir un fichier au format .xlsx");
		alert1.showAndWait();

		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Ecxcel files", "*.xlsx"),
				new ExtensionFilter("All files", "*.*"));

		File file = fileChooser.showOpenDialog(null);

		if (file == null) {
			// No Directory selected
		} else {

			try {

				Session s = HibernateUtil.openSession();

				FileInputStream fileIn = new FileInputStream(file);

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
		c.setImageName(imageNameField.getText());

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Ajout Base de Données");
		alert.setHeaderText(
				"Voulez-vous vraiment ajouter l'objet nommé '" + c.getIdentification() + "' à la base de données?");

		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			if (validateFields()) {
				controller.addContact(c);

				populate();

				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Information Ajout Base de Données");
				alert2.setHeaderText(null);
				alert2.setContentText("L'objet '" + c.getIdentification()
						+ "' a été ajouté à la base de données il porte actuellement le n° " + c.getObjetId()
						+ " au sein de cette base.");
				alert2.showAndWait();

			}
			updateFields.setDisable(true);

		} else {
			return;
//			populate(); // ... user chose CANCEL or closed the dialog
		}

	}

	@FXML
	public void handleUpdateFields(ActionEvent event) {

		Contact c = new Contact();

		c.setObjetId(Integer.parseInt(objetIdField.getText()));
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		c.setInventaire(inventaireField.getText());
		c.setLocalisation(localisationField.getText());

		try {

			BufferedImage bImage = ImageIO.read(new File("photo.jpg"));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bImage, "jpg", bos);
			byte[] data = bos.toByteArray();
			imageByteUpdate = data;

		} catch (Exception e) {
			e.printStackTrace();
		}

		c.setImage(imageByteUpdate);

		c.setImageName(imageNameField.getText());

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Mise à Jour Champ");
		alert.setHeaderText("Voulez-vous vraiment mettre à jour le ou les champs de l'objet N° " + c.getObjetId()
				+ " dans la base de données?");

		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			if (validateFields()) {
				controller.updateContact(c);

				populate();

				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Information Mise à Jour Champ");
				alert2.setHeaderText(null);
				alert2.setContentText("Le ou les champs de l'objet N° " + c.getObjetId() + " mis à jour avec succès.");
				alert2.showAndWait();
			}

		} else {
			return;
//			populate(); // ... user chose CANCEL or closed the dialog
		}

	}

	@FXML
	public void handleUpdateImage(ActionEvent event) {

		Contact c = new Contact();

		c.setObjetId(Integer.parseInt(objetIdField.getText()));
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		c.setInventaire(inventaireField.getText());
		c.setLocalisation(localisationField.getText());
		if (imageBytes != null) {
			c.setImage(imageBytes);
		} else {
			try {

				BufferedImage bImage = ImageIO.read(new File("photo.jpg"));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(bImage, "jpg", bos);
				byte[] data = bos.toByteArray();
				imageByteUpdate = data;

			} catch (Exception e) {
				e.printStackTrace();
			}

			c.setImage(imageByteUpdate);
		}

		c.setImageName(imageNameField.getText());

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Mise à Jour Image");
		alert.setHeaderText("Voulez-vous vraiment mettre à jour l'image de l'objet N° " + c.getObjetId() + " nommé '"
				+ c.getIdentification() + "'  dans la base de données?");

		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			if (validateFields()) {
				controller.updateContact(c);

				populate();

				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Information Mise à Jour Image");
				alert2.setHeaderText(null);
				alert2.setContentText("L'image de l'objet N° " + c.getObjetId() + " nommé '" + c.getIdentification()
						+ "' mis à jour avec succès.");
				alert2.showAndWait();
			}

		} else {
			return;
//			populate(); // ... user chose CANCEL or closed the dialog
		}

	}

	@FXML
	public void handleDeleteIndex(ActionEvent event) {

		Contact c = (Contact) controller.getContactList().get(index);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Supression Index");
		alert.setHeaderText("Voulez-vous vraiment suprimer l'objet n°" + c.getObjetId() + " '" + c.getIdentification()
				+ "' de la base de données?");
		populate();
		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			controller.removeContact(c.getObjetId());
			populate();// ... user chose OK

			Alert alertDeleted = new Alert(AlertType.INFORMATION);
			alertDeleted.setTitle("Information Supression Index");
			alertDeleted.setHeaderText(null);
			alertDeleted.setContentText("L'objet N° " + c.getObjetId() + " nommé '" + c.getIdentification()
					+ "' \n a bien été suprimé de la base de données.");
			alertDeleted.showAndWait();

		} else {
			return;
//			populate(); // ... user chose CANCEL or closed the dialog
		}

	}

	@FXML
	public void handleDeleteRow(ActionEvent event) {

		Contact row = table.getSelectionModel().getSelectedItem();

		if (row != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Supression Row");
			alert.setHeaderText("Voulez-vous vraiment suprimer \n l'objet n°" + row.getObjetId() + " '"
					+ row.getIdentification() + "' de la base de données?");

			java.util.Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				controller.removeContact(row.getObjetId());
				populate();// ... user chose OK

				Alert alertDeleted = new Alert(AlertType.INFORMATION);
				alertDeleted.setTitle("Information Supression Row");
				alertDeleted.setHeaderText(null);
				alertDeleted.setContentText("L'objet N° " + row.getObjetId() + " nommé '" + row.getIdentification()
						+ "' \n a bien été suprimé de la base de données.");
				alertDeleted.showAndWait();

			} else {
				// ... user chose CANCEL or closed the dialog
			}
		} else
			return;
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
		enableButtons();
		deleteRow.setDisable(true);
		deleteIndex.setDisable(false);
		addNew.setDisable(true);
		browseImage.setDisable(false);
		importButton.setDisable(false);
		deleteRow.setDisable(true);
		deleteIndex.setDisable(false);
		searchField.setEditable(true);
		identificationField.setEditable(true);
		prefixeBox.setDisable(false);
		inventaireField.setEditable(true);
		localisationField.setEditable(true);

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
		imageNameField.setText(c.getImageName());
//		listView.
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

		try {
			FileOutputStream outputstream = new FileOutputStream(new File("photo4.jpg"));
			outputstream.write(getImageInBytes);

			outputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream outputstream = new FileOutputStream(new File("photo5.jpg"));
			outputstream.write(getImageInBytes);

			outputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void populateTable() {
		table.getItems().clear();
//		listView.getItems().clear();
//		imageNameField.clear();
//		imv.setImage(null);
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

		TableColumn<Contact, String> imageNameCol = new TableColumn<Contact, String>("Titre de l'image");
		imageNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("imageName"));

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
				localisationCol, imageCol, imageNameCol);

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

		searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
			filteredData.setPredicate(contact -> {
				// If filter text is empty, display all contacts.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (contact.getIdentification().toLowerCase().contains(lowerCaseFilter)) {
					return true;// Filter matches identification.
				} else if (contact.getInventaire().toLowerCase().contains(lowerCaseFilter)) {
					return true;// Filter matches inventaire.
				}
				return false;// Does not match.
			});
		});
		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Contact> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		table.setItems(sortedData);
		deleteIndex.setDisable(true);
		deleteRow.setDisable(false);
		disableButtons();

	}

	@FXML
	private void handleRowSelect() {

		updateImage.setDisable(true);
		updateFields.setDisable(true);
		deleteIndex.setDisable(true);
		disableButtons();

		Contact row = table.getSelectionModel().getSelectedItem();
		if (row == null)
			return;
		if (row != temp) {
			temp = row;
			lastClickTime = new Date();
		} else if (row == temp) {
			Date now = new Date();
			long diff = now.getTime() - lastClickTime.getTime();
			if (diff < 300) { // another click registered in 300 millis

				deleteIndex.setDisable(true);
				deleteRow.setDisable(false);
				disableButtons();

				objetIdField.setText(row.getObjetId().toString());
				identificationField.setText(row.getIdentification());
				prefixeBox.setValue(row.getPrefixeMusee());
				inventaireField.setText(row.getInventaire());
				localisationField.setText(row.getLocalisation());
				imageNameField.setText(row.getImageName());

				byte[] getImageInBytes = row.getImage(); // image convert in byte form

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

				try {
					FileOutputStream outputstream = new FileOutputStream(new File("photo4.jpg"));
					outputstream.write(getImageInBytes);

					outputstream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					FileOutputStream outputstream = new FileOutputStream(new File("photo5.jpg"));
					outputstream.write(getImageInBytes);

					outputstream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				lastClickTime = new Date();
			}
		}
	}

	private boolean validateFields() {

		if (identificationField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Identification est vide");
			alert.showAndWait();
			return false;
		}

		else if (prefixeBox.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le Préfixe du Musée n'a pas été sélèctionné");
			alert.showAndWait();
			return false;
		}

		else if (inventaireField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Inventaire est vide");
			alert.showAndWait();
			return false;
		} else if (localisationField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Localisation est vide");
			alert.showAndWait();
			return false;
		} else if (imageNameField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validate fields");
			alert.setHeaderText(null);
			alert.setContentText(
					"La photo n'a pas été sélectionnée pour cet objet. \nVeuillez sélectionner une photo.");
			alert.showAndWait();
			return false;
		}

		return true;

	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int newWidth, int newHeight) {
		// Make sure the aspect ratio is maintained, so the image is not distorted
		double thumbRatio = (double) newWidth / (double) newHeight;
		int imageWidth = originalImage.getWidth(null);
		int imageHeight = originalImage.getHeight(null);
		double aspectRatio = (double) imageWidth / (double) imageHeight;

		if (thumbRatio < aspectRatio) {
			newHeight = (int) (newWidth / aspectRatio);
		} else {
			newWidth = (int) (newHeight * aspectRatio);
		}

		// Draw the scaled image
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		g.dispose();

		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}

	@FXML
	private void display() {

		Stage popupwindow = new Stage();

		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle(imageNameField.getText());

		VBox layout = new VBox(10);

		Image image2 = new Image("file:photo4.jpg");
		ImageView imv2 = new ImageView();
		imv2.setImage(image2);
		imv2.setFitWidth(900);
		imv2.setFitHeight(900);
		imv2.setPreserveRatio(true);

		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(imv2);

		Scene scene1 = new Scene(layout, 900, 900);

		popupwindow.setScene(scene1);

		popupwindow.showAndWait();

	}

}