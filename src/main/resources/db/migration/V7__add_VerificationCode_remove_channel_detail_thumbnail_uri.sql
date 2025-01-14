drop table if exists verification_code CASCADE;

create table verification_code (
    id bigint generated by default as identity,
    code varchar(10) not null,
    expired_time timestamp,
    phone varchar(50) not null,
    primary key (id)
);

alter table channel_detail drop column if exists thumbnail_uri;
