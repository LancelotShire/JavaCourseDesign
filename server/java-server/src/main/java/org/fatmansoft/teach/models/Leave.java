package org.fatmansoft.teach.models;

import javax.persistence.*;


    @Entity
    @Table(	name = "leave",
            uniqueConstraints = {
            })
    public class Leave {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer leaveId;

        @ManyToOne
        @JoinColumn(name = "student_id")
        private Student student;


        private String reason;
        private String starttime;
        private String endtime;

        public Integer getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(Integer leaveId) {
            this.leaveId = leaveId;
        }

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }



        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }
    }

