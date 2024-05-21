package com.teach.javafx.models;

public class Activity {
    private Integer activityId;
    private Integer studentId;
    private String num;
    private String name;
    private String activityName;
    private String activityType;
    private String time;
    private String activityGrade;
    private String timeOfActivity;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Activity() {
    }

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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
