package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.registration.KurstProjectUserDao;
import org.example.entity.PrUser;
import org.example.entity.views.EmployeeView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChangeRoleController extends Changeable implements Initializable {

    private final KurstProjectUserDao dao = new KurstProjectUserDao();
    @FXML
    private Button backButton;

    @FXML
    private Button changeButton;

    @FXML
    private TableColumn<PrUser, String> passwordColumn;

    @FXML
    private TableView<PrUser> tableView;

    @FXML
    private TableColumn<PrUser, String> userRoleColumn;

    @FXML
    private TableColumn<PrUser, String> usernameColumn;

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        changeButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        PrUser prUser = tableView.getSelectionModel().getSelectedItem();
        if(!tableView.getItems().contains(prUser)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }else{
            changeRole(prUser);
        }
    }

    private void changeRole(PrUser prUser) {
        String alertMessage;
        if(prUser.getRole().equals("default")){
            alertMessage = "вы хотите дать этому пользователю права администратора?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("подтвердить изменения");
            alert.setHeaderText("");
            alert.setContentText(alertMessage);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                dao.update(prUser);
                fillInTableView();
            }
            else
                return ;
        }else{
            alertMessage = "вы хотите убрать у этого пользователя права администратора?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("подтвердить изменения");
            alert.setHeaderText("");
            alert.setContentText(alertMessage);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                dao.update(prUser);
                fillInTableView();
            }
            else
                return ;
        }
    }

    public void fillInTableView(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        tableView.setItems(initiate());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();
    }

    private ObservableList<PrUser> initiate(){
        return FXCollections.observableArrayList(dao.findAll());
    }
}
