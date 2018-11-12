package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee extends User {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Id @GeneratedValue
	private long id;
	@NotEmpty
	private String city;
	@NotEmpty
	private String address;
	@NotEmpty
	private String zipCode;
	@NotEmpty
	private String cellPhone;
	@NotEmpty
	private String homePhone;
	@Enumerated(EnumType.STRING)
	@NotNull
	private Gender gender;
	@Min(0)
	private double salary;
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date birthday;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(cascade = CascadeType.REFRESH) @JoinColumn(name = "company_id")
	private Company company;
	
	public Employee() {

	}

	public Employee(String firstName, String lastName, String username,  String password, String email, Role role,
			String city, String address, String zipCode, String cellPhone,
			String homePhone, Gender gender, double salary, Date birthday) {
		super(firstName, lastName, username, password, email, role);
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

	public Gender getGender() {
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Employee gender(Gender gender) {
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

	public Company getCompany() {
		return company;
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
				", company=" + Optional.ofNullable(getCompany()).map(Company::getId).orElse(null) +
				'}';
	}

}
