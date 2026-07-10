create table if not exists b_values(
  bkey numeric,
  bvalue  varchar(32)
);
delete from b_values;
insert into b_values values( 2, 'b-value' );