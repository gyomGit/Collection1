package application;

import java.io.File;

import org.contact.entity.Contact;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
	private TextField contactIdField;
	
	@FXML 
	private TextField firstNameField;
	
	@FXML 
	private TextField lastNameField;
	
	@FXML 
	private TextField emailField;
	
	@FXML 
	private TextField phoneField;
	
	// --------------------------------------	
	
	@FXML
	private TableView<Contact> table = new TableView<>();
	
	// --------------------------------------	
	
	
	
	private ContactController controller = new ContactController();	

	@FXML
	private ImageView imv;
	
	@FXML
	private static int index;

	@FXML
	private void handle(ActionEvent t) {

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
		imv.setFitWidth(200);
		imv.setFitHeight(200);
		imv.setPreserveRatio(true);
	}
	
	@FXML
	private void handleAdd(ActionEvent event) {

			Contact c = new Contact(111, firstNameField.getText(), lastNameField.getText(), emailField.getText(),
					phoneField.getText());
			controller.addContact(c);
			populate();
	}

	@FXML
	public void handleUpdate(ActionEvent event) {

		Contact c = new Contact(Integer.parseInt(contactIdField.getText()), firstNameField.getText(), lastNameField.getText(), emailField.getText(),
				phoneField.getText());
		controller.updateContact(c);
		System.out.println("update button clicked");
		populate();
	}
	
	@FXML
	public void handleDelete(ActionEvent event) {

		Contact c = (Contact) controller.getContactList().get(index);
		controller.removeContact(c.getContactId());

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
//			System.out.println("update button clicked");
//		} else if (event.getSource().equals(button[2])) {
//			Contact c = (Contact) controller.getContactList().get(index);
//			controller.removeContact(c.getContactId());
//		} else if (event.getSource().equals(button[4])) {
//			if (index > 0) {
//				index--;
//			} else
//				event.consume();
//		} else if (event.getSource().equals(button[3])) {
//			index = 0;
//		} else if (event.getSource().equals(button[5])) {
//			if (index < controller.getContactList().size() - 1) {
//				index++;
//			} else
//				event.consume();
//		} else if (event.getSource().equals(button[6])) {
//			index = controller.getContactList().size() - 1;
//		}
		
	private void populate() {		
		populateForm(index);
		populateTable();
	

}

private void populateForm(int i) {
	if (controller.getContactList().isEmpty())
		return;
	Contact c = (Contact) controller.getContactList().get(i);
	contactIdField.setText(c.getContactId().toString());
	firstNameField.setText(c.getFirstName());
	lastNameField.setText(c.getLastName());
	emailField.setText(c.getEmail());
	phoneField.setText(c.getPhone());
}


private void populateTable() {
	table.getItems().clear();
	table.setItems(controller.getContactList());
	TableColumn<Contact, Integer> contactIdCol = new TableColumn<Contact, Integer>("Contact ID");
	contactIdCol.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("contactId"));
	TableColumn<Contact, String> firstNameCol = new TableColumn<Contact, String>("First Name");
	firstNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
	TableColumn<Contact, String> lastNameCol = new TableColumn<Contact, String>("Last Name");
	lastNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
	TableColumn<Contact, String> emailCol = new TableColumn<Contact, String>("Email");
	emailCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
	TableColumn<Contact, String> phoneCol = new TableColumn<Contact, String>("Phone");
	phoneCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
	table.getColumns().setAll(contactIdCol, firstNameCol, lastNameCol, emailCol, phoneCol);
}
	
}
