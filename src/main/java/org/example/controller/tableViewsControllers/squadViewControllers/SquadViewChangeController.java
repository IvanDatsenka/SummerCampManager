package org.example.controller.tableViewsControllers.squadViewControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.dao.SquadDao;
import org.example.entity.Squad;
import org.example.entity.views.SquadView;
import org.example.service.BuildingService;
import org.example.service.EmployeeService;
import org.example.service.ShiftService;
import org.example.service.impl.BuildingServiceImpl;
import org.example.service.impl.EmployeeServiceImpl;
import org.example.service.impl.ShiftServiceImpl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class SquadViewChangeController  extends Changeable implements Initializable {

    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final BuildingService buildingService = new BuildingServiceImpl();
    private final ShiftService shiftService = new ShiftServiceImpl();
    private final SquadDao squadDao = SquadDao.getInstance();
    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private ChoiceBox<String> buildingChoice;

    @FXML
    private Button changeChildButton;

    @FXML
    private ChoiceBox<String> employeeChoice;

    @FXML
    private Label headerLabel;

    @FXML
    private ChoiceBox<String> shiftChoice;

    @FXML
    private TextField squadNameText;

    private HashMap<Long, String> shiftMap;
    private HashMap<Long, String> employeeMap;
    private HashMap<Long, String> buildingMap;

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {
        changeWindow("/squad-table-view.fxml");
        backToMeinMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        SquadView squadView = SquadViewTableController.getSquadViewSt();

        String shiftChoiceData = shiftChoice.getValue();
        String squadNameTextData = squadNameText.getText();
        String employeeData = employeeChoice.getValue();
        String buildingData = buildingChoice.getValue();

        if(validInputData(squadNameTextData, employeeData, buildingData, shiftChoiceData)){
            if(squadNameTextData.equals(""))
                squadNameTextData=squadView.getSquadName();
            if(shiftChoiceData==null)
                shiftChoiceData=squadView.getShiftName();
            if(employeeData==null)
                employeeData=squadView.getCounselor();
            if(buildingData==null)
                buildingData=squadView.getBuildingName();

            squadDao.update(buildSquad(
                    squadNameTextData,
                    employeeData,
                    buildingData,
                    shiftChoiceData,
                    squadView.getSquadId()
            ));

            changeWindow("/squad-table-view.fxml");
            backToMeinMenuButton.getScene().getWindow().hide();

        }
    }

    private Squad buildSquad(String name, String employee, String building, String shift, Long squadId){
        employeeMap = employeeService.getIdAndNames();
        buildingMap = buildingService.getIdAndNames();
        shiftMap = shiftService.getIdAndNames();

        Long employeeId = null;
        Long shiftId = null;
        Long buildingId = null;
        for (Map.Entry entry : employeeMap.entrySet()) {
            if(entry.getValue().toString().equals(employee)) {
                employeeId= (Long) entry.getKey();
            }
        }
        for (Map.Entry entry : buildingMap.entrySet()) {
            if(entry.getValue().toString().equals(building)) {
                buildingId= (Long) entry.getKey();
            }
        }
        for (Map.Entry entry : shiftMap.entrySet()) {
            if(entry.getValue().toString().equals(shift)) {
                shiftId= (Long) entry.getKey();
            }
        }

        return Squad.builder().id(squadId).employeeId(employeeId).buildingId(buildingId).shiftId(shiftId).name(name).build();
    }

    private boolean validInputData(String name, String employee, String building, String shift){
        if(name.equals("") && employee==null && building == null && shift ==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("");
            alert.setContentText("ЗАПОЛНИТЕ ПОЛЯ ВВОДА");
            alert.showAndWait();
            return false;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("подтвердить изменения");
        alert.setHeaderText("");
        alert.setContentText("");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SquadView squadView = SquadViewTableController.getSquadViewSt();
        headerLabel.setText("Вы изменяете отряд: "+squadView.getSquadName() + ", Корпус: "+squadView.getBuildingName() +"\nВожатый: "+squadView.getCounselor()+"\nСмена: "+squadView.getShiftName());
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

}
