package com.healthcare.patient.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContactResponse {

    private Long id;

    private String contactName;

    private String phoneNumber;

    private String relationship;

    private String email;
}