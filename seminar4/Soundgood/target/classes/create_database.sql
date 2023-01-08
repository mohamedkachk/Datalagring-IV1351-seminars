CREATE TABLE email (
 id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 email VARCHAR(500) NOT NULL
);

ALTER TABLE email ADD CONSTRAINT PK_email PRIMARY KEY (id);


CREATE TABLE instrument (
 instrument_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 name VARCHAR(500) NOT NULL,
 brand VARCHAR(500),
 price INT
);

ALTER TABLE instrument ADD CONSTRAINT PK_instrument PRIMARY KEY (instrument_id);


CREATE TABLE person (
 person_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 person_number VARCHAR(500),
 first_name VARCHAR(500),
 last_name VARCHAR(500),
 street VARCHAR(500),
 zip VARCHAR(500),
 city VARCHAR(500)
);

ALTER TABLE person ADD CONSTRAINT PK_person PRIMARY KEY (person_id);


CREATE TABLE person_email (
 email_id INT NOT NULL,
 person_id INT NOT NULL
);

ALTER TABLE person_email ADD CONSTRAINT PK_person_email PRIMARY KEY (email_id,person_id);


CREATE TABLE phone (
 id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 phone_no VARCHAR(500) NOT NULL
);

ALTER TABLE phone ADD CONSTRAINT PK_phone PRIMARY KEY (id);


CREATE TABLE student (
 student_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 person_id INT NOT NULL,
 skill_level VARCHAR(500)
);

ALTER TABLE student ADD CONSTRAINT PK_student PRIMARY KEY (student_id);


CREATE TABLE contract_of_rental (
 contract_of_rental_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 student_id INT NOT NULL,
 instrument_id INT NOT NULL,
 monthly_price INT NOT NULL,
 start_time TIMESTAMP(6) NOT NULL,
 end_time TIMESTAMP(6) NOT NULL
);

ALTER TABLE contract_of_rental ADD CONSTRAINT PK_contract_of_rental PRIMARY KEY (contract_of_rental_id);


CREATE TABLE instructor (
 instructor_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 person_id INT NOT NULL,
 teach_ensamble BOOLEAN
);

ALTER TABLE instructor ADD CONSTRAINT PK_instructor PRIMARY KEY (instructor_id);


CREATE TABLE instructor_instrument (
 instrument_id INT NOT NULL,
 instructor_id INT NOT NULL
);

ALTER TABLE instructor_instrument ADD CONSTRAINT PK_instructor_instrument PRIMARY KEY (instrument_id,instructor_id);


CREATE TABLE lesson (
 lesson_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 instrument_id INT NOT NULL,
 instructor_id INT NOT NULL,
 lesson_type VARCHAR(500) NOT NULL,
 skill_level VARCHAR(500),
 start_time TIMESTAMP(6) NOT NULL,
 end_time TIMESTAMP(6) NOT NULL
);

ALTER TABLE lesson ADD CONSTRAINT PK_lesson PRIMARY KEY (lesson_id);


CREATE TABLE parent (
 person_id INT NOT NULL,
 student_id INT NOT NULL
);

ALTER TABLE parent ADD CONSTRAINT PK_parent PRIMARY KEY (person_id,student_id);


CREATE TABLE person_phone (
 phone_id INT NOT NULL,
 person_id INT NOT NULL
);

ALTER TABLE person_phone ADD CONSTRAINT PK_person_phone PRIMARY KEY (phone_id,person_id);


CREATE TABLE sibling (
 person_id INT NOT NULL,
 student_id INT NOT NULL
);

ALTER TABLE sibling ADD CONSTRAINT PK_sibling PRIMARY KEY (person_id,student_id);


CREATE TABLE student_payment (
 student_payment_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 student_id INT NOT NULL,
 contract_of_rental_id INT,
 discount DECIMAL(10)
);

ALTER TABLE student_payment ADD CONSTRAINT PK_student_payment PRIMARY KEY (student_payment_id);


CREATE TABLE available_lesson (
 available_lesson_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 instructor_id INT NOT NULL,
 lesson_type CHAR(500) NOT NULL,
 time TIMESTAMP(6) NOT NULL,
 skill_level VARCHAR(500)
);

ALTER TABLE available_lesson ADD CONSTRAINT PK_available_lesson PRIMARY KEY (available_lesson_id);


CREATE TABLE group_lesson (
 group_lesson_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 min_no_students INT NOT NULL,
 max_no_students INT NOT NULL,
 scheduled_lesson_id INT NOT NULL,
 skill_level VARCHAR(500),
 genre VARCHAR(500)
);

