drop table if exists users cascade;
drop table if exists friends cascade;
drop table if exists threads cascade;
drop table if exists posts cascade;
drop table if exists likes cascade;

create table users (
    id serial primary key,
    name varchar(20),
    created_at timestamp
);

create table friends (
    user1_id int,
    user2_id int,
    created_at timestamp,
    foreign key (user1_id)
        references users(id)
        on delete cascade,
    foreign key (user2_id)
        references users(id)
        on delete cascade
);

create table threads (
    id serial primary key,
    user_id int,
    created_at timestamp,
    foreign key (user_id)
        references users(id)
        on delete cascade
);

create table posts (
    id serial primary key,
    user_id int,
    thread_id int,
    created_at timestamp,
    foreign key (user_id)
        references users(id)
        on delete cascade,
    foreign key (thread_id)
        references threads(id)
        on delete cascade
);

create table likes (
    post_id int,
    user_id int,
    created_at timestamp,
    foreign key (post_id)
        references posts(id)
        on delete cascade,
    foreign key (user_id)
        references users(id)
        on delete cascade
);