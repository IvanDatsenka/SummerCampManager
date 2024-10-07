package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TableViewController extends Changeable{

    @FXML
    private Label tableNameLabel;

    @FXML
    private MenuButton menuButton;

    @FXML
    private Button tableViewBackToMainMenu;

    @FXML
    private Button tableViewSortButton;

    @FXML
    private Button tableChoiseButton;

    @FXML
    private Label enterButtonLabel;

    @FXML
    void onChangeRolesButtonClick(ActionEvent event) {
        changeWindow("/change-role-view.fxml");
        tableViewBackToMainMenu.getScene().getWindow().hide();
    }


    @FXML
    private void initialize(){
        if (CurrentUserRole.getCurrentUserRole()==true){
            MenuItem item1 = new MenuItem("Ребёнок");
            MenuItem item2 = new MenuItem("Отряд");
            MenuItem item3 = new MenuItem("Смена");
            MenuItem item4 = new MenuItem("Мероприятие");
            MenuItem item5 = new MenuItem("Корпус");
            MenuItem item6 = new MenuItem("Сотрудники");
            MenuItem item7 = new MenuItem("Должность");
            MenuItem item8 = new MenuItem("Участие");
            MenuItem item9 = new MenuItem("Комната");
            MenuItem item10 = new MenuItem("Спортивная секция");

            menuButton.getItems().addAll(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10);


            item2.setOnAction(actionEvent -> changeViewToTableView(item2.getText()));
            item1.setOnAction(actionEvent -> changeViewToTableView(item1.getText()));
            item3.setOnAction(actionEvent -> changeViewToTableView(item3.getText()));
            item4.setOnAction(actionEvent -> changeViewToTableView(item4.getText()));
            item5.setOnAction(actionEvent -> changeViewToTableView(item5.getText()));
            item6.setOnAction(actionEvent -> changeViewToTableView(item6.getText()));
            item7.setOnAction(actionEvent -> changeViewToTableView(item7.getText()));
            item8.setOnAction(actionEvent -> changeViewToTableView(item8.getText()));
            item9.setOnAction(actionEvent -> changeViewToTableView(item9.getText()));
            item10.setOnAction(actionEvent -> changeViewToTableView(item10.getText()));
        }else{
            MenuItem item1 = new MenuItem("Ребёнок");
            MenuItem item2 = new MenuItem("Отряд");
            MenuItem item3 = new MenuItem("Смена");
            MenuItem item4 = new MenuItem("Мероприятие");
            MenuItem item5 = new MenuItem("Корпус");
            MenuItem item8 = new MenuItem("Участие");
            MenuItem item9 = new MenuItem("Комната");
            MenuItem item10 = new MenuItem("Спортивная секция");

            menuButton.getItems().addAll(item1, item2, item3, item4, item5, item8, item9, item10);


            item2.setOnAction(actionEvent -> changeViewToTableView(item2.getText()));
            item1.setOnAction(actionEvent -> changeViewToTableView(item1.getText()));
            item3.setOnAction(actionEvent -> changeViewToTableView(item3.getText()));
            item4.setOnAction(actionEvent -> changeViewToTableView(item4.getText()));
            item5.setOnAction(actionEvent -> changeViewToTableView(item5.getText()));
            item8.setOnAction(actionEvent -> changeViewToTableView(item8.getText()));
            item9.setOnAction(actionEvent -> changeViewToTableView(item9.getText()));
            item10.setOnAction(actionEvent -> changeViewToTableView(item10.getText()));
        }
    }


    @FXML
    void onTableChoiceButtonClick(ActionEvent event) {

    }


    @FXML
    void onTableViewBackToMainMenuClick(ActionEvent event) {
        changeWindow("/main-menu.fxml");
        menuButton.getScene().getWindow().hide();
    }

    @FXML
    void onTableViewSortButtonClick(ActionEvent event) {

    }

    @FXML
    void onMenuButtonClick(ActionEvent event) {
    }

    public void changeViewToTableView(String text){
        switch (text){
            case "Ребёнок" -> {
                changeWindow("/child-test-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case "Отряд" -> {
                changeWindow("/squad-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Смена" -> {
                changeWindow("/shift-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Мероприятие" -> {
                changeWindow("/event-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Корпус" -> {
                changeWindow("/building-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Сотрудники" -> {
                changeWindow("/employee-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Должность" -> {
                changeWindow("/jobTitle-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Участие" -> {
                changeWindow("/participate-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Комната" -> {
                changeWindow("/room-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            case  "Спортивная секция" -> {
                changeWindow("/sport-table-view.fxml");
                tableChoiseButton.getScene().getWindow().hide();
            }
            default -> {
            }
        }
    }

}
