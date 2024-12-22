delete from users;
delete from friends;
delete from threads;
delete from posts;
delete from likes;

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
