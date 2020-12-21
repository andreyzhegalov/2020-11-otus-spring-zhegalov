alter table books
add column author_id bigint references authors(id) ON DELETE SET NULL;
