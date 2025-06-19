drop table if exists users cascade;

create table users (
    id serial primary key,
    name text not null unique,
    password text not null,
    created_at timestamp with time zone default current_timestamp
);