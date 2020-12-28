--date: 2020-12-28
--author: andrey

create table comments (
    id bigint not null auto_increment,
    text varchar(255),
    book_id bigint references books(id) ON DELETE SET NULL,
    primary key (id)
);
