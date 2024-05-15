package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
//import org.fatmansoft.teach.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/scoreStatistics")
public class ScoreStatisticsController {
//    @Autowired
//    private StatisticsRepository statisticsRepository;

    @PostMapping("getScoreStatisticsList")
    public DataResponse getScoreStatisticList(DataRequest req){
        DataResponse res = new DataResponse();
        System.out.println(114514);
        return res;
    }

}
