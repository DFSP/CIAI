package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String text;
    @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
    private Date creationDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne @JoinColumn(name = "author_id")
    private User author;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne @JoinColumn(name = "proposal_id")
    private Proposal proposal;
	
    public Comment() { }

    public Comment(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Comment id(long id) {
        setId(id);
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Comment title(String title) {
        setTitle(title);
        return this;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Comment text(String text) {
        setText(text);
        return this;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Comment creationDate(Date creationDate) {
        setCreationDate(creationDate);
        return this;
    }

    public Optional<User> getAuthor() {
        return Optional.ofNullable(this.author);
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Comment author(User author) {
        setAuthor(author);
        return this;
    }

    public Optional<Proposal> getProposal() {
        return Optional.ofNullable(this.proposal);
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public Comment proposal(Proposal proposal) {
        setProposal(proposal);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", creationDate=" + creationDate +
                ", author="  + getAuthor().map(User::getId).orElse(null) +
                ", proposal="  + getProposal().map(Proposal::getId).orElse(null) +
                '}';
    }

}
