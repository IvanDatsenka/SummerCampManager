package org.example.controller.tableViewsControllers.JobTitleView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.JobTitleDao;
import org.example.entity.Building;
import org.example.entity.JobTitle;

import java.net.URL;
import java.util.ResourceBundle;

public class JobTitleTableView extends Changeable implements Initializable {
    private final JobTitleDao jobTitleDao = JobTitleDao.getInstance();

    private static JobTitle jobTitleSt;

    public static JobTitle getJobTitleSt(){
        return jobTitleSt;
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
    private TableColumn<JobTitle, String> jobNameColumn;

    @FXML
    private TextField jobTextField;

    @FXML
    private TableView<JobTitle> table;

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        JobTitle jobTitle = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(jobTitle)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        jobTitleSt = jobTitle;
        changeWindow("/jobTitle-change-view.fxml");
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

        JobTitle jobTitle = table.getSelectionModel().getSelectedItem();

        if(!jobTitleDao.delete(jobTitle.getId())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("должность "+ jobTitle.getName() + " " + " удалена");
            alert.showAndWait();
        }
        table.getItems().remove(jobTitle);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String jobName = jobTextField.getText();

        if(jobName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректная информация, проверьте ввод");
            alert.showAndWait();
        }else{
            JobTitle savedJobTitle = jobTitleDao.save(JobTitle.builder().name(jobName).build());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили новую должность: " + savedJobTitle.getName());
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

    private ObservableList<JobTitle> initiate() {
        return FXCollections.observableArrayList(jobTitleDao.findAll());
    }


}
