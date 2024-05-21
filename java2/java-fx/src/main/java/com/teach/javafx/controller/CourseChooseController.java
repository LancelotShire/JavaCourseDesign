package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CourseChooseController {
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
    private TableColumn<Map,String> timeColumn;

    private ArrayList<Map> courseChooseList = new ArrayList<>();
    private ObservableList<Map> observableList = FXCollections.observableArrayList();

    @FXML
    private TextField studentTextField;
    @FXML
    private TextField courseTextField;

    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private ComboBox<OptionItem> courseComboBox;

    @FXML
    private DatePicker beginDatePicker;
    String begin;
    @FXML
    private DatePicker endDatePicker;
    String end;

    public static boolean isNumeric(String str) {
        return str.matches("\\d+"); // 使用正则表达式判断是否只包含数字
    }

    @FXML
    private void onQueryButtonClick(){
        Integer studentId = 0;
        Integer courseId = 0;
        String text = studentTextField.getText();
        if(!Objects.equals(text, "")&&isNumeric(text)){
            studentId = Integer.parseInt(text);
            System.out.println(studentId);
        }else if(!Objects.equals(text, "")&&!isNumeric(text)){
            return;
        }
        text = courseTextField.getText();
        if(!Objects.equals(text, "")&&isNumeric(text)){
            courseId = Integer.parseInt(text);
            System.out.println(courseId);
        }else if(!Objects.equals(text, "")&&!isNumeric(text)){
            return;
        }

        DataRequest req = new DataRequest();
        DataResponse res;
        req.add("studentNum",studentId);
        req.add("courseNum",courseId);
        res = HttpRequestUtil.request("/api/courseChoose/getCourseChooseList",req);
        if(res != null && res.getCode()== 0) {
            courseChooseList = (ArrayList<Map>)res.getData();
        }
        System.out.println(courseChooseList);
        updateNameList();
        setTableViewData();
    }

    private void setTableViewData(){
        observableList.clear();
        for (int j = 0; j < courseChooseList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(courseChooseList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    private void initialize(){
        updateNameList();

        studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        classNameColumn.setCellValueFactory(new MapValueFactory<>("className"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("courseNum"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));

        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }

    public void clearPanel(){
        studentComboBox.getSelectionModel().select(-1);
        courseComboBox.getSelectionModel().select(-1);
    }

    @FXML
    private void onSaveButtonClick(){
        OptionItem opStudent = studentComboBox.getSelectionModel().getSelectedItem();
        OptionItem opCourse = courseComboBox.getSelectionModel().getSelectedItem();
        if(opStudent == null||opCourse == null){
            MessageDialog.showDialog("没有选课或没有选择学生，无法添加！");
            return;
        }
        Integer studentId = opStudent.getId();
        Integer courseId = opCourse.getId();
        DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);

        DataResponse res = HttpRequestUtil.request("/api/courseChoose/courseChooseSave",req);

        if(res != null&&res.getCode() == 0){
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }else if(res != null){
            MessageDialog.showDialog("提交失败！"+res.getMsg());
        }else {
            MessageDialog.showDialog("提交失败！");
        }
        updateNameList();
    }

    @FXML
    private void onDeleteButtonClick(){
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if (ret != MessageDialog.CHOICE_YES) {
            return;
        }
        Integer courseChooseId = CommonMethod.getInteger(form, "courseChooseId");
        DataRequest req = new DataRequest();
        req.add("courseChooseId", courseChooseId);
        DataResponse res = HttpRequestUtil.request("/api/courseChoose/courseChooseDelete", req);
        if (res != null && res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        updateNameList();
    }

    @FXML
    private void onAddButtonClick(){
        clearPanel();
    }

    private void updateNameList(){
        List<OptionItem> studentList;
        List<OptionItem> courseList;
        studentComboBox.getItems().clear();
        courseComboBox.getItems().clear();
        DataRequest req = new DataRequest();
        studentList = HttpRequestUtil.requestOptionItemList("/api/score/getStudentItemOptionList",req);
        System.out.println(studentList);
        studentComboBox.getItems().addAll(studentList);
        courseList = HttpRequestUtil.requestOptionItemList("/api/score/getCourseItemOptionList",req);
        courseComboBox.getItems().addAll(courseList);
    }

    @FXML
    private void onSaveTimeButtonClick(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        begin = beginDatePicker.getValue().format(dateTimeFormatter);
        end = endDatePicker.getValue().format(dateTimeFormatter);
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        DataRequest req = new DataRequest();
        req.add("time",map);
        DataResponse res = HttpRequestUtil.request("/api/selectTime/getSelectTime",req);
        if(res != null&&res.getCode() == 0){
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog("提交失败！");
        }
        updateNameList();
    }

}
