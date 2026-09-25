package com.legal.courtscheduler.repository;

import com.legal.courtscheduler.entity.CaseParty;
import com.legal.courtscheduler.entity.TrackedCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CasePartyRepository extends JpaRepository<CaseParty, Long> {
    List<CaseParty> findByTrackedCase(TrackedCase trackedCase);

    @Modifying
    @Query("DELETE FROM CaseParty cp WHERE cp.trackedCase.id = :caseId")
    void deleteByTrackedCaseId(@Param("caseId") Long caseId);
}
