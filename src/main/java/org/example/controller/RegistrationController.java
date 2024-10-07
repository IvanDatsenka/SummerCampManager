package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.dao.registration.KurstProjectUserDao;
import org.example.entity.PrUser;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationController extends Changeable implements Initializable {
    private final KurstProjectUserDao kurstProjectUserDao = new KurstProjectUserDao();
    @FXML
    private PasswordField passwordFirst;

    @FXML
    private Button backButton;

    @FXML
    private PasswordField passwordSecond;

    @FXML
    private Button registerButton;

    @FXML
    private TextField userNameText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/enter-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onRegisterButtonClick(ActionEvent event) {
        if(checkInputData()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("вы успешно зарегистрировались");
            alert.showAndWait();
            changeWindow("/main-menu.fxml");
        }
    }

    private boolean checkInputData(){
        String firstPasswordText = passwordFirst.getText();
        String secondPasswordText = passwordSecond.getText();
        String userName = userNameText.getText();

        List<PrUser> userList = kurstProjectUserDao.findAll();

        if(firstPasswordText.equals("") || userName.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("заполните поля ввода");
            alert.showAndWait();
            return false;
        }else if(secondPasswordText.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("подтвердите пароль");
            alert.showAndWait();
            return false;
        }else{
            for (PrUser user:userList) {
                if (user.getUserName().equals(userName)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ошибка");
                    alert.setHeaderText("");
                    alert.setContentText("пользователь с таким именем уже зарегистрирован");
                    alert.showAndWait();
                    return false;
                }
            }
            if(!firstPasswordText.equals(secondPasswordText)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ошибка");
                alert.setHeaderText("");
                alert.setContentText("пароли не совпадают");
                alert.showAndWait();
                return false;
            }
            if(firstPasswordText.length()<6){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ошибка");
                alert.setHeaderText("");
                alert.setContentText("длина пароля должна быть больше или равна 6 символов");
                alert.showAndWait();
                return false;
            }
        }
        kurstProjectUserDao.save(PrUser.builder().role("default").password(firstPasswordText).userName(userName).build());
        return true;
    }
}
