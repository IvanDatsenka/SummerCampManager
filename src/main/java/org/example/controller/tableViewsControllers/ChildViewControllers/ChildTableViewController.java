package org.example.controller.tableViewsControllers.ChildViewControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.controller.Changeable;
import org.example.dao.view.AllChildViewDao;
import org.example.entity.*;
import org.example.service.*;
import org.example.service.impl.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChildTableViewController extends Changeable implements Initializable {
    private final ChildTableViewTableService childTableViewTableService =
            new ChildTableViewTableServiceImpl();
    private final AllChildViewDao allChildViewDao =
            new AllChildViewDao();

    private final SquadService squadService = new SquadServiceImpl();
    private final RoomService roomService = new RoomServiceImpl();
    private final SportSectionService sportSectionService = new SportSectionServiceImpl();
    private final ChildService childService = new ChildServiceImpl();

    @FXML
    private Button backButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<ChildView, String> firstNameColumn;

    @FXML
    private TextField firstNameText;

    @FXML
    private Button isertDataButton;

    @FXML
    private TableColumn<ChildView, String> roomColumn;

    @FXML
    private TableColumn<ChildView, String> secondNameColumn;

    @FXML
    private TextField secondNameText;

    @FXML
    private TableColumn<ChildView, String> sportSectionColumn;

    @FXML
    private TableColumn<ChildView, String> squadColumn;

    @FXML
    private TableView<ChildView> table;

    @FXML
    private ChoiceBox<String> roomChoice;

    @FXML
    private ChoiceBox<String> squadChoice;

    @FXML
    private ChoiceBox<String> sportSectionChoice;
    private static ChildView childViewSt;
    public static ChildView getChildView(){
        return childViewSt;
    }
    private void fillInTableView(){
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        secondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        sportSectionColumn.setCellValueFactory(new PropertyValueFactory<>("squadName"));
        squadColumn.setCellValueFactory(new PropertyValueFactory<>("sportSectionName"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        table.setItems(initiateData());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();
        ObservableList<String> squads =
                FXCollections.observableArrayList(getSquadNames());

        ObservableList<String> rooms =
                FXCollections.observableArrayList(getRoomNumbers().stream().distinct().toList());

        ObservableList<String> sportSections =
                FXCollections.observableArrayList(getSportSectionNames());

        squadChoice.getItems().addAll(squads);
        roomChoice.getItems().addAll(rooms);
        sportSectionChoice.getItems().addAll(sportSections);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String name = firstNameText.getText();
        String secondName = secondNameText.getText();
        String squadName = squadChoice.getValue();
        String sportSectionName = sportSectionChoice.getValue();
        String roomNumber1 = roomChoice.getValue();
        String[]  parts = roomNumber1.split(" ");
        String roomNumber = parts[0];
        if(name=="" || secondName=="" || squadName==null || sportSectionName==null || roomNumber==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректная информация");
            alert.showAndWait();
        }else{
            childService.addChildFromTableView(name, secondName, squadName, sportSectionName, roomNumber);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили нового ребёнка: " + name + " "+ secondName+"\n в отряд "+ squadName);
            alert.showAndWait();
            fillInTableView();
        }
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) throws IOException {
        ChildView childView = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(childView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        childViewSt = childView;
        changeWindow("/change-child-table-view.fxml");
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

        ChildView selectedChildView = table.getSelectionModel().getSelectedItem();

        if(!allChildViewDao.deleteChildFromChildView(selectedChildView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("ребёнок "+ selectedChildView.getFirstName() + " " + selectedChildView.getSecondName() + " удален");
            alert.showAndWait();
        }
        table.getItems().remove(selectedChildView);
    }
    private ObservableList<ChildView> initiateData(){
        return FXCollections.<ChildView>observableArrayList(allChildViewDao.getView());
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
