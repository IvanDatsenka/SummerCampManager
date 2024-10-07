package org.example.controller.tableViewsControllers.squadViewControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.view.SquadViewDao;
import org.example.entity.ChildView;
import org.example.entity.Squad;
import org.example.entity.views.SquadView;
import org.example.service.BuildingService;
import org.example.service.EmployeeService;
import org.example.service.ShiftService;
import org.example.service.SquadViewService;
import org.example.service.impl.*;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SquadViewTableController extends Changeable implements Initializable {
    private final SquadViewService squadViewService = new SquadViewServiceImpl();
    private final SquadServiceImpl squadService = new SquadServiceImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final BuildingService buildingService = new BuildingServiceImpl();
    private final ShiftService shiftService = new ShiftServiceImpl();
    private final SquadViewDao squadViewDao = new SquadViewDao();

    private HashMap<Long, String> shiftMap;
    private HashMap<Long, String> employeeMap;
    private HashMap<Long, String> buildingMap;
    @FXML
    private Button backButton;
    @FXML
    private ChoiceBox<String> buildingChoice;
    @FXML
    private TableColumn<SquadView, String> buildingColumn;
    @FXML
    private Button changeButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ChoiceBox<String> employeeChoice;
    @FXML
    private TableColumn<SquadView, String> employeeColumn;
    @FXML
    private Button isertDataButton;
    @FXML
    private ChoiceBox<String> shiftChoice;
    @FXML
    private TableColumn<SquadView, String> shiftColumn;
    @FXML
    private TextField squadNameText;
    @FXML
    private TableColumn<SquadView, String> squadNameColumn;
    @FXML
    private TableView<SquadView> table;

    private static SquadView squadSt;

    public static SquadView getSquadViewSt(){
        return squadSt;
    }

    private void fillInTableView(){
        squadNameColumn.setCellValueFactory(new PropertyValueFactory<>("squadName"));
        shiftColumn.setCellValueFactory(new PropertyValueFactory<>("shiftName"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("counselor"));
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingName"));
        table.setItems(squadViewService.getData());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();

        shiftMap = shiftService.getIdAndNames();
        buildingMap = buildingService.getIdAndNames();
        employeeMap=employeeService.getIdAndNames();


        ObservableList<String> shifts =
                FXCollections.observableArrayList(shiftMap.values());

        ObservableList<String> buildings =
                FXCollections.observableArrayList(buildingMap.values());

        ObservableList<String> employees =
                FXCollections.observableArrayList(employeeMap.values());


        shiftChoice.setItems(shifts);
        buildingChoice.setItems(buildings);
        employeeChoice.setItems(employees);
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        changeButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        SquadView squadView = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(squadView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        squadSt = squadView;
        changeWindow("/squad-view-change-view.fxml");
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

        SquadView squadView = table.getSelectionModel().getSelectedItem();

        if(!squadViewDao.deleteSquadFromSquadDao(squadView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("отряд "+ squadView.getSquadName() + " " + " удалён");
            alert.showAndWait();
        }
        table.getItems().remove(squadView);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String squadName = squadNameText.getText();
        String employeeName = employeeChoice.getValue();
        String shiftName = shiftChoice.getValue();
        String buildingName = buildingChoice.getValue();

        if (squadName == null || employeeName ==null || shiftName == null || buildingName == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректная информация, проверьте ввод");
            alert.showAndWait();
        }else{
            Long employeeId = null;
            Long shiftId = null;
            Long buildingId = null;
            for (Map.Entry entry : employeeMap.entrySet()) {
                if(entry.getValue().toString().equals(employeeName)) {
                    employeeId= (Long) entry.getKey();
                }
            }
            for (Map.Entry entry : buildingMap.entrySet()) {
                if(entry.getValue().toString().equals(buildingName)) {
                    buildingId= (Long) entry.getKey();
                }
            }
            for (Map.Entry entry : shiftMap.entrySet()) {
                if(entry.getValue().toString().equals(shiftName)) {
                    shiftId= (Long) entry.getKey();
                }
            }
            Squad savedSquad = squadService.save(Squad.builder()
                    .name(squadName)
                    .buildingId(buildingId)
                    .shiftId(shiftId)
                    .employeeId(employeeId)
                    .build());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("error");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили новый отряд: " + savedSquad.getName() + " "+ employeeName);
            alert.showAndWait();
            fillInTableView();
        }
    }


}
