package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

/*
    管理员设置选课时间，
    学生发出选课请求时查询选课时间，若在选课时间内，则可以选课。
*/
@Entity
@Table(	name = "select_time",
        uniqueConstraints = {
        })
public class SelectTime {
        public Integer getTimeId() {
                return timeId;
        }

        public void setTimeId(Integer timeId) {
                this.timeId = timeId;
        }

        public String getTimeName() {
                return timeName;
        }

        public void setTimeName(String timeName) {
                this.timeName = timeName;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer timeId;

        @Size(max = 20)
        private String timeName;

        public String getTime() {
                return time;
        }

        public void setTime(String time) {
                this.time = time;
        }

        @Size(max = 20)
        private String time;

}
