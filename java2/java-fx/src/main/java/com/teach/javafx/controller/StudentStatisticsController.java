package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentStatisticsController {
    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> studentNumColumn;
    @FXML
    private TableColumn<Map,String> classNameColumn;
    @FXML
    private TableColumn<Map,String> averageScoreColumn;
    @FXML
    private TableColumn<Map,String> goColumn;
    @FXML
    private TableColumn<Map,String> hyakuColumn;
    @FXML
    private TableColumn<Map,String> totalCreditColumn;

    private ArrayList<Map> scoreList = new ArrayList();
    private ObservableList<Map> observableList= FXCollections.observableArrayList();

    @FXML
    private void onQueryButtonClick(){
        String data = (String) HttpRequestUtil.request("/api/scoreStatistics/getUserName",new DataRequest()).getData();
        Integer studentId = Integer.parseInt(data);
        DataResponse res;
        DataRequest req = new DataRequest();
        req.add("stduentId",studentId);
        req.add("courseId",0);
        res = HttpRequestUtil.request("/api/score/getScoreList",req);

        if(res.getCode()== 0) {
            scoreList = (ArrayList<Map>)res.getData();
        }
        scoreList = calculate(scoreList);

        setTableViewData();
        scoreList = new ArrayList<>();
    }

    private void setTableViewData(){
        observableList.clear();
        for (int j = 0; j < scoreList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(scoreList.get(j)));
        }
        dataTableView.setItems(observableList);
        scoreList = new ArrayList<>();
    }

    @FXML
    public void initialize(){
        studentNumColumn.setCellValueFactory(new MapValueFactory<>("studentNum"));
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        classNameColumn.setCellValueFactory(new MapValueFactory<>("className"));
        averageScoreColumn.setCellValueFactory(new MapValueFactory<>("averageScore"));
        goColumn.setCellValueFactory(new MapValueFactory<>("go"));
        hyakuColumn.setCellValueFactory(new MapValueFactory<>("hyaku"));
        totalCreditColumn.setCellValueFactory(new MapValueFactory<>("totalCredit"));

        onQueryButtonClick();
    }

    private ArrayList<Map> calculate(ArrayList<Map> list){
        ArrayList<Map> result = new ArrayList<>();
        Map info = new HashMap();
        if(list.isEmpty()){
            return result;
        }
        float gainedSumCredit = 0;
        float sumCredit = 0;
        float sumGo = 0;
        float sumMark = 0;
        float count = 0;
        Object studentNum = list.get(0).get("studentNum");
        Object studentName = list.get(0).get("studentName");
        Object className = list.get(0).get("className");
        info.put("studentNum",studentNum);
        info.put("studentName",studentName);
        info.put("className",className);
        for(Map map : list){
            float mark = Float.parseFloat(map.get("mark").toString());
            float credit = Float.parseFloat(map.get("credit").toString());
            sumCredit += credit;
            if(mark < 60){
                continue;
            }
            float go = (mark - 50)/10;
            gainedSumCredit += credit;
            sumGo += go * credit;
            sumMark += mark;
            count += 1;
        }
        float averMark = sumMark/count;
        float averGo = sumGo/sumCredit;
        float averHyaku = averGo * 10 + 50;
        info.put("averageScore",averMark);
        info.put("go",averGo);
        info.put("hyaku",averHyaku);
        info.put("totalCredit",gainedSumCredit);
        result.add(info);
        return result;
    }
}
