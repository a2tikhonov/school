-- liquibase formatted sql

-- changeset atikhonov:1
CREATE INDEX student_idx ON student (name);
-- changeset atikhonov:2
CREATE INDEX faculty_idx ON faculty (color, name);
