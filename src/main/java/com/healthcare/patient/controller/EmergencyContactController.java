
package com.healthcare.patient.controller;

import com.healthcare.patient.dto.request.EmergencyContactRequest;
import com.healthcare.patient.dto.response.ApiResponse;
import com.healthcare.patient.dto.response.EmergencyContactResponse;
import com.healthcare.patient.service.EmergencyContactService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/emergency-contacts")
@RequiredArgsConstructor
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    // Add Emergency Contact
    // ADMIN, RECEPTIONIST and PATIENT can add contacts
    // PATIENT can add only their own emergency contacts
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<EmergencyContactResponse>> addEmergencyContact(
            @PathVariable Long patientId,
            @Valid @RequestBody EmergencyContactRequest request) {

        EmergencyContactResponse response =
                emergencyContactService.addEmergencyContact(
                        patientId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Emergency contact added successfully", response));
    }

    // Get All Emergency Contacts
    // ADMIN, DOCTOR, RECEPTIONIST and PATIENT can view contacts
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<EmergencyContactResponse>>> getEmergencyContacts(
            @PathVariable Long patientId) {

        List<EmergencyContactResponse> response =
                emergencyContactService.getEmergencyContacts(patientId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Emergency contacts retrieved successfully", response));
    }

    // Get Emergency Contact By ID
    // ADMIN, DOCTOR, RECEPTIONIST and PATIENT can view contact details
    @GetMapping("/{contactId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<EmergencyContactResponse>> getEmergencyContactById(
            @PathVariable Long patientId,
            @PathVariable Long contactId) {

        EmergencyContactResponse response =
                emergencyContactService.getEmergencyContactById(
                        patientId, contactId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Emergency contact retrieved successfully", response));
    }

    // Update Emergency Contact
    // ADMIN, RECEPTIONIST and PATIENT can update contacts
    @PutMapping("/{contactId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<EmergencyContactResponse>> updateEmergencyContact(
            @PathVariable Long patientId,
            @PathVariable Long contactId,
            @Valid @RequestBody EmergencyContactRequest request) {

        EmergencyContactResponse response =
                emergencyContactService.updateEmergencyContact(
                        patientId, contactId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Emergency contact updated successfully", response));
    }

    // Delete Emergency Contact
    // ADMIN, RECEPTIONIST and PATIENT can delete contacts
    @DeleteMapping("/{contactId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<Void>> deleteEmergencyContact(
            @PathVariable Long patientId,
            @PathVariable Long contactId) {

        emergencyContactService.deleteEmergencyContact(
                patientId, contactId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Emergency contact deleted successfully", null));
    }
}
