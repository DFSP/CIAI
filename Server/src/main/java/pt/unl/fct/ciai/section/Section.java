package pt.unl.fct.ciai.section;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// TODO
@Entity
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue
    private long id;
    private String title, description, goals, material, workPlan;
    private float budget;
    
    public Section() {
    }
    
    public Section(String title, String description, String goals, String material, String workPlan, float budget) {
    	this.title = title;
    	this.description = description;
    	this.goals = goals;
    	this.material = material;
    	this.workPlan = workPlan;
    	this.budget = budget;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
