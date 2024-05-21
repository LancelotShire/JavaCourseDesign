package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Statistics;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ScoreRepository;
import org.fatmansoft.teach.repository.StatisticsRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/scoreStatistics")
public class ScoreStatisticsController {
    @Autowired
    private StatisticsRepository statisticsRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/getScoreStatisticsList")
    public DataResponse getScoreStatisticsList(@Valid @RequestBody DataRequest req){
        List<Statistics> sList;
        if(req.getData().containsKey("numName")){
            String numName = req.getString("numName");
            System.out.println(numName);
            sList = statisticsRepository.findByStudentNumName(numName);
        } else if (req.getData().containsKey("statisticsId")) {
            String statisticsId = req.getString("statisticsId");
            System.out.println(statisticsId);
            sList = statisticsRepository.findByStatisticsId(statisticsId);
        } else {
            sList = statisticsRepository.findByStudentNumName("");
        }
        List dataList = new ArrayList<>();
        if(sList.isEmpty()){
            return CommonMethod.getReturnData(dataList);
        }
        Map map;
        for(Statistics statistics : sList){
            map = new HashMap<>();
            map.put("statisticsId",statistics.getStatisticsId());
            map.put("studentNum",statistics.getStudentNum().toString());
            map.put("studentName",statistics.getStudentName());
            map.put("className",statistics.getClassName());
            map.put("go",statistics.getGo());
            map.put("hyaku",statistics.getHyaku());
            map.put("averageScore",statistics.getAverageScore());
            map.put("totalCredit",statistics.getTotalCredit());

            System.out.println(statistics.getStudentNum());
            dataList.add(map);
        }

        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/saveScoreStatistics")
    public DataResponse saveScoreStatistics(@Valid @RequestBody DataRequest req){
        System.out.println(req.getData());
        Integer statisticsId = req.getInteger("statisticsId");
        Integer studentNum = req.getInteger("studentNum");
        String studentName = req.getString("studentName");
        String className = req.getString("className");
        double go = req.getDouble("go");
        double hyaku = req.getDouble("hyaku");
        double averageScore = req.getDouble("averageScore");
        double totalCredit = req.getDouble("totalCredit");
        System.out.println(studentNum);

        List<Statistics> deleteItem = statisticsRepository.findByStudentNumName(studentName);
        if(!deleteItem.isEmpty()){
            statisticsId = deleteItem.get(0).getStatisticsId();
            deleteScoreStatics(deleteItem.get(0));
        }

        Statistics s;
        s = new Statistics();
        s.setStatisticsId(statisticsId);
        s.setStudentNum(studentNum);
        s.setStudentName(studentName);
        s.setClassName(className);
        s.setGo((float) go);
        s.setHyaku((float) hyaku);
        s.setAverageScore((float) averageScore);
        s.setTotalCredit((float) totalCredit);
        statisticsRepository.save(s);
        return CommonMethod.getReturnMessageOK();
    }

    public void deleteScoreStatics(Statistics statistics){
        statisticsRepository.delete(statistics);
    }

//    @PostMapping("/getStudentScoreList")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('ROLE_STUDENT')")
//    public DataResponse getStudentScoreList(@Valid @RequestBody DataRequest dataRequest) {
//        Integer userId = CommonMethod.getUserId();
//        Optional<Student> op = studentRepository.findByUserId(userId);
//        Student student = null;
//        if(op.isPresent()){
//            student = op.get();
//        }
//        Integer studentId = student.getPerson().getPersonId();
//        List<Score> sList = scoreRepository.findByStudentStudentId(studentId);
//        Map m;
//        List dataList = new ArrayList();
//        for (Score s : sList) {
//            m = new HashMap();
//            m.put("scoreId", s.getScoreId()+"");
//            m.put("studentId",s.getStudent().getStudentId()+"");
//            m.put("courseId",s.getCourse().getCourseId()+"");
//            m.put("studentNum",s.getStudent().getPerson().getNum());
//            m.put("studentName",s.getStudent().getPerson().getName());
//            m.put("className",s.getStudent().getClassName());
//            m.put("courseNum",s.getCourse().getNum());
//            m.put("courseName",s.getCourse().getName());
//            m.put("credit",""+s.getCourse().getCredit());
//            m.put("mark",""+s.getMark());
//            dataList.add(m);
//        }
//
//        return CommonMethod.getReturnData(dataList);
//    }

    @PostMapping("/getUserName")
    public DataResponse getUserName(@Valid @RequestBody DataRequest dataRequest){
        System.out.println(CommonMethod.getUsername());
        Optional<Student> op = studentRepository.findByPersonNum(CommonMethod.getUsername());
        Student student = null;
        if(op.isPresent()){
            student = op.get();
        }
        return CommonMethod.getReturnData(student.getStudentId().toString());
    }

}
