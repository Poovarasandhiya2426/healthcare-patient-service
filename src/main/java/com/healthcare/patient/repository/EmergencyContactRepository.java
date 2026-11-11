
package com.healthcare.patient.repository;

import com.healthcare.patient.entity.EmergencyContact;
import com.healthcare.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyContactRepository
        extends JpaRepository<EmergencyContact, Long> {

    List<EmergencyContact> findByPatient(Patient patient);

    void deleteByPatientId(Long patientId);
}
