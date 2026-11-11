
package com.healthcare.patient.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientAuditRepository
        extends JpaRepository<PatientAudit, Long> {

    // Get all audit records of a patient
    List<PatientAudit> findByPatientId(Long patientId);

    // Get audit records by action
    List<PatientAudit> findByAction(String action);

    // Get latest audit records first
    List<PatientAudit> findByPatientIdOrderByPerformedAtDesc(
            Long patientId
    );
}
