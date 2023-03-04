drop table if exists authentication cascade;
drop table if exists channel_book_mark cascade;
drop table if exists channel_detail cascade;
drop table if exists channel_link cascade;
drop table if exists channel_user_relations cascade;
drop table if exists channel_view cascade;
drop table if exists channels cascade;
drop table if exists content_book_mark cascade;
drop table if exists content_view cascade;
drop table if exists contents cascade;
drop table if exists extra_content_detail cascade;
drop table if exists interest cascade;
drop table if exists interest_relations cascade;
drop table if exists notification cascade;
drop table if exists user_detail cascade;
drop table if exists users cascade;

create table authentication (
   id  bigserial not null,
    role varchar(20) not null,
    user_id int8,
    primary key (id)
);

create table channel_book_mark (
   id  bigserial not null,
    channel_id int8,
    user_id int8,
    primary key (id)
);

create table channel_detail (
   channel_id int8 not null,
    content_text TEXT not null,
    last_recruit_date timestamp not null,
    limit_people int4,
    location varchar(255) not null,
    name varchar(255) not null,
    online int4 not null,
    purpose varchar(255) not null,
    recruit_method varchar(255) not null,
    recruit_text TEXT not null,
    thumbnail_uri TEXT,
    primary key (channel_id)
);

create table channel_link (
   id  bigserial not null,
    link_url TEXT not null,
    channel_detail_id int8 not null,
    primary key (id)
);

create table channel_user_relations (
   id  bigserial not null,
    created_at timestamp,
    removed_at timestamp,
    updated_at timestamp,
    approve_request_at timestamp,
    exit_message varchar(255),
    refuse_message varchar(255),
    status varchar(255),
    channel_id int8,
    user_id int8,
    primary key (id)
);

create table channel_view (
   id  bigserial not null,
    channel_id int8,
    user_id int8,
    primary key (id)
);

create table channels (
   id  bigserial not null,
    created_at timestamp,
    removed_at timestamp,
    updated_at timestamp,
    active_notify boolean,
    channel_type varchar(255) not null,
    content_id int8,
    leader_user_id int8,
    primary key (id)
);

create table content_book_mark (
   id  bigserial not null,
    content_id int8,
    user_id int8,
    primary key (id)
);

create table content_view (
   id  bigserial not null,
    content_id int8,
    user_id int8,
    primary key (id)
);

create table contents (
   id  bigserial not null,
    created_at timestamp,
    removed_at timestamp,
    updated_at timestamp,
    content_type varchar(255) not null,
    primary key (id)
);

create table extra_content_detail (
   contents_id int8 not null,
    benefit varchar(255) not null,
    company varchar(255) not null,
    contact varchar(255) not null,
    content_text TEXT not null,
    end_date timestamp not null,
    link_uri TEXT,
    preview_img_uri TEXT,
    start_date timestamp not null,
    target varchar(255) not null,
    thumbnail_uri TEXT,
    name varchar(255) not null,
    primary key (contents_id)
);

create table interest (
   id  bigserial not null,
    name varchar(255),
    primary key (id)
);

create table interest_relations (
   id  bigserial not null,
    channel_detail_id int8,
    extra_content_detail_id int8,
    interest_id int8,
    user_detail_id int8,
    primary key (id)
);

create table notification (
   id  bigserial not null,
    created_at timestamp,
    removed_at timestamp,
    updated_at timestamp,
    content varchar(255),
    title varchar(255),
    user_id int8,
    primary key (id)
);

create table user_detail (
   user_id int8 not null,
    age int4 not null,
    capability varchar(255),
    gender int4 not null,
    introduce_text varchar(255),
    job varchar(50),
    profile_picture_uri varchar(255),
    username varchar(50) not null,
    primary key (user_id)
);

create table users (
   id  bigserial not null,
    created_at timestamp,
    removed_at timestamp,
    updated_at timestamp,
    is_available boolean,
    password varchar(255),
    phone varchar(15),
    reason_for_withdrawal varchar(255),
    primary key (id)
);

alter table interest 
   add constraint UK_n9khibjfvt2sanahgb13qslgd unique (name);

alter table authentication 
   add constraint FKkdfm8h65x6tk3a0xdhxy4blve 
   foreign key (user_id) 
   references users;

alter table channel_book_mark 
   add constraint FKf8f2jfi1rcfhhh4288em3hhu2 
   foreign key (channel_id) 
   references channels;

alter table channel_book_mark 
   add constraint FKqf61vax2y728lf9t5mj6b2tse 
   foreign key (user_id) 
   references users;

alter table channel_detail 
   add constraint FKjnajjtj5psmmc9wjb5i4o404b 
   foreign key (channel_id) 
   references channels;

alter table channel_link 
   add constraint FKhkt07mbmuclkj8rccvypuxpfn 
   foreign key (channel_detail_id) 
   references channel_detail;

alter table channel_user_relations 
   add constraint FKfddbo54amnm762pqdau5yvb4j 
   foreign key (channel_id) 
   references channels;

alter table channel_user_relations 
   add constraint FKogc3t1kjj8o3vpoq4mlg3k1vb 
   foreign key (user_id) 
   references users;

alter table channel_view 
   add constraint FK8mstc5arypj30qao75d862j1u 
   foreign key (channel_id) 
   references channels;

alter table channel_view 
   add constraint FK2n408mx2513lo0nau93prsme5 
   foreign key (user_id) 
   references users;

alter table channels 
   add constraint FKei9ht6c7ayf1fdpp2f3rvwep1 
   foreign key (content_id) 
   references contents;

alter table channels 
   add constraint FKtqm3jdqqhs0ymp4w47c4bsxsb 
   foreign key (leader_user_id) 
   references users;

alter table content_book_mark 
   add constraint FKq70vq6mxsayabh4eg5bc1ko8n 
   foreign key (content_id) 
   references contents;

alter table content_book_mark 
   add constraint FKmk2554nfj5y173brtmsnnp3fp 
   foreign key (user_id) 
   references users;

alter table content_view 
   add constraint FKm4g3rhrdo24wuefy4x4r88yb2 
   foreign key (content_id) 
   references contents;

alter table content_view 
   add constraint FK4g37b40rdc37v04787j64c0e9 
   foreign key (user_id) 
   references users;

alter table extra_content_detail 
   add constraint FKsttg49lv5089lild7h9n39ira 
   foreign key (contents_id) 
   references contents;

alter table interest_relations 
   add constraint FKqvvsbs4su64v44ftb1mfrybf0 
   foreign key (channel_detail_id) 
   references channel_detail;

alter table interest_relations 
   add constraint FKd5c0kv9og24wqoww0qvysuee4 
   foreign key (extra_content_detail_id) 
   references extra_content_detail;

alter table interest_relations 
   add constraint FKggqkgp0xibjmno96mmq007jlb 
   foreign key (interest_id) 
   references interest;

alter table interest_relations 
   add constraint FKr5ttutm0om7yfiyifbg33jb35 
   foreign key (user_detail_id) 
   references user_detail;

alter table notification 
   add constraint FKnk4ftb5am9ubmkv1661h15ds9 
   foreign key (user_id) 
   references users;

alter table user_detail 
   add constraint FKr6i0t96qgu9l8l5nn2vqo8rcl 
   foreign key (user_id) 
   references users;