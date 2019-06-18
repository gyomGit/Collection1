package org.objet.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javafx.beans.property.IntegerProperty;
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
 *          Classe entité pour la crétion de la table Musée dans la base de
 *          données de l'application 'Collection'
 */

@Entity
@Table(name = "musee")
public class Musee {

	private final IntegerProperty museeId = new SimpleIntegerProperty();
	private final StringProperty nomMusee = new SimpleStringProperty();
	private final StringProperty prefixeMusee = new SimpleStringProperty();
	private final StringProperty adressMusee = new SimpleStringProperty();
	private final StringProperty emailMusee = new SimpleStringProperty();
	private final StringProperty telMusee = new SimpleStringProperty();
	private List<Objet> objets = new ArrayList<Objet>();

	public Musee() {
		super();
	}

	public Musee(Integer museeId, String nomMusee, String prefixeMusee, String adressMusee, String emailMusee,
			String telMusee, List<Objet> objets) {
		super();
		setMuseeId(museeId);
		setNomMusee(nomMusee);
		setPrefixeMusee(prefixeMusee);
		setAdressMusee(adressMusee);
		setEmailMusee(emailMusee);
		setTelMusee(telMusee);
		setObjets(objets);

	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "MUSEEID", updatable = false, nullable = false)
	public Integer getMuseeId() {
		return museeId.get();
	}

	public void setMuseeId(Integer museeId) {
		this.museeId.set(museeId);
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "musee")
	@Column(name = "MUSEE_ID", nullable = false)
	public List<Objet> getObjets() {
		return this.objets;
	}

	public void setObjets(List<Objet> objets) {
		this.objets = objets;
	}

	@Column(name = "NOMMUSEE")
	public String getNomMusee() {
		return nomMusee.get();
	}

	public void setNomMusee(String nomMusee) {
		this.nomMusee.set(nomMusee);
	}

	@Column(name = "PREFIXEMUSEE")
	public String getPrefixeMusee() {
		return prefixeMusee.get();
	}

	public void setPrefixeMusee(String prefixeMusee) {
		this.prefixeMusee.set(prefixeMusee);
	}

	@Column(name = "ADRESSMUSEE")
	public String getAdressMusee() {
		return adressMusee.get();
	}

	public void setAdressMusee(String adressMusee) {
		this.adressMusee.set(adressMusee);
	}

	@Column(name = "EMAILMUSEE")
	public String getEmailMusee() {
		return emailMusee.get();
	}

	public void setEmailMusee(String emailMusee) {
		this.emailMusee.set(emailMusee);
	}

	@Column(name = "TELMUSEE")
	public String getTelMusee() {
		return telMusee.get();
	}

	public void setTelMusee(String telMusee) {
		this.telMusee.set(telMusee);
	}

}