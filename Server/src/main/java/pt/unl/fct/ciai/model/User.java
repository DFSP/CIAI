package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Employee.class)
})
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String role;
	@JsonIgnore
	private String password;

	@OneToMany(mappedBy="approver") // cascade?
	private Set<Proposal> proposalsToApprove;
	
	@ManyToMany
	private Set<Proposal> bidingInterests;
	
	@ManyToMany
	private Set<Proposal> userInProposals;
	
	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private Set<Proposal> myOwnProposals;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Review> authorOfReviews;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Comment> authorOfComments;

	public User(){}

	public User(String firstName, String lastName, String username, String email, String role, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.role = role;
		this.password = password;
	}

	public User addProposalToApprove(Proposal proposalToApprove) {
		if (this.proposalsToApprove == null) {
			this.proposalsToApprove = new HashSet<Proposal>();
		}
		this.proposalsToApprove.add(proposalToApprove);
		return this;
	}

	public User removeProposalToApprove(Proposal proposalToApprove) {
		this.proposalsToApprove.remove(proposalToApprove);
		return this;
	}

	public Set<Proposal> getProposalsToApprove(){
		return this.proposalsToApprove;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<Proposal> getUserInProposals() {
		return this.userInProposals;
	}
	
	public void addProposal(Proposal p) {
		if (this.userInProposals == null) {
			this.userInProposals = new HashSet<Proposal>();
		}
		this.userInProposals.add(p);
	}

}
