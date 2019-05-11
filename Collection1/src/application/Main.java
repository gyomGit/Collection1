package application;
	
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.contact.entity.Contact;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

//View Layer of MVC
/* Our front-end object is implemented in JavaFX. Main.java acts as a View layer for our 
* application. The View layer translates data for visual rendering in response to the client. 
* The data is supplied primarily by the Controller, ContactController.java in this case.
*/

//Main.java

public class Main extends Application {

	String bgcolor = "-fx-background-color: #f0f0f0";
	String style = "-fx-font-weight:normal; -fx-color: #f0f0f0; -fx-font-size:11; -fx-font-family: Verdana;";

	private TableView<Contact> table = new TableView<>();
	private String buttonCaption[] = { "Add New", "Update", "Delete", "|<", "<<", ">>", ">|" };
	private String label[] = { "Contact ID", "First Name", "Lat Name", "Email", "Phone" };
	private Button button[] = new Button[7];
	private TextField textField[] = new TextField[5];
	@FXML
	private Button btnLoad;
	@FXML
	private ImageView imv;
	
	

	private ContactController controller = new ContactController();


	
	
	public static void main(String[] args) throws Exception {
		Application.launch(args);

	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Collection");
		Parent root = FXMLLoader.load(getClass().getResource("/application/Main2.fxml"));
		BorderPane border = new BorderPane();
		
	
		btnLoad = new Button("Load");
        btnLoad.setOnAction(btnLoadEventListener);
		
		 VBox rootBox = new VBox();
		 border.setCenter(rootBox);
		
		Scene scene = new Scene(root, 1200, 700);
		imv = new ImageView();
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		border.setTop(createButtonBox());
		border.setLeft(createForm());
		rootBox.getChildren().addAll(btnLoad, imv);
		border.setBottom(table);
		border.setStyle(bgcolor);
		
		
		border.setPadding(new Insets(10, 10, 10, 10));
		
		populateForm(0);
		populateTable();	
	    primaryStage.setScene(scene);
		primaryStage.show();	
	}	

    EventHandler<ActionEvent> btnLoadEventListener
    = new EventHandler<ActionEvent>(){
  
        @Override
        public void handle(ActionEvent t) {
        	
        	
            FileChooser fileChooser = new FileChooser();        

    		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter("Image files","*.png", "*.jpg", "*.gif"),
			new ExtensionFilter("All files","*.*"));
 
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
              imv.setImage(image);
      		  imv.setFitWidth(200);
      		  imv.setFitHeight(200);
      		  imv.setPreserveRatio(true);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
	
	private Pane createForm() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setHgap(20);
		grid.setStyle(style);
		grid.setVgap(2);
		for (int i = 0; i < label.length; i++) {
			grid.add(new Label(label[i] + " :"), 1, i);
			textField[i] = new TextField();
			grid.add(textField[i], 2, i);
		}
		textField[0].setEditable(false);
		textField[0].setTooltip(new Tooltip("This field is automatically generated hence not editable"));
		return grid;
	}
	
	private Pane createButtonBox() {
		int width = 100;
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(5);

		for (int i = 0; i < buttonCaption.length; i++) {
			button[i] = new Button(buttonCaption[i]);
			button[i].setStyle(style);
			button[i].setMinWidth(width);
			box.getChildren().add(button[i]);
		}
		button[0].setTooltip(new Tooltip("Add this Contact to the list as new one"));
		button[1].setTooltip(new Tooltip("Update this existing Contact from the list"));
		return box;
	}

	private void populateForm(int i) {
		if (controller.getContactList().isEmpty())
			return;
		Contact c = (Contact) controller.getContactList().get(i);
		textField[0].setText(c.getObjetId().toString());
		textField[1].setText(c.getIdentification());
		textField[2].setText(c.getPrefixeMusee());
		textField[3].setText(c.getInventaire());
		textField[4].setText(c.getLocalisation());
	}

	@SuppressWarnings("unchecked")
	private void populateTable() {
		table.getItems().clear();
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

		table.getColumns().setAll(objetIdCol, identificationCol, prefixeMuseeCol, inventaireCol, localisationCol, imageCol);
	}
}

