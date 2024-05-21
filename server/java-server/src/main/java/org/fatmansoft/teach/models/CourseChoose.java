package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table( name = "course_choose",uniqueConstraints = {})
public class CourseChoose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseChooseId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String time;

    public Integer getCourseChooseId() {
        return courseChooseId;
    }

    public void setCourseChooseId(Integer courseChooseId) {
        this.courseChooseId = courseChooseId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
