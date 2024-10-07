package org.example.controller.tableViewsControllers.EmployeeView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.controller.tableViewsControllers.EventView.EventTableViewController;
import org.example.dao.EmployeeDao;
import org.example.dao.JobTitleDao;
import org.example.dao.view.EmployeeViewDao;
import org.example.entity.Employee;
import org.example.entity.Event;
import org.example.entity.views.EmployeeView;
import org.example.entity.views.EventView;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeChangeView extends Changeable implements Initializable {
    private final EmployeeViewDao employeeViewDao = new EmployeeViewDao();
    private final JobTitleDao jobTitleDao = JobTitleDao.getInstance();
    private final EmployeeDao employeeDao = EmployeeDao.getInstance();

    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeChildButton;

    @FXML
    private TextField firstNameText;

    @FXML
    private Label headerLabel;

    @FXML
    private ChoiceBox<String> jobChoice;

    @FXML
    private TextField secondNameText;

    private HashMap<Long, String> jobHash;

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {

    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        EmployeeView employeeView = EmployeeTableView.getEmployeeViewSt();

        Long id = employeeView.getEmployeeId();
        String fullName = employeeView.getEmployeeFullName();
        String[] cred = fullName.split(" ");
        String firstNameFromView = cred[cred.length-2];
        String secondNameFromView = cred[cred.length-1];

       String firstName = firstNameText.getText();
       String secondName = secondNameText.getText();
       String jobTitle = jobChoice.getValue();

        if(validInputData(firstName, secondName, jobTitle)){
            if(firstName.equals("")) {
                firstName = firstNameFromView;
            }
            if(secondName.equals("")){
                secondName=secondNameFromView;
            }
            if(jobTitle==null)
                jobTitle = employeeView.getJobTitleName();

            employeeDao.update(buildEmployee(id,firstName, secondName, jobTitle));
            changeWindow("/employee-table-view.fxml");
            changeChildButton.getScene().getWindow().hide();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmployeeView employeeView = EmployeeTableView.getEmployeeViewSt();

        jobHash=jobTitleDao.getIdAndNames();

        ObservableList<String> employees =
                FXCollections.observableArrayList(jobHash.values());

        jobChoice.setItems(employees);
    }

    private Employee buildEmployee(Long id, String firstName, String secondName, String jobTitle) {
        jobHash = jobTitleDao.getIdAndNames();
        Long jobTitleId = null;
        for (Map.Entry entry : jobHash.entrySet()) {
            if(entry.getValue().toString().equals(jobTitle)) {
                jobTitleId = (Long) entry.getKey();
            }
        }

        return Employee.builder().id(id).jobTitleId(jobTitleId).firstName(firstName).secondName(secondName).build();

    }

    private boolean validInputData(String name, String employee, String title){
        if(name.equals("") && employee==null && title == null) {
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
