--date: 2020-12-22
--author: andrey

create table genres (
    id bigint not null auto_increment,
    name varchar(255),
    primary key (id)
);

alter table books
add column genre_id bigint references genres(id) ON DELETE SET NULL;
