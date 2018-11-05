package pt.unl.fct.ciai.model;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "companies") //TODO apagar renomeações?
public class Company {

	@Id @GeneratedValue
	private long id;
	private String name;
	private String city;
	private String zipCode;
	private String address;
	private String phone;
	private String email;
	private String fax;
	@JsonIgnore
	@OneToMany(mappedBy="company", cascade = CascadeType.ALL)
	private Set<Employee> employees;
	@JsonIgnore
	@OneToOne(mappedBy = "adminOfCompany", cascade = CascadeType.ALL)
	private Employee myAdmin;

	public Company() { }

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
			this.employees = new LinkedHashSet<>();
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

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	
	public Employee getAdmin() {
		return this.myAdmin;
	}
	
	public void setAdmin(Employee employee) {
		this.myAdmin = employee;
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
		return Objects.equals(this.id, company.id);
	}



	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", city=" + city + ", zipCode=" + zipCode + ", address="
				+ address + ", phone=" + phone + ", email=" + email + ", fax=" + fax + ", employees=" + employees
				+ ", myAdmin=" + myAdmin + "]";
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

