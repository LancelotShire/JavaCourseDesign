package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(	name = "activit"
)
public class Activit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activitId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private String thing;
    private String someword;


    private String model;

    private String windate;

    public Integer getActivitId() {
        return activitId;
    }

    public void setActivityId(Integer activityId) {
        this.activitId = activitId;
    }


    public String getThing() {
        return thing;
    }

    public void setThing(String thing) {
        this.thing = thing;
    }
    public String getSomeword() {
        return someword;
    }

    public void setSomeword(String someword) {
        this.someword = someword;
    }
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public String getWindate() {
        return windate;
    }

    public void setWindate(String windate) {
        this.windate= windate;
    }
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
