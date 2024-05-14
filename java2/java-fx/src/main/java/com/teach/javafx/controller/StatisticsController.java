package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Integer studentId = 0;
        Integer courseId = 0;
        OptionItem op;
        op = nameComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        res = HttpRequestUtil.request("/api/score/getScoreList",req);
        if(res != null && res.getCode()== 0) {
            scoreList = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
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
        OptionItem item = new OptionItem(null,"0","请选择");
        nameComboBox.getItems().addAll(item);
        nameComboBox.getItems().addAll(nameList);

        studentNumColumn.setCellValueFactory(new MapValueFactory<>("studentNum"));
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        classNameColumn.setCellValueFactory(new MapValueFactory<>("className"));
        averageScoreColumn.setCellValueFactory(new MapValueFactory<>("averageScore"));
        goColumn.setCellValueFactory(new MapValueFactory<>("go"));
        hyakuColumn.setCellValueFactory(new MapValueFactory<>("hyaku"));
        totalCreditColumn.setCellValueFactory(new MapValueFactory<>("totalCredit"));


    }

}
