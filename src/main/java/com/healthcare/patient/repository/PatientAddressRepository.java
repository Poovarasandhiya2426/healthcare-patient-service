
package com.healthcare.patient.repository;

import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.entity.PatientAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientAddressRepository
        extends JpaRepository<PatientAddress, Long> {

    // Get all addresses of a patient
    List<PatientAddress> findByPatient(Patient patient);

    // Get address by ID and patient
    Optional<PatientAddress> findByIdAndPatient(
            Long id,
            Patient patient
    );

    // Delete all addresses of a patient
    void deleteByPatientId(Long patientId);
}
