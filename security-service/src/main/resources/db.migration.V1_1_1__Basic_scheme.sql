create schema if not exists users;

create table users.t_users (
    c_id BIGINT primary key,
    c_username varchar(255) unique,
    c_password varchar(255),
    c_email varchar(255) unique,
    c_role varchar(50)
);