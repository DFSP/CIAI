package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    public enum Gender {
        MALE, FEMALE
    }

    public enum Role {
        ROLE_SYS_ADMIN, ROLE_COMPANY_ADMIN, ROLE_PROPOSAL_APPROVER
    }

    @Id @GeneratedValue
    private long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @Column(unique = true)
    private String username;
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotEmpty
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(cascade = CascadeType.REFRESH)
    private Set<Proposal> proposals;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(cascade = CascadeType.REFRESH)
    private Set<Proposal> biddings;

    public User() {
    }

    public User(String firstName, String lastName, String username,
                String password, String email, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User role(Role role) {
        setRole(role);
        return this;
    }

    public Optional<Set<Proposal>> getProposals() {
        return Optional.ofNullable(this.proposals);
    }

    public void setProposals(Set<Proposal> proposals) {
        this.proposals = proposals;
    }

    public User proposals(Set<Proposal> proposals) {
        setProposals(proposals);
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
                ", biddings=" + getBiddings()
                .map(p -> p.stream().map(Proposal::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList()) +
                '}';
    }
}
