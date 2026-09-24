package com.legal.courtscheduler.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "case_parties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Nume parte
    private String partyRole; // Calitate parte

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracked_case_id", nullable = false)
    private TrackedCase trackedCase;
}
