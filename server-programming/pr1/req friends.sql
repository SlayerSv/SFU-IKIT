select users.name, us.name as friend from users
left join friends on user1_id = id
left join users as us on user2_id = us.id
order by name asc