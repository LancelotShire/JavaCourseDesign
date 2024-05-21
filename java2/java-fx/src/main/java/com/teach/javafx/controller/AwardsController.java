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

public class AwardsController extends ToolController {

    @FXML
    private TableView<Map> dataTableView;  //信息
    @FXML
    private TableColumn<Map,String> numColumn;   //信息表 编号列
    @FXML
    private TableColumn<Map,String> nameColumn; //信息表 名称列
    @FXML
    private TableColumn<Map,String> awardsNameColumn;//活动名称
    @FXML
    private TableColumn<Map,String> awardsTypeColumn;//类型
    @FXML
    private TableColumn<Map,String> timeColumn;//志愿时长
    @FXML
    private TableColumn<Map,String> timeOfAwardsColumn;//活动时间

    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField awardsNameField;
    @FXML
    private TextField timeField;

    @FXML
    private ComboBox<OptionItem> awardsTypeComboBox;
    @FXML
    private DatePicker timeOfAwardsPick;
    @FXML
    private TextField numNameTextField;  //查询输入域

    private Integer awardsId = null;  //当前编辑修改的主键
    private ArrayList<Map> awardsList = new ArrayList();  // 学生信息列表数据//等级选择列表数据
    private List<OptionItem> awardsTypeList;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    /**
     *
     * 将数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < awardsList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(awardsList.get(j)));
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
        res = HttpRequestUtil.request("/api/awards/getAwardsList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            awardsList = (ArrayList<Map>)res.getData();
        }
        numColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        awardsNameColumn.setCellValueFactory(new MapValueFactory<>("awardsName"));
        awardsTypeColumn.setCellValueFactory(new MapValueFactory<>("awardsType"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        timeOfAwardsColumn.setCellValueFactory(new MapValueFactory<>("timeOfAwards"));

        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        awardsTypeList = HttpRequestUtil.getDictionaryOptionItemList("AwardsType");
        awardsTypeComboBox.getItems().addAll(awardsTypeList);

        timeOfAwardsPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));

    }

    /**
     * 清除表单中输入信息
     */
    public void clearPanel(){
        awardsId = null;
        numField.setText("");
        nameField.setText("");
        awardsNameField.setText("");

        timeField.setText("");
        awardsTypeComboBox.getSelectionModel().select(-1);
        timeOfAwardsPick.getEditor().setText("");
    }
    //更改信息
    protected void changeActivityInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        awardsId = CommonMethod.getInteger(form,"awardsId");
        DataRequest req = new DataRequest();
        req.add("awardsId",awardsId);
        DataResponse res = HttpRequestUtil.request("/api/awards/getAwardsInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map)res.getData();  //从map取出值放入表单里面
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        awardsNameField.setText(CommonMethod.getString(form, "awardsName"));
        timeField.setText(CommonMethod.getString(form, "time"));
        awardsTypeComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(awardsTypeList, CommonMethod.getString(form, "awardsType")));
        timeOfAwardsPick.getEditor().setText(CommonMethod.getString(form, "timeOfAwards"));
    }
    /**
     * 点击学生列表的某一行，根据Id ,从后台查询荣誉的基本信息，切换学生的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeActivityInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配在列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        DataResponse res = HttpRequestUtil.request("/api/awards/getAwardsList",req);
        if(res != null && res.getCode()== 0) {
            awardsList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }

    }

    /**
     *  添加， 清空输入信息， 输入相关信息，点击保存即可添加
     */
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }
    /**
     * 点击删除按钮 删除当前编辑的数据
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
        awardsId = CommonMethod.getInteger(form,"awardsId");
        DataRequest req = new DataRequest();
        req.add("awardsId", awardsId);
        //后端数据库中根据Id删除信息
        DataResponse res = HttpRequestUtil.request("/api/awards/awardsDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 点击保存按钮，保存当前编辑信息
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
        form.put("awardsName",awardsNameField.getText());
        form.put("time",timeField.getText());
        if(awardsTypeComboBox.getSelectionModel() != null && awardsTypeComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("awardsType",awardsTypeComboBox.getSelectionModel().getSelectedItem().getValue());

        form.put("timeOfAwards",timeOfAwardsPick.getEditor().getText());

        DataRequest req = new DataRequest();
        req.add("awardsId", awardsId);
        req.add("form", form);
        DataResponse res = HttpRequestUtil.request("/api/awards/awardsEditSave",req);
        if(res.getCode() == 0) {
            awardsId = CommonMethod.getIntegerFromObject(res.getData());
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
        //  String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("name","");
        byte[] bytes = HttpRequestUtil.requestByteData("/api/awards/getAwardsListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("前选择保存的文件");
                fileDialog.setInitialDirectory(new File("D:/"));
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
        DataResponse res =HttpRequestUtil.importData("/api/term/importAwardsData",file.getPath(),paras);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

}

