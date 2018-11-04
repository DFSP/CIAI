package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends User {
	
	@Id @GeneratedValue
	private long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	private String city;
	private String address;
	private String zipCode;
	private String cellPhone;
	private String homePhone;
	private String gender;
	private double salary;
	private String birthday;
	
	@OneToOne
	@JoinColumn(name = "companies_id")
    private Company adminOfCompany;
	
	public Employee() { }

	public Employee(String firstName, String lastName, String username, String email, String role, String password,
			String city, String address, String zipCode, String cellPhone,
			String homePhone, String gender, double salary, String birthday) {
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
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
