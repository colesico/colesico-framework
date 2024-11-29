create table if not exists users(
  id numeric,
  person varchar(16),
 );
delete from users;
insert into users values( 1, 'Ivan',  );