--date: 2020-12-30
--author: andrey

create table author_book(
    author_id bigint references authors(id) on delete cascade,
    book_id bigint references books(id),
    primary key (author_id, book_id)
);

alter table books
drop column author_id;
