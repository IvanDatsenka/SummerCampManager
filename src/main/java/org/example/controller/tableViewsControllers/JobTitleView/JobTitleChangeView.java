package org.example.controller.tableViewsControllers.JobTitleView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.controller.tableViewsControllers.BuildingView.BuildingViewController;
import org.example.dao.JobTitleDao;
import org.example.entity.Building;
import org.example.entity.JobTitle;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class JobTitleChangeView extends Changeable implements Initializable {
    private final JobTitleDao jobTitleDao = JobTitleDao.getInstance();

    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeChildButton;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField jobNameText;

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {
        changeWindow("/jobTitle-table-view.fxml");
        backToMeinMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        JobTitle jobTitle = JobTitleTableView.getJobTitleSt();
        String jobName = jobNameText.getText();
        if(jobName.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("");
            alert.setContentText("ЗАПОЛНИТЕ ПОЛЯ ВВОДА");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("подтвердить изменения");
        alert.setHeaderText("");
        alert.setContentText("");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
            jobTitleDao.update(JobTitle.builder().name(jobName).id(jobTitle.getId()).build());

        changeWindow("/jobTitle-table-view.fxml");
        changeChildButton.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        headerLabel.setText("вы изменяете название должности "+ JobTitleTableView.getJobTitleSt().getName());
    }
}
