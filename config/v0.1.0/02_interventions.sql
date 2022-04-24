CREATE TABLE application.intervention
(
    id            serial      NOT NULL,
    patient_id    serial      NOT NULL,
    "type"        text        NOT NULL,
    teeth         text        NULL,
    price         text        NOT NULL,
    intervened_at timestamptz not null,
    created_at    timestamptz null,
    constraint intervention_pk PRIMARY KEY (id),
    constraint intervention_patient_fk FOREIGN KEY (patient_id) REFERENCES application.patient (id)
);
