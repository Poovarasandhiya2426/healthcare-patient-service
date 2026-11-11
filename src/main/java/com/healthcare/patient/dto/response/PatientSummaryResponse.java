
package com.healthcare.patient.dto.response;

import com.healthcare.patient.enums.Gender;
import com.healthcare.patient.enums.PatientStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientSummaryResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    private PatientStatus status;
}
