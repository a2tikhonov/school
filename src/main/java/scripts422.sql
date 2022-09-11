CREATE TABLE car (
                     id SERIAL PRIMARY KEY,
                     brand TEXT,
                     part_number TEXT UNIQUE,
                     price INTEGER CHECK ( price >= 50000 ) NOT NULL
);

CREATE TABLE men (
                     id SERIAL PRIMARY KEY,
                     name TEXT,
                     age SMALLINT CHECK ( age >= 16 ) NOT NULL,
                     car_rights BOOLEAN,
                     car_id SERIAL REFERENCES car (id)
);