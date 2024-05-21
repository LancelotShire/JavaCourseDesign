package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Leave;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.LeaveRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/leave")
public class LeaveController {
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    @PostMapping("/getLeaveList")
   public DataResponse getLeaveList(@Valid @RequestBody DataRequest dataRequest) {
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






        List<Leave> sList = leaveRepository.findByStudentStudentId(studentId);
        List dataList = new ArrayList();
        Map m;
        for (Leave s : sList) {
            m = new HashMap();
            m.put("leaveId", s.getLeaveId());
            m.put("studentId",s.getStudent().getStudentId());
            m.put("num",s.getStudent().getPerson().getNum());
            m.put("name",s.getStudent().getPerson().getName());
            m.put("reason",s.getReason());
            m.put("starttime",s.getStarttime());
            m.put("endtime",s.getEndtime());
            dataList.add(m);
        }
       System.out.println(dataList);
       System.out.println("123");
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/leaveDelete")
    public DataResponse leaveDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer leaveId = dataRequest.getInteger("leaveId");
        Optional<Leave> op;
        Leave s = null;
        if(leaveId != null) {
            op= leaveRepository.findById(leaveId);
            if(op.isPresent()) {
                s = op.get();
                leaveRepository.delete(s);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/leaveSave")
    public DataResponse leaveSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer leaveId = dataRequest.getInteger("leaveId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");
        String name = CommonMethod.getString(form, "name"); //Map 获取属性的值
        String reason = CommonMethod.getString(form, "reason");
        String starttime = CommonMethod.getString(form, "starttime");
        String endtime = CommonMethod.getString(form, "endtime");
        Optional<Student> opp;
        Student ss = null;
        opp=studentRepository.findByPersonNum(num);
        ss= opp.get();
        Integer studentId=ss.getStudentId();
        Optional<Leave> op;
        Leave s = null;
        if(leaveId != null) {
            op= leaveRepository.findById(leaveId);
            if(op.isPresent())
                s = op.get();
        }
        if(s == null) {
            s = new Leave();
            s.setStudent(studentRepository.findById(studentId).get());
        }
        s.setReason(reason);
        s.setStarttime(starttime);
        s.setEndtime(endtime);
        leaveRepository.save(s);
        return CommonMethod.getReturnData(s.getLeaveId());
    }

    @PostMapping("/getleaveInfo")
    public DataResponse getLeaveInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer leaveId = dataRequest.getInteger("leaveId");
        Leave s = null;
        Optional<Leave> op;
        System.out.println("啊哈哈哈哈哈哈哈");
        if (leaveId != null) {
            op = leaveRepository.findById(leaveId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                s = op.get();
            }


        }
        if(s == null) {
            s = new Leave();
            s.setStudent(new Student());
        }
        Map m;

        m = new HashMap();
        m.put("leaveId", s.getLeaveId());
        m.put("studentId",s.getStudent().getStudentId());
        m.put("num",s.getStudent().getPerson().getNum());
        m.put("name",s.getStudent().getPerson().getName());
        m.put("reason",s.getReason());
        m.put("starttime",s.getStarttime());
        m.put("endtime",s.getEndtime());

        return CommonMethod.getReturnData(m);
    }

}
