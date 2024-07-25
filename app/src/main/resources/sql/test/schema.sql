-- 외래 키를 가진 테이블부터 삭제
DROP TABLE IF EXISTS `chat`;
DROP TABLE IF EXISTS `participant`;
DROP TABLE IF EXISTS `board_post`;
DROP TABLE IF EXISTS `file`;

-- 그 다음 참조되는 테이블 삭제
DROP TABLE IF EXISTS `room`;
DROP TABLE IF EXISTS `board`;
DROP TABLE IF EXISTS `member`;

-- 이제 테이블 생성 (참조되는 테이블부터 생성)
CREATE TABLE `member` (
    id bigint not null auto_increment,
    login_id varchar(50) not null unique,
    nickname varchar(50) not null unique,
    password varchar(250) not null,
    role varchar(30) not null,
    created_date timestamp(3) not null default current_timestamp,
    update_date timestamp(3),
    primary key(id)
);

CREATE TABLE `board` (
    id bigint not null auto_increment,
    board_name varchar(50) not null,
    is_activated tinyint not null,
    create_date timestamp(3) not null default current_timestamp,
    update_date timestamp(3),
    primary key(id)
);

CREATE TABLE `room` (
    id bigint not null auto_increment,
    room_name varchar(50) not null,
    is_private tinyint not null default 0,
    create_date timestamp(3) not null default current_timestamp,
    update_date timestamp(3),
    primary key(id)
);

CREATE TABLE `board_post` (
    id bigint not null auto_increment,
    board_id bigint not null,
    writer_id bigint,
    title varchar(255) not null,
    text_content text,
    has_file tinyint not null,
    create_date timestamp(3) not null default current_timestamp,
    update_date timestamp(3),
    primary key(id),
    FOREIGN KEY (board_id) REFERENCES board(id),
    FOREIGN KEY (writer_id) REFERENCES member(id)
);

CREATE TABLE `chat` (
    id bigint not null auto_increment,
    chat_type varchar(50) not null,
    message varchar(500),
    room_id bigint not null,
    sender_nickname varchar(50),
    has_files tinyint not null,
    create_date timestamp(3) not null default current_timestamp,
    primary key(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE `participant` (
    id bigint not null auto_increment,
    room_id bigint not null,
    member_id bigint not null,
    invitable tinyint not null,
    create_date timestamp(3) not null default current_timestamp,
    update_date timestamp(3),
    primary key(id),
    FOREIGN KEY (room_id) REFERENCES room(id),
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE `file` (
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