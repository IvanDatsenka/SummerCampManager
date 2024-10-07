package org.example.controller.tableViewsControllers.ShiftView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.controller.Changeable;
import org.example.controller.tableViewsControllers.JobTitleView.JobTitleTableView;
import org.example.dao.ShiftDao;
import org.example.entity.JobTitle;
import org.example.entity.Shift;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShiftChangeController extends Changeable implements Initializable {

    @FXML
    private Button backToMeinMenuButton;

    @FXML
    private Button changeChildButton;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField shiftNameText;

    @FXML
    private DatePicker te;

    private final ShiftDao shiftDao = ShiftDao.getInstance();

    @FXML
    void onBackToMeinMenuButtonClick(ActionEvent event) {
        changeWindow("/shift-table-view.fxml");
        backToMeinMenuButton.getScene().getWindow().hide();
    }

    @FXML
    void onChangeChildButtonClick(ActionEvent event) {
        Shift shift = ShiftTableController.getShiftSt();
        String shiftName = shiftNameText.getText();
        LocalDate localDate = te.getValue();
        if(shiftName.equals("")&&localDate==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("");
            alert.setContentText("ЗАПОЛНИТЕ ПОЛЯ ВВОДА");
            alert.showAndWait();
            return;
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("подтвердить изменения");
            alert.setHeaderText("");
            alert.setContentText("");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Date date;
                if(localDate==null){
                    date = shift.getDate();
                }else{
                    date=Date.valueOf(localDate);
                }

                if(shiftName.equals(""))
                    shiftName=shift.getName();

                shiftDao.update(Shift.builder().id(shift.getId()).name(shiftName).date(date).build());
            }
            changeWindow("/shift-table-view.fxml");
            changeChildButton.getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        headerLabel.setText("вы изменяете название смены "+ ShiftTableController.getShiftSt().getName());
    }
}
