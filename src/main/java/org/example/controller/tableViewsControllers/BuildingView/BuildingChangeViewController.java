package org.example.controller.tableViewsControllers.BuildingView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.dao.BuildingDao;
import org.example.entity.Building;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuildingChangeViewController extends Changeable implements Initializable {
    private final BuildingDao buildingDao = BuildingDao.getInstance();
    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeChildButton;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField squadNameText;

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {
        changeWindow("/building-table-view.fxml");
        backToMeinMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        Building building = BuildingViewController.getBuildingSt();
        if(squadNameText == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("");
            alert.setContentText("ЗАПОЛНИТЕ ПОЛЯ ВВОДА");
            alert.showAndWait();
        }
        String name = squadNameText.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("подтвердить изменения");
        alert.setHeaderText("");
        alert.setContentText("");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
            buildingDao.update(Building.builder().name(name).id(building.getId()).build());

        changeWindow("/building-table-view.fxml");
        changeChildButton.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        headerLabel.setText("вы изменяете название корпуса "+BuildingViewController.getBuildingSt().getName());
    }
}
