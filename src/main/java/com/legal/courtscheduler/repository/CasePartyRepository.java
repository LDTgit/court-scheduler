package com.legal.courtscheduler.repository;

import com.legal.courtscheduler.entity.CaseParty;
import com.legal.courtscheduler.entity.TrackedCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CasePartyRepository extends JpaRepository<CaseParty, Long> {
    List<CaseParty> findByTrackedCase(TrackedCase trackedCase);
}
