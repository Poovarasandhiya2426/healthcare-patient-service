
-- ==========================================
-- V2: Create Patient Management Tables
-- ==========================================

-- 1. PATIENTS TABLE

CREATE TABLE patients (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          phone_number VARCHAR(20) NOT NULL UNIQUE,
                          date_of_birth DATE NOT NULL,
                          gender VARCHAR(20) NOT NULL,
                          blood_group VARCHAR(10),
                          profile_image_url VARCHAR(500),
                          status VARCHAR(20) NOT NULL,
                          created_at DATETIME(6) NOT NULL,
                          updated_at DATETIME(6)
);


-- 2. PATIENT ADDRESSES TABLE

CREATE TABLE patient_addresses (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   address_line VARCHAR(255),
                                   city VARCHAR(100),
                                   state VARCHAR(100),
                                   country VARCHAR(100),
                                   postal_code VARCHAR(20),
                                   address_type VARCHAR(50),
                                   patient_id BIGINT NOT NULL,

                                   CONSTRAINT fk_address_patient
                                       FOREIGN KEY (patient_id)
                                           REFERENCES patients(id)
);


-- 3. EMERGENCY CONTACTS TABLE

CREATE TABLE emergency_contacts (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    contact_name VARCHAR(100),
                                    phone_number VARCHAR(20),
                                    relationship VARCHAR(50),
                                    email VARCHAR(150),
                                    patient_id BIGINT NOT NULL,

                                    CONSTRAINT fk_emergency_patient
                                        FOREIGN KEY (patient_id)
                                            REFERENCES patients(id)
);


-- 4. PATIENT AUDIT LOGS TABLE

CREATE TABLE patient_audit_logs (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    patient_id BIGINT NOT NULL,
                                    action VARCHAR(100) NOT NULL,
                                    description VARCHAR(500),
                                    performed_by VARCHAR(100),
                                    performed_at DATETIME(6) NOT NULL,

                                    INDEX idx_patient_audit_patient_id (patient_id),
                                    INDEX idx_patient_audit_action (action)
);
