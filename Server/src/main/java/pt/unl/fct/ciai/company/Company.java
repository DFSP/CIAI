package pt.unl.fct.ciai.company;

import pt.unl.fct.ciai.employee.Employee;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String city;
	private String zipCode;
	private String address;
	private String phone;
	private String email;
	private String fax;
	@OneToMany(mappedBy="company", cascade = CascadeType.ALL)
	private List<Employee> employees;

	public Company(){}

	public Company(String name, String city, String zipCode, String address, String phone, String email, String fax) {
		this.name = name;
		this.city = city;
		this.zipCode = zipCode;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.fax = fax;
	}

	public Company addEmployee(Employee employee) {
		if (this.employees == null) {
			this.employees = new ArrayList<Employee>();
		}
		this.employees.add(employee);
		return this;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
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
				Objects.equals(this.fax, company.fax) &&
				Objects.equals(this.zipCode, company.zipCode) &&
				Objects.equals(this.phone, company.phone) &&
				Objects.equals(this.employees, company.employees);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address, phone, zipCode, fax, email, employees);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Company {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    employees: ").append(toIndentedString(employees)).append("\n");
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

