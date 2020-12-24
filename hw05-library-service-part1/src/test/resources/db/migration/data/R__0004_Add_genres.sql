insert into genres(id, name)
values (1, 'genre1'),
       (2, 'genre2');

update books set genre_id=2 where id=1;
update books set genre_id=1 where id=2
