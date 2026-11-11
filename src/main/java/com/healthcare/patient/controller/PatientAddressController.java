
package com.healthcare.patient.controller;

import com.healthcare.patient.dto.request.PatientAddressRequest;
import com.healthcare.patient.dto.response.ApiResponse;
import com.healthcare.patient.dto.response.PatientAddressResponse;
import com.healthcare.patient.service.PatientAddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/addresses")
@RequiredArgsConstructor
public class PatientAddressController {

    private final PatientAddressService patientAddressService;

    // Add patient address
    // ADMIN and RECEPTIONIST can add addresses
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientAddressResponse>> addAddress(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientAddressRequest request) {

        PatientAddressResponse response =
                patientAddressService.addAddress(patientId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address added successfully", response));
    }

    // Get all addresses of a patient
    // ADMIN, DOCTOR and RECEPTIONIST can view addresses
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<PatientAddressResponse>>> getPatientAddresses(
            @PathVariable Long patientId) {

        List<PatientAddressResponse> response =
                patientAddressService.getPatientAddresses(patientId);

        return ResponseEntity.ok(
                ApiResponse.success("Addresses retrieved successfully", response));
    }

    // Get address by ID
    // ADMIN, DOCTOR and RECEPTIONIST can view address details
    @GetMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientAddressResponse>> getAddressById(
            @PathVariable Long patientId,
            @PathVariable Long addressId) {

        PatientAddressResponse response =
                patientAddressService.getAddressById(patientId, addressId);

        return ResponseEntity.ok(
                ApiResponse.success("Address retrieved successfully", response));
    }

    // Update address
    // ADMIN and RECEPTIONIST can update addresses
    @PutMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientAddressResponse>> updateAddress(
            @PathVariable Long patientId,
            @PathVariable Long addressId,
            @Valid @RequestBody PatientAddressRequest request) {

        PatientAddressResponse response =
                patientAddressService.updateAddress(patientId, addressId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Address updated successfully", response));
    }

    // Delete address
    // Only ADMIN and RECEPTIONIST can delete addresses
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long patientId,
            @PathVariable Long addressId) {

        patientAddressService.deleteAddress(patientId, addressId);

        return ResponseEntity.ok(
                ApiResponse.success("Address deleted successfully", null));
    }
}
