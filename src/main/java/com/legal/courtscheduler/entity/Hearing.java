package com.legal.courtscheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hearings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hearing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String hearingUid;  // to prevent duplicates

    private LocalDateTime hearingDateTime; // date and time of the hearing
    private String courtroom;
    private String summary;  // court case subject matter
    private String stage;  // stage of the procedure (first instance, appeal, cassation, etc)

    private String googleEventId;  // Event ID from Google calendar
    private boolean syncedToCalendar; // sync status

    // Many-to-One Relationship: Multiple hearings belong to one court file
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracked_case_id", nullable = false)
    private TrackedCase trackedCase;

}
