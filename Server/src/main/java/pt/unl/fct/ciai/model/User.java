package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Employee.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany
    private Set<Proposal> proposals;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "approver")
    private Set<Proposal> approveProposals;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany
    private Set<Proposal> biddings;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "proposer", cascade = CascadeType.REMOVE) //TODO remover utilizador remove tambem as suas propostas?
    private Set<Proposal> myProposals;
/*	@JsonIgnore
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Review> authorOfReviews;
	@JsonIgnore
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Comment> authorOfComments;*/

    public User() {
    }

    public User(String firstName, String lastName, String username,
                String email, String role, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User id(long id) {
        setId(id);
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User lastName(String lastName) {
        setLastName(lastName);
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User username(String username) {
        setUsername(username);
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User email(String email) {
        setEmail(email);
        return this;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User role(String role) {
        setRole(role);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User password(String password) {
        setPassword(password);
        return this;
    }

    public Optional<Set<Proposal>> getProposals() {
        return Optional.ofNullable(this.proposals);
    }

    public void setProposal(Set<Proposal> proposals) {
        this.proposals = proposals;
    }

    public User proposals(Set<Proposal> proposals) {
        setProposal(proposals);
        return this;
    }

    public User addProposal(Proposal proposal) {
        if (this.proposals == null) {
            this.proposals = new HashSet<Proposal>();
        }
        this.proposals.add(proposal);
        return this;
    }

    public User removeProposal(Proposal proposal) {
        if (this.proposals != null) {
            this.proposals.remove(proposal);
        }
        return this;
    }

    public Optional<Set<Proposal>> getApproveProposals() {
        return Optional.ofNullable(this.approveProposals);
    }

    public void setApproveProposals(Set<Proposal> proposals) {
        this.approveProposals = proposals;
    }

    public User approveProposals(Set<Proposal> proposals) {
        setApproveProposals(proposals);
        return this;
    }

    public User addApproveProposal(Proposal proposal) {
        if (this.approveProposals == null) {
            this.approveProposals = new HashSet<Proposal>();
        }
        this.approveProposals.add(proposal);
        return this;
    }

    public User removeApproveProposal(Proposal proposal) {
        if (this.approveProposals != null) {
            this.approveProposals.remove(proposal);
        }
        return this;
    }

    public Optional<Set<Proposal>> getBiddings() {
        return Optional.ofNullable(this.biddings);
    }

    public void setBiddings(Set<Proposal> proposals) {
        this.biddings = proposals;
    }

    public User biddings(Set<Proposal> proposals) {
        setBiddings(proposals);
        return this;
    }

    public User addBidding(Proposal proposal) {
        if (this.biddings == null) {
            this.biddings = new HashSet<Proposal>();
        }
        this.biddings.add(proposal);
        return this;
    }

    public User removeBidding(Proposal proposal) {
        if (this.biddings != null) {
            this.biddings.remove(proposal);
        }
        return this;
    }

    public Optional<Set<Proposal>> getMyProposals() {
        return Optional.ofNullable(this.myProposals);
    }

    public void setMyProposals(Set<Proposal> proposals) {
        this.myProposals = proposals;
    }

    public User myProposals(Set<Proposal> proposals) {
        setMyProposals(proposals);
        return this;
    }

    public User addMyProposal(Proposal proposal) {
        if (this.myProposals == null) {
            this.myProposals = new HashSet<Proposal>();
        }
        this.myProposals.add(proposal);
        return this;
    }

    public User removeMyProposal(Proposal proposal) {
        if (this.myProposals != null) {
            this.myProposals.remove(proposal);
        }
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
        User other = (User) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", proposals=" + getProposals()
                .map(p -> p.stream().map(Proposal::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList()) +
                ", approveProposals=" + getApproveProposals()
                .map(p -> p.stream().map(Proposal::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList()) +
                ", biddings=" + getBiddings()
                .map(p -> p.stream().map(Proposal::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList()) +
                ", myProposals=" + getMyProposals()
                .map(p -> p.stream().map(Proposal::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList()) +
                '}';
    }
}
