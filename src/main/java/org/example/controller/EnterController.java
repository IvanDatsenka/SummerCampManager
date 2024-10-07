package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.dao.registration.KurstProjectUserDao;
import org.example.entity.PrUser;
import org.example.service.EnterService;
import org.example.service.impl.EnterServiceImpl;

import java.util.List;

public class EnterController extends Changeable{
    private boolean radioAdmin = false;
    private boolean radioUser = false;

    private final EnterService enterService = new EnterServiceImpl();

    private final KurstProjectUserDao kurstProjectUserDao = new KurstProjectUserDao();

    @FXML
    private Button backToMainMenuEnterMenuButton;

    @FXML
    private Button enterMenuEnterButton;

    @FXML
    private PasswordField enterPassword;

    @FXML
    private TextField enterUsername;

    @FXML
    private RadioButton radioAdminEnterMenu;

    @FXML
    private RadioButton radioUserEnterMenu;

    @FXML
    void onBackToMainMenuEnterMenuButtonClick(ActionEvent event) {
        changeWindow("/hello-view.fxml");
        backToMainMenuEnterMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onEnterMenuEnterButtonClick(ActionEvent event) {
        System.out.println(enterUserCheck());
        if (enterUserCheck()){
            CurrentUserRole.setNotAdminCurrentUser();
            changeWindow("/main-menu.fxml");
            enterMenuEnterButton.getScene().getWindow().hide();
        }
        if (enterAdminCheck()){
            CurrentUserRole.setAdminCurrentUser();
            changeWindow("/main-menu.fxml");
            enterMenuEnterButton.getScene().getWindow().hide();
        }

    }

    @FXML
    void radioAdminActive(ActionEvent event) {
        if(radioAdmin == false)
            radioAdmin = true;
        else
            radioAdmin = false;
        System.out.println("radio admin is "+radioAdmin);
    }

    @FXML
    void radioUserActive(ActionEvent event) {
        if(radioUser == false)
            radioUser = true;
        else
            radioUser = false;
        System.out.println("radio user is "+radioUser);
    }

    private boolean isRadioButtonPressCorrectly(){
        if (radioUser == true && radioAdmin == true)
            return false;
        else if(radioUser == false && radioAdmin == false)
            return false;
        else return true;
    }

    private boolean enterUserCheck(){
        String password = enterPassword.getText();
        String userName = enterUsername.getText();
        if(password.equals("")||userName.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("заполните поля ввода");
            alert.showAndWait();
            return false;
        }

        if(!isRadioButtonPressCorrectly()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("ошибка в выборе роли");
            alert.showAndWait();
            return false;
        }

        List<PrUser> list = kurstProjectUserDao.findAll();


        if(radioUser == true){
            for (PrUser prUser:list) {
                if(prUser.getUserName().equals(userName)&&prUser.getPassword().equals(password)&&prUser.getRole().equals("default"))
                    return true;
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("некорекктные данные, проверьте ввод пароля и логина");
            alert.showAndWait();
            return false;
        }else
            return false;
    }

    private boolean enterAdminCheck(){
        String password = enterPassword.getText();
        String userName = enterUsername.getText();
        if(password.equals("")||userName.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("заполните поля ввода");
            alert.showAndWait();
            return false;
        }

        if(!isRadioButtonPressCorrectly()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("ошибка в выборе роли");
            alert.showAndWait();
            return false;
        }

        List<PrUser> list = kurstProjectUserDao.findAll();


        if(radioAdmin == true){
            for (PrUser prUser:list) {
                if(prUser.getUserName().equals(userName)&&prUser.getPassword().equals(password)&&prUser.getRole().equals("admin"))
                    return true;
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("некорекктные данные, проверьте ввод пароля и логина");
            alert.showAndWait();
            return false;
        }else
            return false;
    }
}
