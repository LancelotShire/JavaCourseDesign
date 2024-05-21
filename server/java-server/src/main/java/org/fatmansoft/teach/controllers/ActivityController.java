package org.fatmansoft.teach.controllers;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.models.Activity;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ActivityRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.service.ActivityService;
import org.fatmansoft.teach.service.SystemService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SystemService systemService;


    public List getActivityMapList(String numName) {
        List dataList = new ArrayList();
        List<Activity> sList = activityRepository.findActivityListByNumName(numName);  //数据库查询操作
        if (sList == null || sList.size() == 0)
            return dataList;
        for (int i = 0; i < sList.size(); i++) {
            dataList.add(activityService.getMapFromActivity(sList.get(i)));
        }
        return dataList;
    }
    @PostMapping("/getActivityList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getActivityList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List dataList = getActivityMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/getStudentActivityList")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getStudentActivityList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = CommonMethod.getUsername();
        List dataList = getActivityMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }



    @PostMapping("/activityDelete")
    public DataResponse activityDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");  //获取achievementId值
        Activity a = null;
        Optional<Activity> op;
        if (activityId != null) {
            op = activityRepository.findById(activityId);   //查询获得实体对象
            if (op.isPresent()) {
                a = op.get();
            }
        }
        if (a != null) {
            //从数据库中删除学生对应的荣誉信息
            activityRepository.delete(a);    //首先数据库永久删除荣誉信息
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    /**
     * getActivityInfo 前端点击学生列表时前端获取学生详细信息请求服务
     *
     * @param dataRequest 从前端获取 ActivityId 查询学生信息的主键 Activity_id
     * @return 根据ActivityId从数据库中查出数据，存在Map对象里，并返回前端
     */
    @PostMapping("/getActivityInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getActivityInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");
        Activity a = null;
        Optional<Activity> op;
        if (activityId != null) {
            op = activityRepository.findById(activityId); //根据荣誉主键从数据库查询学生的信息
            if (op.isPresent()) {
                a= op.get();
            }
        }
        return CommonMethod.getReturnData(activityService.getMapFromActivity(a)); //这里回传包含学生信息的Map对象
    }

    @PostMapping("/getStudentActivityInfo")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getStudentActivityInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");
        Activity a = null;
        Optional<Activity> op;
        if (activityId != null) {
            op = activityRepository.findById(activityId); //根据主键从数据库查询学生的信息
            if (op.isPresent()) {
                a= op.get();
            }
        }
        return CommonMethod.getReturnData(activityService.getMapFromActivity(a)); //这里回传包含学生信息的Map对象
    }


   /* @PostMapping("/activityNum")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse activityNum(@Valid @RequestBody DataRequest dataRequest) {
        String num= dataRequest.getString("num");
        Integer id=CommonMethod.getUserId();
        Student s = studentRepository.findByUserId(id).get(); //获取当前学生
        String snum=s.getPerson().getNum();
        if(!Objects.equals(num, snum))
        {
            return CommonMethod.getReturnMessageError("学号不一致，请重新输入！");
        }
        return CommonMethod.getReturnMessageOK();
    }*/

    @PostMapping("/activityEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        Activity a = null;
        Student s ;
        Optional<Activity> op;
        boolean isNew = false;
        if (activityId != null) {
            op = activityRepository.findById(activityId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                a = op.get();
            }
        }
        if (a == null) {
            a = new Activity();   // 创建实体对象
        }

        //前端传过来一个num，根据num获取到学生，实现了student与achievement的关联
        num = CommonMethod.getString(form, "num");
        a.setStudent(studentRepository.findByPersonNum(num).get());


        a.setActivityName(CommonMethod.getString(form, "activityName"));
        a.setActivityType(CommonMethod.getString(form, "activityType"));
        a.setTime(CommonMethod.getString(form, "time"));
        a.setActivityGrade(CommonMethod.getString(form, "activityGrade"));
        a.setTimeOfActivity(CommonMethod.getString(form, "timeOfActivity"));
        activityRepository.save(a);  //修改保存学生信息
        return CommonMethod.getReturnData(a.getActivityId());  // 将achievementId返回前端
    }

    @PostMapping("/activityStudentEditSave")
    @PreAuthorize(" hasRole('STUDENT')")
    public DataResponse activityStudentEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        Activity a = null;
        Student s ;
        Optional<Activity> op;
        boolean isNew = false;
        if (activityId != null) {
            op = activityRepository.findById(activityId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                a = op.get();
            }
        }
        if (a == null) {
            a = new Activity();   // 创建实体对象
        }

        //前端传过来一个num，根据num获取到学生，实现了student与achievement的关联
        num = CommonMethod.getString(form, "num");
        a.setStudent(studentRepository.findByPersonNum(num).get());


        a.setActivityName(CommonMethod.getString(form, "activityName"));
        a.setActivityType(CommonMethod.getString(form, "activityType"));
        a.setTime(CommonMethod.getString(form, "time"));
        a.setActivityGrade(CommonMethod.getString(form, "activityGrade"));
        a.setTimeOfActivity(CommonMethod.getString(form, "timeOfActivity"));
        activityRepository.save(a);  //修改保存学生信息
        return CommonMethod.getReturnData(a.getActivityId());  // 将Id返回前端
    }
    @PostMapping("/getActivityListExcl")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StreamingResponseBody> getActivityListExcl(@Valid @RequestBody DataRequest dataRequest) {
        String name= dataRequest.getString("name");
        List list = getActivityMapList(name);
        Integer widths[] = {8, 20, 10, 15,15,15,15,15};
        int i, j, k;
        String titles[] = {"序号", "学号", "姓名", "活动名称", "活动类型","活动时长","活动等级","活动时间"};
        String outPutSheetName = "student.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for (j = 0; j < widths.length; j++) {
            sheet.setColumnWidth(j, widths[j] * 256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 11);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        Map m;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                m = (Map) list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(CommonMethod.getString(m, "num"));
                cell[2].setCellValue(CommonMethod.getString(m, "name"));
                cell[3].setCellValue(CommonMethod.getString(m, "activityName"));
                cell[4].setCellValue(CommonMethod.getString(m, "activityType"));
                cell[5].setCellValue(CommonMethod.getString(m, "time"));
                cell[6].setCellValue(CommonMethod.getString(m, "activityGrade"));
                cell[7].setCellValue(CommonMethod.getString(m, "timeOfActivity"));
            }
        }
        try {
            StreamingResponseBody stream = outputStream -> {
                wb.write(outputStream);
            };
            return ResponseEntity.ok()
                    .contentType(CommonMethod.exelType)
                    .body(stream);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
    @PostMapping("/getStudentActivityListExcl")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StreamingResponseBody> getStudentActivityListExcl(@Valid @RequestBody DataRequest dataRequest) {
        String name=CommonMethod.getUsername();
        List list = getActivityMapList(name);
        Integer widths[] = {8, 20, 10, 15,15,15,15,15};
        int i, j, k;
        String titles[] = {"序号", "学号", "姓名", "活动名称", "活动类型","活动时长","活动等级","活动时间"};
        String outPutSheetName = "student.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for (j = 0; j < widths.length; j++) {
            sheet.setColumnWidth(j, widths[j] * 256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 11);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        Map m;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                m = (Map) list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(CommonMethod.getString(m, "num"));
                cell[2].setCellValue(CommonMethod.getString(m, "name"));
                cell[3].setCellValue(CommonMethod.getString(m, "activityName"));
                cell[4].setCellValue(CommonMethod.getString(m, "activityType"));
                cell[5].setCellValue(CommonMethod.getString(m, "time"));
                cell[6].setCellValue(CommonMethod.getString(m, "activityGrade"));
                cell[7].setCellValue(CommonMethod.getString(m, "timeOfActivity"));
            }
        }
        try {
            StreamingResponseBody stream = outputStream -> {
                wb.write(outputStream);
            };
            return ResponseEntity.ok()
                    .contentType(CommonMethod.exelType)
                    .body(stream);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}