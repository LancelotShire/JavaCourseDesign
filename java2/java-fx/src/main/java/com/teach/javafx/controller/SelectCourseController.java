package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectCourseController {
    public TextField numNameTextField;
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map, String> numColumn;
    @FXML
    private TableColumn<Map, String> nameColumn;
    @FXML
    private TableColumn<Map, String> creditColumn;
    @FXML
    private TableColumn<Map, String> preCourseColumn;
    @FXML
    private TableColumn<Map, String> timeColumn;
    @FXML
    private TableColumn<Map, FlowPane> operateColumn;

    private List<Map> courseList = new ArrayList();
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    @FXML
    public void initialize() {
        numColumn.setCellValueFactory(new MapValueFactory("num"));
        numColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        nameColumn.setCellValueFactory(new MapValueFactory("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        creditColumn.setCellValueFactory(new MapValueFactory("credit"));
        creditColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        preCourseColumn.setCellValueFactory(new MapValueFactory("preCourse"));
        preCourseColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        timeColumn.setCellValueFactory(new MapValueFactory("time"));
        timeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        operateColumn.setCellValueFactory(new MapValueFactory("operate"));
        dataTableView.setEditable(false);

        onAllCourseButtonClick();
    }

    @FXML
    private void onQueryButtonClick() {
        DataResponse res;

        DataRequest req =new DataRequest();

        String numName = numNameTextField.getText();
        req.add("numName", numName);

        res = HttpRequestUtil.request("/api/course/getCourseList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            courseList = (ArrayList<Map>)res.getData();
        }
//        /* 删除课程号为null的记录*/
//        courseList.removeIf(map -> map.get("name") == null);

        /* 设置时间为空 */
        for(Map map:courseList) {
            map.put("time", "");
        }

        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        FlowPane flowPane;
        Button selectButton;
        Button withdrawButton;
        for(int j = 0; j < courseList.size(); ++j) {
            map = courseList.get(j);

            flowPane = new FlowPane();
            flowPane.setHgap(10);
            flowPane.setAlignment(Pos.CENTER);

            selectButton = new Button("选课");
            selectButton.setId("select" + j);
            selectButton.setOnAction(e -> {
                selectItem(((Button)e.getSource()).getId());
            });

            withdrawButton = new Button("退课");
            withdrawButton.setId("withdraw" + j);
            withdrawButton.setOnAction(e -> {
                withdrawItem(((Button)e.getSource()).getId());
            });

            flowPane.getChildren().addAll(selectButton, withdrawButton);
            map.put("operate", flowPane);
            observableList.addAll(FXCollections.observableArrayList(map));
        }

        dataTableView.setItems(observableList);
    }

    private void selectItem(String name) {
        if(name == null)
            return;
        DataResponse res;
        DataRequest req =new DataRequest();
        res = HttpRequestUtil.request("/api/selectTime/getSelectPermission",req);

        if(res != null && res.getCode() == 0) {
            String permission = (String) res.getData();
            if(permission.equals("0")) {
                MessageDialog.showDialog("不在选/退课开放时间段内");
                return;
            }
        }


        int j = Integer.parseInt(name.substring(6));
        Map data = courseList.get(j);

        System.out.println("选课数据" + data);

        DataRequest req1 = new DataRequest();
        req1.add("courseNum", data.get("num").toString());
        res = HttpRequestUtil.request("/api/courseChoose/selectSave", req1);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("选课成功！");
            setTableViewData();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }

    }

    private void withdrawItem(String name) {
        if(name == null)
            return;
        DataResponse res;
        DataRequest req =new DataRequest();
        res = HttpRequestUtil.request("/api/selectTime/getSelectPermission",req);

        if(res != null && res.getCode() == 0) {
            String permission = (String) res.getData();
            if(permission.equals("0")) {
                MessageDialog.showDialog("不在选/退课开放时间段内");
                return;
            }
        }

        int j = Integer.parseInt(name.substring(8));
        Map data = courseList.get(j);

        System.out.println("withdraw:" + data);

        DataRequest req1 = new DataRequest();
        req1.add("courseNum", data.get("num"));
        DataResponse res1 = HttpRequestUtil.request("/api/courseChoose/withdrawCourse", req1);

        if(res1.getCode() == 0) {
            MessageDialog.showDialog("退课成功！");
            setTableViewData();
        }
        else {
            MessageDialog.showDialog(res1.getMsg());
        }
        onAllCourseButtonClick();
    }

    public void onAllCourseButtonClick() {
        DataResponse res;
        DataRequest req =new DataRequest();
        res = HttpRequestUtil.request("/api/course/getCourseList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            courseList = (ArrayList<Map>)res.getData();
        }
//        /* 删除课程号为null的记录*/
//        courseList.removeIf(map -> map.get("name") == null);

        setTableViewData();
    }

    @FXML
    public void onSelectedCourseButtonClick() {
        DataResponse res;
        DataRequest req =new DataRequest();
        res = HttpRequestUtil.request("/api/courseChoose/getSelectedCourse",req);

        if(res.getCode() == 0) {
            courseList = (List<Map>) res.getData();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }

        setTableViewData();
    }


}