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
@Table(name = "CONTACTS")
public class Contact {

	@Id
	@GeneratedValue
	@Column(name = "CONTACTID")
	private Integer contactId;
	
	@Column(name = "FIRSTNAME")
	private String firstName;
	
	@Column(name = "LASTNAME")
	private String lastName;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "PHONE")
	private String phone;
	
	@Lob
	@Column(name = "IMAGE")
	private byte[]image;

	public Contact() {
		super();
	}

	public Contact(Integer contactId, String firstName, String lastName, String email, String phone, byte[] image) {
		super();
		this.contactId = contactId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.image = image;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	public byte[] getImage() {
		return image;
	}
	
}
