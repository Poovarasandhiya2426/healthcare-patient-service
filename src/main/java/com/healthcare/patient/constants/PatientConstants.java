
package com.healthcare.patient.constants;

public final class PatientConstants {

    private PatientConstants() {
        // Prevent object creation
    }

    // Success messages
    public static final String PATIENT_REGISTERED =
            "Patient registered successfully";

    public static final String PATIENT_RETRIEVED =
            "Patient retrieved successfully";

    public static final String PATIENTS_RETRIEVED =
            "Patients retrieved successfully";

    public static final String PATIENT_UPDATED =
            "Patient updated successfully";

    public static final String PATIENT_DEACTIVATED =
            "Patient deactivated successfully";

    // Error messages
    public static final String PATIENT_NOT_FOUND =
            "Patient not found";

    public static final String EMAIL_ALREADY_EXISTS =
            "Patient email already exists";

    public static final String PHONE_ALREADY_EXISTS =
            "Patient phone number already exists";

    public static final String INTERNAL_SERVER_ERROR =
            "Something went wrong";
}
