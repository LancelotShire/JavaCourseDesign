package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Awards;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.util.ComDataUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AwardsService {
    public Map getMapFromAwards(Awards a) {
        Map m = new HashMap();
        if(a == null)
            return m;
        Student s;
        s=a.getStudent();
        if(s == null)
            return m;
        m.put("studentId", s.getStudentId());
        m.put("awardsId", a.getAwardsId());
        m.put("num",s.getPerson().getNum());
        m.put("name",s.getPerson().getName());
        m.put("awardsName",a.getAwardsName());
        String awardsType = a.getAwardsType();
        m.put("awardsType",awardsType);
        m.put("awardsTypeName", ComDataUtil.getInstance().getDictionaryLabelByValue("AwardsType", awardsType)); //性别类型的值转换成数据类型名
        m.put("time",a.getTime());//性别类型的值转换成数据类型名
        m.put("timeOfAwards",a.getTimeOfAwards());
        return m;
    }
}