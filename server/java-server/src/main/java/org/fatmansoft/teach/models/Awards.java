package org.fatmansoft.teach.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(	name = "awards",
        uniqueConstraints = {
        })

public class Awards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer awardsId;
    @ManyToOne
    @JoinColumn(name="student_id")
    @JsonIgnore
    private Student student;
    @Size(max = 50)
    private String awardsName;//activity名称


    @Size(max = 50)
    private String time ;//活动时长

    @Size(max = 20)
    private String  awardsType;//activity类型
    @Size(max = 50)
    private String  timeOfAwards;//活动时间

    public Integer getAwardsId() {
        return awardsId;
    }

    public void setAwardsId(Integer awardsId) {
        this.awardsId = awardsId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getAwardsName() {
        return awardsName;
    }

    public void setAwardsName(String awardsName) {
        this.awardsName = awardsName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAwardsType() {
        return awardsType;
    }

    public void setAwardsType(String awardsType) {
        this.awardsType = awardsType;
    }

    public String getTimeOfAwards() {
        return timeOfAwards;
    }

    public void setTimeOfAwards(String timeOfAwards) {
        this.timeOfAwards = timeOfAwards;
    }
}

