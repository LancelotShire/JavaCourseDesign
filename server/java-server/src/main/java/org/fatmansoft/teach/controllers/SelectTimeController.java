package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.SelectTime;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.SelectTimeRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
    @RequestMapping("/api/selectTime")
public class SelectTimeController {
    @Autowired
    private SelectTimeRepository selectTimeRepository;

    @PostMapping("/getSelectTime")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse setSelectTime(@Valid @RequestBody DataRequest dataRequest) {
        Map time = dataRequest.getMap("time");
        String begin = (String) time.get("begin");
        String end = (String) time.get("end");

        SelectTime bt = selectTimeRepository.findSelectTimeByTimeName("begin").get();
        SelectTime et = selectTimeRepository.findSelectTimeByTimeName("end").get();

        bt.setTime(begin);
        et.setTime(end);

        selectTimeRepository.save(bt);
        selectTimeRepository.save(et);

        return CommonMethod.getReturnMessageOK();
    }


    @PostMapping("/getSelectPermission")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse getSelectPermission(DataRequest ignoredReq) {
        Optional<SelectTime> beginTime = selectTimeRepository.findSelectTimeByTimeName("begin");
        Optional<SelectTime> endTime = selectTimeRepository.findSelectTimeByTimeName("end");

        String ret;
        if(isWithinTimeRange(beginTime.get().getTime(), endTime.get().getTime())) {
            ret = "1";
        }
        else {
            ret = "0";
        }
        return CommonMethod.getReturnData(ret);
    }

    public boolean isWithinTimeRange(String beginTime, String endTime) {
        LocalDate beginDate = LocalDate.parse(beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 判断当前日期是否在指定范围内
        System.out.println("开放选课:" + (!currentDate.isBefore(beginDate) && !currentDate.isAfter(endDate)));
        return !currentDate.isBefore(beginDate) && !currentDate.isAfter(endDate);
    }
}

