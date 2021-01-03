create schema if not exists database_project;

use database_project;

drop table if exists staff;
drop table if exists patient_status;
drop table if exists checklist;
drop table if exists patient;
drop table if exists bed;
drop table if exists treatment_region;
drop table if exists doctor;
create table if not exists doctor
(
    id               varchar(20) not null unique,
    name             varchar(50) not null,
    password         varchar(50) not null ,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male' check ( gender in ('male', 'female') ),
    primary key (id)
)charset = utf8;
insert into doctor(id, name, password, age, gender) VALUES ('D001', 'doctor1', '123456', 26, 'male');
insert into doctor(id, name, password, age, gender) VALUES ('D002', 'doctor2', '123456', 36, 'male');
insert into doctor(id, name, password, age, gender) VALUES ('D003', 'doctor3', '123456', 28, 'female');


drop table if exists chief_nurse;
create table if not exists chief_nurse
(
    id               varchar(20) not null unique,
    name             varchar(50) not null,
    password         varchar(50) not null ,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male' check ( gender in ('male', 'female') ),
    primary key (id)
)charset = utf8;
insert into chief_nurse(id, name, password, age, gender) VALUES ('C001', 'chief_nurse1', '123456', 26, 'female');
insert into chief_nurse(id, name, password, age, gender) VALUES ('C002', 'chief_nurse2', '123456', 36, 'male');
insert into chief_nurse(id, name, password, age, gender) VALUES ('C003', 'chief_nurse3', '123456', 28, 'female');


drop table if exists emergency_nurse;
create table if not exists emergency_nurse
(
    id               varchar(20) not null unique,
    name             varchar(50) not null,
    password         varchar(50) not null ,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male' check ( gender in ('male', 'female') ),
    primary key (id)
)charset = utf8;
insert into emergency_nurse(id, name, password, age, gender) VALUES ('E001', 'emergency_nurse1', '123456', 26, 'female');
insert into emergency_nurse(id, name, password, age, gender) VALUES ('E002', 'emergency_nurse2', '123456', 36, 'male');
insert into emergency_nurse(id, name, password, age, gender) VALUES ('E003', 'emergency_nurse3', '123456', 28, 'female');


drop table if exists hospital_nurse;
create table if not exists hospital_nurse
(
    id               varchar(20) not null unique,
    name             varchar(50) not null,
    password         varchar(50) not null ,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male' check ( gender in ('male', 'female') ),
    current_resp_num int not null default 0 check ( current_resp_num >= 0 and current_resp_num <= 3 ),
    primary key (id)
)charset = utf8;
insert into hospital_nurse(id, name, password, age, gender) VALUES ('H001', 'hospital_nurse1', '123456', 26, 'female');
insert into hospital_nurse(id, name, password, age, gender) VALUES ('H002', 'hospital_nurse2', '123456', 36, 'male');
insert into hospital_nurse(id, name, password, age, gender) VALUES ('H003', 'hospital_nurse3', '123456', 28, 'female');


create table if not exists treatment_region
(
    level int default 0 not null check ( level >= 0 and level <= 3),  # 1, 2, 3 依次加重，0 表示在隔离区域
    nurse_num int default 0 check ( nurse_num >= 0 ),
    nurse_resp_num int default 0 check ( nurse_resp_num >= 0 ),
    doctor_id varchar(20),
    nurse_id varchar(20),
    primary key (level),
    foreign key (doctor_id) references doctor(id),
    foreign key (nurse_id) references chief_nurse(id),
    check ( (level != 0 and nurse_num is not null and nurse_resp_num is not null
        and doctor_id is not null and nurse_id is not null) or (level = 0))
)charset = utf8;
insert into treatment_region(level, nurse_num, nurse_resp_num, doctor_id, nurse_id) VALUES (0, null, null, null, null);
insert into treatment_region(level, nurse_num, nurse_resp_num, doctor_id, nurse_id) VALUES (1, 10, 3, 'D001', 'C001');
insert into treatment_region(level, nurse_num, nurse_resp_num, doctor_id, nurse_id) VALUES (2, 10, 2, 'D002', 'C002');
insert into treatment_region(level, nurse_num, nurse_resp_num, doctor_id, nurse_id) VALUES (3, 10, 1, 'D003', 'C003');


