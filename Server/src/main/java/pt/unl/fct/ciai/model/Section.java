package pt.unl.fct.ciai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Section {
	
    @Id @GeneratedValue
    private long id;
    private String goals; //TODO String[] se nao complicar muito as queries
    private String material; //TODO String[] se nao complicar muito as queries
    private String workPlan;
    private float budget;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    public Section() { }
    
    public Section(String goals, String material, String workPlan, float budget) {
    	this.goals = goals;
    	this.material = material;
    	this.workPlan = workPlan;
    	this.budget = budget;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Section id(long id) {
        setId(id);
        return this;
    }

    public String getGoals() {
        return this.goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public Section goals(String goals) {
        setGoals(goals);
        return this;
    }

    public String getMaterial() {
        return this.material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Section material(String material) {
        setMaterial(material);
        return this;
    }

    public String getWorkPlan() {
        return this.workPlan;
    }

    public void setWorkPlan(String workPlan) {
        this.workPlan = workPlan;
    }

    public Section workPlan(String workPlan) {
        setWorkPlan(workPlan);
        return this;
    }

    public float getBudget() {
        return this.budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public Section budget(float budget) {
        setBudget(budget);
        return this;
    }

    public Optional<Proposal> getProposal() {
        return Optional.ofNullable(this.proposal);
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public Section proposal(Proposal proposal) {
        setProposal(proposal);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id == section.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", goals='" + goals + '\'' +
                ", material='" + material + '\'' +
                ", workPlan='" + workPlan + '\'' +
                ", budget=" + budget +
                ", proposal=" + getProposal().map(Proposal::getId).orElse(null) +
                '}';
    }
}
