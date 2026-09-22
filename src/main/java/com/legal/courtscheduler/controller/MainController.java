package com.legal.courtscheduler.controller;

import com.legal.courtscheduler.entity.TrackedCase;
import com.legal.courtscheduler.service.TrackedCaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainController {
    private final TrackedCaseService trackedCaseService;

    @FXML
    private TableView<TrackedCase> caseTable;
    @FXML
    private TableColumn<TrackedCase, Long> colId;
    @FXML
    private TableColumn<TrackedCase, String> colCaseNumber;
    @FXML
    private TableColumn<TrackedCase, String> colCourtName;

    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCaseNumber.setCellValueFactory(new PropertyValueFactory<>("caseNumber"));
        colCourtName.setCellValueFactory(new PropertyValueFactory<>("courtName"));

        loadCases();
    }

    public void loadCases(){
        List<TrackedCase> cases = trackedCaseService.getAllCases();
        ObservableList<TrackedCase> observableCases = FXCollections.observableArrayList(cases);
        caseTable.setItems(observableCases);
    }
}
