package com.healthcare.patient.serviceImpl;

import com.healthcare.patient.dto.request.EmergencyContactRequest;
import com.healthcare.patient.dto.response.EmergencyContactResponse;
import com.healthcare.patient.entity.EmergencyContact;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.repository.EmergencyContactRepository;
import com.healthcare.patient.repository.PatientRepository;
import com.healthcare.patient.service.EmergencyContactService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private final PatientRepository patientRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    // Add emergency contact
    @Override
    public EmergencyContactResponse addEmergencyContact(
            Long patientId, EmergencyContactRequest request) {

        Patient patient = getPatient(patientId);

        EmergencyContact contact = EmergencyContact.builder()
                .contactName(request.getContactName())
                .phoneNumber(request.getPhoneNumber())
                .relationship(request.getRelationship())
                .email(request.getEmail())
                .patient(patient)
                .build();

        EmergencyContact savedContact =
                emergencyContactRepository.save(contact);

        return toResponse(savedContact);
    }

    // Get all emergency contacts
    @Override
    @Transactional(readOnly = true)
    public List<EmergencyContactResponse> getEmergencyContacts(Long patientId) {

        Patient patient = getPatient(patientId);

        return emergencyContactRepository.findByPatient(patient)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get emergency contact by ID
    @Override
    @Transactional(readOnly = true)
    public EmergencyContactResponse getEmergencyContactById(
            Long patientId, Long contactId) {

        Patient patient = getPatient(patientId);

        EmergencyContact contact = emergencyContactRepository
                .findByIdAndPatient(contactId, patient)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Emergency contact not found"));

        return toResponse(contact);
    }

    // Update emergency contact
    @Override
    public EmergencyContactResponse updateEmergencyContact(
            Long patientId,
            Long contactId,
            EmergencyContactRequest request) {

        Patient patient = getPatient(patientId);

        EmergencyContact contact = emergencyContactRepository
                .findByIdAndPatient(contactId, patient)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Emergency contact not found"));

        contact.setContactName(request.getContactName());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setRelationship(request.getRelationship());
        contact.setEmail(request.getEmail());

        EmergencyContact updatedContact =
                emergencyContactRepository.save(contact);

        return toResponse(updatedContact);
    }

    // Delete emergency contact
    @Override
    public void deleteEmergencyContact(Long patientId, Long contactId) {

        Patient patient = getPatient(patientId);

        EmergencyContact contact = emergencyContactRepository
                .findByIdAndPatient(contactId, patient)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Emergency contact not found"));

        emergencyContactRepository.delete(contact);
    }

    // Find patient
    private Patient getPatient(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found"));
    }

    // Convert Entity to Response DTO
    private EmergencyContactResponse toResponse(EmergencyContact contact) {

        return EmergencyContactResponse.builder()
                .id(contact.getId())
                .contactName(contact.getContactName())
                .phoneNumber(contact.getPhoneNumber())
                .relationship(contact.getRelationship())
                .email(contact.getEmail())
                .build();
    }
}