package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee extends User {

	//TODO definir quais campos são not null
	//TODO definir campos unique -> @Column(unique = true)

	@Id @GeneratedValue
	private long id;
	private String city;
	private String address;
	private String zipCode;
	private String cellPhone;
	private String homePhone;
	private char gender;
	private double salary;
	private Date birthday;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
/*	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToOne
	@JoinColumn(name = "company_id")
    private Company adminOfCompany;*/  // pode ser feito com if (getrole == admin) then is admin of company
	
	public Employee() { }

	public Employee(String firstName, String lastName, String username, String email, Role role, String password,
			String city, String address, String zipCode, String cellPhone,
			String homePhone, char gender, double salary, Date birthday) {
		super(firstName, lastName, username, email, role, password);
		this.city = city;
		this.address = address;
		this.zipCode = zipCode;
		this.cellPhone = cellPhone;
		this.homePhone = homePhone;
		this.gender = gender;
		this.salary = salary;
		this.birthday = birthday;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee id(long id) {
		setId(id);
		return this;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Employee city(String city) {
		setCity(city);
		return this;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Employee address(String address) {
		setAddress(address);
		return this;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Employee zipCode(String zipCode) {
		setZipCode(zipCode);
		return this;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public Employee cellPhone(String cellPhone) {
		setCellPhone(cellPhone);
		return this;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public Employee homePhone(String homePhone) {
		setHomePhone(homePhone);
		return this;
	}

	public char getGender() {
		return this.gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public Employee gender(char gender) {
		setGender(gender);
		return this;
	}

	public double getSalary() {
		return this.salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Employee salary(double salary) {
		setSalary(salary);
		return this;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Employee birthday(Date birthday) {
		setBirthday(birthday);
		return this;
	}

	public Optional<Company> getCompany() {
		return Optional.ofNullable(company);
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Employee company(Company company) {
		setCompany(company);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Employee employee = (Employee) o;
		return id == employee.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id);
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", city='" + city + '\'' +
				", address='" + address + '\'' +
				", zipCode='" + zipCode + '\'' +
				", cellPhone='" + cellPhone + '\'' +
				", homePhone='" + homePhone + '\'' +
				", gender=" + gender +
				", salary=" + salary +
				", birthday=" + birthday +
				", company=" + getCompany().map(Company::getId).orElse(null) +
				'}';
	}

}
