package pt.unl.fct.ciai.contact;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pt.unl.fct.ciai.company.Company;

@Entity
public class Contact {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@JsonIgnore
	@ManyToOne
	private Company company;
    @JsonIgnore
    private String password;

	public Long getId() {
		return id;
	}

	public Contact name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Contact company(Company company) {
		this.company = company;
		return this;
	}
	
	public Company getCompany() {
		return this.company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public Contact password(String password) {
		this.password = password;
		return this;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Contact contact = (Contact) o;
		return Objects.equals(this.id, contact.id) &&
				Objects.equals(this.name, contact.name)
				&& Objects.equals(this.company, contact.company);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, company);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Contact {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    companyId: ").append(toIndentedString(company.getId())).append("\n");
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

