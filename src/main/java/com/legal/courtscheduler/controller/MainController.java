package com.legal.courtscheduler.controller;

import com.legal.courtscheduler.entity.Client;
import com.legal.courtscheduler.entity.Hearing;
import com.legal.courtscheduler.entity.TrackedCase;
import com.legal.courtscheduler.service.JusticeApiService;
import com.legal.courtscheduler.service.TrackedCaseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MainController {
    private final TrackedCaseService trackedCaseService;
    private final JusticeApiService justiceApiService;

//    FXML Components: Calendar fo 7 days
    @FXML
    private TableView<Hearing> upcomingHearingsTable;
    @FXML
    private TableColumn<Hearing, LocalDateTime> colHearingDate;
    @FXML
    private TableColumn<Hearing, String> colHearingCaseNum;
    @FXML
    private TableColumn<Hearing, String> colHearingCourt;
    @FXML
    private TableColumn<Hearing, String> colHearingStage;

//    FXML Components: Client and case file Form
    @FXML private RadioButton existingClientRadio;
    @FXML private RadioButton newClientRadio;
    @FXML private VBox existingClientBox;
    @FXML private VBox newClientBox;
    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private TextField newClientNameField;
    @FXML
    private TextField newClientContractField;

    @FXML
    private TextField caseNumberField;
    @FXML
    private ComboBox<String> courtComboBox;
    @FXML
    private TextField notesField;
    @FXML
    private CheckBox delegationCheckBox;
    @FXML
    private Button saveCaseButton;

//    FXML Components: Search and Main Table
    @FXML private RadioButton searchByNumberRadio;
    @FXML private RadioButton searchByClientRadio;
    @FXML private TextField searchCaseField;
//    @FXML
//    private ComboBox<Client> filterClientComboBox;
    @FXML
    private TableView<TrackedCase> caseTable;
    @FXML
    private TableColumn<TrackedCase, String> colClientName;
    @FXML
    private TableColumn<TrackedCase, String> colCaseNumber;
    @FXML
    private TableColumn<TrackedCase, String> colCourtName;
    @FXML
    private TableColumn<TrackedCase, String> colDelegation;

    @FXML
    public void initialize(){
        colClientName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient() != null ? cellData.getValue().getClient().getName() : "N/A"));
        colCaseNumber.setCellValueFactory(new PropertyValueFactory<>("caseNumber"));
        colCourtName.setCellValueFactory(new PropertyValueFactory<>("courtName"));
        colDelegation.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isDelegationFiled() ? "Da" : "Nu"));

        colHearingDate.setCellValueFactory(new PropertyValueFactory<>("hearingDateTime"));
        colHearingCaseNum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrackedCase() !=null ? cellData.getValue().getTrackedCase().getCaseNumber() : ""));
        colHearingCourt.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrackedCase() != null ? cellData.getValue().getTrackedCase().getCourtName() : ""));
        colHearingStage.setCellValueFactory(new PropertyValueFactory<>("stage"));

        courtComboBox.setItems(FXCollections.observableArrayList("Curtea de Apel BUCURESTI", "Curtea Militara de  Apel  BUCURESTI", "Tribunalul BUCURESTI", "Tribunalul Militar BUCURESTI", "Tribunalul Militar Teritorial BUCURESTI", "Judecatoria SECTORUL 1 BUCURESTI", "Judecatoria SECTORUL 2 BUCURESTI", "Judecatoria SECTORUL 3 BUCURESTI", "Judecatoria SECTORUL 4 BUCURESTI", "Judecatoria SECTORUL 5 BUCURESTI", "Judecatoria SECTORUL 6 BUCURESTI", "Curtea de Apel  ALBA IULIA", "Curtea de Apel  BRASOV", "Curtea de Apel  CRAIOVA", "Curtea de Apel  GALATI", "Curtea de Apel  IASI", "Curtea de Apel  PITESTI", "Curtea de Apel  TIMISOARA", "Curtea de Apel BACAU", "Curtea de Apel CLUJ", "Curtea de Apel CONSTANTA", "Curtea de Apel ORADEA", "Curtea de Apel PLOIESTI", "Curtea de Apel SUCEAVA", "Curtea de Apel TARGU MURES", "Judecatoria ADJUD", "Judecatoria AGNITA", "Judecatoria AIUD", "Judecatoria ALBAIULIA", "Judecatoria ALESD", "Judecatoria ALEXANDRIA", "Judecatoria ARAD", "Judecatoria AVRIG", "Judecatoria BABADAG", "Judecatoria BACAU", "Judecatoria BAIA DE ARAMA", "Judecatoria BAIA MARE", "Judecatoria BAILESTI", "Judecatoria BALCESTI", "Judecatoria BALS", "Judecatoria BARLAD", "Judecatoria BECLEAN", "Judecatoria BEIUS", "Judecatoria BICAZ", "Judecatoria BISTRITA", "Judecatoria BLAJ", "Judecatoria BOLINTIN VALE", "Judecatoria BOTOSANI", "Judecatoria BOZOVICI", "Judecatoria BRAD", "Judecatoria BRAILA", "Judecatoria BRASOV", "Judecatoria BREZOI", "Judecatoria BUFTEA", "Judecatoria BUHUSI", "Judecatoria BUZAU", "Judecatoria CALAFAT", "Judecatoria CALARASI", "Judecatoria CAMPENI", "Judecatoria CAMPINA", "Judecatoria CAMPULUNG MOLDOVENESC", "Judecatoria CAMPULUNG", "Judecatoria CARACAL", "Judecatoria CARANSEBES", "Judecatoria CAREI", "Judecatoria CHISINEU CRIS", "Judecatoria CLUJ NAPOCA", "Judecatoria CONSTANTA", "Judecatoria CORABIA", "Judecatoria CORNETU", "Judecatoria COSTESTI", "Judecatoria CRAIOVA", "Judecatoria CURTEA DE ARGES", "Judecatoria Darabani", "Judecatoria DEJ", "Judecatoria DETA", "Judecatoria DEVA", "Judecatoria DOROHOI", "Judecatoria DRAGASANI", "Judecatoria DRAGOMIRESTI", "Judecatoria DROBETA TURNU SEVERIN", "Judecatoria FAGARAS", "Judecatoria FAGET", "Judecatoria FALTICENI", "Judecatoria FAUREI", "Judecatoria FETESTI", "Judecatoria FILIASI", "Judecatoria FOCSANI", "Judecatoria GAESTI", "Judecatoria GALATI", "Judecatoria GHEORGHENI", "Judecatoria GHERLA", "Judecatoria GIURGIU", "Judecatoria GURA HUMORULUI", "Judecatoria GURAHONT", "Judecatoria HARLAU", "Judecatoria HARSOVA", "Judecatoria HATEG", "Judecatoria HOREZU", "Judecatoria HUEDIN", "Judecatoria HUNEDOARA", "Judecatoria HUSI", "Judecatoria IASI", "Judecatoria INEU", "Judecatoria INSURATEI", "Judecatoria INTORSURA BUZAULUI", "Judecatoria JIBOU", "Judecatoria LEHLIU GARA", "Judecatoria LIESTI", "Judecatoria LIPOVA", "Judecatoria LUDUS", "Judecatoria LUGOJ", "Judecatoria MACIN", "Judecatoria MANGALIA", "Judecatoria MARGHITA", "Judecatoria MEDGIDIA", "Judecatoria MEDIAS", "Judecatoria MIERCUREA CIUC", "Judecatoria MIZIL", "Judecatoria MOINESTI", "Judecatoria MOLDOVA NOUA", "Judecatoria MORENI", "Judecatoria MOTRU", "Judecatoria MURGENI", "Judecatoria NASAUD", "Judecatoria NEGRESTI OAS", "Judecatoria NOVACI", "Judecatoria ODORHEIUL SECUIESC", "Judecatoria OLTENITA", "Judecatoria ONESTI", "Judecatoria ORADEA", "Judecatoria ORASTIE", "Judecatoria ORAVITA", "Judecatoria ORSOVA", "Judecatoria PANCIU", "Judecatoria PASCANI", "Judecatoria PATARLAGELE", "Judecatoria PETROSANI", "Judecatoria PIATRA NEAMT", "Judecatoria PITESTI", "Judecatoria PLOIESTI", "Judecatoria PODU TURCULUI", "Judecatoria POGOANELE", "Judecatoria PUCIOASA", "Judecatoria RACARI", "Judecatoria RADAUTI", "Judecatoria RADUCANENI", "Judecatoria RAMNICU SARAT", "Judecatoria RAMNICU VALCEA", "Judecatoria REGHIN", "Judecatoria RESITA", "Judecatoria ROMAN", "Judecatoria ROSIORI DE VEDE", "Judecatoria RUPEA", "Judecatoria SALISTE", "Judecatoria SALONTA", "Judecatoria SANNICOLAUL MARE", "Judecatoria SATU MARE", "Judecatoria SAVENI", "Judecatoria SEBES", "Judecatoria SEGARCEA", "Judecatoria SFANTU GHEORGHE", "Judecatoria SIBIU", "Judecatoria SIGHETU MARMATIEI", "Judecatoria SIGHISOARA", "Judecatoria SIMLEUL SILVANIEI", "Judecatoria SINAIA", "Judecatoria SLATINA", "Judecatoria SLOBOZIA", "Judecatoria SOMCUTA MARE", "Judecatoria STREHAIA", "Judecatoria SUCEAVA", "Judecatoria TARGOVISTE", "Judecatoria TARGU BUJOR", "Judecatoria TARGU CARBUNESTI", "Judecatoria TARGU JIU", "Judecatoria TARGU MURES", "Judecatoria TARGU NEAMT", "Judecatoria TARGU SECUIESC", "Judecatoria TARGUL APUS", "Judecatoria TARNAVENI", "Judecatoria TECUCI", "Judecatoria TIMISOARA", "Judecatoria TOPLITA", "Judecatoria TOPOLOVENI", "Judecatoria TULCEA", "Judecatoria TURDA", "Judecatoria TURNU MAGURELE", "Judecatoria URZICENI", "Judecatoria VALENII DE MUNTE", "Judecatoria VANJU MARE", "Judecatoria VASLUI", "Judecatoria VATRA DORNEI", "Judecatoria VIDELE", "Judecatoria VISEU DE SUS", "Judecatoria ZALAU", "Judecatoria ZARNESTI", "Judecatoria ZIMNICEA", "Tribunalul ALBA", "Tribunalul ARAD", "Tribunalul ARGES", "Tribunalul BACAU", "Tribunalul BIHOR", "Tribunalul BISTRITA NASAUD", "Tribunalul BOTOSANI", "Tribunalul BRAILA", "Tribunalul BRASOV", "Tribunalul BUZAU", "Tribunalul CALARASI", "Tribunalul CARAS SEVERIN", "Tribunalul CLUJ", "Tribunalul Comercial ARGES", "Tribunalul Comercial CLUJ", "Tribunalul Comercial MURES", "Tribunalul CONSTANTA", "Tribunalul COVASNA", "Tribunalul DAMBOVITA", "Tribunalul DOLJ", "Tribunalul GALATI", "Tribunalul GIURGIU", "Tribunalul GORJ", "Tribunalul HARGHITA", "Tribunalul HUNEDOARA", "Tribunalul IALOMITA", "Tribunalul IASI", "Tribunalul ILFOV", "Tribunalul MARAMURES", "Tribunalul MEHEDINTI", "Tribunalul Militar CLUJ NAPOCA", "Tribunalul Militar IASI", "Tribunalul Militar TIMISOARA", "Tribunalul MURES", "Tribunalul NEAMT", "Tribunalul OLT", "Tribunalul pentru minori Si familie BRASOV", "Tribunalul PRAHOVA", "Tribunalul SALAJ", "Tribunalul SATU MARE", "Tribunalul SIBIU", "Tribunalul SUCEAVA", "Tribunalul TELEORMAN", "Tribunalul TIMIS", "Tribunalul TULCEA", "Tribunalul VALCEA", "Tribunalul VASLUI", "Tribunalul VRANCEA"));

