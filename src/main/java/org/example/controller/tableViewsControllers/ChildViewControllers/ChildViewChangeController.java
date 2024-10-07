package org.example.controller.tableViewsControllers.ChildViewControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.controller.tableViewsControllers.ChildViewControllers.ChildTableViewController;
import org.example.dao.ChildDao;
import org.example.dao.view.AllChildViewDao;
import org.example.entity.*;
import org.example.service.RoomService;
import org.example.service.SportSectionService;
import org.example.service.SquadService;
import org.example.service.impl.RoomServiceImpl;
import org.example.service.impl.SportSectionServiceImpl;
import org.example.service.impl.SquadServiceImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChildViewChangeController extends Changeable implements Initializable {
    private final SquadService squadService = new SquadServiceImpl();
    private final RoomService roomService = new RoomServiceImpl();
    private final SportSectionService sportSectionService = new SportSectionServiceImpl();
    private final AllChildViewDao allChildViewDao =
            new AllChildViewDao();
    private final ChildDao childDao = ChildDao.getInstance();

    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeChildButton;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField nameText;

    @FXML
    private ChoiceBox<String> roomChoice;

    @FXML
    private TextField secondNameText;

    @FXML
    private ChoiceBox<String> squadChoice;

    @FXML
    private ChoiceBox<String> ssChioice;
    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {
        changeWindow("/child-test-table-view.fxml");
        backToMeinMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        ChildView childView = ChildTableViewController.getChildView();
        if(validData(childView)){
            String name = nameText.getText();
            String secondName = secondNameText.getText();
            String squadName = squadChoice.getValue();
            String ssName = ssChioice.getValue();
            String roomName = roomChoice.getValue();
            if(name.equals(""))
                name=childView.getFirstName();
            if(secondName.equals(""))
                secondName=childView.getSecondName();
            if(squadName==null)
                squadName=childView.getSquadName();
            if(roomName==null)
                roomName=childView.getRoomNumber();
            if(ssName==null)
                ssName=childView.getSportSectionName();

            String[]  parts = roomName.split(" ");
            roomName = parts[0];
            childDao.update(Child.builder()
                            .id(childView.getChildId())
                            .firstName(name)
                            .secondName(secondName)
                            .squadId(squadService.getIdByName(squadName).get())
                            .roomId(roomService.findIdByRoomNumber(roomName).get())
                            .sportSectionId(sportSectionService.findIdByName(ssName).get())
                    .build());
            changeWindow("/child-test-table-view.fxml");
            changeChildButton.getScene().getWindow().hide();
        }else{

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChildView childView = ChildTableViewController.getChildView();
        headerLabel.setText("вы изменяете ребенка " + childView.getFirstName()+" "+childView.getSecondName()+"\n"+"Отряд: "+childView.getSquadName()+", комната: "+childView.getRoomNumber()+"\nСпортивная секция: "+childView.getSportSectionName());
        ObservableList<String> squads =
                FXCollections.observableArrayList(getSquadNames());

        ObservableList<String> rooms =
                FXCollections.observableArrayList(getRoomNumbers());

        ObservableList<String> sportSections =
                FXCollections.observableArrayList(getSportSectionNames());

        squadChoice.getItems().addAll(squads);
        roomChoice.getItems().addAll(rooms);
        ssChioice.getItems().addAll(sportSections);
    }

    private boolean validData(ChildView childView){
        if(squadChoice.getValue()==null && ssChioice.getValue()==null && secondNameText.getText().equals("") && roomChoice.getValue()==null && nameText.getText().equals("")){
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
        alert.setContentText("вы меняете информацию о ребенке : " +childView.getFirstName() + " " + childView.getSecondName() + " из отряда: " + childView.getSquadName()  + " комнаты: " + childView.getRoomNumber() + " спортивная секции: " + childView.getSportSectionName() + "\n" + "" + "на " +"" + nameText.getText() + " " + secondNameText.getText() + " squad: " + squadChoice.getValue() + " sport section: " + ssChioice.getValue() + " room namber: " + roomChoice.getValue());

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    private List<String> getSquadNames(){
        List<Squad> squads = squadService.getAllSquads();

        List<String> names = new ArrayList<>();
        for (Squad squad : squads) {
            names.add(squad.getName());
        }
        return names;
    }

    private List<String> getRoomNumbers(){
        List<ChildView> childViews = allChildViewDao.getView();
        List<String> numbers = new ArrayList<>();
        for (ChildView childView : childViews) {
            numbers.add(childView.getRoomNumber());
        }
        return numbers;
    }

    private List<String> getSportSectionNames(){
        List<SportSection> sportSections = sportSectionService.getAllSportSections();

        List<String> names = new ArrayList<>();

        for (SportSection sportSection : sportSections) {
            names.add(sportSection.getName());
        }

        return names;
    }
}

