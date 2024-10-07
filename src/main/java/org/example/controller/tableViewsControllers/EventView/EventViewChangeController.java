package org.example.controller.tableViewsControllers.EventView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.dao.EventDao;
import org.example.entity.Event;
import org.example.entity.views.EventView;
import org.example.service.EmployeeService;
import org.example.service.impl.EmployeeServiceImpl;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventViewChangeController extends Changeable implements Initializable {

    private final EmployeeService employeeService = new EmployeeServiceImpl();

    private final EventDao eventDao = EventDao.getInstance();

    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeButton;

    @FXML
    private DatePicker dateChoice;

    @FXML
    private ChoiceBox<String> employeeChoice;

    @FXML
    private TextField eventNameText;
    private HashMap<Long, String> employeeMap;

    @FXML
    private Label headerLabel;

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {
        changeWindow("/event-table-view.fxml");
        backToMeinMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        EventView eventView = EventTableViewController.getEventViewSt();

        LocalDate date = dateChoice.getValue();
        Date date1;
        if(date==null)
            date1=eventView.getEventDate();
        else {
            date1 = Date.valueOf(date);
        }

        String eventName = eventNameText.getText();
        String employeeName = employeeChoice.getValue();

        if(validInputData(eventName, employeeName, date)){
            if(eventName.equals(""))
                eventName=eventView.getEventName();
            if(employeeName==null)
                employeeName = eventView.getEmployee();

            eventDao.update(buildEvent(eventName, date1, employeeName, eventView.getEventId()));
            changeWindow("/event-table-view.fxml");
            changeButton.getScene().getWindow().hide();
        }
    }

    private Event buildEvent(String eventName, Date date1, String employeeName, Long id) {
        employeeMap = employeeService.getIdAndNames();
        Long employeeId = null;
        for (Map.Entry entry : employeeMap.entrySet()) {
            if(entry.getValue().toString().equals(employeeName)) {
                employeeId= (Long) entry.getKey();
            }
        }

        return Event.builder().id(id).name(eventName).employeeId(employeeId).date(date1).build();

    }

    private boolean validInputData(String name, String employee, LocalDate date){
        if(name.equals("") && employee==null && date == null) {
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
        EventView eventView = EventTableViewController.getEventViewSt();

        employeeMap=employeeService.getIdAndNames();

        ObservableList<String> employees =
                FXCollections.observableArrayList(employeeMap.values());

        employeeChoice.setItems(employees);
    }
}
