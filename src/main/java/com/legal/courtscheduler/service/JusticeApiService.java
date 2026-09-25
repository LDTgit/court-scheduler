package com.legal.courtscheduler.service;

import com.legal.courtscheduler.entity.CaseParty;
import com.legal.courtscheduler.entity.Hearing;
import com.legal.courtscheduler.entity.TrackedCase;
import com.legal.courtscheduler.repository.CasePartyRepository;
import com.legal.courtscheduler.repository.HearingRepository;
import com.legal.courtscheduler.repository.TrackedCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class JusticeApiService {

    private final TrackedCaseRepository trackedCaseRepository;
    private final HearingRepository hearingRepository;
    private final CasePartyRepository casePartyRepository;

    private static final String SOAP_URL = "http://portalquery.just.ro/query.asmx";

    public void fetchAndPopulateCaseDetails(TrackedCase trackedCase){
        try{
            // Curățăm numărul dosarului de spații accidentale
            String cleanCaseNumber = trackedCase.getCaseNumber() != null ? trackedCase.getCaseNumber().trim() : "";

            // Curățăm și adaptăm denumirea instituției pentru a respecta formatul SOAP (fără spații)
            String rawCourtName = trackedCase.getCourtName() != null ? trackedCase.getCourtName().trim() : "";
            String institutionCode = rawCourtName.replace(" ", ""); // ex: "Judecatoria SECTORUL 1 BUCURESTI" devine "JudecatoriaSECTORUL1BUCURESTI"

//            SOAP request
            String soapEnvelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                    "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<soap:Body>" +
                    "<CautareDosare2 xmlns=\"portalquery.just.ro\">" +
                    "<numarDosar>" + cleanCaseNumber + "</numarDosar>" +
                    "<institutie>" + institutionCode + "</institutie>" +
                    "</CautareDosare2>" +
                    "</soap:Body>" +
                    "</soap:Envelope>";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SOAP_URL))
                    .header("Content-Type", "text/xml; charset=utf-8")
                    .header("SOAPAction", "portalquery.just.ro/CautareDosare2")
                    .POST(HttpRequest.BodyPublishers.ofString(soapEnvelope))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 && response.body() != null){
                // Afișează în consolă răspunsul XML brut pentru a inspecta nodul <sedinte>
                System.out.println("RĂSPUNS XML PRIMIT: " + response.body());
                parseAndSaveSoapResponse(response.body(), trackedCase);
            } else {
                setFallbackData(trackedCase);
            }

        } catch (Exception e){
            System.err.println("Eroare la interogarea serviciului web just.ro: "+ e.getMessage());
            setFallbackData(trackedCase);
        }
    }

    private void parseAndSaveSoapResponse(String xmlResponse, TrackedCase trackedCase){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.parse(new InputSource(new StringReader(xmlResponse)));
            doc.getDocumentElement().normalize();

            trackedCase.setObject(getElementValue(doc, "obiect"));
            trackedCase.setSection(getElementValue(doc, "departament"));
            trackedCase.setStage(getElementValue(doc, "stadiuProcesualNume"));

            String dataRegStr = getElementValue(doc, "data");
            if (dataRegStr != null && !dataRegStr.isEmpty()){
                trackedCase.setRegistrationDate(parseDateTime(dataRegStr));
            }

            String dataModifStr = getElementValue(doc, "dataModificare");
            if (dataModifStr != null && !dataModifStr.isEmpty()){
                trackedCase.setLastModifiedDate(parseDateTime(dataModifStr));
            }

            if (trackedCase.getId() != null) {
                casePartyRepository.deleteByTrackedCaseId(trackedCase.getId());
            }

//            Extraction of parties
            StringBuilder partiesBuilder = new StringBuilder();
            NodeList partiNodes = doc.getElementsByTagNameNS("*", "DosarParte");

            for (int i=0; i<partiNodes.getLength(); i++){
                Element parteElement = (Element) partiNodes.item(i);
                String nume = getChildElementValue(parteElement, "nume");
                String calitate = getChildElementValue(parteElement, "calitateParte");

                if (nume != null){
                    partiesBuilder.append(calitate).append(": ").append(nume).append("\n");

                    CaseParty party = new CaseParty();
                    party.setName(nume);
                    party.setPartyRole(calitate);
                    party.setTrackedCase(trackedCase);
                    casePartyRepository.save(party);
                }
            }
            trackedCase.setParties(partiesBuilder.toString());

//            Extraction of court dates
            NodeList sedinteNodes = doc.getElementsByTagNameNS("*", "DosarSedinta");

            if (trackedCase.getId() != null) {
                hearingRepository.deleteByTrackedCaseId(trackedCase.getId());
            }

            for (int i=0; i<sedinteNodes.getLength(); i++){
                Element sedintaElement = (Element) sedinteNodes.item(i);
                String dataSedintaStr = getChildElementValue(sedintaElement, "data");
                String solutie = getChildElementValue(sedintaElement, "solutie");
                String complet = getChildElementValue(sedintaElement, "complet");

                if (dataSedintaStr != null && !dataSedintaStr.isEmpty()){
                    Hearing hearing = new Hearing();
                    hearing.setTrackedCase(trackedCase);
                    hearing.setHearingDateTime(parseDateTime(dataSedintaStr));
                    hearing.setCourtroom(complet != null ? complet : "Complet necunoscut");
                    hearing.setStage(solutie != null && !solutie.isEmpty() ? solutie : "Termen în curs");
                    hearing.setHearingUid("UID" + trackedCase.getCaseNumber() + "-" + i);
                    hearing.setSyncedToCalendar(false);

                    hearingRepository.save(hearing);
                }
            }
            trackedCaseRepository.save(trackedCase);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getElementValue(Document doc, String tagName){
        NodeList list = doc.getElementsByTagNameNS("*", tagName);
        if (list.getLength() > 0){
            return list.item(0).getTextContent();
        }
        return null;
    }

    private String getChildElementValue(Element parent, String tagName){
        NodeList list = parent.getElementsByTagNameNS("*", tagName);
        if (list.getLength() > 0){
            return list.item(0). getTextContent();
        }
        return null;
    }

    private LocalDateTime parseDateTime(String dateStr){
        try{
            if (dateStr.contains("+")){
                dateStr = dateStr.substring(0, dateStr.indexOf("+"));
            }
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e){
            return LocalDateTime.now();
        }
    }

    private void setFallbackData(TrackedCase trackedCase){
        trackedCase.setObject("Dosar înregistrat");
        trackedCase.setStage("Fond");
        trackedCase.setRegistrationDate(LocalDateTime.now());
        trackedCaseRepository.save(trackedCase);
    }
}
