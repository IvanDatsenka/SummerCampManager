package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloController extends Changeable{
    @FXML
    private Button mainMenuButton;

    @FXML
    private Button mainMenuRegistrationButton;

    @FXML
    void onMainMenuButtonClick(ActionEvent event) {
        changeWindow("/enter-view.fxml");
        mainMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onMainMenuRegistrationButtonClick(ActionEvent event) {
        changeWindow("/registration-view.fxml");
        mainMenuButton.getScene().getWindow().hide();
    }
}