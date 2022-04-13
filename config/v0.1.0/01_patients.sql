CREATE TABLE application.patient
(
    id      serial NOT NULL,
    name    text   NOT NULL,
    surname text   not null,
    birth   date   null,
    number  text   null,
    email   text   null,
    address text   null,
    constraint patient_pk PRIMARY KEY (id),
    constraint patient_number_unique UNIQUE (number)
);
