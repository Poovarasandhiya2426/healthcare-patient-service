
package com.healthcare.patient.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String performedBy;

    @Column(nullable = false)
    private LocalDateTime performedAt;

    @PrePersist
    public void onCreate() {
        performedAt = LocalDateTime.now();
    }
}
