create schema if not exists database_project;

use database_project;

# doctor
drop table if exists staff;
drop table if exists patient_status;
drop table if exists checklist;
drop table if exists bed;
drop table if exists patient;
drop table if exists hospital_nurse;
drop table if exists treatment_region;
drop table if exists doctor;
create table if not exists doctor
(
    id               varchar(20) not null unique,
    name             varchar(50) not null,
    password         varchar(50) not null,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male' check ( gender in ('male', 'female') ),
    primary key (id)
)charset = utf8;
insert into doctor(id, name, password, age, gender) VALUES ('D001', 'doctor1', '123456', 26, 'male');
insert into doctor(id, name, password, age, gender) VALUES ('D002', 'doctor2', '123456', 36, 'male');
insert into doctor(id, name, password, age, gender) VALUES ('D003', 'doctor3', '123456', 28, 'female');


# chief nurse
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


# emergency nurse
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


# treatment region
create table if not exists treatment_region
(
    level varchar(10) not null check ( level in ('quarantine', 'light', 'severe', 'critical')),  # 1, 2, 3 依次加重，0 表示在隔离区域
    nurse_resp_num int default 0 check ( nurse_resp_num >= 0 ),
    doctor_id varchar(20),
    nurse_id varchar(20),
    primary key (level),
    foreign key (doctor_id) references doctor(id),
    foreign key (nurse_id) references chief_nurse(id),
    check ( (level != 'quarantine' and nurse_resp_num is not null
        and doctor_id is not null and nurse_id is not null) or (level = 'quarantine'))
)charset = utf8;
insert into treatment_region(level, nurse_resp_num, doctor_id, nurse_id) VALUES ('quarantine', null, null, null);
insert into treatment_region(level, nurse_resp_num, doctor_id, nurse_id) VALUES ('light', 3, 'D001', 'C001');
insert into treatment_region(level, nurse_resp_num, doctor_id, nurse_id) VALUES ('severe', 2, 'D002', 'C002');
insert into treatment_region(level, nurse_resp_num, doctor_id, nurse_id) VALUES ('critical', 1, 'D003', 'C003');


# hospital nurse
create table if not exists hospital_nurse
(
    id               varchar(20) not null unique,
    name             varchar(50) not null,
    password         varchar(50) not null ,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male' check ( gender in ('male', 'female') ),
    current_resp_num int not null default 0 check ( current_resp_num >= 0 and current_resp_num <= 3 ),
    treatment_region_level varchar(20),
    primary key (id),
    foreign key (treatment_region_level) references treatment_region(level)
)charset = utf8;
insert into hospital_nurse(id, name, password, age, gender, treatment_region_level, current_resp_num) VALUES ('H001', 'hospital_nurse1', '123456', 26, 'female', 'light', 3);
insert into hospital_nurse(id, name, password, age, gender, treatment_region_level, current_resp_num) VALUES ('H002', 'hospital_nurse2', '123456', 26, 'female', 'light', 0);
insert into hospital_nurse(id, name, password, age, gender, treatment_region_level, current_resp_num) VALUES ('H003', 'hospital_nurse3', '123456', 36, 'male', 'severe', 1);
insert into hospital_nurse(id, name, password, age, gender, treatment_region_level, current_resp_num) VALUES ('H004', 'hospital_nurse4', '123456', 36, 'male', 'severe', 0);
insert into hospital_nurse(id, name, password, age, gender, treatment_region_level, current_resp_num) VALUES ('H005', 'hospital_nurse5', '123456', 28, 'female', 'critical', 1);
insert into hospital_nurse(id, name, password, age, gender, treatment_region_level, current_resp_num) VALUES ('H006', 'hospital_nurse6', '123456', 28, 'female', null, 0);


