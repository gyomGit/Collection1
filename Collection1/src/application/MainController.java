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
 *          en relation avec les données graphiques du fichier Main2.fxml
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

	/**
	 * Méthode servant à activer certains boutons et certains champs.
	 */

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
	
	/**
	 * Méthode servant à désactiver les boutons de navigation par saut d'index
	 * dans la base.
	 */

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

	/**
	 * Méthode d'initialisation déclanchée systématiquement au démarrage de
	 * l'application servant tout d'abord à déclancher la méthode handleAddMusees()
	 * qui est appelée ici pour assurer la création et le bon remplissage de la
	 * table Musée dans la base de données. Ensuite, pour attribuer la liste des
	 * préfixes Musée à la ChoiceBox nommée prefixeBox. Puis pour rendre des champs
	 * et une vignette image vierges. Et enfin, instancie un petit 'Hamburger Back
	 * Arrow' avec son petit tiroir coulissant pour faire apparaitre dans une
	 * vignette tiroir la même image que celle affichée dans la vignette de
	 * l'interface IHM située en dessous (petit gadget graphique).
	 */

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

	/**
	 * Méthode déclanchée par le champ 'recherche' de l'IHM quand un ou plusieurs
	 * charactères sont rentrés et que la touche du clavier est pressée. Cette
	 * méthode complète ansi la méthode handleSearch() qui elle se déclanche quand
	 * la touche du clavier est relachée. Cette méthode sert à rechercher les
	 * occurences d'un charactère ou d'une suite de charactères correspondant à un
	 * mot ou une suite de chiffres. La méthode recherche ici uniquement dans les
	 * colonnes 'identification' et 'N° Inventaire'. Chaque mot ou suite de chiffres
	 * ainsi entré va déclancher une proposition d'auto remplissage du champ
	 * recherche dans un menu déroulant juste en dessous de ce champ 'recherche'.
	 */

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
//	            	Alternative aux deux lignes du dessus:
//	                BufferedImage bufferedImage = ImageIO.read(file);
//	                Image image = SwingFXUtils.toFXImage(bufferedImage, null);

			imv.setImage(image);
			imv.setFitWidth(130);
			imv.setFitHeight(130);
			imv.setPreserveRatio(true);

			byte[] imageInBytes = new byte[(int) file.length()]; // image convertie en byte
			FileInputStream inputStream = new FileInputStream(file); // inputstream objet créé pour lire le fichier
			inputStream.read(imageInBytes); // ici inputstream lit le fichier
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
			// Aucun dossier sélectionné
		} else {

			try {

				XSSFWorkbook wb = new XSSFWorkbook(); // pour des versions anciennes utiliser HSSF

				CreationHelper helper = wb.getCreationHelper();

				// crée la page
				XSSFSheet sheet = wb.createSheet("Objets selection");

				// auto-size picture relative to its top-left corner
				// dimenssionne l'image en fonction de son coin en haut à gauche

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

						// prend l'image depuis la base de données et la place dans un fichier nommé
						// "photo2.jpg"
						byte[] getImageInBytes2 = objet.getImage(); // image convertie en tableau de bytes

						try {
							FileOutputStream outputstream = new FileOutputStream(new File("photo2.jpg"));

							outputstream.write(getImageInBytes2);

							outputstream.close();

						} catch (Exception e) {
							e.printStackTrace();
						}

						// redimenssionne l'image avant de l'ajouter au classeur:

						BufferedImage originalImage = ImageIO.read(new File("photo2.jpg"));
						int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

						BufferedImage resizeImageJpg = resizeImage(originalImage, type, 50, 50);
						ImageIO.write(resizeImageJpg, "jpg", new File("photo3.jpg"));

						// ajoute les données de l'image au classeur
						InputStream is = new FileInputStream("photo3.jpg");
						byte[] bytes = IOUtils.toByteArray(is);
						int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
						is.close();

						// Crée patriarchze du dessin. C'est le conteneur de base de toutes les
						// représentations.
						@SuppressWarnings("rawtypes")
						Drawing drawing = sheet.createDrawingPatriarch();

						// ajoute une représentation de l'image
						ClientAnchor anchor = helper.createClientAnchor();
						// attribue le coin en haut à gauche à l'image,
						// Chaque operation de redimession Picture#resize() opererons depuis ce coin là.

						anchor.setCol1(5); // Column F

						anchor.setRow1(index); // Row 2

						Picture pict = drawing.createPicture(anchor, pictureIdx);

						pict.resize();
						index++;
					}

				FileOutputStream fileOut = new FileOutputStream(
						selectedDirectory.getAbsolutePath() + "/ObjetSelection1.xlsx"); // avant 2007 version xls
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
	 * pour les placer respectivement dans chaque tuple corespondant dans la base de
	 * données.
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
				+ "Veuillez m'en excusez, et maintenant: choisir un fichier Excel au format .xlsx ou bien Annuler.");
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
	 * Méthode déclanchée à chaque démarrage de l'Application appelée par la méthode
	 * intialize(). Cette méthode consiste à détecter si les Musées suseptibles
	 * d'être présents dans la table Musée de la base de données le sont bien. Si un
	 * ou plusieurs Musées n'y sont pas il sont alors systématiquement ajoutés par
	 * cette méthode. Ainsi, dans la base de données, la table Musée est toujours
	 * vérifiée et complétée le cas échéant au démarrage de l'Application.
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
	 * quand arrive la question de l'attribution du Musée à cet objet, c'est alors
	 * grâce à une boucle que la méthode va faire correspondre le bon péfixe Musée
	 * sélectionné par l'utilisateur parmi ceux de la base de données. Elle va
	 * ensuite attribuer le Musée à l'objet. Puis, pour finir, l'objet est enregisté
	 * dans la base de données avec la référence à son Musée et tous ses attributs.
	 * 
	 * Concernant le déclanchement des messages d'alerte: tout d'abord, si
	 * l'utilisateur omet de sélectionner un préfixe Musée la méthode déclanche une
	 * alerte d'avertissement. Ensuite, la méthode déclanche, dans la boucle, une
	 * alerte de confirmation demandant à l'utilisateur si il veut vraiment ajouter
	 * les données du nouvel objet à la base. Si l'utilisateur choisit 'OK', la
	 * métohode vérifie à nouveau si les champs obligatoitres, restant à remplir,
	 * sont bien tous remplis sans quoi, une alerte d'avertissement est déclanchée
	 * pour chacune de ommissions. Enfin, si tout est bien rempli, une alerte
	 * apparait informant l'utilisateur du succès de l'opération et en donnant en
	 * plus à ce dernier, quelques détails sur cette opération finale.
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

	/**
	 * Méthode déclanchée par le bouton 'Update Fields' de l'IHM servant à mettre à
	 * jour toutes les valeurs des attributs de l'objet mais pas son image. Méthode
	 * identique à la méthode addNew() décrite précédement à deux exceptions prêt:
	 * la première execption est que la méthode recopie l'image déjà attribuée à
	 * l'objet de manière à ce que cette dernière soit remplacée à l'identique au
	 * côtés de son objet comme si elle n'avait jamais été touchée. Il n'est pas
	 * question ici de modifier l'image de l'objet. La deuxième exception qui fait
	 * que cette méthode est différente de addNew() décrite précédement est que
	 * cette méthode là va opérer une simple mise à jour corrective de l'objet et
	 * non pas la création d'un nouvel objets en plus aux côtés des autres déjà
	 * présents. Enfin concernant les messages d'alerte: la méthode se comporte
	 * exactement de la même manière que la méthode addNew() décrite précédement.
	 * 
	 */

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

	/**
	 * Méthode déclanchée par le bouton 'Update Image' de l'IHM servant à mettre à
	 * jour uniquement l'image de l'objet. Méthode identique à la méthode
	 * updateFields() décrite précédement à une exceptions prêt: la méthode copie la
	 * nouvelle image qui vient juste d'être selectionnée par l'utilisateur dans son
	 * système de fichiers et qui est par conséquent bien présente dans la variable
	 * 'imageBytes'. Si l'utilisateur ne sélectionne pas d'image, change d'avi en
	 * clickant 'Cancel', cette variable 'imageBytes' est alors null et par
	 * conséquent, la méthode n'a d'autre choix que de recopier l'image qui été déjà
	 * attribuée à l'objet, de manière à ce que cette image soit remplacée à
	 * l'identique au côtés de son objet comme si elle n'avait jamais été touchée.
	 * Il est question ici de ne pas laiser un objet dans la base avec une image
	 * attribuée ayant une valeur null. Enfin concernant les messages d'alerte: la
	 * méthode se comporte exactement de la même manière que la méthode addNew() ou
	 * updateFields() décrites précédement.
	 */

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
				}
				return;
			}
		}
	}

	/**
	 * Méthode déclanchée par le bouton 'Delete Index' de l'IHM servant à effacer un
	 * objet et ses attributs de la base de donnés qui à préalablement été
	 * sélectionné par sa position dans la base de données c'est à dire: son index.
	 * La méthode place ensuite le curseur sur l'index = 0. Concernant les messages
	 * d'alerte, un premier message de confirmation est déclanché et si l'option
	 * 'Ok' est choisie, un deuxième message apparait informant l'utilisateur du
	 * succès de l'opération en donnant à ce dernier quelques détails.
	 */

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
		}
	}

	/**
	 * Méthode déclanchée par le bouton 'Delete Row' de l'IHM servant à effacer un
	 * objet et ses attributs de la base de donnés qui à préalablement été
	 * sélectionné dans le tableau de l'IHM par un double click de l'utilisateur sur
	 * la ligne attribuée à l'objet dans le tableau. La méthode place ensuite le
	 * curseur sur l'index = 0. Concernant les messages d'alerte, un premier message
	 * de confirmation est déclanché et si l'option 'Ok' est choisie, un deuxième
	 * message apparait informant l'utilisateur du succès de l'opération en donnant
	 * à ce dernier, quelques détails.
	 */

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

	/**
	 * Méthode déclanchée par le bouton 'flêche retour au début' de l'IHM servant à
	 * revenir au début de liste dans la base de données c'est à dire à l'index = 0
	 */

	@FXML
	public void handleBack() {
		index = 0;
		populate();
	}

	/**
	 * Méthode déclanchée par le bouton 'flêche retour en arrière' de l'IHM servant
	 * à se déplacer par saut de curseur en remontant les lignes une par une de la
	 * liste dans la base de données.
	 */

	@FXML
	public void handleBackward(ActionEvent event) {

		if (index > 0) {
			index--;
		} else
			event.consume();
		populate();
	}

	/**
	 * Méthode déclanchée par le bouton 'flêche avancer en avant' de l'IHM servant à
	 * se dépplacer par saut de curseur en descendant les lignes une par une de la
	 * liste dans la base de données.
	 */

	@FXML
	public void handleForward(ActionEvent event) {

		if (index < controller.getObjetList().size() - 1) {
			index++;
		} else
			event.consume();
		populate();
	}

	/**
	 * Méthode déclanchée par le bouton 'flêche aller à la fin' de l'IHM servant à
	 * aller de la liste dans la base de données c'est à dire à l'index maximum.
	 */

	@FXML
	public void handleUpfront() {

		index = controller.getObjetList().size() - 1;
		populate();
	}

	/**
	 * Méthode fréquemment appelée par les autres méthodes consistant à rafrachir
	 * les données affichées dans l'IHM et par la même occasion rendant actifs ou
	 * inactifs certains boutons de commande de l'IHM.
	 */

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

	/**
	 * Méthode généralement appelée par la méthode populate() qui consiste à
	 * renseigner les champs et images de l'IHM avec les valeurs attribuées à
	 * l'objet qui est selectionné par son index dans la base de données. La méthode
	 * vérifie tout d'abord que la valeur des attributs 'Musée' et 'image' n'est pas
	 * null car si c'est le cas, un message d'alerte averti du problème et la
	 * méthode est alors avortée. Sinon elle se poursuit en affichant toutes les
	 * données. Concernant l'image attribuée à l'objet: la méthode crée 3 fichiers
	 * jpg différents. Le premier sert pour l'affichage d'une vignette dans
	 * l'interface, le deuxième sert si l'utilisateur click sur la vignette pour
	 * faire apparaitre une grande image zoomée et la troisième sert si
	 * l'utilisateur click sur le petit hamburder flêche pour faire apparaître
	 * l'image dans un petit tiroir glissant en venant du bord en haut à gauche de
	 * la fenêtre.
	 * 
	 * @param i
	 */

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

	/**
	 * Méthode généralement appelée par la méthode populate() qui consiste à remplir
	 * le tableau de l'IHM avec les données provenant de la base. Une opération
	 * complexe consistant à déployer les cases à cocher pour chaque ligne est
	 * opérée ici au niveau de la colonne 'sectectCol' de type booléen. Cette
	 * opération implémente une interface 'Callback' avec une méthode 'Call' une
	 * méthode 'updateItem' une classe 'ListBinding' offrant des fonctions de
	 * type'binding' permettant à l'utilisateur de cocher, décocher pour
	 * selectionner et deselectionner des objets créant ainsi une liste utilisable
	 * pour, par exemple, un export de selection sous forme de tableur Excel.
	 */

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
	}

