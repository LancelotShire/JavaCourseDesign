package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Activity;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.util.ComDataUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ActivityService {
    public Map getMapFromActivity(Activity a) {
        Map m = new HashMap();
        if(a == null)
            return m;
        Student s;
        s=a.getStudent();
        if(s == null)
            return m;
        m.put("studentId", s.getStudentId());
        m.put("activityId", a.getActivityId());
        m.put("num",s.getPerson().getNum());
        m.put("name",s.getPerson().getName());
        m.put("activityName",a.getActivityName());
        String activityType = a.getActivityType();
        m.put("activityType",activityType);
        m.put("activityTypeName", ComDataUtil.getInstance().getDictionaryLabelByValue("ActivityType", activityType)); //性别类型的值转换成数据类型名
        m.put("time",a.getTime());
        String activityGrade = a.getActivityGrade();
        m.put("activityGrade",activityGrade);
        m.put("activityGradeName", ComDataUtil.getInstance().getDictionaryLabelByValue("ActivityGrade", activityGrade)); //性别类型的值转换成数据类型名
        m.put("timeOfActivity",a.getTimeOfActivity());
        return m;
    }
}
