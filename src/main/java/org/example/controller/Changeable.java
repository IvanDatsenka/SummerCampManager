package org.example.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Changeable {
    public void changeWindow(String viewFilesName){
        List<String> fullScreenFileNames = new ArrayList<>();
        fullScreenFileNames.addAll(List.of("/child-test-table-view.fxml",
                "/squad-table-view.fxml","/participate-table-view.fxml",
                "/event-table-view.fxml","/employee-table-view.fxml",
                "/sport-table-view.fxml", "/shift-table-view.fxml",
                "/room-table-view.fxml", "/jobTitle-table-view.fxml",
                "/building-table-view.fxml"
                ));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewFilesName));
            Parent root1 = fxmlLoader.load();
            Stage stage1 = new Stage();
            if(fullScreenFileNames.contains(viewFilesName))
                stage1.setFullScreen(true);
            stage1.setFullScreenExitHint("");
            stage1.setScene(new Scene(root1, 600, 400));
            stage1.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
