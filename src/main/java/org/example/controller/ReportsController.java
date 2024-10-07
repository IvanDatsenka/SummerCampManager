package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.example.dao.EventDao;
import org.example.entity.Event;
import org.example.service.reports.Report1Service;
import org.example.service.reports.Report2Service;
import org.example.service.reports.Report3Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsController extends Changeable implements Initializable {

    private final Report2Service report2Service = new Report2Service();
    private final Report3Service report3Service = new Report3Service();
    private final Report1Service report1Service = new Report1Service();
    private final EventDao eventDao = EventDao.getInstance();
    @FXML
    private Button backFromReportsMenu;

    @FXML
    private ChoiceBox<String> chooseEvent;

    @FXML
    private Button choosenSquadReportButton;

    @FXML
    private Button firstReport;

    @FXML
    private Button referenceBook;

    @FXML
    void onBackFromReportsMenuClick(ActionEvent event) {
        changeWindow("/main-menu.fxml");
        backFromReportsMenu.getScene().getWindow().hide();
    }

    @FXML
    void onChoosenSquadReportButtonClick(ActionEvent event) throws IOException {
        if (chooseEvent.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ошибка");
            alert.setHeaderText("пустые данные");
            alert.setContentText("выберите мероприятие");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("сообщение");
            alert.setHeaderText("");
            alert.setContentText("отчет по участникам мероприятия "+chooseEvent.getValue()+" создан");
            alert.showAndWait();
            report2Service.createSecondReport(chooseEvent.getValue());
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            desktop.open(new File("report2.xls"));
        }
    }

    @FXML
    void onReferenceBookClick(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("сообщение");
        alert.setHeaderText("");
        alert.setContentText("отчет по сотрудникам создан!!!");
        alert.showAndWait();
        report3Service.createReport3();
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        desktop.open(new File("report3.xls"));
    }

    @FXML
    void onShiftReportButtonClick(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("сообщение");
        alert.setHeaderText("");
        alert.setContentText("отчет количеству детей в отряде создан!!!");
        alert.showAndWait();
        report1Service.createFirstReport();
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        desktop.open(new File("report1.xls"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(CurrentUserRole.getCurrentUserRole()==false){
            referenceBook.setVisible(false);
        }
        List<Event> eventE= eventDao.findAll();
        List<String> names = new ArrayList<>();
        for (Event e :eventE) {
            names.add(e.getName());
        }
        ObservableList<String> events =
                FXCollections.observableArrayList(names);

        chooseEvent.setItems(events);
    }
}
