package pt.unl.fct.ciai.comment;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pt.unl.fct.ciai.proposal.Proposal;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    //private long proposalId;
    //private long commentatorId;

    private String title;
    private String text;
    private String date;

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


}
