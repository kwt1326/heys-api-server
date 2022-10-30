ALTER TABLE content_detail DROP COLUMN recruit_period;
ALTER TABLE content_detail ADD COLUMN last_recruit_date timestamp not null;