package com.legal.courtscheduler.repository;

import com.legal.courtscheduler.entity.Hearing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HearingRepository extends JpaRepository<Hearing, Long> {
    // Find hearings in a timeframe
    List<Hearing> findByHearingDateTimeBetweenOrderByHearingDateTimeAsc(LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("DELETE FROM Hearing h WHERE h.trackedCase.id = :caseId")
    void deleteByTrackedCaseId(@Param("caseId") Long caseId);
}
