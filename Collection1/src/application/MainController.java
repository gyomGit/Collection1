package application;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.objet.entity.HibernateUtil;
import org.objet.entity.Musee;
import org.objet.entity.Objet;
import org.objet.service.MuseeService;
import org.objet.service.MuseeServiceImpl;
import org.objet.service.ObjetService;
import org.objet.service.ObjetServiceImpl;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.sun.istack.logging.Logger;

import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *          Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *          que j'appelle 'Collection' servant à gérer les objets dans les
 *          Musées.
 * 
 *          Classe Contrôleur Principal de l'application 'Collection'
 */

public class MainController implements Initializable {

	@FXML
	private MenuItem menuItemImport;

	@FXML
	private MenuItem menuItemExport;

	@FXML
	private MenuItem menuItemClose;

	@FXML
	private MenuItem menuItemAddNew;

	@FXML
	private MenuItem menuItemUpdateImage;

	@FXML
	private MenuItem menuItemUpdateFields;

	@FXML
	private MenuItem menuItemDeleteRow;

	@FXML
	private MenuItem menuItemDeleteIndex;

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
	private TextField userNameField;

	@FXML
	private PasswordField passWordField;

	@FXML
	private Label statusLabel;

//  ---------------------------------------

	@FXML
	private TextField objetIdField;

	@FXML
	private TextField identificationField;

	@FXML
	private Label identificationView;

	@FXML
	private TextField inventaireField;

	@FXML
	private TextField localisationField;

	@FXML
	private TextField searchField;

	@FXML
	private TextField imageNameField;

	@FXML
	private TextField museeIdField;

	@FXML
	private TextField nomMuseeField;

	@FXML
	private TextField emailMuseeField;

	@FXML
	private TextField telMuseeField;

	@FXML
	private TextArea adresseMuseeField;

	// --------------------------------------

	@FXML
	private TableView<Objet> table;

	private Objet temp;

	private Date lastClickTime;

	// -----------------------------------------
	private ObjetController controller = new ObjetController();
	private MuseeController controllerMus = new MuseeController();

	@FXML
	private ImageView imv;

	@FXML
	private static int index;

	private byte[] imageBytes;

	private byte[] imageByteUpdate;

	ObservableList<String> prefixeList = FXCollections.observableArrayList("", "ADN", "CEC", "FOR", "MAR", "MDLV",
			"MGD", "MMHV", "MMV", "MPGV", "MST", "SIST", "SLG", "UBAY");

	@FXML
	private ChoiceBox<String> prefixeBox;

	private void enableButtons() {

		back.setDisable(false);
		backward.setDisable(false);
		forward.setDisable(false);
		upfront.setDisable(false);

		museeIdField.setDisable(false);
		nomMuseeField.setDisable(false);
		emailMuseeField.setDisable(false);
		telMuseeField.setDisable(false);
		adresseMuseeField.setDisable(false);
	}

	private void disableButtons() {

		back.setDisable(true);
		backward.setDisable(true);
		forward.setDisable(true);
		upfront.setDisable(true);

	}

	@FXML
	private void enableDisableFiels() {

		updateFields.setDisable(false);
		menuItemUpdateFields.setDisable(false);
		updateImage.setDisable(true);
		menuItemUpdateImage.setDisable(true);
	}

	@FXML
	private void enableDisableImage() {

		updateFields.setDisable(true);
		menuItemUpdateFields.setDisable(true);
	}

	@FXML
	private void loadDatabase() {

		populate();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		handleAddMusees();
		prefixeBox.setItems(prefixeList);
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

		ObjetService objetService = new ObjetServiceImpl();

		TextFields.bindAutoCompletion(searchField, objetService.listIdentification());
		TextFields.bindAutoCompletion(searchField, objetService.listInventaire());
	}

