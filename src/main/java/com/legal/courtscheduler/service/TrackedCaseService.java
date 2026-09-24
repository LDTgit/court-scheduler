package com.legal.courtscheduler.service;

import com.legal.courtscheduler.entity.Client;
import com.legal.courtscheduler.entity.Hearing;
import com.legal.courtscheduler.entity.TrackedCase;
import com.legal.courtscheduler.repository.ClientRepository;
import com.legal.courtscheduler.repository.HearingRepository;
import com.legal.courtscheduler.repository.TrackedCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackedCaseService {

    private final TrackedCaseRepository trackedCaseRepository;
    private final ClientRepository clientRepository;
    private final HearingRepository hearingRepository;

    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }
    public Client saveClient(Client client){
        return clientRepository.save(client);
    }

    public List<TrackedCase> getAllCases(){
        return trackedCaseRepository.findAllWithClient();
    }
    public TrackedCase saveCase(TrackedCase trackedCase){
        return trackedCaseRepository.save(trackedCase);
    }

    public Optional<TrackedCase> findByCaseNumberAndCourt(String caseNumber, String courtName){
        return trackedCaseRepository.findByCaseNumberIgnoreCase(caseNumber.trim());
    }

    public List<TrackedCase> getCasesByClient(Client client){
        return trackedCaseRepository.findAll().stream()
                .filter(c->c.getClient().equals(client))
                .toList();
    }

    public List<TrackedCase> searchCasesByNumber(String query){
        return trackedCaseRepository.findAllWithClient().stream()
                .filter(c->c.getCaseNumber().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<TrackedCase> searchCasesByClientName(String query){
        return trackedCaseRepository.findAllWithClient().stream()
                .filter(c->c.getClient().getName().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Hearing> getUpcomingHearings(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysLater = now.plusDays(7);
        return hearingRepository.findAll().stream()
                .filter(h->h.getHearingDateTime() != null &&
                        !h.getHearingDateTime().isBefore(now) &&
                        !h.getHearingDateTime().isAfter(sevenDaysLater))
                .sorted((h1, h2) -> h1.getHearingDateTime().compareTo(h2.getHearingDateTime()))
                .toList();
    }

    @Transactional
    public void deleteCascadeWithHearings(TrackedCase trackedCase){
        trackedCaseRepository.delete(trackedCase);
    }
}
