-- Add Identity Service user mapping to patients table

ALTER TABLE patients
    ADD COLUMN identity_user_id BIGINT NULL;

ALTER TABLE patients
    ADD CONSTRAINT uk_patients_identity_user_id
        UNIQUE (identity_user_id);