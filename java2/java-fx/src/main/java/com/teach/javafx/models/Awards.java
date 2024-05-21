package com.teach.javafx.models;

public class Awards {
        private Integer awardsId;
        private Integer studentId;
        private String num;
        private String name;
        private String awardsName;
        private String awardsType;
        private String time;

        private String timeOfAwards;

    public Integer getAwardsId() {
        return awardsId;
    }

    public void setAwardsId(Integer awardsId) {
        this.awardsId = awardsId;
    }

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

    public String getAwardsName() {
        return awardsName;
    }

    public void setAwardsName(String awardsName) {
        this.awardsName = awardsName;
    }

    public String getAwardsType() {
        return awardsType;
    }

    public void setAwardsType(String awardsType) {
        this.awardsType = awardsType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeOfAwards() {
        return timeOfAwards;
    }

    public void setTimeOfAwards(String timeOfAwards) {
        this.timeOfAwards = timeOfAwards;
    }
}
