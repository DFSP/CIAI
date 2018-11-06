package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "proposals")
@JsonIgnoreProperties(ignoreUnknown = true, value = {
		"sections", "staff", "members", "approver", "reviews", "comments", "biddings", "creator"})
public class Proposal {

	public enum ProposalState {
		PENDING_APPROVAL, APPROVED, REJECTED;
	}

	@Id @GeneratedValue
	private long id;
	private String title;
	private String description;
	private ProposalState state;
	@Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
	private Date creationDate;
	@OneToMany(mappedBy="proposal", cascade = CascadeType.ALL)
	private Set<Section> sections;
	@ManyToMany(mappedBy = "proposals")
	private Set<User> staff;
	@ManyToMany(mappedBy = "proposals")
	private Set<User> members;
	@ManyToOne @JoinColumn(name="approver_id")
	private User approver;
	@OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
	private Set<Review> reviews;
	@OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
	private Set<Comment> comments;
	@ManyToMany(mappedBy = "proposalBiddings", cascade = CascadeType.ALL)
	private Set<User> biddings;
	@ManyToOne @JoinColumn(name="user_id")
	private User creator;

	public Proposal() { }

	public Proposal(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Proposal id(long id) {
		setId(id);
		return this;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Proposal title(String title) {
		setTitle(title);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Proposal description(String description) {
		setDescription(description);
		return this;
	}

	public ProposalState getState() {
		return state;
	}

	public void setApproved() throws IllegalStateException {
		if (state != ProposalState.PENDING_APPROVAL)
			throw new IllegalStateException();
		state = ProposalState.APPROVED;
	}

	public Proposal approved() throws IllegalStateException {
		setApproved();
		return this;
	}

	public void setRejected() throws IllegalStateException {
		if (state != ProposalState.PENDING_APPROVAL)
			throw new IllegalStateException();
		state = ProposalState.REJECTED;
	}

	public Proposal rejected() throws IllegalStateException {
		setRejected();
		return this;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date date) {
		this.creationDate = date;
	}

	public Proposal creationDate(Date date) {
		setCreationDate(date);
		return this;
	}

	public Optional<Set<Section>> getSections() {
		return Optional.ofNullable(sections);
	}

	@JsonProperty
	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

	public Proposal sections(Set<Section> sections) {
		setSections(sections);
		return this;
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

	public Optional<Set<Review>> getReviews() {
		return Optional.ofNullable(reviews);
	}

	@JsonProperty
	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Proposal reviews(Set<Review> reviews) {
		this.reviews = reviews;
		return this;
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

	public Optional<Set<Comment>> getComments() {
		return Optional.ofNullable(comments);
	}

	@JsonProperty
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Proposal comments(Set<Comment> comments) {
		setComments(comments);
		return this;
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

	public Optional<User> getApprover() {
		return Optional.ofNullable(this.approver);
	}

	@JsonProperty
	public void setApprover(User approver) {
		this.approver = approver;
	}

	public Proposal approver(User approver) {
		setApprover(approver);
		return this;
	}

	public Optional<Set<User>> getBiddings() {
		return Optional.ofNullable(this.biddings);
	}

	@JsonProperty
	public void setBiddings(Set<User> biddings) {
		this.biddings = biddings;
	}

	public Proposal biddings(Set<User> biddings) {
		setBiddings(biddings);
		return this;
	}

	public void addBidding(User user) {
		if (this.biddings == null) {
			this.biddings = new HashSet<User>();
		}
		this.biddings.add(user);
	}

	public void removeBidding(User user) {
		if (this.biddings != null)
			this.biddings.remove(user);
	}

	public Optional<User> getCreator() {
		return Optional.ofNullable(this.creator);
	}

	@JsonProperty
	public void setCreator(User user) {
		this.creator = user;
	}

	public Proposal creator(User creator) {
		setCreator(creator);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proposal other = (Proposal) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
