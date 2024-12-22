select count(distinct threads.id) as threads_count,
count(distinct posts.id) as posts_count,
count(likes.user_id) as likes_count
from threads
join posts on threads.id = posts.thread_id
join users on posts.user_id = users.id
join likes on posts.id = likes.post_id
where threads.created_at >= '2024-01-01' and
threads.created_at < '2024-08-01'
and users.name like 'Maria%'
and likes.user_id < 20