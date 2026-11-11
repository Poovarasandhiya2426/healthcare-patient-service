
package com.healthcare.patient.audit;

import com.healthcare.patient.dto.response.PatientAuditResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientAuditServiceImpl implements PatientAuditService {

    private final PatientAuditRepository patientAuditRepository;

    // Save patient activity
    @Override
    public void logActivity(
            Long patientId,
            String action,
            String description,
            String performedBy) {

        PatientAudit audit = PatientAudit.builder()
                .patientId(patientId)
                .action(action)
                .description(description)
                .performedBy(performedBy)
                .build();

        patientAuditRepository.save(audit);
    }

    // Get patient audit history
    @Override
    @Transactional(readOnly = true)
    public List<PatientAuditResponse> getPatientAuditHistory(Long patientId) {

        return patientAuditRepository
                .findByPatientIdOrderByPerformedAtDesc(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get audit history by action
    @Override
    @Transactional(readOnly = true)
    public List<PatientAuditResponse> getAuditHistoryByAction(String action) {

        return patientAuditRepository.findByAction(action)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Convert Entity to Response DTO
    private PatientAuditResponse toResponse(PatientAudit audit) {

        return PatientAuditResponse.builder()
                .id(audit.getId())
                .patientId(audit.getPatientId())
                .action(audit.getAction())
                .description(audit.getDescription())
                .performedBy(audit.getPerformedBy())
                .performedAt(audit.getPerformedAt())
                .build();
    }
}
