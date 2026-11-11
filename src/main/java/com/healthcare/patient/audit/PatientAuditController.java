
package com.healthcare.patient.audit;

import com.healthcare.patient.dto.response.ApiResponse;
import com.healthcare.patient.dto.response.PatientAuditResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-audits")
@RequiredArgsConstructor
public class PatientAuditController {

    private final PatientAuditService patientAuditService;

    // Get audit history by patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<PatientAuditResponse>>> getPatientAuditHistory(
            @PathVariable Long patientId) {

        List<PatientAuditResponse> auditHistory =
                patientAuditService.getPatientAuditHistory(patientId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Patient audit history retrieved successfully",
                        auditHistory
                )
        );
    }

    // Get audit history by action
    @GetMapping("/action/{action}")
    public ResponseEntity<ApiResponse<List<PatientAuditResponse>>> getAuditHistoryByAction(
            @PathVariable String action) {

        List<PatientAuditResponse> auditHistory =
                patientAuditService.getAuditHistoryByAction(
                        action.toUpperCase()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Audit history retrieved successfully",
                        auditHistory
                )
        );
    }
}
