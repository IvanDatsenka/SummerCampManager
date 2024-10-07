package org.example.controller.tableViewsControllers.EmployeeView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.EmployeeDao;
import org.example.dao.JobTitleDao;
import org.example.dao.view.EmployeeViewDao;
import org.example.entity.Employee;
import org.example.entity.Event;
import org.example.entity.JobTitle;
import org.example.entity.views.EmployeeView;
import org.example.entity.views.EventView;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EmployeeTableView  extends Changeable implements Initializable {

    private final EmployeeViewDao employeeViewDao = new EmployeeViewDao();

    private final EmployeeDao employeeDao = EmployeeDao.getInstance();

    private final JobTitleDao jobTitleDao = JobTitleDao.getInstance();
    @FXML
    private Button backButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<EmployeeView, String> firstNameColumn;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private Button isertDataButton;

    @FXML
    private ChoiceBox<String> jobChoice;

    @FXML
    private TableColumn<EmployeeView, String> jobColumn;

    @FXML
    private TextField secondNameTextField;

    @FXML
    private TableView<EmployeeView> table;

    private  HashMap<Long,String> jobsHash;
    private static EmployeeView employeeViewSt;

    public static EmployeeView getEmployeeViewSt(){
        return employeeViewSt;
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {

        EmployeeView employeeView = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(employeeView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        employeeViewSt = employeeView;
        changeWindow("/employee-change-view.fxml");
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

        EmployeeView employeeView = table.getSelectionModel().getSelectedItem();

        if(!employeeViewDao.deleteEventFromEventView(employeeView)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("отряд "+ employeeView.getEmployeeFullName() + " " + " удалён");
            alert.showAndWait();
        }
        table.getItems().remove(employeeView);
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String firstName = firstNameTextField.getText();
        String secondName = secondNameTextField.getText();
        String jobName = jobChoice.getValue();


        if(firstName.equals("")||secondName.equals("")||jobName==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректные данные, проверьте ввод");
            alert.showAndWait();
        }else{
            Long jobId = null;
            for (Map.Entry entry : jobsHash.entrySet()) {
                if(entry.getValue().toString().equals(jobName)) {
                    jobId= (Long) entry.getKey();
                }
            }


           Employee employee = Employee.builder().jobTitleId(jobId).firstName(firstName).secondName(secondName).build();
            Employee employee1 = employeeDao.save(employee);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили нового сотрудника: " + employee1.getFirstName()+" "+ employee1.getSecondName());
            alert.showAndWait();
            fillInTableView();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();

        jobsHash = jobTitleDao.getIdAndNames();
        ObservableList<String> jpbTitles =
                FXCollections.observableArrayList(jobsHash.values());

        jobChoice.setItems(jpbTitles);
    }

    private void fillInTableView(){
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFullName"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitleName"));
        table.setItems(initiate());
    }

    private ObservableList<EmployeeView> initiate(){
        return FXCollections.observableArrayList(employeeViewDao.getView());
    }
}
