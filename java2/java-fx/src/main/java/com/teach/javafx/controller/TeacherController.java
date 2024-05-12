package com.teach.javafx.controller;

import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import com.teach.javafx.controller.base.MessageDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class TeacherController extends ToolController {
    @FXML
    private TableView<Teacher> dataTableView;  //学生信息表
    @FXML
    private TableColumn<Teacher,String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Teacher,String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<Teacher,String> deptColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<Teacher,String> titleColumn; //学生信息表 专业列
    @FXML
    private TableColumn<Teacher,String> classNameColumn; //学生信息表 班级列
    @FXML
    private TableColumn<Teacher,String> degreeColumn; //学生信息表 课序号列
    @FXML
    private TableColumn<Teacher,String> cardColumn; //学生信息表 证件号码列
    @FXML
    private TableColumn<Teacher,String> genderColumn; //学生信息表 性别列
    @FXML
    private TableColumn<Teacher,String> birthdayColumn; //学生信息表 出生日期列
    @FXML
    private TableColumn<Teacher,String> emailColumn; //学生信息表 邮箱列
    @FXML
    private TableColumn<Teacher,String> phoneColumn; //学生信息表 电话列
    @FXML
    private TableColumn<Teacher,String> addressColumn;//学生信息表 地址列

    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField deptField; //学生信息  院系输入域
    @FXML
    private TextField titleField; //学生信息  专业输入域
    @FXML
    private TextField degreeField; //学生信息  班级输入域
    @FXML
    private TextField cardField; //学生信息  证件号码输入域
    @FXML
    private ComboBox<OptionItem> genderComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker birthdayPick;  //学生信息  出生日期选择域
    @FXML
    private TextField emailField;  //学生信息  邮箱输入域
    @FXML
    private TextField phoneField;   //学生信息  电话输入域
    @FXML
    private TextField addressField;  //学生信息  地址输入域

    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer teacherId = null;  //当前编辑修改的学生的主键

    private List teacherList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<Teacher> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < teacherList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList((Teacher)teacherList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("numName","");
        teacherList = HttpRequestUtil.requestDataList("/api/teacher/getTeacherList",req);
        numColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Teacher, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Teacher, String> cellData) {
                return new SimpleStringProperty(cellData.getValue().getPerson().getNum());
            }
        });
        nameColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getName()));
        deptColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getDept()));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        degreeColumn.setCellValueFactory(new PropertyValueFactory<>("degree"));
        cardColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getCard()));
        genderColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getGenderName()));
        birthdayColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getBirthday()));
        emailColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getEmail()));
        phoneColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getPhone()));
        addressColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getAddress()));
        TableView.TableViewSelectionModel<Teacher> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        genderComboBox.getItems().addAll(genderList);
        birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));


    }

    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        teacherId = null;
        numField.setText("");
        nameField.setText("");
        deptField.setText("");
        titleField.setText("");
        degreeField.setText("");
        cardField.setText("");
        genderComboBox.getSelectionModel().select(-1);
        birthdayPick.getEditor().setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    protected void changeStudentInfo() {
        Teacher teacher = dataTableView.getSelectionModel().getSelectedItem();
        if(teacher == null) {
            clearPanel();
            return;
        }
        teacherId = teacher.getTeacherId();
        DataRequest req = new DataRequest();
        req.add("teacherId",teacherId);
        teacher = (Teacher)HttpRequestUtil.requestDataObject("/api/teacher/getTeacherInfo",req);
        numField.setText(teacher.getPerson().getNum());
        nameField.setText(teacher.getPerson().getName());
        deptField.setText(teacher.getPerson().getDept());
        titleField.setText(teacher.getTitle());
        degreeField.setText(teacher.getDegree());
        cardField.setText(teacher.getPerson().getCard());
        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, teacher.getPerson().getGender()));
        birthdayPick.getEditor().setText(teacher.getPerson().getBirthday());
        emailField.setText(teacher.getPerson().getEmail());
        phoneField.setText(teacher.getPerson().getPhone());
        addressField.setText(teacher.getPerson().getAddress());

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
        req.add("numName",numName);
        teacherList =  (List<Teacher>)HttpRequestUtil.requestDataList("/api/teacher/getTeacherList",req);
        setTableViewData();
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
        Teacher teacher = dataTableView.getSelectionModel().getSelectedItem();
        if(teacher == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        teacherId = teacher.getTeacherId();
        DataRequest req = new DataRequest();
        req.add("teacherId", teacherId);
        DataResponse res = HttpRequestUtil.request("/api/teacher/teacherDelete",req);
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
        Teacher t = new Teacher();
        Person p = new Person();
        t.setPerson(p);
        p.setNum(numField.getText());
        p.setName(nameField.getText());
        p.setDept(deptField.getText());
        t.setTitle(titleField.getText());
        t.setDegree(degreeField.getText());
        p.setCard(cardField.getText());
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            p.setGender(genderComboBox.getSelectionModel().getSelectedItem().getValue());
        p.setBirthday(birthdayPick.getEditor().getText());
        p.setEmail(emailField.getText());
        p.setPhone(phoneField.getText());
        p.setAddress(addressField.getText());
        DataRequest req = new DataRequest();
        req.add("teacherId", teacherId);
        req.add("form", t);
        DataResponse res = HttpRequestUtil.request("/api/teacher/teacherEditSave",req);
        if(res.getCode() == 0) {
            teacherId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }
    public void doDelete(){
        onDeleteButtonClick();
    }


}
