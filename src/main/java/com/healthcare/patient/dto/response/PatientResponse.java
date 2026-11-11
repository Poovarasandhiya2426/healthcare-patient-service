
package com.healthcare.patient.dto.response;

import com.healthcare.patient.enums.BloodGroup;
import com.healthcare.patient.enums.Gender;
import com.healthcare.patient.enums.PatientStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    private PatientStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
