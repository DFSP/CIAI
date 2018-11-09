package pt.unl.fct.ciai.model;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
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
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy="company", cascade = CascadeType.ALL)
	private Set<Employee> employees;
/*	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToOne(mappedBy = "adminOfCompany", cascade = CascadeType.ALL)
	private Employee myAdmin;*/ //TODO substituir pela query
// pode ser feito com query: select employee where employee.role == admin

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

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Company id(long id) {
		setId(id);
		return this;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company name(String name) {
		setName(name);
		return this;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Company city(String city) {
		setCity(city);
		return this;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Company zipCode(String zipCode) {
		setZipCode(zipCode);
		return this;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Company address(String address) {
		setAddress(address);
		return this;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Company phone(String phone) {
		setPhone(phone);
		return this;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Company email(String email) {
		setEmail(email);
		return this;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Company fax(String fax) {
		setFax(fax);
		return this;
	}

	public Optional<Set<Employee>> getEmployees() {
		return Optional.ofNullable(employees);
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Company addEmployee(Employee employee) {
		if (this.employees == null) {
			this.employees = new LinkedHashSet<>();
		}
		this.employees.add(employee);
		return this;
	}

	public Company removeEmployee(Employee employee) {
		getEmployees().ifPresent(employees -> employees.remove(employee));
		return this;
	}

	public Company updateEmployee(Employee employee) {
		Set<Employee> employees = this.getEmployees().orElseThrow(() ->
				new IllegalStateException(String.format("Company %d has no employees", getId())));
		if (!employees.remove(employee)) {
			throw new IllegalArgumentException(
					String.format("Company %d doesn't have an employee %d", getId(), employee.getId()));
		}
		employees.add(employee); //TODO verificar se mudança local tambem afeta o global
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Company company = (Company) o;
		return id == company.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Company{" +
				"id=" + id +
				", name='" + name + '\'' +
				", city='" + city + '\'' +
				", zipCode='" + zipCode + '\'' +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", fax='" + fax + '\'' +
				", employees=" + getEmployees()
				.map(p -> p.stream().map(Employee::getId).collect(Collectors.toList()))
				.orElse(Collections.emptyList()) +
				'}';
	}
}

