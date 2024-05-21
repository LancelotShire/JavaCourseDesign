package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Activit;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ActivitRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/activit")
public class ActivitController {
    @Autowired
    private ActivitRepository activityRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    @PostMapping("/getActivityList")
    public DataResponse getActivityList(@Valid @RequestBody DataRequest dataRequest) {
        System.out.println("ahhhhhhhhhh");
        String num= dataRequest.getString("numName");
        System.out.println(num);
        System.out.println(num);
        Integer studentId=0;
        if (num==null){num="";}
        if(num.equals("")){ studentId=0;}
        else {
            List<Student> opp;
            Student ss = null;
            opp = studentRepository.findStudentListByNumName(num);
            ss=opp.get(0);

            studentId = ss.getStudentId();
        }






        List<Activit> sList = activityRepository.findByStudentStudentId(studentId);
        List dataList = new ArrayList();
        Map m;
        for (Activit s : sList) {
            m = new HashMap();
            m.put("activityId", s.getActivitId());
            m.put("studentId",s.getStudent().getStudentId());
            m.put("num",s.getStudent().getPerson().getNum());
            m.put("name",s.getStudent().getPerson().getName());
            m.put("thing",s.getThing());
            m.put("windate",s.getWindate());
            m.put("someword",s.getSomeword());
            m.put("model",s.getModel());
            dataList.add(m);
        }
        System.out.println(dataList);
        System.out.println("123");
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/ActivityDelete")
    public DataResponse ActivityDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer activitId = dataRequest.getInteger("activityId");
        Optional<Activit> op;
        Activit s = null;
        if(activitId != null) {
            op= activityRepository.findById(activitId);
            if(op.isPresent()) {
                s = op.get();
                activityRepository.delete(s);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/activitySave")
    public DataResponse activitySave(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");
        String name = CommonMethod.getString(form, "name"); //Map 获取属性的值
        String thing = CommonMethod.getString(form, "thing");
        String someword = CommonMethod.getString(form, "someword");
        String windate = CommonMethod.getString(form, "windate");
        String model = CommonMethod.getString(form, "model");
        Optional<Student> opp;
        Student ss = null;
        opp=studentRepository.findByPersonNum(num);
        ss= opp.get();
        Integer studentId=ss.getStudentId();
        Optional<Activit> op;
        Activit s = null;
        if(activityId != null) {
            op= activityRepository.findById(activityId);
            if(op.isPresent())
                s = op.get();
        }
        if(s == null) {
            s = new Activit();
            s.setStudent(studentRepository.findById(studentId).get());
        }
        s.setThing(thing);
        s.setWindate(windate);
        s.setSomeword(someword);
        s.setModel(model);
        activityRepository.save(s);
        return CommonMethod.getReturnData(s.getActivitId());
    }

    @PostMapping("/getactivityInfo")
    public DataResponse getActivityInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");
        Activit s = null;
        Optional<Activit> op;
        System.out.println("啊哈哈哈哈哈哈哈");
        if (activityId != null) {
            op = activityRepository.findByActivitId(activityId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                s = op.get();
            }


        }
        if(s == null) {
            s = new Activit();
            s.setStudent(new Student());
        }
        Map m;

        m = new HashMap();
        m.put("activityId", s.getActivitId());
        m.put("studentId",s.getStudent().getStudentId());
        m.put("num",s.getStudent().getPerson().getNum());
        m.put("name",s.getStudent().getPerson().getName());
        m.put("thing",s.getThing());
        m.put("someword",s.getSomeword());
        m.put("windate",s.getWindate());
        m.put("model",s.getModel());
        return CommonMethod.getReturnData(m);
    }

}
