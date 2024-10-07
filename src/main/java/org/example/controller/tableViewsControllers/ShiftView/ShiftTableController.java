package org.example.controller.tableViewsControllers.ShiftView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.controller.Changeable;
import org.example.dao.ShiftDao;
import org.example.entity.JobTitle;
import org.example.entity.Shift;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ShiftTableController extends Changeable implements Initializable {
    private final ShiftDao shiftDao = ShiftDao.getInstance();
    @FXML
    private Button backButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button isertDataButton;

    @FXML
    private TableColumn<Shift, Date> shiftDateColumn;

    @FXML
    private DatePicker shiftDateInput;

    @FXML
    private TableColumn<Shift, String> shiftNameColumn;

    @FXML
    private TextField shiftNameText;

    @FXML
    private TableView<Shift> table;

    private static Shift shiftSt;

    public static Shift getShiftSt(){
        return shiftSt;
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        changeWindow("/talbe-view.fxml");
        backButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeButtonClick(ActionEvent event) {
        Shift shift = table.getSelectionModel().getSelectedItem();

        if(!table.getItems().contains(shift)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("");
            alert.setContentText("выберите строку");
            alert.showAndWait();
            return;
        }

        shiftSt = shift;
        changeWindow("/shift-change-view.fxml");
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

        Shift shift= table.getSelectionModel().getSelectedItem();

        if(!shiftDao.delete(shift.getId())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("ошибка удаления");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("смена "+ shift.getName() + " " + " удалена");
            alert.showAndWait();
        }
        table.getItems().remove(shift);
    }

    private void fillInTableView(){
        shiftNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        shiftDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.setItems(initiate());
    }

    private ObservableList<Shift> initiate() {
        return FXCollections.observableArrayList(shiftDao.findAll());
    }

    @FXML
    void onInsertDataButtonClick(ActionEvent event) {
        String shiftName = shiftNameText.getText();
        LocalDate localDate = shiftDateInput.getValue();

        if(shiftName.equals("")||localDate==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("некоректный ввод");
            alert.showAndWait();
        }else{
            Shift savedShift = shiftDao.save(Shift.builder().name(shiftName).date(Date.valueOf(localDate)).build());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("сообщение");
            alert.setHeaderText(null);
            alert.setContentText("вы добавили новую смену: " + savedShift.getName()+" "+ savedShift.getDate());
            alert.showAndWait();
            fillInTableView();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillInTableView();
    }
}
