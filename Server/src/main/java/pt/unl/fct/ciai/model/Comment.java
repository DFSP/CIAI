package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "proposals_id")
    private Proposal proposal;

    //private long proposalId;
    //private long commentatorId;

    private String title;
    private String text;
    private String date;
    
    @ManyToOne
    @JoinColumn(name = "comments_id")
    private User author;

    public Comment(){}

    public Comment(String title, String text, String date) {
        this.title = title;
        this.text = text;
        this.date = date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
    
    public void setAuthor(User u) {
    	this.author = u;
    }


}
