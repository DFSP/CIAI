package pt.unl.fct.ciai.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.unl.fct.ciai.company.Company;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Employee {
    @Id
    @GeneratedValue
    private long id;

    private long userId;
    private long companyId;
    private String city;
    private String zipCode;
    private String address;
    private String homePhone;
    private String cellPhone;

    private String gender;
    private float salary;
    private Date birthday;
    private String role;

    @JsonIgnore
    private String password;

    @ManyToOne
    private Company company;

    public Employee() {
    }

    public Company getCompany() {
        return company;
    }



    public Employee(long userId, long companyId, String city, String zipCode, String address,
                    String homePhone, String cellPhone, String gender, float salary, Date birthday, String role) {
        this.userId = userId;
        this.companyId = companyId;
        this.city = city;
        this.zipCode = zipCode;
        this.address = address;
        this.homePhone = homePhone;
        this.cellPhone = cellPhone;
        this.gender = gender;
        this.salary = salary;
        this.birthday = birthday;
        this.role = role;

    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Employee(Company company) {
        this.company = company;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
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

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}