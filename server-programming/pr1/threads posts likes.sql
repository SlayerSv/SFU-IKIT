select threads.id as thread_id, users.name as author, posts.id as post_id,
posters.name as posted_by, likers.name as liked_by from threads
join users on threads.user_id = users.id
left join posts on thread_id = threads.id
left join users as posters on posters.id = posts.user_id
left join likes on likes.post_id = posts.id
left join users as likers on likers.id = likes.user_id
order by threads.id asc