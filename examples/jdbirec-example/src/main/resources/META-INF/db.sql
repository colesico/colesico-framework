create table if not exists v_users(
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
delete from v_users;
insert into v_users values( 1, 'Ivan','', '','','','','w-address','extra@email','extra-address');

create table if not exists r_users(
    user_id numeric,
    user_name varchar(16),
    h_mobile_number varchar(16),
    h_post_addr varchar(16),
    w_phone varchar(16),
    w_location varchar(16)
);
delete from r_users;
insert into r_users values( 1, 'Ivan','', 'Moscow 1','+7888999000','Russia');
