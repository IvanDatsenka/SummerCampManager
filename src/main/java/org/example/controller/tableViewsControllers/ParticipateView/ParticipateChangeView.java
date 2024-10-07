package org.example.controller.tableViewsControllers.ParticipateView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.dao.EventDao;
import org.example.dao.ParticipateDao;
import org.example.dao.SquadDao;
import org.example.dao.view.ParticipateViewDao;
import org.example.entity.Event;
import org.example.entity.Participate;
import org.example.entity.Squad;
import org.example.entity.views.ParticipateView;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ParticipateChangeView extends Changeable implements Initializable {

    private final ParticipateDao participateDao = ParticipateDao.getInstance();
    private final EventDao eventDao = EventDao.getInstance();
    private final ParticipateViewDao participateViewDao = new ParticipateViewDao();

    private final SquadDao squadDao = SquadDao.getInstance();

    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeButton;

    @FXML
    private DatePicker dateChoice;

    @FXML
    private ChoiceBox<String> eventChoice;

    @FXML
    private Label headerLabel;

    @FXML
    private ChoiceBox<String> squadChoice;

    private HashMap<Long, String> eventHash;
    private HashMap<Long, String> squadHash;

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {

    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        ParticipateView participateView = ParticipateTableView.getParticipateSt();
        String squadName = squadChoice.getValue();
        String eventName = eventChoice.getValue();
        LocalDate localDate = dateChoice.getValue();
        Date date1;
        if(localDate==null)
            date1=participateView.getEventDate();
        else {
            date1 = Date.valueOf(localDate);
        }
        if(validInputData(eventName, squadName, localDate)){
            if(squadName==null)
                squadName = participateView.getSquadName();
            if(eventName==null)
                eventName = participateView.getEventName();
            if(localDate==null)
                date1 = participateView.getEventDate();

            participateDao.update(buildParticipate(eventName, squadName, participateView.getParticipateId()));
            changeWindow("/participate-table-view.fxml");
            changeButton.getScene().getWindow().hide();

        }


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        squadHash = squadDao.getIdAndNames();
        eventHash = eventDao.getIdAndNames();

        ParticipateView participateView = ParticipateTableView.getParticipateSt();

        ObservableList<String> events =
                FXCollections.observableArrayList(eventHash.values());

        ObservableList<String> squads =
                FXCollections.observableArrayList(squadHash.values());

        squadChoice.setItems(squads);
        eventChoice.setItems(events);
    }

    private Participate buildParticipate(String eventName, String squadName, Long id) {
        Long squadId = null;
        for (Map.Entry entry : squadHash.entrySet()) {
            if(entry.getValue().toString().equals(squadName)) {
                squadId= (Long) entry.getKey();
            }
        }
        Long eventId = null;
        for (Map.Entry entry : eventHash.entrySet()) {
            if(entry.getValue().toString().equals(eventName)) {
                eventId= (Long) entry.getKey();
            }
        }
        return Participate.builder().eventId(eventId).squadId(squadId).id(id).build();
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

}
