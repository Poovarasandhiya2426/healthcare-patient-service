
package com.healthcare.patient.service;

import com.healthcare.patient.dto.request.PatientAddressRequest;
import com.healthcare.patient.dto.response.PatientAddressResponse;

import java.util.List;

public interface PatientAddressService {

    // Add address to patient
    PatientAddressResponse addAddress(
            Long patientId,
            PatientAddressRequest request
    );

    // Get all addresses of a patient
    List<PatientAddressResponse> getPatientAddresses(
            Long patientId
    );

    // Get address by ID
    PatientAddressResponse getAddressById(
            Long patientId,
            Long addressId
    );

    // Update patient address
    PatientAddressResponse updateAddress(
            Long patientId,
            Long addressId,
            PatientAddressRequest request
    );

    // Delete patient address
    void deleteAddress(
            Long patientId,
            Long addressId
    );
}
