package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "proposals_id")
    private Proposal proposal;

    //private long proposalId;
    //private long reviewerId;

    private String title;
    private String text;
    private String summary;
    private double classification;
    @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
    private Date date;

    public Review(){ }

    public Review(String title, String text, String summary, double classification, String date) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getClassification() {
        return classification;
    }

    public void setClassification(double classification) {
        this.classification = classification;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
}
