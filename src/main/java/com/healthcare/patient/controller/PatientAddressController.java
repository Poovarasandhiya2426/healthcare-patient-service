package com.healthcare.patient.controller;

import com.healthcare.patient.dto.request.PatientAddressRequest;
import com.healthcare.patient.dto.response.ApiResponse;
import com.healthcare.patient.dto.response.PatientAddressResponse;
import com.healthcare.patient.service.PatientAddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/addresses")
@RequiredArgsConstructor
public class PatientAddressController {

    private final PatientAddressService patientAddressService;

    // Add patient address
    @PostMapping
    public ResponseEntity<ApiResponse<PatientAddressResponse>> addAddress(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientAddressRequest request) {

        PatientAddressResponse response =
                patientAddressService.addAddress(patientId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address added successfully", response));
    }

    // Get all addresses of a patient
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientAddressResponse>>> getPatientAddresses(
            @PathVariable Long patientId) {

        List<PatientAddressResponse> response =
                patientAddressService.getPatientAddresses(patientId);

        return ResponseEntity.ok(
                ApiResponse.success("Addresses retrieved successfully", response));
    }

    // Get address by ID
    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<PatientAddressResponse>> getAddressById(
            @PathVariable Long patientId,
            @PathVariable Long addressId) {

        PatientAddressResponse response =
                patientAddressService.getAddressById(patientId, addressId);

        return ResponseEntity.ok(
                ApiResponse.success("Address retrieved successfully", response));
    }

    // Update address
    @PutMapping("/{addressId}")
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
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long patientId,
            @PathVariable Long addressId) {

        patientAddressService.deleteAddress(patientId, addressId);

        return ResponseEntity.ok(
                ApiResponse.success("Address deleted successfully", null));
    }
}