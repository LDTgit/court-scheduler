package com.legal.courtscheduler.repository;

import com.legal.courtscheduler.entity.Hearing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HearingRepository extends JpaRepository<Hearing, Long> {
    // Check if a hearing has already been registered
    boolean existsByHearingUid(String hearingUid);
}
