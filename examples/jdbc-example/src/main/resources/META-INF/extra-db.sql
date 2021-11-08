create table if not exists avalues(
  akey numeric,
  avalue  varchar(32)
);
delete from avalues;
insert into avalues values( 2, 'b-value' );