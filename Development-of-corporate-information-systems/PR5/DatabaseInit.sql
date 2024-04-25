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