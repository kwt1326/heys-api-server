drop table if exists authentication cascade
drop table if exists channel_user_relations cascade
drop table if exists channel_users cascade
drop table if exists channel_view cascade
drop table if exists channels cascade
drop table if exists chat cascade
drop table if exists chat_notice cascade
drop table if exists content_detail cascade
drop table if exists content_view cascade
drop table if exists contents cascade
drop table if exists interest cascade
drop table if exists interest_relations cascade
drop table if exists message_reaction cascade
drop table if exists message_reaction_relations cascade
drop table if exists notification cascade
drop table if exists user_detail cascade
drop table if exists users cascade

create table authentication (
       id  bigserial not null,
        role varchar(20) not null,
        user_id int8,
        primary key (id)
    )
create table channel_user_relations (
       id  bigserial not null,
        channel_user_id int8,
        joined_channel_id int8,
        waiting_channel_id int8,
        primary key (id)
    )
create table channel_users (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        approve_request_at timestamp,
        exit_message varchar(255),
        refuse_message varchar(255),
        status varchar(255),
        user_id int8,
        primary key (id)
    )
create table channel_view (
       id  bigserial not null,
        count int8,
        channel_id int8,
        primary key (id)
    )
create table channels (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        content_id int8,
        leader_user_id int8,
        primary key (id)
    )
create table chat (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        message varchar(255) not null,
        channel_id int8,
        user_id int8,
        primary key (id)
    )
create table chat_notice (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        content varchar(255) not null,
        title varchar(255) not null,
        primary key (id)
    )
create table content_detail (
       contents_id int8 not null,
        content_text oid not null,
        last_recruit_date timestamp not null,
        limit_people int4,
        location varchar(255) not null,
        name varchar(255) not null,
        online int4 not null,
        purpose varchar(255) not null,
        recruit_method varchar(255) not null,
        primary key (contents_id)
    )
create table content_view (
       id  bigserial not null,
        count int8,
        content_id int8,
        primary key (id)
    )
create table contents (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        content_type varchar(255) not null,
        primary key (id)
    )
create table interest (
       id  bigserial not null,
        name varchar(255),
        primary key (id)
    )
create table interest_relations (
       id  bigserial not null,
        channel_id int8,
        content_detail_id int8,
        interest_id int8,
        user_detail_id int8,
        primary key (id)
    )
create table message_reaction (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        reaction int4,
        user_id int8,
        primary key (id)
    )
create table message_reaction_relations (
       id  bigserial not null,
        chat_id int8,
        chat_notice_id int8,
        reaction_id int8,
        primary key (id)
    )
create table notification (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        content varchar(255),
        title varchar(255),
        user_id int8,
        primary key (id)
    )
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
    )
create table users (
       id  bigserial not null,
        created_at timestamp,
        removed_at timestamp,
        updated_at timestamp,
        is_available boolean,
        password varchar(255),
        phone varchar(15),
        reason_for_withdrawal varchar(255),
        user_id int8,
        primary key (id)
    )
alter table contents
       add constraint UK_9nmres2gx0eifs7ky57y8yn5i unique (content_type)
alter table interest
       add constraint UK_n9khibjfvt2sanahgb13qslgd unique (name)
alter table authentication
       add constraint FKkdfm8h65x6tk3a0xdhxy4blve
       foreign key (user_id)
       references users
alter table channel_user_relations
       add constraint FKcvq8el9nhdmk37oqho3thh8vl
       foreign key (channel_user_id)
       references channel_users
alter table channel_user_relations
       add constraint FK1n90f4mhw9liawow22j6g11q6
       foreign key (joined_channel_id)
       references channels
alter table channel_user_relations
       add constraint FKjks1xfg8o3mlwpq0nakx1qw90
       foreign key (waiting_channel_id)
       references channels
alter table channel_users
       add constraint FKd8th7utgty0qlood9tm8hdu
       foreign key (user_id)
       references users
alter table channel_view
       add constraint FK8mstc5arypj30qao75d862j1u
       foreign key (channel_id)
       references channels
alter table channels
       add constraint FKei9ht6c7ayf1fdpp2f3rvwep1
       foreign key (content_id)
       references contents
alter table channels
       add constraint FKtqm3jdqqhs0ymp4w47c4bsxsb
       foreign key (leader_user_id)
       references users
alter table chat
       add constraint FKclpeyg3t0f7x4mftfriv56yjn
       foreign key (channel_id)
       references channels
alter table chat
       add constraint FK1x766u663l7m0mxuj0o72muu
       foreign key (user_id)
       references users
alter table content_detail
       add constraint FK5uhvb46t5wkxvi2b0tqooq0b9
       foreign key (contents_id)
       references contents
alter table content_view
       add constraint FKm4g3rhrdo24wuefy4x4r88yb2
       foreign key (content_id)
       references contents
alter table interest_relations
       add constraint FKo9b570lgvv22opvyd4rbriaxt
       foreign key (channel_id)
       references channels
alter table interest_relations
       add constraint FKa0yvq6l0hr3jxl59fub5hcoo0
       foreign key (content_detail_id)
       references content_detail
alter table interest_relations
       add constraint FKggqkgp0xibjmno96mmq007jlb
       foreign key (interest_id)
       references interest
alter table interest_relations
       add constraint FKr5ttutm0om7yfiyifbg33jb35
       foreign key (user_detail_id)
       references user_detail
alter table message_reaction
       add constraint FKe3ngmocd08j6nfeo1bxdtp8bh
       foreign key (user_id)
       references users
alter table message_reaction_relations
       add constraint FKso15ga4xbb6autfkb1hkocm5j
       foreign key (chat_id)
       references chat
alter table message_reaction_relations
       add constraint FK3pwkn70a1srb7l32agbn8v492
       foreign key (chat_notice_id)
       references chat_notice
alter table message_reaction_relations
       add constraint FKiy642qutjo4ys49sektk2ed24
       foreign key (reaction_id)
       references message_reaction
alter table notification
       add constraint FKnk4ftb5am9ubmkv1661h15ds9
       foreign key (user_id)
       references users
alter table user_detail
       add constraint FKr6i0t96qgu9l8l5nn2vqo8rcl
       foreign key (user_id)
       references users
alter table users
       add constraint FKjogqshccrcue0m1009j4pquln
       foreign key (user_id)
       references content_view