
package com.healthcare.patient.repository;

import com.healthcare.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.healthcare.patient.enums.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);


    @Query("""
    SELECT p FROM Patient p
    WHERE (
        :keyword IS NULL OR
        LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        p.phoneNumber LIKE CONCAT('%', :keyword, '%')
    )
    AND (:status IS NULL OR p.status = :status)
    """)
    Page<Patient> searchPatients(
            @Param("keyword") String keyword,
            @Param("status") PatientStatus status,
            Pageable pageable
    );
}
