--date: 2020-12-30
--author: andrey

create table book_author(
    book_id bigint not null references books(id),
    author_id bigint not null references authors(id) on delete cascade,
    primary key (book_id, author_id)
);

alter table books
drop column author_id;
