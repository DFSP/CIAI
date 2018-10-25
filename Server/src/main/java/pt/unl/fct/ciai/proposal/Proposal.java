package pt.unl.fct.ciai.proposal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Proposal {
    @Id
    @GeneratedValue
    private long id;
    private boolean isApproved;
    private String date;

    public Proposal() {
    }

    public Proposal(boolean isApproved, String date) {
        this.isApproved = isApproved;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
