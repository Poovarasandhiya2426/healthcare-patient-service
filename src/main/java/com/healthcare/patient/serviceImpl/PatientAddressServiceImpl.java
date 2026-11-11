
package com.healthcare.patient.serviceImpl;

import com.healthcare.patient.dto.request.PatientAddressRequest;
import com.healthcare.patient.dto.response.PatientAddressResponse;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.entity.PatientAddress;
import com.healthcare.patient.repository.PatientAddressRepository;
import com.healthcare.patient.repository.PatientRepository;
import com.healthcare.patient.service.PatientAddressService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientAddressServiceImpl implements PatientAddressService {

    private final PatientRepository patientRepository;
    private final PatientAddressRepository patientAddressRepository;

    // Add Patient Address
    @Override
    public PatientAddressResponse addAddress(
            Long patientId, PatientAddressRequest request) {

        Patient patient = getPatient(patientId);

        PatientAddress address = PatientAddress.builder()
                .addressLine(request.getAddressLine())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .addressType(request.getAddressType())
                .patient(patient)
                .build();

        PatientAddress savedAddress =
                patientAddressRepository.save(address);

        return toResponse(savedAddress);
    }

    // Get All Patient Addresses
    @Override
    @Transactional(readOnly = true)
    public List<PatientAddressResponse> getPatientAddresses(
            Long patientId) {

        Patient patient = getPatient(patientId);

        return patientAddressRepository.findByPatient(patient)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get Address By ID
    @Override
    @Transactional(readOnly = true)
    public PatientAddressResponse getAddressById(
            Long patientId, Long addressId) {

        Patient patient = getPatient(patientId);

        PatientAddress address = patientAddressRepository
                .findByIdAndPatient(addressId, patient)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient address not found"));

        return toResponse(address);
    }

    // Update Patient Address
    @Override
    public PatientAddressResponse updateAddress(
            Long patientId,
            Long addressId,
            PatientAddressRequest request) {

        Patient patient = getPatient(patientId);

        PatientAddress address = patientAddressRepository
                .findByIdAndPatient(addressId, patient)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient address not found"));

        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());

        PatientAddress updatedAddress =
                patientAddressRepository.save(address);

        return toResponse(updatedAddress);
    }

    // Delete Patient Address
    @Override
    public void deleteAddress(Long patientId, Long addressId) {

        Patient patient = getPatient(patientId);

        PatientAddress address = patientAddressRepository
                .findByIdAndPatient(addressId, patient)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient address not found"));

        patientAddressRepository.delete(address);
    }

    // Get Patient and Validate Ownership
    private Patient getPatient(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found"));

        validatePatientOwnership(patient);

        return patient;
    }

    // Validate Logged-in Patient Ownership
    private void validatePatientOwnership(Patient patient) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PATIENT"))) {

            String loggedInEmail = authentication.getName();

            if (!patient.getEmail().equalsIgnoreCase(loggedInEmail)) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You are not authorized to access this patient's address");
            }
        }
    }

    // Convert Entity to Response DTO
    private PatientAddressResponse toResponse(
            PatientAddress address) {

        return PatientAddressResponse.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .addressType(address.getAddressType())
                .build();
    }
}
