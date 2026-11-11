
package com.healthcare.patient.service;

import com.healthcare.patient.dto.request.PatientRegistrationRequest;
import com.healthcare.patient.dto.request.PatientUpdateRequest;
import com.healthcare.patient.dto.response.ApiResponse;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.dto.response.PatientSummaryResponse;
import com.healthcare.patient.enums.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {

    ApiResponse<PatientResponse> registerPatient(
            PatientRegistrationRequest request);

    ApiResponse<PatientResponse> getPatientById(Long patientId);

    ApiResponse<PatientResponse> getPatientByEmail(String email);

    ApiResponse<List<PatientSummaryResponse>> getAllPatients();

    ApiResponse<PatientResponse> updatePatient(
            Long patientId,
            PatientUpdateRequest request);

    ApiResponse<Void> deactivatePatient(Long patientId);

    ApiResponse<Page<PatientSummaryResponse>> searchPatients(
            String keyword,
            PatientStatus status,
            Pageable pageable
    );
}
