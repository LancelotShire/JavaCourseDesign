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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                updateNameList();

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
        scoreList = new ArrayList<>();
    }

    @FXML
    public void initialize(){
        updateNameList();

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

    @FXML
    private void onQueryButtonClickInDatabase(){
        Integer studentId;
        OptionItem op;
        DataRequest req = new DataRequest();
        op = nameComboBox.getSelectionModel().getSelectedItem();
        if(op != null && op.getValue() != null){
            String input = op.getTitle();
            Pattern pattern = Pattern.compile("\\d+"); // 匹配一个或多个数字
            Matcher matcher = pattern.matcher(input);
            String studentNum = "";

            while (matcher.find()) {
                studentNum = matcher.group();
            }

            System.out.println(studentNum);
            req.add("numName",studentNum);
            DataResponse res = HttpRequestUtil.request("/api/scoreStatistics/getScoreStatisticsList",req);

            if(res.getCode()== 0) {
                scoreList = (ArrayList<Map>)res.getData();
                System.out.println(scoreList.get(0).get("studentNum"));
            }
        }else{
            req.add("statisticsId","");
            DataResponse res = HttpRequestUtil.request("/api/scoreStatistics/getScoreStatisticsList",req);

            if(res.getCode()== 0) {
                scoreList = (ArrayList<Map>)res.getData();
            }
        }

        System.out.println(scoreList);
        setTableViewData();
        scoreList = new ArrayList<>();
        updateNameList();
    }

    @FXML
    private void onSaveButtonClick(){
        boolean status = true;
        Integer studentId;
        Integer courseId = 0;
        DataRequest req;
        ArrayList<Map> personalList = new ArrayList<>();
        System.out.println(nameList);
        for(OptionItem person : nameList) {
            studentId = person.getId();
            DataResponse res;
            req = new DataRequest();
            req.add("studentId", studentId);
            req.add("courseId", courseId);
            res = HttpRequestUtil.request("/api/score/getScoreList", req);

            ArrayList<Map> arrayList = (ArrayList<Map>) res.getData();
            System.out.println(arrayList);
            if (res.getCode() == 0) {
                personalList = calculate(arrayList);
            }
            Map map = personalList.get(0);
            map.put("studentId",studentId);
            scoreList.add(map);
            System.out.println(map);
            System.out.println(scoreList);
        }
        System.out.println(scoreList);

        if(scoreList.isEmpty()){
            MessageDialog.showDialog("提交失败,因为表是空的！");
            return;
        }

        for(Map map:scoreList){
            req = new DataRequest();
            req.add("statisticsId",map.get("studentId"));
            req.add("studentNum",map.get("studentNum"));
            req.add("studentName",map.get("studentName"));
            req.add("className",map.get("className"));
            req.add("go",map.get("go"));
            req.add("hyaku",map.get("hyaku"));
            req.add("averageScore",map.get("averageScore"));
            req.add("totalCredit",map.get("totalCredit"));

            DataResponse res = HttpRequestUtil.request("/api/scoreStatistics/saveScoreStatistics",req);

            if (res != null && res.getCode() != 0) status = false;
        }

        if(status){
            MessageDialog.showDialog("提交成功！");
            scoreList = new ArrayList<>();
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog("提交失败！");
        }

        scoreList = new ArrayList<>();
        updateNameList();
    }

    private void updateNameList(){
        nameList = new ArrayList<>();
        nameComboBox.getItems().clear();
        DataRequest req =new DataRequest();
        nameList = HttpRequestUtil.requestOptionItemList("/api/score/getStudentItemOptionList",req);
        OptionItem item = new OptionItem(0,null,"请选择");
        nameComboBox.getItems().addAll(item);
        nameComboBox.getItems().addAll(nameList);
    }
}
