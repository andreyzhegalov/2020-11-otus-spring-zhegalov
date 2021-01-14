--date: 2020-12-28
--author: andrey

create table comments (
    id bigint not null auto_increment,
    text varchar(255),
    book_id bigint not null,

    primary key (id),

    constraint books_book_id_fk
       foreign key (book_id)
       references books (id)
       on delete cascade
)
