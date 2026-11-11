
package com.healthcare.patient.repository;

import com.healthcare.patient.entity.PatientAddress;
import com.healthcare.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientAddressRepository
        extends JpaRepository<PatientAddress, Long> {

    List<PatientAddress> findByPatient(Patient patient);

    void deleteByPatientId(Long patientId);
}
