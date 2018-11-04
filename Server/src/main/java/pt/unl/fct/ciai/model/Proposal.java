package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "proposals")
public class Proposal {
	
    @Id @GeneratedValue
    private long id;
    private boolean isApproved;
    @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
    private Date date;

    @JsonIgnore
    @OneToMany(mappedBy="proposal")//, cascade = CascadeType.ALL)
    private Set<Section> sections;

    @JsonIgnore
    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @JsonIgnore
    @ManyToOne @JoinColumn(name="approver_id")
    private User approver;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "proposalBiddings", cascade = CascadeType.ALL)
    private Set<User> biddings;

    public Proposal() {
    }

    public Proposal(boolean isApproved) {
        this.isApproved = isApproved;
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
    
    public Set<User> getBiddings() {
    	return this.biddings;
    }
    
    public void addBidding(User user) {
        if (this.biddings == null)
            this.biddings = new HashSet<User>();
        this.biddings.add(user);
    }
    
    public void removeBidding(User user) {
    	if (this.biddings != null)
    		this.biddings.remove(user);
    }
}
