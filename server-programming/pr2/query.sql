select count(distinct threads.id) as threads_count,
count(distinct posts.id) as posts_count,
count(likes.user_id) as likes_count
from threads
left join posts on threads.id = posts.thread_id
left join likes on posts.id = likes.post_id
where threads.created_at >= '2024-01-03' and
threads.created_at < '2024-01-04'