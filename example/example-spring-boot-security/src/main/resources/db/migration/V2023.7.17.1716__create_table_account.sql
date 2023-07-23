drop database if exists account;
create table account
(
    id          varchar(32) primary key comment 'primary key',
    name        varchar(50) unique not null default '' comment 'name',
    age         tinyint unsigned   not null default 0 comment 'age',
    phone       varchar(11)        not null default '' comment 'phone number',
    birthday    datetime                    default null comment 'birthday',
    create_time datetime                    default null comment 'created time',
    update_time datetime                    default null comment 'updated time',
    create_user varchar(32)        not null default '' comment 'created user id',
    update_user varchar(32)        not null default '' comment 'updated user id',
    deleted     tinyint(1)         not null default '0' comment 'logic delete flag, 0-exist, 1-deleted'
);