package com.healthcare.patient.service;

import com.healthcare.patient.dto.request.EmergencyContactRequest;
import com.healthcare.patient.dto.response.EmergencyContactResponse;

import java.util.List;

public interface EmergencyContactService {

    EmergencyContactResponse addEmergencyContact(
            Long patientId,
            EmergencyContactRequest request
    );

    List<EmergencyContactResponse> getEmergencyContacts(
            Long patientId
    );

    EmergencyContactResponse getEmergencyContactById(
            Long patientId,
            Long contactId
    );

    EmergencyContactResponse updateEmergencyContact(
            Long patientId,
            Long contactId,
            EmergencyContactRequest request
    );

    void deleteEmergencyContact(
            Long patientId,
            Long contactId
    );
}