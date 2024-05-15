package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
import javafx.beans.binding.ObjectExpression;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.*;

public class StatisticsController {
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

    private List<OptionItem> nameList;
    @FXML
    private ComboBox<OptionItem> nameComboBox;

    @FXML
    private void onQueryButtonClick(){
        Integer studentId;
        Integer courseId = 0;
        OptionItem op;
        op = nameComboBox.getSelectionModel().getSelectedItem();
        if(op != null && op.getValue() != null){
            studentId = Integer.parseInt(op.getValue());
            DataResponse res;
            DataRequest req = new DataRequest();
            req.add("studentId",studentId);
            req.add("courseId",courseId);
            res = HttpRequestUtil.request("/api/score/getScoreList",req);

            if(res.getCode()== 0) {
                scoreList = (ArrayList<Map>)res.getData();
            }
            scoreList = calculate(scoreList);
        }else{
            DataRequest req;
            ArrayList<Map> personalList = new ArrayList<>();
            for(OptionItem person : nameList){
                studentId = person.getId();
                DataResponse res;
                req = new DataRequest();
                req.add("studentId",studentId);
                req.add("courseId",courseId);
                res = HttpRequestUtil.request("/api/score/getScoreList",req);

                if(res.getCode()== 0) {
                    personalList = calculate((ArrayList<Map>)res.getData());
                }

                scoreList.add(personalList.get(0));
            }
        }

        setTableViewData();
        scoreList = new ArrayList<>();
    }

    private void setTableViewData(){
        observableList.clear();
        for (int j = 0; j < scoreList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(scoreList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    public void initialize(){
        DataRequest req =new DataRequest();
        nameList = HttpRequestUtil.requestOptionItemList("/api/score/getStudentItemOptionList",req);
        OptionItem item = new OptionItem(0,null,"请选择");
        nameComboBox.getItems().addAll(item);
        nameComboBox.getItems().addAll(nameList);

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
            if(mark < 60){
                continue;
            }
            float credit = Float.parseFloat(map.get("credit").toString());
            float go = (mark - 50)/10;
            sumCredit += credit;
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
        info.put("totalCredit",sumCredit);
        result.add(info);
        return result;
    }

    @FXML
    private void save(){
        DataRequest req = new DataRequest();
        DataResponse res = HttpRequestUtil.request("/api/scoreStatistics/getScoreStatisticsList",req);
    }

}
