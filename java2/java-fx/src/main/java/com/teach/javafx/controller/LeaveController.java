package com.teach.javafx.controller;

import com.teach.javafx.AppStore;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaveController extends ToolController {
    @FXML
    private TableView<Map> dataTableView;  //学生信息表
    @FXML
    private TableColumn<Map,String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Map,String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<Map,String> reasonColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<Map,String> starttimeColumn; //学生信息表 出生日期列
    @FXML
    private TableColumn<Map,String> endtimeColumn; //学生信息表 邮箱列



    @FXML
    private Button shanchu;
    @FXML
    private Button chaxun;
    @FXML
    private Label biaoqian;









    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField reasonField; //学生信息  院系输入域

    @FXML
    private DatePicker starttimePick;  //学生信息  出生日期选择域

    @FXML
    private DatePicker endtimePick;  //学生信息  出生日期选择域


    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer leaveId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> leaveList = new ArrayList();  // 学生信息列表数据

    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表


    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < leaveList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(leaveList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {

        DataResponse res;


        DataRequest req =new DataRequest();

        if(AppStore.getJwt().getRoles().equals("ROLE_ADMIN"))
            req.add("numName","");
        else
            req.add("numName",AppStore.getJwt().getUsername());
        res = HttpRequestUtil.request("/api/leave/getLeaveList",req); //从后台获取所有学生信息列表集合


        if(res != null && res.getCode()== 0) {
            leaveList = (ArrayList<Map>)res.getData();
        }

        if(AppStore.getJwt().getRoles().equals("ROLE_STUDENT")){
            shanchu.setVisible(false);
            biaoqian.setVisible(false);
            chaxun.setVisible(false);
            numNameTextField.setVisible(false);
        }
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));  //设置列值工程属性

        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        reasonColumn.setCellValueFactory(new MapValueFactory<>("reason"));
        starttimeColumn.setCellValueFactory(new MapValueFactory<>("starttime"));
        endtimeColumn.setCellValueFactory(new MapValueFactory<>("endtime"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        starttimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        endtimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }

    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        leaveId = null;
        numField.setText("");
        nameField.setText("");
        reasonField.setText("");
        starttimePick.getEditor().setText("");
        endtimePick.getEditor().setText("");
    }

    protected void changeStudentInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        leaveId = CommonMethod.getInteger(form,"studentId");
        DataRequest req = new DataRequest();
        req.add("leaveId",leaveId);
        DataResponse res = HttpRequestUtil.request("/api/leave/getleaveInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map)res.getData();
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        reasonField.setText(CommonMethod.getString(form, "reason"));
        starttimePick.getEditor().setText(CommonMethod.getString(form, "starttime"));
        endtimePick.getEditor().setText(CommonMethod.getString(form, "endtime"));

    }
    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeStudentInfo();
    }



    @FXML
    protected void onshuaxinButtonClick(){
        DataResponse res;


        DataRequest req =new DataRequest();

        if(AppStore.getJwt().getRoles().equals("ROLE_ADMIN"))
            req.add("numName","");
        else
            req.add("numName",AppStore.getJwt().getUsername());
        res = HttpRequestUtil.request("/api/leave/getLeaveList",req); //从后台获取所有学生信息列表集合


        if(res != null && res.getCode()== 0) {
            leaveList = (ArrayList<Map>)res.getData();
        }

        if(AppStore.getJwt().getRoles().equals("ROLE_STUDENT")){
            shanchu.setVisible(false);
            biaoqian.setVisible(false);
            chaxun.setVisible(false);
            numNameTextField.setVisible(false);
        }
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));  //设置列值工程属性

        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        reasonColumn.setCellValueFactory(new MapValueFactory<>("reason"));
        starttimeColumn.setCellValueFactory(new MapValueFactory<>("starttime"));
        endtimeColumn.setCellValueFactory(new MapValueFactory<>("endtime"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        starttimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        endtimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }







    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();

        DataResponse res;





            req.add("numName",numName);
        res = HttpRequestUtil.request("/api/leave/getLeaveList",req); //从后台获取所有学生信息列表集合


        if(res != null && res.getCode()== 0) {
           leaveList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }

    /**
     * 点击删除按钮 删除当前编辑的学生的数据
     */
    @FXML
    protected void onDeleteButtonClick() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        leaveId = CommonMethod.getInteger(form,"leaveId");
        DataRequest req = new DataRequest();
        req.add("leaveId", leaveId);
        DataResponse res = HttpRequestUtil.request("/api/leave/leaveDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {
        if( numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Map form = new HashMap();
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        form.put("reason",reasonField.getText());
        form.put("starttime",starttimePick.getEditor().getText());
        form.put("endtime",endtimePick.getEditor().getText());
        DataRequest req = new DataRequest();
        req.add("leaveId", leaveId);
        req.add("form", form);
        DataResponse res = HttpRequestUtil.request("/api/leave/leaveSave",req);
        if(res.getCode() == 0) {
            leaveId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对学生的增，删，改操作
     */
    public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }
    public void doDelete(){
        onDeleteButtonClick();
    }

    /**
     * 导出学生信息表的示例 重写ToolController 中的doExport 这里给出了一个导出学生基本信息到Excl表的示例， 后台生成Excl文件数据，传回前台，前台将文件保存到本地
     */






}

