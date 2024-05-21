package com.teach.javafx.controller;

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
import javafx.stage.FileChooser;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentActivityController extends ToolController {
    @FXML
    private TableView<Map> dataTableView;  //信息
    @FXML
    private TableColumn<Map,String> numColumn;   //信息表 编号列
    @FXML
    private TableColumn<Map,String> nameColumn; //信息表 名称列
    @FXML
    private TableColumn<Map,String> activityNameColumn;//名称
    @FXML
    private TableColumn<Map,String> activityTypeColumn;
    @FXML
    private TableColumn<Map,String> timeColumn;//
    @FXML
    private TableColumn<Map,String> activityGradeColumn;
    @FXML
    private TableColumn<Map,String> timeOfActivityColumn;

    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField activityNameField;  //学生信息   活动名称
    @FXML
    private TextField timeField;
    @FXML
    private ComboBox<OptionItem> activityGradeComboBox;

    @FXML
    private ComboBox<OptionItem> activityTypeComboBox;
    @FXML
    private DatePicker timeOfActivityPick;
    @FXML
    private TextField numNameTextField;  //查询 输入域

    private Integer activityId = null;  //当前编辑修改的主键
    private ArrayList<Map> activityList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> activityGradeList;   //等级选择列表数据
    private List<OptionItem> activityTypeList;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    /**
     *
     * 将数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < activityList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(activityList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    //用于在页面上显示
    @FXML
    public void initialize() {
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("numName","");
        res = HttpRequestUtil.request("/api/activity/getStudentActivityList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            activityList = (ArrayList<Map>)res.getData();
        }
        numColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        activityNameColumn.setCellValueFactory(new MapValueFactory<>("activityName"));
        activityTypeColumn.setCellValueFactory(new MapValueFactory<>("activityType"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        activityGradeColumn.setCellValueFactory(new MapValueFactory<>("activityGrade"));
        timeOfActivityColumn.setCellValueFactory(new MapValueFactory<>("timeOfActivity"));

        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        activityGradeList = HttpRequestUtil.getDictionaryOptionItemList("ActivityGrade");
        activityGradeComboBox.getItems().addAll(activityGradeList);
        activityTypeList = HttpRequestUtil.getDictionaryOptionItemList("ActivityType");
        activityTypeComboBox.getItems().addAll(activityTypeList);

        timeOfActivityPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));

    }

    /**
     * 清除表单中输入信息
     */
    public void clearPanel(){
        activityId = null;
        numField.setText("");
        nameField.setText("");
        activityNameField.setText("");
        timeField.setText("");
        activityGradeComboBox.getSelectionModel().select(-1);
        activityTypeComboBox.getSelectionModel().select(-1);
        timeOfActivityPick.getEditor().setText("");
    }
    //更改信息
    protected void changeActivityInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        activityId = CommonMethod.getInteger(form,"activityId");
        DataRequest req = new DataRequest();
        req.add("activityId",activityId);
        DataResponse res = HttpRequestUtil.request("/api/activity/getStudentActivityInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map)res.getData();  //从map取出值放入表单里面
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        activityNameField.setText(CommonMethod.getString(form, "activityName"));
        timeField.setText(CommonMethod.getString(form, "time"));
        activityGradeComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(activityGradeList, CommonMethod.getString(form, "activityGrade")));
        activityTypeComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(activityTypeList, CommonMethod.getString(form, "activityType")));
        timeOfActivityPick.getEditor().setText(CommonMethod.getString(form, "timeOfActivity"));
    }
    /**
     * 点击学生列表的某一行，根据Id ,从后台查询的基本信息，切换编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeActivityInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的在列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        DataResponse res = HttpRequestUtil.request("/api/activity/getStudentActivityList",req);
        if(res != null && res.getCode()== 0) {
            activityList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }
    }
    //关键字查询按钮

    /**
     *  添加
     */
    @FXML
    protected void onAddButtonClick() {
        clearPanel();

    }
    /**
     * 点击删除按钮 删除
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
        //后端数据库中根据achievementId删除信息
        DataResponse res = HttpRequestUtil.request("/api/activity/activityDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 点击保存按钮，保存
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
        form.put("activityName",activityNameField.getText());
        form.put("time",timeField.getText());
        if(activityGradeComboBox.getSelectionModel() != null && activityGradeComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("activityGrade",activityGradeComboBox.getSelectionModel().getSelectedItem().getValue());
        if(activityTypeComboBox.getSelectionModel() != null && activityTypeComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("activityType",activityTypeComboBox.getSelectionModel().getSelectedItem().getValue());

        form.put("timeOfActivity",timeOfActivityPick.getEditor().getText());

        DataRequest req = new DataRequest();
        req.add("activityId", activityId);
        req.add("form", form);
        DataResponse res = HttpRequestUtil.request("/api/activity/activityStudentEditSave",req);
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
    public void doExport(){
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/activity/getActivityListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("前选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                File file = fileDialog.showSaveDialog(null);
                if(file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    @FXML
    protected void onImportButtonClick() {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择学生数据表");
        fileDialog.setInitialDirectory(new File("D:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        String paras = "";
        DataResponse res =HttpRequestUtil.importData("/api/term/importActivityData",file.getPath(),paras);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

}

