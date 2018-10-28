package pt.unl.fct.ciai.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import pt.unl.fct.ciai.company.Company;
import pt.unl.fct.ciai.user.User;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private User user;
    // ID from user to link Employee to the correspondent UserTable
    private long user_id;

    private String city;
    private String address;
    private String zipCode;
    private String cellPhone;
    private String homePhone;
    private String gender;
    private double salary;
    private String birthday;


    public Employee(){}



    public Employee(String city, String address, String zipCode, String cellPhone,
                    String homePhone, String gender, double salary, String birthday,
                    long user_id) {
        this.city = city;
        this.address = address;
        this.zipCode = zipCode;
        this.cellPhone = cellPhone;
        this.homePhone = homePhone;
        this.gender = gender;
        this.salary = salary;
        this.birthday = birthday;
        this.user_id = user_id;
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

    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
