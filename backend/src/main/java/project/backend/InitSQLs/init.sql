create schema if not exists database_project;


use database_project;

drop table if exists staff;
drop table if exists treatment_region;
create table if not exists treatment_region
(
    level int default 0 not null check ( level >= 0 and level <= 3),  # 1, 2, 3 依次加重，0 表示在隔离区域
    nurse_num int default 0 check ( nurse_num >= 0 ),
    nurse_resp_num int default 0 check ( nurse_resp_num >= 0 ),
    primary key (level),
    check ( (level != 0 and nurse_num is not null and nurse_resp_num is not null) or (level = 0))
)charset=utf8;
insert into treatment_region(level, nurse_num, nurse_resp_num) VALUES (0, null, null);
insert into treatment_region(level, nurse_num, nurse_resp_num) VALUES (1, 10, 3);
insert into treatment_region(level, nurse_num, nurse_resp_num) VALUES (2, 10, 2);
insert into treatment_region(level, nurse_num, nurse_resp_num) VALUES (3, 10, 1);



drop table if exists staff;
create table if not exists staff
(
    id               varchar(20) not null,
    type             varchar(20) check ( type in ('doctor', 'chief_nurse', 'emergency_nurse', 'hospital_nurse') ),
    name             varchar(50) not null unique,
    password         varchar(50) not null ,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male' check ( gender in ('male', 'female') ),
    current_resp_num integer,
    treatment_region_level integer,
    PRIMARY KEY (id),
    foreign key (treatment_region_level) references treatment_region(level),
    check ( (type != 'hospital_nurse') or (type = 'hospital_nurse' and current_resp_num is not null and current_resp_num >= 0) ),
    check ( (type = 'emergency_nurse') or (type != 'emergency_nurse' and treatment_region_level is not null and treatment_region_level >= 1 and treatment_region_level <= 3) )
)charset=utf8;

insert into staff(id, type, name, password, age, gender, treatment_region_level) values('D001','doctor', 'Tom', '123456', 30, 'male', 1);
insert into staff(id, type, name, password, age, gender, treatment_region_level) values('D002','doctor', 'doctor2', '123456', 40, 'male', 2);
insert into staff(id, type, name, password, age, gender, treatment_region_level) values('D003','doctor', 'doctor3', '123456', 32, 'female', 3);
insert into staff(id, type, name, password, age, gender, treatment_region_level) values('C001','chief_nurse', 'chief_nurse1', '123456', 32, 'female', 1);
insert into staff(id, type, name, password, age, gender, treatment_region_level) values('C002','chief_nurse', 'chief_nurse2', '123456', 32, 'female', 2);
insert into staff(id, type, name, password, age, gender, treatment_region_level) values('C003','chief_nurse', 'chief_nurse3', '123456', 32, 'female', 3);
insert into staff(id, type, name, password, age, gender) values('E001','emergency_nurse', 'emergence_nurse1', '123456', 28, 'female');
insert into staff(id, type, name, password, age, gender, current_resp_num, treatment_region_level) values('H001','hospital_nurse', 'hospital_nurse1', '123456', 30, 'male', 0, 1);
insert into staff(id, type, name, password, age, gender, current_resp_num, treatment_region_level) values('H002','hospital_nurse', 'hospital_nurse2', '123456', 30, 'male', 0, 2);
insert into staff(id, type, name, password, age, gender, current_resp_num, treatment_region_level) values('H003','hospital_nurse', 'hospital_nurse3', '123456', 30, 'male', 0, 3);


