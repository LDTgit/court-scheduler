package com.legal.courtscheduler.controller;

import com.legal.courtscheduler.entity.TrackedCase;
import com.legal.courtscheduler.service.TrackedCaseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;


public class CaseDetailsDialogController {
    @FXML
    private TextField caseNumberField;
    @FXML
    private TextField courtField;
    @FXML
    private TextField objectField;
    @FXML
    private TextField stageField;
    @FXML
    private TextField sectionField;
    @FXML
    private TextArea partiesArea;
    @FXML
    private TextField notesField;
    @FXML
    private CheckBox delegationCheckBox;

    @Setter
    private TrackedCase trackedCase;
    @Setter
    private TrackedCaseService trackedCaseService;
    @Setter
    private Stage dialogStage;

    private boolean deleted = false;

    public void initData(TrackedCase trackedCase){
        this.trackedCase = trackedCase;
        caseNumberField.setText(trackedCase.getCaseNumber());
        courtField.setText(trackedCase.getCourtName());
        objectField.setText(trackedCase.getObject());
        stageField.setText(trackedCase.getStage());
        sectionField.setText(trackedCase.getSection());
        partiesArea.setText(trackedCase.getParties());
        notesField.setText(trackedCase.getCustomNotes());
        delegationCheckBox.setSelected(trackedCase.isDelegationFiled());
    }

    @FXML
    private void handleSave(){
        trackedCase.setCaseNumber(caseNumberField.getText());
        trackedCase.setCourtName(courtField.getText());
        trackedCase.setObject(objectField.getText());
        trackedCase.setStage(stageField.getText());
        trackedCase.setParties(partiesArea.getText());
        trackedCase.setCustomNotes(notesField.getText());
        trackedCase.setDelegationFiled(delegationCheckBox.isSelected());

        trackedCaseService.saveCase(trackedCase);
        dialogStage.close();
    }

    @FXML
    private void handleDeleteCase(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare ștergere");
        alert.setHeaderText("Sigur vreți să îl ștergem?");
        alert.setContentText("Vor fi șterse toate ședințele aferente dosarului.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK){
            trackedCaseService.deleteCascadeWithHearings(trackedCase);
            deleted=true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    public boolean isDeleted(){
        return deleted;
    }
}
