drop table if exists `member`;

create table `member`(
id bigint not null auto_increment,
login_id varchar(50) not null unique,
nickname varchar(50) not null unique,
password varchar(250) not null,
role varchar(30) not null,
created_date timestamp(3) not null default current_timestamp,
primary key(login_id)
);

