package com.legal.courtscheduler.repository;

import com.legal.courtscheduler.entity.TrackedCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackedCaseRepository extends JpaRepository<TrackedCase, Long> {
    // Finding a case file by its number
    Optional<TrackedCase> findByCaseNumber(String caseNumber);

    // Check if a file exists
    boolean existsByCaseNumber(String caseNumber);
}
