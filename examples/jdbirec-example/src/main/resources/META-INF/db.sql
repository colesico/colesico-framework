create table if not exists users(
  id numeric

  person varchar(16),

  home_phn  varchar(32),
  home_addr varchar(32),

  wrk_phone  varchar(32),
 );
delete from users;
insert into users values( 1, 'Ivan', '+5555555','Moscow, Arbat st. 33', '+9999999' );