
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

    // Add Patient Address
    // ADMIN, RECEPTIONIST and PATIENT can add addresses
    // PATIENT can add only their own address
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<PatientAddressResponse>> addAddress(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientAddressRequest request) {

        PatientAddressResponse response =
                patientAddressService.addAddress(patientId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Address added successfully", response));
    }

    // Get All Patient Addresses
    // ADMIN, DOCTOR, RECEPTIONIST and PATIENT can view addresses
    // PATIENT can view only their own addresses
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<PatientAddressResponse>>> getPatientAddresses(
            @PathVariable Long patientId) {

        List<PatientAddressResponse> response =
                patientAddressService.getPatientAddresses(patientId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Addresses retrieved successfully", response));
    }

    // Get Address By ID
    // ADMIN, DOCTOR, RECEPTIONIST and PATIENT can view address details
    @GetMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<PatientAddressResponse>> getAddressById(
            @PathVariable Long patientId,
            @PathVariable Long addressId) {

        PatientAddressResponse response =
                patientAddressService.getAddressById(patientId, addressId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Address retrieved successfully", response));
    }

    // Update Patient Address
    // ADMIN, RECEPTIONIST and PATIENT can update addresses
    @PutMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<PatientAddressResponse>> updateAddress(
            @PathVariable Long patientId,
            @PathVariable Long addressId,
            @Valid @RequestBody PatientAddressRequest request) {

        PatientAddressResponse response =
                patientAddressService.updateAddress(
                        patientId, addressId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Address updated successfully", response));
    }

    // Delete Patient Address
    // ADMIN, RECEPTIONIST and PATIENT can delete addresses
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long patientId,
            @PathVariable Long addressId) {

        patientAddressService.deleteAddress(patientId, addressId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Address deleted successfully", null));
    }
}
