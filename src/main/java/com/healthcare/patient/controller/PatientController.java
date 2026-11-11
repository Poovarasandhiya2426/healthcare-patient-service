
package com.healthcare.patient.controller;

import com.healthcare.patient.dto.request.PatientRegistrationRequest;
import com.healthcare.patient.dto.request.PatientUpdateRequest;
import com.healthcare.patient.dto.response.ApiResponse;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.dto.response.PatientSummaryResponse;
import com.healthcare.patient.enums.PatientStatus;
import com.healthcare.patient.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // Register Patient
    // ADMIN and RECEPTIONIST can register patients
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> registerPatient(
            @Valid @RequestBody PatientRegistrationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.registerPatient(request));
    }

    // Get Patient By ID
    // ADMIN, DOCTOR and RECEPTIONIST can view patient details
    @GetMapping("/{patientId:\\d+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                patientService.getPatientById(patientId));
    }

    // Get Patient By Email
    // ADMIN, DOCTOR and RECEPTIONIST can search by email
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientByEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                patientService.getPatientByEmail(email));
    }

    // Get All Patients
    // ADMIN and RECEPTIONIST can view all patients
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<PatientSummaryResponse>>> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatients());
    }

    // Update Patient
    // ADMIN and RECEPTIONIST can update patient details
    @PutMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientUpdateRequest request) {

        return ResponseEntity.ok(
                patientService.updatePatient(patientId, request));
    }

    // Deactivate Patient
    // Only ADMIN can deactivate patients
    @PatchMapping("/{patientId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivatePatient(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                patientService.deactivatePatient(patientId));
    }

    // Search, Filter and Pagination
    // ADMIN, DOCTOR and RECEPTIONIST can search patients
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Page<PatientSummaryResponse>>> searchPatients(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) PatientStatus status,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return ResponseEntity.ok(
                patientService.searchPatients(keyword, status, pageable)
        );
    }
}
