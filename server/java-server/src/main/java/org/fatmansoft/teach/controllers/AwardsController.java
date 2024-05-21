package org.fatmansoft.teach.controllers;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.models.Awards;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.AwardsRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.service.AwardsService;
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
@RequestMapping("/api/awards")
public class AwardsController {
    @Autowired
    private AwardsService awardsService;
    @Autowired
    private AwardsRepository awardsRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SystemService systemService;


    public List getAwardsMapList(String numName) {
        List dataList = new ArrayList();
        List<Awards> sList = awardsRepository.findAwardsListByNumName(numName);  //数据库查询操作
        if (sList == null || sList.size() == 0)
            return dataList;
        for (int i = 0; i < sList.size(); i++) {
            dataList.add(awardsService.getMapFromAwards(sList.get(i)));
        }
        return dataList;
    }
    @PostMapping("/getAwardsList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getAwardsList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List dataList = getAwardsMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/getStudentAwardsList")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getStudentAwardsList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = CommonMethod.getUsername();
        List dataList = getAwardsMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }



    @PostMapping("/awardsDelete")
    public DataResponse awardsDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer awardsId = dataRequest.getInteger("awardsId");  //获取achievementId值
        Awards a = null;
        Optional<Awards> op;
        if (awardsId != null) {
            op = awardsRepository.findById(awardsId);   //查询获得实体对象
            if (op.isPresent()) {
                a = op.get();
            }
        }
        if (a != null) {
            //从数据库中删除学生对应的荣誉信息
            awardsRepository.delete(a);    //首先数据库永久删除荣誉信息
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    /**
     * getActivityInfo 前端点击学生列表时前端获取学生详细信息请求服务
     *
     * @param dataRequest 从前端获取 ActivityId 查询学生信息的主键 Activity_id
     * @return 根据ActivityId从数据库中查出数据，存在Map对象里，并返回前端
     */
    @PostMapping("/getAwardsInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getAwardsInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer awardsId = dataRequest.getInteger("awardsId");
        Awards a = null;
        Optional<Awards> op;
        if (awardsId != null) {
            op = awardsRepository.findById(awardsId); //根据荣誉主键从数据库查询学生的信息
            if (op.isPresent()) {
                a= op.get();
            }
        }
        return CommonMethod.getReturnData(awardsService.getMapFromAwards(a)); //这里回传包含学生信息的Map对象
    }

    @PostMapping("/getStudentAwardsInfo")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getStudentAwardsInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer awardsId = dataRequest.getInteger("awardsId");
        Awards a = null;
        Optional<Awards> op;
        if (awardsId != null) {
            op = awardsRepository.findById(awardsId); //根据主键从数据库查询学生的信息
            if (op.isPresent()) {
                a= op.get();
            }
        }
        return CommonMethod.getReturnData(awardsService.getMapFromAwards(a)); //这里回传包含学生信息的Map对象
    }



    @PostMapping("/awardsEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse awardsEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer awardsId = dataRequest.getInteger("awardsId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        Awards a = null;
        Student s ;
        Optional<Awards> op;
        boolean isNew = false;
        if (awardsId != null) {
            op = awardsRepository.findById(awardsId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                a = op.get();
            }
        }
        if (a == null) {
            a = new Awards();   // 创建实体对象
        }

        //前端传过来一个num，根据num获取到学生，实现了student与achievement的关联
        num = CommonMethod.getString(form, "num");
        a.setStudent(studentRepository.findByPersonNum(num).get());


        a.setAwardsName(CommonMethod.getString(form, "awardsName"));
        a.setAwardsType(CommonMethod.getString(form, "awardsType"));
        a.setTime(CommonMethod.getString(form, "time"));
        a.setTimeOfAwards(CommonMethod.getString(form, "timeOfAwards"));
        awardsRepository.save(a);  //修改保存学生信息
        return CommonMethod.getReturnData(a.getAwardsId());  // 将achievementId返回前端
    }

    @PostMapping("/awardsStudentEditSave")
    @PreAuthorize(" hasRole('STUDENT')")
    public DataResponse awardsStudentEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer awardsId = dataRequest.getInteger("activityId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        Awards a = null;
        Student s ;
        Optional<Awards> op;
        boolean isNew = false;
        if (awardsId != null) {
            op = awardsRepository.findById(awardsId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                a = op.get();
            }
        }
        if (a == null) {
            a = new Awards();   // 创建实体对象
        }

        //前端传过来一个num，根据num获取到学生，实现了student与achievement的关联
        num = CommonMethod.getString(form, "num");
        a.setStudent(studentRepository.findByPersonNum(num).get());


        a.setAwardsName(CommonMethod.getString(form, "awardsName"));
        a.setAwardsType(CommonMethod.getString(form, "awardsType"));
        a.setTime(CommonMethod.getString(form, "time"));
        a.setTimeOfAwards(CommonMethod.getString(form, "timeOfAwards"));
        awardsRepository.save(a);  //修改保存学生信息
        return CommonMethod.getReturnData(a.getAwardsId());  // 将Id返回前端
    }
    @PostMapping("/getAwardsListExcl")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StreamingResponseBody> getAwardsListExcl(@Valid @RequestBody DataRequest dataRequest) {
        String name= dataRequest.getString("name");
        List list = getAwardsMapList(name);
        Integer widths[] = {8, 20, 10, 15,15,15,15,15};
        int i, j, k;
        String titles[] = {"序号", "学号", "姓名", "荣誉名称", "荣誉类型","荣誉时长","活动时间"};
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
                cell[3].setCellValue(CommonMethod.getString(m, "awardsName"));
                cell[4].setCellValue(CommonMethod.getString(m, "awardsType"));
                cell[5].setCellValue(CommonMethod.getString(m, "time"));
                cell[7].setCellValue(CommonMethod.getString(m, "timeOfAwards"));
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
    @PostMapping("/getStudentAwardsListExcl")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StreamingResponseBody> getStudentAwardsListExcl(@Valid @RequestBody DataRequest dataRequest) {
        String name=CommonMethod.getUsername();
        List list = getAwardsMapList(name);
        Integer widths[] = {8, 20, 10, 15,15,15,15,15};
        int i, j, k;
        String titles[] = {"序号", "学号", "姓名", "荣誉名称", "荣誉类型","荣誉时长","荣誉时间"};
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
                cell[3].setCellValue(CommonMethod.getString(m, "awardsName"));
                cell[4].setCellValue(CommonMethod.getString(m, "awardsType"));
                cell[5].setCellValue(CommonMethod.getString(m, "time"));
                cell[7].setCellValue(CommonMethod.getString(m, "timeOfAwards"));
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