
package com.healthcare.patient.serviceImpl;

import com.healthcare.patient.audit.PatientAuditService;
import com.healthcare.patient.dto.request.PatientRegistrationRequest;
import com.healthcare.patient.dto.response.PatientResponse;
import com.healthcare.patient.dto.response.PatientSummaryResponse;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.mapper.PatientMapper;
import com.healthcare.patient.repository.PatientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private PatientAuditService patientAuditService;

    @InjectMocks
    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        // Mockito initializes mocks automatically.
    }

    // Test 1: Successful patient registration
    @Test
    void registerPatient_shouldRegisterPatient_whenValidRequest() {

        PatientRegistrationRequest request =
                mock(PatientRegistrationRequest.class);

        Patient patient = mock(Patient.class);
        PatientResponse patientResponse = mock(PatientResponse.class);

        when(request.getEmail()).thenReturn("test@gmail.com");
        when(request.getPhoneNumber()).thenReturn("9876543210");

        when(patientRepository.existsByEmail("test@gmail.com"))
                .thenReturn(false);

        when(patientRepository.existsByPhoneNumber("9876543210"))
                .thenReturn(false);

        when(patientMapper.toEntity(request))
                .thenReturn(patient);

        when(patientRepository.save(patient))
                .thenReturn(patient);

        when(patient.getId()).thenReturn(1L);

        when(patientMapper.toResponse(patient))
                .thenReturn(patientResponse);

        var response = patientService.registerPatient(request);

        assertNotNull(response);

        verify(patientRepository).save(patient);

        verify(patientAuditService).logActivity(
                1L,
                "REGISTER",
                "New patient registered",
                "SYSTEM"
        );

        verify(patientMapper).toResponse(patient);
    }

    // Test 2: Duplicate email
    @Test
    void registerPatient_shouldThrowConflict_whenEmailAlreadyExists() {

        PatientRegistrationRequest request =
                mock(PatientRegistrationRequest.class);

        when(request.getEmail()).thenReturn("test@gmail.com");

        when(patientRepository.existsByEmail("test@gmail.com"))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> patientService.registerPatient(request)
        );

        assertEquals(
                HttpStatus.CONFLICT,
                exception.getStatusCode()
        );

        verify(patientRepository, never()).save(any());
    }

    // Test 3: Duplicate phone number
    @Test
    void registerPatient_shouldThrowConflict_whenPhoneAlreadyExists() {

        PatientRegistrationRequest request =
                mock(PatientRegistrationRequest.class);

        when(request.getEmail()).thenReturn("new@gmail.com");
        when(request.getPhoneNumber()).thenReturn("9876543210");

        when(patientRepository.existsByEmail("new@gmail.com"))
                .thenReturn(false);

        when(patientRepository.existsByPhoneNumber("9876543210"))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> patientService.registerPatient(request)
        );

        assertEquals(
                HttpStatus.CONFLICT,
                exception.getStatusCode()
        );

        verify(patientRepository, never()).save(any());
    }


    @Test
    void getPatientById_shouldReturnPatient_whenPatientExists() {

        Patient patient = mock(Patient.class);
        PatientResponse patientResponse = mock(PatientResponse.class);

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        when(patientMapper.toResponse(patient))
                .thenReturn(patientResponse);

        var response = patientService.getPatientById(1L);

        assertNotNull(response);

        verify(patientRepository).findById(1L);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void getPatientById_shouldThrowNotFound_whenPatientDoesNotExist() {

        when(patientRepository.findById(100L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> patientService.getPatientById(100L)
        );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );

        verify(patientMapper, never()).toResponse(any());
    }

    @Test
    void getPatientById_shouldThrowForbidden_whenPatientAccessesAnotherProfile() {

        Patient patient = mock(Patient.class);

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        when(patient.getEmail())
                .thenReturn("another@gmail.com");

        var authentication = new UsernamePasswordAuthenticationToken(
                "patient@gmail.com",
                null,
                java.util.List.of(
                        new SimpleGrantedAuthority("ROLE_PATIENT")
                )
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> patientService.getPatientById(1L)
            );

            assertEquals(
                    HttpStatus.FORBIDDEN,
                    exception.getStatusCode()
            );

            verify(patientMapper, never()).toResponse(any());

        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    @Test
    void getPatientByEmail_shouldReturnPatient_whenEmailExists() {

        String email = "patient@gmail.com";

        Patient patient = mock(Patient.class);
        PatientResponse patientResponse = mock(PatientResponse.class);

        when(patientRepository.findByEmail(email))
                .thenReturn(Optional.of(patient));

        when(patientMapper.toResponse(patient))
                .thenReturn(patientResponse);

        var response = patientService.getPatientByEmail(email);

        assertNotNull(response);

        verify(patientRepository).findByEmail(email);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void getPatientByEmail_shouldThrowNotFound_whenEmailDoesNotExist() {

        String email = "unknown@gmail.com";

        when(patientRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> patientService.getPatientByEmail(email)
        );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );

        verify(patientMapper, never()).toResponse(any());
    }


    @Test
    void getAllPatients_shouldReturnPatientList_whenPatientsExist() {

        Patient patient1 = mock(Patient.class);
        Patient patient2 = mock(Patient.class);

        PatientSummaryResponse response1 =
                mock(PatientSummaryResponse.class);

        PatientSummaryResponse response2 =
                mock(PatientSummaryResponse.class);

        when(patientRepository.findAll())
                .thenReturn(List.of(patient1, patient2));

        when(patientMapper.toSummaryResponse(patient1))
                .thenReturn(response1);

        when(patientMapper.toSummaryResponse(patient2))
                .thenReturn(response2);

        var response = patientService.getAllPatients();

        assertNotNull(response);
        assertEquals(2, response.getData().size());

        verify(patientRepository).findAll();
        verify(patientMapper, times(2)).toSummaryResponse(any(Patient.class));
    }

    @Test
    void getAllPatients_shouldReturnEmptyList_whenNoPatientsExist() {

        when(patientRepository.findAll())
                .thenReturn(List.of());

        var response = patientService.getAllPatients();

        assertNotNull(response);
        assertTrue(response.getData().isEmpty());

        verify(patientRepository).findAll();
        verify(patientMapper, never()).toSummaryResponse(any());
    }



}
