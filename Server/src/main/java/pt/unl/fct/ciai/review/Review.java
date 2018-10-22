package pt.unl.fct.ciai.review;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String text;
    private String summary;
    private float classification;
    private Date date;

    public Review() {
    }

    public Review(String title, String text, String summary,
                  float classification, Date date) {
        this.title = title;
        this.text = text;
        this.summary = summary;
        this.classification = classification;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getClassification() {
        return classification;
    }

    public void setClassification(float classification) {
        this.classification = classification;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



}
