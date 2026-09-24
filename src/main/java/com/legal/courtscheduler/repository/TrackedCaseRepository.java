package com.legal.courtscheduler.repository;

import com.legal.courtscheduler.entity.Client;
import com.legal.courtscheduler.entity.TrackedCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackedCaseRepository extends JpaRepository<TrackedCase, Long> {
  @Query("SELECT tc FROM TrackedCase tc JOIN FETCH tc.client")
    List<TrackedCase> findAllWithClient();

  Optional<TrackedCase> findByCaseNumberIgnoreCase(String caseNmber);
}
