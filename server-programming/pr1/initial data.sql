insert into users (name, created_at)
values
('Anna Zhukova', timestamp '2024-12-01 04:05:06'),
('Pavel Volkov', timestamp '2024-12-02 12:31:02'),
('Sergey Nosikov', timestamp '2024-12-03 13:04:10'),
('Alena Novikova', timestamp '2024-12-04 23:59:01'),
('Nikolay Zubarev', timestamp '2024-12-05 18:33:16'),
('Svetlana Ivanova', timestamp '2024-12-06 11:42:27'),
('Ivan Udalcov', timestamp '2024-12-07 14:22:59'),
('Maria Dubina', timestamp '2024-12-08 15:14:09'),
('Matvey Smirnov', timestamp '2024-12-09 21:35:56'),
('Olga Malceva', timestamp '2024-12-10 14:09:01')
;

insert into friends
values
(1, 2, timestamp '2024-12-13 04:05:06'),
(2, 1, timestamp '2024-12-13 04:05:06'),
(1, 4, timestamp '2024-12-03 12:31:02'),
(4, 1, timestamp '2024-12-03 12:31:02'),
(2, 10, timestamp '2024-12-13 13:04:10'),
(10, 2, timestamp '2024-12-13 13:04:10'),
(1, 5, timestamp '2024-12-07 23:59:01'),
(5, 1, timestamp '2024-12-07 23:59:01'),
(5, 6, timestamp '2024-12-09 11:42:27'),
(6, 5, timestamp '2024-12-09 11:42:27'),
(5, 7, timestamp '2024-12-19 14:22:59'),
(7, 5, timestamp '2024-12-19 14:22:59'),
(9, 10, timestamp '2024-12-20 15:14:09'),
(10, 9, timestamp '2024-12-20 15:14:09'),
(1, 7, timestamp '2024-12-22 14:09:01'),
(7, 1, timestamp '2024-12-22 14:09:01')
;

insert into threads (user_id, created_at)
values
(1, timestamp '2024-12-01 05:05:06'),
(3, timestamp '2024-12-04 12:31:02'),
(2, timestamp '2024-12-02 14:04:10'),
(1, timestamp '2024-12-02 23:59:01'),
(2, timestamp '2024-12-11 18:33:16'),
(5, timestamp '2024-12-07 11:42:27'),
(5, timestamp '2024-12-08 14:22:59'),
(9, timestamp '2024-12-12 15:14:09'),
(2, timestamp '2024-12-04 21:35:56'),
(1, timestamp '2024-12-13 14:09:01')
;

insert into posts (user_id, thread_id, created_at)
values
(1, 2, timestamp '2024-12-01 14:05:06'),
(1, 4, timestamp '2024-12-14 12:31:02'),
(2, 10, timestamp '2024-12-15 13:04:10'),
(4, 5, timestamp '2024-12-16 23:59:01'),
(3, 8, timestamp '2024-12-17 18:33:16'),
(7, 6, timestamp '2024-12-18 11:42:27'),
(5, 7, timestamp '2024-12-19 14:22:59'),
(9, 10, timestamp '2024-12-20 15:14:09'),
(2, 3, timestamp '2024-12-21 21:35:56'),
(1, 7, timestamp '2024-12-22 14:09:01')
;

insert into likes
values
(1, 2, timestamp '2024-12-13 04:05:06'),
(1, 4, timestamp '2024-12-14 12:31:02'),
(2, 10, timestamp '2024-12-15 13:04:10'),
(1, 5, timestamp '2024-12-16 23:59:01'),
(2, 8, timestamp '2024-12-17 18:33:16'),
(5, 6, timestamp '2024-12-18 11:42:27'),
(5, 7, timestamp '2024-12-19 14:22:59'),
(9, 10, timestamp '2024-12-20 15:14:09'),
(2, 3, timestamp '2024-12-21 21:35:56'),
(1, 7, timestamp '2024-12-22 14:09:01')
;