//        Dynamic toggle
        ToggleGroup clientTypeGroup = new ToggleGroup();
        existingClientRadio.setToggleGroup(clientTypeGroup);
        newClientRadio.setToggleGroup(clientTypeGroup);

        clientTypeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newClientRadio.isSelected()){
                existingClientBox.setVisible(false);
                existingClientBox.setManaged(false);
                newClientBox.setVisible(true);
                newClientBox.setManaged(true);
            } else {
                existingClientBox.setVisible(true);
                existingClientBox.setManaged(true);
                newClientBox.setVisible(false);
                newClientBox.setManaged(false);
            }
            validateForm();
        });

//        Listeners for deactivating save button until all required fields are filled
        caseNumberField.textProperty().addListener((observableValue, s, t1) -> validateForm());
        courtComboBox.valueProperty().addListener((observableValue, s, t1) -> validateForm());
        clientComboBox.valueProperty().addListener((observableValue, client, t1) -> validateForm());
        newClientNameField.textProperty().addListener((observableValue, s, t1) -> validateForm());

//        Double click on the Table row to open case details
        caseTable.setRowFactory(tv -> {
            TableRow<TrackedCase> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())){
                    TrackedCase selectedCase = row.getItem();
                    openCaseDetailsDialog(selectedCase);
                }
            });
            return row;
        });

        loadData();
        validateForm();
    }

    public void loadData(){
        loadCases();
        loadUpcomingHearings();
        loadClients();
    }

    public void loadCases(){
        List<TrackedCase> cases = trackedCaseService.getAllCases();
        ObservableList<TrackedCase> observableCases = FXCollections.observableArrayList(cases);
        caseTable.setItems(observableCases);
    }

    public void loadUpcomingHearings(){
        List<Hearing> hearings = trackedCaseService.getUpcomingHearings();
        upcomingHearingsTable.setItems(FXCollections.observableArrayList(hearings));
    }

    public void loadClients(){
        List<Client> clients = trackedCaseService.getAllClients();
        clientComboBox.setItems(FXCollections.observableArrayList(clients));
    }


    private void validateForm(){
        boolean caseNumValid = caseNumberField.getText() != null && !caseNumberField.getText().trim().isEmpty();
        boolean courtValid = courtComboBox.getValue() != null;

        boolean clientValid;
        if (existingClientRadio.isSelected()){
            clientValid = clientComboBox.getValue() != null;
        } else {
            clientValid = newClientNameField.getText() != null && !newClientNameField.getText().trim().isEmpty();
        }

        saveCaseButton.setDisable(!(caseNumValid && courtValid && clientValid));
    }



    @FXML
    private void handleSaveCase(){
        String caseNumber = caseNumberField.getText().trim();
        String courtName = courtComboBox.getValue();

//        Check if the court file already exists in the database
        Optional<TrackedCase> existingCase = trackedCaseService.findByCaseNumberAndCourt(caseNumber, courtName);

        TrackedCase targetCase;

        if (existingCase.isPresent()){
            targetCase = existingCase.get();
            targetCase.setCourtName(courtName);
            targetCase.setCustomNotes(notesField.getText());
            targetCase.setDelegationFiled(delegationCheckBox.isSelected());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Actualizare Dosar");
            alert.setHeaderText("Dosar existent");
            alert.setContentText("Dosarul " + caseNumber + "de la " + courtName +" exista deja în baza de date. Datele au fost actualizate!" );
            alert.showAndWait();
        } else {
            Client client;
            if (existingClientRadio.isSelected()){
                client = clientComboBox.getValue();
            } else {
                client = new Client();
                client.setName(newClientNameField.getText().trim());
                client.setContractNumber(newClientNameField.getText().trim());
                client = trackedCaseService.saveClient(client);
                loadClients();
            }

            targetCase = new TrackedCase();
            targetCase.setClient(client);
            targetCase.setCaseNumber(caseNumber);
            targetCase.setCourtName(courtName);
            targetCase.setCreatedAt(LocalDateTime.now());
        }

        targetCase.setCustomNotes(notesField.getText());
        targetCase.setDelegationFiled(delegationCheckBox.isSelected());

//        Save and API data retrieval
        trackedCaseService.saveCase(targetCase);
        justiceApiService.fetchAndPopulateCaseDetails(targetCase);

//        Form reset
        caseNumberField.clear();
        notesField.clear();
        newClientNameField.clear();
        newClientContractField.clear();
        delegationCheckBox.setSelected(false);
        clientComboBox.getSelectionModel().clearSelection();

        loadData();
    }

