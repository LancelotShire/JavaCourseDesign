package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *
 * @FXML 属性 对应fxml文件中的
 * @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class StudentController extends ToolController {
    @FXML
    private TableView<Map> dataTableView;  //学生信息表
    @FXML
    private TableColumn<Map, String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Map, String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<Map, String> deptColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<Map, String> majorColumn; //学生信息表 专业列
    @FXML
    private TableColumn<Map, String> classNameColumn; //学生信息表 班级列
    @FXML
    private TableColumn<Map, String> cardColumn; //学生信息表 证件号码列
    @FXML
    private TableColumn<Map, String> genderColumn; //学生信息表 性别列
    @FXML
    private TableColumn<Map, String> birthdayColumn; //学生信息表 出生日期列
    @FXML
    private TableColumn<Map, String> emailColumn; //学生信息表 邮箱列
    @FXML
    private TableColumn<Map, String> phoneColumn; //学生信息表 电话列
    @FXML
    private TableColumn<Map, String> addressColumn;//学生信息表 地址列

    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField deptField; //学生信息  院系输入域
    @FXML
    private TextField majorField; //学生信息  专业输入域
    @FXML
    private TextField classNameField; //学生信息  班级输入域
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

    private Integer studentId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> studentList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表


    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < studentList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(studentList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        DataResponse res;
        DataRequest req = new DataRequest();
        req.add("numName", "");
        res = HttpRequestUtil.request("/api/student/getStudentList", req); //从后台获取所有学生信息列表集合
        if (res != null && res.getCode() == 0) {
            studentList = (ArrayList<Map>) res.getData();
        }
        numColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        deptColumn.setCellValueFactory(new MapValueFactory<>("dept"));
        majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
        classNameColumn.setCellValueFactory(new MapValueFactory<>("className"));
        cardColumn.setCellValueFactory(new MapValueFactory<>("card"));
        genderColumn.setCellValueFactory(new MapValueFactory<>("genderName"));
        birthdayColumn.setCellValueFactory(new MapValueFactory<>("birthday"));
        emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new MapValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new MapValueFactory<>("address"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");

        genderComboBox.getItems().addAll(genderList);
        birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }

    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel() {
        studentId = null;
        numField.setText("");
        nameField.setText("");
        deptField.setText("");
        majorField.setText("");
        classNameField.setText("");
        cardField.setText("");
        genderComboBox.getSelectionModel().select(-1);
        birthdayPick.getEditor().setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    protected void changeStudentInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            clearPanel();
            return;
        }
        studentId = CommonMethod.getInteger(form, "studentId");
        DataRequest req = new DataRequest();
        req.add("studentId", studentId);
        DataResponse res = HttpRequestUtil.request("/api/student/getStudentInfo", req);
        if (res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map) res.getData();
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        deptField.setText(CommonMethod.getString(form, "dept"));
        majorField.setText(CommonMethod.getString(form, "major"));
        classNameField.setText(CommonMethod.getString(form, "className"));
        cardField.setText(CommonMethod.getString(form, "card"));
        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, CommonMethod.getString(form, "gender")));
        birthdayPick.getEditor().setText(CommonMethod.getString(form, "birthday"));
        emailField.setText(CommonMethod.getString(form, "email"));
        phoneField.setText(CommonMethod.getString(form, "phone"));
        addressField.setText(CommonMethod.getString(form, "address"));

    }

    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change) {
        changeStudentInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName", numName);
        DataResponse res = HttpRequestUtil.request("/api/student/getStudentList", req);
        if (res != null && res.getCode() == 0) {
            studentList = (ArrayList<Map>) res.getData();
            setTableViewData();
        }
    }


    /**
     * 添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
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
        if (form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if (ret != MessageDialog.CHOICE_YES) {
            return;
        }
        studentId = CommonMethod.getInteger(form, "studentId");
        DataRequest req = new DataRequest();
        req.add("studentId", studentId);
        DataResponse res = HttpRequestUtil.request("/api/student/studentDelete", req);
        if (res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        } else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /**
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {
        if (numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Map form = new HashMap();
        form.put("num", numField.getText());
        form.put("name", nameField.getText());
        form.put("dept", deptField.getText());
        form.put("major", majorField.getText());
        form.put("className", classNameField.getText());
        form.put("card", cardField.getText());
        if (genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("gender", genderComboBox.getSelectionModel().getSelectedItem().getValue());
        form.put("birthday", birthdayPick.getEditor().getText());
        form.put("email", emailField.getText());
        form.put("phone", phoneField.getText());
        form.put("address", addressField.getText());
        DataRequest req = new DataRequest();
        req.add("studentId", studentId);
        req.add("form", form);
        DataResponse res = HttpRequestUtil.request("/api/student/studentEditSave", req);
        if (res.getCode() == 0) {
            studentId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        } else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对学生的增，删，改操作
     */
    public void doNew() {
        clearPanel();
    }

    public void doSave() {
        onSaveButtonClick();
    }

    public void doDelete() {
        onDeleteButtonClick();
    }

    /**
     * 导出学生信息表的示例 重写ToolController 中的doExport 这里给出了一个导出学生基本信息到Excl表的示例， 后台生成Excl文件数据，传回前台，前台将文件保存到本地
     */
    public void doExport() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName", numName);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getStudentListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("前选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                File file = fileDialog.showSaveDialog(null);
                if (file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            } catch (Exception e) {
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
        DataResponse res = HttpRequestUtil.importData("/api/term/importStudentData", file.getPath(), paras);
        if (res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        } else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    @FXML
    protected void onFamilyButtonClick() {
        DataRequest req = new DataRequest();
        req.add("studentId", studentId);
        DataResponse res = HttpRequestUtil.request("/api/student/getFamilyMemberList", req);
        if (res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        List<Map> familyList = (List<Map>) res.getData();
        ObservableList<Map> oList = FXCollections.observableArrayList(familyList);

        Scene scene = null, pScene = null;
        Stage stage;
        stage = new Stage();
        TableView<Map> table = new TableView<>(oList);
        table.setEditable(true);

        TableColumn<Map, String> relationColumn = new TableColumn<>("关系");
        relationColumn.setCellValueFactory(new MapValueFactory("relation"));
        relationColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        relationColumn.setOnEditCommit(event -> {
            TableView tempTable = event.getTableView();
            Map tempEntity = (Map) tempTable.getItems().get(event.getTablePosition().getRow());
            tempEntity.put("relation",event.getNewValue());
        });
        table.getColumns().add(relationColumn);

        TableColumn<Map, String> nameColumn = new TableColumn<>("姓名");
        nameColumn.setCellValueFactory(new MapValueFactory("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            TableView tempTable = event.getTableView();
            Map tempEntity = (Map) tempTable.getItems().get(event.getTablePosition().getRow());
            tempEntity.put("name",event.getNewValue());
        });
        table.getColumns().add(nameColumn);

        TableColumn<Map, String> genderColumn = new TableColumn<>("性别");
        genderColumn.setCellValueFactory(new MapValueFactory("gender"));
        genderColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        genderColumn.setOnEditCommit(event -> {
            TableView tempTable = event.getTableView();
            Map tempEntity = (Map) tempTable.getItems().get(event.getTablePosition().getRow());
            tempEntity.put("gender",event.getNewValue());
        });
        table.getColumns().add(genderColumn);

        TableColumn<Map, String> ageColumn = new TableColumn<>("年龄");
        ageColumn.setCellValueFactory(new MapValueFactory("age"));
        ageColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        ageColumn.setOnEditCommit(event -> {
            TableView tempTable = event.getTableView();
            Map tempEntity = (Map) tempTable.getItems().get(event.getTablePosition().getRow());
            tempEntity.put("age",event.getNewValue());
        });
        table.getColumns().add(ageColumn);

        TableColumn<Map, String> unitColumn = new TableColumn<>("单位");
        unitColumn.setCellValueFactory(new MapValueFactory("unit"));
        unitColumn.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        unitColumn.setOnEditCommit(event -> {
            TableView tempTable = event.getTableView();
            Map tempEntity = (Map) tempTable.getItems().get(event.getTablePosition().getRow());
            tempEntity.put("unit",event.getNewValue());
        });
        table.getColumns().add(unitColumn);

        BorderPane root = new BorderPane();
        FlowPane flowPane = new FlowPane();
        Button obButton = new Button("确定");
        obButton.setOnAction(event -> {
            for(Map map: table.getItems()) {
                System.out.println("map:"+map);

                if(map.get("relation") == null || map.get("name") == null ||
                        map.get("gender") == null || map.get("age") == null || map.get("unit") == null) {
                    MessageDialog.showDialog("字段不能为空!");
                    onQueryButtonClick();
                    break;
                }

                System.out.println("uploading...");
                DataRequest req1 = new DataRequest();
                req1.add("studentId", map.get("studentId"));
                req1.add("memberId", map.get("memberId"));
                req1.add("relation", map.get("relation"));
                req1.add("name", map.get("name"));
                req1.add("gender", map.get("gender"));
                req1.add("age", map.get("age"));
                req1.add("unit", map.get("unit"));
                DataResponse res1 = HttpRequestUtil.request("/api/student/familyMemberSave", req1);
                if (res.getCode() == 0) {
                    studentId = CommonMethod.getIntegerFromObject(res.getData());
                    MessageDialog.showDialog("提交成功！");
                    onQueryButtonClick();
                } else {
                    MessageDialog.showDialog(res.getMsg());
                }
            }
            stage.close();
        });
        flowPane.getChildren().add(obButton);

        /* 为两个按钮之间添加空白 */
        Region spacer = new Region();
        spacer.setPrefWidth(10);
        flowPane.getChildren().add(spacer);

        // 添加新成员
        Button addButton = new Button("添加");
        addButton.setOnAction(event -> {
            Map newEntry = new HashMap<>(); // 创建一个新的空数据行
            newEntry.put("studentId", studentId);
            newEntry.put("memberId", null);
            newEntry.put("relation", "新成员"); // 设置关系字段为空
            newEntry.put("name", null); // 设置姓名字段为空
            newEntry.put("gender", null); // 设置性别字段为空
            newEntry.put("age", null); // 设置年龄字段为空
            newEntry.put("unit", null); // 设置单位字段为空
            table.getItems().add(newEntry); // 向TableView中添加新行
        });


        int obButtonIndex = flowPane.getChildren().indexOf(obButton); // 获取"确定"按钮的索引位置
        flowPane.getChildren().add(obButtonIndex + 2, addButton); // 将新按钮添加到"确定"按钮的右边
        /* 为两个按钮之间添加空白 */
        Region spacer1 = new Region();
        spacer1.setPrefWidth(10);
        flowPane.getChildren().add(spacer1);

        Button delButton = new Button("删除");
        delButton.setOnAction(event -> {
            ObservableList<Map> selectedItems = table.getSelectionModel().getSelectedItems();
            System.out.println("delete");
            if (!selectedItems.isEmpty()) {
                for (Map selectedItem : selectedItems) {
                    // 调用后端函数以删除对应的数据
                    DataRequest deleteReq = new DataRequest();
                    deleteReq.add("memberId", selectedItem.get("memberId"));
                    DataResponse deleteRes = HttpRequestUtil.request("/api/student/familyMemberDelete", deleteReq);
                    if (deleteRes.getCode() == 0) {
                        // 如果删除成功，从TableView中移除该行
                        table.getItems().remove(selectedItem);
                        MessageDialog.showDialog("删除成功！");
                    } else {
                        // 如果删除失败，显示错误消息
                        MessageDialog.showDialog(deleteRes.getMsg());
                    }
                }
            } else {
                // 如果没有选中任何行，显示提示消息
                MessageDialog.showDialog("请选择要删除的行。");
            }
        });

        int addButtonIndex = flowPane.getChildren().indexOf(addButton); // 获取"确定"按钮的索引位置
        flowPane.getChildren().add(addButtonIndex + 2, delButton); // 将新按钮添加到"确定"按钮的右边

        /* 提示正文 */
        BorderPane titlePane = new BorderPane();
        Label descriptionLabel = new Label("  提示: 双击表格进行编辑");
        Label blankSpace = new Label("                        ");
        VBox titleBox = new VBox(descriptionLabel, blankSpace);
        titlePane.setTop(titleBox);

        root.setCenter(table);
        root.setBottom(flowPane);
        root.setTop(titlePane);


        scene = new Scene(root, 400, 200);
        stage.initOwner(MainApplication.getMainStage());
        stage.initModality(Modality.NONE);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("家庭成员");
        stage.setOnCloseRequest(event -> {
            MainApplication.setCanClose(true);
        });
        stage.showAndWait();
    }

    @FXML
    protected void onIntroductionButtonClick() {
        if(studentId == null) {
            MessageDialog.showDialog("学号为空！");
            return;
        }

        try {
        // 创建FXMLLoader实例并加载FXML文件
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teach/javafx/student-introduce-by-admin-panel.fxml"));

        // 加载FXML文件并获取根节点
        Parent root = fxmlLoader.load();
        StudentIntroduceByAdminController controller = fxmlLoader.getController();
        controller.setStudentId(studentId);

        // 设置场景和显示舞台
        Scene scene = new Scene(root, 1200, 600);
        Stage stage = new Stage();
        stage.setTitle("学生个人简介");
        stage.setScene(scene);
        stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}



