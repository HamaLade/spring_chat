--member
drop table if exists `member`;
create table `member`(
id bigint not null auto_increment,
login_id varchar(50) not null unique,
nickname varchar(50) not null unique,
password varchar(250) not null,
role varchar(30) not null,
created_date timestamp(3) not null default current_timestamp,
update_date timestamp(3),
primary key(login_id)
);


--board
drop table if exists `board`;
create table `board`(
id bigint not null auto_increment,
board_name varchar(50) not null,
is_activated tinyint not null,
create_date timestamp(3) not null default current_timestamp,
update_date timestamp(3),
primary key(id)
);



--board post
drop table if exists `board_post`;
create table `board_post`(
id bigint not null auto_increment,
board_id bigint not null,
writer_id bigint,
title varchar(255) not null,
text_content text,
has_file tinyint not null,
create_date timestamp(3) not null default current_timestamp,
update_date timestamp(3),
primary key(id)
);



--room
drop table if exists `room`;
create table `room`(
id bigint not null auto_increment,
room_name varchar(50) not null,
is_private tinyint not null default 0,
create_date timestamp(3) not null default current_timestamp,
update_date timestamp(3),
primary key(id)
);



--participant
drop table if exists `participant`;
create table `participant`(
id bigint not null auto_increment,
room_id bigint not null,
member_id bigint not null,
invitable tinyint not null,
create_date timestamp(3) not null default current_timestamp,
update_date timestamp(3),
primary key(id)
);



--file
drop table if exists `file`;
create table `file`(
id bigint not null auto_increment,
file_size bigint not null,
file_name_extension varchar(10) not null,
file_name varchar(512) not null,
file_name_original varchar(512) not null,
referrer_id bigint,
file_referrer varchar(50) not null,
create_date timestamp(3) not null default current_timestamp,
update_date timestamp(3),
primary key(id)
);