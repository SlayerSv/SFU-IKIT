drop table if exists users cascade;
drop table if exists friends cascade;
drop table if exists threads cascade;
drop table if exists posts cascade;
drop table if exists likes cascade;

create table users (
    id serial,
    name varchar(20) not null,
    created_at timestamp not null
);

create table friends (
    user1_id int not null,
    user2_id int not null,
    created_at timestamp not null
);

create table threads (
    id serial,
    user_id int not null,
    title varchar(20) not null,
    created_at timestamp not null
);

create table posts (
    id serial,
    user_id int not null,
    thread_id int not null,
    body varchar(40),
    created_at timestamp not null
);

create table likes (
    post_id int not null,
    user_id int not null,
    created_at timestamp not null
);

copy users
from 'C:\Users\Public\documents\users.csv'
delimiter ',';

copy friends
from 'C:\Users\Public\documents\friends.csv'
delimiter ',';

copy threads
from 'C:\Users\Public\documents\threads.csv'
delimiter ',';

copy posts
from 'C:\Users\Public\documents\posts.csv'
delimiter ',';

copy likes
from 'C:\Users\Public\documents\likes.csv'
delimiter ',';

SELECT setval(pg_get_serial_sequence('users', 'id'), coalesce(max(id),0) + 1, false) FROM users;
SELECT setval(pg_get_serial_sequence('threads', 'id'), coalesce(max(id),0) + 1, false) FROM threads;
SELECT setval(pg_get_serial_sequence('posts', 'id'), coalesce(max(id),0) + 1, false) FROM posts;

alter table users
add primary key(id);

alter table threads
add primary key(id);

alter table posts
add primary key(id);

alter table friends
add constraint friends_user1_id_fkey
foreign key(user1_id)
references users(id)
on delete cascade;

alter table friends
add constraint friends_user2_id_fkey
foreign key(user2_id)
references users(id)
on delete cascade;

alter table threads
add constraint threads_user_id_fkey
foreign key(user_id)
references users(id)
on delete cascade;

alter table posts
add constraint posts_user_id_fkey
foreign key(user_id)
references users(id)
on delete cascade;

alter table posts
add constraint posts_thread_id_fkey
foreign key(thread_id)
references threads(id)
on delete cascade;

alter table likes
add constraint likes_user_id_fkey
foreign key(user_id)
references users(id)
on delete cascade;

alter table likes
add constraint likes_post_id_fkey
foreign key(post_id)
references posts(id)
on delete cascade;
