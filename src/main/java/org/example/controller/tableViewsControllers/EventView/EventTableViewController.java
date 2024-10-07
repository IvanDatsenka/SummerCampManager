package org.example.controller.tableViewsControllers.EventView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.EventDao;
import org.example.dao.view.EventViewDao;
import org.example.entity.ChildView;
import org.example.entity.Event;
import org.example.entity.views.EventView;
import org.example.entity.views.SquadView;
import org.example.service.EmployeeService;
import org.example.service.impl.EmployeeServiceImpl;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class EventTableViewController extends Changeable implements Initializable {

    private final EventViewDao eventViewDao = new EventViewDao();
    private final EmployeeService employeeService = new EmployeeServiceImpl();

    private final EventDao eventDao = EventDao.getInstance();
    @FXML
    private Button backButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ChoiceBox<String> employeeChoice;


    @FXML
    private TableColumn<EventView, String> employeeColumn;

    @FXML
    private TableColumn<EventView, String> eventDateColumn;

    @FXML
    private DatePicker eventDateInput;

    @FXML
    private TextField eventNamaTextField;

    @FXML
    private Button isertDataButton;
    private HashMap<Long, String> employeeMap;

    @FXML
    private Button findButton;

    @FXML
    private TextField findText;

    @FXML
    private TableColumn<EventView, String> eventNameColumn;

    @FXML
    private TableView<EventView> table;

    private static EventView eventViewSt;

    public static EventView getEventViewSt(){
        return eventViewSt;
    }

    private void fillInTableView(){
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
        table.setItems(initiate());
    }

    private void fillInTableViewForFind(String name){
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
        table.setItems(initiateForFind(name));
    }

    private ObservableList<EventView> initiate(){
        return FXCollections.observableArrayList(eventViewDao.getView());
    }

    private ObservableList<EventView> initiateForFind(String eventName){
        return FXCollections.observableArrayList(eventViewDao.getView().stream().filter(v -> v.getEventName().equals(eventName)).toList());
    }
    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        EventView eventView = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(eventView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        eventViewSt = eventView;
        changeWindow("/event-view-change.fxml");
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

        EventView eventView = table.getSelectionModel().getSelectedItem();

        if(!eventViewDao.deleteEventFromEventView(eventView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("мероприятие "+ eventView.getEventName() + " " +eventView.getEventDate()+ " удаленое");
            alert.showAndWait();
        }
        table.getItems().remove(eventView);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String eventName = eventNamaTextField.getText();
        LocalDate localDate = eventDateInput.getValue();
        String employeeName = employeeChoice.getValue();


        if(eventName.equals("")||localDate==null||employeeName==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректная информация, проверьте ввод");
            alert.showAndWait();
        }else{
            Long employeeId = null;
            for (Map.Entry entry : employeeMap.entrySet()) {
                if(entry.getValue().toString().equals(employeeName)) {
                    employeeId= (Long) entry.getKey();
                }
            }

            Date date = Date.valueOf(localDate);
            Event event1 = Event.builder()
                    .date(date)
                    .employeeId(employeeId)
                    .name(eventName)
                    .build();
            Event savedEvent = eventDao.save(event1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили новое мероприятие: " + savedEvent.getName());
            alert.showAndWait();
            fillInTableView();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();

        employeeMap=employeeService.getIdAndNames();

        ObservableList<String> employees =
                FXCollections.observableArrayList(employeeMap.values());

        employeeChoice.setItems(employees);
    }

    @FXML
    void onFindButtonClick(ActionEvent event) {
        String inputText = findText.getText();
        if(inputText.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректная информация, проверьте ввод");
            alert.showAndWait();
            return;
        }else{
            fillInTableViewForFind(inputText);
        }
    }

    @FXML
    void onDefaultViewButtonClick(ActionEvent event) {
        fillInTableView();
    }
}
