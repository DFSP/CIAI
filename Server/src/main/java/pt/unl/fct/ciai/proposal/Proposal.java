package pt.unl.fct.ciai.proposal;

import pt.unl.fct.ciai.employee.Employee;
import pt.unl.fct.ciai.review.Review;
import pt.unl.fct.ciai.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Entity
public class Proposal {
    @Id
    @GeneratedValue
    private long id;
    private boolean isApproved;
    private Date date;

    //private List<Document> documents;
    private List<Review> reviews;
    private List<User> users;
    private List<Employee> employees;

    public Proposal() {
    }

    public Proposal(boolean isApproved, Date date,
                    List<User> users, List<Employee> employees) {
        this.isApproved = isApproved;
        this.date = date;
        this.users = users;
        this.employees = employees;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
