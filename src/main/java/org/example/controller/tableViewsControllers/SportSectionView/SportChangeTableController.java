package org.example.controller.tableViewsControllers.SportSectionView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.controller.tableViewsControllers.JobTitleView.JobTitleTableView;
import org.example.dao.SportSectionDao;
import org.example.entity.JobTitle;
import org.example.entity.SportSection;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SportChangeTableController extends Changeable implements Initializable {
    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeChildButton;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField jobNameText;

    private final SportSectionDao ssDao = SportSectionDao.getInstance();

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {
        changeWindow("/sport-table-view.fxml");
        backToMeinMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        SportSection sportSection = SportTableController.getSportSectionSt();
        String ssname = jobNameText.getText();
        if(ssname.equals("")){
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
            ssDao.update(SportSection.builder().name(ssname).id(sportSection.getId()).build());

        changeWindow("/sport-table-view.fxml");
        changeChildButton.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        headerLabel.setText("вы изменяете название спортивной секции "+ SportTableController.getSportSectionSt().getName());
    }
}
