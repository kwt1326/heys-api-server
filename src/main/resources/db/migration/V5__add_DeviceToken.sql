create table device_token (
    id  bigserial not null,
    arn varchar(200),
    token varchar(200) not null,
    user_id int8,
    primary key (id)
);

alter table device_token
   add constraint FKdklq4fbedbwx14v2varmsjeb5
   foreign key (user_id)
   references users;