	/**
	 * Méthode déclanchée par le bouton 'Upload Image' de l'IHM servant à aller
	 * chercher une image dans le système de fichiers, affiche l'image dans l'IHM,
	 * converti cette dernière en byte[] et enfin la place dans une variable prête
	 * pour éventuellement être chargée dans la base de données. La méthode active
	 * par la même occasion les boutons 'Update Image' et 'Add New' de l'IHM qui
	 * sont initialement désactivés.
	 * 
	 * @throws IOException
	 */
	public void handleImage() throws IOException {

		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image files", "*.jpg"),
				new ExtensionFilter("All files", "*.*"));

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {

			updateImage.setDisable(false);
			menuItemUpdateImage.setDisable(false);
			addNew.setDisable(false);
			menuItemAddNew.setDisable(false);

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
			return;
		}
	}

	/**
	 * Méthode déclanchée par le bouton 'Export' de l'IHM servant à choisir une
	 * destination dans le système de fichiers pour ensuite y placer un fichier
	 * tableur Excel avec à l'intèrieur une sélection d'objets et leurs attributs.
	 * La sélection d'objets correspond aux objets qui ont été préalablement cochés
	 * dans le tableau de l'IHM par l'utilisateur. Les Objet sont placés avec la
	 * valeur de leurs attributs respectivement dans chaque ligne du tableur Excel à
	 * l'aide d'une boucle.
	 * 
	 * La méthode déclanchera tout d'abord une alerte d'introduction indiquant à
	 * l'utilisateur de choisir un dossier de destination. Puis pour finir à nouveau
	 * une alerte en conclusion pour informer en cas de succès de l'opération c'est
	 * à dire de la sauvegarde du fichier Excel à l'emplacement choisi.
	 * 
	 */

	@SuppressWarnings("resource")
	@FXML
	public void handleExport() {

		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setTitle("Information Dialog");
		alert1.setHeaderText(null);
		alert1.setContentText("Veuillez choisir un dossier de destination");
		alert1.showAndWait();

		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(null);

		if (selectedDirectory == null) {
			// No Directory selected
		} else {

			try {

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
				for (Objet objet : table.getItems())
					if (objet.getSelected()) {
						XSSFRow row = sheet.createRow(index);
						row.setHeightInPoints(40);

						row.createCell(0).setCellValue(objet.getObjetId().toString());
						row.createCell(1).setCellValue(objet.getIdentification());
						row.createCell(2).setCellValue(objet.getPrefixeMusee());
						row.createCell(3).setCellValue(objet.getInventaire());
						row.createCell(4).setCellValue(objet.getLocalisation());

						// get image from the database to a file named "photo2.jpg"
						byte[] getImageInBytes2 = objet.getImage(); // image convert in byte form

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

						anchor.setCol1(5); // Column F

						anchor.setRow1(index); // Row 2

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

	/**
	 * Méthode déclanchée par le bouton 'Import' de l'IHM servant à choisir un
	 * fichier tableur Excel au format .xlsx dans le système de fichiers pour
	 * ensuite importer chaque attribut situés dans chaque ligne du tableur Excel
	 * pour les placer respectivement dans chaque tuple coorespondant dans la base
	 * de données.
	 * 
	 * La méthode déclanche tout d'abord une alerte d'introduction indiquant à
	 * l'utilisateur de choisir un dossier Excel à importer puis pour finir à
	 * nouveau une alerte en conclusion pour informer en cas de succès de
	 * l'opération.
	 * 
	 * @throws IOException
	 */

	@SuppressWarnings("resource")
	@FXML
	public void handleImport() throws IOException {

		Alert alert1 = new Alert(AlertType.WARNING);
		alert1.setTitle("Information Dialog");
		alert1.setHeaderText(null);
		alert1.setContentText("Attention! Chaque donnée importée depuis un fichier Excel "
				+ "ne possèderont ni référence à un Musée ni image. Bien que les données seront importées et "
				+ "visibles dans la base et dans le tableau de l'interface, "
				+ "ces dernières, sans leur référence à un Musée et sans leur image, ne seront ni consultable "
				+ "ni modifiable. L'application 'Collection' (version 0.1) ne "
				+ "permet malheureusement pas de traiter le problème à ce stade. "
				+ "Veuillez nous en excusez, et maintenant: choisir un fichier Excel au format .xlsx ou bien Annuler.");
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
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
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
					Objet c = new Objet();
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
				populate();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Méthode déclanchée à chaque démarrage de l'Application par la méthode
	 * intialize() qui consiste à détecter si les Musées suseptibles d'être présents
	 * dans la table Musée de la base de données le sont bien. Si un ou plusieurs
	 * Musées n'y sont pas il sont alors systématiquement ajoutés par cette méthode.
	 * Ainsi, dans la bas de données, la table Musée est toujours vérifiée et
	 * complétée le cas échéant au démarrage de l'Application 'Collection'.
	 * 
	 */

	public void handleAddMusees() {

//		"ADN", "CEC", "CG04", "EXPO", "FOR",
//		"MAR", "MDLV", "MGD", "MMHV", "MMV", "MPGV", "MST", "SIST", "SLG", "UBAY");

		MuseeService museeService = new MuseeServiceImpl();

		while (!museeService.listNomMusee().contains("Musée au fil de la vallée")) {

			Musee ubayeValleeM = new Musee();
			ubayeValleeM.setNomMusee("Musée au fil de la vallée");
			ubayeValleeM.setEmailMusee("patrimoine@ubaye.com");
			ubayeValleeM.setTelMusee("04 92 81 27 15");
			ubayeValleeM.setAdressMusee("4 av des 3 frères Arnaud 04400 BARCELONNETTE");
			ubayeValleeM.setPrefixeMusee("UBAY");

			controllerMus.addMusee(ubayeValleeM);

		}

		while (!museeService.listNomMusee().contains("Musée de la Faience")) {

			Musee moustierFaienceM = new Musee();
			moustierFaienceM.setNomMusee("Musée de la Faience");
			moustierFaienceM.setEmailMusee("musee@moustiers.eu");
			moustierFaienceM.setTelMusee("04 92 74 61 64");
			moustierFaienceM.setAdressMusee("Rue du Seigneur de la Clue 04360 Moustiers-Sainte-Marie");
			moustierFaienceM.setPrefixeMusee("MST");

			controllerMus.addMusee(moustierFaienceM);

		}

		while (!museeService.listNomMusee().contains("Musée de la Préhistoire des Gorges du Verdon")) {

			Musee prehistoireQuinsonM = new Musee();
			prehistoireQuinsonM.setNomMusee("Musée de la Préhistoire des Gorges du Verdon");
			prehistoireQuinsonM.setEmailMusee("contact@museeprehistoire.com");
			prehistoireQuinsonM.setTelMusee("04 92 74 09 59");
			prehistoireQuinsonM.setAdressMusee("Route de Montmeyan 04500 Quinson");
			prehistoireQuinsonM.setPrefixeMusee("MPGV");

			controllerMus.addMusee(prehistoireQuinsonM);

		}

		while (!museeService.listNomMusee().contains("Musée Pierre Martel")) {

			Musee martelVachereM = new Musee();
			martelVachereM.setNomMusee("Musée Pierre Martel");
			martelVachereM.setEmailMusee("mairievach@orange.fr");
			martelVachereM.setTelMusee("04 92 75 62 15");
			martelVachereM.setAdressMusee("place de la mairie 04110 VACHERES");
			martelVachereM.setPrefixeMusee("MMV");

			controllerMus.addMusee(martelVachereM);

		}

		while (!museeService.listNomMusee().contains("Maison-Musée du Haut-Verdon")) {

			Musee maisonHautVerdonM = new Musee();
			maisonHautVerdonM.setNomMusee("Maison-Musée du Haut-Verdon");
			maisonHautVerdonM.setEmailMusee("maisonmusee.hautverdon@gmail.com");
			maisonHautVerdonM.setTelMusee("04 92 83 41 92");
			maisonHautVerdonM.setAdressMusee("Place Neuve 04370 Colmars les Alpes");
			maisonHautVerdonM.setPrefixeMusee("MMHV");

			controllerMus.addMusee(maisonHautVerdonM);

		}

		while (!museeService.listNomMusee().contains("Musée Gassendi à Digne")) {

			Musee digneGassendiM = new Musee();
			digneGassendiM.setNomMusee("Musée Gassendi à Digne");
			digneGassendiM.setEmailMusee("musee@musee-gassendi.org");
			digneGassendiM.setTelMusee("04 92 31 45 29");
			digneGassendiM.setAdressMusee("64, Boulevard Gassendi 04000 Digne-les-Bains");
			digneGassendiM.setPrefixeMusee("MGD");

			controllerMus.addMusee(digneGassendiM);

		}

		while (!museeService.listNomMusee().contains("Musée Archéologique de Riez")) {

			Musee archeoRiezM = new Musee();
			archeoRiezM.setNomMusee("Musée Archéologique de Riez");
			archeoRiezM.setEmailMusee("mairie.riez@wanadoo.fr");
			archeoRiezM.setTelMusee("04 92 77 99 09");
			archeoRiezM.setAdressMusee("route de Marseille 04500 Riez-la-Romaine");
			archeoRiezM.setPrefixeMusee("MAR");

			controllerMus.addMusee(archeoRiezM);

		}

		while (!museeService.listNomMusee().contains("Musée du Moyen Verdon")) {

			Musee moyenVerdonM = new Musee();
			moyenVerdonM.setNomMusee("Musée du Moyen Verdon");
			moyenVerdonM.setEmailMusee("maisonnaturepatrimoines@gmail.com");
			moyenVerdonM.setTelMusee("04 92 83 19 23");
			moyenVerdonM.setAdressMusee("Place Marcel Sauvaire 04120 Castellane");
			moyenVerdonM.setPrefixeMusee("CEC");

			controllerMus.addMusee(moyenVerdonM);

		}

		while (!museeService.listNomMusee().contains("Maison Alexandra David Néel")) {

			Musee maisonAlexDavNel = new Musee();
			maisonAlexDavNel.setNomMusee("Maison Alexandra David Néel");
			maisonAlexDavNel.setEmailMusee("association.davidneel@gmail.com");
			maisonAlexDavNel.setTelMusee("04 92 31 32 38");
			maisonAlexDavNel.setAdressMusee("27, avenue du Maréchal Juin 04000 Digne-les-Bains");
			maisonAlexDavNel.setPrefixeMusee("ADN");

			controllerMus.addMusee(maisonAlexDavNel);

		}

		while (!museeService.listNomMusee().contains("Musée de la Vallée")) {

			Musee museeDelaValM = new Musee();
			museeDelaValM.setNomMusee("Musée de la Vallée");
			museeDelaValM.setEmailMusee("musse@ville-barcellonnette.fr");
			museeDelaValM.setTelMusee("04 92 81 27 15");
			museeDelaValM.setAdressMusee("Villa la Sapinière - 10 avenue de la Libération 04400 Barcelonnette");
			museeDelaValM.setPrefixeMusee("MDLV");

			controllerMus.addMusee(museeDelaValM);

		}

		while (!museeService.listNomMusee().contains("Musée de Salagon")) {

			Musee salagonM = new Musee();
			salagonM.setNomMusee("Musée de Salagon");
			salagonM.setEmailMusee("musse.salagon@le04.fr");
			salagonM.setTelMusee("04 92 75 70 50");
			salagonM.setAdressMusee("Le Prieuré 04300 Mane");
			salagonM.setPrefixeMusee("SLG");

			controllerMus.addMusee(salagonM);

		}
		while (!museeService.listNomMusee().contains("Musée de Forcalquier")) {

			Musee forcalquierM = new Musee();
			forcalquierM.setNomMusee("Musée de Forcalquier");
			forcalquierM.setEmailMusee("musse.forcalquier@le04.fr");
			forcalquierM.setTelMusee("04 92 75 00 13");
			forcalquierM.setAdressMusee("5 Place du Bourguet 04300 Forcalquier");
			forcalquierM.setPrefixeMusee("FOR");

			controllerMus.addMusee(forcalquierM);

		}
		while (!museeService.listNomMusee().contains("Musée de Sisteron")) {

			Musee sisteronM = new Musee();
			sisteronM.setNomMusee("Musée de Sisteron");
			sisteronM.setEmailMusee("musee.archeo@sisteron.fr");
			sisteronM.setTelMusee("04 92 61 58 40");
			sisteronM.setAdressMusee("8 Rue Saunerie 04200 Sisteron");
			sisteronM.setPrefixeMusee("SIST");

			controllerMus.addMusee(sisteronM);

		}
	}

	/**
	 * Méthode déclanchée par le bouton 'Add New' de l'IHM servant à ajouter les
	 * données d'un objet de collection dans la base de données. La méthode commence
	 * à attribuer chacune des valeurs entrées par l'utilisateur à l'objet, mais
	 * quand arrive la question de l'attribution du Musée, c'est alors grâce à une
	 * boucle que la méthode va faire correspondre le bon péfixe Musée parmi ceux de
	 * la base de données avec celui qui a été sélectionné par l'utilisateur. Elle
	 * va ensuite attribuer le Musée à l'objet. Puis pour finir l'objet est
	 * enregisté dans la base de données avec tous ses attributs et la référence à
	 * son Musée.
	 * 
	 * La méthode déclanche dans la boucle une alerte de confirmation demandant à
	 * l'utilisateur si il veut vraiment ajouter les données du nouvel objet à la
	 * base. Si l'utilisateur choisit OK une alerte apparait l'informant du succès
	 * de l'opération en donnant quelque détails.
	 * 
	 */

	@FXML
	public void handleAdd() {

		Objet c = new Objet();

		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());

		if (prefixeBox.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Le Préfixe du Musée n'a pas été sélèctionné");
			alert.showAndWait();
			return;
		}

		c.setInventaire(inventaireField.getText());
		c.setLocalisation(localisationField.getText());
		c.setImage(imageBytes);
		c.setImageName(imageNameField.getText());

		for (int i = 0; i < controllerMus.getMuseeList().size(); i++) {

			if ((controllerMus.getMuseeList().get(i).getPrefixeMusee()).equals(prefixeBox.getValue()))

			{

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Ajout Base de Données");
				alert.setHeaderText("Voulez-vous vraiment ajouter l'objet nommé '" + c.getIdentification()
						+ "' à la base de données?");
				java.util.Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {

					if (validateFields()) {

						c.setMusee(controllerMus.getMuseeList().get(i));
						controller.addObjet(c);
						System.out.println("OUAII!!!");

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
					menuItemUpdateFields.setDisable(true);
				} else {
					return;
				}
				return;
			}
		}
	}

	@FXML
	public void handleUpdateFields() {

		Objet c = new Objet();

		c.setObjetId(Integer.parseInt(objetIdField.getText()));
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		
		if (prefixeBox.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Le Préfixe du Musée n'a pas été sélèctionné");
			alert.showAndWait();
			return;
		}
		
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

		for (int i = 0; i < controllerMus.getMuseeList().size(); i++) {

			if ((controllerMus.getMuseeList().get(i).getPrefixeMusee()).equals(prefixeBox.getValue()))

			{

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Mise à Jour Champ");
				alert.setHeaderText("Voulez-vous vraiment mettre à jour le ou les champs de l'objet N° "
						+ c.getObjetId() + " dans la base de données?");

				java.util.Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {

					if (validateFields()) {

						c.setMusee(controllerMus.getMuseeList().get(i));
						controller.updateObjet(c);
						System.out.println("OUAII!!!");

						populate();

						Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setTitle("Information Mise à Jour Champ");
						alert2.setHeaderText(null);
						alert2.setContentText(
								"Le ou les champs de l'objet N° " + c.getObjetId() + " mis à jour avec succès.");
						alert2.showAndWait();
					}
				} else {
					return;
					// ... user chose CANCEL or closed the dialog
				}
				return;
			}
		}
	}

	@FXML
	public void handleUpdateImage() {

		Objet c = new Objet();

		c.setObjetId(Integer.parseInt(objetIdField.getText()));
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		
		if (prefixeBox.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Le Préfixe du Musée n'a pas été sélèctionné");
			alert.showAndWait();
			return;
		}
		
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

		for (int i = 0; i < controllerMus.getMuseeList().size(); i++) {

			if ((controllerMus.getMuseeList().get(i).getPrefixeMusee()).equals(prefixeBox.getValue()))

			{

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Mise à Jour Image");
				alert.setHeaderText("Voulez-vous vraiment mettre à jour l'image de l'objet N° " + c.getObjetId()
						+ " nommé '" + c.getIdentification() + "'  dans la base de données?");

				java.util.Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {

					if (validateFields()) {

						c.setMusee(controllerMus.getMuseeList().get(i));
						controller.updateObjet(c);
						System.out.println("OUAII!!!");

						populate();

						Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setTitle("Information Mise à Jour Image");
						alert2.setHeaderText(null);
						alert2.setContentText("L'image de l'objet N° " + c.getObjetId() + " nommé '"
								+ c.getIdentification() + "' mis à jour avec succès.");
						alert2.showAndWait();
					}

				} else {
					return;
//			populate(); // ... user chose CANCEL or closed the dialog
				}

				return;

			}

		}

	}

	@FXML
	public void handleDeleteIndex() {

		Objet c = (Objet) controller.getObjetList().get(index);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Supression Index");
		alert.setHeaderText("Voulez-vous vraiment suprimer l'objet n°" + c.getObjetId() + " '" + c.getIdentification()
				+ "' de la base de données?");
		populate();
		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			controller.removeObjet(c.getObjetId());
			index = 0;
			populate();

			Alert alertDeleted = new Alert(AlertType.INFORMATION);
			alertDeleted.setTitle("Information Supression Index");
			alertDeleted.setHeaderText(null);
			alertDeleted.setContentText("L'objet N° " + c.getObjetId() + " nommé '" + c.getIdentification()
					+ "' \n a bien été suprimé de la base de données.");
			alertDeleted.showAndWait();

		} else {
			return;
			// ... user chose CANCEL or closed the dialog
		}
	}

	@FXML
	public void handleDeleteRow() {

		Objet row = table.getSelectionModel().getSelectedItem();

		if (row != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Supression Row");
			alert.setHeaderText("Voulez-vous vraiment suprimer \n l'objet n°" + row.getObjetId() + " '"
					+ row.getIdentification() + "' de la base de données?");

			java.util.Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				controller.removeObjet(row.getObjetId());
				index = 0;
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
	public void handleBack() {
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

		if (index < controller.getObjetList().size() - 1) {
			index++;
		} else
			event.consume();
		populate();
	}

	@FXML
	public void handleUpfront() {

		index = controller.getObjetList().size() - 1;
		populate();
	}

	private void populate() {

		populateForm(index);
		populateTable();
		enableButtons();
		addNew.setDisable(true);
		menuItemAddNew.setDisable(true);
		browseImage.setDisable(false);
		importButton.setDisable(false);
		deleteRow.setDisable(true);
		menuItemDeleteRow.setDisable(true);
		deleteIndex.setDisable(false);
		menuItemDeleteIndex.setDisable(false);
		searchField.setEditable(true);
		identificationField.setEditable(true);
		prefixeBox.setDisable(false);
		inventaireField.setEditable(true);
		localisationField.setEditable(true);
		hamburger.setVisible(true);
	}

	private void populateForm(int i) {
		if (controller.getObjetList().isEmpty())
			return;

		Objet c = (Objet) controller.getObjetList().get(i);

		if (c.getMusee() == null || c.getImage() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Cet objet N° " + c.getObjetId() + " qui a probablement été importé "
					+ "depuis un fichier Excel ne possède pas de référence à un Musée "
					+ "ou pas d'image ou bien les deux à la fois. "
					+ "L'objet est par conséquent ni consultable ni modifiable. "
					+ "L'application 'Collection' version 0.1 ne permet pas de traiter le problème à ce stade.");
			alert.showAndWait();
//			populateTable();
			return;
		}

		objetIdField.setText(c.getObjetId().toString());
		identificationField.setText(c.getIdentification());
		identificationView.setText(c.getIdentification());
		prefixeBox.setValue(c.getPrefixeMusee());
		inventaireField.setText(c.getInventaire());
		localisationField.setText(c.getLocalisation());
		imageNameField.setText(c.getImageName());

		museeIdField.setText(c.getMusee().getMuseeId().toString());
		nomMuseeField.setText(c.getMusee().getNomMusee());
		emailMuseeField.setText(c.getMusee().getEmailMusee());
		telMuseeField.setText(c.getMusee().getTelMusee());
		adresseMuseeField.setText(c.getMusee().getAdressMusee());

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
		table.setItems(controller.getObjetList());

		TableColumn<Objet, Integer> objetIdCol = new TableColumn<Objet, Integer>("Objet ID");
		objetIdCol.setCellValueFactory(new PropertyValueFactory<Objet, Integer>("objetId"));

		TableColumn<Objet, String> identificationCol = new TableColumn<Objet, String>("Identification");
		identificationCol.setCellValueFactory(new PropertyValueFactory<Objet, String>("identification"));

		TableColumn<Objet, String> prefixeMuseeCol = new TableColumn<Objet, String>("Prefixe Musée");
		prefixeMuseeCol.setCellValueFactory(new PropertyValueFactory<Objet, String>("prefixeMusee"));

		TableColumn<Objet, String> inventaireCol = new TableColumn<Objet, String>("Inventaire");
		inventaireCol.setCellValueFactory(new PropertyValueFactory<Objet, String>("inventaire"));

		TableColumn<Objet, String> localisationCol = new TableColumn<Objet, String>("Localisation");
		localisationCol.setCellValueFactory(new PropertyValueFactory<Objet, String>("localisation"));

		TableColumn<Objet, String> imageNameCol = new TableColumn<Objet, String>("Titre de l'image");
		imageNameCol.setCellValueFactory(new PropertyValueFactory<Objet, String>("imageName"));

		TableColumn<Objet, byte[]> imageCol = new TableColumn<Objet, byte[]>("Image");
		imageCol.setCellValueFactory(new PropertyValueFactory<Objet, byte[]>("image"));

		TableColumn<Objet, Boolean> selectCol = new TableColumn<Objet, Boolean>("");
		selectCol.setMinWidth(50);
		selectCol.setGraphic(getSelectAllCheckBox());
		selectCol.setCellValueFactory(new PropertyValueFactory<Objet, Boolean>("selected"));

		selectCol.setCellFactory(new Callback<TableColumn<Objet, Boolean>, TableCell<Objet, Boolean>>() {

			public TableCell<Objet, Boolean> call(TableColumn<Objet, Boolean> p) {
				final TableCell<Objet, Boolean> cell = new TableCell<Objet, Boolean>() {

					@Override
					public void updateItem(final Boolean item, boolean empty) {
						if (item == null)
							return;
						super.updateItem(item, empty);
						if (!isEmpty()) {
							final Objet c = getTableView().getItems().get(getIndex());
							CheckBox checkBox = new CheckBox();
							checkBox.selectedProperty().bindBidirectional(c.selectedProperty());
							setGraphic(checkBox);
							getExportButton().setDisable(!checkBox.isSelected());
							getMenuItemExport().setDisable(!checkBox.isSelected());
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
				for (Objet c : table.getItems()) {
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
					for (Objet item : table.getItems()) {
						item.setSelected(selectAllCheckBox.isSelected());
					}
					getExportButton().setDisable(!selectAllCheckBox.isSelected());
					getMenuItemExport().setDisable(!selectAllCheckBox.isSelected());
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

	public MenuItem getMenuItemExport() {
		if (this.menuItemExport == null) {
			final MenuItem exportMenuItem = new MenuItem();
			exportMenuItem.setDisable(true);
			this.menuItemExport = exportMenuItem;
		}
		return this.menuItemExport;
	}

	@FXML
	private void handleSearch() {

		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<Objet> filteredData = new FilteredList<>(controller.getObjetList(), e -> true);

		searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
			filteredData.setPredicate(objet -> {
				// If filter text is empty, display all objets.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (objet.getIdentification().toLowerCase().contains(lowerCaseFilter)) {
					return true;// Filter matches identification.
				} else if (objet.getInventaire().toLowerCase().contains(lowerCaseFilter)) {
					return true;// Filter matches inventaire.
				}
				return false;// Does not match.
			});
		});
		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Objet> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		table.setItems(sortedData);

		deleteIndex.setDisable(true);
		menuItemDeleteIndex.setDisable(true);
		deleteRow.setDisable(false);
		menuItemDeleteRow.setDisable(false);
		updateFields.setDisable(true);
		menuItemUpdateFields.setDisable(true);
		disableButtons();

	}

	@FXML
	private void handleRowSelect() {

		addNew.setDisable(true);
		updateImage.setDisable(true);
		menuItemUpdateImage.setDisable(true);
		updateFields.setDisable(true);
		menuItemUpdateFields.setDisable(true);
		deleteIndex.setDisable(true);
		menuItemDeleteIndex.setDisable(true);
		disableButtons();

		Objet row = table.getSelectionModel().getSelectedItem();
		if (row == null)
			return;
		if (row != temp) {
			temp = row;
			lastClickTime = new Date();
		} else if (row == temp) {
			Date now = new Date();
			long diff = now.getTime() - lastClickTime.getTime();
			if (diff < 300) { // another click registered in 300 millis

				if (row.getMusee() == null || row.getImage() == null) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Avertissement");
					alert.setHeaderText(null);
					alert.setContentText("Cet objet N° " + row.getObjetId() + " qui a probablement été importé "
							+ "depuis un fichier Excel ne possède pas de référence à un Musée "
							+ "ou pas d'image ou bien les deux à la fois. "
							+ "L'objet est par conséquent ni consultable ni modifiable. "
							+ "L'application 'Collection' version 0.1 ne permet pas de traiter le problème à ce stade. "
							+ "Vous pouvez choisir de suprimer cet objet en clickant le bouton 'Delete Row'");
					alert.showAndWait();
					deleteRow.setDisable(false);
					identificationField.setDisable(true);
					prefixeBox.setDisable(true);
					inventaireField.setDisable(true);
					localisationField.setDisable(true);
					browseImage.setDisable(true);
					return;
				}

				identificationField.setDisable(false);
				prefixeBox.setDisable(false);
				inventaireField.setDisable(false);
				localisationField.setDisable(false);
				browseImage.setDisable(false);

				deleteIndex.setDisable(true);
				menuItemDeleteIndex.setDisable(true);
				deleteRow.setDisable(false);
				menuItemDeleteRow.setDisable(false);
				disableButtons();

				objetIdField.setText(row.getObjetId().toString());
				identificationField.setText(row.getIdentification());
				identificationView.setText(row.getIdentification());
				prefixeBox.setValue(row.getPrefixeMusee());
				inventaireField.setText(row.getInventaire());
				localisationField.setText(row.getLocalisation());
				imageNameField.setText(row.getImageName());

				museeIdField.setText(row.getMusee().getMuseeId().toString());
				nomMuseeField.setText(row.getMusee().getNomMusee());
				emailMuseeField.setText(row.getMusee().getEmailMusee());
				telMuseeField.setText(row.getMusee().getTelMusee());
				adresseMuseeField.setText(row.getMusee().getAdressMusee());

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
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Identification est vide");
			alert.showAndWait();
			return false;
		} else if (inventaireField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Inventaire est vide");
			alert.showAndWait();
			return false;
		} else if (localisationField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Le champ Localisation est vide");
			alert.showAndWait();
			return false;
		} else if (imageNameField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Avertissement");
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
		imv2.setFitWidth(800);
		imv2.setFitHeight(800);
		imv2.setPreserveRatio(true);

		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(imv2);

		Scene scene1 = new Scene(layout, 1000, 1000);

		popupwindow.setScene(scene1);

		popupwindow.showAndWait();
	}

	@FXML
	private void closeApp() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation de fermeture de l'application");
		alert.setHeaderText("Voulez-vous vraiment quitter l'application?");

		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			Platform.exit();
			System.exit(0);

		} else {
			return;
		}
	}
}