package pt.unl.fct.ciai.proposal;

import pt.unl.fct.ciai.comment.Comment;
import pt.unl.fct.ciai.review.Review;

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

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private Set<Comment> comments;

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

    /*public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }*/

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

    /*public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }*/
}
