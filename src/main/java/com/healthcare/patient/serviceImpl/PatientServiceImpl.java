
package com.healthcare.patient.serviceImpl;

import com.healthcare.patient.audit.PatientAuditService;
import com.healthcare.patient.constants.PatientConstants;
import com.healthcare.patient.dto.request.PatientRegistrationRequest;
import com.healthcare.patient.dto.request.PatientUpdateRequest;
import com.healthcare.patient.dto.response.ApiResponse;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.dto.response.PatientSummaryResponse;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.enums.PatientStatus;
import com.healthcare.patient.mapper.PatientMapper;
import com.healthcare.patient.repository.PatientRepository;
import com.healthcare.patient.security.JwtAuthenticationDetails;
import com.healthcare.patient.service.PatientService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PatientAuditService patientAuditService;

    // Get currently logged-in user's email
    private String getCurrentUserEmail() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            return authentication.getName();
        }

        return "SYSTEM";
    }


    // Check whether logged-in patient owns the profile
    private void validatePatientOwnership(Patient patient) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PATIENT"))) {

            Object details = authentication.getDetails();

            if (!(details instanceof JwtAuthenticationDetails jwtDetails)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Unable to verify patient identity"
                );
            }

            Long loggedInUserId = jwtDetails.getUserId();

            if (patient.getIdentityUserId() == null
                    || !patient.getIdentityUserId().equals(loggedInUserId)) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You are not authorized to access this patient profile"
                );
            }
        }
    }


    // Register Patient
    @Override
    public ApiResponse<PatientResponse> registerPatient(
            PatientRegistrationRequest request) {

        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    PatientConstants.EMAIL_ALREADY_EXISTS
            );
        }

        if (patientRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    PatientConstants.PHONE_ALREADY_EXISTS
            );
        }

        Patient patient = patientMapper.toEntity(request);

        Patient savedPatient = patientRepository.save(patient);

        patientAuditService.logActivity(
                savedPatient.getId(),
                "REGISTER",
                "New patient registered",
                getCurrentUserEmail()
        );

        return ApiResponse.success(
                PatientConstants.PATIENT_REGISTERED,
                patientMapper.toResponse(savedPatient)
        );
    }

    // Get Patient By ID
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PatientResponse> getPatientById(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found with ID: " + patientId
                ));

        validatePatientOwnership(patient);

        return ApiResponse.success(
                PatientConstants.PATIENT_RETRIEVED,
                patientMapper.toResponse(patient)
        );
    }

    // Get Patient By Email
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PatientResponse> getPatientByEmail(String email) {

        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found with email: " + email
                ));

        validatePatientOwnership(patient);

        return ApiResponse.success(
                PatientConstants.PATIENT_RETRIEVED,
                patientMapper.toResponse(patient)
        );
    }

    // Get All Patients
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<PatientSummaryResponse>> getAllPatients() {

        List<PatientSummaryResponse> patients =
                patientRepository.findAll()
                        .stream()
                        .map(patientMapper::toSummaryResponse)
                        .toList();

        return ApiResponse.success(
                PatientConstants.PATIENTS_RETRIEVED,
                patients
        );
    }

    // Update Patient
    @Override
    public ApiResponse<PatientResponse> updatePatient(
            Long patientId,
            PatientUpdateRequest request) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found with ID: " + patientId
                ));

        // Validate patient ownership before updating
        validatePatientOwnership(patient);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isPatient = authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PATIENT"));

        // Patient cannot change their registered email
        if (isPatient
                && !patient.getEmail().equalsIgnoreCase(request.getEmail())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Patients are not allowed to change their registered email"
            );
        }

        boolean emailUsed = patientRepository
                .findByEmail(request.getEmail())
                .filter(existing -> !existing.getId().equals(patientId))
                .isPresent();

        if (emailUsed) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    PatientConstants.EMAIL_ALREADY_EXISTS
            );
        }

        boolean phoneUsed = patientRepository
                .findByPhoneNumber(request.getPhoneNumber())
                .filter(existing -> !existing.getId().equals(patientId))
                .isPresent();

        if (phoneUsed) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    PatientConstants.PHONE_ALREADY_EXISTS
            );
        }

        patientMapper.updateEntity(patient, request);

        Patient updatedPatient = patientRepository.save(patient);

        // Audit logging
        patientAuditService.logActivity(
                updatedPatient.getId(),
                "UPDATE",
                "Patient details updated",
                getCurrentUserEmail()
        );

        return ApiResponse.success(
                PatientConstants.PATIENT_UPDATED,
                patientMapper.toResponse(updatedPatient)
        );
    }

    // Deactivate Patient
    @Override
    public ApiResponse<Void> deactivatePatient(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found with ID: " + patientId
                ));

        patient.setStatus(PatientStatus.INACTIVE);

        patientRepository.save(patient);

        patientAuditService.logActivity(
                patient.getId(),
                "DEACTIVATE",
                "Patient account deactivated",
                getCurrentUserEmail()
        );

        return ApiResponse.success(
                PatientConstants.PATIENT_DEACTIVATED,
                null
        );
    }

    // Search, Filter and Pagination
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<PatientSummaryResponse>> searchPatients(
            String keyword,
            PatientStatus status,
            Pageable pageable) {

        Page<Patient> patients = patientRepository.searchPatients(
                keyword,
                status,
                pageable
        );

        Page<PatientSummaryResponse> response = patients.map(
                patientMapper::toSummaryResponse
        );

        return ApiResponse.success(
                "Patients retrieved successfully",
                response
        );
    }
}