//    Search
    @FXML
    private void handleSearch(){
        String query = searchCaseField.getText();
        if (query == null || query.trim().isEmpty()){
            loadCases();
            return;
        }

        List<TrackedCase> results;
        if (searchByNumberRadio.isSelected()){
            results = trackedCaseService.searchCasesByNumber(query.trim());
        } else {
            results = trackedCaseService.searchCasesByClientName(query.trim());
        }
        caseTable.setItems(FXCollections.observableArrayList(results));
    }



    @FXML
    private void handleResetFilter(){
        searchCaseField.clear();
        searchByNumberRadio.setSelected(true);
        loadCases();
    }

//    Open a pop-up with case details on double click
    private void openCaseDetailsDialog(TrackedCase trackedCase){
        try{
            TrackedCase freschCase = trackedCaseService.findByCaseNumberAndCourt(trackedCase.getCaseNumber(), trackedCase.getCourtName())
                    .orElse(trackedCase);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/case-details-dialog.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Detalii dosar " + trackedCase.getCaseNumber());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(caseTable.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            CaseDetailsDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTrackedCaseService(trackedCaseService);
            controller.initData(freschCase);

            dialogStage.showAndWait();

            loadData();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSyncGoogleCalendar(){
        System.out.println("Sincronizarea cu Google Calendar va fi inițiată...");
//        TO ADD GOOGLE API
    }
}
