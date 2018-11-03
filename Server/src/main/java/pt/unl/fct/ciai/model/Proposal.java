package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "proposals")
public class Proposal {
    @Id
    @GeneratedValue
    private long id;
    private boolean isApproved;
    private String date;

    @OneToMany(mappedBy="proposal")//, cascade = CascadeType.ALL)
    private Set<Section> sections;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @JsonIgnore
    @ManyToOne @JoinColumn(name="approver_id")
    private User approver;
    
    @ManyToMany(mappedBy = "bidingInterests", cascade = CascadeType.ALL)
    private Set<User> usersForBiding;

    public Proposal() {
    }

    public Proposal(boolean isApproved, String date) {
        this.isApproved = isApproved;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public Proposal addSection(Section section) {
        if (this.sections == null) {
            this.sections = new HashSet<Section>();
        }
        this.sections.add(section);
        return this;
    }

    public Proposal removeSection(Section section) {
        this.sections.remove(section);
        return this;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Proposal addReview(Review review) {
        if (this.reviews == null) {
            this.reviews = new HashSet<Review>();
        }
        this.reviews.add(review);
        return this;
    }

    public Proposal removeReview(Review review) {
        this.reviews.remove(review);
        return this;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Proposal addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = new HashSet<Comment>();
        }
        this.comments.add(comment);
        return this;
    }

    public Proposal removeComment(Comment comment) {
        this.comments.remove(comment);
        return this;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }
    
    public Set<User> getUsersForBiding() {
    	return this.usersForBiding;
    }
    
    public void addUserForBiding(User user) {
        if (this.usersForBiding == null)
            this.usersForBiding = new HashSet<User>();
        this.usersForBiding.add(user);
    }
    
    public void removeUserForBiding(User user) {
    	if (this.usersForBiding != null)
    		this.usersForBiding.remove(user);
    }
}
