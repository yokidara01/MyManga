# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table task (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  done                      integer,
  due_date                  datetime,
  constraint pk_task primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table task;

SET FOREIGN_KEY_CHECKS=1;