ALTER TABLE group_lesson ADD CONSTRAINT PK_group_lesson PRIMARY KEY (group_lesson_id);


CREATE TABLE group_student (
 group_lesson_id INT NOT NULL,
 student_id INT NOT NULL
);

ALTER TABLE group_student ADD CONSTRAINT PK_group_student PRIMARY KEY (group_lesson_id,student_id);


CREATE TABLE individual_lesson (
 student_id INT NOT NULL,
 available_lesson_id INT NOT NULL
);

ALTER TABLE individual_lesson ADD CONSTRAINT PK_individual_lesson PRIMARY KEY (student_id,available_lesson_id);


CREATE TABLE instructor_payment (
 instructor_payment_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
 instructor_id INT NOT NULL,
 lesson_id INT NOT NULL,
 worked_hours INT NOT NULL
);

ALTER TABLE instructor_payment ADD CONSTRAINT PK_instructor_payment PRIMARY KEY (instructor_payment_id);


ALTER TABLE person_email ADD CONSTRAINT FK_person_email_0 FOREIGN KEY (email_id) REFERENCES email (id);
ALTER TABLE person_email ADD CONSTRAINT FK_person_email_1 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE student ADD CONSTRAINT FK_student_0 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE contract_of_rental ADD CONSTRAINT FK_contract_of_rental_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE contract_of_rental ADD CONSTRAINT FK_contract_of_rental_1 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);


ALTER TABLE instructor ADD CONSTRAINT FK_instructor_0 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE instructor_instrument ADD CONSTRAINT FK_instructor_instrument_0 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);
ALTER TABLE instructor_instrument ADD CONSTRAINT FK_instructor_instrument_1 FOREIGN KEY (instructor_id) REFERENCES instructor (instructor_id);


ALTER TABLE lesson ADD CONSTRAINT FK_lesson_0 FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id);
ALTER TABLE lesson ADD CONSTRAINT FK_lesson_1 FOREIGN KEY (instructor_id) REFERENCES instructor (instructor_id);


ALTER TABLE parent ADD CONSTRAINT FK_parent_0 FOREIGN KEY (person_id) REFERENCES person (person_id);
ALTER TABLE parent ADD CONSTRAINT FK_parent_1 FOREIGN KEY (student_id) REFERENCES student (student_id);


ALTER TABLE person_phone ADD CONSTRAINT FK_person_phone_0 FOREIGN KEY (phone_id) REFERENCES phone (id);
ALTER TABLE person_phone ADD CONSTRAINT FK_person_phone_1 FOREIGN KEY (person_id) REFERENCES person (person_id);


ALTER TABLE sibling ADD CONSTRAINT FK_sibling_0 FOREIGN KEY (person_id) REFERENCES person (person_id);
ALTER TABLE sibling ADD CONSTRAINT FK_sibling_1 FOREIGN KEY (student_id) REFERENCES student (student_id);


ALTER TABLE student_payment ADD CONSTRAINT FK_student_payment_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE student_payment ADD CONSTRAINT FK_student_payment_1 FOREIGN KEY (contract_of_rental_id) REFERENCES contract_of_rental (contract_of_rental_id);


ALTER TABLE available_lesson ADD CONSTRAINT FK_available_lesson_0 FOREIGN KEY (instructor_id) REFERENCES instructor (instructor_id);


ALTER TABLE group_lesson ADD CONSTRAINT FK_group_lesson_0 FOREIGN KEY (scheduled_lesson_id) REFERENCES available_lesson (available_lesson_id);


ALTER TABLE group_student ADD CONSTRAINT FK_group_student_0 FOREIGN KEY (group_lesson_id) REFERENCES group_lesson (group_lesson_id);
ALTER TABLE group_student ADD CONSTRAINT FK_group_student_1 FOREIGN KEY (student_id) REFERENCES student (student_id);


ALTER TABLE individual_lesson ADD CONSTRAINT FK_individual_lesson_0 FOREIGN KEY (student_id) REFERENCES student (student_id);
ALTER TABLE individual_lesson ADD CONSTRAINT FK_individual_lesson_1 FOREIGN KEY (available_lesson_id) REFERENCES available_lesson (available_lesson_id);


ALTER TABLE instructor_payment ADD CONSTRAINT FK_instructor_payment_0 FOREIGN KEY (instructor_id) REFERENCES instructor (instructor_id);
ALTER TABLE instructor_payment ADD CONSTRAINT FK_instructor_payment_1 FOREIGN KEY (lesson_id) REFERENCES lesson (lesson_id);


