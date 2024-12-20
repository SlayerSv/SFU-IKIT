drop table if exists users cascade;
drop table if exists friends cascade;
drop table if exists threads cascade;
drop table if exists posts cascade;
drop table if exists likes cascade;

create table users (
    id serial primary key,
    name varchar(20) not null,
    created_at timestamp not null
);

create table friends (
    user1_id int not null,
    user2_id int not null,
    created_at timestamp not null,
    foreign key (user1_id)
        references users(id)
        on delete cascade,
    foreign key (user2_id)
        references users(id)
        on delete cascade
);

create table threads (
    id serial primary key,
    user_id int not null,
    created_at timestamp not null,
    foreign key (user_id)
        references users(id)
        on delete cascade
);

create table posts (
    id serial primary key,
    user_id int not null,
    thread_id int not null,
    created_at timestamp not null,
    foreign key (user_id)
        references users(id)
        on delete cascade,
    foreign key (thread_id)
        references threads(id)
        on delete cascade
);

create table likes (
    post_id int not null,
    user_id int not null,
    created_at timestamp not null,
    foreign key (post_id)
        references posts(id)
        on delete cascade,
    foreign key (user_id)
        references users(id)
        on delete cascade
);