// -------------------------------------------------------------------------------------------------------------

	/**
	 * Méthode qui retourne une case à cocher destinnée à être placée en tête de
	 * colonne permettant de selectionner ou déselectionner toutes les cases à la
	 * fois de la colonne. La méthode s'occupe en plus d'activer ou désactiver en
	 * conséquence, le bouton 'Export' et menu item 'Export' de la barre de menu.
	 * 
	 * @return une case à cocher liée aux autre servant à les cocher/décocher toute
	 */

	public CheckBox getSelectAllCheckBox() {
		if (selectAllCheckBox == null) {
			final CheckBox selectAllCheckBox = new CheckBox();
			// Ajoute un 'EventHandler' aux cases à cocher pour selectionner/deselectionner
			// tous les objets dans le tableau.
			selectAllCheckBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Attribue la valeur à tout les objets.
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

	/**
	 * Méthode appelée par les méthodes updateItem(boolean, boolean) et
	 * getSelectAllCheckBox() permettant à ces dernières d'opérer un chagement
	 * d'état sur le bouton 'Export'.
	 * 
	 * @return Le bouton 'Export' désactivé.
	 */

	public Button getExportButton() {
		if (this.exportButton == null) {
			final Button exportButton = new Button();
			exportButton.setDisable(true);
			this.exportButton = exportButton;
		}
		return this.exportButton;
	}

	/**
	 * Méthode appelée par les méthodes updateItem(boolean, boolean) et
	 * getSelectAllCheckBox() permettant à ces dernières d'opérer un chagement
	 * d'état sur le menu item 'Export'.
	 * 
	 * @return Le menu item 'Export' désactivé.
	 */

	public MenuItem getMenuItemExport() {
		if (this.menuItemExport == null) {
			final MenuItem exportMenuItem = new MenuItem();
			exportMenuItem.setDisable(true);
			this.menuItemExport = exportMenuItem;
		}
		return this.menuItemExport;
	}

	/**
	 * Méthode déclanchée par le champ 'recherche' de l'IHM quand un ou plusieurs
	 * charactères sont rentrés et que la touche du clavier est relachée. Cette
	 * méthode complète ansi la méthode autoComplete qui elle se déclanche quand la
	 * touche du clavier est pressée. Cette méthode sert à rechercher les occurences
	 * d'un charactère ou d'une suite de charactères correspondant à un mot ou une
	 * suite de chiffres. La méthode recherche ici uniquement dans les colonnes
	 * 'identification' et 'N° Inventaire'. Chaque objet ainsi trouvé dans les
	 * listes viens s'afficher dans l'extrémité haute du tableau de l'IHM laissant
	 * le reste du tableau vide.
	 * 
	 */

	@FXML
	private void handleSearch() {

		// Emballe l'ObservableList dans une FilteredList (affichant initialement toutes
		// les données).
		FilteredList<Objet> filteredData = new FilteredList<>(controller.getObjetList(), e -> true);

		searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
			filteredData.setPredicate(objet -> {
				// Si le texte filtre est vide, tous les objets sont affichés.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (objet.getIdentification().toLowerCase().contains(lowerCaseFilter)) {
					return true;// Le filtre correspond à 'identification'.
				} else if (objet.getInventaire().toLowerCase().contains(lowerCaseFilter)) {
					return true;// Le filtre correspond à 'inventaire'.
				}
				return false;// Ne correspond à rien.
			});
		});
		// Emballe la FilteredList dans une SortedList.
		SortedList<Objet> sortedData = new SortedList<>(filteredData);

		// Join le SortedList comparator avec le TableView comparator.
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

	/**
	 * Méthode déclanchée par un double click sur une ligne du tableau dans l'IHM
	 * servant à la sélectionner puis ainsi renseigner les champs et images de l'IHM
	 * avec les valeurs attribuées à l'objet dont la ligne est selectionnée dans le
	 * tableau. identique à la méthode poulateForm(int), la méthode vérifie tout
	 * d'abord que la valeur des attributs 'Musée' et 'image' n'est pas null car si
	 * c'est le cas, un mesaage d'alerte averti du problème en suggérant d'effacer
	 * cette objet. Puis certain boutons son désactivés et la méthode est alors
	 * avortée. Sinon elle se poursuit en affichant toutes les données. Concernant
	 * l'image attribuée à l'objet: la méthode crée 3 fichiers jpg différents. Le
	 * premier sert pour l'affichage d'une vignette dans l'interface, le deuxième
	 * sert si l'utilisateur click sur la vignette pour faire apparaitre une grande
	 * image zoomée et la troisième sert si l'utilisateur click sur le petit
	 * hamburder flêche pour faire apparaître l'image dans un petit tiroir glissant
	 * en venant du bord en haut à gauche de la fenêtre.
	 */

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

	/**
	 * Méthode appelée par les méthodes handleAdd(), handleUpdateFields() et
	 * handleUpdateImage() servant à détecter si un champ et vide. Si cela est le
	 * cas déclanche l'alerte d'avertissement appropriée.
	 * 
	 * @return true si aucun champ n'est vide
	 */

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

	/**
	 * Méthode appelée par la méthode handleExport() servant à redimenssionnéer une
	 * image afin qu'elle puisse figurer dans le tableur Excel.
	 * 
	 * @param originalImage
	 * @param type
	 * @param newWidth
	 * @param newHeight
	 * @return l'image redimenssionnée
	 */

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int newWidth, int newHeight) {
		// Contôle du respect des proportions, ainsi l'image n'est pas distordue
		double thumbRatio = (double) newWidth / (double) newHeight;
		int imageWidth = originalImage.getWidth(null);
		int imageHeight = originalImage.getHeight(null);
		double aspectRatio = (double) imageWidth / (double) imageHeight;

		if (thumbRatio < aspectRatio) {
			newHeight = (int) (newWidth / aspectRatio);
		} else {
			newWidth = (int) (newHeight * aspectRatio);
		}

		// Dessine l'image ajustée
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

	/**
	 * Méthode déclanchée par un double click sur la vignette image de l'IHM servant
	 * à obtenir cette image en grand sur tout l'écran. La méthode se sert du
	 * fichier jpg généré par: soit la méthode populateForm(int), soit la méthode
	 * handleRow() et affiche dans une nouvelle fenêtre la grande image.
	 */

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

	/**
	 * Méthode servant à quitter l'application.
	 */

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