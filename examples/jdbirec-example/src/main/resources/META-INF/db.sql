create table if not exists users(
    id numeric,
    name varchar(16), 
    h_phone varchar(16),
    h_email varchar(16), 
    h_address varchar(16), 
    w_phone varchar(16),
    w_email varchar(16),
    w_address varchar(16), 
    extra_email varchar(16), 
    extra_address varchar(16)
 );
delete from users;
insert into users values( 1, 'Ivan','', '','','','','w-address','extra@email','extra-address');