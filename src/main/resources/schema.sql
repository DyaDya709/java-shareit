drop table if exists bookings;
drop table if exists comments;
drop table if exists items;
drop table if exists users;

create table if not exists users
(
    id    bigint generated by default as identity
        primary key,
    email varchar(100)
        unique,
    name  varchar(100)
);

alter table users
    owner to root;

create table if not exists items
(
    id          bigint generated by default as identity
        primary key,
    user_id     bigint
        constraint items_users_id_fk
            references users,
    name        varchar(100),
    description varchar(255),
    available   boolean default true
);

alter table items
    owner to root;

create index items_user_id_index
    on items (user_id);

create index items_description_index
    on items (description);

create index items_name_index
    on items (name);

create table if not exists bookings
(
    id            bigint generated by default as identity
        primary key,
    item_id       bigint
        constraint bookings_items_id_fk
            references items,
    user_id       bigint
        constraint bookings_users_id_fk
            references users,
    start_date    timestamp,
    end_date      timestamp,
    status        varchar(20)
);

alter table bookings
    owner to root;

create index bookings_user_id_index
    on bookings (user_id);

create index bookings_status_index
    on bookings (status);

create index bookings_start_date_index
    on bookings (start_date);

create index bookings_end_date_index
    on bookings (end_date);

create index bookings_item_id_index
    on bookings (item_id);

create table if not exists comments
(
    id      bigint generated by default as identity
        primary key,
    user_id bigint
        constraint comments_users_id_fk
            references users,
    text    varchar(500),
    item_id bigint
        constraint comments_items_id_fk
            references items
);

alter table comments
    owner to root;





