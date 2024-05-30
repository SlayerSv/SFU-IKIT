drop table if exists products;
create table products (
    id serial primary key,
    item varchar(30) not null,
    type varchar(30) not null,
    producer varchar(30) not null,
    price numeric(9, 2) not null,
    check(price >= 0),
    weight int not null,
    check(weight > 0)
);

insert into products (item, type, producer, price, weight)
values
    ('Bread', 'White wheat bread', 'Hot bakery', 40, 700),
    ('Apples', 'Fuji', 'Apple farm', 200, 1000),
    ('Milk', 'Whole cow milk', 'Milking world', 80, 1000),
    ('Oranges', 'Mandarin', 'Orange plantation', 310, 900),
    ('Tomatoes', 'Cherry', 'Tomato happiness', 230, 800),
    ('Cake', 'Napoleon', 'Wonderful cakes', 900, 1500),
    ('Carrots', 'Imperator', 'Fruts and Veggies', 150, 700),
    ('Bananas', 'Cavendish', 'Sunny Tropico', 230, 950);

create index price on products(price);

drop table if exists users;
create table users (
    name varchar(20) primary key,
    password varchar(60) not null,
    role varchar(10) not null,
    check(role in ('admin', 'user'))
);

insert into users
values
    ('admin', '$2a$10$4/SAuC6UzsGbuce.Q0pXXeuYMhqMznsJaKCL6yXhuKwCbf/WGeRY2', 'admin'),
    ('user', '$2a$10$fW6qFLbiG4cccqvx/QTQCe0aJu9oOpFVen6YGw8gyOCeK4PbSL3kC', 'user');