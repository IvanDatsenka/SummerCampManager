package org.example.controller.tableViewsControllers.RoomView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.controller.tableViewsControllers.EmployeeView.EmployeeTableView;
import org.example.dao.BuildingDao;
import org.example.dao.RoomDao;
import org.example.entity.Employee;
import org.example.entity.Room;
import org.example.entity.views.EmployeeView;
import org.example.entity.views.RoomView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class RoomChangeController extends Changeable implements Initializable {
    private final RoomDao roomDao = RoomDao.getInstance();
    private final BuildingDao buildingDao = BuildingDao.getInstance();

    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private ChoiceBox<String> buildingChoice;

    @FXML
    private Button changeChildButton;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField roomNumberChoice;

    private HashMap<Long, String> buildingHash;

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RoomView roomView = RoomTableController.getRoomViewSt();

        buildingHash=buildingDao.getIdAndNames();

        ObservableList<String> buildings =
                FXCollections.observableArrayList(buildingHash.values());

        buildingChoice.setItems(buildings);
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        RoomView roomView = RoomTableController.getRoomViewSt();

       String roomNumber = roomNumberChoice.getText();
       String buildingName = buildingChoice.getValue();

        if(validInputData(roomNumber, buildingName)){
            if(roomNumber.equals("")) {
                roomNumber = roomView.getRoomNumber();
            }
            if(buildingName==null)
                buildingName = roomView.getBuildingNumber();

            roomDao.update(buildRoom(roomNumber, buildingName, roomView.getRoomId()));
            changeWindow("/room-table-view.fxml");
            changeChildButton.getScene().getWindow().hide();
        }
    }

    private Room buildRoom(String roomNumber, String buildingName, Long id) {
        buildingHash = buildingDao.getIdAndNames();
        Long buildingId = null;
        for (Map.Entry entry : buildingHash.entrySet()) {
            if(entry.getValue().toString().equals(buildingName)) {
                buildingId = (Long) entry.getKey();
            }
        }
        return Room.builder().number(roomNumber).buildingId(buildingId).id(id).build();
    }

    private boolean validInputData(String roomNumber, String buildingName){
        if(roomNumber.equals("") && buildingName==null) {
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

}
