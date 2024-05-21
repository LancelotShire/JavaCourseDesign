package com.teach.javafx.controller;

import com.teach.javafx.AppStore;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
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
import java.util.List;
import java.util.Map;


public class ActivitController extends ToolController {
    @FXML
    private TableView<Map> dataTableView;  //学生信息表
    @FXML
    private TableColumn<Map,String> thingColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Map,String> nameColumn;
    @FXML
    private TableColumn<Map,String> numColumn;
    @FXML
    private TableColumn<Map,String> windateColumn; //学生信息表 名称列
    @FXML
    private TableColumn<Map,String> somewordColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<Map,String> modelColumn; //学生信息表 出生日期列



    @FXML
    private Button shanchu;
    @FXML
    private Button chaxun;
    @FXML
    private Label biaoqian;









    @FXML
    private TextField thingField; //学生信息  学号输入域
    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField; //学生信息  学号输入域

    @FXML
    private TextField somewordField;
    @FXML
    private ComboBox<OptionItem> modelComboBox;
    @FXML
    private DatePicker windatePick;  //学生信息  出生日期选择域




    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer activityId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> activityList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> modelList;

    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表


    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < activityList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(activityList.get(j)));
        }
        dataTableView.setItems(observableList);
    }





    @FXML
    protected void onShuaxinButtonClick(){
        DataResponse res;


        DataRequest req =new DataRequest();

        if(AppStore.getJwt().getRoles().equals("ROLE_ADMIN"))
            req.add("numName","");
        else
            req.add("numName",AppStore.getJwt().getUsername());
        res = HttpRequestUtil.request("/api/activit/getActivityList",req); //从后台获取所有学生信息列表集合


        if(res != null && res.getCode()== 0) {
            activityList = (ArrayList<Map>)res.getData();
        }

        if(AppStore.getJwt().getRoles().equals("ROLE_STUDENT")){
            shanchu.setVisible(false);
            biaoqian.setVisible(false);
            chaxun.setVisible(false);
            numNameTextField.setVisible(false);
        }
        thingColumn.setCellValueFactory(new MapValueFactory<>("thing"));  //设置列值工程属性
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        somewordColumn.setCellValueFactory(new MapValueFactory<>("someword"));
        modelColumn.setCellValueFactory(new MapValueFactory<>("model"));
        windateColumn.setCellValueFactory(new MapValueFactory<>("windate"));

        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        modelList = HttpRequestUtil.getDictionaryOptionItemList("MOD");

        windatePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));}







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
        res = HttpRequestUtil.request("/api/activit/getActivityList",req); //从后台获取所有学生信息列表集合


        if(res != null && res.getCode()== 0) {
            activityList = (ArrayList<Map>)res.getData();
        }

        if(AppStore.getJwt().getRoles().equals("ROLE_STUDENT")){
            shanchu.setVisible(false);
            biaoqian.setVisible(false);
            chaxun.setVisible(false);
            numNameTextField.setVisible(false);
        }
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));  //设置列值工程属性

        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        thingColumn.setCellValueFactory(new MapValueFactory<>("thing"));
        windateColumn.setCellValueFactory(new MapValueFactory<>("windate"));
        somewordColumn.setCellValueFactory(new MapValueFactory<>("someword"));
        modelColumn.setCellValueFactory(new MapValueFactory<>("model"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        modelList = HttpRequestUtil.getDictionaryOptionItemList("GAD");
        modelComboBox.getItems().addAll(modelList);
        windatePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }

    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        activityId = null;
        numField.setText("");
        nameField.setText("");
        thingField.setText("");
        somewordField.setText("");
        windatePick.getEditor().setText("");
        modelComboBox.getSelectionModel().select(-1);
    }

    protected void changeStudentInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        activityId = CommonMethod.getInteger(form,"studentId");
        DataRequest req = new DataRequest();
        req.add("activityId",activityId);
        DataResponse res = HttpRequestUtil.request("/api/activit/getactivityInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map)res.getData();
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        thingField.setText(CommonMethod.getString(form, "thing"));
        somewordField.setText(CommonMethod.getString(form, "someword"));
        windatePick.getEditor().setText(CommonMethod.getString(form, "windate"));
        modelComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(modelList, CommonMethod.getString(form, "model")));

    }
    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeStudentInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();

        DataResponse res;

        if(AppStore.getJwt().getRoles().equals("ROLE_STUDENT"))
            req.add("numName",AppStore.getJwt().getUsername());
        else
        if(numName==null){
            req.add("numName","");}
        else{ req.add("numName",numName);}




        res = HttpRequestUtil.request("/api/activit/getActivityList",req); //从后台获取所有学生信息列表集合


        if(res != null && res.getCode()== 0) {
            activityList = (ArrayList<Map>)res.getData();
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
        activityId = CommonMethod.getInteger(form,"activityId");
        DataRequest req = new DataRequest();
        req.add("activityId", activityId);
        DataResponse res = HttpRequestUtil.request("/api/activit/ActivityDelete",req);
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
        form.put("thing",thingField.getText());
        form.put("someword",somewordField.getText());
        form.put("windate",windatePick.getEditor().getText());
        if(modelComboBox.getSelectionModel() != null && modelComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("model",modelComboBox.getSelectionModel().getSelectedItem().getValue());
        DataRequest req = new DataRequest();
        req.add("activityId", activityId);
        req.add("form", form);
        DataResponse res = HttpRequestUtil.request("/api/activit/activitySave",req);
        if(res.getCode() == 0) {
            activityId = CommonMethod.getIntegerFromObject(res.getData());
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


