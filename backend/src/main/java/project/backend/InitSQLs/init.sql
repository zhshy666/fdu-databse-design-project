-- 创建 schema
create schema if not exists database_project;

-- 建表
use database_project;
drop table if exists staff;
-- staff
create table if not exists staff
(
    id               int auto_increment,
    type             varchar(20) check ( type in ('doctor', 'chief_nurse', 'emergency_nurse', 'hospital_nurse') ),
    username         varchar(50) not null,
    password         varchar(50) not null ,
    age              int not null check ( age > 0 ),
    gender           varchar(10) default 'male',
    current_resp_num integer,
    PRIMARY KEY (id),
    -- TODO: current_resp_num 还需要有一个最大值的约束，取决于该护士所在的治疗区域，之后实行一下
    check ( (type != 'hospital_nurse') or (type = 'hospital_nurse' and current_resp_num is not null and current_resp_num >= 0) )
)charset=utf8;
-- 构造数据
insert into staff(type, username, password, age, gender) values('doctor', 'Tom', '123456', 30, 'male');
insert into staff(type, username, password, age, gender, current_resp_num) values('hospital_nurse', 'Jerry', '123456', 30, 'male', 3);