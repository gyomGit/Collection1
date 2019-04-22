package application;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.contact.entity.Contact;
import org.contact.service.ContactService;
import org.contact.service.ContactServiceImpl;

// Controller Layer: Controller
/* The Controller translates requests coming from the View layer 
* into outgoing responses. In order to do this, ContactController.
* java takes request data and passes it to the service layer. 
* The service layer then returns data that the Controller injects 
* into a View for rendering. This view may be HTML for a standard 
* Web request or JSON (JavaScript Object Notation) for a RESTful 
* API request. In our case, it is a JavaFX UI.
*/

// ContactController.java

public class ContactController
{
     private ContactService contactService = new ContactServiceImpl();
     private ObservableList<Contact> contactList = FXCollections.observableArrayList();

     public void addContact(Contact contact){
          contactService.addContact(contact);
     }

     public ObservableList<Contact> getContactList(){
          if(!contactList.isEmpty())
               contactList.clear();
          contactList = FXCollections.observableList((List<Contact>) contactService.listContact());
          return contactList;
     }

     public void removeContact(Integer id)     {
          contactService.removeContact(id);
     }

     public void updateContact(Contact contact){
          contactService.updateContact(contact);
     }
     
}
