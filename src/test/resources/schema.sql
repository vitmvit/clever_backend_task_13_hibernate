CREATE TABLE IF NOT EXISTS house
(
    id
    bigint
    PRIMARY
    KEY
    NOT
    NULL,
    uuid
    uuid
    NOT
    NULL,
    area
    character
    varying
(
    255
) NOT NULL,
    country character varying
(
    255
) NOT NULL,
    city character varying
(
    255
) NOT NULL,
    street character varying
(
    255
) NOT NULL,
    number integer NOT NULL,
    create_date timestamp
(
    6
),
    CONSTRAINT house_uuid_key UNIQUE
(
    uuid
)
    );

CREATE TABLE IF NOT EXISTS person
(
    id
    bigint
    PRIMARY
    KEY
    NOT
    NULL,
    uuid
    uuid
    NOT
    NULL,
    name
    character
    varying
(
    255
) NOT NULL,
    surname character varying
(
    255
) NOT NULL,
    sex character varying
(
    255
) NOT NULL,
    passport_series character varying
(
    255
),
    passport_number character varying
(
    255
),
    create_date timestamp
(
    6
),
    update_date timestamp
(
    6
),
    house_id bigint NOT NULL,
    CONSTRAINT person_passport_series_passport_number_key UNIQUE
(
    uuid,
    passport_series,
    passport_number
),
    CONSTRAINT person_uuid_key UNIQUE
(
    uuid
)
    );

CREATE TABLE IF NOT EXISTS house_owner
(
    house_id
    bigint
    NOT
    NULL,
    person_id
    bigint
    NOT
    NULL
);