package application;

import java.util.List;

import org.objet.entity.Objet;
import org.objet.service.ObjetService;
import org.objet.service.ObjetServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

public class ObjetController
{
     private ObjetService objetService = new ObjetServiceImpl();
     private ObservableList<Objet> objetList = FXCollections.observableArrayList();

     public void addObjet(Objet objet){
          objetService.addObjet(objet);
     }

     public ObservableList<Objet> getObjetList(){
          if(!objetList.isEmpty())
               objetList.clear();
          objetList = FXCollections.observableList((List<Objet>) objetService.listObjet());
          return objetList;
     }

     public void removeObjet(Integer id)     {
          objetService.removeObjet(id);
     }

     public void updateObjet(Objet objet){
          objetService.updateObjet(objet);
     }
     
}
