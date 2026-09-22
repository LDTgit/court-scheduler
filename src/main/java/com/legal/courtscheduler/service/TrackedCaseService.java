package com.legal.courtscheduler.service;

import com.legal.courtscheduler.entity.TrackedCase;
import com.legal.courtscheduler.repository.TrackedCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackedCaseService {

    private final TrackedCaseRepository trackedCaseRepository;

    public List<TrackedCase> getAllCases(){
        return trackedCaseRepository.findAll();
    }
    public TrackedCase saveCase(TrackedCase trackedCase){
        return trackedCaseRepository.save(trackedCase);
    }
}
