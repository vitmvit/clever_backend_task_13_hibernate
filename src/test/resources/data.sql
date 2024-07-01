INSERT INTO house (id, uuid, area, city, country, street, number, create_date)
VALUES (1, '9dd06f39-dba5-4533-8472-f1d7be435491', 'AreaTwo', 'City', 'Country', 'StreetOne', 34,
        '2024-01-19 13:16:13.992677'),
       (2, 'e9ae795e-cc7c-48d8-9621-1a441deff256', 'AreaOne', 'CityOne', 'Country', 'StreetTwo', 65,
        '2024-01-19 13:16:13.992677'),
       (3, '835915e7-2402-48a4-8f60-1364c6d99ed2', 'AreaThree', 'City', 'Country', 'StreetOne', 76,
        '2024-01-19 13:16:13.992677'),
       (4, 'fc4cab52-356b-48bd-83d8-c2fc377ba1ea', 'AreaOne', 'City', 'Country', 'StreetThree', 23,
        '2024-01-19 13:16:13.992677'),
       (5, '5e9832cf-3a74-40ae-9dae-c70f47f08804', 'AreaTwo', 'City', 'Country', 'Street', 1659,
        '2024-01-19 13:16:13.992677');

insert into person (create_date, house_id, name, passport_number, passport_series, sex, surname, update_date, uuid, id)
values ('2024-01-19 13:23:18.356477', 1, 'Sophia', '1234567', 'AB', 'MALE', 'Johnson', '2024-01-19 13:23:18.356477',
        'faa3c8d8-b6f8-4100-b253-3cd453a03da7', 1),
       ('2024-01-19 13:23:18.356477', 2, 'Noah', '5643216', 'AS', 'FEMALE', 'Wilson', '2024-01-19 13:23:18.356477',
        '1cd31719-2064-4f90-a909-d7dd3b880d1e', 2),
       ('2024-01-19 13:23:18.356477', 2, 'Olivia', '5554432', 'SB', 'MALE', 'Anderson', '2024-01-19 13:23:18.356477',
        '8ac37416-6e16-495f-a5ed-28af69e78ea7', 3),
       ('2024-01-19 13:23:18.356477', 3, 'Emma', '6675432', 'AG', 'FEMALE', 'Lee', '2024-01-19 13:23:18.356477',
        'b1bb5882-a45e-4982-b2bc-3fa626a83edf', 4),
       ('2024-01-19 13:23:18.356477', 4, 'Jackson', '9876543', 'RB', 'FEMALE', 'Harris', '2024-01-19 13:23:18.356477',
        '9701364c-e2d4-4857-aac9-488342b66d77', 5);

INSERT INTO house_owner (house_id, person_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (1, 2),
       (1, 3);