package org.fatmansoft.teach.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(	name = "activity",
        uniqueConstraints = {
        })

public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;
    @ManyToOne
    @JoinColumn(name="student_id")
    @JsonIgnore
    private Student student;
    @Size(max = 50)
    private String activityName;//activity名称


    @Size(max = 50)
    private String time ;//活动时长

    @Size(max = 20)
    private String  activityType;//activity类型

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityGrade() {
        return activityGrade;
    }

    public void setActivityGrade(String activityGrade) {
        this.activityGrade = activityGrade;
    }

    public String getTimeOfActivity() {
        return timeOfActivity;
    }

    public void setTimeOfActivity(String timeOfActivity) {
        this.timeOfActivity = timeOfActivity;
    }

    @Size(max = 20)
    private String  activityGrade;//activity等级

    @Size(max = 50)
    private String  timeOfActivity;//活动时间

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }



}

