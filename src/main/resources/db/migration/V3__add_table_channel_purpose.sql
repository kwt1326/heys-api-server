create table channel_purpose (
       id  bigserial not null,
       purpose varchar(255) not null,
       channel_detail_id int8 not null,
       primary key (id)
);

alter table channel_purpose
       add constraint FKggwx3dfi88kh5yjwdq3mgn1fg
       foreign key (channel_detail_id)
       references channel_detail;