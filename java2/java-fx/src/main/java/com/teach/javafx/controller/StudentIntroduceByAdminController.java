package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

public class StudentIntroduceByAdminController extends  ToolController {
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
        getIntroduceData();
    }

    private Integer studentId;

    private ImageView photoImageView;
    private ObservableList<Map> observableList = FXCollections.observableArrayList();

    @FXML
    private HTMLEditor introduceHtml; //个人简历HTML编辑器
    @FXML
    private Button photoButton;  //照片显示和上传按钮

    public void setNum(Label num) {
        this.num = num;
    }

    @FXML
    private Label num;  //学号标签
    @FXML
    private Label name;//姓名标签
    @FXML
    private Label dept; //学院标签
    @FXML
    private Label major; //专业标签
    @FXML
    private Label className; //班级标签
    @FXML
    private Label card;  //证件号码标签
    @FXML
    private Label gender; //性别标签
    @FXML
    private Label birthday; //出生日期标签
    @FXML
    private Label email; //邮箱标签
    @FXML
    private Label phone; //电话标签
    @FXML
    private Label address; //地址标签
    @FXML
    private TableView<Map> scoreTable;  //成绩表TableView
    @FXML
    private TableColumn<Map, String> courseNumColumn;  //课程号列
    @FXML
    private TableColumn<Map, String> courseNameColumn; //课程名列
    @FXML
    private TableColumn<Map, String> creditColumn; //学分列
    @FXML
    private TableColumn<Map, String> markColumn; //成绩列
    @FXML
    private TableColumn<Map, String> rankingColumn; //排名列

    @FXML
    private BarChart<String, Number> barChart;  //消费直方图控件
    @FXML
    private PieChart pieChart;   //成绩分布饼图控件
    private Integer personId = null;  //学生关联人员主键

    @FXML
    public void initialize(){
        this.studentId = studentId;
        photoImageView = new ImageView();
        photoImageView.setFitHeight(100);
        photoImageView.setFitWidth(100);
        photoButton.setGraphic(photoImageView);
        courseNumColumn.setCellValueFactory(new MapValueFactory("courseNum"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
        rankingColumn.setCellValueFactory(new MapValueFactory<>("ranking"));

        //getIntroduceData();
    }

    /**
     * getIntroduceData 从后台获取当前学生的所有信息，不传送的面板各个组件中
     */
    public void getIntroduceData() {

        DataRequest req = new DataRequest();
        req.add("studentId", studentId);
        System.out.println("getIntroduceData:studentId:" + studentId);
        DataResponse res;
        res = HttpRequestUtil.request("/api/student/getStudentIntroduceDataByAdmin", req);
        if (res.getCode() != 0)
            return;
        Map data = (Map) res.getData();
        Map info = (Map) data.get("info");
        studentId = CommonMethod.getInteger(info, "studentId");
        personId = CommonMethod.getInteger(info, "personId");
        num.setText(CommonMethod.getString(info, "num"));
        name.setText(CommonMethod.getString(info, "name"));
        dept.setText(CommonMethod.getString(info, "dept"));
        major.setText(CommonMethod.getString(info, "major"));
        className.setText(CommonMethod.getString(info, "className"));
        card.setText(CommonMethod.getString(info, "card"));
        gender.setText(CommonMethod.getString(info, "genderName"));
        birthday.setText(CommonMethod.getString(info, "birthday"));
        email.setText(CommonMethod.getString(info, "email"));
        phone.setText(CommonMethod.getString(info, "phone"));
        address.setText(CommonMethod.getString(info, "address"));
        introduceHtml.setHtmlText(CommonMethod.getString(info, "introduce"));
        List<Map> scoreList = (List) data.get("scoreList");
        List<Map> markList = (List) data.get("markList");
        List<Map> feeList = (List) data.get("feeList");

        /* 删除课程名为空值的课程 */
        scoreList.removeIf(map -> map.get("courseName") == null);

        for (Map m : scoreList) {
            observableList.addAll(FXCollections.observableArrayList(m));
        }

        scoreTable.setItems(observableList);  // 成绩表数据显示


        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        for (Map m : markList) {
            chartData.add(new PieChart.Data(m.get("title").toString(), Double.parseDouble(m.get("value").toString())));
        }
        pieChart.setData(chartData);  //成绩分类表显示

        XYChart.Series<String, Number> seriesFee = new XYChart.Series<>();
        seriesFee.setName("日常消费");
        for (Map m : feeList)
            seriesFee.getData().add(new XYChart.Data<>(m.get("title").toString(), Double.parseDouble(m.get("value").toString())));
        ObservableList<XYChart.Series<String, Number>> barData =
                FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        barData.add(seriesFee);
        barChart.setData(barData); //消费数据直方图展示

        displayPhoto();
    }

    public void displayPhoto() {
        System.out.println("To get photo...");
        DataRequest req = new DataRequest();
        req.add("fileName", "photo/" + personId + ".jpg");  //个人照片显示
        byte[] bytes = HttpRequestUtil.requestByteData("/api/base/getFileByteData", req);
        if (bytes != null) {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            Image img = new Image(in);
            photoImageView.setImage(img);
        }
    }


    @FXML
    public void onPhotoButtonClick(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("图片上传");

        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG 文件", "*.jpg"));
        File file = fileDialog.showOpenDialog(null);
        if(file == null)
            return;
        DataResponse res =HttpRequestUtil.uploadFile("/api/base/uploadPhoto",file.getPath(),"photo/" + personId + ".jpg");
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
            displayPhoto();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    @FXML
    public void onIntroduceDownloadClick(){
        DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getStudentIntroducePdf", req);
        if (bytes != null) {
            try {
                MessageDialog.pdfViewerDialog(bytes);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onSubmitButtonClick(){
        doSave();
    }

    public void doSave(){
        String introduce = introduceHtml.getHtmlText();
        DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        req.add("introduce",introduce);
        DataResponse res = HttpRequestUtil.request("/api/student/saveStudentIntroduce", req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("提交成功！");
        }else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    public void doImport(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择消费数据表");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        String paras = "studentId="+studentId;
        DataResponse res =HttpRequestUtil.importData("/api/student/importFeeData",file.getPath(),paras);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

}

