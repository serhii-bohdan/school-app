drop table if exists groups cascade;

create table groups
(
    group_id integer primary key,
  group_name varchar(5) not null
);