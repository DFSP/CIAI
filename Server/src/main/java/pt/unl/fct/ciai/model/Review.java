package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {

    @Id @GeneratedValue
    private long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    @NotEmpty
    private String summary;
    @Range(min = 0, max = 10)
    private int classification;
    @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
    private Date creationDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(cascade = CascadeType.REFRESH) @JoinColumn(name = "author_id")
    private User author;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(cascade = CascadeType.REFRESH) @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    public Review() { }

    public Review(String title, String text, String summary, int classification) {
        this.title = title;
        this.text = text;
        this.summary = summary;
        this.classification = classification;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Review id(long id) {
        setId(id);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Review title(String title) {
        setTitle(title);
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Review text(String text) {
        setText(text);
        return this;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Review summary(String summary) {
        setSummary(summary);
        return this;
    }

    public double getClassification() {
        return this.classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public Review classification(int classification) {
        setClassification(classification);
        return this;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Review creationData(Date creationDate) {
        setCreationDate(creationDate);
        return this;
    }

    public Optional<Proposal> getProposal() {
        return Optional.ofNullable(this.proposal);
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public Review proposal(Proposal proposal) {
        setProposal(proposal);
        return this;
    }

    public Optional<User> getAuthor() {
    	return Optional.ofNullable(this.author);
    }
    
    public void setAuthor(User author) {
    	this.author = author;
    }

    public Review author(User author) {
        setAuthor(author);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", summary='" + summary + '\'' +
                ", classification=" + classification +
                ", creationDate=" + creationDate +
                ", author="  + getAuthor().map(User::getUsername).orElse(null) +
                ", proposal="  + getProposal().map(Proposal::getId).orElse(null) +
                '}';
    }
}
