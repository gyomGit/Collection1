package application;

import java.util.List;

import org.objet.entity.Musee;
import org.objet.service.MuseeService;
import org.objet.service.MuseeServiceImpl;

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

public class MuseeController
{
     private MuseeService museeService = new MuseeServiceImpl();
     private ObservableList<Musee> museeList = FXCollections.observableArrayList();

     public void addMusee(Musee musee){
          museeService.addMusee(musee);
     }

     public ObservableList<Musee> getMuseeList(){
          if(!museeList.isEmpty())
               museeList.clear();
          museeList = FXCollections.observableList((List<Musee>) museeService.listMusee());
          return museeList;
     }

     public void removeObjet(Integer id)     {
          museeService.removeMusee(id);
     }

     public void updateMusee(Musee musee){
          museeService.updateMusee(musee);
     }
     
}
