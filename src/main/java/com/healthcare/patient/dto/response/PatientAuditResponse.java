
package com.healthcare.patient.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAuditResponse {

    private Long id;

    private Long patientId;

    private String action;

    private String description;

    private String performedBy;

    private LocalDateTime performedAt;
}
