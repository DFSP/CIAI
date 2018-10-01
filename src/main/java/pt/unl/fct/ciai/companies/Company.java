package pt.unl.fct.ciai.companies;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import pt.unl.fct.ciai.contacts.Contact;

@Entity
public class Company {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String name;
	private String address;
	private String email;
	@ElementCollection
	private List<Contact> contacts;

	public Company id(Long id) {
		this.id = id;
		this.name = null;
		this.address = null;
		this.email = null;
		this.contacts = null;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Company name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company address(String address) {
		this.address = address;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Company email(String email) {
		this.email = email;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Company contacts(List<Contact> contacts) {
		this.contacts = contacts;
		return this;
	}

	public Company addContact(Contact contact) {
		if (this.contacts == null) {
			this.contacts = new ArrayList<Contact>();
		}
		this.contacts.add(contact);
		return this;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Company company = (Company) o;
		return Objects.equals(this.id, company.id) &&
				Objects.equals(this.name, company.name) &&
				Objects.equals(this.address, company.address) &&
				Objects.equals(this.email, company.email) &&
				Objects.equals(this.contacts, company.contacts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address, email, contacts);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Company {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    contacts: ").append(toIndentedString(contacts)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

