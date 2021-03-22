package com.nikchat.ppmtool.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Entity
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(updatable = false)
    private String projectSequence; // projectIdentifier + PTSequence
    
    @NotBlank(message = "Please include a project summary")
    private String summary;
    
    private String acceptanceCriteria;
    private String status;
    private Integer priority;
    private Date dueDate;
    
    //ManyToOne with Backlog
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name="backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;

    @Column(updatable = false)
    private String projectIdentifier;
    
    private Date create_At;
    private Date update_At;

    
    public ProjectTask() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectSequence() {
        return projectSequence;
    }

    public void setProjectSequence(String projectSequence) {
        this.projectSequence = projectSequence;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public Date getCreate_At() {
        return create_At;
    }

    public void setCreate_At(Date create_At) {
        this.create_At = create_At;
    }

    public Date getUpdate_At() {
        return update_At;
    }

    public void setUpdate_At(Date update_At) {
        this.update_At = update_At;
    }

    public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}

	@PrePersist
    protected void onCreate(){
        this.create_At = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.update_At = new Date();
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProjectTask [id=");
		builder.append(id);
		builder.append(", projectSequence=");
		builder.append(projectSequence);
		builder.append(", summary=");
		builder.append(summary);
		builder.append(", acceptanceCriteria=");
		builder.append(acceptanceCriteria);
		builder.append(", status=");
		builder.append(status);
		builder.append(", priority=");
		builder.append(priority);
		builder.append(", dueDate=");
		builder.append(dueDate);
		builder.append(", projectIdentifier=");
		builder.append(projectIdentifier);
		builder.append(", create_At=");
		builder.append(create_At);
		builder.append(", update_At=");
		builder.append(update_At);
		builder.append("]");
		return builder.toString();
	}

}

