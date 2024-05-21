package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Fee;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.FeeRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fee")
public class FeeController {
    @Autowired
    private FeeRepository feeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    @PostMapping("/getFeeList")
   public DataResponse getFeeList(@Valid @RequestBody DataRequest dataRequest) {
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






        List<Fee> sList = feeRepository.findByStudentStudentId(studentId);
        List dataList = new ArrayList();
        Map m;
        for (Fee s : sList) {
            m = new HashMap();
            m.put("feeId", s.getFeeId());
            m.put("studentId",s.getStudent().getStudentId());
            m.put("num",s.getStudent().getPerson().getNum());
            m.put("name",s.getStudent().getPerson().getName());
            m.put("buy",s.getBuy());
            m.put("money",s.getMoney());
            m.put("earntime",s.getDay());
            dataList.add(m);
        }
       System.out.println(dataList);
       System.out.println("123");
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/feeDelete")
    public DataResponse feeDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer feeId = dataRequest.getInteger("feeId");
        Optional<Fee> op;
        Fee s = null;
        if(feeId != null) {
            op= feeRepository.findById(feeId);
            if(op.isPresent()) {
                s = op.get();
                feeRepository.delete(s);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/feeSave")
    public DataResponse leaveSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer feeId = dataRequest.getInteger("feeId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");
        String name = CommonMethod.getString(form, "name"); //Map 获取属性的值
        String buy = CommonMethod.getString(form, "buy");
        Double money = CommonMethod.getDouble(form, "money");
        String day = CommonMethod.getString(form, "earntime");
        Optional<Student> opp;
        Student ss = null;
        opp=studentRepository.findByPersonNum(num);
        ss= opp.get();
        Integer studentId=ss.getStudentId();
        Optional<Fee> op;
        Fee s = null;
        if(feeId != null) {
            op= feeRepository.findById(feeId);
            if(op.isPresent())
                s = op.get();
        }
        if(s == null) {
            s = new Fee();
            s.setStudent(studentRepository.findById(studentId).get());
        }
        s.setBuy(buy);
        s.setMoney(money);
        s.setDay(day);
        feeRepository.save(s);
        return CommonMethod.getReturnData(s.getFeeId());
    }

    @PostMapping("/getfeeInfo")
    public DataResponse getFeeInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer feeId = dataRequest.getInteger("feeId");
        Fee s = null;
        Optional<Fee> op;
        System.out.println("啊哈哈哈哈哈哈哈");
        if (feeId != null) {
            op =feeRepository.findById(feeId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                s = op.get();
            }


        }
        if(s == null) {
            s = new Fee();
            s.setStudent(new Student());
        }
        Map m;

        m = new HashMap();
        m.put("feeId", s.getFeeId());
        m.put("studentId",s.getStudent().getStudentId());
        m.put("num",s.getStudent().getPerson().getNum());
        m.put("name",s.getStudent().getPerson().getName());
        m.put("buy",s.getBuy());
        m.put("moner",s.getMoney());
        m.put("earntime",s.getDay());

        return CommonMethod.getReturnData(m);
    }

}
