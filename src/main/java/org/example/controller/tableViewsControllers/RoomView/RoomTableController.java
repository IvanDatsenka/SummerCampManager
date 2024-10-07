package org.example.controller.tableViewsControllers.RoomView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.BuildingDao;
import org.example.dao.RoomDao;
import org.example.dao.view.RoomViewDao;
import org.example.entity.Building;
import org.example.entity.Employee;
import org.example.entity.Room;
import org.example.entity.views.EmployeeView;
import org.example.entity.views.RoomView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RoomTableController extends Changeable implements Initializable  {

    private final RoomViewDao roomViewDao = new RoomViewDao();
    private final RoomDao roomDao = RoomDao.getInstance();
    private final BuildingDao buildingDao = BuildingDao.getInstance();
    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<String> buildingChoice;

    @FXML
    private TableColumn<RoomView, String> buildingColumn;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button isertDataButton;

    @FXML
    private TableColumn<RoomView, String> roomNumberColumn;

    @FXML
    private TextField roomNumberInput;

    @FXML
    private TableView<RoomView> table;

    private HashMap<Long, String> buildingHash;

    private static RoomView roomViewSt;
    public static RoomView getRoomViewSt(){
        return roomViewSt;
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        RoomView roomView = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(roomView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        roomViewSt = roomView;
        changeWindow("/room-change-view.fxml");
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

        RoomView roomView = table.getSelectionModel().getSelectedItem();

        if(!roomViewDao.deleteRoomView(roomView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("комната "+ roomView.getRoomNumber() + "в корпусе"+ roomView.getBuildingNumber() + " удалена");
            alert.showAndWait();
        }
        table.getItems().remove(roomView);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String roomNumber = roomNumberInput.getText();
        String buildingName = buildingChoice.getValue();


        if(roomNumber.equals("")||buildingName==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректная информация, проверьте поля ввода");
            alert.showAndWait();
        }else{
            Long buildingId = null;
            for (Map.Entry entry : buildingHash.entrySet()) {
                if(entry.getValue().toString().equals(buildingName)) {
                    buildingId= (Long) entry.getKey();
                }
            }


            Room room = Room.builder().number(roomNumber).buildingId(buildingId).build();
            roomDao.save(room);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили новую комнату: " + roomNumber + " в корпусе: "+buildingName);
            alert.showAndWait();
            fillInTableView();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();

        buildingHash = buildingDao.getIdAndNames();
        ObservableList<String> jpbTitles =
                FXCollections.observableArrayList(buildingHash.values());

        buildingChoice.setItems(jpbTitles);
    }

    private void fillInTableView(){
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        table.setItems(initiate());
    }

    private ObservableList<RoomView> initiate(){
        return FXCollections.observableArrayList(roomViewDao.getView());
    }

}
