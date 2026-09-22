package com.legal.courtscheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TrackedCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String caseNumber;
    private String courtName;

    @Column(length = 1000)
    private String customNotes;  // user can add their own notes to the cases
    private LocalDateTime createdAt;

//    One-to-Many relationship: one court case can have multiple terms
    @OneToMany(mappedBy = "trackedCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Hearing> hearings = new ArrayList<>();

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
