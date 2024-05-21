package com.teach.javafx.controller.base;

import com.teach.javafx.AppStore;
import com.teach.javafx.MainApplication;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.LoginRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class LoginChooseController {
    String userName;
    String passWord;
    @FXML
    protected void onAdminButtonClick() {
        userName = "admin";
        passWord = "123456";
        logIn(userName, passWord);
    }

    @FXML
    protected void onStudentButtonClick() {
        userName = "2022030001";
        passWord = "123456";
        logIn(userName, passWord);
    }

    @FXML
    protected void onTeacherButtonClick() {
        userName = "200799013517";
        passWord = "123456";
        logIn(userName, passWord);
    }

    @FXML
    protected void onDefaultButtonClick() throws IOException {
        userName = "admin";
        passWord = "123456";

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add(getClass().getResource("/com/teach/javafx/css/styles.css").toExternalForm());
        stage.setTitle("登录");
        stage.setScene(scene);
        //stage.show();
        stage.setOnCloseRequest(event -> HttpRequestUtil.close());
        MainApplication.setMainStage(stage);
    }

    protected void logIn(String userName, String passWord) {
        LoginRequest loginRequest = new LoginRequest(userName,passWord);
        String msg = HttpRequestUtil.login(loginRequest);
        if(msg != null) {
            MessageDialog.showDialog(msg);
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/main-frame.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), -1, -1);
            AppStore.setMainFrameController((MainFrameController) fxmlLoader.getController());
            MainApplication.resetStage("教学管理系统", scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
