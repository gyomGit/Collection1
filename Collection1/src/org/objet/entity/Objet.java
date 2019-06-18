package org.objet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *          Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *          que j'appelle 'Collection' servant à gérer les objets dans les
 *          Musées.
 * 
 *          Classe entité pour la crétion de la table objet dans la base de
 *          données de l'application 'Collection'
 */

@Entity
@Table(name = "OBJET")
public class Objet {

	private final IntegerProperty objetId = new SimpleIntegerProperty();
	private final BooleanProperty selected = new SimpleBooleanProperty();
	private final StringProperty identification = new SimpleStringProperty();
	private final StringProperty prefixeMusee = new SimpleStringProperty();
	private final StringProperty inventaire = new SimpleStringProperty();
	private final StringProperty localisation = new SimpleStringProperty();
	private byte[] image;
	private final StringProperty imageName = new SimpleStringProperty();
	private Musee musee;

	public Objet() {
		super();
	}

	public Objet(Integer objetId, Boolean selected, String identification, String prefixeMusee, String inventaire,
			String localisation, byte[] image, String imageName, Musee musee) {
		super();
		setObjetId(objetId);
		setSelected(selected);
		setIdentification(identification);
		setPrefixeMusee(prefixeMusee);
		setInventaire(inventaire);
		setLocalisation(localisation);
		setImage(image);
		setImageName(imageName);
		setMusee(musee);

	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "OBJETID", updatable = false, nullable = false)
	public Integer getObjetId() {
		return objetId.get();
	}

	public void setObjetId(Integer objetId) {
		this.objetId.set(objetId);
	}

	@ManyToOne
	@JoinColumn(name = "fk_musee")
	public Musee getMusee() {
		return musee;
	}

	public void setMusee(Musee musee) {
		this.musee = musee;
	}

	public BooleanProperty selectedProperty() {
		return selected;
	}

	@Column(name = "SELECTED", nullable = false)
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