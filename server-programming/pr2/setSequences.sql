SELECT setval(pg_get_serial_sequence('users', 'id'), coalesce(max(id),0) + 1, false) FROM users;
SELECT setval(pg_get_serial_sequence('threads', 'id'), coalesce(max(id),0) + 1, false) FROM threads;
SELECT setval(pg_get_serial_sequence('posts', 'id'), coalesce(max(id),0) + 1, false) FROM posts;