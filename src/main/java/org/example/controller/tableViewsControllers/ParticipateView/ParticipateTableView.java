package org.example.controller.tableViewsControllers.ParticipateView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.EventDao;
import org.example.dao.ParticipateDao;
import org.example.dao.SquadDao;
import org.example.dao.view.ParticipateViewDao;
import org.example.entity.Event;
import org.example.entity.Participate;
import org.example.entity.views.EventView;
import org.example.entity.views.ParticipateView;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ParticipateTableView extends Changeable implements Initializable {

    private final EventDao eventDao = EventDao.getInstance();
    private final SquadDao squadDao = SquadDao.getInstance();
    private final ParticipateDao participateDao = ParticipateDao.getInstance();
    ParticipateViewDao participateViewDao = new ParticipateViewDao();

    @FXML
    private Button backButton;

    @FXML
    private Button changeButton;

    @FXML
    private TableColumn<ParticipateView, Date> dateColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private ChoiceBox<String> eventChoice;

    @FXML
    private TableColumn<ParticipateView, String> eventColumn;

    @FXML
    private Button isertDataButton;

    @FXML
    private ChoiceBox<String> squadChoice;

    @FXML
    private TableColumn<ParticipateView, String> squadColumn;

    private HashMap<Long,String> squadMap;

    private HashMap<Long, String> eventMap;

    @FXML
    private TableView<ParticipateView> table;

    private static ParticipateView participateSt;

    public static ParticipateView getParticipateSt(){
        return participateSt;
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();

    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        ParticipateView participateView = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(participateView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        participateSt = participateView;
        changeWindow("/participate-change-view.fxml");
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

        ParticipateView participateView = table.getSelectionModel().getSelectedItem();


        if(!participateViewDao.deleteParticipateFromParticipateView(participateView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("участие отряда "+ participateView.getSquadName() + " в мероприятии "+ participateView.getEventName() + " удалено");
            alert.showAndWait();
        }
        table.getItems().remove(participateView);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String eventName = eventChoice.getValue();
        String squadName = squadChoice.getValue();


        if(eventName==null||squadName==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("проверьте ввод");
            alert.showAndWait();
        }else{
            Long eventId = null;
            for (Map.Entry entry : eventMap.entrySet()) {
                if(entry.getValue().toString().equals(eventName)) {
                    eventId= (Long) entry.getKey();
                }
            }

            Long squadId = null;
            for (Map.Entry entry : squadMap.entrySet()) {
                if(entry.getValue().toString().equals(squadName)) {
                    squadId= (Long) entry.getKey();
                }
            }

            Participate participate = Participate.builder()
                    .squadId(squadId)
                    .eventId(eventId)
                    .build();
            Participate savedParticipate = participateDao.save(participate);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили новые участие");
            alert.showAndWait();
            fillInTableView();
        }
    }

    private void fillInTableView(){
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        squadColumn.setCellValueFactory(new PropertyValueFactory<>("squadName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        table.setItems(initiate());
    }

    private ObservableList<ParticipateView> initiate(){
        return FXCollections.observableArrayList(participateViewDao.getView());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();

        eventMap=eventDao.getIdAndNames();

        squadMap = squadDao.getIdAndNames();

        ObservableList<String> events =
                FXCollections.observableArrayList(eventMap.values());

        ObservableList<String> squads =
                FXCollections.observableArrayList(squadMap.values());

        eventChoice.setItems(events);
        squadChoice.setItems(squads);
    }
}
