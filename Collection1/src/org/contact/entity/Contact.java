package org.contact.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Model Layer: Entity Class
/* The model layer implements the domain logic of 
 * the application with strong separation from the way 
 * that the user requests and sees results. 
 * Contact.java is our annotation-based entity class 
 * for creating the Contacts database table.
 */

@Entity
@Table(name = "OBJET")
public class Contact {
	
	
    private final IntegerProperty objetId = new SimpleIntegerProperty() ; 
    private final BooleanProperty selected= new SimpleBooleanProperty() ;
    private final StringProperty identification = new SimpleStringProperty() ;
    private final StringProperty prefixeMusee = new SimpleStringProperty() ;
    private final StringProperty inventaire = new SimpleStringProperty() ;
    private final StringProperty localisation = new SimpleStringProperty() ;
    private byte[] image;
    private final StringProperty imageName = new SimpleStringProperty() ;


	

	public Contact() {
		super();
	}

	public Contact(Integer objetId,Boolean selected, String identification, String prefixeMusee, String inventaire,
			String localisation, byte[] image, String imageName) {
		super();
		setObjetId(objetId);
		setSelected(selected);
		setIdentification(identification);
		setPrefixeMusee(prefixeMusee);
		setInventaire(inventaire);
		setLocalisation(localisation);
		setImage(image);
		setImageName(imageName);
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "OBJETID")
	public Integer getObjetId() {
		return objetId.get();
	}

	public void setObjetId(Integer objetId) {
		this.objetId.set(objetId);
	}
	
	public BooleanProperty selectedProperty() {
		return selected;
	}

	@Column(name = "SELECTED", columnDefinition = "tinyint", nullable = false)	
	public Boolean getSelected() {
		return selected.get();
	}
	
	public void setSelected(Boolean selected) {
		this.selected.set(selected);
	}

	@Column(name = "IDENTIFICATION")
	public String getIdentification() {
		return identification.get();
	}

	public void setIdentification(String identification) {
		this.identification.set(identification);
	}

	@Column(name = "PREFIXEMUSEE")
	public String getPrefixeMusee() {
		return prefixeMusee.get();
	}

	public void setPrefixeMusee(String prefixeMusee) {
		this.prefixeMusee.set(prefixeMusee);
	}

	@Column(name = "INVENTAIRE")
	public String getInventaire() {
		return inventaire.get();
	}

	public void setInventaire(String inventaire) {
		this.inventaire.set(inventaire);
	}

	@Column(name = "LOCALISATION")
	public String getLocalisation() {
		return localisation.get();
	}

	public void setLocalisation(String localisation) {
		this.localisation.set(localisation);
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}

	@Lob
	@Column(name = "IMAGE")
	public byte[] getImage() {
		return image;
	}
	
	@Column(name = "IMAGENAME")
	public String getImageName() {
		return imageName.get();
	}

	public void setImageName(String imageName) {
		this.imageName.set(imageName);
	}

}