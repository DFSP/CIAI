package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Proposal {

	//TODO definir quais campos são not null
	//TODO definir campos unique -> @Column(unique = true)

	public enum ProposalState {
		PENDING_APPROVAL, APPROVED, REJECTED;
	}

	@Id @GeneratedValue
	private long id;
	private String title;
	private String description;
	@Enumerated(EnumType.STRING)
	private ProposalState state;
	@Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
	private Date creationDate;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy="proposal", cascade = CascadeType.ALL)
	private Set<Section> sections;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany(mappedBy = "proposals")
	private Set<User> staff;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany(mappedBy = "proposals")
	private Set<Employee> members;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne @JoinColumn(name="approver_id")
	private User approver;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
	private Set<Review> reviews;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
	private Set<Comment> comments;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany(mappedBy = "biddings", cascade = CascadeType.ALL)
	private Set<User> reviewBiddings;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne @JoinColumn(name="proposer_id")
	private User proposer;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany(mappedBy = "proposals")
	private Set<User> reviewers;

	public Proposal() {
		this.state = ProposalState.PENDING_APPROVAL;
	}

	public Proposal(String title, String description) {
		this();
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
	
	@JsonIgnore
	public boolean isPendingApproval() {
		return state == ProposalState.PENDING_APPROVAL;
	}
	
	@JsonIgnore
	public boolean isApproved() {
		return state == ProposalState.APPROVED;
	}
	
	@JsonIgnore
	public boolean isRejected() {
		return state == ProposalState.REJECTED;
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
		this.getSections().ifPresent((sections -> sections.remove(section)));
		return this;
	}

	public Proposal updateSection(Section section) {
		Set<Section> sections = this.getSections().orElseThrow(() ->
				new IllegalStateException(String.format("Proposal %d has no sections", getId())));
		if (!sections.remove(section)) {
			throw new IllegalArgumentException(
					String.format("Proposal %d doesn't have a section %d", getId(), section.getId()));
		}
		sections.add(section); //TODO global vs local
		return this;
	}

	public Optional<Set<User>> getStaff() {
		return Optional.ofNullable(staff);
	}

	public void setStaff(Set<User> staff) {
		this.staff = staff;
	}

	public Proposal staff(Set<User> staff) {
		setStaff(staff);
		return this;
	}

	public Proposal addStaff(User user) {
		if (this.staff == null) {
			this.staff = new HashSet<User>();
		}
		this.staff.add(user);
		return this;
	}

	public Proposal removeStaff(User user) {
		this.getStaff().ifPresent(staff -> staff.remove(user));
		return this;
	}

	public Optional<Set<Employee>> getMembers() {
		return Optional.ofNullable(this.members);
	}

	public void setMembers(Set<Employee> members) {
		this.members = members;
	}

	public Proposal member(Set<Employee> member) {
		setMembers(member);
		return this;
	}

	public Proposal addMember(Employee member) {
		if (this.members == null) {
			this.members = new HashSet<Employee>();
		}
		this.members.add(member);
		return this;
	}

	public Proposal removeMember(Employee member) {
		this.getMembers().ifPresent(members -> members.remove(member));
		return this;
	}

	public Optional<User> getApprover() {
		return Optional.ofNullable(this.approver);
	}

	public void setApprover(User approver) {
		this.approver = approver;
	}

	public Proposal approver(User approver) {
		setApprover(approver);
		return this;
	}

	public Optional<Set<Review>> getReviews() {
		return Optional.ofNullable(reviews);
	}

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
		this.getReviews().ifPresent(reviews -> reviews.remove(review));
		return this;
	}

	public Proposal updateReview(Review review) {
		Set<Review> reviews = this.getReviews().orElseThrow(() ->
				new IllegalStateException(String.format("Proposal %d has no reviews", getId())));
		if (!reviews.remove(review)) {
			throw new IllegalArgumentException(
					String.format("Proposal %d doesn't have a review %d", getId(), review.getId()));
		}
		reviews.add(review); //TODO verificar se mudança local tambem afeta o global
		return this;
	}

	public Optional<Set<Comment>> getComments() {
		return Optional.ofNullable(comments);
	}

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
		this.getComments().ifPresent(comments -> comments.remove(comment));
		return this;
	}

	public Proposal updateComment(Comment comment) {
		Set<Comment> comments = this.getComments().orElseThrow(() ->
				new IllegalStateException(String.format("Proposal %d has no comments", getId())));
		if (!comments.remove(comment)) {
			throw new IllegalArgumentException(
					String.format("Proposal %d doesn't have a comment %d", getId(), comment.getId()));
		}
		comments.add(comment); //TODO verificar se mudança local tambem afeta o global
		return this;
	}

	public Optional<Set<User>> getReviewBiddings() {
		return Optional.ofNullable(this.reviewBiddings);
	}

	public void setReviewBiddings(Set<User> reviewBiddings) {
		this.reviewBiddings = reviewBiddings;
	}

	public Proposal reviewBiddings(Set<User> biddings) {
		setReviewBiddings(biddings);
		return this;
	}

	public Proposal addReviewBidding(User user) {
		if (this.reviewBiddings == null) {
			this.reviewBiddings = new HashSet<User>();
		}
		this.reviewBiddings.add(user);
		return this;
	}

	public Proposal removeReviewBidding(User bidding) {
		this.getReviewBiddings().ifPresent(reviewBiddings -> reviewBiddings.remove(bidding));
		return this;
	}

	public Proposal updateReviewBidding(User bidding) {
		Set<User> biddings = this.getReviewBiddings().orElseThrow(() ->
				new IllegalStateException(String.format("Proposal %d has no review biddings", getId())));
		if (!biddings.remove(bidding)) {
			throw new IllegalArgumentException(
					String.format("Proposal %d doesn't have a bidding %d", getId(), bidding.getId()));
		}
		biddings.add(bidding); //TODO verificar se mudança local tambem afeta o global
		return this;
	}

	public Optional<User> getProposer() {
		return Optional.ofNullable(this.proposer);
	}

	public void setProposer(User user) {
		this.proposer = user;
	}

	public Proposal proposer(User proposer) {
		setProposer(proposer);
		return this;
	}
	
	public Optional<Set<User>> getReviewers() {
		return Optional.ofNullable(this.reviewers);
	}

	public void setReviewers(Set<User> reviewers) {
		this.reviewers = reviewers;
	}

	public Proposal reviewer(Set<User> reviewers) {
		setReviewers(reviewers);
		return this;
	}

	public Proposal addReviewer(User reviewer) {
		if (this.reviewers == null) {
			this.reviewers = new HashSet<User>();
		}
		this.reviewers.add(reviewer);
		return this;
	}

	public Proposal removeReviewer(User reviewer) {
		this.getReviewers().ifPresent(reviewers -> reviewers.remove(reviewer));
		return this;
	}

	@Override
	public int hashCode() {
		 return Objects.hash(id);
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
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Proposal{" +
				"id=" + id +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", state=" + state +
				", creationDate=" + creationDate +
				", sections=" + getSections()
				.map(p -> p.stream().map(Section::getId).collect(Collectors.toList()))
				.orElse(Collections.emptyList()) +
				", staff=" + getStaff()
				.map(p -> p.stream().map(User::getId).collect(Collectors.toList()))
				.orElse(Collections.emptyList()) +
				", members=" + getMembers()
				.map(p -> p.stream().map(Employee::getId).collect(Collectors.toList()))
				.orElse(Collections.emptyList()) +
				", approver=" + getApprover().map(User::getId)
				.orElse(null) +
				", reviews=" + getReviews()
				.map(p -> p.stream().map(Review::getId).collect(Collectors.toList()))
				.orElse(Collections.emptyList()) +
				", comments=" + getComments()
				.map(p -> p.stream().map(Comment::getId).collect(Collectors.toList()))
				.orElse(Collections.emptyList()) +
				", reviewBiddings=" + getReviewBiddings()
				.map(p -> p.stream().map(User::getId).collect(Collectors.toList()))
				.orElse(Collections.emptyList()) +
				", proposer=" + getProposer().map(User::getId)
				.orElse(null) +
				'}';
	}
}
