package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController extends Changeable{

    @FXML
    private Button addDataButton;

    @FXML
    private Button backToRegistrationButton;

    @FXML
    private Button campDataButton;

    @FXML
    private Button campReportsButton;

    @FXML
    private Button usersListButton;

    @FXML
    void onAddDataButtonClick(ActionEvent event) {

    }

    @FXML
    void onBackToRegistrationButtonClick(ActionEvent event) {
        changeWindow("/enter-view.fxml");
        backToRegistrationButton.getScene().getWindow().hide();
    }

    @FXML
    void onCampDataButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        campDataButton.getScene().getWindow().hide();
    }

    @FXML
    void onCampReportsButtonClick(ActionEvent event) {
        changeWindow("/reports-view.fxml");
        campDataButton.getScene().getWindow().hide();
    }

    @FXML
    void onUsersListButtonClick(ActionEvent event) {

    }
}
