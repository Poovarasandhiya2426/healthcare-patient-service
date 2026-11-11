package com.healthcare.patient.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAddressResponse {

    private Long id;

    private String addressLine;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private String addressType;
}