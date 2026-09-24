package com.legal.courtscheduler.entity;

import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tracked_case")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"client", "hearings", "partiesList"})
public class TrackedCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String caseNumber;
    @Column(nullable = false)
    private String courtName;

    private String section;
    private String domain;
    private String object;
    private String stage;
    private String parties;

    @Column(length = 1000)
    private String customNotes;  // user can add their own notes to the cases

    private LocalDateTime registrationDate;
    private LocalDateTime lastModifiedDate;
    private LocalDateTime createdAt;

    private  boolean delegationFiled;

    private String parti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @ToString.Exclude
    private Client client;

//    One-to-Many relationship: one court case can have multiple terms
    @OneToMany(mappedBy = "trackedCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Hearing> hearings;

    @OneToMany(mappedBy = "trackedCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CaseParty> partiesList;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
