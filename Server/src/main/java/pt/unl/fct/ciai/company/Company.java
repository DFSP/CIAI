package pt.unl.fct.ciai.company;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import pt.unl.fct.ciai.contact.Contact;
import pt.unl.fct.ciai.employee.Employee;

@Entity
public class Company {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String city;
	private String zipCode;
	private String address;
	private String phone; //+351 919229292
	private String email;
	private String fax;

	@OneToMany(mappedBy="company", cascade = CascadeType.ALL)
	private List<Employee> employees;

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

	public Company employees(List<Employee> employees) {
		this.employees = employees;
		return this;
	}

	public Company addEmployee(Employee employee) {
		if (this.employees == null) {
			this.employees = new ArrayList<Employee>();
		}
		this.employees.add(employee);
		return this;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
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
				Objects.equals(this.employees, company.employees);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address, email, employees);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Company {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    contacts: ").append(toIndentedString(employees)).append("\n");
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

