package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.CourseChoose;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseChooseRepository;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courseChoose")
public class CourseChooseController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseChooseRepository courseChooseRepository;

    @PostMapping("/getCourseChooseList")
    public DataResponse getCourseChooseList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentNum = dataRequest.getInteger("studentNum");
        if(studentNum == null)
            studentNum = 0;
        Integer courseNum = dataRequest.getInteger("courseNum");
        if(courseNum == null)
            courseNum = 0;
        List<CourseChoose> cList = courseChooseRepository.findByStudentNumCourseNum(studentNum,courseNum);
        List dataList = new ArrayList();
        Map m;
        for(CourseChoose s:cList){
            m = new HashMap();
            m.put("courseChooseId", s.getCourseChooseId()+"");
            m.put("studentId",s.getStudent().getStudentId()+"");
            m.put("courseId",s.getCourse().getCourseId()+"");
            m.put("studentNum",s.getStudent().getPerson().getNum());
            m.put("studentName",s.getStudent().getPerson().getName());
            m.put("className",s.getStudent().getClassName());
            m.put("courseNum",s.getCourse().getNum());
            m.put("courseName",s.getCourse().getName());
            m.put("time",s.getTime());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/courseChooseSave")
    public DataResponse courseChooseSave(@Valid @RequestBody DataRequest dataRequest){
        Integer courseChooseId = dataRequest.getInteger("courseChooseId");
        Integer studentId = dataRequest.getInteger("studentId");
        Integer courseId = dataRequest.getInteger("courseId");
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = localDateTime.format(dateTimeFormatter);
        Optional<CourseChoose> op;
        CourseChoose courseChoose = null;

        List<CourseChoose> test = courseChooseRepository.findByStudentCourse(studentId,courseId);
        if(!test.isEmpty())
            return CommonMethod.getReturnMessageError("请勿重复选课");

        if(courseChooseId != null){
            op = courseChooseRepository.findById(courseChooseId);
            if (op.isPresent()){
                courseChoose = op.get();
            }
        }
        if(courseChoose == null){
            courseChoose = new CourseChoose();
            courseChoose.setStudent(studentRepository.findById(studentId).get());
            courseChoose.setCourse(courseRepository.findById(courseId).get());
        }
        courseChoose.setTime(time);
        courseChooseRepository.save(courseChoose);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/courseChooseDelete")
    public DataResponse courseChooseDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer courseChooseId = dataRequest.getInteger("courseChooseId");
        Optional<CourseChoose> op;
        CourseChoose courseChoose = null;
        if(courseChooseId != null){
            op = courseChooseRepository.findById(courseChooseId);
            if(op.isPresent()){
                courseChoose = op.get();
                courseChooseRepository.delete(courseChoose);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }


    /* 获取当前学生用户的已选课程 */
    @PostMapping("/getSelectedCourse")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse getSelectedCourse(@Valid @RequestBody DataRequest dataRequest) {
        String username = CommonMethod.getUsername();
        Optional<Student> sOp = studentRepository.findByPersonNum(username);  // 查询获得 Student对象
        if (!sOp.isPresent())
            return CommonMethod.getReturnMessageError("学生不存在！");
        Student s = sOp.get();

        List<CourseChoose> courseChooseList = courseChooseRepository.findByStudent(s);
        List<Map> mapList = new ArrayList<>();

        for(CourseChoose courseChoose: courseChooseList) {
            Course course = courseChoose.getCourse();
            Map map = new HashMap<>();
            map.put("num", course.getNum());
            map.put("name", course.getName());
            map.put("credit", course.getCredit().toString());
            map.put("preCourse", course.getPreCourse() != null ? course.getPreCourse().getName() : "");
            map.put("time", courseChoose.getTime());
            mapList.add(map);
        }

        return CommonMethod.getReturnData(mapList);
    }

    /* 保存当前学生用户的已选课程 */
    @PostMapping("/selectSave")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse selectSave(@Valid @RequestBody DataRequest dataRequest) {
        String username = CommonMethod.getUsername();
        Optional<Student> sOp = studentRepository.findByPersonNum(username);  // 查询获得 Student对象
        if (!sOp.isPresent())
            return CommonMethod.getReturnMessageError("学生不存在！");
        Student s = sOp.get();

        String courseNum = dataRequest.getString("courseNum");

        System.out.println("selectSave:" + "courseNum=" + courseNum);

        Optional<Course> courseOp = courseRepository.findByNum(courseNum);
        if(!courseOp.isPresent())
            return CommonMethod.getReturnMessageError("课程不存在!");
        Course c = courseOp.get();

        Optional<CourseChoose> cc = courseChooseRepository.findByStudentAndCourse(s, c);
        if(cc.isPresent())
            return CommonMethod.getReturnMessageError("请勿重复选课!");

        CourseChoose courseChoose = new CourseChoose();
        courseChoose.setCourse(c);
        courseChoose.setStudent(s);
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.toString();
        courseChoose.setTime(time);

        courseChooseRepository.save(courseChoose);

        return CommonMethod.getReturnMessageOK();
    }


    @PostMapping("/withdrawCourse")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse withdrawCourse(@Valid @RequestBody DataRequest dataRequest) {
        String username = CommonMethod.getUsername();
        Optional<Student> sOp = studentRepository.findByPersonNum(username);  // 查询获得 Student对象
        if (!sOp.isPresent())
            return CommonMethod.getReturnMessageError("学生不存在！");
        Student s = sOp.get();

        String courseNum = dataRequest.getString("courseNum");
        Optional<Course> courseOp = courseRepository.findByNum(courseNum);
        if(!courseOp.isPresent())
            return CommonMethod.getReturnMessageError("课程不存在!");
        Course c = courseOp.get();

        Optional<CourseChoose> ccOp = courseChooseRepository.findByStudentAndCourse(s, c);
        if(!ccOp.isPresent())
            return CommonMethod.getReturnMessageError("删除失败:未选该课!");
        CourseChoose cc = ccOp.get();

        courseChooseRepository.delete(cc);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getAllCourseList")
    public DataResponse getAllCourseList(@Valid @RequestBody DataRequest dataRequest){
        List<Course> courseList = courseRepository.findAll();
        List<Map> mapList = new ArrayList<>();

        for(Course course: courseList) {
            Map map = new HashMap<>();
            map.put("num", course.getNum());
            map.put("name", course.getName());
            map.put("credit", course.getCredit().toString());
            map.put("preCourse", course.getPreCourse() != null ? course.getPreCourse().getName() : "");
            map.put("time", "");
            mapList.add(map);
        }

        return CommonMethod.getReturnData(mapList);
    }

}
