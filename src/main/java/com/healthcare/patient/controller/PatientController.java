
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // Allowed sorting fields
    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "id",
            "firstName",
            "lastName",
            "email",
            "createdAt",
            "updatedAt"
    );

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
    // ADMIN, DOCTOR, RECEPTIONIST and PATIENT can access
    @GetMapping("/{patientId:\\d+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT')")
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
    // ADMIN, RECEPTIONIST and PATIENT can update
    @PutMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
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

        // Validate page number
        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number cannot be negative"
            );
        }

        // Validate page size
        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be between 1 and 100"
            );
        }

        // Validate sorting field
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort field. Allowed fields: " + ALLOWED_SORT_FIELDS
            );
        }

        // Validate sorting direction
        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sorting direction. Use ASC or DESC"
            );
        }

        // Create pagination and sorting
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
