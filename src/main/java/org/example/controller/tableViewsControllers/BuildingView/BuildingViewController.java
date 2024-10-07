package org.example.controller.tableViewsControllers.BuildingView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.BuildingDao;
import org.example.entity.Building;
import org.example.entity.Event;
import org.example.entity.views.EventView;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BuildingViewController extends Changeable implements Initializable {

    private final BuildingDao buildingDao = BuildingDao.getInstance();

    @FXML
    private Button backButton;

    @FXML
    private TextField buildingNameText;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button isertDataButton;

    @FXML
    private TableColumn<Building, String> squadNameColumn;

    @FXML
    private TableView<Building> table;
    private HashMap<Long, String> buildingMap;

    private static Building buildingSt;

    public static Building getBuildingSt(){
        return buildingSt;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();
    }

    private void fillInTableView(){
        squadNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.setItems(initiate());
    }

    private ObservableList<Building> initiate() {
        return FXCollections.observableArrayList(buildingDao.findAll());
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        Building building = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(building)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите запись");
            alert.showAndWait();
            return;
        }

        buildingSt = building;
        changeWindow("/building-change-view.fxml");
        changeButton.getScene().getWindow().hide();
    }

    @FXML
    void onDeleteButtonClick(ActionEvent event) {

        if(!table.getItems().contains(table.getSelectionModel().getSelectedItem())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("выберите строку");
            alert.showAndWait();
        }

        Building building = table.getSelectionModel().getSelectedItem();

        if(!buildingDao.delete(building.getId())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("корпус "+ building.getName() + " " + " удален");
            alert.showAndWait();
        }
        table.getItems().remove(building);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String buildingName = buildingNameText.getText();



        if(buildingName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("проверьте ввод");
            alert.showAndWait();
        }else{
            Building savedBuilding = buildingDao.save(Building.builder()
                            .name(buildingName)
                    .build());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили новые корпус: " + savedBuilding.getName());
            alert.showAndWait();
            fillInTableView();
        }
    }


}
