package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentScoreTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> studentNumColumn;
    @FXML
    private TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> classNameColumn;
    @FXML
    private TableColumn<Map,String> courseNumColumn;
    @FXML
    private TableColumn<Map,String> courseNameColumn;
    @FXML
    private TableColumn<Map,String> creditColumn;
    @FXML
    private TableColumn<Map,String> markColumn;


    private ArrayList<Map> scoreList = new ArrayList();  // 学生信息列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    @FXML
    private ComboBox<OptionItem> courseComboBox;


    private List<OptionItem> courseList;

    private ScoreEditController scoreEditController = null;
    private Stage stage = null;

    public List<OptionItem> getCourseList() {
        return courseList;
    }


    @FXML
    private void onQueryButtonClick(){
        Integer studentId = Integer.parseInt((String) HttpRequestUtil.request("/api/scoreStatistics/getUserName",new DataRequest()).getData());
        Integer courseId = 0;
        OptionItem op;
        op = courseComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            courseId = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        res = HttpRequestUtil.request("/api/score/getScoreList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            scoreList = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        for (int j = 0; j < scoreList.size(); j++) {
            map = scoreList.get(j);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    public void initialize() {
        studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        classNameColumn.setCellValueFactory(new MapValueFactory<>("className"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("courseNum"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));

        DataRequest req =new DataRequest();
        courseList = HttpRequestUtil.requestOptionItemList("/api/score/getCourseItemOptionList",req); //从后台获取所有学生信息列表集合
        OptionItem item = new OptionItem(null,"0","请选择");
        courseComboBox.getItems().addAll(item);
        courseComboBox.getItems().addAll(courseList);

        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }
}