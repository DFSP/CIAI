package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "comments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @Id
    @GeneratedValue
    private long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "proposals_id")
    private Proposal proposal;
    private String title;
    private String text;
    @Temporal(TemporalType.TIMESTAMP) 
    @CreationTimestamp
    private Date date;
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "comments_id")
    private User author;
	
    public Comment() { }

    public Comment(String title, String text) {
        this.title = title;
        this.text = text;
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
    
    public User getAuthor() {
    	return this.author;
    }
    
    public void setAuthor(User user) {
    	this.author = user;
    }


}
