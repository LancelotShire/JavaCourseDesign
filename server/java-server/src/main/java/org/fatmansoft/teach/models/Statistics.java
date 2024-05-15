package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "statistics",
        uniqueConstraints = {
        })
public class Statistics {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer statisticsId;

        private Integer studentNum;

        @Size(max = 20)
        private String studentName;

        @Size(max = 20)
        private String className;

        float averageScore;

        float go;

        float hyaku;

        float totalCredit;

        public Integer getStatisticsId() {
                return statisticsId;
        }

        public void setStatisticsId(Integer statisticsId) {
                this.statisticsId = statisticsId;
        }

        public Integer getStudentNum() {
                return studentNum;
        }

        public void setStudentNum(Integer studentNum) {
                this.studentNum = studentNum;
        }

        public String getStudentName() {
                return studentName;
        }

        public void setStudentName(String studentName) {
                this.studentName = studentName;
        }

        public String getClassName() {
                return className;
        }

        public void setClassName(String className) {
                this.className = className;
        }

        public float getAverageScore() {
                return averageScore;
        }

        public void setAverageScore(float averageScore) {
                this.averageScore = averageScore;
        }

        public float getGo() {
                return go;
        }

        public void setGo(float go) {
                this.go = go;
        }

        public float getHyaku() {
                return hyaku;
        }

        public void setHyaku(float hyaku) {
                this.hyaku = hyaku;
        }

        public float getTotalCredit() {
                return totalCredit;
        }

        public void setTotalCredit(float totalCredit) {
                this.totalCredit = totalCredit;
        }
}
