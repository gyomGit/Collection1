package org.contact.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

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

	@Id
	@GeneratedValue
	@Column(name = "OBJETID")
	private Integer objetId;

	@Column(name = "IDENTIFICATION")
	private String identification;

	@Column(name = "PREFIXEMUSEE")
	private String prefixeMusee;

	@Column(name = "INVENTAIRE")
	private String inventaire;

	@Column(name = "LOCALISATION")
	private String localisation;

	@Lob
	@Column(name = "IMAGE")
	private byte[] image;

	public Contact() {
		super();
	}

	public Contact(Integer objetId, String identification, String prefixeMusee, String inventaire, String localisation,
			byte[] image) {
		super();
		this.objetId = objetId;
		this.identification = identification;
		this.prefixeMusee = prefixeMusee;
		this.inventaire = inventaire;
		this.localisation = localisation;
		this.image = image;
	}

	public Integer getObjetId() {
		return objetId;
	}

	public void setObjetId(Integer objetId) {
		this.objetId = objetId;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getPrefixeMusee() {
		return prefixeMusee;
	}

	public void setPrefixeMusee(String prefixeMusee) {
		this.prefixeMusee = prefixeMusee;
	}

	public String getInventaire() {
		return inventaire;
	}

	public void setInventaire(String inventaire) {
		this.inventaire = inventaire;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

}
