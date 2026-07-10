create table if not exists a_values(
  akey numeric,
  avalue  varchar(32)
);
delete from a_values;
insert into a_values values( 1, 'a-value' );