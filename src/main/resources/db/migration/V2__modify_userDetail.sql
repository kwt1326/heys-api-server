alter table user_detail add column birth_date date not null;
alter table user_detail add column user_personality varchar(255);
alter table user_detail drop column profile_picture_uri;
alter table user_detail drop column age;

create table user_profile_link (
    id  bigserial not null,
    link_url varchar(255) not null,
    user_detail_id int8 not null,
    primary key (id)
);

alter table user_profile_link
    add constraint FKnidcev0rxqlynnlep5oi9kpct
    foreign key (user_detail_id)
    references user_detail;