# patient
create table if not exists patient
(
    patient_id int not null auto_increment,
    name varchar(30) not null,
    gender varchar(10) default 'male' check ( gender in ('male', 'female') ),
    age int not null check ( age > 0 ),
    disease_level varchar(10) not null check ( disease_level in ('light', 'severe', 'critical')),
    life_status varchar(10) default 'treating' not null check ( life_status in ('healthy', 'treating', 'dead') ),
    nurse_id varchar(20),
    treatment_region_level varchar(10) default 'quarantine',
    primary key (patient_id),
    foreign key (nurse_id) references hospital_nurse(id),
    foreign key (treatment_region_level) references treatment_region(level)
)charset = utf8;
insert into patient(name, gender, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('Cathy', 'female', 20, 'light', 'treating', 'H001', 'light');
insert into patient(name, gender, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('Dan', 'male', 20, 'severe', 'treating', 'H003', 'severe');
insert into patient(name, gender, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('Eric', 'male', 20, 'critical', 'treating', 'H005', 'critical');
insert into patient(name, gender, age, disease_level, life_status, treatment_region_level) VALUES ('Frank', 'male', 25, 'light', 'treating', 'quarantine');
insert into patient(name, gender, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('Alice', 'female', 33,  'light', 'treating', 'H001', 'light');
insert into patient(name, gender, age, disease_level, life_status, nurse_id, treatment_region_level) VALUES ('Bob', 'male', 23,  'light', 'treating', 'H001', 'light');
create index patient_treatment_region_level on patient(treatment_region_level);


# bed
create table if not exists bed
(
    bed_id int auto_increment not null,
    patient_id int default null,
    treatment_region_level varchar(10) not null check ( treatment_region_level in ('light', 'severe', 'critical') ),
    primary key (bed_id),
    foreign key (patient_id) references patient(patient_id),
    foreign key (treatment_region_level) references treatment_region(level)
)charset = utf8;
insert into bed(treatment_region_level) values ('light');
# insert into bed(treatment_region_level) values ('light');
# insert into bed(treatment_region_level) values ('light');
insert into bed(treatment_region_level) values ('severe');
# insert into bed(treatment_region_level) values ('severe');
# insert into bed(treatment_region_level) values ('severe');
# insert into bed(treatment_region_level) values ('critical');
# insert into bed(treatment_region_level) values ('critical');
# insert into bed(treatment_region_level) values ('critical');
# TODO: 理论上像上边那样新增床位，下边的只是为了测试
insert into bed(patient_id, treatment_region_level) VALUES (1, 'light');
insert into bed(patient_id, treatment_region_level) VALUES (2, 'severe');
insert into bed(patient_id, treatment_region_level) VALUES (3, 'critical');
# insert into bed(patient_id, treatment_region_level) VALUES (4, 'light');
insert into bed(patient_id, treatment_region_level) VALUES (5, 'light');
insert into bed(patient_id, treatment_region_level) VALUES (6, 'light');


# check list
create table if not exists checklist
(
    id int auto_increment not null,
    test_result varchar(10) default '-',
    date timestamp,
    disease_level varchar(10) default '-',
    doctor_id varchar(20),
    patient_id int not null,
    primary key (id),
    foreign key (doctor_id) references doctor(id),
    foreign key (patient_id) references patient(patient_id),
    check ( (disease_level is null) or (disease_level is not null and disease_level in ('-', 'light', 'severe', 'critical')) ),
    check ( (test_result is null) or (test_result is not null and test_result in ('-', 'positive', 'negative')) )
)charset = utf8;
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('positive', '2020-12-20 13:30:20', 'critical', 'D002', 2);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('negative', '2020-12-25 13:30:20', 'light', 'D003', 3);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('positive', '2020-12-25 13:30:20', 'light', 'D001', 1);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('negative', '2020-12-26 13:30:20', 'light', 'D001', 1);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('negative', '2020-12-27 13:30:20', 'light', 'D001', 1);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('negative', '2020-12-27 13:30:20', 'light', null, 4);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('negative', '2020-12-27 13:30:20', 'light', null, 5);
insert into checklist(test_result, date, disease_level, doctor_id, patient_id) VALUES ('negative', '2020-12-27 13:30:20', 'light', null, 6);


#  patient status
create table if not exists patient_status
(
    id int auto_increment not null,
    temperature double not null,
    symptom varchar(100) not null,
    life_status varchar(10) not null check ( life_status in ('healthy', 'treating', 'dead') ),
    date timestamp not null,
    patient_id int not null,
    nurse_id varchar(20) not null,
    checklist_id int,
    primary key (id),
    foreign key (patient_id) references patient(patient_id),
    foreign key (nurse_id) references hospital_nurse(id),
    foreign key (checklist_id) references checklist(id)
)charset = utf8;
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (37.6, 'fever', 'treating', '2020-12-20 13:30:20', 2, 'H003', 1);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (37.1, 'healthy', 'treating', '2020-12-25 13:30:20', 3, 'H005', 2);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (37.0, 'healthy', 'treating', '2020-12-25 13:30:20', 1, 'H001', 3);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (37.1, 'healthy', 'treating', '2020-12-26 13:30:20', 1, 'H001', 4);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (36.1, 'healthy', 'treating', '2020-12-27 13:30:20', 1, 'H001', 5);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (36.1, 'healthy', 'treating', '2020-12-27 13:30:20', 4, 'H003', 6);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (36.1, 'healthy', 'treating', '2020-12-27 13:30:20', 5, 'H001', 7);
insert into patient_status(temperature, symptom, life_status, date, patient_id, nurse_id, checklist_id)
            VALUES (36.1, 'healthy', 'treating', '2020-12-27 13:30:20', 6, 'H001', 8);


# 站内信
create table if not exists message
(
    id int auto_increment,
    receiver_id varchar(20) not null,
    content varchar(100) not null,
    status int not null,
    time timestamp not null,
    primary key (id)
)charset = utf8;

# 创建用户并授权
drop user 'doctor'@'localhost';
create user 'doctor'@'localhost' identified by '123456';
grant select, update on doctor to 'doctor'@'localhost';
grant select, update on patient to 'doctor'@'localhost';
grant select on chief_nurse to 'doctor'@'localhost';
grant select, update on hospital_nurse to 'doctor'@'localhost';
grant select, insert on checklist to 'doctor'@'localhost';
grant select on patient_status to 'doctor'@'localhost';
grant select on treatment_region to 'doctor'@'localhost';
grant select, update on bed to 'doctor'@'localhost';

drop user 'chief_nurse'@'localhost';
create user 'chief_nurse'@'localhost' identified by '123456';
grant select, update on chief_nurse to 'chief_nurse'@'localhost';
grant select on patient to 'chief_nurse'@'localhost';
grant select, insert, delete, update on hospital_nurse to 'chief_nurse'@'localhost';
grant select, insert on checklist to 'chief_nurse'@'localhost';
grant select on treatment_region to 'chief_nurse'@'localhost';
grant select on patient_status to 'chief_nurse'@'localhost';
grant select on bed to 'chief_nurse'@'localhost';

drop user 'emergency_nurse'@'localhost';
create user 'emergency_nurse'@'localhost' identified by '123456';
grant select, update on emergency_nurse to 'emergency_nurse'@'localhost';
grant select, insert on checklist to 'emergency_nurse'@'localhost';
grant select, insert on patient to 'emergency_nurse'@'localhost';
grant select on treatment_region to 'emergency_nurse'@'localhost';

drop user 'hospital_nurse'@'localhost';
create user 'hospital_nurse'@'localhost' identified by '123456';
grant select, update on hospital_nurse to 'hospital_nurse'@'localhost';
grant select on patient to 'hospital_nurse'@'localhost';
grant select, update on checklist to 'hospital_nurse'@'localhost';
grant select, update, insert on patient_status to 'hospital_nurse'@'localhost';
grant select on treatment_region to 'hospital_nurse'@'localhost';

# select * from patient;
select * from checklist;
select * from hospital_nurse;
select * from patient_status;
