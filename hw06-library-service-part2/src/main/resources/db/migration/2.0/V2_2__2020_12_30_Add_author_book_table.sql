--date: 2020-12-30
--author: andrey

create table book_author(
    fk_book bigint references books(id),
    fk_author bigint references authors(id) on delete cascade,
    primary key (fk_book, fk_author)
);

alter table books
drop column author_id;
