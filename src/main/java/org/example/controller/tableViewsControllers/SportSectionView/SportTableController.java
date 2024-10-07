package org.example.controller.tableViewsControllers.SportSectionView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.SportSectionDao;
import org.example.entity.JobTitle;
import org.example.entity.SportSection;

import java.net.URL;
import java.util.ResourceBundle;

public class SportTableController extends Changeable implements Initializable {

    private final SportSectionDao ssDao = SportSectionDao.getInstance();

    private static SportSection sportSectionSt;

    public static SportSection getSportSectionSt(){
        return sportSectionSt;
    }

    @FXML
    private Button backButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button isertDataButton;

    @FXML
    private TableColumn<SportSection, String> jobNameColumn;

    @FXML
    private TextField jobTextField;

    @FXML
    private TableView<SportSection> table;

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        SportSection sportSection = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(sportSection)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        sportSectionSt = sportSection;
        changeWindow("/sport-change-table.fxml");
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

        SportSection sportSection = table.getSelectionModel().getSelectedItem();

        if(!ssDao.delete(sportSection.getId())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("спортивная секция "+ sportSection.getName() + " " + " удалена");
            alert.showAndWait();
        }
        table.getItems().remove(sportSection);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String ssName = jobTextField.getText();

        if(ssName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректные данные");
            alert.showAndWait();
        }else{
            SportSection savedSS = ssDao.save(SportSection.builder().name(ssName).build());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили спортивную секцию : " + savedSS.getName());
            alert.showAndWait();
            fillInTableView();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();
    }

    private void fillInTableView(){
        jobNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.setItems(initiate());
    }

    private ObservableList<SportSection> initiate() {
        return FXCollections.observableArrayList(ssDao.findAll());
    }

}

