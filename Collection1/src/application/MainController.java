package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.contact.entity.Contact;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
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

// --------------------------------------	

	@FXML
	private TextField objetIdField;
	
	@FXML
	private CheckBoxTableCell<?, ?> checkBoxTableCell;

	@FXML
	private TextField identificationField;

	@FXML
	private TextField prefixeMuseeField;

	@FXML
	private TextField inventaireField;

	@FXML
	private TextField localisationField;

	// --------------------------------------

	@FXML
	private TableView<Contact> table = new TableView<>();
	
	@FXML
	TableColumn<Contact, Integer> objetIdCol;
	
	@FXML
	TableColumn<Contact, CheckBox> selectCol;
	
	@FXML
	TableColumn<Contact, String> identificationCol;
	
	@FXML
	TableColumn<Contact, String> prefixeMuseeCol;
	
	@FXML
	TableColumn<Contact, String> inventaireCol;
	
	@FXML
	TableColumn<Contact, String> localisationCol;
	
	@FXML
	TableColumn<Contact, byte[]> imageCol;
	

	// --------------------------------------

	private ContactController controller = new ContactController();

	@FXML
	private ImageView imv;

	@FXML
	private static int index;

	private byte[] imageBytes;

	ObservableList<String> prefixeList = FXCollections.observableArrayList("Préfixe Musée", "ADN", "CEC", "CG04",
			"EXPO", "FOR", "MAR", "MDLV", "MGD", "MMHV", "MMV", "MPGV", "MST", "SIST", "SLG", "UBAY");

	@FXML
	private ChoiceBox<String> prefixeBox;

	private TableColumn<Contact, Integer> active;

	@FXML
	private void initialize() {
		prefixeBox.setValue("Préfixe Musée");
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

	@FXML
	private void handleAdd(ActionEvent event) {

		Contact c = new Contact();

		c.setObjetId(111);
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		c.setInventaire(inventaireField.getText());
		c.setLocalisation(localisationField.getText());
		c.setImage(imageBytes);

		controller.addContact(c);
		populate();
	}

	@FXML
	public void handleUpdate(ActionEvent event) {

//		Contact c = new Contact(Integer.parseInt(contactIdField.getText()), firstNameField.getText(), lastNameField.getText(), emailField.getText(),
//				phoneField.getText(), imv.getImage());

		Contact c = new Contact();

		c.setObjetId(Integer.parseInt(objetIdField.getText()));
		c.setIdentification(identificationField.getText());
		c.setPrefixeMusee(prefixeBox.getValue());
		c.setInventaire(inventaireField.getText());
		c.setLocalisation(localisationField.getText());
		c.setImage(imageBytes);

		controller.updateContact(c);
		System.out.println("update button clicked");
		populate();
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
		table.setItems(controller.getContactList());
		TableColumn<Contact, Integer> objetIdCol = new TableColumn<Contact, Integer>("Objet ID");
		objetIdCol.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("objetId"));
		
		TableColumn<Contact, Boolean> selectCol = new TableColumn<Contact, Boolean>("Select");
//		selectCol.setMinWidth(50);
//		selectCol.setGraphic(getSelectAllCheckBox());
		selectCol.setCellValueFactory(new PropertyValueFactory<Contact, Boolean>("select"));
		
		selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
		
// -----------------------------------------------------------------------------------		
		
//		selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, 
//				ObservableValue<Boolean>>() {
//
//				        @Override
//				        public ObservableValue<Boolean> call(Integer param) {
//				            System.out.println("Cours "+ controller.getContactList().get(param).getIdentification()+" changed value to " +controller.getContactList().get(param).isSelected());
//				            for (Integer i = 0; i < controller.getContactList().size(); i++) {
//				                if (i != param && controller.getContactList().get(param).isSelected()) {
//				                	controller.getContactList().get(i).setSelect(false);
//				                } // if
//				            } //  for
//				            return controller.getContactList().get(param).selectedProperty();
//				        }
//				    }));
		
// -------------------------------------------------------------------------------		
		
		
//		selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
//			
//		    @Override
//		    public ObservableValue<Boolean> call(Integer param) {
//		        System.out.println("Cours "+item.get(param).getCours()+" changed value to " +items.get(param).isChecked());
//		        return items.get(param).checkedProperty();
//		    }
//		}));

// ---------------------------------------------------------------------------		
		
//		selectCol.setCellFactory(new Callback  <TableColumn<Contact, Boolean>, TableCell <Contact, Boolean>>() {
//		public TableCell<Contact, Boolean> call(TableColumn<Contact, Boolean> p) {
//			final TableCell<Contact, Boolean> cell = new TableCell<Contact, Boolean>() {
//				@Override
//				public void updateItem(final Boolean item, boolean empty) {
//					if (item == null)
//						return;
//					super.updateItem(item, empty);
//					if (!isEmpty()) {
//						final Contact c = getTableView().getItems().get(getIndex());
//						CheckBox checkBox = new CheckBox();
//						checkBox.selectedProperty().bindBidirectional(c.selectedProperty());
//						// checkBox.setOnAction(event);
//						setGraphic(checkBox);
//					}
//				}
//			};
//			cell.setAlignment(Pos.CENTER);
//			return cell;
//		}
//	});

		
// -----------------------------------------------------------------
		
		
//		Callback<TableColumn<Contact, Boolean>, TableCell<Contact, Boolean>> booleanCellFactory = 
//	            new Callback<TableColumn<Contact, Boolean>, TableCell<Contact, Boolean>>() {
//	            @Override
//	                public TableCell<Contact, Boolean> call(TableColumn<Contact, Boolean> p) {
//	                    return new TableCell<Contact, Boolean>();
//	            }
//	        };
//	        active.setCellValueFactory(new PropertyValueFactory<Contact,Boolean>("select"));
//	        active.setCellFactory(booleanCellFactory);
//
//	class BooleanCell extends TableCell<Contact, Boolean> {
//	        private CheckBox checkBox;
//	        public BooleanCell() {
//	            checkBox = new CheckBox();
//	            checkBox.setDisable(true);
//	            checkBox.selectedProperty().addListener(new ChangeListener<Boolean> () {
//	                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//	                    if(isEditing())
//	                        commitEdit(newValue == null ? false : newValue);
//	                }
//	            });
//	            this.setGraphic(checkBox);
//	            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//	            this.setEditable(true);
//	        }
//	        @Override
//	        public void startEdit() {
//	            super.startEdit();
//	            if (isEmpty()) {
//	                return;
//	            }
//	            checkBox.setDisable(false);
//	            checkBox.requestFocus();
//	        }
//	        @Override
//	        public void cancelEdit() {
//	            super.cancelEdit();
//	            checkBox.setDisable(true);
//	        }
//	        public void commitEdit(Boolean value) {
//	            super.commitEdit(value);
//	            checkBox.setDisable(true);
//	        }
//	        @Override
//	        public void updateItem(Boolean item, boolean empty) {
//	            super.updateItem(item, empty);
//	            if (!isEmpty()) {
//	                checkBox.setSelected(item);
//	            }
//	        }
//	    }
	
		
// -----------------------------------------------------------------		
		
	
		
		
			
		
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

		table.getColumns().setAll(objetIdCol, selectCol, identificationCol, prefixeMuseeCol, inventaireCol, localisationCol,
				imageCol);
	}

}