create table if not exists patient
(
    patient_id int not null auto_increment,
    name varchar(30) not null,
    gender varchar(10) default 'male' check ( gender in ('male', 'female') ),
    age int not null check ( age > 0 ),
    disease_level int not null check ( disease_level >= 1 and disease_level <= 3 ),
    life_status varchar(10) not null check ( life_status in ('healthy', 'treating', 'dead') ),
    nurse_id varchar(20),
    treatment_region_level int not null,
    primary key (patient_id),
    foreign key (nurse_id) references chief_nurse(id),
    foreign key (treatment_region_level) references treatment_region(level)
)charset = utf8;
insert into patient(name, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('p1', 20, 1, 'treating', 'C001', 1);
insert into patient(name, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('p2', 20, 2, 'treating', 'C002', 2);
insert into patient(name, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('p3', 20, 3, 'treating', 'C003', 3);


create table if not exists bed
(
    bed_id int auto_increment not null,
    patient_id int,
    treatment_region_level int not null check ( treatment_region_level >= 1 and treatment_region_level <= 3 ),
    primary key (bed_id),
    foreign key (treatment_region_level) references treatment_region(level)
)charset = utf8;
insert into bed(patient_id, treatment_region_level) VALUES (1, 1);
insert into bed(patient_id, treatment_region_level) VALUES (2, 2);
insert into bed(patient_id, treatment_region_level) VALUES (3, 3);


create table if not exists checklist
(
    id int auto_increment not null,
    test_result varchar(10) not null check ( test_result in ('positive', 'negative') ),
    date datetime not null,
    # ToDo: 这个病情评级和上边病人的病情评级严格来说没有 reference 的必要，到时候如果这边有新的记录记得同步病人的病情评级属性
    disease_level int not null check ( disease_level >= 1 and disease_level <= 3 ),
    doctor_id varchar(20) not null,
    patient_id int not null,
    primary key (id),
    foreign key (doctor_id) references doctor(id),
    foreign key (patient_id) references patient(patient_id)
)charset = utf8;
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('positive', '2020-12-20 13:30:20', 3, 'D003', 1);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('negative', '2020-12-25 13:30:20', 1, 'D001', 3);


create table if not exists patient_status
(
    id int auto_increment not null,
    temperature double not null,
    symptom varchar(100) not null,
    life_status varchar(10) not null check ( life_status in ('healthy', 'treating', 'dead') ),
    date datetime not null,
    patient_id int not null,
    nurse_id varchar(20) not null,
    checklist_id int not null,
    primary key (id),
    foreign key (patient_id) references patient(patient_id),
    foreign key (nurse_id) references chief_nurse(id),
    foreign key (checklist_id) references checklist(id)
)charset = utf8;
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (37.6, 'fever', 'treating', '2020-12-20 13:30:20', 3, 'C001', 1);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (37.1, 'healthy', 'treating', '2020-12-25 13:30:20', 1, 'C001', 2);

drop user 'doctor'@'localhost';
create user 'doctor'@'localhost' identified by '123456';
grant select, update on doctor to 'doctor'@'localhost';
grant select, update on patient to 'doctor'@'localhost';
grant select on chief_nurse to 'doctor'@'localhost';
grant select on hospital_nurse to 'doctor'@'localhost';
grant select, insert on checklist to 'doctor'@'localhost';

drop user 'chief_nurse'@'localhost';
create user 'chief_nurse'@'localhost' identified by '123456';
grant select, update on chief_nurse to 'chief_nurse'@'localhost';
grant select on patient to 'chief_nurse'@'localhost';
grant select, insert, delete on hospital_nurse to 'chief_nurse'@'localhost';
grant select on bed to 'chief_nurse'@'localhost';

drop user 'emergency_nurse'@'localhost';
create user 'emergency_nurse'@'localhost' identified by '123456';
grant select, update on emergency_nurse to 'emergency_nurse'@'localhost';
grant select, insert on patient to 'emergency_nurse'@'localhost';
grant select, insert on patient_status to 'emergency_nurse'@'localhost';
grant select on treatment_region to 'emergency_nurse'@'localhost';

drop user 'hospital_nurse'@'localhost';
create user 'hospital_nurse'@'localhost' identified by '123456';
grant select, update on hospital_nurse to 'hospital_nurse'@'localhost';
grant select on patient to 'hospital_nurse'@'localhost';
grant select, update on checklist to 'hospital_nurse'@'localhost';
grant select, update, insert on patient_status to 'hospital_nurse'@'localhost';

